import { login, getInfo, queryCurrentAuthority, registerNewSession, getSmsCaptcha, loginMobile } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  id: '',
  nickname: '',
  phone: '',
  geographic: '',
  address: '',
  profile: '',
  introduction: '',
  buttons:[],
  roles: []
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ID: (state, id) => {
    state.id = id
  },
  SET_NICKNAME: (state, nickname) => {
    state.nickname = nickname
  },
  SET_PHONE: (state, phone) => {
    state.phone = phone
  },
  SET_GEOGRAPHIC: (state, geographic) => {
    state.geographic = geographic
  },
  SET_ADDRESS: (state, address) => {
    state.address = address
  },
  SET_PROFILE: (state, profile) => {
    state.profile = profile
  },
  SET_BUTTONS:(state,buttons)=>{
    state.buttons=buttons
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const {  username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({  username: username.trim(), password: password }).then(response => {
        commit('SET_TOKEN', response.access_token)
        setToken(response.access_token)
        registerNewSession().then(response => {
        })
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  loginMobile({ commit }, userInfo) {
    const { tenantCode, mobile, smsCaptcha } = userInfo
    return new Promise((resolve, reject) => {
      loginMobile({ tenantCode: tenantCode.trim(), mobile: mobile.trim(), smsCaptcha: smsCaptcha.trim() }).then(response => {
        commit('SET_TOKEN', response.access_token)
        setToken(response.access_token)
        registerNewSession().then(response => {
        })
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  getSmsCaptcha({ commit }, mobile) {
    return new Promise((resolve, reject) => {
      getSmsCaptcha(mobile.trim()).then(response => {
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response

        if (!data) {
          reject('验证失败，请重新登录。')
        }

        data.name = data.username
        data.introduction = data.nickname
        const { roles, name, avatar, id, nickname, phone, geographic, address, profile, introduction } = data

        if (!roles || roles.length <= 0) {
          reject('角色必须为非null数组！')
        }

        commit('SET_ROLES', roles)
        commit('SET_NAME', name)
        commit('SET_AVATAR', avatar)
        commit('SET_ID', id)
        commit('SET_NICKNAME', nickname)
        commit('SET_PHONE', phone)
        commit('SET_GEOGRAPHIC', geographic)
        commit('SET_ADDRESS', address)
        commit('SET_PROFILE', profile)
        commit('SET_INTRODUCTION', introduction)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  queryCurrentAuthority({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      queryCurrentAuthority({ username: state.name }).then(response => {
        const data = JSON.stringify(response.menu)
        const buttons=response.menuButton;
        const buttonAuthList=[]
        buttons.forEach(button=>{
          if(button){
            buttonAuthList.push(button)
          }
        })
        commit('SET_BUTTONS', buttonAuthList)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      commit('SET_TOKEN', '')
      commit('SET_BUTTONS', [])
      commit('SET_ROLES', [])
      removeToken()
      resetRouter()

      dispatch('tagsView/delAllViews', null, { root: true })

      resolve()
    })
  },

  // remove token
  resetToken({ commit, dispatch }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_BUTTONS', [])
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  // dynamically modify permissions
  async changeRoles({ commit, dispatch }, role) {
    const token = role + '-token'

    commit('SET_TOKEN', token)
    setToken(token)

    const { roles } = await dispatch('getInfo')

    resetRouter()

    // generate accessible routes map based on roles
    const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })

    // dynamically add accessible routes
    router.addRoutes(accessRoutes)

    // reset visited views and cached views
    dispatch('tagsView/delAllViews', null, { root: true })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
