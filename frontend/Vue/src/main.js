import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import "./axios"
import "./conflux/conflux"
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

import './assets/css/icon.css';
import 'babel-polyfill';
Vue.use(ElementUI);

import VueAwesomeSwiper from 'vue-awesome-swiper'
import 'swiper/dist/css/swiper.css'
Vue.use(VueAwesomeSwiper);


import VueLazyLoad from 'vue-lazyload'
Vue.use(VueLazyLoad, {
  //完全显示的时候加载
  preLoad: 1,
  //失败时显示的图片
  error: require('./assets/img/error.png'),
  //加载时显示的GIF图
  loading: require('./assets/img/loading.gif'),
  //尝试加载几次
  attempt: 1
});

import axios from 'axios'
//设置超时时间
axios.defaults.timeout = 5000
//全局定义axios
Vue.prototype.$axios = axios


import Clipboard from 'v-clipboard'

Vue.use(Clipboard)


Vue.config.productionTip = false
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
