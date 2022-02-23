<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.postCode" :placeholder="$t('syspost.postCode')" clearable class="filter-input filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.postName" :placeholder="$t('syspost.postName')" clearable class="filter-input filter-item" @keyup.enter.native="handleFilter" />
      <div style="float: right">
        <el-button v-waves type="primary" class="filter-item" icon="el-icon-search" @click="handleFilter">
          {{ $t('common.query') }}
        </el-button>
        <el-button v-waves type="primary" class="filter-item" icon="el-icon-refresh-right" @click="handleReset">
          {{ $t('common.reset') }}
        </el-button>
        <el-button type="primary" class="filter-item" icon="el-icon-plus" @click="handleCreate">
          {{ $t('common.add') }}
        </el-button>
        <el-button type="primary" class="filter-item" style="margin-right: 10px;" icon="el-icon-upload" @click="handleImport">
          {{ $t('common.import') }}
        </el-button>
        <el-dropdown v-waves split-button :loading="downloadLoading" type="primary" class="filter-item" style="padding-right: 10px;" trigger="click" @command="handleExport">
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
        <el-button type="danger" plain class="filter-item" icon="el-icon-delete" @click="handleDelete">
          {{ $t('common.delete') }}
        </el-button>
      </div>
    </div>

    <el-table
      v-loading="listLoading"
      row-key="id"
      :data="list.slice((listQuery.currentPage-1)*listQuery.pageSize,listQuery.currentPage*listQuery.pageSize)"
      border
      fit
      highlight-current-row
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="40" />
      <el-table-column :label="$t('syspost.postCode')" prop="postCode" />
      <el-table-column :label="$t('syspost.postName')" prop="postName" />
      <el-table-column :label="$t('syspost.postSequence')" prop="postSequence" />
      <el-table-column :label="$t('common.operate')" align="center">
        <template slot-scope="{row,$index}">
          <el-button type="text" @click="handleUpdate(row)">
            <i class="el-icon-edit-outline" />{{ $t('common.edit') }}
          </el-button>
          <el-popover :ref="'popover-' + row.id" placement="top" width="160" :title="$t('common.confirm-delete')" trigger="click">
            <div>
              <el-button size="mini" @click="$refs[`popover-` + row.id].doClose()">{{ $t('common.cancel') }}</el-button>
              <el-button type="primary" size="mini" @click="$refs[`popover-` + row.id].doClose();handleDelete(row,$index)">{{ $t('common.confirm') }}</el-button>
            </div>
            <el-button slot="reference" type="text" style="margin-left: 10px">
              <i class="el-icon-delete" />{{ $t('common.delete') }}
            </el-button>
          </el-popover>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.currentPage" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-dialog v-el-drag-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" :label-position="labelPosition" label-width="90px" style="width: 100%">
        <el-form-item :label="$t('syspost.postCode')" prop="postCode">
          <el-input v-model="temp.postCode" :placeholder="$t('common.enter')" :disabled="dialogStatus==='update'" maxlength="32" clearable />
        </el-form-item>
        <el-form-item :label="$t('syspost.postName')" prop="postName">
          <el-input v-model="temp.postName" :placeholder="$t('common.enter')" maxlength="64" clearable />
        </el-form-item>
        <el-form-item :label="$t('syspost.postSequence')" prop="postSequence">
          <el-input-number v-model="temp.postSequence" :placeholder="$t('common.enter')" :min="1" size="small" style="width: 100%" />
        </el-form-item>
        <el-form-item :label="$t('syspost.postParent')" prop="parentId">
          <el-tooltip :content="$t('common.choose-tooltip')" placement="bottom" effect="light">
            <el-select ref="selectParentId" v-model="temp.parentId" :placeholder="$t('common.choose')" style="width: 100%" clearable @clear="handleClearParentId">
              <el-option :key="temp.parentId" hidden :value="temp.parentId" :label="parentName" />
              <el-tree ref="treeParentId" node-key="id" :data="parentIdOptions" :props="{label: 'title'}" :expand-on-click-node="false" :check-on-click-node="true" @node-click="handleNodeClickParentId" />
            </el-select>
          </el-tooltip>
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
      <p>{{ $t('common.download-template') }}: <a style="color: #1890ff" @click="handleDownload(1)">{{ this.$t('route.syspost') + '.xlsx' }}</a></p>
      <center>
        <el-upload ref="upload" :data="dataObj" :headers="elUploadHeaders" action="/api/admin/syspost/importSysPost" :auto-upload="false" :on-success="handleSuccess" drag>
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
import { querySysPost, addSysPost, updateSysPost, deleteSysPost, querySysPostTree, exportSysPost, exportWordSysPost, exportPDFSysPost } from '@/api/admin'
import waves from '@/directive/waves' // waves directive
import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import qs from 'qs'
import { saveAs } from 'file-saver'
import mammoth from 'mammoth'
import { getToken } from '@/utils/auth'

export default {
  name: 'SysPost',
  components: { Pagination },
  directives: { waves, elDragDialog },
  data() {
    return {
      labelPosition: 'right',
      list: [],
      total: 0,
      listLoading: true,
      disabledStatus: false,
      listQuery: {
        currentPage: 1,
        pageSize: 10,
        postCode: undefined,
        postName: undefined
      },
      multipleSelection: [],
      parentIdOptions: [],
      parentName: '',
      temp: {
        id: undefined
      },
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
        postCode: [{ required: true, validator: this.checkPostCode, trigger: ['blur', 'change'] }],
        postName: [{ required: true, validator: this.checkPostName, trigger: ['blur', 'change'] }]
      },
      downloadLoading: false
    }
  },
  created() {
    if (window.innerWidth < 700) {
      this.labelPosition = 'top'
    }
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      querySysPost(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.pagination.total
        this.listLoading = false
      })
      querySysPostTree().then(response => {
        this.parentIdOptions = response.data
      })
    },
    handleFilter() {
      this.listQuery.currentPage = 1
      this.getList()
    },
    handleReset() {
      this.listQuery.postCode = ''
      this.listQuery.postName = ''
      this.handleFilter()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    resetTemp() {
      this.temp = {
        id: undefined
      }
    },
    handleCreate() {
      this.resetTemp()
      this.parentName = ''
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
          if (!this.temp.parentId) {
            this.temp.parentId = 0
          }
          this.disabledStatus = true
          addSysPost(this.temp).then(response => {
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
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
        if (this.temp.parentId === '0') {
          this.parentName = this.$t('common.none')
        } else {
          this.parentName = this.$refs.treeParentId.getNode(this.temp.parentId).label
        }
      })
    },
    updateData() {
      if (this.$refs.selectParentId.value) {
        this.validateParentId()
      }
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // const tempData = Object.assign({}, this.temp)
          const tempData = {}
          tempData.id = this.temp.id
          tempData.postCode = this.temp.postCode
          tempData.postName = this.temp.postName
          tempData.postSequence = this.temp.postSequence
          tempData.parentId = this.temp.parentId ? this.temp.parentId : 0
          this.disabledStatus = true
          updateSysPost(tempData).then(response => {
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
      deleteSysPost(deleteArray).then(response => {
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
      exportWordSysPost(qs.stringify(listQueryData)).then(response => {
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
      listQueryData.postName = this.listQuery.postName
      return listQueryData
    },
    handleDownload(isTemplate) { // 导出 Excel
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      listQueryData.isTemplate = isTemplate // 1为模板，0不为模板
      exportSysPost(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.syspost') + '.xlsx')
      })
      this.downloadLoading = false
    },
    handleDownloadWord() { // 导出 Word
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportWordSysPost(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.syspost') + '.docx')
      })
      this.downloadLoading = false
    },
    handleDownloadPDF() { // 导出 PDF
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportPDFSysPost(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.syspost') + '.pdf')
      })
      this.downloadLoading = false
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
        this.$refs['dataForm'].clearValidate('parentId')
      })
    },
    handleNodeClickParentId(data) {
      this.parentName = data.title
      this.temp.parentId = data.id
      this.$refs.selectParentId.blur()
      this.validateParentId()
    },
    handleClearParentId() {
      this.parentName = ''
      this.temp.parentId = ''
    },
    checkPostCode(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('syspost.postCode-required')))
      } else {
        callback()
      }
    },
    checkPostName(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('syspost.postName-required')))
      } else {
        callback()
      }
    }
  }
}
</script>
