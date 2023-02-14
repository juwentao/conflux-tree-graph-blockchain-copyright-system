<template>
  <div id="app">
    <el-container>
      <el-header v-if="headerIsShow" >
        <div class="logo">
          <img src="./assets/img/logo5.png" alt />
        </div>
        <el-menu
            :default-active="$route.path"
            class="el-menu-demo"
            mode="horizontal"
            :router="router"
        >
          <el-menu-item index="/">首页</el-menu-item>
          <el-menu-item index="/news">新闻资讯</el-menu-item>
          <el-menu-item index="/case">经典案例</el-menu-item>
<!--          <el-menu-item index="/help">使用说明</el-menu-item>-->
<!--          <el-menu-item index="/about">了解树链</el-menu-item>-->
          <el-menu-item index="/login">登录</el-menu-item>
        </el-menu>
      </el-header>
      <el-main>
        <router-view v-on:header="header" v-on:footer="footer"></router-view>
      </el-main>
      <div class="footer" v-if="footerIsShow">
        <div class="footer-content">
          <ul class="content-nav">
            <li>
              <p>走进树图</p>
              <span>发展历程</span>
              <span>企业文化</span>
              <span>资质荣誉</span>
              <span>合作伙伴</span>
            </li>
            <li>
              <p>新闻资讯</p>
              <span>公司新闻</span>
              <span>行业动态</span>
            </li>
            <li>
              <p>产品中心</p>
              <span>介绍视频</span>
              <span>管理模式</span>
              <span>平台目标</span>
              <span>功能模块</span>
            </li>
            <li>
              <p>联系我们</p>
              <span>邮箱：841916140@qq.com</span>
              <span>电话：13036082950</span>
              <span>地址：湖南大学天马学生公寓</span>
            </li>
          </ul>
          <img src="./assets/img/ercode.png" alt />
        </div>
        <div class="copyright">
          <span style="text-align: center;display:block">树图区块链版权平台所有</span>
        </div>
      </div>
    </el-container>
  </div>
</template>

<script>

export default {
  data() {
    return {
      router: true,
      defaultActive: "/",
      footerIsShow: false,
      headerIsShow: true
    };
  },
  methods: {

    header:function (isShow){
      this.headerIsShow=isShow;
    },
    footer:function (isShow){
      console.log("footer被调用咯")
      this.footerIsShow=isShow;
    }
  },
  async created() {

  }
};
</script>

<style lang="scss">
* {
  padding: 0;
  margin: 0;
}
html,
body {
  height: 100%;
}

#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

.el-header {
  display: flex;
  justify-content: space-between;
  align-content: center;
  width: 1240px;
  margin: 0 auto;
  //background-color: #fff;

  .logo {
    width: 280px;
    padding: 10px;
    img {
      width: 100%;
      line-height: 60px;
    }
  }
}
.el-main {
  padding: 0 !important;
}

.footer {
  width: 100%;
  height: 240px;
  overflow: hidden;
  background-color: #14679f;
  &-content {
    width: 1240px;
    margin: 0 auto;
    padding-top: 20px;
    display: flex;
    justify-content: space-between;
    .content-nav {
      display: flex;
      justify-content: space-around;
      li {
        display: flex;
        flex-direction: column;
        padding: 0 20px;
        //justify-content: center;
        align-items: flex-start;
        p {
          font-size: 20px;
          color: #d4edff;
          padding: 10px 0;
        }
        span {
          color: #f7f7f7;
          font-weight: 300;
          padding: 5px 0;
        }
      }
    }
    img {
      width: 170px;
      height: 170px;
      padding: 10px;
    }
  }
  .copyright {
    height: 30px;
    background: #125688;
    span {
      color: #fff;
      line-height: 30px;
    }
  }

}
</style>