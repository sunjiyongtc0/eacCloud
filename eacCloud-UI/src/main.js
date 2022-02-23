import Vue from 'vue'
import Cookies from 'js-cookie'
import 'normalize.css/normalize.css' // a modern alternative to CSS resets
import Element from 'element-ui'
import './styles/element-variables.scss'
import '@/styles/index.scss' // global css
import App from './App'
import store from './store'
import router from './router'
import i18n from './lang' // internationalization
import './icons' // icon
import './permission' // permission control
import './utils/error-log' // error log
import Tinymce from '@/components/Tinymce'
import axios from 'axios'
import { loadScriptQueue } from '@/utils/loadScript'
import * as filters from './filters' // global filters
import moment from 'moment'
import dataV from '@jiaminghi/data-view'
import { hasBtnPermission } from './permission'

Vue.use(dataV)

Vue.filter('formatDate', function(value) {
  if (value) {
    if (value === '-') {
      return '-'
    } else {
      return moment(value).format('YYYY-MM-DD HH:mm:ss')
    }
  } else {
    return ''
  }
})

Vue.component('tinymce', Tinymce) // eslint-disable-line
Vue.prototype.$axios = axios

if (process.env.NODE_ENV === 'production' || process.env.NODE_ENV === 'development') {
  const { mockXHR } = require('../mock')
  mockXHR()
}

Vue.use(Element, {
  size: Cookies.get('size') || 'small', // set element-ui default size
  i18n: (key, value) => i18n.t(key, value)
})

Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false
Vue.prototype.hasPerm=hasBtnPermission;

const $previewApp = document.getElementById('previewApp')
const childAttrs = {
  file: '',
  dialog: ' width="600px" class="dialog-width" v-if="visible" :visible.sync="visible" :modal-append-to-body="false" '
}

window.addEventListener('message', init, false)

function buildLinks(links) {
  let strs = ''
  links.forEach(url => {
    strs += `<link href="${url}" rel="stylesheet">`
  })
  return strs
}


function init(event) {
  if (event.data.type === 'refreshFrame') {
    const code = event.data.data
    const attrs = childAttrs[code.generateConf.type]
    let links = ''

    if (Array.isArray(code.links) && code.links.length > 0) {
      links = buildLinks(code.links)
    }

    $previewApp.innerHTML = `${links}<style>${code.css}</style><div id="app2"></div>`

    if (Array.isArray(code.scripts) && code.scripts.length > 0) {
      loadScriptQueue(code.scripts, () => {
        newVue(attrs, code.js, code.html)
      })
    } else {
      newVue(attrs, code.js, code.html)
    }
    document.getElementById('app').style.display = 'none'
  }
}


function newVue(attrs, main, html) {
  main = eval(`(${main})`) // eslint-disable-line
  main.template = `<div>${html}</div>`
  new Vue({
    components: {
      child: main
    },
    data() {
      return {
        visible: true
      }
    },
    template: `<div><child ${attrs}/></div>`
  }).$mount('#app2')
}
new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: h => h(App)
})
