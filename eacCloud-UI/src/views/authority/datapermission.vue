<template>
  <div class="app-container">
    <el-container>
      <div ref="asideDiv" style="width: 173px; height: 675px; overflow: scroll; margin-right: 5px; border: 1px solid #C0D4F0">
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
      <div style="width: 8px; height: 675px; margin-right: 5px; background-color: #C0D4F0">
        <i v-if="leftIcon" class="el-icon-caret-left" style="position: relative; top: 50%; left: -3px; cursor: pointer; color: #97A8BE" @click="handleLeftIcon" />
        <i v-if="rightIcon" class="el-icon-caret-right" style="position: relative; top: 50%; left: -5px; cursor: pointer; color: #97A8BE" @click="handleRightIcon" />
      </div>
      <div ref="mainDiv" style="width: 87%">
        <div class="filter-container">
          <el-input v-model="listQuery.menuName" :placeholder="$t('sysmenu.menuName')" clearable class="filter-input filter-item" @keyup.enter.native="handleFilter" />
          <el-input v-model="listQuery.menuCode" :placeholder="$t('sysmenu.menuCode')" clearable class="filter-input filter-item" @keyup.enter.native="handleFilter" />
          <el-input v-model="listQuery.menuIcon" :placeholder="$t('sysmenu.menuIcon')" clearable class="filter-input filter-item" @keyup.enter.native="handleFilter" />
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
          </div>
          <div v-if="advancedSearch" style="float: left">
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
            <template slot-scope="{row}">
              <el-button type="text" style="margin-left: 10px" @click="handleAuthorize(row.menuCode)">
                <i class="el-icon-connection" />{{ $t('common.authorize') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="listQuery.currentPage" :limit.sync="listQuery.pageSize" @pagination="getList" />
      </div>
    </el-container>

    <el-drawer :title="$t('sysmenu.dataTitle')" :visible.sync="drawerTableVisible" direction="rtl" size="60%">
      <el-button style="margin-left: 10px" type="primary" icon="el-icon-plus" @click="handleCreate">
        {{ $t('common.add') }}
      </el-button>
      <el-table :data="roleFieldDataSourceList">
        <el-table-column :label="$t('sysrole.roleCode')" prop="roleCode" min-width="30%" />
        <el-table-column :label="$t('sysmenu.dataFilter')" prop="dataField" />
        <el-table-column :label="$t('common.operate')" align="center" min-width="30%">
          <template slot-scope="{row,$index}">
            <el-button type="text" @click="handleUpdate(row)">
              <i class="el-icon-edit-outline" />{{ $t('common.edit') }}
            </el-button>
            <el-popover :ref="'popover-' + row.roleCode" placement="top" width="160" :title="$t('common.confirm-delete')" trigger="click">
              <div>
                <el-button size="mini" @click="$refs[`popover-` + row.roleCode].doClose()">{{ $t('common.cancel') }}</el-button>
                <el-button type="primary" size="mini" @click="$refs[`popover-` + row.roleCode].doClose();handleDelete(row,$index)">{{ $t('common.confirm') }}</el-button>
              </div>
              <el-button slot="reference" type="text" style="margin-left: 10px">
                <i class="el-icon-delete" />{{ $t('common.delete') }}
              </el-button>
            </el-popover>
          </template>
        </el-table-column>
      </el-table>
    </el-drawer>

    <el-dialog v-el-drag-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" :label-position="labelPosition" label-width="210px" style="width: 100%">
        <el-form-item :label="$t('sysrole.roleCode')" prop="roleCode">
          <el-select v-model="temp.roleCode" :disabled="dialogStatus==='update'" :placeholder="$t('common.choose')" clearable style="width: 100%">
            <el-option v-for="item in roleCodeList" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('sysmenu.dataFilter')" prop="dataField">
          <el-input v-model="temp.dataField" :placeholder="$t('common.enter')" clearable />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          {{ $t('common.cancel') }}
        </el-button>
        <el-button type="primary" @click="createData()">
          {{ $t('common.confirm') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { querySysMenu, queryRoleCode, queryDataFieldByMenuCode, authorizeMenuDataToRole, deleteDataField } from '@/api/admin'
import waves from '@/directive/waves' // waves directive
import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import qs from 'qs'

export default {
  name: 'DataPermission',
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
      list: [],
      allList: [],
      total: 0,
      listLoading: true,
      listQuery: {
        currentPage: 1,
        pageSize: 10,
        menuName: undefined,
        menuCode: undefined,
        menuIcon: undefined,
        menuStatus: undefined,
        menuPath: undefined
      },
      allListQuery: {
        currentPage: 1,
        pageSize: 10000
      },
      multipleSelection: [],
      drawerTableVisible: false,
      roleCodeList: [],
      roleFieldDataSourceList: [],
      labelPosition: 'right',
      roleFieldDataSourceCount: 0,
      temp: {
        id: undefined
      },
      dialogStatus: '',
      textMap: {
        update: this.$t('common.edit'),
        create: this.$t('common.add')
      },
      rules: {
        roleCode: [{ required: true, validator: this.checkRoleCode, trigger: ['blur', 'change'] }],
        dataField: [{ required: true, validator: this.checkDataField, trigger: ['blur', 'change'] }]
      },
      dialogFormVisible: false,
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
    handleAuthorize(menuCode) {
      this.menuCode = menuCode
      queryRoleCode().then(response => {
        this.roleCodeList = response.data
      })
      queryDataFieldByMenuCode(menuCode).then(response => {
        this.roleFieldDataSourceList = response.data.list
        this.roleFieldDataSourceCount = response.data.count
      })
      this.drawerTableVisible = true
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
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.menuCode = this.menuCode
          authorizeMenuDataToRole(qs.stringify(this.temp)).then(response => {
            if (response.message === 'success') {
              this.dialogFormVisible = false
              queryDataFieldByMenuCode(this.menuCode).then(response => {
                this.roleFieldDataSourceList = response.data.list
                this.roleFieldDataSourceCount = response.data.count
              })
            } else {
              this.handleWarning(response)
            }
          })
        }
      })
    },
    handleDelete(row, index) {
      this.temp.roleCode = row.roleCode
      this.temp.menuCode = this.menuCode
      deleteDataField(qs.stringify(this.temp)).then(response => {
        if (response.message === 'success') {
          this.$message({
            message: this.$t('common.delete-success'),
            type: 'success',
            duration: 2000
          })
          this.roleFieldDataSourceList.splice(index, 1)
        } else {
          this.handleWarning(response)
        }
      })
    },
    checkRoleCode(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysmenu.roleCode-required')))
      } else {
        callback()
      }
    },
    checkDataField(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('sysmenu.dataField-required')))
      } else {
        callback()
      }
    },
    handleWarning(response) {
      this.$message({
        message: response.message || response,
        type: 'warning',
        duration: 2000
      })
    }
  }
}
</script>
<style>
  .el-drawer__header {
    padding: 10px;
  }
  .aside-table td,.aside-table th.is-leaf {
    border: none;
  }
  .aside-table::before {
    width: 0;
  }
</style>
