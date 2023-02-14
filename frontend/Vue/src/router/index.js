import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/Home.vue'

Vue.use(VueRouter)

const routes = [

  {
    path: '/',
    name: 'home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/case',
    name: 'case',
    component: () => import('../views/Case.vue')
  },
  {
    path: '/casedetails/:id',
    name: 'casedetails',
    component: () => import('../views/CaseDetails.vue')
  },
  {
    path: '/news',
    name: 'news',
    component: () => import('../views/News.vue')
  },
  {
    path: '/newsdetails/:id',
    name: 'newsdetails',
    component: () => import('../views/NewsDetails.vue'),
  },
  {
    path: '/help',
    name: 'help',
    component: () => import('../views/Help.vue')
  },
  {
    path: '/about',
    name: 'about',
    component: () => import('../views/About.vue')
  },
  {
    path: '/system',
    name: 'system',
    redirect: 'system/dashboard',
    component: () => import('../views/System.vue'),
    meta:{title:'控制台',requireAuth:true},
    children: [
      {
        path: '/system/dashboard',
        component: () => import(/* webpackChunkName: "dashboard" */ '../views/System/Dashboard.vue'),
        meta: { title: '首页',requireAuth:true }
      },
      {
        // 权限页面
        path: '/system/cases',
        component: () => import(/* webpackChunkName: "permission" */ '../views/System/Cases.vue'),
        meta: { title: '案例管理', requireAuth:true,permission: true }
      },
      {
        // 权限页面
        path: '/system/news',
        component: () => import(/* webpackChunkName: "permission" */ '../views/System/News.vue'),
        meta: { title: '新闻管理', requireAuth:true,permission: true }
      },
      {
        path: '/system/CertificateOfEntitlement',
        component: () => import(/* webpackChunkName: "CertificateOfEntitlement" */ '../views/System/CertificateOfEntitlement.vue'),
        meta: { title: '权益存证' , requireAuth:true}
      },
      {
        path: '/system/BlockChainVerification',
        component: () => import(/* webpackChunkName: "BlockChainVerificationvue" */ '../views/System/BlockChainVerification.vue'),
        meta: { title: '链上核验', requireAuth:true }
      },
      {
        path: '/system/ResourceCenter',
        name: 'ResourceCenter',
        component: () => import(/* webpackChunkName: "ResourceCenter" */ '../views/System/ResourceCenter.vue'),
        meta: { title: '资源中心', requireAuth:true }
      },
      {
        path: '/system/Mine',
        name: 'Mine',
        component: () => import(/* webpackChunkName: "ResourceCenter" */ '../views/System/Mine.vue'),
        meta: { title: '我的', requireAuth:true }
      },
      {
        name: 'resourceDetail',
        path: '/system/ResourceDetail/:sid',
        component: () => import(/* webpackChunkName: "dashboard" */ '../views/System/ResourceDetail.vue'),
        meta: { title: '资源详情',requireAuth:true }
      },
      {
        path: '/system/Verification',
        name: 'Verification',
        component: () => import(/* webpackChunkName: "ResourceCenter" */ '../views/System/Verification.vue'),
        meta: { title: '实名认证', requireAuth:true }
      },
      {
        path: '/system/RechargeCenter',
        name: 'RechargeCenter',
        component: () => import(/* webpackChunkName: "ResourceCenter" */ '../views/System/RechargeCenter.vue'),
        meta: { title: '充值中心', requireAuth:true }
      },
      {
        path: '/system/MessageBox',
        name: 'MessageBox',
        component: () => import(/* webpackChunkName: "ResourceCenter" */ '../views/System/MessageBox.vue'),
        meta: { title: '消息盒子', requireAuth:true }
      },
      {
        path: '/system/403',
        component: () => import(/* webpackChunkName: "403" */ '../views/System/403.vue'),
        meta: { title: '403' }
      },

    ]
  },
  {
    path: '*',
    name: '404',
    component: () => import(/* webpackChunkName: "404" */ '../views/System/404.vue'),
  }
]

const router = new VueRouter({
  mode: 'history',
  base: '/CopyrightSystem/',
  routes
})

// 判断是否需要登录权限 以及是否登录
router.beforeEach((to, from, next) => {
  // 判断是否需要登录权限
  if (to.matched.some(res => res.meta.requireAuth)) {
    // 判断是否登录
    if (sessionStorage.getItem('token')) {
      if(to.meta.permission)
      {
        let isadmin = JSON.parse(sessionStorage.getItem("userInfo")).isadmin
        // 如果是管理员权限则可进入，这里只是简单的模拟管理员权限而已
        isadmin === true ? next() : next('/system/403');
      }else{
        next()
      }
    } else {
      // 没登录则跳转到登录界面
      next({
        path: '/login',
         query: {
           redirect: to.fullPath
         }
      })
    }
  } else {
    next()
  }
})


export default router
