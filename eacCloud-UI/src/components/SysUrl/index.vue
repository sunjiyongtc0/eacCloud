<template>
  <div class="app-container">
    <el-container>
      <div ref="asideDiv" style="width: 160px; height: 595px; overflow: scroll; margin-right: 5px; border: 1px solid #C0D4F0">
        <el-aside style="background-color: white; padding: 0px 5px 0px 0px">
          <div style="width: 130px; padding: 10px 10px 0px 10px" class="allQueryClass">
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
      <div style="width: 8px; height: 595px; margin-right: 5px; background-color: #C0D4F0">
        <i v-if="leftIcon" class="el-icon-caret-left" style="position: relative; top: 50%; left: -3px; cursor: pointer; color: #97A8BE" @click="handleLeftIcon" />
        <i v-if="rightIcon" class="el-icon-caret-right" style="position: relative; top: 50%; left: -5px; cursor: pointer; color: #97A8BE" @click="handleRightIcon" />
      </div>
      <div ref="mainDiv" style="width: 82%">
        <div class="filter-container">
          <el-input v-model="listQuery.url" :placeholder="$t('sysurl.url')" clearable class="filter-input filter-item" style="width: 150px" @keyup.enter.native="handleFilter" />
          <el-input v-model="listQuery.description" :placeholder="$t('sysurl.description')" clearable class="filter-input filter-item" style="width: 150px" @keyup.enter.native="handleFilter" />
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
          <el-table-column :label="$t('sysurl.url')" prop="url" min-width="100px" />
          <el-table-column :label="$t('sysurl.description')" prop="description" min-width="100px" />
          <el-table-column :label="$t('common.create-time')" prop="createTime" sortable="custom" :class-name="getSortClass('createTime')">
            <template slot-scope="{row}">
              <span>{{ row.createTime | formatDate }}</span>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="listQuery.currentPage" :limit.sync="listQuery.pageSize" @pagination="getList" />
      </div>
    </el-container>
    <div slot="footer" class="dialog-footer" style="float: right; padding-top: 5px">
      <el-button @click="hideSysUrlComponent()">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="createSysUrlComponentMsg()">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </div>
</template>

<script>
import { querySysUrl, querySysMenu } from '@/api/admin'
import waves from '@/directive/waves' // waves directive
import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'SysUrl',
  components: { Pagination },
  directives: { waves, elDragDialog },
  props: ['msg'], // eslint-disable-line
  data() {
    return {
      leftIcon: true,
      rightIcon: false,
      tableKey: 0,
      labelPosition: 'right',
      list: [],
      allList: [],
      total: 0,
      listLoading: true,
      listQuery: {
        currentPage: 1,
        pageSize: 10,
        url: undefined,
        description: undefined,
        menuCode: undefined,
        roleId: undefined,
        sorter: 'createTime_descend'
      },
      allListQuery: {
        currentPage: 1,
        pageSize: 10000
      },
      multipleSelection: []
    }
  },
  watch: {
    msg(oldValue, newValue) {
      if (oldValue) {
        this.listQuery.roleId = oldValue
        this.getList()
      }
    }
  },
  created() {
    if (window.innerWidth < 700) {
      this.labelPosition = 'top'
    }
    if (this.msg) {
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
      this.$refs.asideDiv.style.width = '140px'
      this.$refs.asideDiv.style.minWidth = '130px'
      this.$refs.mainDiv.style.width = '82%'
      this.leftIcon = true
      this.rightIcon = false
    },
    handleAsideFilter() {
      querySysMenu(this.allListQuery).then(response => {
        this.allList = response.data.list
      })
    },
    clickAsideData(row, event, column) {
      this.listQuery.menuCode = row.menuCode
      this.listLoading = true
      querySysUrl(this.listQuery).then(response => {
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
      querySysUrl(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.pagination.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.currentPage = 1
      this.listQuery.menuCode = undefined
      this.getList()
    },
    handleReset() {
      this.listQuery.url = undefined
      this.listQuery.description = undefined
      this.listQuery.menuCode = undefined
      this.handleFilter()
      this.allListQuery.menuName = undefined
      querySysMenu(this.allListQuery).then(response => {
        this.allList = response.data.list
      })
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
    hideSysUrlComponent() {
      this.$emit('hideSysUrlComponent', 1)
    },
    createSysUrlComponentMsg() {
      this.$emit('sysUrlComponentMsg', this.multipleSelection)
      this.$emit('hideSysUrlComponent', 1)
    },
    getSortClass: function(key) {
      const sorter = this.listQuery.sorter
      return sorter === `${key}_ascend` ? 'ascending' : 'descending'
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
<style scoped>
  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
  .el-table .cell {
    white-space: nowrap;
    text-overflow: unset;
    margin-right: 10px;
  }
  .pagination-container[data-v-72233bcd] {
    padding: 0
  }
</style>
