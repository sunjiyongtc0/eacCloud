import Layout from '@/layout'

const authorityRouter = {
  path: '/authority',
  component: Layout,
  redirect: 'noRedirect',
  alwaysShow: true,
  name: 'authority',
  meta: { title: 'authority', icon: 'lock' },
  children: [
    {
      path: '/sysrole',
      component: () => import('@/views/authority/sysrole'),
      name: 'SysRole',
      meta: { title: 'sysrole', icon: 'el-icon-postcard' }
    },
    {
      path: '/sysurl',
      component: () => import('@/views/authority/sysurl'),
      name: 'SysUrl',
      meta: { title: 'sysurl', icon: 'el-icon-set-up' }
    },
    {
      path: '/datapermission',
      component: () => import('@/views/authority/datapermission'),
      name: 'DataPermission',
      meta: { title: 'datapermission', icon: 'el-icon-connection' }
    }
  ]
}
export default authorityRouter
