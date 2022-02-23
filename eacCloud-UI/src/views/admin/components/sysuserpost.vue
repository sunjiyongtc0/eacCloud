<template>
  <div class="app-container">
    <div class="filter-container">
      <span class="allQueryClass">
        <el-input v-model="listQuery.postName" :placeholder="$t('sysuserpost.postName')" clearable class="filter-input filter-item" style="width: 120px" @keyup.enter.native="handleFilter" />
      </span>
      <div style="float: right">
        <el-button v-waves type="primary" class="filter-item" icon="el-icon-search" @click="handleFilter">
          {{ $t('common.query') }}
        </el-button>
        <el-button v-waves type="primary" class="filter-item" style="margin-left: 5px" icon="el-icon-refresh-right" @click="handleReset">
          {{ $t('common.reset') }}
        </el-button>
        <el-button v-waves type="primary" class="filter-item" style="margin-left: 5px" icon="el-icon-search" @click="clickSwitch">
          {{ $t('common.advanced-search') }}
        </el-button>
        <el-button type="primary" class="filter-item" style="margin-left: 5px" icon="el-icon-plus" @click="handleCreate">
          {{ $t('common.add') }}
        </el-button>
        <el-button type="primary" class="filter-item" style="margin-left: 5px" icon="el-icon-edit-outline" @click="handleUpdate">
          {{ $t('common.edit') }}
        </el-button>
        <el-button type="primary" class="filter-item" style="margin-left: 5px; margin-right: 5px;" icon="el-icon-upload" @click="handleImport">
          {{ $t('common.import') }}
        </el-button>
        <el-dropdown v-waves split-button :loading="downloadLoading" type="primary" class="filter-item" style="padding-right: 5px;" trigger="click" @command="handleExport">
          <i class="el-icon-document" style="margin-right: 5px" /> {{ $t('common.export') }}
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="excel"><svg-icon icon-class="excel" style="margin-right: 5px" />{{ $t('common.export') }}Excel</el-dropdown-item>
            <el-dropdown-item command="word"><svg-icon icon-class="documentation" style="margin-right: 5px" />{{ $t('common.export') }}Word</el-dropdown-item>
            <el-dropdown-item command="pdf"><svg-icon icon-class="pdf" style="margin-right: 5px" />{{ $t('common.export') }}PDF</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-button type="primary" class="filter-item" icon="el-icon-printer" @click="handlePrint">
          {{ $t('common.print') }}
        </el-button>
        <el-button type="danger" plain class="filter-item" style="margin-left: 5px;" icon="el-icon-delete" @click="handleDelete">
          {{ $t('common.delete') }}
        </el-button>
      </div>
      <div v-if="advancedSearch" style="float: left">
        <el-input v-model="listQuery.postCode" :placeholder="$t('syspost.postCode')" clearable class="filter-input filter-item" style="width: 150px" @keyup.enter.native="handleFilter" />
        <el-select v-model="listQuery.postType" :placeholder="$t('sysuserpost.postType')" clearable class="filter-input filter-item" style="width: 150px">
          <el-option :label="$t('sysuserpost.mainPost')" value="1" />
          <el-option :label="$t('sysuserpost.parttimePost')" value="2" />
        </el-select>
        <el-select v-model="listQuery.status" :placeholder="$t('sysuser.status')" clearable class="filter-input filter-item" style="width: 150px">
          <el-option :label="$t('common.show')" value="1" />
          <el-option :label="$t('common.hide')" value="0" />
        </el-select>
        <el-input v-model="listQuery.postDescription" :placeholder="$t('sysuserpost.postDescription')" clearable class="filter-input filter-item" style="width: 150px" @keyup.enter.native="handleFilter" />
      </div>
    </div>
    <div>
      <el-table
        :key="tableKey"
        ref="multipleTable"
        :data="list"
        border
        fit
        highlight-current-row
        :row-style="{height:'30px'}"
        :cell-style="{padding:'0px'}"
        :header-cell-style="{padding:'0px'}"
        style="width: 100%"
        @sort-change="sortChange"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="40" />
        <el-table-column fixed :label="$t('sysuserpost.postName')" prop="postName" min-width="320px" show-overflow-tooltip />
        <el-table-column :label="$t('syspost.postCode')" prop="postCode" min-width="140px" />
        <el-table-column :label="$t('sysuserpost.postType')" class-name="status-col">
          <template slot-scope="{row}">
            <span v-if="row.postType === 1">{{ $t('sysuserpost.mainPost') }}</span>
            <span v-else>{{ $t('sysuserpost.parttimePost') }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('sysuser.status')" class-name="status-col">
          <template slot-scope="{row}">
            <el-tag :type="row.status | statusFilter">
              <span v-if="row.status === 1">{{ $t('common.show') }}</span>
              <span v-else>{{ $t('common.hide') }}</span>
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('sysuserpost.postDescription')" min-width="320px">
          <template slot-scope="{row}">
            <template v-if="row.edit">
              <el-input v-model="row.postDescription" class="edit-input" size="small" />
              <el-button
                class="cancel-btn"
                style="height: 28px; padding: 0 10px"
                size="small"
                icon="el-icon-refresh"
                type="warning"
                @click="cancelEdit(row)"
              >
                {{ $t('common.cancel') }}
              </el-button>
            </template>
            <span v-else>{{ row.postDescription }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.create-time')" prop="createTime" min-width="160px" sortable="custom" :class-name="getSortClass('createTime')">
          <template slot-scope="{row}">
            <span>{{ row.createTime | formatDate }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('common.operate')" min-width="150px">
          <template slot-scope="{row,$index}">
            <el-button
              v-if="row.edit"
              type="success"
              style="height: 28px; padding: 0 10px"
              size="small"
              icon="el-icon-check"
              @click="updateData(row)"
            >
              {{ $t('common.confirm') }}
            </el-button>
            <el-button
              v-else
              type="text"
              style="height: 28px; padding: 0 10px"
              size="small"
              @click="row.edit=!row.edit"
            >
              <i class="el-icon-edit" />{{ $t('common.edit') }}
            </el-button>
            <el-popover :ref="'popover-' + row.id" placement="top" width="160" :title="$t('common.confirm-delete')" trigger="click">
              <div>
                <el-button size="mini" @click="$refs[`popover-` + row.id].doClose()">{{ $t('common.cancel') }}</el-button>
                <el-button type="primary" size="mini" @click="$refs[`popover-` + row.id].doClose();handleDelete(row,$index)">{{ $t('common.confirm') }}</el-button>
              </div>
              <el-button slot="reference" type="text">
                <i class="el-icon-delete" />{{ $t('common.delete') }}
              </el-button>
            </el-popover>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.currentPage" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-dialog v-el-drag-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" :label-position="labelPosition" label-width="120px" style="width: 100%">
        <el-form-item :label="$t('syspost.postName')" prop="postCode">
          <el-select ref="selectParentId" v-model="temp.postCode" :placeholder="$t('common.choose')" style="width: 100%" clearable @clear="handleClearParentId">
            <el-option :key="temp.postCode" hidden :value="temp.postCode" :label="parentName" />
            <el-tree ref="treeParentId" node-key="postCode" :data="parentIdOptions" :props="{label: 'title'}" :expand-on-click-node="false" :check-on-click-node="true" @node-click="handleNodeClickParentId" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('sysuserpost.postDescription')" prop="postDescription">
          <el-input v-model="temp.postDescription" type="textarea" :placeholder="$t('common.enter')" maxlength="256" clearable />
        </el-form-item>
        <el-form-item :label="$t('sysuserpost.postType')" prop="postType">
          <el-radio-group v-model="temp.postType">
            <el-radio :label="1">{{ $t('sysuserpost.mainPost') }}</el-radio>
            <el-radio :label="2">{{ $t('sysuserpost.parttimePost') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('sysuser.status')" prop="status">
          <el-radio-group v-model="temp.status">
            <el-radio :label="1">{{ $t('common.show') }}</el-radio>
            <el-radio :label="0">{{ $t('common.hide') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          {{ $t('common.cancel') }}
        </el-button>
        <el-button type="primary" :disabled="disabledStatus" @click="dialogStatus==='create'?createData():updateData()">
          {{ $t('common.confirm') }}
        </el-button>
      </div>
    </el-dialog>

    <el-dialog v-el-drag-dialog :title="$t('common.import')" :visible.sync="importDialogFormVisible">
      <p>{{ $t('common.download-template') }}: <a style="color: #1890ff" @click="handleDownload(1)">{{ this.$t('sysuserpost.postInfo') + '.xlsx' }}</a></p>
      <center>
        <el-upload ref="upload" :data="dataObj" :headers="elUploadHeaders" action="/api/admin/sysuserpost/importSysUserPost" :auto-upload="false" :on-success="handleSuccess" drag>
          <i class="el-icon-upload" />
          <div class="el-upload__text">
            {{ $t('file.drag') }}<em>{{ $t('file.clickupload') }}</em>
          </div>
        </el-upload>
      </center>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogFormVisible = false">
          {{ $t('common.cancel') }}
        </el-button>
        <el-button type="primary" @click="uploadData()">
          {{ $t('common.confirm') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { querySysUserPost, querySysPostTree, addSysUserPost, updateSysUserPost, deleteSysUserPost, exportSysUserPost, exportWordSysUserPost, exportPDFSysUserPost } from '@/api/admin'
import waves from '@/directive/waves' // waves directive
import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import qs from 'qs'
import { saveAs } from 'file-saver'
import mammoth from 'mammoth'
import { getToken } from '@/utils/auth'

export default {
  name: 'SysUserPost',
  components: { Pagination },
  directives: { waves, elDragDialog },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'danger'
      }
      return statusMap[status]
    }
  },
  props: ['msg'], // eslint-disable-line
  data() {
    return {
      tableKey: 0,
      labelPosition: 'right',
      list: [],
      total: 0,
      listQuery: {
        currentPage: 1,
        pageSize: 10,
        userId: undefined,
        postCode: undefined,
        postType: undefined,
        status: undefined,
        postDescription: undefined,
        sorter: 'createTime_descend'
      },
      temp: {
        id: undefined,
        postType: 2,
        status: 1
      },
      parentIdOptions: [],
      parentName: '',
      multipleSelection: [],
      dialogFormVisible: false,
      importDialogFormVisible: false,
      dataObj: { },
      elUploadHeaders: { 'Authorization': 'Bearer ' + getToken() },
      dialogStatus: '',
      textMap: {
        update: this.$t('common.edit'),
        create: this.$t('common.add')
      },
      rules: {
        postCode: [{ required: true, validator: this.checkPostCode, trigger: 'change' }],
        postType: [{ required: true }],
        status: [{ required: true }]
      },
      downloadLoading: false,
      disabledStatus: false,
      advancedSearch: false
    }
  },
  watch: {
    msg(oldValue, newValue) {
      this.getList()
    }
  },
  created() {
    if (window.innerWidth < 700) {
      this.labelPosition = 'top'
    }
    if (this.msg[0]) {
      this.getList()
    }
  },
  methods: {
    clickSwitch() {
      this.advancedSearch = !this.advancedSearch
    },
    getList() {
      this.listQuery.userId = this.msg && this.msg.length > 0 ? this.msg[0].id : '00000000'
      querySysUserPost(this.listQuery).then(response => {
        this.list = response.data.list.map(v => {
          this.$set(v, 'edit', false)
          v.originalTitle = v.postDescription
          return v
        })
        this.total = response.data.pagination.total
      })
      querySysPostTree().then(response => {
        this.parentIdOptions = response.data
      })
    },
    handleFilter() {
      if (!this.msg[0]) {
        this.handleWarning(this.$t('sysuserpost.sysuser-required'))
        return
      }
      this.listQuery.currentPage = 1
      this.getList()
    },
    handleReset() {
      this.listQuery.postCode = undefined
      this.listQuery.postType = undefined
      this.listQuery.status = undefined
      this.listQuery.postDescription = undefined
      this.listQuery.postName = undefined
      this.handleFilter()
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'createTime') {
        this.sortByCreateTime(order)
      }
    },
    sortByCreateTime(order) {
      if (order === 'ascending') {
        this.listQuery.sorter = 'createTime_ascend'
      } else {
        this.listQuery.sorter = 'createTime_descend'
      }
      this.handleFilter()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        postType: 2,
        status: 1
      }
    },
    cancelEdit(row) {
      row.postDescription = row.originalTitle
      row.edit = false
    },
    handleCreate() {
      if (!this.msg[0]) {
        this.handleWarning(this.$t('sysuserpost.sysuser-required'))
        return
      }
      this.resetTemp()
      this.parentName = undefined
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      if (this.$refs.selectParentId.value) {
        this.validateParentId()
      }
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.userId = this.msg[0].id
          this.disabledStatus = true
          addSysUserPost(this.temp).then(response => {
            if (response.message === 'success') {
              this.list.unshift(this.temp)
              this.disabledStatus = false
              this.dialogFormVisible = false
              this.$message({
                message: this.$t('common.add-success'),
                type: 'success',
                duration: 2000
              })
              this.handleReset()
            } else {
              this.handleWarning(response)
            }
          })
        }
      })
    },
    handleUpdate() {
      if (this.multipleSelection.length === 0) {
        this.handleWarning(this.$t('common.check-required'))
        return
      }
      if (this.multipleSelection.length > 1) {
        this.handleWarning(this.$t('common.check-one'))
        return
      }
      this.temp = Object.assign({}, this.multipleSelection[0]) // copy obj
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
        if (this.temp.postCode === 0) {
          this.parentName = this.$t('common.choose')
        } else {
          this.parentName = this.$refs.treeParentId.getNode(this.temp.postCode) ? this.$refs.treeParentId.getNode(this.temp.postCode).label : ''
        }
      })
    },
    updateData(row) {
      if (!row) {
        if (this.$refs.selectParentId.value) {
          this.validateParentId()
        }
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            // const tempData = Object.assign({}, this.temp)
            const tempData = {}
            tempData.id = this.temp.id
            tempData.userId = this.msg[0].id
            tempData.postCode = this.temp.postCode
            tempData.postDescription = this.temp.postDescription
            tempData.postType = this.temp.postType
            tempData.status = this.temp.status
            this.disabledStatus = true
            updateSysUserPost(tempData).then(response => {
              if (response.message === 'success') {
                this.disabledStatus = false
                this.dialogFormVisible = false
                this.$message({
                  message: this.$t('common.edit-success'),
                  type: 'success',
                  duration: 2000
                })
                this.handleReset()
              } else {
                this.handleWarning(response)
              }
            })
          }
        })
      } else {
        const tempData = {}
        tempData.id = row.id
        tempData.userId = this.msg[0].id
        tempData.postCode = row.postCode
        tempData.postDescription = row.postDescription
        tempData.postType = row.postType
        tempData.status = row.status
        updateSysUserPost(tempData).then(response => {
          if (response.message === 'success') {
            this.dialogFormVisible = false
            this.$message({
              message: this.$t('common.edit-success'),
              type: 'success',
              duration: 2000
            })
            this.handleReset()
          } else {
            this.handleWarning(response)
          }
        })
      }
    },
    handleDelete(row, index) {
      if (row.id) {
        this.delete(row.id)
      } else {
        if (this.multipleSelection.length === 0) {
          this.handleWarning(this.$t('common.choose-delete'))
          return
        }
        this.$confirm(this.$t('common.confirm-batch-delete'), this.$t('common.batch-delete'), {
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel'),
          type: 'warning'
        }).then(() => {
          this.delete(this.multipleSelection.map(item => { return item.id }).join(','))
        }).catch(() => {})
      }
    },
    delete(deleteArray) {
      deleteSysUserPost(deleteArray).then(response => {
        if (response.message === 'success') {
          this.$message({
            message: this.$t('common.delete-success'),
            type: 'success',
            duration: 2000
          })
          this.handleReset()
        } else {
          this.handleWarning(response)
        }
      })
    },
    handlePrint() {
      const listQueryData = this.handleCommonQuery()
      exportWordSysUserPost(qs.stringify(listQueryData)).then(response => {
        var reader = new FileReader()
        reader.readAsArrayBuffer(response)
        reader.onload = function(e) {
          const buffer = e.target.result
          mammoth.convertToHtml({ arrayBuffer: buffer }).then((result) => {
            const printer = window.open('', 'printwindow')
            var htmlToPrint = '<style type="text/css">' +
                'table {' + 'border-right: 1px solid #000;' + 'border-bottom: 1px solid #000;}' +
                'table td {' + 'border-left: 1px solid #000;' + 'border-top: 1px solid #000;}' +
                '</style>'
            printer.document.write(htmlToPrint + result.value.replace('</table><table>', ''))
            printer.print()
            printer.close()
          }).done()
        }
      })
    },
    handleImport() {
      this.importDialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.upload.clearFiles()
      })
    },
    uploadData() {
      if (this.$refs.upload.uploadFiles.length === 0) {
        this.handleWarning(this.$t('file.isnotempty'))
        return
      }
      this.$refs.upload.submit()
    },
    handleSuccess(response, file, fileList) {
      if (response.status !== 200) {
        this.handleWarning(response.message)
        this.$refs.upload.clearFiles()
      } else {
        this.$message({
          message: this.$t('common.import') + this.$t('common.success'),
          type: 'success',
          duration: 2000
        })
        this.importDialogFormVisible = false
        this.handleReset()
      }
    },
    handleExport(command) {
      if (!this.msg[0]) {
        this.handleWarning(this.$t('sysuserpost.sysuser-required'))
        return
      }
      if (command === 'excel') {
        this.handleDownload(0)
      } else if (command === 'word') {
        this.handleDownloadWord()
      } else if (command === 'pdf') {
        this.handleDownloadPDF()
      }
    },
    handleCommonQuery() { // 导出的查询条件
      const listQueryData = {}
      listQueryData.postCode = this.listQuery.postCode
      listQueryData.postType = this.listQuery.postType
      listQueryData.status = this.listQuery.status
      listQueryData.postDescription = this.listQuery.postDescription
      listQueryData.postName = this.listQuery.postName
      listQueryData.userId = this.msg && this.msg.length > 0 ? this.msg[0].id : '00000000'
      return listQueryData
    },
    handleDownload(isTemplate) { // 导出 Excel
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      listQueryData.isTemplate = isTemplate // 1为模板，0不为模板
      exportSysUserPost(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('sysuserpost.postInfo') + '.xlsx')
      })
      this.downloadLoading = false
    },
    handleDownloadWord() { // 导出 Word
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportWordSysUserPost(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('sysuserpost.postInfo') + '.docx')
      })
      this.downloadLoading = false
    },
    handleDownloadPDF() { // 导出 PDF
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportPDFSysUserPost(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('sysuserpost.postInfo') + '.pdf')
      })
      this.downloadLoading = false
    },
    getSortClass: function(key) {
      const sorter = this.listQuery.sorter
      return sorter === `${key}_ascend` ? 'ascending' : 'descending'
    },
    handleWarning(response) {
      this.$message({
        message: response.message || response,
        type: 'warning',
        duration: 2000
      })
      this.disabledStatus = false
    },
    validateParentId() {
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate('postCode')
      })
    },
    handleNodeClickParentId(data) {
      this.parentName = data.title
      this.temp.postCode = data.postCode
      this.$refs.selectParentId.blur()
      this.validateParentId()
    },
    handleClearParentId() {
      this.parentName = undefined
      this.temp.postCode = undefined
    },
    checkPostCode(rule, value, callback) {
      if (!this.$refs.selectParentId.value) {
        return callback(new Error(this.$t('sysuserpost.postName-required')))
      } else {
        callback()
      }
    }
  }
}
</script>
<style>
  .el-tabs--border-card > .el-tabs__content {
    padding: 0px;
  }
  .allQueryClass input.el-input__inner {
    padding: 10px;
  }
</style>
<style scoped>
  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
  .edit-input {
    padding-right: 100px;
  }
  .cancel-btn {
    position: absolute;
    right: 15px;
    top: 2px;
  }
  .pagination-container {
    padding: 10px;
    margin: 0px;
  }
</style>
