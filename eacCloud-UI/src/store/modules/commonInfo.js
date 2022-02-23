import { querySysOrg } from '@/api/admin'

const state = {
  orgs: []
}

const mutations = {
  SET_ORGS: (state, orgs) => {
    state.orgs = orgs
  }
}

const actions = {
  querySysOrg({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      querySysOrg({ currentPage: 1, pageSize: 10000 }).then(response => {
        const data = response.data.list
        commit('SET_ORGS', data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
