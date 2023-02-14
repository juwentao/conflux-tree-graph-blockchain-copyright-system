<template>
    <div class="header">
        <!-- 折叠按钮 -->
        <div class="collapse-btn" @click="collapseChage">
            <i v-if="!collapse" class="el-icon-s-fold"></i>
            <i v-else class="el-icon-s-unfold"></i>
        </div>
        <div class="logo">树链版权平台控制台</div>
        <div class="header-right">
            <div class="header-user-con">
              <div class="btn-connectToWallet" v-if="isConnected===true">
                <el-tooltip class="item" effect="dark" content="点击复制账号地址" placement="top-start">
                  <el-button  size="small" type="primary" v-clipboard:copy="account" v-clipboard:success="onCopySuccess" v-clipboard:error="onCopyError">{{account}}</el-button>
                </el-tooltip>
              </div>
              <div class="btn-connectToWallet" v-if="isConnected===false">
                <el-tooltip class="item" effect="dark" content="点击连接conflux钱包" placement="top-start">
                  <el-button v-if="isConnected===false" @click="connectToWallet" size="small" type="warning" >连接到钱包</el-button>
                </el-tooltip>
              </div>
              <!-- 全屏显示 -->
              <div class="btn-fullscreen" @click="handleFullScreen">
                <el-tooltip effect="dark" :content="fullscreen?`取消全屏`:`全屏`" placement="bottom">
                  <i class="el-icon-rank"></i>
                </el-tooltip>
              </div>
              <!-- 消息中心 -->
              <div class="btn-bell">
                <el-tooltip
                    effect="dark"
                    :content="message?`有${message}条未读消息`:`消息中心`"
                    placement="bottom"
                >
                  <router-link to="/system/MessageBox">
                    <i class="el-icon-bell"></i>
                  </router-link>
                </el-tooltip>
                <span class="btn-bell-badge" v-if="message"></span>
              </div>
              <!-- 用户头像 -->
              <div class="user-avator">
                <img src="../assets/img/img.jpg" />
              </div>
              <!-- 用户名下拉菜单 -->
              <el-dropdown class="user-name" trigger="click" @command="handleCommand">
                <span class="el-dropdown-link">
                  {{username}}
                  <i class="el-icon-caret-bottom"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item divided command="loginout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
        </div>
    </div>
</template>
<script>
import bus from './bus';

import confluxPortal from '../conflux/conflux-portal'
export default {
    data() {
        return {
          collapse: false,
          fullscreen: false,
          name: 'admin',
          message: 0,
          isConnected:false,
          account:''
        };
    },
    computed: {
        username() {
            let username = JSON.parse(sessionStorage.getItem("userInfo")).username;
            console.log("用户名"+JSON.parse(sessionStorage.getItem("userInfo")).username);
            return username ? username : this.name;
        }
    },
    methods: {
      getUnRead(){
        let username = JSON.parse(sessionStorage.getItem("userInfo")).username;
        this.$axios.post('/notice/getNotice', {
              'username':username,
              'type':0
            }
        ).then(res => {
          console.log(res)
          this.message = res.data.result.length
        })
      },
      onCopySuccess() {
        this.$message.success("复制成功");
      },
      onCopyError() {
        this.$message.error("复制失败");
      },
      async connectToWallet() {
        await confluxPortal.enable();
        const account = confluxPortal.getAccount();
        console.log(account);
        if(account==='')
        {
          this.isConnected=false;
        }
        else
        {
          this.isConnected=true;
          this.account=account;
          bus.$emit('refreshDashboard');
          bus.$emit('refreshRechargeCenter');

        }
      },
      // 用户名下拉菜单选择事件
      handleCommand(command) {
        if (command === 'loginout') {
          sessionStorage.removeItem("userInfo");
          sessionStorage.removeItem("token");
          this.$router.push('/');
        }
      },
      // 侧边栏折叠
      collapseChage() {
        this.collapse = !this.collapse;
        bus.$emit('collapse', this.collapse);
      },
      // 全屏事件
      handleFullScreen() {
        let element = document.documentElement;
        if (this.fullscreen) {
          if (document.exitFullscreen) {
            document.exitFullscreen();
          } else if (document.webkitCancelFullScreen) {
            document.webkitCancelFullScreen();
          } else if (document.mozCancelFullScreen) {
            document.mozCancelFullScreen();
          } else if (document.msExitFullscreen) {
            document.msExitFullscreen();
          }
        } else {
          if (element.requestFullscreen) {
            element.requestFullscreen();
          } else if (element.webkitRequestFullScreen) {
            element.webkitRequestFullScreen();
          } else if (element.mozRequestFullScreen) {
            element.mozRequestFullScreen();
          } else if (element.msRequestFullscreen) {
            // IE11
            element.msRequestFullscreen();
          }
        }
        this.fullscreen = !this.fullscreen;
      }
    },
  mounted() {
      console.log("1112323")
    if (document.body.clientWidth < 1500) {
      this.collapseChage();
    }
    this.getUnRead()
    bus.$on('message_change',(count)=>{
      this.message=count
    })
  },
  beforeDestroy() {
    bus.$off('message_change')
  }
};
</script>
<style scoped>
.header {
    position: relative;
    box-sizing: border-box;
    width: 100%;
    height: 70px;
    font-size: 22px;
    color: #fff;
}
.collapse-btn {
    float: left;
    padding: 0 21px;
    cursor: pointer;
    line-height: 70px;
}
.header .logo {
    float: left;
    width: 250px;
    line-height: 70px;
}
.header-right {
    float: right;
    padding-right: 50px;
}
.header-user-con {
    display: flex;
    height: 70px;
    align-items: center;
}

.btn-fullscreen {
  transform: rotate(45deg);
  margin-right: 5px;
  font-size: 24px;
}
.btn-bell,
.btn-connectToWallet{
  margin-right: 10px;
  position: relative;
  text-align: center;
  cursor: pointer;
}
.btn-fullscreen {
    position: relative;
    width: 30px;
    height: 30px;
    text-align: center;
    border-radius: 15px;
    cursor: pointer;
}
.btn-bell-badge {
    position: absolute;
    right: 0;
    top: -2px;
    width: 8px;
    height: 8px;
    border-radius: 4px;
    background: #f56c6c;
    color: #fff;
}
.btn-bell .el-icon-bell {
    color: #fff;
}
.user-name {
    margin-left: 10px;
}
.user-avator {
    margin-left: 20px;
}
.user-avator img {
    display: block;
    width: 40px;
    height: 40px;
    border-radius: 50%;
}
.el-dropdown-link {
    color: #fff;
    cursor: pointer;
}
.el-dropdown-menu__item {
    text-align: center;
}
</style>
