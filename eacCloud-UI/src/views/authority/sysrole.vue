<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.roleName" :placeholder="$t('sysrole.roleName')" clearable class="filter-input filter-item" style="width: 120px" @keyup.enter.native="handleFilter" />
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
        <el-dropdown split-button type="primary" class="filter-item" style="padding-left: 10px; padding-right: 10px;" trigger="click" @command="handleCommand">
          <i class="el-icon-postcard" style="margin-right: 5px" /> {{ $t('common.authorize') }}
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item icon="el-icon-postcard" command="menu">{{ $t('sysrole.authorize-menu') }}</el-dropdown-item>
            <el-dropdown-item icon="el-icon-postcard" command="button">{{ $t('sysrole.authorize-button') }}</el-dropdown-item>
            <el-dropdown-item icon="el-icon-postcard" command="url">{{ $t('sysrole.authorize-url') }}</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-dropdown split-button type="primary" class="filter-item" style="padding-right: 10px;" trigger="click" @command="handleUserCommand">
          <svg-icon icon-class="user" style="margin-right: 5px" /> {{ $t('sysuser.user') }}
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="view"><svg-icon icon-class="user" style="margin-right: 5px" /> {{ $t('sysuser.view-user') }}</el-dropdown-item>
            <el-dropdown-item command="assign"><svg-icon icon-class="user" style="margin-right: 5px" /> {{ $t('sysrole.assign-user') }}</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
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
        <el-input v-model="listQuery.roleCode" :placeholder="$t('sysrole.roleCode')" clearable class="filter-input filter-item" @keyup.enter.native="handleFilter" />
        <el-input v-model="listQuery.roleDescription" :placeholder="$t('sysrole.roleDescription')" clearable class="filter-input filter-item" @keyup.enter.native="handleFilter" />
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
      <el-table-column :label="$t('sysrole.roleCode')" prop="roleCode" />
      <el-table-column :label="$t('sysrole.roleName')" prop="roleName" />
      <el-table-column :label="$t('sysrole.roleDescription')" prop="roleDescription" />
      <el-table-column :label="$t('common.create-time')" min-width="100px" prop="createTime" sortable="custom" :class-name="getSortClass('createTime')">
        <template slot-scope="{row}">
          <span>{{ row.createTime | formatDate }}</span>
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

    <el-dialog v-el-drag-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" :label-position="labelPosition" label-width="90px" style="width: 100%">
        <el-form-item :label="$t('sysrole.roleCode')" prop="roleCode">
          <el-input v-model="temp.roleCode" :placeholder="$t('common.enter')" :disabled="dialogStatus==='update'" maxlength="32" clearable />
        </el-form-item>
        <el-form-item :label="$t('sysrole.roleName')" prop="roleName">
          <el-input v-model="temp.roleName" :placeholder="$t('common.enter')" maxlength="64" clearable />
        </el-form-item>
        <el-form-item :label="$t('sysrole.roleDescription')" prop="roleDescription">
          <el-input v-model="temp.roleDescription" type="textarea" :placeholder="$t('common.enter')" maxlength="256" clearable />
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

    <el-dialog v-el-drag-dialog :title="$t('sysrole.authorize-menu')" :visible.sync="menuTreeDialogFormVisible">
      <el-tree ref="menuTreeRef" :data="menuTree" show-checkbox node-key="id" highlight-current :props="defaultProps" />
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuTreeDialogFormVisible = false">
          {{ $t('common.cancel') }}
        </el-button>
        <el-button type="primary" @click="createAuthorizeMenu()">
          {{ $t('common.confirm') }}
        </el-button>
      </div>
    </el-dialog>

    <el-dialog v-el-drag-dialog :title="$t('sysrole.authorize-buttontip')" :visible.sync="menuButtonTreeDialogFormVisible">
      <el-tree ref="menuButtonTreeRef" :data="menuButtonTree" show-checkbox node-key="key" highlight-current :props="defaultProps" />
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuButtonTreeDialogFormVisible = false">
          {{ $t('common.cancel') }}
        </el-button>
        <el-button type="primary" @click="createAuthorizeMenuButton()">
          {{ $t('common.confirm') }}
        </el-button>
      </div>
    </el-dialog>

    <el-dialog v-el-drag-dialog :title="$t('sysrole.assign-user')" :visible.sync="authorizeUserDialogFormVisible" width="700px">
      <tree-transfer :from_data="orgUserTree" :to_data="assignOrgUserTree" :title="treeTransferTitle" root-pid-value="0" filter open-all @addBtn="addTransfer" @removeBtn="removeTransfer" />
      <div slot="footer" class="dialog-footer">
        <el-button @click="authorizeUserDialogFormVisible = false">
          {{ $t('common.cancel') }}
        </el-button>
        <el-button type="primary" @click="createAuthorizeUserButton()">
          {{ $t('common.confirm') }}
        </el-button>
      </div>
    </el-dialog>

    <el-dialog v-el-drag-dialog :title="$t('sysrole.authorize-url')" :visible.sync="authorizeUrlDialogFormVisible" width="65%">
      <el-container style="width: 100%">
        <div ref="asideDiv" style="width: 150px; height: 525px; overflow: scroll; margin-right: 5px; border: 1px solid #C0D4F0">
          <el-aside style="background-color: white; padding: 0px 5px 0px 0px">
            <div style="width: 120px; padding: 10px 10px 0px 10px" class="allQueryClass">
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
        <div style="width: 8px; height: 525px; margin-right: 5px; background-color: #C0D4F0">
          <i v-if="leftIcon" class="el-icon-caret-left" style="position: relative; top: 50%; left: -3px; cursor: pointer; color: #97A8BE" @click="handleLeftIcon" />
          <i v-if="rightIcon" class="el-icon-caret-right" style="position: relative; top: 50%; left: -5px; cursor: pointer; color: #97A8BE" @click="handleRightIcon" />
        </div>
        <div ref="mainDiv" style="width: 87%">
          <div class="filter-container">
            <el-input v-model="urlListQuery.url" :placeholder="$t('sysurl.url')" clearable class="filter-input filter-item" @keyup.enter.native="getUrlList" />
            <el-input v-model="urlListQuery.description" :placeholder="$t('sysurl.description')" clearable class="filter-input filter-item" @keyup.enter.native="getUrlList" />
            <el-button v-waves type="primary" class="filter-item" icon="el-icon-search" @click="getUrlList">
              {{ $t('common.query') }}
            </el-button>
            <el-button v-waves type="primary" class="filter-item" icon="el-icon-refresh-right" @click="handleResetUrl">
              {{ $t('common.reset') }}
            </el-button>
          </div>
          <el-table
            ref="urlTable"
            :key="2"
            :data="urlList"
            border
            fit
            highlight-current-row
            style="width: 100%; height: 470px; overflow: auto"
            @selection-change="handleSelectionChangeUrl"
          >
            <el-table-column type="selection" width="40" />
            <el-table-column :label="$t('sysurl.url')" prop="url" show-overflow-tooltip />
            <el-table-column :label="$t('sysurl.description')" prop="description" show-overflow-tooltip />
          </el-table>
        </div>
      </el-container>
      <div slot="footer" class="dialog-footer">
        <el-button @click="authorizeUrlDialogFormVisible = false">
          {{ $t('common.cancel') }}
        </el-button>
        <el-button type="primary" @click="createAuthorizeUrl()">
          {{ $t('common.confirm') }}
        </el-button>
      </div>
    </el-dialog>

    <el-dialog v-el-drag-dialog :title="$t('sysuser.view-user')" :visible.sync="viewUserDialogFormVisible" width="68%">
      <sysuser-component :msg="singleMulti" @hideSysUserComponent="hideSysUserComponent" @sysUserComponentMsg="sysUserComponentMsg" />
    </el-dialog>

    <el-dialog v-el-drag-dialog :title="$t('common.import')" :visible.sync="importDialogFormVisible">
      <p>{{ $t('common.download-template') }}: <a style="color: #1890ff" @click="handleDownload(1)">{{ this.$t('route.sysrole') + '.xlsx' }}</a></p>
      <center>
        <el-upload ref="upload" :data="dataObj" :headers="elUploadHeaders" action="/api/admin/sysrole/importSysRole" :auto-upload="false" :on-success="handleSuccess" drag>
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
import { querySysRole, querySysMenuTree, queryOrgUserTree, queryMenuIdByRoleId, authorizeMenuToRole, querySysMenuButtonTree, queryMenuButtonByRoleCode, querySysMenu, queryUrlIdByRoleId, querySysUrl, authorizeMenuButtonToRole, authorizeUserToRole, authorizeUrlToRole, addSysRole, updateSysRole, deleteSysRole, exportSysRole, exportWordSysRole, exportPDFSysRole } from '@/api/admin'
import waves from '@/directive/waves' // waves directive
import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import SysUserComponent from '@/components/SysUser'
import qs from 'qs'
import { saveAs } from 'file-saver'
import treeTransfer from 'el-tree-transfer'
import mammoth from 'mammoth'
import { getToken } from '@/utils/auth'

export default {
  name: 'SysRole',
  components: { Pagination, treeTransfer, 'sysuser-component': SysUserComponent },
  directives: { waves, elDragDialog },
  data() {
    return {
      leftIcon: true,
      rightIcon: false,
      tableKey: 0,
      labelPosition: 'right',
      list: [],
      allList: [],
      urlList: [],
      checkedUrlList: [],
      total: 0,
      singleMulti: undefined,
      listLoading: true,
      disabledStatus: false,
      listQuery: {
        currentPage: 1,
        pageSize: 10,
        roleName: undefined,
        roleCode: undefined,
        roleDescription: undefined,
        sorter: 'createTime_descend'
      },
      allListQuery: {
        currentPage: 1,
        pageSize: 10000,
        menuName: undefined
      },
      urlListQuery: {
        currentPage: 1,
        pageSize: 10000,
        menuCode: undefined,
        url: undefined,
        description: undefined
      },
      temp: {
        id: undefined
      },
      multipleSelection: [],
      multipleSelectionUrl: [],
      menuTreeDialogFormVisible: false,
      menuTree: [],
      defaultProps: {
        children: 'children',
        label: 'title'
      },
      menuButtonTree: [],
      orgUserTree: [],
      assignOrgUserTree: [],
      toData: [],
      toUserData: [],
      treeTransferTitle: [this.$t('sysrole.candidate-user'), this.$t('sysrole.selected-user')],
      menuButtonTreeDialogFormVisible: false,
      authorizeUserDialogFormVisible: false,
      authorizeUrlDialogFormVisible: false,
      viewUserDialogFormVisible: false,
      dialogFormVisible: false,
      importDialogFormVisible: false,
      dataObj: { },
      elUploadHeaders: { 'Authorization': 'Bearer ' + getToken() },
      advancedSearch: false,
      dialogStatus: '',
      textMap: {
        update: this.$t('common.edit'),
        create: this.$t('common.add')
      },
      rules: {
        roleCode: [{ required: true, validator: this.checkRoleCode, trigger: ['blur', 'change'] }],
        roleName: [{ required: true, validator: this.checkRoleName, trigger: ['blur', 'change'] }]
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
    clickSwitch() {
      this.advancedSearch = !this.advancedSearch
    },
    getList() {
      this.listLoading = true
      querySysRole(this.listQuery).then(response => {
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
      this.listQuery.roleName = undefined
      this.listQuery.roleCode = undefined
      this.listQuery.roleDescription = undefined
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
    handleSelectionChangeUrl(val) {
      this.multipleSelectionUrl = val
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
          addSysRole(this.temp).then(response => {
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
          tempData.roleCode = this.temp.roleCode
          tempData.roleName = this.temp.roleName
          tempData.roleDescription = this.temp.roleDescription
          this.disabledStatus = true
          updateSysRole(tempData).then(response => {
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
      deleteSysRole(deleteArray).then(response => {
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
    handleLeftIcon() {
      this.$refs.asideDiv.style.width = '10px'
      this.$refs.asideDiv.style.minWidth = '10px'
      this.$refs.mainDiv.style.width = '98%'
      this.leftIcon = false
      this.rightIcon = true
    },
    handleRightIcon() {
      this.$refs.asideDiv.style.width = '130px'
      this.$refs.asideDiv.style.minWidth = '130px'
      this.$refs.mainDiv.style.width = '82.5%'
      this.leftIcon = true
      this.rightIcon = false
    },
    handleAsideFilter() {
      querySysMenu(this.allListQuery).then(response => {
        this.allList = response.data.list
      })
    },
    clickAsideData(row, event, column) {
      this.urlListQuery.menuCode = row.menuCode
      querySysUrl(this.urlListQuery).then(response => {
        this.urlList = response.data.list
        this.urlList.forEach(row => {
          this.$nextTick(function() {
            if (this.checkedUrlList.indexOf(row.id) !== -1) {
              this.$refs.urlTable.toggleRowSelection(row)
            }
          })
        })
      })
    },
    getUrlList() {
      querySysUrl(this.urlListQuery).then(response => {
        this.urlList = response.data.list
        this.urlList.forEach(row => {
          this.$nextTick(function() {
            if (this.checkedUrlList.indexOf(row.id) !== -1) {
              this.$refs.urlTable.toggleRowSelection(row)
            }
          })
        })
      })
    },
    handleResetUrl() {
      this.urlListQuery.menuCode = undefined
      this.urlListQuery.url = undefined
      this.urlListQuery.description = undefined
      this.getUrlList()
      this.allListQuery.menuName = undefined
      querySysMenu(this.allListQuery).then(response => {
        this.allList = response.data.list
      })
    },
    createAuthorizeUrl() {
      this.urlTemp = {}
      const urlList = []
      for (let i = 0; i < this.multipleSelectionUrl.length; i++) {
        urlList.push(this.multipleSelectionUrl[i].id)
      }
      this.urlTemp.urlId = urlList.toString()
      this.urlTemp.roleId = this.multipleSelection[0].id
      authorizeUrlToRole(qs.stringify(this.urlTemp)).then(response => {
        if (response.message === 'success') {
          this.authorizeUrlDialogFormVisible = false
          this.$message({
            message: this.$t('common.authorize-success'),
            type: 'success',
            duration: 2000
          })
        } else {
          this.handleWarning(response)
        }
      })
    },
    handleCommand(command) {
      if (this.multipleSelection.length === 0) {
        this.handleWarning(this.$t('common.check-required'))
        return
      }
      if (this.multipleSelection.length > 1) {
        this.handleWarning(this.$t('common.check-one'))
        return
      }
      if (command === 'menu') {
        this.handleAuthorizeMenu(this.multipleSelection[0].id)
      } else if (command === 'button') {
        this.handleAuthorizeMenuButton(this.multipleSelection[0].roleCode)
      } else if (command === 'url') {
        querySysMenu(this.allListQuery).then(response => {
          this.allList = response.data.list
        })
        queryUrlIdByRoleId(this.multipleSelection[0].id).then(response => {
          this.checkedUrlList = response
          this.authorizeUrlDialogFormVisible = true
          this.getUrlList()
        })
      }
    },
    handleAuthorizeMenu(roleId) {
      this.roleId = roleId
      querySysMenuTree().then(response => {
        this.menuTree = response.data
      })
      queryMenuIdByRoleId(roleId).then(response => {
        this.$refs.menuTreeRef.setCheckedKeys(response.data)
      })
      this.menuTreeDialogFormVisible = true
    },
    createAuthorizeMenu() {
      this.temp.roleId = this.roleId
      this.temp.menuId = this.$refs.menuTreeRef.getCheckedKeys().join(',')
      authorizeMenuToRole(qs.stringify(this.temp)).then(response => {
        if (response.message === 'success') {
          this.menuTreeDialogFormVisible = false
          this.$message({
            message: this.$t('common.authorize-success'),
            type: 'success',
            duration: 2000
          })
        } else {
          this.handleWarning(response)
        }
      })
    },
    handleAuthorizeMenuButton(roleCode) {
      this.roleCode = roleCode
      querySysMenuButtonTree().then(response => {
        this.menuButtonTree = response.data
      })
      queryMenuButtonByRoleCode(roleCode).then(response => {
        this.$refs.menuButtonTreeRef.setCheckedKeys(response.data)
      })
      this.menuButtonTreeDialogFormVisible = true
    },
    createAuthorizeMenuButton() {
      this.temp.roleCode = this.roleCode
      this.temp.menuButton = this.$refs.menuButtonTreeRef.getCheckedKeys().join(',')
      authorizeMenuButtonToRole(qs.stringify(this.temp)).then(response => {
        if (response.message === 'success') {
          this.menuButtonTreeDialogFormVisible = false
          this.$message({
            message: this.$t('common.authorize-success'),
            type: 'success',
            duration: 2000
          })
        } else {
          this.handleWarning(response)
        }
      })
    },
    handleUserCommand(command) {
      if (this.multipleSelection.length === 0) {
        this.handleWarning(this.$t('common.check-required'))
        return
      }
      if (this.multipleSelection.length > 1) {
        this.handleWarning(this.$t('common.check-one'))
        return
      }
      if (command === 'view') {
        this.singleMulti = this.multipleSelection[0].id
        this.viewUserDialogFormVisible = true
      } else if (command === 'assign') {
        this.handleAuthorizeUser()
      }
    },
    hideSysUserComponent(e) {
      if (e === 1) {
        this.viewUserDialogFormVisible = false
      }
    },
    sysUserComponentMsg(e) {
      const usernameArray = []
      for (const data of e) {
        usernameArray.push(data.username)
      }
    },
    handleAuthorizeUser() {
      if (this.multipleSelection.length === 0) {
        this.handleWarning(this.$t('sysrole.check-user'))
        return
      }
      if (this.multipleSelection.length > 1) {
        this.handleWarning(this.$t('common.check-one'))
        return
      }
      this.orgUserTree = []
      this.assignOrgUserTree = []
      // assign为0是未分配，为1是已分配
      queryOrgUserTree(this.multipleSelection[0].id, 0).then(response => {
        this.orgUserTree = response.data
        queryOrgUserTree(this.multipleSelection[0].id, 1).then(assignResponse => {
          this.assignOrgUserTree = assignResponse.data
          this.authorizeUserDialogFormVisible = true
        })
      })
    },
    addTransfer(fromData, toData, obj) { // 监听穿梭框组件添加
      this.toUserData.push(fromData)
    },
    removeTransfer(fromData, toData, obj) { // 监听穿梭框组件移除
      this.toUserData.splice(toData)
    },
    createUser(toData) {
      const res = []
      toData.forEach(data => {
        const tmp = { ...data }
        if (tmp.children) {
          tmp.children = this.createUser(tmp.children)
        }
        if (tmp.label.split('-').length > 1) {
          res.push(tmp.id)
        }
      })
      if (res.length > 0) {
        this.toUserData.push(res)
      }
    },
    createAuthorizeUserButton() {
      this.toUserData = []
      this.createUser(this.assignOrgUserTree)

      if (this.toUserData.length === 0) {
        this.handleWarning(this.$t('sysrole.user-required'))
        return
      }

      const tempData = {}
      tempData.userId = this.toUserData
      tempData.roleId = this.multipleSelection[0].id
      authorizeUserToRole(tempData).then(response => {
        if (response.message === 'success') {
          this.authorizeUserDialogFormVisible = false
          this.$message({
            message: this.$t('sysrole.assign-user') + this.$t('common.success'),
            type: 'success',
            duration: 2000
          })
        } else {
          this.handleWarning(response)
        }
      })
    },
    handlePrint() {
      const listQueryData = this.handleCommonQuery()
      exportWordSysRole(qs.stringify(listQueryData)).then(response => {
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
      listQueryData.roleName = this.listQuery.roleName
      listQueryData.roleCode = this.listQuery.roleCode
      listQueryData.roleDescription = this.listQuery.roleDescription
      return listQueryData
    },
    handleDownload(isTemplate) { // 导出 Excel
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      listQueryData.isTemplate = isTemplate // 1为模板，0不为模板
      exportSysRole(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.sysrole') + '.xlsx')
      })
      this.downloadLoading = false
    },
    handleDownloadWord() { // 导出 Word
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportWordSysRole(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.sysrole') + '.docx')
      })
      this.downloadLoading = false
    },
    handleDownloadPDF() { // 导出 PDF
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportPDFSysRole(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.sysrole') + '.pdf')
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
    checkRoleCode(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysrole.roleCode-required')))
      } else {
        callback()
      }
    },
    checkRoleName(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysrole.roleName-required')))
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
  .allQueryClass input.el-input__inner {
    padding: 10px;
  }
</style>
