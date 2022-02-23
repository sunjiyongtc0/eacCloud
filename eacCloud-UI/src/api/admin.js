import request from '@/utils/request'

/** 用户API开始 */

export function queryOnlineSysUser(query) {
  return request({
    url: '/api/admin/sysuser/queryOnlineSysUser',
    method: 'get',
    params: query
  })
}

export function querySysUser(query) {
  return request({
    url: '/api/admin/sysuser/querySysUser',
    method: 'get',
    params: query
  })
}

export function addSysUser(data) {
  return request({
    url: '/api/admin/sysuser/addSysUser',
    method: 'post',
    data
  })
}

export function updateSysUser(data) {
  return request({
    url: '/api/admin/sysuser/updateSysUser',
    method: 'put',
    data
  })
}

export function updateSysUserDetail(data) {
  return request({
    url: '/api/account/sysuserdetail/updateSysUserDetail',
    method: 'put',
    data
  })
}

export function deleteSysUser(data) {
  return request({
    url: '/api/admin/sysuser/deleteSysUser',
    method: 'post',
    params: {
      id: data
    }
  })
}

export function queryRoleName(userId, postCode, assign) {
  return request({
    url: '/api/admin/sysrole/queryRoleName',
    method: 'get',
    params: {
      userId: userId,
      postCode: postCode,
      assign: assign
    }
  })
}

export function queryUsername() {
  return request({
    url: '/api/admin/sysuser/queryUsername',
    method: 'get'
  })
}

export function exportSysUser(data) {
  return request({
    url: '/api/admin/sysuser/exportSysUser',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportWordSysUser(data) {
  return request({
    url: '/api/admin/sysuser/exportWordSysUser',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportPDFSysUser(data) {
  return request({
    url: '/api/admin/sysuser/exportPDFSysUser',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function authorizeRoleToUser(data) {
  return request({
    url: '/api/admin/sysuser/authorizeRoleToUser',
    method: 'post',
    data
  })
}

export function updatePassword(password, newPassword, id) {
  return request({
    url: '/api/account/sysuserdetail/updatePassword',
    method: 'put',
    params: {
      password: password,
      newPassword: newPassword,
      id: id
    }
  })
}

/** 用户API结束 */
/** 岗位API开始 */

export function querySysPost(query) {
  return request({
    url: '/api/admin/syspost/querySysPost',
    method: 'get',
    params: query
  })
}

export function querySysPostTree() {
  return request({
    url: '/api/admin/syspost/querySysPostTree',
    method: 'get'
  })
}

export function addSysPost(data) {
  return request({
    url: '/api/admin/syspost/addSysPost',
    method: 'post',
    data
  })
}

export function updateSysPost(data) {
  return request({
    url: '/api/admin/syspost/updateSysPost',
    method: 'put',
    data
  })
}

export function deleteSysPost(data) {
  return request({
    url: '/api/admin/syspost/deleteSysPost',
    method: 'post',
    params: {
      id: data
    }
  })
}

export function exportSysPost(data) {
  return request({
    url: '/api/admin/syspost/exportSysPost',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportWordSysPost(data) {
  return request({
    url: '/api/admin/syspost/exportWordSysPost',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportPDFSysPost(data) {
  return request({
    url: '/api/admin/syspost/exportPDFSysPost',
    method: 'post',
    responseType: 'blob',
    data
  })
}

/** 岗位API结束 */
/** 用户与岗位关联API开始 */

export function querySysUserPost(query) {
  return request({
    url: '/api/admin/sysuserpost/querySysUserPost',
    method: 'get',
    params: query
  })
}

export function querySysUserPostTree(userId) {
  return request({
    url: '/api/admin/sysuserpost/querySysUserPostTree',
    method: 'get',
    params: {
      userId: userId
    }
  })
}

export function addSysUserPost(data) {
  return request({
    url: '/api/admin/sysuserpost/addSysUserPost',
    method: 'post',
    data
  })
}

export function updateSysUserPost(data) {
  return request({
    url: '/api/admin/sysuserpost/updateSysUserPost',
    method: 'put',
    data
  })
}

export function deleteSysUserPost(data) {
  return request({
    url: '/api/admin/sysuserpost/deleteSysUserPost',
    method: 'post',
    params: {
      id: data
    }
  })
}

export function exportSysUserPost(data) {
  return request({
    url: '/api/admin/sysuserpost/exportSysUserPost',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportWordSysUserPost(data) {
  return request({
    url: '/api/admin/sysuserpost/exportWordSysUserPost',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportPDFSysUserPost(data) {
  return request({
    url: '/api/admin/sysuserpost/exportPDFSysUserPost',
    method: 'post',
    responseType: 'blob',
    data
  })
}

/** 用户与岗位关联API结束 */
/** 角色API开始 */

export function querySysRole(query) {
  return request({
    url: '/api/admin/sysrole/querySysRole',
    method: 'get',
    params: query
  })
}

export function addSysRole(data) {
  return request({
    url: '/api/admin/sysrole/addSysRole',
    method: 'post',
    data
  })
}

export function updateSysRole(data) {
  return request({
    url: '/api/admin/sysrole/updateSysRole',
    method: 'put',
    data
  })
}

export function deleteSysRole(data) {
  return request({
    url: '/api/admin/sysrole/deleteSysRole',
    method: 'post',
    params: {
      id: data
    }
  })
}

export function queryRoleNameCheckbox() {
  return request({
    url: '/api/admin/sysrole/queryRoleNameCheckbox',
    method: 'get'
  })
}

export function queryRoleCode() {
  return request({
    url: '/api/admin/sysrole/queryRoleCode',
    method: 'get'
  })
}

export function exportSysRole(data) {
  return request({
    url: '/api/admin/sysrole/exportSysRole',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportWordSysRole(data) {
  return request({
    url: '/api/admin/sysrole/exportWordSysRole',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportPDFSysRole(data) {
  return request({
    url: '/api/admin/sysrole/exportPDFSysRole',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function authorizeUserToRole(data) {
  return request({
    url: '/api/admin/sysrole/authorizeUserToRole',
    method: 'post',
    data
  })
}

/** 角色API结束 */
/** URL API开始 */

export function querySysUrl(query) {
  return request({
    url: '/api/admin/sysurl/querySysUrl',
    method: 'get',
    params: query
  })
}

export function addSysUrl(data) {
  return request({
    url: '/api/admin/sysurl/addSysUrl',
    method: 'post',
    data
  })
}

export function updateSysUrl(data) {
  return request({
    url: '/api/admin/sysurl/updateSysUrl',
    method: 'put',
    data
  })
}

export function deleteSysUrl(data) {
  return request({
    url: '/api/admin/sysurl/deleteSysUrl',
    method: 'post',
    params: {
      id: data
    }
  })
}

export function queryRoleIdByUrlId(data) {
  return request({
    url: '/api/admin/sysurl/queryRoleIdByUrlId',
    method: 'get',
    params: {
      urlId: data
    }
  })
}

export function queryUrlIdByRoleId(roleId) {
  return request({
    url: '/api/admin/sysurl/queryUrlIdByRoleId',
    method: 'get',
    params: {
      roleId: roleId
    }
  })
}

export function authorizeRoleToUrl(data) {
  return request({
    url: '/api/admin/sysurl/authorizeRoleToUrl',
    method: 'post',
    data
  })
}

export function authorizeUrlToRole(data) {
  return request({
    url: '/api/admin/sysurl/authorizeUrlToRole',
    method: 'post',
    data
  })
}

export function exportSysUrl(data) {
  return request({
    url: '/api/admin/sysurl/exportSysUrl',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportWordSysUrl(data) {
  return request({
    url: '/api/admin/sysurl/exportWordSysUrl',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportPDFSysUrl(data) {
  return request({
    url: '/api/admin/sysurl/exportPDFSysUrl',
    method: 'post',
    responseType: 'blob',
    data
  })
}

/** URL API结束 */
/** 菜单API开始 */

export function querySysMenu(query) {
  return request({
    url: '/api/admin/sysmenu/querySysMenu',
    method: 'get',
    params: query
  })
}

export function addSysMenu(data) {
  return request({
    url: '/api/admin/sysmenu/addSysMenu',
    method: 'post',
    data
  })
}

export function updateSysMenu(data) {
  return request({
    url: '/api/admin/sysmenu/updateSysMenu',
    method: 'put',
    data
  })
}

export function deleteSysMenu(data) {
  return request({
    url: '/api/admin/sysmenu/deleteSysMenu',
    method: 'post',
    params: {
      id: data
    }
  })
}

export function querySysMenuTree() {
  return request({
    url: '/api/admin/sysmenu/querySysMenuTree',
    method: 'get'
  })
}

export function queryMenuIdByRoleId(data) {
  return request({
    url: '/api/admin/sysmenu/queryMenuIdByRoleId',
    method: 'get',
    params: {
      roleId: data
    }
  })
}

export function authorizeMenuToRole(data) {
  return request({
    url: '/api/admin/sysmenu/authorizeMenuToRole',
    method: 'post',
    data
  })
}

export function queryMenuButtonCheckbox() {
  return request({
    url: '/api/admin/sysmenu/queryMenuButtonCheckbox',
    method: 'get'
  })
}

export function queryCheckedMenuButton(data) {
  return request({
    url: '/api/admin/sysmenu/queryCheckedMenuButton',
    method: 'get',
    params: {
      menuCode: data
    }
  })
}

export function querySysMenuButtonTree() {
  return request({
    url: '/api/admin/sysmenu/querySysMenuButtonTree',
    method: 'get'
  })
}

export function queryMenuButtonByRoleCode(data) {
  return request({
    url: '/api/admin/sysmenu/queryMenuButtonByRoleCode',
    method: 'get',
    params: {
      roleCode: data
    }
  })
}

export function authorizeMenuButtonToRole(data) {
  return request({
    url: '/api/admin/sysmenu/authorizeMenuButtonToRole',
    method: 'post',
    data
  })
}

export function queryDataFieldByMenuCode(data) {
  return request({
    url: '/api/admin/sysmenu/queryDataFieldByMenuCode',
    method: 'get',
    params: {
      menuCode: data
    }
  })
}

export function deleteDataField(data) {
  return request({
    url: '/api/admin/sysmenu/deleteDataField',
    method: 'post',
    data
  })
}

export function authorizeMenuDataToRole(data) {
  return request({
    url: '/api/admin/sysmenu/authorizeMenuDataToRole',
    method: 'post',
    data
  })
}

export function exportSysMenu(data) {
  return request({
    url: '/api/admin/sysmenu/exportSysMenu',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportWordSysMenu(data) {
  return request({
    url: '/api/admin/sysmenu/exportWordSysMenu',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportPDFSysMenu(data) {
  return request({
    url: '/api/admin/sysmenu/exportPDFSysMenu',
    method: 'post',
    responseType: 'blob',
    data
  })
}

/** 菜单API结束 */
/** 机构API开始 */

export function querySysOrg(query) {
  return request({
    url: '/api/admin/sysorg/querySysOrg',
    method: 'get',
    params: query
  })
}

export function queryOrgType() {
  return request({
    url: '/api/admin/sysorg/queryOrgType',
    method: 'get'
  })
}

export function querySysOrgTree() {
  return request({
    url: '/api/admin/sysorg/querySysOrgTree',
    method: 'get'
  })
}

export function queryOrgUserTree(roleId, assign) {
  return request({
    url: '/api/admin/sysorg/queryOrgUserTree',
    method: 'get',
    params: {
      roleId: roleId,
      assign: assign
    }
  })
}

export function queryModelOrgUserTree(modelId, assign) {
  return request({
    url: '/api/admin/sysorg/queryModelOrgUserTree',
    method: 'get',
    params: {
      modelId: modelId,
      assign: assign
    }
  })
}

export function addSysOrg(data) {
  return request({
    url: '/api/admin/sysorg/addSysOrg',
    method: 'post',
    data
  })
}

export function updateSysOrg(data) {
  return request({
    url: '/api/admin/sysorg/updateSysOrg',
    method: 'put',
    data
  })
}

export function deleteSysOrg(data) {
  return request({
    url: '/api/admin/sysorg/deleteSysOrg',
    method: 'post',
    params: {
      id: data
    }
  })
}

export function exportSysOrg(data) {
  return request({
    url: '/api/admin/sysorg/exportSysOrg',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportWordSysOrg(data) {
  return request({
    url: '/api/admin/sysorg/exportWordSysOrg',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportPDFSysOrg(data) {
  return request({
    url: '/api/admin/sysorg/exportPDFSysOrg',
    method: 'post',
    responseType: 'blob',
    data
  })
}

/** 机构API结束 */
/** 字典API开始 */

export function querySysDict(query) {
  return request({
    url: '/api/admin/sysdict/querySysDict',
    method: 'get',
    params: query
  })
}

export function querySysDictTree() {
  return request({
    url: '/api/admin/sysdict/querySysDictTree',
    method: 'get'
  })
}

export function addSysDict(data) {
  return request({
    url: '/api/admin/sysdict/addSysDict',
    method: 'post',
    data
  })
}

export function updateSysDict(data) {
  return request({
    url: '/api/admin/sysdict/updateSysDict',
    method: 'put',
    data
  })
}

export function deleteSysDict(data) {
  return request({
    url: '/api/admin/sysdict/deleteSysDict',
    method: 'post',
    params: {
      id: data
    }
  })
}

export function exportSysDict(data) {
  return request({
    url: '/api/admin/sysdict/exportSysDict',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportWordSysDict(data) {
  return request({
    url: '/api/admin/sysdict/exportWordSysDict',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportPDFSysDict(data) {
  return request({
    url: '/api/admin/sysdict/exportPDFSysDict',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function queryDictByDictType(dictType) {
  return request({
    url: '/api/admin/sysdict/queryDictByDictType',
    method: 'get',
    params: {
      dictType: dictType
    }
  })
}

/** 字典API结束 */
/** 区域API开始 */

export function querySysRegion(query) {
  return request({
    url: '/api/admin/sysregion/querySysRegion',
    method: 'get',
    params: query
  })
}

export function queryRegionType() {
  return request({
    url: '/api/admin/sysregion/queryRegionType',
    method: 'get'
  })
}

export function querySysRegionTree() {
  return request({
    url: '/api/admin/sysregion/querySysRegionTree',
    method: 'get'
  })
}

export function addSysRegion(data) {
  return request({
    url: '/api/admin/sysregion/addSysRegion',
    method: 'post',
    data
  })
}

export function updateSysRegion(data) {
  return request({
    url: '/api/admin/sysregion/updateSysRegion',
    method: 'put',
    data
  })
}

export function deleteSysRegion(data) {
  return request({
    url: '/api/admin/sysregion/deleteSysRegion',
    method: 'post',
    params: {
      regionCode: data
    }
  })
}

export function exportSysRegion(data) {
  return request({
    url: '/api/admin/sysregion/exportSysRegion',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportWordSysRegion(data) {
  return request({
    url: '/api/admin/sysregion/exportWordSysRegion',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportPDFSysRegion(data) {
  return request({
    url: '/api/admin/sysregion/exportPDFSysRegion',
    method: 'post',
    responseType: 'blob',
    data
  })
}

/** 区域API结束 */
/** 参数API开始 */

export function querySysParam(query) {
  return request({
    url: '/api/admin/sysparam/querySysParam',
    method: 'get',
    params: query
  })
}

export function addSysParam(data) {
  return request({
    url: '/api/admin/sysparam/addSysParam',
    method: 'post',
    data
  })
}

export function updateSysParam(data) {
  return request({
    url: '/api/admin/sysparam/updateSysParam',
    method: 'put',
    data
  })
}

export function deleteSysParam(data) {
  return request({
    url: '/api/admin/sysparam/deleteSysParam',
    method: 'post',
    params: {
      id: data
    }
  })
}

export function exportSysParam(data) {
  return request({
    url: '/api/admin/sysparam/exportSysParam',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportWordSysParam(data) {
  return request({
    url: '/api/admin/sysparam/exportWordSysParam',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportPDFSysParam(data) {
  return request({
    url: '/api/admin/sysparam/exportPDFSysParam',
    method: 'post',
    responseType: 'blob',
    data
  })
}

/** 参数API结束 */
/** 应用API开始 */

export function queryAppClient(query) {
  return request({
    url: '/api/uaa/appclient/queryAppClient',
    method: 'get',
    params: query
  })
}

export function addAppClient(data) {
  return request({
    url: '/api/uaa/appclient/addAppClient',
    method: 'post',
    data
  })
}

export function updateAppClient(data) {
  return request({
    url: '/api/uaa/appclient/updateAppClient',
    method: 'put',
    data
  })
}

export function deleteAppClient(data) {
  return request({
    url: '/api/uaa/appclient/deleteAppClient',
    method: 'post',
    params: {
      id: data
    }
  })
}

export function exportAppClient(data) {
  return request({
    url: '/api/uaa/appclient/exportAppClient',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportWordAppClient(data) {
  return request({
    url: '/api/admin/appclient/exportWordAppClient',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportPDFAppClient(data) {
  return request({
    url: '/api/admin/appclient/exportPDFAppClient',
    method: 'post',
    responseType: 'blob',
    data
  })
}

/** 应用API结束 */
/** 租户API开始 */

export function querySysTenant(query) {
  return request({
    url: '/api/admin/systenant/querySysTenant',
    method: 'get',
    params: query
  })
}

export function addSysTenant(data) {
  return request({
    url: '/api/admin/systenant/addSysTenant',
    method: 'post',
    data
  })
}

export function updateSysTenant(data) {
  return request({
    url: '/api/admin/systenant/updateSysTenant',
    method: 'put',
    data
  })
}

export function deleteSysTenant(data) {
  return request({
    url: '/api/admin/systenant/deleteSysTenant',
    method: 'post',
    params: {
      id: data
    }
  })
}

export function exportSysTenant(data) {
  return request({
    url: '/api/admin/systenant/exportSysTenant',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportWordSysTenant(data) {
  return request({
    url: '/api/admin/systenant/exportWordSysTenant',
    method: 'post',
    responseType: 'blob',
    data
  })
}

export function exportPDFSysTenant(data) {
  return request({
    url: '/api/admin/systenant/exportPDFSysTenant',
    method: 'post',
    responseType: 'blob',
    data
  })
}

/** 租户API结束 */
/** 消息通知API开始 */

export function queryNotification(query) {
  return request({
    url: '/api/notification/notification/queryNotification',
    method: 'get',
    params: query
  })
}

export function addNotification(data) {
  return request({
    url: '/api/notification/notification/addNotification',
    method: 'post',
    data
  })
}

export function updateNotification(data) {
  return request({
    url: '/api/notification/notification/updateNotification',
    method: 'put',
    data
  })
}

export function deleteNotification(data) {
  return request({
    url: '/api/notification/notification/deleteNotification',
    method: 'post',
    params: {
      id: data
    }
  })
}

export function queryMyNotification(query) {
  return request({
    url: '/api/notification/notification/queryMyNotification',
    method: 'get',
    params: query
  })
}

export function queryNotificationType() {
  return request({
    url: '/api/notification/notification/queryNotificationType',
    method: 'get'
  })
}

export function queryNotificationPriority() {
  return request({
    url: '/api/notification/notification/queryNotificationPriority',
    method: 'get'
  })
}

/** 消息通知API结束 */
