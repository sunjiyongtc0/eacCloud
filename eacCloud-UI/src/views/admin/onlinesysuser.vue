<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.username" :placeholder="$t('sysuser.username')" clearable class="filter-input filter-item" style="width: 130px" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.nickname" :placeholder="$t('sysuser.nickname')" clearable class="filter-input filter-item" style="width: 130px" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.mobile" :placeholder="$t('sysuser.mobile')" clearable class="filter-input filter-item" style="width: 130px" @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.roleId" :placeholder="$t('sysuser.roleId')" clearable class="filter-input filter-item" style="width: 130px">
        <el-option v-for="item in roleOptions" :key="item.value" :label="item.name" :value="item.value" />
      </el-select>
      <el-select ref="selectOrgId" v-model="listQuery.orgId" :placeholder="$t('sysuser.orgId')" class="filter-input filter-item" style="width: 150px" clearable @clear="handleClearOrgId">
        <el-option :key="listQuery.orgId" hidden :value="listQuery.orgId" :label="orgName" />
        <el-tree :data="orgIdOptions" :props="{label: 'title'}" :expand-on-click-node="false" :check-on-click-node="true" @node-click="handleNodeClickOrgId" />
      </el-select>
      <div style="float: right">
        <el-button v-waves type="primary" class="filter-item" icon="el-icon-search" @click="handleFilter">
          {{ $t('common.query') }}
        </el-button>
        <el-button v-waves type="primary" class="filter-item" icon="el-icon-refresh-right" @click="handleReset">
          {{ $t('common.reset') }}
        </el-button>
        <el-dropdown v-waves split-button :loading="downloadLoading" type="primary" class="filter-item" style="margin-left: 10px; margin-right: 10px" trigger="click" @command="handleExport">
          <i class="el-icon-document" /> {{ $t('common.export') }}
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="excel"><svg-icon icon-class="excel" style="margin-right: 5px" />{{ $t('common.export') }}Excel</el-dropdown-item>
            <el-dropdown-item command="word"><svg-icon icon-class="documentation" style="margin-right: 5px" />{{ $t('common.export') }}Word</el-dropdown-item>
            <el-dropdown-item command="pdf"><svg-icon icon-class="pdf" style="margin-right: 5px" />{{ $t('common.export') }}PDF</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-button type="primary" class="filter-item" icon="el-icon-printer" @click="handlePrint">
          {{ $t('common.print') }}
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
      <el-table-column :label="$t('sysuser.username')" prop="username" />
      <el-table-column :label="$t('sysuser.nickname')" prop="nickname" />
      <el-table-column :label="$t('sysuser.mobile')" prop="mobile" />
      <el-table-column :label="$t('sysuser.roleId')" prop="roleIdCn" />
      <el-table-column :label="$t('sysuser.orgId')" prop="orgIdCn" />
      <el-table-column :label="$t('common.create-time')" min-width="100px" prop="createTime" sortable="custom" :class-name="getSortClass('createTime')">
        <template slot-scope="{row}">
          <span>{{ row.createTime | formatDate }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.currentPage" :limit.sync="listQuery.pageSize" @pagination="getList" />

  </div>
</template>

<script>
import { queryOnlineSysUser, queryRoleName, querySysOrgTree, exportSysUser, exportWordSysUser, exportPDFSysUser } from '@/api/admin'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import qs from 'qs'
import { saveAs } from 'file-saver'
import mammoth from 'mammoth'
import { getToken } from '@/utils/auth'

export default {
  name: 'OnlineSysUser',
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      labelPosition: 'right',
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        currentPage: 1,
        pageSize: 10,
        username: undefined,
        nickname: undefined,
        mobile: undefined,
        roleId: undefined,
        orgId: undefined,
        sorter: 'createTime_descend'
      },
      orgName: undefined,
      importDialogFormVisible: false,
      dataObj: { },
      elUploadHeaders: { 'Authorization': 'Bearer ' + getToken() },
      orgIdOptions: [],
      roleOptions: [],
      multipleSelection: [],
      downloadLoading: false
    }
  },
  created() {
    if (window.innerWidth < 700) {
      this.labelPosition = 'top'
    }
    this.getInitList()
  },
  methods: {
    getInitList() {
      queryRoleName().then(response => {
        this.roleOptions = response.data
      })
      querySysOrgTree().then(response => {
        this.orgIdOptions = response.data.list
      })
      this.getList()
    },
    getList() {
      this.listLoading = true
      queryOnlineSysUser(this.listQuery).then(response => {
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
      this.listQuery.username = undefined
      this.listQuery.nickname = undefined
      this.listQuery.mobile = undefined
      this.listQuery.roleId = undefined
      this.listQuery.orgId = undefined
      this.orgName = undefined
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
            printer.document.write(htmlToPrint + result.value.replace('</table><table>', '').replace('用户管理', '在线用户'))
            printer.print()
            printer.close()
          }).done()
        }
      })
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
      listQueryData.onlinestatus = 1
      listQueryData.username = this.listQuery.username
      listQueryData.nickname = this.listQuery.nickname
      listQueryData.mobile = this.listQuery.mobile
      listQueryData.roleId = this.listQuery.roleId
      listQueryData.orgId = this.listQuery.orgId
      return listQueryData
    },
    handleDownload(isTemplate) { // 导出 Excel
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      listQueryData.isTemplate = isTemplate // 1为模板，0不为模板
      exportSysUser(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.onlinesysuser') + '.xlsx')
      })
      this.downloadLoading = false
    },
    handleDownloadWord() { // 导出 Word
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportWordSysUser(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.onlinesysuser') + '.docx')
      })
      this.downloadLoading = false
    },
    handleDownloadPDF() { // 导出 PDF
      this.downloadLoading = true
      const listQueryData = this.handleCommonQuery()
      exportPDFSysUser(qs.stringify(listQueryData)).then(response => {
        saveAs(response, this.$t('route.onlinesysuser') + '.pdf')
      })
      this.downloadLoading = false
    },
    getSortClass: function(key) {
      const sorter = this.listQuery.sorter
      return sorter === `${key}_ascend` ? 'ascending' : 'descending'
    },
    handleNodeClickOrgId(data) {
      this.orgName = data.title
      this.listQuery.orgId = data.id
      this.$refs.selectOrgId.blur()
    },
    handleClearOrgId() {
      this.orgName = undefined
      this.listQuery.orgId = undefined
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
  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
</style>
