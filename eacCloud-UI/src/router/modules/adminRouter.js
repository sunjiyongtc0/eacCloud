import Layout from '@/layout'

const adminRouter = {
  path: '/admin',
  component: Layout,
  redirect: 'noRedirect',
  alwaysShow: true,
  name: 'admin',
  meta: { title: 'admin', icon: 'el-icon-s-management' },
  children: [
    {
      path: '/sysuser',
      component: () => import('@/views/admin/sysuser'),
      name: 'SysUser',
      meta: { title: 'sysuser', icon: 'el-icon-user' }
    },
    {
      path: '/syspost',
      component: () => import('@/views/admin/syspost'),
      name: 'SysPost',
      meta: { title: 'syspost', icon: 'el-icon-bank-card' }
    },
    {
      path: '/sysmenu',
      component: () => import('@/views/admin/sysmenu'),
      name: 'SysMenu',
      meta: { title: 'sysmenu', icon: 'el-icon-menu' }
    },
    {
      path: '/sysorg',
      component: () => import('@/views/admin/sysorg'),
      name: 'SysOrg',
      meta: { title: 'sysorg', icon: 'peoples' }
    },
    {
      path: '/sysdict',
      component: () => import('@/views/admin/sysdict'),
      name: 'SysDict',
      meta: { title: 'sysdict', icon: 'el-icon-notebook-1' }
    },
    {
      path: '/sysregion',
      component: () => import('@/views/admin/sysregion'),
      name: 'SysRegion',
      meta: { title: 'sysregion', icon: 'international' }
    },
    {
      path: '/sysparam',
      component: () => import('@/views/admin/sysparam'),
      name: 'SysParam',
      meta: { title: 'sysparam', icon: 'el-icon-s-order' }
    },
    {
      path: '/notification',
      component: () => import('@/views/admin/notification'),
      name: 'Notification',
      meta: { title: 'notification', icon: 'el-icon-message' }
    },
    {
      path: '/appclient',
      component: () => import('@/views/admin/appclient'),
      name: 'AppClient',
      meta: { title: 'appclient', icon: 'el-icon-s-operation' }
    },
    {
      path: '/onlinesysuser',
      component: () => import('@/views/admin/onlinesysuser'),
      name: 'OnlineSysUser',
      meta: { title: 'onlinesysuser', icon: 'el-icon-chat-line-round' }
    }
  ]
}
export default adminRouter
