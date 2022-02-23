<template>
  <div class="app-container">
    <el-container style="width: 100%">
      <div ref="asideDiv" style="width: 150px; overflow: scroll; margin-right: 5px; border: 1px solid #C0D4F0">
        <el-aside style="background-color: white; padding: 0px 5px 0px 0px">
          <div style="width: 130px; padding: 10px 10px 0px 10px">
            <el-input v-model="orgName" :placeholder="$t('sysorg.orgName')" clearable />
          </div>
          <el-table
            row-key="id"
            :data="allOrgList"
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
      <div style="width: 8px; margin-right: 5px; background-color: #C0D4F0">
        <i v-if="leftIcon" class="el-icon-caret-left" style="position: relative; top: 50%; left: -3px; cursor: pointer; color: #97A8BE" @click="handleLeftIcon" />
        <i v-if="rightIcon" class="el-icon-caret-right" style="position: relative; top: 50%; left: -5px; cursor: pointer; color: #97A8BE" @click="handleRightIcon" />
      </div>
      <div ref="mainDiv" style="width: 87%">
        <div class="filter-container">
          <el-input v-model="listQuery.username" :placeholder="$t('sysuser.username')" clearable class="filter-input filter-item" @keyup.enter.native="handleFilter" />
          <el-select v-model="listQuery.status" :placeholder="$t('sysuser.status')" clearable class="filter-input filter-item">
            <el-option v-for="item in statusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
          <div style="float: right">
            <el-button v-waves type="primary" class="filter-item" icon="el-icon-search" @click="handleFilter">
              {{ $t('common.query') }}
            </el-button>
            <el-button v-waves type="primary" class="filter-item" icon="el-icon-refresh-right" @click="handleReset">
              {{ $t('common.reset') }}
            </el-button>
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
          <el-table-column :label="$t('sysuser.username')" prop="username" show-overflow-tooltip />
          <el-table-column :label="$t('sysuser.nickname')" prop="nickname" show-overflow-tooltip />
          <el-table-column :label="$t('sysuser.mobile')" prop="mobile" show-overflow-tooltip />
          <el-table-column :label="$t('sysuser.roleId')" prop="roleIdCn" show-overflow-tooltip />
          <el-table-column :label="$t('sysuser.orgId')" prop="orgIdCn" show-overflow-tooltip />
          <el-table-column :label="$t('common.create-time')" min-width="100px" prop="createTime" sortable="custom" :class-name="getSortClass('createTime')" show-overflow-tooltip>
            <template slot-scope="{row}">
              <span>{{ row.createTime | formatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('sysuser.status')" class-name="status-col">
            <template slot-scope="{row}">
              <el-tag :type="row.status | statusFilter">
                <span v-if="row.status === 1">{{ $t('common.able') }}</span>
                <span v-else>{{ $t('common.disable') }}</span>
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="listQuery.currentPage" :limit.sync="listQuery.pageSize" @pagination="getList" />
      </div>
    </el-container>
    <div slot="footer" class="dialog-footer" style="float: right">
      <el-button @click="hideSysUserComponent()">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="createSysUserComponentMsg()">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </div>
</template>

<script>
import { querySysUser, querySysOrg } from '@/api/admin'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'SysUserComponent',
  components: { Pagination },
  directives: { waves },
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
      leftIcon: true,
      rightIcon: false,
      tableKey: 0,
      labelPosition: 'right',
      list: [],
      allOrgList: this.$store.getters.orgs,
      total: 0,
      listLoading: true,
      listQuery: {
        currentPage: 1,
        pageSize: 10,
        username: undefined,
        status: undefined,
        orgId: undefined,
        orgName: undefined,
        sorter: 'createTime_descend'
      },
      orgName: undefined,
      statusOptions: [
        { key: '1', display_name: this.$t('common.able') },
        { key: '0', display_name: this.$t('common.disable') }
      ],
      multipleSelection: [],
      dialogStatus: undefined,
      textMap: {
        update: this.$t('common.edit'),
        create: this.$t('common.add')
      }
    }
  },
  watch: {
    msg(oldValue, newValue) {
      if (oldValue && !isNaN(oldValue)) {
        this.listQuery.roleId = oldValue
        this.getList()
      }
    },
    orgName(value) {
      if (!value) {
        this.allOrgList = this.$store.getters.orgs
      } else {
        querySysOrg({ 'orgName': value }).then(response => {
          this.allOrgList = response.data.list
        })
      }
    }
  },
  created() {
    if (window.innerWidth < 700) {
      this.labelPosition = 'top'
    }
    if (this.msg && !isNaN(this.msg)) {
      this.listQuery.roleId = this.msg
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
      this.$refs.asideDiv.style.width = '130px'
      this.$refs.asideDiv.style.minWidth = '130px'
      this.$refs.mainDiv.style.width = '82%'
      this.leftIcon = true
      this.rightIcon = false
    },
    getList() {
      this.listLoading = true
      querySysUser(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.pagination.total
        this.listLoading = false
      })
    },
    clickAsideData(row, event, column) {
      this.listQuery.orgId = row.id
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
      this.listQuery.status = undefined
      this.listQuery.orgId = undefined
      this.listQuery.orgName = undefined
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
      if (this.msg === 'single') {
        if (val.length > 1) {
          this.$refs.multipleTable.clearSelection()
          this.$refs.multipleTable.toggleRowSelection(val.pop())
        } else {
          this.multipleSelection = val
        }
      } else {
        this.multipleSelection = val
      }
    },
    hideSysUserComponent() {
      this.$emit('hideSysUserComponent', 1)
    },
    createSysUserComponentMsg() {
      this.$emit('sysUserComponentMsg', this.multipleSelection)
      this.$emit('hideSysUserComponent', 1)
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
<style scoped>
  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
  .el-table .cell {
    white-space: nowrap;
    text-overflow: unset;
    margin-right: 10px;
  }
  ::-webkit-scrollbar {
    width: 8px;
    height: 8px;
  }
  ::-webkit-scrollbar-thumb {
    background-color: #C0D4F0;
    border-radius: 3px;
  }
  .pagination-container[data-v-72233bcd] {
    padding: 0
  }
</style>
