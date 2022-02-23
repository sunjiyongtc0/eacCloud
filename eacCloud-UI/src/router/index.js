/**
 * 路由配置
 */
import Vue from 'vue'
import Router from 'vue-router'

const originalPush = Router.prototype.push
Router.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
  return originalPush.call(this, location).catch(err => err)
}

Vue.use(Router)

/* Layout */
import Layout from '@/layout'



import chartsRouter from './modules/chartsRouter'
import authorityRouter from './modules/authorityRouter'
import adminRouter from './modules/adminRouter'
// import personaloaRouter from './modules/personaloaRouter'
// import activitiRouter from './modules/activitiRouter'
// import fileRouter from './modules/fileRouter'

export const templateRoutes = []
export const constantRoutes = [
  {
    path: '/',
    component: Layout,
    redirect: '/workbench',
    name: 'home',
    meta: { title: 'home', icon: 'el-icon-s-home' },
    children: [
      {
        path: '/workbench',
        component: () => import('@/views/home/index'),
        name: 'Workbench',
        meta: { title: 'workbench', icon: 'dashboard', affix: true }
      },
      {
        path: '/mynotification',
        component: () => import('@/views/home/mynotification'),
        name: 'MyNotification',
        meta: { title: 'mynotification', icon: 'message' }
      }
    ]
  },
  authorityRouter,
  adminRouter,
  chartsRouter,
  {
    path: '/monitors',
    component: Layout,
    redirect: 'noRedirect',
    name: 'monitors',
    meta: { title: 'monitors', icon: 'el-icon-monitor' },
    children: [
      {
        path: '/sentinel',
        component: () => import('@/views/monitors/sentinel'),
        name: 'Sentinel',
        meta: { title: 'sentinel', icon: 'link' }
      },
      {
        path: '/nacos',
        component: () => import('@/views/monitors/nacos'),
        name: 'Nacos',
        meta: { title: 'nacos', icon: 'link' }
      },
      {
        path: '/swagger',
        component: () => import('@/views/monitors/swagger'),
        name: 'Swagger',
        meta: { title: 'swagger', icon: 'link' }
      },
      {
        path: '/druid',
        component: () => import('@/views/monitors/druid'),
        name: 'Druid',
        meta: { title: 'druid', icon: 'link' }
      },
      {
        path: '/grafana',
        component: () => import('@/views/monitors/prometheusgrafana'),
        name: 'PrometheusGrafana',
        meta: { title: 'prometheusgrafana', icon: 'link' }
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  }
  ]

// export const templateRoutes = [
//   {
//     path: 'guide',
//     component: () => import('@/views/guide/index'),
//     name: 'Guide',
//     meta: { title: 'guide', icon: 'guide', noCache: true }
//   },
//   {
//     path: 'icon',
//     component: () => import('@/views/icons/index'),
//     name: 'Icons',
//     meta: { title: 'icons', icon: 'icon', noCache: true }
//   },
//   {
//     path: '/error',
//     component: () => import('@/views/error-page'),
//     redirect: 'noRedirect',
//     name: 'ErrorPages',
//     meta: {
//       title: 'errorPages',
//       icon: '404'
//     },
//     children: [
//       {
//         path: '401',
//         component: () => import('@/views/error-page/401'),
//         name: 'Page401',
//         meta: { title: 'page401', noCache: true }
//       },
//       {
//         path: '404',
//         component: () => import('@/views/error-page/404'),
//         name: 'Page404',
//         meta: { title: 'page404', noCache: true }
//       }
//     ]
//   },
//   {
//     path: 'error-log',
//     component: () => import('@/views/error-log/index'),
//     name: 'ErrorLog',
//     meta: { title: 'errorLog', icon: 'bug' }
//   },
//
//   {
//     path: 'clipboard',
//     component: () => import('@/views/clipboard/index'),
//     name: 'ClipboardDemo',
//     meta: { title: 'clipboardDemo', icon: 'clipboard' }
//   },
//   {
//     path: 'external-link',
//     component: Layout,
//     children: [
//       {
//         path: 'https://www.mscodecloud.com',
//         meta: { title: 'externalLink', icon: 'link' }
//       }
//     ]
//   },
//   { path: '*', redirect: '/404', hidden: true }
// ]



// export const constantRoutes = [
//   {
//     path: '/redirect',
//     component: Layout,
//     hidden: true,
//     children: [
//       {
//         path: '/redirect/:path(.*)',
//         component: () => import('@/views/redirect/index')
//       }
//     ]
//   },
//   {
//     path: '/login',
//     component: () => import('@/views/login/index'),
//     hidden: true
//   },
//   {
//     path: '/auth-redirect',
//     component: () => import('@/views/login/auth-redirect'),
//     hidden: true
//   },
//   {
//     path: '/forgotpassword',
//     component: () => import('@/views/login/forgotpassword'),
//     hidden: true
//   },
//   {
//     path: '/register',
//     component: () => import('@/views/login/register'),
//     hidden: true
//   },
//   {
//     path: '/404',
//     component: () => import('@/views/error-page/404'),
//     hidden: true
//   },
//   {
//     path: '/401',
//     component: () => import('@/views/error-page/401'),
//     hidden: true
//   },
//   {
//     path: '/',
//     component: Layout,
//     redirect: '/workbench',
//     name: 'home',
//     meta: { title: 'home', icon: 'el-icon-s-home' },
//     children: [
//       {
//         path: '/workbench',
//         component: () => import('@/views/home/index'),
//         name: 'Workbench',
//         meta: { title: 'workbench', icon: 'dashboard', affix: true }
//       },
//       {
//         path: '/mynotification',
//         component: () => import('@/views/home/mynotification'),
//         name: 'MyNotification',
//         meta: { title: 'mynotification', icon: 'message' }
//       }
//     ]
//   },
//   authorityRouter,
//   adminRouter,
//   personaloaRouter,
//   activitiRouter,
//   {
//     path: '/devtool',
//     component: Layout,
//     redirect: 'noRedirect',
//     alwaysShow: true,
//     name: 'devtool',
//     meta: { title: 'devtool', icon: 'el-icon-s-tools' },
//     children: [
//       {
//         path: '/codegenerator',
//         component: () => import('@/views/devtool/codegenerator'),
//         redirect: 'noRedirect',
//         name: 'CodeGenerator',
//         meta: { title: 'codegenerator', icon: 'el-icon-folder-add' },
//         children: [
//           {
//             path: 'generatortemplate',
//             component: () => import('@/views/devtool/codegenerator/generatortemplate'),
//             name: 'GeneratorTemplate',
//             meta: { title: 'generatortemplate', icon: 'el-icon-copy-document' }
//           },
//           {
//             path: 'forwardgenerator',
//             component: () => import('@/views/devtool/codegenerator/forwardgenerator'),
//             name: 'ForwardGenerator',
//             meta: { title: 'forwardgenerator', icon: 'el-icon-document-copy' }
//           },
//           {
//             path: 'reversegenerator',
//             component: () => import('@/views/devtool/codegenerator/reversegenerator'),
//             name: 'ReverseGenerator',
//             meta: { title: 'reversegenerator', icon: 'el-icon-document-copy' }
//           }
//         ]
//       },
//       {
//         path: '/formgenerator',
//         component: () => import('@/views/devtool/formgenerator'),
//         redirect: 'noRedirect',
//         name: 'FormGenerator',
//         meta: { title: 'formgenerator', icon: 'documentation' },
//         children: [
//           {
//             path: '/formdesign',
//             component: () => import('@/views/devtool/formgenerator/index/Home'),
//             name: 'FormDesign',
//             meta: { title: 'formdesign', icon: 'form' }
//           },
//           {
//             path: '/generatorform',
//             component: () => import('@/views/devtool/formgenerator/generatorform'),
//             name: 'GeneratorForm',
//             meta: { title: 'generatorform', icon: 'skill' }
//           }
//         ]
//       }
//     ]
//   },
//   chartsRouter,
//   {
//     path: '/tenant',
//     component: Layout,
//     redirect: 'noRedirect',
//     alwaysShow: true,
//     name: 'tenant',
//     meta: { title: 'tenant', icon: 'el-icon-cloudy-and-sunny' },
//     children: [
//       {
//         path: '/systenant',
//         component: () => import('@/views/admin/systenant'),
//         name: 'SysTenant',
//         meta: { title: 'systenant', icon: 'el-icon-cloudy' }
//       }
//     ]
//   },
//   fileRouter,
//   {
//     path: '/distributedlog',
//     component: Layout,
//     redirect: 'noRedirect',
//     alwaysShow: true,
//     name: 'distributedlog',
//     meta: { title: 'distributedlog', icon: 'el-icon-notebook-2' },
//     children: [
//       {
//         path: '/elk',
//         component: () => import('@/views/distributedlog/elk'),
//         name: 'ELK',
//         meta: { title: 'kibana', icon: 'nested' }
//       }
//     ]
//   },
//   {
//     path: '/monitors',
//     component: Layout,
//     redirect: 'noRedirect',
//     name: 'monitors',
//     meta: { title: 'monitors', icon: 'el-icon-monitor' },
//     children: [
//       {
//         path: '/sentinel',
//         component: () => import('@/views/monitors/sentinel'),
//         name: 'Sentinel',
//         meta: { title: 'sentinel', icon: 'link' }
//       },
//       {
//         path: '/nacos',
//         component: () => import('@/views/monitors/nacos'),
//         name: 'Nacos',
//         meta: { title: 'nacos', icon: 'link' }
//       },
//       {
//         path: '/swagger',
//         component: () => import('@/views/monitors/swagger'),
//         name: 'Swagger',
//         meta: { title: 'swagger', icon: 'link' }
//       },
//       {
//         path: '/druid',
//         component: () => import('@/views/monitors/druid'),
//         name: 'Druid',
//         meta: { title: 'druid', icon: 'link' }
//       },
//       {
//         path: '/grafana',
//         component: () => import('@/views/monitors/prometheusgrafana'),
//         name: 'PrometheusGrafana',
//         meta: { title: 'prometheusgrafana', icon: 'link' }
//       }
//     ]
//   },
//   {
//     path: '/profile',
//     component: Layout,
//     redirect: '/profile/index',
//     hidden: true,
//     children: [
//       {
//         path: 'index',
//         component: () => import('@/views/profile/index'),
//         name: 'Profile',
//         meta: { title: 'profile', icon: 'user', noCache: true }
//       }
//     ]
//   }
// ]

export const asyncTemplateRoutes = [
  {
    path: '/template',
    component: Layout,
    redirect: 'noRedirect',
    name: 'template',
    meta: {
      title: 'template',
      icon: 'table'
    },
    children: templateRoutes
  }
]


export const asyncRoutes = [
  {
    path: '/permission',
    component: Layout,
    redirect: '/permission/page',
    alwaysShow: true, // will always show the root menu
    name: 'Permission',
    meta: {
      title: 'permission',
      icon: 'lock',
      roles: ['ROLE_EDITOR'] // you can set roles in root nav
    },
    children: [
      {
        path: 'page',
        component: () => import('@/views/permission/page'),
        name: 'PagePermission',
        meta: {
          title: 'pagePermission',
          roles: ['ROLE_ADMIN'] // or you can only set roles in sub nav
        }
      },
      {
        path: 'directive',
        component: () => import('@/views/permission/directive'),
        name: 'DirectivePermission',
        meta: {
          title: 'directivePermission'
        }
      },
      {
        path: 'role',
        component: () => import('@/views/permission/role'),
        name: 'RolePermission',
        meta: {
          title: 'rolePermission',
          roles: ['ROLE_ADMIN']
        }
      }
    ]
  }
]

const createRouter = () => new Router({
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
