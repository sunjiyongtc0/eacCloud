<template>
  <div class="app-container">
    <el-container>
      <div ref="asideDiv" style="width: 140px; height: 628px; overflow: scroll; margin-right: 5px; border: 1px solid #C0D4F0">
        <el-aside style="background-color: white; padding: 0px 5px 0px 0px">
          <div style="width: 135px; padding: 10px 10px 0px 10px">
            <el-input v-model="allListQuery.dictName" :placeholder="$t('sysdict.dictName')" clearable @keyup.enter.native="handleAsideFilter" @blur="handleAsideFilter" />
          </div>
          <el-table
            row-key="id"
            :data="allList"
            fit
            highlight-current-row
            style="width: 100%; cursor: pointer"
            class="aside-table"
            @row-click="clickAsideData"
          >
            <el-table-column :label="$t('sysdict.dictName')" prop="dictName" />
          </el-table>
        </el-aside>
      </div>
      <div style="width: 8px; height: 628px; margin-right: 5px; background-color: #C0D4F0">
        <i v-if="leftIcon" class="el-icon-caret-left" style="position: relative; top: 50%; left: -3px; cursor: pointer; color: #97A8BE" @click="handleLeftIcon" />
        <i v-if="rightIcon" class="el-icon-caret-right" style="position: relative; top: 50%; left: -5px; cursor: pointer; color: #97A8BE" @click="handleRightIcon" />
      </div>
      <div ref="mainDiv" style="width: 87%">
        <div class="filter-container">
          <el-input v-model="listQuery.dictName" :placeholder="$t('sysdict.dictName')" clearable class="filter-input filter-item" style="width: 100px" @keyup.enter.native="handleFilter" />
          <el-input v-model="listQuery.dictValue" :placeholder="$t('sysdict.dictValue')" clearable class="filter-input filter-item" style="width: 100px" @keyup.enter.native="handleFilter" />
          <el-input v-model="listQuery.dictType" :placeholder="$t('sysdict.dictType')" clearable class="filter-input filter-item" style="width: 100px" @keyup.enter.native="handleFilter" />
          <div style="float: right">
            <el-button v-waves type="primary" class="filter-item" icon="el-icon-search" @click="handleFilter">
              {{ $t('common.query') }}
            </el-button>
            <el-button v-waves type="primary" class="filter-item" style="margin-left: 5px;" icon="el-icon-refresh-right" @click="handleReset">
              {{ $t('common.reset') }}
            </el-button>
            <el-button type="primary" class="filter-item" style="margin-left: 5px;" icon="el-icon-plus" @click="handleCreate">
              {{ $t('common.add') }}
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
          <el-table-column :label="$t('sysdict.dictName')" prop="dictName" />
          <el-table-column :label="$t('sysdict.dictValue')" prop="dictValue" />
          <el-table-column :label="$t('sysdict.dictType')" prop="dictType" />
          <el-table-column :label="$t('sysdict.dictSequence')" prop="dictSequence" />
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
      </div>
    </el-container>

    <el-dialog v-el-drag-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" :label-position="labelPosition" label-width="90px" style="width: 100%">
        <el-form-item :label="$t('sysdict.dictName')" prop="dictName">
          <el-input v-model="temp.dictName" :placeholder="$t('common.enter')" maxlength="32" clearable />
        </el-form-item>
        <el-form-item :label="$t('sysdict.dictValue')" prop="dictValue">
          <el-input v-model="temp.dictValue" :placeholder="$t('common.enter')" maxlength="32" clearable />
        </el-form-item>
        <el-form-item :label="$t('sysdict.dictType')" prop="dictType">
          <el-input v-model="temp.dictType" :disabled="dialogStatus==='update'" :placeholder="$t('common.enter')" maxlength="32" clearable />
        </el-form-item>
        <el-form-item :label="$t('sysdict.dictSequence')" prop="dictSequence">
          <el-input-number v-model="temp.dictSequence" :placeholder="$t('common.enter')" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item :label="$t('sysdict.dictParent')" prop="parentId">
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
      <p>{{ $t('common.download-template') }}: <a style="color: #1890ff" @click="handleDownload(1)">{{ this.$t('route.sysdict') + '.xlsx' }}</a></p>
      <center>
        <el-upload ref="upload" :data="dataObj" :headers="elUploadHeaders" action="/api/admin/sysdict/importSysDict" :auto-upload="false" :on-success="handleSuccess" drag>
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
import { querySysDict, querySysDictTree, addSysDict, updateSysDict, deleteSysDict, exportSysDict, exportWordSysDict, exportPDFSysDict } from '@/api/admin'
import waves from '@/directive/waves' // waves directive
import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import qs from 'qs'
import { saveAs } from 'file-saver'
import mammoth from 'mammoth'
import { getToken } from '@/utils/auth'

export default {
  name: 'SysDict',
  components: { Pagination },
  directives: { waves, elDragDialog },
  data() {
    return {
      leftIcon: true,
      rightIcon: false,
      labelPosition: 'right',
      list: [],
      allList: [],
      total: 0,
      listLoading: true,
      disabledStatus: false,
      listQuery: {
        currentPage: 1,
        pageSize: 10,
        dictName: undefined,
        dictType: undefined,
        dictValue: undefined
      },
      allListQuery: {
        currentPage: 1,
        pageSize: 10000
      },
      parentIdOptions: [],
      parentName: '',
      temp: {
        id: undefined
      },
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
        dictName: [{ required: true, validator: this.checkDictName, trigger: ['blur', 'change'] }],
        dictValue: [{ required: true, validator: this.checkDictValue, trigger: ['blur', 'change'] }],
        dictType: [{ required: true, validator: this.checkDictType, trigger: ['blur', 'change'] }]
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
    handleLeftIcon() {
      this.$refs.asideDiv.style.width = '10px'
      this.$refs.asideDiv.style.minWidth = '10px'
      this.$refs.mainDiv.style.width = '98%'
      this.leftIcon = false
      this.rightIcon = true
    },
    handleRightIcon() {
      this.$refs.asideDiv.style.width = '135px'
      this.$refs.asideDiv.style.minWidth = '135px'
      this.$refs.mainDiv.style.width = '86%'
      this.leftIcon = true
      this.rightIcon = false
    },
    handleAsideFilter() {
      querySysDict(this.allListQuery).then(response => {
        this.allList = response.data.list
      })
    },
    clickAsideData(row, event, column) {
      this.listQuery.id = row.id
      this.listQuery.parentDictName = row.dictName
      this.listLoading = true
      querySysDict(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.pagination.total
        this.listLoading = false
      })
    },
    getList() {
      this.listLoading = true
      querySysDict(this.allListQuery).then(response => {
        this.allList = response.data.list
      })
      querySysDict(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.pagination.total
        this.listLoading = false
      })
      querySysDictTree().then(response => {
        this.parentIdOptions = response.data.list
      })
    },
    handleFilter() {
      this.listQuery.currentPage = 1
      this.listQuery.id = undefined
      this.listQuery.parentDictName = undefined
      this.getList()
    },
    handleReset() {
      this.listQuery.dictName = undefined
      this.listQuery.dictType = undefined
      this.listQuery.dictValue = undefined
      this.handleFilter()
      this.listQuery.id = undefined
      this.listQuery.parentDictName = undefined
      this.allListQuery.dictName = undefined
      querySysDict(this.allListQuery).then(response => {
        this.allList = response.data.list
      })
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
          if (!this.temp.parentId) {
            this.temp.parentId = 0
          }
          this.disabledStatus = true
          addSysDict(this.temp).then(response => {
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
          tempData.dictName = this.temp.dictName
          tempData.dictType = this.temp.dictType
          tempData.dictValue = this.temp.dictValue
          tempData.dictSequence = this.temp.dictSequence
          tempData.parentId = this.temp.parentId ? this.temp.parentId : 0
          this.disabledStatus = true
          updateSysDict(tempData).then(response => {
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
      deleteSysDict(deleteArray).then(response => {
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
      exportWordSysDict(qs.stringify(listQueryData)).then(response => {
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
      listQueryData.dictName = this.listQuery.dictName
      listQueryData.dictValue = this.listQuery.dictValue
      listQueryData.dictType = this.listQuery.dictType
      return listQueryData
    },
    handleDownload(isTemplate) { // 导出 Excel
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      listQueryData.isTemplate = isTemplate // 1为模板，0不为模板
      exportSysDict(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.sysdict') + '.xlsx')
      })
      this.downloadLoading = false
    },
    handleDownloadWord() { // 导出 Word
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportWordSysDict(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.sysdict') + '.docx')
      })
      this.downloadLoading = false
    },
    handleDownloadPDF() { // 导出 PDF
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportPDFSysDict(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.sysdict') + '.pdf')
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
      this.parentName = undefined
      this.temp.parentId = undefined
    },
    checkDictName(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysdict.dictName-required')))
      } else {
        callback()
      }
    },
    checkDictValue(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysdict.dictValue-required')))
      } else {
        callback()
      }
    },
    checkDictType(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysdict.dictType-required')))
      } else {
        callback()
      }
    }
  }
}
</script>
<style>
  .aside-table td,.aside-table th.is-leaf {
    border: none;
  }
  .aside-table::before {
    width: 0;
  }
</style>
