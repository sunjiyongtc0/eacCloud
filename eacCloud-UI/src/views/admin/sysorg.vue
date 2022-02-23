<template>
  <div class="app-container">
    <el-container>
      <div ref="asideDiv" style="width: 140px; height: 510px; overflow: scroll; margin-right: 5px; border: 1px solid #C0D4F0">
        <el-aside style="background-color: white; padding: 0px 5px 0px 0px">
          <div style="width: 135px; padding: 10px 10px 0px 10px">
            <el-input v-model="allListQuery.orgName" :placeholder="$t('sysorg.orgName')" clearable @keyup.enter.native="handleAsideFilter" @blur="handleAsideFilter" />
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
            <el-table-column :label="$t('sysorg.orgName')" prop="orgName" />
          </el-table>
        </el-aside>
      </div>
      <div style="width: 8px; height: 510px; margin-right: 5px; background-color: #C0D4F0">
        <i v-if="leftIcon" class="el-icon-caret-left" style="position: relative; top: 50%; left: -3px; cursor: pointer; color: #97A8BE" @click="handleLeftIcon" />
        <i v-if="rightIcon" class="el-icon-caret-right" style="position: relative; top: 50%; left: -5px; cursor: pointer; color: #97A8BE" @click="handleRightIcon" />
      </div>
      <div ref="mainDiv" style="width: 87%">
        <div class="filter-container">
          <el-input v-model="listQuery.orgName" :placeholder="$t('sysorg.orgName')" clearable class="filter-input filter-item" style="width: 100px" @keyup.enter.native="handleFilter" />
          <el-select v-model="listQuery.orgType" :placeholder="$t('sysorg.orgType')" clearable class="filter-input filter-item" style="width: 100px">
            <el-option v-for="item in orgTypeOptions" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
          <el-input v-model="listQuery.orgDescription" :placeholder="$t('sysorg.orgDescription')" clearable class="filter-input filter-item" style="width: 100px" @keyup.enter.native="handleFilter" />
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
          <el-table-column :label="$t('sysorg.orgName')" prop="orgName" />
          <el-table-column :label="$t('sysorg.orgType')" prop="orgTypeCn" />
          <el-table-column :label="$t('sysorg.orgDescription')" prop="orgDescription" />
          <el-table-column :label="$t('sysorg.orgSequence')" prop="orgSequence" />
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
        <el-form-item :label="$t('sysorg.orgName')" prop="orgName">
          <el-input v-model="temp.orgName" :placeholder="$t('common.enter')" maxlength="64" clearable />
        </el-form-item>
        <el-form-item :label="$t('sysorg.orgType')" prop="orgType">
          <el-select v-model="temp.orgType" :placeholder="$t('common.choose')" clearable style="width: 100%">
            <el-option v-for="item in orgTypeOptions" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('sysorg.orgDescription')" prop="orgDescription">
          <el-input v-model="temp.orgDescription" :placeholder="$t('common.enter')" maxlength="256" clearable />
        </el-form-item>
        <el-form-item :label="$t('sysorg.orgSequence')" prop="orgSequence">
          <el-input-number v-model="temp.orgSequence" :placeholder="$t('common.enter')" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item :label="$t('sysorg.orgParent')" prop="parentId">
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
      <p>{{ $t('common.download-template') }}: <a style="color: #1890ff" @click="handleDownload(1)">{{ this.$t('route.sysorg') + '.xlsx' }}</a></p>
      <center>
        <el-upload ref="upload" :data="dataObj" :headers="elUploadHeaders" action="/api/admin/sysorg/importSysOrg" :auto-upload="false" :on-success="handleSuccess" drag>
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
import { querySysOrg, queryOrgType, querySysOrgTree, addSysOrg, updateSysOrg, deleteSysOrg, exportSysOrg, exportWordSysOrg, exportPDFSysOrg } from '@/api/admin'
import waves from '@/directive/waves' // waves directive
import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import qs from 'qs'
import { saveAs } from 'file-saver'
import mammoth from 'mammoth'
import { getToken } from '@/utils/auth'

export default {
  name: 'SysOrg',
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
        orgName: undefined,
        orgType: undefined,
        orgDescription: undefined
      },
      allListQuery: {
        currentPage: 1,
        pageSize: 10000
      },
      multipleSelection: [],
      orgTypeOptions: [],
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
        orgName: [{ required: true, validator: this.checkOrgName, trigger: ['blur', 'change'] }],
        orgType: [{ required: true, validator: this.checkOrgType, trigger: 'change' }]
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
      this.$refs.asideDiv.style.width = '140px'
      this.$refs.asideDiv.style.minWidth = '140px'
      this.$refs.mainDiv.style.width = '86%'
      this.leftIcon = true
      this.rightIcon = false
    },
    handleAsideFilter() {
      querySysOrg(this.allListQuery).then(response => {
        this.allList = response.data.list
      })
    },
    clickAsideData(row, event, column) {
      this.listQuery.id = row.id
      this.listQuery.parentOrgName = row.orgName
      this.listLoading = true
      querySysOrg(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.pagination.total
        this.listLoading = false
      })
    },
    getList() {
      this.listLoading = true
      querySysOrg(this.allListQuery).then(response => {
        this.allList = response.data.list
      })
      querySysOrg(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.pagination.total
        this.listLoading = false
      })
      queryOrgType().then(response => {
        this.orgTypeOptions = response.data.list
      })
      querySysOrgTree().then(response => {
        this.parentIdOptions = response.data.list
      })
    },
    handleFilter() {
      this.listQuery.currentPage = 1
      this.listQuery.id = undefined
      this.listQuery.parentOrgName = undefined
      this.getList()
    },
    handleReset() {
      this.listQuery.orgName = undefined
      this.listQuery.orgType = undefined
      this.listQuery.orgDescription = undefined
      this.handleFilter()
      this.listQuery.id = undefined
      this.listQuery.parentOrgName = undefined
      this.allListQuery.orgName = undefined
      querySysOrg(this.allListQuery).then(response => {
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
          addSysOrg(this.temp).then(response => {
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
          tempData.orgName = this.temp.orgName
          tempData.orgType = this.temp.orgType
          tempData.orgDescription = this.temp.orgDescription
          tempData.orgSequence = this.temp.orgSequence
          tempData.parentId = this.temp.parentId ? this.temp.parentId : 0
          this.disabledStatus = true
          updateSysOrg(tempData).then(response => {
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
      deleteSysOrg(deleteArray).then(response => {
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
      exportWordSysOrg(qs.stringify(listQueryData)).then(response => {
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
      listQueryData.orgName = this.listQuery.orgName
      listQueryData.orgType = this.listQuery.orgType
      listQueryData.orgDescription = this.listQuery.orgDescription
      return listQueryData
    },
    handleDownload(isTemplate) { // 导出 Excel
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      listQueryData.isTemplate = isTemplate // 1为模板，0不为模板
      exportSysOrg(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.sysorg') + '.xlsx')
      })
      this.downloadLoading = false
    },
    handleDownloadWord() { // 导出 Word
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportWordSysOrg(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.sysorg') + '.docx')
      })
      this.downloadLoading = false
    },
    handleDownloadPDF() { // 导出 PDF
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportPDFSysOrg(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.sysorg') + '.pdf')
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
    checkOrgName(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysorg.orgName-required')))
      } else {
        callback()
      }
    },
    checkOrgType(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysorg.orgType-required')))
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
