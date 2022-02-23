<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.clientCode" :placeholder="$t('appclient.clientCode')" clearable class="filter-input filter-item" style="width: 150px" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.clientSecret" :placeholder="$t('appclient.clientSecret')" clearable class="filter-input filter-item" style="width: 150px" @keyup.enter.native="handleFilter" />
      <div style="float: right">
        <el-button v-waves type="primary" class="filter-item" icon="el-icon-search" @click="handleFilter">
          {{ $t('common.query') }}
        </el-button>
        <el-button v-waves type="primary" class="filter-item" icon="el-icon-refresh-right" @click="handleReset">
          {{ $t('common.reset') }}
        </el-button>
        <el-button v-waves type="primary" class="filter-item" icon="el-icon-search" @click="clickSwitch">
          {{ $t('common.advanced-search') }}
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
      <div v-if="advancedSearch" style="float: left">
        <el-input v-model="listQuery.authType" :placeholder="$t('appclient.authType')" clearable class="filter-input filter-item" style="width: 150px" @keyup.enter.native="handleFilter" />
        <el-input v-model="listQuery.authScope" :placeholder="$t('appclient.authScope')" clearable class="filter-input filter-item" style="width: 150px" @keyup.enter.native="handleFilter" />
        <el-input-number v-model="listQuery.tokenSeconds" :placeholder="$t('appclient.tokenSeconds')" clearable class="filter-input filter-item" style="width: 150px" @keyup.enter.native="handleFilter" />
        <el-input-number v-model="listQuery.refreshSeconds" :placeholder="$t('appclient.refreshSeconds')" clearable class="filter-input filter-item" style="width: 150px" @keyup.enter.native="handleFilter" />
        <el-input v-model="listQuery.fallbackUrl" :placeholder="$t('appclient.fallbackUrl')" clearable class="filter-input filter-item" style="width: 150px" @keyup.enter.native="handleFilter" />
        <el-input v-model="listQuery.clientDescription" :placeholder="$t('appclient.clientDescription')" clearable class="filter-input filter-item" style="width: 150px" @keyup.enter.native="handleFilter" />
      </div>
    </div>

    <el-table
      :key="tableKey"
      ref="multipleTable"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%"
      @sort-change="sortChange"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="40" />
      <el-table-column :label="$t('appclient.clientCode')" prop="clientCode" />
      <el-table-column :label="$t('appclient.clientSecret')" prop="clientSecret" />
      <el-table-column :label="$t('appclient.authType')" prop="authType" />
      <el-table-column :label="$t('appclient.authScope')" prop="authScope" />
      <el-table-column :label="$t('appclient.tokenSeconds')" prop="tokenSeconds" />
      <el-table-column :label="$t('appclient.refreshSeconds')" prop="refreshSeconds" />
      <el-table-column :label="$t('appclient.fallbackUrl')" prop="fallbackUrl" />
      <el-table-column :label="$t('appclient.clientDescription')" prop="clientDescription" />
      <el-table-column :label="$t('common.create-time')" min-width="80px" prop="createTime" sortable="custom" :class-name="getSortClass('createTime')">
        <template slot-scope="{row}">
          <span>{{ row.createTime | formatDate }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operate')" min-width="100px" align="center">
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
        <el-row>
          <el-col :span="12" :xs="24">
            <el-form-item :label="$t('appclient.clientCode')" prop="clientCode">
              <el-input v-model="temp.clientCode" :placeholder="$t('common.enter')" :disabled="dialogStatus==='update'" maxlength="32" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12" :xs="24">
            <el-form-item :label="$t('appclient.clientSecret')" prop="clientSecret">
              <el-input v-model="temp.clientSecret" :placeholder="$t('common.enter')" maxlength="64" clearable />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :xs="24">
            <el-form-item :label="$t('appclient.authType')" prop="authType">
              <el-input v-model="temp.authType" :placeholder="$t('common.enter')" maxlength="64" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12" :xs="24">
            <el-form-item :label="$t('appclient.authScope')" prop="authScope">
              <el-input v-model="temp.authScope" :placeholder="$t('common.enter')" maxlength="32" clearable />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :xs="24">
            <el-form-item :label="$t('appclient.tokenSeconds')" prop="tokenSeconds">
              <el-input-number v-model="temp.tokenSeconds" :placeholder="$t('common.enter')" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12" :xs="24">
            <el-form-item :label="$t('appclient.refreshSeconds')" prop="refreshSeconds">
              <el-input-number v-model="temp.refreshSeconds" :placeholder="$t('common.enter')" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item :label="$t('appclient.fallbackUrl')" prop="fallbackUrl">
          <el-input v-model="temp.fallbackUrl" :placeholder="$t('common.enter')" maxlength="512" clearable />
        </el-form-item>
        <el-form-item :label="$t('appclient.clientDescription')" prop="clientDescription">
          <el-input v-model="temp.clientDescription" type="textarea" :placeholder="$t('common.enter')" maxlength="256" clearable />
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
      <p>{{ $t('common.download-template') }}: <a style="color: #1890ff" @click="handleDownload(1)">{{ this.$t('route.appclient') + '.xlsx' }}</a></p>
      <center>
        <el-upload ref="upload" :data="dataObj" :headers="elUploadHeaders" action="/api/admin/appclient/importAppClient" :auto-upload="false" :on-success="handleSuccess" drag>
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
        <el-button type="primary"  @click="uploadData()">
         {{ $t('common.confirm') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { queryAppClient, addAppClient, updateAppClient, deleteAppClient, exportAppClient, exportWordAppClient, exportPDFAppClient } from '@/api/admin'
import waves from '@/directive/waves' // waves directive
import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import qs from 'qs'
import { saveAs } from 'file-saver'
import mammoth from 'mammoth'
import { getToken } from '@/utils/auth'

export default {
  name: 'AppClient',
  components: { Pagination },
  directives: { waves, elDragDialog },
  data() {
    return {
      tableKey: 0,
      labelPosition: 'right',
      list: [],
      total: 0,
      listLoading: true,
      disabledStatus: false,
      listQuery: {
        currentPage: 1,
        pageSize: 10,
        clientCode: undefined,
        authType: undefined,
        clientSecret: undefined,
        authScope: undefined,
        tokenSeconds: undefined,
        refreshSeconds: undefined,
        fallbackUrl: undefined,
        clientDescription: undefined,
        sorter: 'createTime_descend'
      },
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
        clientCode: [{ required: true, validator: this.checkClientCode, trigger: ['blur', 'change'] }],
        clientSecret: [{ required: true, validator: this.checkClientSecret, trigger: ['blur', 'change'] }],
        authType: [{ required: true, validator: this.checkAuthType, trigger: ['blur', 'change'] }],
        authScope: [{ required: true, validator: this.checkAuthScope, trigger: ['blur', 'change'] }],
        tokenSeconds: [{ required: true, validator: this.checkTokenSeconds, trigger: ['blur', 'change'] }],
        refreshSeconds: [{ required: true, validator: this.checkRefreshSeconds, trigger: ['blur', 'change'] }],
        fallbackUrl: [{ required: true, validator: this.checkFallbackUrl, trigger: ['blur', 'change'] }]
      },
      downloadLoading: false,
      advancedSearch: false
    }
  },
  created() {
    if (window.innerWidth < 700) {
      this.labelPosition = 'top'
    }
    this.getList()
  },
  methods: {
    clickSwitch() {
      this.advancedSearch = !this.advancedSearch
    },
    getList() {
      this.listLoading = true
      queryAppClient(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.pagination.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.currentPage = 1
      this.getList()
    },
    handleReset() {
      this.listQuery.clientCode = undefined
      this.listQuery.authType = undefined
      this.listQuery.clientSecret = undefined
      this.listQuery.authScope = undefined
      this.listQuery.tokenSeconds = undefined
      this.listQuery.refreshSeconds = undefined
      this.listQuery.fallbackUrl = undefined
      this.listQuery.clientDescription = undefined
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
    getSortClass: function(key) {
      const sorter = this.listQuery.sorter
      return sorter === `${key}_ascend` ? 'ascending' : 'descending'
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
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.disabledStatus = true
          addAppClient(this.temp).then(response => {
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
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // const tempData = Object.assign({}, this.temp)
          const tempData = {}
          tempData.id = this.temp.id
          tempData.clientCode = this.temp.clientCode
          tempData.clientSecret = this.temp.clientSecret
          tempData.authType = this.temp.authType
          tempData.authScope = this.temp.authScope
          tempData.tokenSeconds = this.temp.tokenSeconds
          tempData.refreshSeconds = this.temp.refreshSeconds
          tempData.fallbackUrl = this.temp.fallbackUrl
          tempData.clientDescription = this.temp.clientDescription
          this.disabledStatus = true
          updateAppClient(tempData).then(response => {
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
      deleteAppClient(deleteArray).then(response => {
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
      exportWordAppClient(qs.stringify(listQueryData)).then(response => {
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
      listQueryData.clientCode = this.listQuery.clientCode
      listQueryData.authType = this.listQuery.authType
      listQueryData.clientSecret = this.listQuery.clientSecret
      listQueryData.authScope = this.listQuery.authScope
      listQueryData.tokenSeconds = this.listQuery.tokenSeconds
      listQueryData.refreshSeconds = this.listQuery.refreshSeconds
      listQueryData.fallbackUrl = this.listQuery.fallbackUrl
      listQueryData.clientDescription = this.listQuery.clientDescription
      return listQueryData
    },
    handleDownload(isTemplate) { // 导出 Excel
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      listQueryData.isTemplate = isTemplate // 1为模板，0不为模板
      exportAppClient(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.appclient') + '.xlsx')
      })
      this.downloadLoading = false
    },
    handleDownloadWord() { // 导出 Word
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportWordAppClient(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.appclient') + '.docx')
      })
      this.downloadLoading = false
    },
    handleDownloadPDF() { // 导出 PDF
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportPDFAppClient(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.appclient') + '.pdf')
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
    checkClientCode(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('appclient.clientCode-required')))
      } else {
        callback()
      }
    },
    checkClientSecret(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('appclient.clientSecret-required')))
      } else {
        callback()
      }
    },
    checkAuthType(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('appclient.authType-required')))
      } else {
        callback()
      }
    },
    checkAuthScope(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('appclient.authScope-required')))
      } else {
        callback()
      }
    },
    checkTokenSeconds(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('appclient.tokenSeconds-required')))
      } else {
        callback()
      }
    },
    checkRefreshSeconds(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('appclient.refreshSeconds-required')))
      } else {
        callback()
      }
    },
    checkFallbackUrl(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('appclient.fallbackUrl-required')))
      } else {
        callback()
      }
    }
  }
}
</script>
