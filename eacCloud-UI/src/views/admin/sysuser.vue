<template>
  <div class="app-container">
    <el-container>
      <div ref="asideDiv" style="width: 120px; height: 935px; overflow: scroll; margin-right: 5px; border: 1px solid #C0D4F0">
        <el-aside style="background-color: white; padding: 0px 5px 0px 0px">
          <div style="width: 120px; padding: 10px 10px 0px 10px" class="allQueryClass">
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
      <div style="width: 8px; height: 935px; margin-right: 5px; background-color: #C0D4F0">
        <i v-if="leftIcon" class="el-icon-caret-left" style="position: relative; top: 50%; left: -3px; cursor: pointer; color: #97A8BE" @click="handleLeftIcon" />
        <i v-if="rightIcon" class="el-icon-caret-right" style="position: relative; top: 50%; left: -5px; cursor: pointer; color: #97A8BE" @click="handleRightIcon" />
      </div>
      <div ref="mainDiv" style="width: 87%; overflow: scroll">
        <div class="filter-container">
          <el-input v-model="listQuery.username" :placeholder="$t('sysuser.username')" clearable class="filter-input filter-item" style="width: 100px" @keyup.enter.native="handleFilter" />
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
            <el-dropdown split-button type="primary" class="filter-item" style="padding-left: 5px; padding-right: 5px;" trigger="click" @command="handleRoleUrl">
              <i class="el-icon-postcard" style="margin-right: 5px" /> {{ $t('sysuser.role-url') }}
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="grant"><i class="el-icon-postcard" style="margin-right: 5px" />{{ $t('sysuser.grant-role') }}</el-dropdown-item>
                <el-dropdown-item command="view"><i class="el-icon-set-up" style="margin-right: 5px" />{{ $t('sysuser.view-url') }}</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <el-button type="primary" class="filter-item" style="margin-right: 5px;" icon="el-icon-upload" @click="handleImport">
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
            <el-input v-model="listQuery.nickname" :placeholder="$t('sysuser.nickname')" clearable class="filter-input filter-item" style="width: 150px" @keyup.enter.native="handleFilter" />
            <el-input v-model="listQuery.mobile" :placeholder="$t('sysuser.mobile')" clearable class="filter-input filter-item" style="width: 150px" @keyup.enter.native="handleFilter" />
            <el-select v-model="listQuery.roleId" :placeholder="$t('sysuser.roleId')" clearable class="filter-input filter-item" style="width: 150px">
              <el-option v-for="item in roleOptions" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
            <el-select v-model="listQuery.status" :placeholder="$t('sysuser.status')" clearable class="filter-input filter-item" style="width: 150px">
              <el-option v-for="item in statusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
            </el-select>
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
          :row-style="{height:'30px'}"
          :cell-style="{padding:'0px'}"
          :header-cell-style="{padding:'0px'}"
          style="width: 100%"
          @sort-change="sortChange"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="40" />
          <el-table-column :label="$t('sysuser.username')" prop="username" />
          <el-table-column :label="$t('sysuser.nickname')" prop="nickname" />
          <el-table-column :label="$t('sysuser.mobile')" prop="mobile" min-width="90px" />
          <el-table-column :label="$t('sysuser.roleId')" prop="roleIdCn" />
          <el-table-column :label="$t('sysuser.orgId')" prop="orgIdCn" />
          <el-table-column :label="$t('sysuser.status')" class-name="status-col">
            <template slot-scope="{row}">
              <el-tag :type="row.status | statusFilter">
                <span v-if="row.status === 1">{{ $t('common.able') }}</span>
                <span v-else>{{ $t('common.disable') }}</span>
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column :label="$t('common.create-time')" min-width="120px" prop="createTime" sortable="custom" :class-name="getSortClass('createTime')">
            <template slot-scope="{row}">
              <span>{{ row.createTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('common.operate')" align="center" min-width="100px">
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

        <el-tabs v-model="activeName" style="width: 100%" type="border-card">
          <el-tab-pane v-for="item in tabMapOptions" :key="item.key" :label="item.label" :name="item.key">
            <keep-alive>
              <sysuserpost v-if="activeName==item.key" :msg="parentMsg" :type="item.key" />
            </keep-alive>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-container>

    <el-dialog v-el-drag-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" :label-position="labelPosition" label-width="90px" style="width: 100%">
        <el-form-item :label="$t('sysuser.username')" prop="username">
          <el-input v-model="temp.username" :placeholder="$t('common.enter')" :disabled="dialogStatus==='update'" minlength="3" maxlength="64" clearable />
        </el-form-item>
        <el-form-item v-if="dialogStatus==='create'" :label="$t('sysuser.password')" prop="password">
          <el-input v-model="temp.password" :placeholder="$t('common.enter')" show-password minlength="6" maxlength="31" clearable />
        </el-form-item>
        <el-form-item :label="$t('sysuser.email')" prop="email">
          <el-input v-model="temp.email" :placeholder="$t('common.enter')" maxlength="100" clearable />
        </el-form-item>
        <el-form-item :label="$t('sysuser.mobile')">
          <el-row>
            <el-col :span="4" :xs="24">
              <el-select v-model="temp.prefix" :placeholder="$t('common.choose')">
                <el-option label="+86" value="86" />
                <el-option label="+87" value="87" />
              </el-select>
            </el-col>
            <el-col :span="20" :xs="24">
              <el-form-item prop="mobile">
                <el-input v-model="temp.mobile" :placeholder="$t('common.enter')" class="input-with-select" maxlength="20" clearable />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item :label="$t('sysuser.orgId')" prop="orgId">
          <el-select ref="selectOrgId" v-model="temp.orgId" :placeholder="$t('common.choose')" style="width: 100%" clearable @clear="handleClearOrgId">
            <el-option :key="temp.orgId" hidden :value="temp.orgId" :label="orgName" />
            <el-tree :data="orgIdOptions" :props="{label: 'title'}" :expand-on-click-node="false" :check-on-click-node="true" @node-click="handleNodeClickOrgId" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('sysuser.status')" prop="status">
          <el-radio-group v-model="temp.status">
            <el-radio :label="1">{{ $t('common.able') }}</el-radio>
            <el-radio :label="0">{{ $t('common.disable') }}</el-radio>
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

    <el-dialog v-el-drag-dialog :title="$t('sysuser.grant-role')" :visible.sync="authorizeRoleDialogFormVisible" width="620px">
      <el-select v-model="temp.postCode" :placeholder="$t('syspost.choose-post')" style="width: 97%" clearable @change="handleNodeClickParentId">
        <el-option v-for="item in postCodeOptions" :key="item.value" :label="item.name" :value="item.value" />
      </el-select>
      <br>
      <br>
      <el-transfer v-model="grantRoleList" :data="roleList" filterable :props="{label: 'name', key: 'value'}" :filter-placeholder="$t('sysuser.filter-roleName')" :titles="[$t('sysuser.unauthorized-role'), $t('sysuser.authorized-role')]" />

      <div slot="footer" class="dialog-footer">
        <el-button @click="authorizeRoleDialogFormVisible = false">
          {{ $t('common.cancel') }}
        </el-button>
        <el-button type="primary" @click="createAuthorizeRoleButton()">
          {{ $t('common.confirm') }}
        </el-button>
      </div>
    </el-dialog>

    <el-dialog v-el-drag-dialog :title="$t('sysuser.view-url')" :visible.sync="viewUrlDialogFormVisible" width="68%">
      <sysurl-component :msg="singleMulti" @hideSysUrlComponent="hideSysUrlComponent" @sysUrlComponentMsg="sysUrlComponentMsg" />
    </el-dialog>

    <el-dialog v-el-drag-dialog :title="$t('common.import')" :visible.sync="importDialogFormVisible">
      <p>{{ $t('common.download-template') }}: <a style="color: #1890ff" @click="handleDownload(1)">{{ this.$t('route.sysuser') + '.xlsx' }}</a></p>
      <center>
        <el-upload ref="upload" :data="dataObj" :headers="elUploadHeaders" action="/api/admin/sysuser/importSysUser" :auto-upload="false" :on-success="handleSuccess" drag>
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
import { querySysUser, querySysUserPostTree, addSysUser, updateSysUser, queryRoleName, querySysOrg, querySysOrgTree, deleteSysUser, exportSysUser, exportWordSysUser, exportPDFSysUser, authorizeRoleToUser } from '@/api/admin'
import waves from '@/directive/waves' // waves directive
import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import SysUrlComponent from '@/components/SysUrl'
import sysuserpost from './components/sysuserpost'
import qs from 'qs'
import { saveAs } from 'file-saver'
import mammoth from 'mammoth'
import { getToken } from '@/utils/auth'

export default {
  name: 'SysUser',
  components: { Pagination, sysuserpost, 'sysurl-component': SysUrlComponent },
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
  data() {
    return {
      tabMapOptions: [
        { label: this.$t('sysuserpost.postInfo'), key: 'sysuserpost' }
      ],
      activeName: 'sysuserpost',
      parentMsg: [],
      leftIcon: true,
      rightIcon: false,
      tableKey: 0,
      labelPosition: 'right',
      list: [],
      allList: [],
      total: 0,
      singleMulti: undefined,
      listLoading: true,
      disabledStatus: false,
      listQuery: {
        currentPage: 1,
        pageSize: 10,
        username: undefined,
        nickname: undefined,
        mobile: undefined,
        roleId: undefined,
        status: undefined,
        orgId: undefined,
        orgName: undefined,
        sorter: 'createTime_descend'
      },
      allListQuery: {
        currentPage: 1,
        pageSize: 10000
      },
      roleList: [],
      grantRoleList: [],
      orgIdOptions: [],
      postCodeOptions: [],
      roleOptions: [],
      orgName: '',
      statusOptions: [
        { key: '1', display_name: this.$t('common.able') },
        { key: '0', display_name: this.$t('common.disable') }
      ],
      temp: {
        id: undefined,
        status: 1,
        prefix: '86'
      },
      multipleSelection: [],
      dialogFormVisible: false,
      authorizeRoleDialogFormVisible: false,
      viewUrlDialogFormVisible: false,
      importDialogFormVisible: false,
      dataObj: { },
      elUploadHeaders: { 'Authorization': 'Bearer ' + getToken() },
      dialogStatus: '',
      textMap: {
        update: this.$t('common.edit'),
        create: this.$t('common.add')
      },
      rules: {
        username: [{ required: true, validator: this.checkUsername, trigger: ['blur', 'change'] }],
        password: [{ required: true, validator: this.checkPassword, trigger: ['blur', 'change'] }],
        email: [{ required: true, type: 'email', validator: this.checkEmail, trigger: ['blur', 'change'] }],
        mobile: [{ required: true, validator: this.checkMobile, trigger: ['blur', 'change'] }],
        orgId: [{ required: true, validator: this.checkOrgId, trigger: 'change' }],
        status: [{ required: true }]
      },
      downloadLoading: false,
      advancedSearch: false
    }
  },
  created() {
    if (window.innerWidth < 700) {
      this.labelPosition = 'top'
    }
    this.getInitList()
  },
  methods: {
    clickSwitch() {
      this.advancedSearch = !this.advancedSearch
    },
    getInitList() {
      querySysOrgTree().then(response => {
        this.orgIdOptions = response.data.list
      })
      queryRoleName().then(response => {
        this.roleOptions = response.data
      })
      this.getList()
    },
    getList() {
      this.listLoading = true
      querySysOrg(this.allListQuery).then(response => {
        this.allList = response.data.list
      })
      querySysUser(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.pagination.total
        this.listLoading = false
        this.$nextTick(() => {
          if (this.list && this.list.length > 0) {
            this.$refs.multipleTable.toggleRowSelection(this.list[this.list.length - 1], true)
          }
        })
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
      this.$refs.asideDiv.style.width = '120px'
      this.$refs.asideDiv.style.minWidth = '120px'
      this.$refs.mainDiv.style.width = '88%'
      this.leftIcon = true
      this.rightIcon = false
    },
    handleAsideFilter() {
      querySysOrg(this.allListQuery).then(response => {
        this.allList = response.data.list
      })
    },
    clickAsideData(row, event, column) {
      this.listQuery.orgId = row.id
      this.listQuery.orgName = row.orgName
      this.listLoading = true
      querySysUser(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.pagination.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.currentPage = 1
      this.listQuery.orgId = undefined
      this.listQuery.orgName = undefined
      this.getList()
    },
    handleReset() {
      this.listQuery.username = undefined
      this.listQuery.nickname = undefined
      this.listQuery.mobile = undefined
      this.listQuery.roleId = undefined
      this.listQuery.status = undefined
      this.listQuery.orgId = undefined
      this.listQuery.orgName = undefined
      this.getList()
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
      if (val.length > 1) {
        this.$refs.multipleTable.clearSelection()
        this.$refs.multipleTable.toggleRowSelection(val.pop())
      } else {
        this.multipleSelection = val
        this.parentMsg = val
      }
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        status: 1,
        prefix: '86'
      }
    },
    handleCreate() {
      this.resetTemp()
      this.orgName = undefined
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      if (this.$refs.selectOrgId.value) {
        this.validateOrgId()
      }
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.disabledStatus = true
          addSysUser(this.temp).then(response => {
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
      if (row.roleId) {
        this.temp.roleId = row.roleId.split(',')
      }
      this.orgName = this.temp.orgIdCn
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      if (this.$refs.selectOrgId.value) {
        this.validateOrgId()
      }
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // const tempData = Object.assign({}, this.temp)
          const tempData = {}
          tempData.id = this.temp.id
          tempData.username = this.temp.username
          tempData.email = this.temp.email
          tempData.mobile = this.temp.mobile
          tempData.prefix = this.temp.prefix
          tempData.roleId = this.temp.roleId
          tempData.orgId = this.temp.orgId
          tempData.status = this.temp.status
          this.disabledStatus = true
          updateSysUser(tempData).then(response => {
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
    handleRoleUrl(command) {
      if (this.multipleSelection.length === 0) {
        this.handleWarning(this.$t('common.check-required'))
        return
      }
      if (this.multipleSelection.length > 1) {
        this.handleWarning(this.$t('common.check-one'))
        return
      }
      if (command === 'view') {
        this.singleMulti = this.multipleSelection[0].roleId
        this.viewUrlDialogFormVisible = true
      } else if (command === 'grant') {
        this.handleAuthorizeRole()
      }
    },
    hideSysUrlComponent(e) {
      if (e === 1) {
        this.viewUrlDialogFormVisible = false
      }
    },
    sysUrlComponentMsg(e) {
      const urlArray = []
      for (const data of e) {
        urlArray.push(data.id)
      }
    },
    handleAuthorizeRole() {
      this.temp.postCode = this.multipleSelection[0].postCode
      queryRoleName().then(response => {
        this.roleList = response.data
        queryRoleName(this.multipleSelection[0].id, this.temp.postCode, 1).then(assignResponse => {
          const assignData = []
          assignResponse.data.map(item => { assignData.push(item.value) })
          this.grantRoleList = assignData
          querySysUserPostTree(this.multipleSelection[0].id).then(sysUserPostResponse => {
            this.postCodeOptions = sysUserPostResponse.data
            this.authorizeRoleDialogFormVisible = true
          })
        })
      })
    },
    createAuthorizeRoleButton() {
      if (!this.temp.postCode) {
        this.handleWarning(this.$t('syspost.choosepost-required'))
        return
      }
      if (this.grantRoleList.length === 0) {
        this.handleWarning(this.$t('sysuser.role-required'))
        return
      }

      const tempData = {}
      tempData.userId = this.multipleSelection[0].id
      tempData.roleId = this.grantRoleList
      tempData.postCode = this.temp.postCode
      authorizeRoleToUser(tempData).then(response => {
        if (response.message === 'success') {
          this.authorizeRoleDialogFormVisible = false
          this.$message({
            message: this.$t('sysuser.grant-role') + this.$t('common.success'),
            type: 'success',
            duration: 2000
          })
          this.handleReset()
        } else {
          this.handleWarning(response)
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
      deleteSysUser(deleteArray).then(response => {
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
      exportWordSysUser(qs.stringify(listQueryData)).then(response => {
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
      listQueryData.username = this.listQuery.username
      listQueryData.nickname = this.listQuery.nickname
      listQueryData.mobile = this.listQuery.mobile
      listQueryData.roleId = this.listQuery.roleId
      listQueryData.status = this.listQuery.status
      return listQueryData
    },
    handleDownload(isTemplate) { // 导出 Excel
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      listQueryData.isTemplate = isTemplate // 1为模板，0不为模板
      exportSysUser(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.sysuser') + '.xlsx')
      })
      this.downloadLoading = false
    },
    handleDownloadWord() { // 导出 Word
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportWordSysUser(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.sysuser') + '.docx')
      })
      this.downloadLoading = false
    },
    handleDownloadPDF() { // 导出 PDF
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportPDFSysUser(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.sysuser') + '.pdf')
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
    checkUsername(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysuser.username-required')))
      } else {
        if (value.length > 2 && value.length < 65) {
          callback()
        } else {
          return callback(new Error(this.$t('sysuser.username-length')))
        }
      }
    },
    checkPassword(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysuser.password-required')))
      } else {
        if (value.length > 5 && value.length < 32) {
          callback()
        } else {
          return callback(new Error(this.$t('sysuser.password-length')))
        }
      }
    },
    checkEmail(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysuser.email-required')))
      } else {
        const reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
        if (reg.test(value)) {
          callback()
        } else {
          return callback(new Error(this.$t('sysuser.email-format')))
        }
      }
    },
    checkMobile(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysuser.mobile-required')))
      } else {
        const reg = /^1\d{10}$/
        if (reg.test(value)) {
          callback()
        } else {
          return callback(new Error(this.$t('sysuser.mobile-format')))
        }
      }
    },
    checkOrgId(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysuser.orgId-required')))
      } else {
        callback()
      }
    },
    validateOrgId() {
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate('orgId')
      })
    },
    handleNodeClickOrgId(data) {
      this.orgName = data.title
      this.temp.orgId = data.id
      this.$refs.selectOrgId.blur()
      this.validateOrgId()
    },
    handleClearOrgId() {
      this.orgName = ''
      this.temp.orgId = ''
    },
    handleNodeClickParentId(data) {
      queryRoleName(this.multipleSelection[0].id, data, 1).then(assignResponse => {
        const assignData = []
        assignResponse.data.map(item => { assignData.push(item.value) })
        this.grantRoleList = assignData
      })
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
  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
  .allQueryClass input.el-input__inner {
    padding: 10px;
  }
</style>
<style scoped>
  .pagination-container {
    padding: 10px;
    margin: 0px;
  }
</style>
