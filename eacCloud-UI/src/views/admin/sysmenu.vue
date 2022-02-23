<template>
  <div class="app-container">
    <el-container>
      <div ref="asideDiv" style="width: 173px; height: 670px; overflow: scroll; margin-right: 5px; border: 1px solid #C0D4F0">
        <el-aside style="background-color: white; padding: 0px 5px 0px 0px">
          <div style="width: 155px; padding: 10px 10px 0px 10px">
            <el-input v-model="allListQuery.menuName" :placeholder="$t('sysmenu.menuName')" clearable @keyup.enter.native="handleAsideFilter" @blur="handleAsideFilter" />
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
            <el-table-column :label="$t('sysmenu.menuName')" prop="menuName" />
          </el-table>
        </el-aside>
      </div>
      <div style="width: 8px; height: 670px; margin-right: 5px; background-color: #C0D4F0">
        <i v-if="leftIcon" class="el-icon-caret-left" style="position: relative; top: 50%; left: -3px; cursor: pointer; color: #97A8BE" @click="handleLeftIcon" />
        <i v-if="rightIcon" class="el-icon-caret-right" style="position: relative; top: 50%; left: -5px; cursor: pointer; color: #97A8BE" @click="handleRightIcon" />
      </div>
      <div ref="mainDiv" style="width: 87%">
        <div class="filter-container">
          <el-input v-model="listQuery.menuName" :placeholder="$t('sysmenu.menuName')" clearable class="filter-input filter-item" style="width: 150px" @keyup.enter.native="handleFilter" />
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
            <el-button type="danger"  v-if="hasPerm('sysmenu:delete')"  plain class="filter-item" icon="el-icon-delete" @click="handleDelete">
              {{ $t('common.delete') }}
            </el-button>
          </div>
          <div v-if="advancedSearch" style="float: left">
            <el-input v-model="listQuery.menuCode" :placeholder="$t('sysmenu.menuCode')" clearable class="filter-input filter-item" @keyup.enter.native="handleFilter" />
            <el-input v-model="listQuery.menuIcon" :placeholder="$t('sysmenu.menuIcon')" clearable class="filter-input filter-item" @keyup.enter.native="handleFilter" />
            <el-input v-model="listQuery.menuPath" :placeholder="$t('sysmenu.menuPath')" clearable class="filter-input filter-item" @keyup.enter.native="handleFilter" />
            <el-select v-model="listQuery.menuStatus" :placeholder="$t('sysmenu.menuStatus')" clearable class="filter-input filter-item">
              <el-option :label="$t('common.show')" value="1" />
              <el-option :label="$t('common.hide')" value="0" />
            </el-select>
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
          <el-table-column :label="$t('sysmenu.menuName')" prop="menuName" />
          <el-table-column :label="$t('sysmenu.menuCode')" prop="menuCode" />
          <el-table-column :label="$t('sysmenu.menuIcon')" prop="menuIcon" />
          <el-table-column :label="$t('sysmenu.menuPath')" prop="menuPath" />
          <el-table-column :label="$t('sysmenu.menuSequence')" prop="menuSequence" />
          <el-table-column :label="$t('sysmenu.menuStatus')" class-name="status-col">
            <template slot-scope="{row}">
              <el-tag :type="row.menuStatus | statusFilter">
                <span v-if="row.menuStatus === 1">{{ $t('common.show') }}</span>
                <span v-else>{{ $t('common.hide') }}</span>
              </el-tag>
            </template>
          </el-table-column>
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

    <el-dialog v-el-drag-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="52%">
      <el-form ref="dataForm" :rules="rules" :model="temp" :inline="true" :label-position="labelPosition" label-width="90px" style="width: 100%">
        <el-form-item :label="$t('sysmenu.menuCode')" prop="menuCode">
          <el-input v-model="temp.menuCode" :placeholder="$t('common.enter')" :disabled="dialogStatus==='update'" maxlength="32" clearable />
        </el-form-item>
        <el-form-item :label="$t('sysmenu.menuName')" prop="menuName">
          <el-input v-model="temp.menuName" :placeholder="$t('common.enter')" maxlength="32" clearable />
        </el-form-item>
        <el-form-item :label="$t('sysmenu.menuIcon')" prop="menuIcon">
          <el-input v-model="temp.menuIcon" :placeholder="$t('common.enter')" maxlength="32" clearable />
        </el-form-item>
        <el-form-item :label="$t('sysmenu.menuPath')" prop="menuPath">
          <el-input v-model="temp.menuPath" :placeholder="$t('common.enter')" maxlength="512" clearable />
        </el-form-item>
        <el-form-item :label="$t('sysmenu.menuComponent')" prop="menuComponent">
          <el-input v-model="temp.menuComponent" :placeholder="$t('common.enter')" maxlength="256" clearable />
        </el-form-item>
        <el-form-item :label="$t('sysmenu.menuSequence')" prop="menuSequence">
          <el-input-number v-model="temp.menuSequence" :placeholder="$t('common.enter')" :min="1" size="small" :style="elInputNumberStyle" />
        </el-form-item>
        <el-form-item :label="$t('sysmenu.menuParent')" prop="parentId">
          <el-tooltip :content="$t('common.choose-tooltip')" placement="bottom" effect="light">
            <el-select ref="selectParentId" v-model="temp.parentId" :placeholder="$t('common.choose')" style="width: 100%" clearable @clear="handleClearParentId">
              <el-option :key="temp.parentId" hidden :value="temp.parentId" :label="parentName" />
              <el-tree ref="treeParentId" node-key="id" :data="parentIdOptions" :props="{label: 'title'}" :expand-on-click-node="false" :check-on-click-node="true" @node-click="handleNodeClickParentId" />
            </el-select>
          </el-tooltip>
        </el-form-item>
        <el-form-item :label="$t('sysmenu.menuStatus')" prop="menuStatus">
          <el-radio-group v-model="temp.menuStatus">
            <el-radio :label="1">{{ $t('common.show') }}</el-radio>
            <el-radio :label="0">{{ $t('common.hide') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('sysmenu.menuButton')" prop="menuButton">
          <el-checkbox-group v-model="checkedMenuButtonList">
            <el-checkbox v-for="item in menuButtonList" :key="item.value" :label="item.value">{{ item.label }}</el-checkbox>
          </el-checkbox-group>
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
      <p>{{ $t('common.download-template') }}: <a style="color: #1890ff" @click="handleDownload(1)">{{ this.$t('route.sysmenu') + '.xlsx' }}</a></p>
      <center>
        <el-upload ref="upload" :data="dataObj" :headers="elUploadHeaders" action="/api/admin/sysmenu/importSysMenu" :auto-upload="false" :on-success="handleSuccess" drag>
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
import { querySysMenu, addSysMenu, updateSysMenu, deleteSysMenu, querySysMenuTree, queryMenuButtonCheckbox, queryCheckedMenuButton, exportSysMenu, exportWordSysMenu, exportPDFSysMenu } from '@/api/admin'
import waves from '@/directive/waves' // waves directive
import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import qs from 'qs'
import { saveAs } from 'file-saver'
import mammoth from 'mammoth'
import { getToken } from '@/utils/auth'

export default {
  name: 'SysMenu',
  components: { Pagination },
  directives: { waves, elDragDialog },
  filters: {
    statusFilter(menuStatus) {
      const statusMap = {
        1: 'success',
        0: 'danger'
      }
      return statusMap[menuStatus]
    }
  },
  data() {
    return {
      leftIcon: true,
      rightIcon: false,
      labelPosition: 'right',
      elInputNumberStyle: 'width: 200px',
      list: [],
      allList: [],
      total: 0,
      listLoading: true,
      disabledStatus: false,
      listQuery: {
        currentPage: 1,
        pageSize: 10,
        menuName: undefined,
        menuPath: undefined
      },
      allListQuery: {
        currentPage: 1,
        pageSize: 10000
      },
      multipleSelection: [],
      parentIdOptions: [],
      parentName: '',
      checkedMenuButtonList: [],
      menuButtonList: [],
      temp: {
        id: undefined,
        menuStatus: 1
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
        menuCode: [{ required: true, validator: this.checkMenuCode, trigger: ['blur', 'change'] }],
        menuName: [{ required: true, validator: this.checkMenuName, trigger: ['blur', 'change'] }],
        menuPath: [{ required: true, validator: this.checkMenuPath, trigger: 'change' }],
        menuStatus: [{ required: true }]
      },
      downloadLoading: false,
      advancedSearch: false
    }
  },
  created() {
    if (window.innerWidth < 700) {
      this.labelPosition = 'top'
      this.elInputNumberStyle = 'width: 100%'
    }
    this.getInitList()
  },
  methods: {
    clickSwitch() {
      this.advancedSearch = !this.advancedSearch
    },
    handleLeftIcon() {
      this.$refs.asideDiv.style.width = '10px'
      this.$refs.asideDiv.style.minWidth = '10px'
      this.$refs.mainDiv.style.width = '98%'
      this.leftIcon = false
      this.rightIcon = true
    },
    handleRightIcon() {
      this.$refs.asideDiv.style.width = '165px'
      this.$refs.asideDiv.style.minWidth = '165px'
      this.$refs.mainDiv.style.width = '83.5%'
      this.leftIcon = true
      this.rightIcon = false
    },
    handleAsideFilter() {
      querySysMenu(this.allListQuery).then(response => {
        this.allList = response.data.list
      })
    },
    clickAsideData(row, event, column) {
      this.listQuery.id = row.id
      this.listQuery.parentMenuName = row.menuName
      this.listLoading = true
      querySysMenu(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.pagination.total
        this.listLoading = false
      })
    },
    getInitList() {
      querySysMenuTree().then(response => {
        this.parentIdOptions = response.data
      })
      queryMenuButtonCheckbox().then(response => {
        this.menuButtonList = response.data
      })
      this.getList()
    },
    getList() {
      this.listLoading = true
      querySysMenu(this.allListQuery).then(response => {
        this.allList = response.data.list
      })
      querySysMenu(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.pagination.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.currentPage = 1
      this.listQuery.id = undefined
      this.listQuery.parentMenuName = undefined
      this.getList()
    },
    handleReset() {
      this.listQuery.menuName = undefined
      this.listQuery.menuCode = undefined
      this.listQuery.menuIcon = undefined
      this.listQuery.menuStatus = undefined
      this.listQuery.menuPath = undefined
      this.listQuery.id = undefined
      this.listQuery.parentMenuName = undefined
      this.handleFilter()
      this.allListQuery.menuName = undefined
      querySysMenu(this.allListQuery).then(response => {
        this.allList = response.data.list
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        menuStatus: 1
      }
    },
    handleCreate() {
      this.resetTemp()
      this.parentName = undefined
      this.checkedMenuButtonList = []
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
          this.temp.menuButton = this.checkedMenuButtonList
          this.disabledStatus = true
          addSysMenu(this.temp).then(response => {
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
      queryCheckedMenuButton(this.temp.menuCode).then(response => {
        this.checkedMenuButtonList = response.data
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
          if (this.temp.parentId === '0') {
            this.parentName = this.$t('common.none')
          } else {
            this.parentName = this.$refs.treeParentId.getNode(this.temp.parentId).label
          }
        })
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
          tempData.menuCode = this.temp.menuCode
          tempData.menuName = this.temp.menuName
          tempData.menuIcon = this.temp.menuIcon
          tempData.menuPath = this.temp.menuPath
          tempData.menuComponent = this.temp.menuComponent
          tempData.menuSequence = this.temp.menuSequence
          tempData.menuStatus = this.temp.menuStatus
          tempData.parentId = this.temp.parentId ? this.temp.parentId : 0
          tempData.menuButton = this.checkedMenuButtonList
          this.disabledStatus = true
          updateSysMenu(tempData).then(response => {
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
      deleteSysMenu(deleteArray).then(response => {
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
      exportWordSysMenu(qs.stringify(listQueryData)).then(response => {
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
      listQueryData.menuName = this.listQuery.menuName
      listQueryData.menuCode = this.listQuery.menuCode
      listQueryData.menuIcon = this.listQuery.menuIcon
      listQueryData.menuStatus = this.listQuery.menuStatus
      listQueryData.menuPath = this.listQuery.menuPath
      return listQueryData
    },
    handleDownload(isTemplate) { // 导出 Excel
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      listQueryData.isTemplate = isTemplate // 1为模板，0不为模板
      exportSysMenu(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.sysmenu') + '.xlsx')
      })
      this.downloadLoading = false
    },
    handleDownloadWord() { // 导出 Word
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportWordSysMenu(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.sysmenu') + '.docx')
      })
      this.downloadLoading = false
    },
    handleDownloadPDF() { // 导出 PDF
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportPDFSysMenu(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.sysmenu') + '.pdf')
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
    checkMenuCode(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysmenu.menuCode-required')))
      } else {
        callback()
      }
    },
    checkMenuName(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysmenu.menuName-required')))
      } else {
        callback()
      }
    },
    checkMenuPath(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysmenu.menuPath-required')))
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
