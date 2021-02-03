import { getInfo, login, logout } from '@/api/user'
import { getToken, removeToken, setToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'
import { getFaculties, getTitles } from '@/api/common'

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  userInfo: {},
  roles: [],
  // 增补院系关系的映射
  faculties: [{ id: '', name: '' }],
  // 职称
  titles: [{ id: '', name: '' }]
}

const mutations = {
  SET_USER_INFO: (state, userInfo) => {
    state.userInfo = userInfo
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  // 增补院系关系的映射
  SET_FACULTIES: (state, faculties) => {
    state.faculties = faculties
  },
  // 增补院系关系的映射
  SET_TITLES: (state, titles) => {
    state.titles = titles
  }
}

const actions = {
  // user 登录
  login({ commit }, userInfo) {
    const { username, password, role } = userInfo
    return new Promise((resolve, reject) => {
      login(
        {
          username: username.trim(),
          password: password,
          role: role
        })
        .then(response => {
          const { data } = response

          commit('SET_TOKEN', data)

          setToken(data)

          resolve()
        }).catch(error => {
        reject(error)
      })
    })
  },
  // 获取用户信息
  getInfo({ commit }) {
    return new Promise((resolve, reject) => {
      getInfo().then(response => {
        const { data } = response

        if (!data) {
          reject('登录验证失败,请重新登录')
        }

        const { roles, name, avatar } = data

        // roles must be a non-empty array
        if (!roles || roles.length <= 0) {
          reject('错误:请确认用户组是正确的')
        }

        commit('SET_ROLES', roles)
        //提交用户信息到本地
        commit('SET_USER_INFO', data)

        commit('SET_NAME', name)
        commit('SET_AVATAR', avatar)
        // commit('SET_INTRODUCTION', introduction)
        resolve(data)
      }).catch(error => {
        reject(error)
      })

    })
  },
  //获取院系表
  getFaculties({ commit }) {
    return new Promise((resolve, reject) => {
      // 获取院系信息
      getFaculties().then(response => {
        if (response.code === 20000) {
          commit('SET_FACULTIES', response.data)
          resolve(response.data)
        }
      }).catch(error => {
        reject(error)
      })

    })
  },
  //获取职称表
  getTitles({ commit }) {
    return new Promise((resolve, reject) => {
      // 获取院系信息
      getTitles().then(response => {
        if (response.code === 20000) {
          commit('SET_TITLES', response.data)
          resolve(response.data)
        }
      }).catch(error => {
        reject(error)
      })

    })
  },

  // user logout
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        removeToken()
        resetRouter()

        // reset visited views and cached views
        // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
        /* dispatch('tagsView/delAllViews', null, { root: true })*/

        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  // dynamically modify permissions
  changeRoles({ commit, dispatch }, role) {
    return new Promise(async resolve => {
      const token = role + '-token'

      commit('SET_TOKEN', token)
      setToken(token)

      const { roles } = await dispatch('getInfo')

      resetRouter()

      // generate accessible routes map based on roles
      const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })

      // dynamically add accessible routes
      router.addRoutes(accessRoutes)

      /* // reset visited views and cached views
       dispatch('tagsView/delAllViews', null, { root: true })*/

      resolve()
    })
  }

}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
