import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // token: '',
    // userInfo: JSON.parse(sessionStorage.getItem("userInfo"))
  },
  getters: {
    // get
    // getUser: state => {
    //   return state.userInfo
    // },
    // getToken: state => {
    //   return state.token
    // }
  },
  mutations: {
    // set
    // SET_TOKEN: (state, token) => {
    //   state.token = token
    //   sessionStorage.setItem("token", token)
    // },
    // SET_USERINFO: (state, userInfo) => {
    //   state.userInfo = userInfo
    //   sessionStorage.setItem("userInfo", JSON.stringify(userInfo))
    // },
    // REMOVE_INFO: (state) => {
    //   state.token = ''
    //   state.userInfo = {}
    //   sessionStorage.removeItem("token")
    //   sessionStorage.removeItem("userInfo")
    // }
  },
  actions: {
  },
  modules: {
  }
})
