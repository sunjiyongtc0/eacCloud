/** When your routing table is too long, you can split it into small modules**/
import Layout from '@/layout'

const chartsRouter = {
  path: '/chart-report',
  component: Layout,
  redirect: 'noRedirect',
  alwaysShow: true,
  name: 'ChartReport',
  meta: { title: 'chartreport', icon: 'el-icon-s-grid' },
  children: [
    {
      path: '/bigscreen',
      component: () => import('@/views/chart-report/bigscreen/index'), // Parent router-view,
      name: 'BigScreen',
      meta: { title: 'bigscreen', icon: 'el-icon-full-screen' },
      redirect: 'noRedirect',
      alwaysShow: true,
      children: [
        {
          path: 'construction',
          component: () => import('@/views/chart-report/bigscreen/construction'),
          name: 'Construction',
          meta: { title: 'construction', icon: 'component', noCache: true }
        },
        {
          path: 'electronic',
          component: () => import('@/views/chart-report/bigscreen/electronic'),
          name: 'Electronic',
          meta: { title: 'electronic', icon: 'component', noCache: true }
        },
        {
          path: 'maintenance',
          component: () => import('@/views/chart-report/bigscreen/maintenance'),
          name: 'Maintenance',
          meta: { title: 'maintenance', icon: 'component', noCache: true }
        },
        {
          path: 'dataanalysis',
          component: () => import('@/views/chart-report/bigscreen/dataanalysis'),
          name: 'DataAnalysis',
          meta: { title: 'dataanalysis', icon: 'component', noCache: true }
        }
      ]
    }
  ]
}

export default chartsRouter
