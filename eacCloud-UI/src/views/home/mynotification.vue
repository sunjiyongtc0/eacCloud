<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.title" :placeholder="$t('notification.title')" clearable class="filter-input filter-item" @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.type" :placeholder="$t('notification.type')" clearable class="filter-input filter-item">
        <el-option v-for="item in typeOptions" :key="item.value" :label="item.name" :value="item.value" />
      </el-select>
      <el-select v-model="listQuery.priority" :placeholder="$t('notification.priority')" clearable class="filter-input filter-item">
        <el-option v-for="item in priorityOptions" :key="item.value" :label="item.name" :value="item.value" />
      </el-select>
      <el-input v-model="listQuery.publisher" :placeholder="$t('notification.publisher')" clearable class="filter-input filter-item" @keyup.enter.native="handleFilter" />
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
        <el-input v-model="listQuery.content" :placeholder="$t('notification.content')" clearable class="filter-input filter-item" @keyup.enter.native="handleFilter" />
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
      <el-table-column :label="$t('notification.title')" prop="title" />
      <el-table-column :label="$t('notification.type')" prop="typeCn" />
      <el-table-column :label="$t('notification.priority')" prop="priorityCn" />
      <el-table-column :label="$t('notification.publisher')" prop="publisher" />
      <el-table-column :label="$t('notification.create-time')" min-width="100px" prop="createTime" sortable="custom" :class-name="getSortClass('createTime')">
        <template slot-scope="{row}">
          <span>{{ row.createTime | formatDate }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operate')" align="center">
        <template slot-scope="{row}">
          <el-button type="text" @click="handleView(row)">
            <i class="el-icon-view" />{{ $t('common.view') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.currentPage" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-form ref="dataForm" :model="temp">
      <el-dialog v-el-drag-dialog :title="temp.title" :visible.sync="dialogFormVisible" width="850px">
        <el-row>
          <el-col :span="6">{{ $t('notification.publisher') }}：{{ temp.publisher }}</el-col>
          <el-col :span="18">{{ $t('notification.create-time') }}：{{ temp.createTime | formatDate }}</el-col>
        </el-row>
        <div style="overflow-x: auto; max-height: 500px" v-html="temp.content" />
      </el-dialog>
    </el-form>
  </div>
</template>

<script>
import { queryMyNotification, queryNotificationType, queryDictByDictType } from '@/api/admin'
import waves from '@/directive/waves' // waves directive
import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'MyNotification',
  components: { Pagination },
  directives: { waves, elDragDialog },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        currentPage: 1,
        pageSize: 10,
        title: undefined,
        content: undefined,
        type: undefined,
        priority: undefined,
        publisher: undefined,
        sorter: 'createTime_descend'
      },
      multipleSelection: [],
      typeOptions: [],
      priorityOptions: [],
      temp: {},
      dialogFormVisible: false,
      advancedSearch: false
    }
  },
  created() {
    this.getInitList()
  },
  methods: {
    clickSwitch() {
      this.advancedSearch = !this.advancedSearch
    },
    getInitList() {
      queryNotificationType().then(response => {
        this.typeOptions = response.data
      })
      queryDictByDictType('priority').then(response => {
        this.priorityOptions = response
      })
      this.getList()
    },
    getList() {
      this.listLoading = true
      queryMyNotification(this.listQuery).then(response => {
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
      this.listQuery.title = undefined
      this.listQuery.content = undefined
      this.listQuery.type = undefined
      this.listQuery.priority = undefined
      this.listQuery.publisher = undefined
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
    getSortClass: function(key) {
      const sorter = this.listQuery.sorter
      return sorter === `${key}_ascend` ? 'ascending' : 'descending'
    },
    handleView(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.dialogFormVisible = true
    }
  }
}
</script>
