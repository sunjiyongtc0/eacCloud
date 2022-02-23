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
        <el-button type="danger" plain class="filter-item" icon="el-icon-delete" @click="handleDelete">
          {{ $t('common.delete') }}
        </el-button>
      </div>
      <div v-if="advancedSearch" style="float: left">
        <el-input v-model="listQuery.publisher" :placeholder="$t('notification.publisher')" clearable class="filter-input filter-item" @keyup.enter.native="handleFilter" />
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

    <el-dialog v-el-drag-dialog :title="dialogStatus === 'create' ? $t('common.add') : $t('common.edit')" :visible.sync="dialogFormVisible" width="850px">
      <el-form ref="dataForm" :rules="rules" :model="temp" :label-position="labelPosition" label-width="90px" style="width: 100%">
        <el-form-item :label="$t('notification.title')" prop="title">
          <el-input v-model="temp.title" :placeholder="$t('common.enter')" maxlength="100" clearable />
        </el-form-item>
        <el-form-item :label="$t('notification.type')" prop="type">
          <el-select v-model="temp.type" :placeholder="$t('common.choose')" clearable style="width: 100%">
            <el-option v-for="item in typeOptions" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('notification.priority')" prop="priority">
          <el-select v-model="temp.priority" :placeholder="$t('common.choose')" clearable style="width: 100%">
            <el-option v-for="item in priorityOptions" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('notification.period')" prop="rangeTime">
          <el-tooltip :content="$t('notification.period-tooltip')" placement="bottom" effect="light">
            <el-date-picker v-model="temp.rangeTime" type="datetimerange" style="width: 100%" clearable />
          </el-tooltip>
        </el-form-item>
        <el-form-item :label="$t('notification.content')" prop="content">
          <tinymce ref="editor" v-model="temp.content" :height="100" :placeholder="$t('common.enter')" clearable />
        </el-form-item>
        <el-form-item :label="$t('notification.publish-object')" prop="username">
          <el-tooltip :content="$t('notification.choose-tooltip')" placement="bottom" effect="light">
            <el-select v-model="temp.username" multiple :placeholder="$t('common.choose')" clearable style="width: 100%">
              <el-option v-for="item in usernameOptions" :key="item.value" :label="item.value" :value="item.value" />
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
  </div>
</template>

<script>
import { queryNotification, queryNotificationType, queryUsername, queryNotificationPriority, addNotification, updateNotification, deleteNotification } from '@/api/admin'
import waves from '@/directive/waves' // waves directive
import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import Tinymce from '@/components/Tinymce'

export default {
  name: 'Notification',
  components: { Pagination, Tinymce },
  directives: { waves, elDragDialog },
  data() {
    return {
      labelPosition: 'right',
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      disabledStatus: false,
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
      usernameOptions: [],
      temp: {
        id: undefined
      },
      dialogFormVisible: false,
      advancedSearch: false,
      dialogStatus: '',
      textMap: {
        update: this.$t('common.edit'),
        create: this.$t('common.add')
      },
      rules: {
        title: [{ required: true, validator: this.checkTitle, trigger: ['blur', 'change'] }],
        type: [{ required: true, validator: this.checkType, trigger: ['blur', 'change'] }],
        content: [{ required: true, validator: this.checkContent, trigger: ['blur', 'change'] }]
      }
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
      queryNotificationType().then(response => {
        this.typeOptions = response.data
      })
      queryNotificationPriority().then(response => {
        this.priorityOptions = response.data
      })
      queryUsername().then(response => {
        this.usernameOptions = response.data
      })
      this.getList()
    },
    getList() {
      this.listLoading = true
      queryNotification(this.listQuery).then(response => {
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
      if (this.$refs.editor) {
        this.$refs.editor.setContent('')
      }
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.startTime = this.temp.rangeTime ? this.temp.rangeTime[0] : null
          this.temp.endTime = this.temp.rangeTime ? this.temp.rangeTime[1] : null
          this.temp.publisher = this.$store.getters.name
          delete this.temp.rangeTime
          this.disabledStatus = true
          addNotification(this.temp).then(response => {
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
      if (this.$refs.editor) {
        this.$refs.editor.setContent(this.temp.content)
      }
      this.temp.rangeTime = this.temp.startTime && this.temp.endTime ? [this.temp.startTime, this.temp.endTime] : []
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // const tempData = Object.assign({}, this.temp)
          const tempData = {}
          tempData.id = this.temp.id
          tempData.title = this.temp.title
          tempData.type = this.temp.type
          tempData.priority = this.temp.priority
          tempData.startTime = this.temp.rangeTime ? this.temp.rangeTime[0] : null
          tempData.endTime = this.temp.rangeTime ? this.temp.rangeTime[1] : null
          tempData.content = this.temp.content
          tempData.username = this.temp.username
          this.disabledStatus = true
          updateNotification(tempData).then(response => {
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
      deleteNotification(deleteArray).then(response => {
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
    handleWarning(response) {
      this.$message({
        message: response.message || response,
        type: 'warning',
        duration: 2000
      })
      this.disabledStatus = false
    },
    checkTitle(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('notification.title-required')))
      } else {
        callback()
      }
    },
    checkType(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('notification.type-required')))
      } else {
        callback()
      }
    },
    checkContent(rule, value, callback) {
      if (!value) {
        return callback(new Error(this.$t('notification.content-required')))
      } else {
        callback()
      }
    }
  }
}
</script>
