const getters = {
  sidebar: state => state.app.sidebar,
  language: state => state.app.language,
  size: state => state.app.size,
  device: state => state.app.device,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  id: state => state.user.id,
  nickname: state => state.user.nickname,
  phone: state => state.user.phone,
  geographic: state => state.user.geographic,
  address: state => state.user.address,
  profile: state => state.user.profile,
  introduction: state => state.user.introduction,
  roles: state => state.user.roles,
  orgs: state => state.commonInfo.orgs,
  buttons: state => state.user.buttons,
  permission_routes: state => state.permission.routes,
  errorLogs: state => state.errorLog.logs
}
export default getters
