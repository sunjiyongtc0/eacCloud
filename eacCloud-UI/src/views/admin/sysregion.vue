<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.regionName" :placeholder="$t('sysregion.regionName')" clearable class="filter-input filter-item" style="width: 140px" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.regionCode" :placeholder="$t('sysregion.regionCode')" clearable class="filter-input filter-item" style="width: 140px" @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.regionType" :placeholder="$t('sysregion.regionType')" clearable class="filter-input filter-item" style="width: 140px">
        <el-option v-for="item in regionTypeOptions" :key="item.value" :label="item.name" :value="item.value" />
      </el-select>
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
      <el-table-column :label="$t('sysregion.regionName')" prop="regionName" />
      <el-table-column :label="$t('sysregion.regionCode')" prop="regionCode" />
      <el-table-column :label="$t('sysregion.regionType')" prop="regionTypeCn" />
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
        <el-form-item :label="$t('sysregion.regionName')" prop="regionName">
          <el-input v-model="temp.regionName" :placeholder="$t('common.enter')" minlength="2" maxlength="40" clearable />
        </el-form-item>
        <el-form-item :label="$t('sysregion.regionCode')" prop="regionCode">
          <el-input v-model="temp.regionCode" :disabled="dialogStatus==='update'" :placeholder="$t('common.enter')" minlength="6" maxlength="20" clearable />
        </el-form-item>
        <el-form-item :label="$t('sysregion.regionType')" prop="regionType">
          <el-select v-model="temp.regionType" :placeholder="$t('common.choose')" clearable style="width: 100%">
            <el-option v-for="item in regionTypeOptions" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('sysregion.regionParent')" prop="parentRegionCode">
          <el-tooltip :content="$t('common.choose-tooltip')" placement="bottom" effect="light">
            <el-select ref="selectParentId" v-model="temp.parentRegionCode" :placeholder="$t('common.choose')" style="width: 100%" clearable @clear="handleClearParentId">
              <el-option :key="temp.parentRegionCode" hidden :value="temp.parentRegionCode" :label="parentName" />
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
      <p>{{ $t('common.download-template') }}: <a style="color: #1890ff" @click="handleDownload(1)">{{ this.$t('route.sysregion') + '.xlsx' }}</a></p>
      <center>
        <el-upload ref="upload" :data="dataObj" :headers="elUploadHeaders" action="/api/admin/sysregion/importSysRegion" :auto-upload="false" :on-success="handleSuccess" drag>
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
import { querySysRegion, queryRegionType, querySysRegionTree, addSysRegion, updateSysRegion, deleteSysRegion, exportSysRegion, exportWordSysRegion, exportPDFSysRegion } from '@/api/admin'
import waves from '@/directive/waves' // waves directive
import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import qs from 'qs'
import { saveAs } from 'file-saver'
import mammoth from 'mammoth'
import { getToken } from '@/utils/auth'

export default {
  name: 'SysRegion',
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
        regionName: undefined,
        regionType: undefined,
        regionCode: undefined
      },
      multipleSelection: [],
      regionTypeOptions: [],
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
        regionName: [{ required: true, validator: this.checkRegionName, trigger: ['blur', 'change'] }],
        regionCode: [{ required: true, validator: this.checkRegionCode, trigger: ['blur', 'change'] }],
        regionType: [{ required: true, validator: this.checkRegionType, trigger: 'change' }]
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
      querySysRegion(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.pagination.total
        this.listLoading = false
      })
      queryRegionType().then(response => {
        this.regionTypeOptions = response.data.list
      })
      querySysRegionTree().then(response => {
        this.parentIdOptions = response.data.list
      })
    },
    handleFilter() {
      this.listQuery.currentPage = 1
      this.getList()
    },
    handleReset() {
      this.listQuery.regionName = ''
      this.listQuery.regionType = ''
      this.listQuery.regionCode = ''
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
          if (!this.temp.parentRegionCode) {
            this.temp.parentRegionCode = 0
          }
          this.disabledStatus = true
          addSysRegion(this.temp).then(response => {
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
        if (this.temp.parentRegionCode === '0') {
          this.parentName = this.$t('common.none')
        } else {
          this.parentName = this.$refs.treeParentId.getNode(this.temp.parentRegionCode).label
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
          tempData.regionName = this.temp.regionName
          tempData.regionCode = this.temp.regionCode
          tempData.regionType = this.temp.regionType
          tempData.parentRegionCode = this.temp.parentRegionCode ? this.temp.parentRegionCode : 0
          this.disabledStatus = true
          updateSysRegion(tempData).then(response => {
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
      if (row.regionCode) {
        this.delete(row.regionCode)
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
          this.delete(this.multipleSelection.map(item => { return item.regionCode }).join(','))
        }).catch(() => {})
      }
    },
    delete(deleteArray) {
      deleteSysRegion(deleteArray).then(response => {
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
      exportWordSysRegion(qs.stringify(listQueryData)).then(response => {
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
      listQueryData.regionName = this.listQuery.regionName
      listQueryData.regionCode = this.listQuery.regionCode
      listQueryData.regionType = this.listQuery.regionType
      return listQueryData
    },
    handleDownload(isTemplate) { // 导出 Excel
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      listQueryData.isTemplate = isTemplate // 1为模板，0不为模板
      exportSysRegion(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.sysregion') + '.xlsx')
      })
      this.downloadLoading = false
    },
    handleDownloadWord() { // 导出 Word
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportWordSysRegion(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.sysregion') + '.docx')
      })
      this.downloadLoading = false
    },
    handleDownloadPDF() { // 导出 PDF
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportPDFSysRegion(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.sysregion') + '.pdf')
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
        this.$refs['dataForm'].clearValidate('parentRegionCode')
      })
    },
    handleNodeClickParentId(data) {
      this.parentName = data.title
      this.temp.parentRegionCode = data.id
      this.$refs.selectParentId.blur()
      this.validateParentId()
    },
    handleClearParentId() {
      this.parentName = ''
      this.temp.parentRegionCode = ''
    },
    checkRegionName(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysregion.regionName-required')))
      } else {
        if (value.length >= 2 && value.length <= 40) {
          callback()
        } else {
          return callback(new Error(this.$t('sysregion.regionName-length')))
        }
      }
    },
    checkRegionCode(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysregion.regionCode-required')))
      } else {
        if (value.length >= 6 && value.length <= 20) {
          const reg = /^[0123456789]+$/
          if (reg.test(value)) {
            callback()
          } else {
            return callback(new Error(this.$t('sysregion.regionCode-number')))
          }
        } else {
          return callback(new Error(this.$t('sysregion.regionCode-length')))
        }
      }
    },
    checkRegionType(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysregion.regionType-required')))
      } else {
        callback()
      }
    }
  }
}
</script>
