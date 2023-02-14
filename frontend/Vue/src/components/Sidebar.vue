<template>
    <div class="sidebar">
        <el-menu
            class="sidebar-el-menu"
            :default-active="onRoutes"
            :collapse="collapse"
            background-color="#324157"
            text-color="#bfcbd9"
            active-text-color="#20a0ff"
            unique-opened
            @select="handleSelect"
        >
            <template v-for="item in items">
                <template v-if="item.subs">
                    <el-submenu :index="item.index"   :key="item.index">
                        <template slot="title">
                            <i :class="item.icon"></i>
                            <span slot="title">{{ item.title }}</span>
                        </template>
                        <template v-for="subItem in item.subs">
                            <el-submenu
                                v-if="subItem.subs"
                                :index="subItem.index"
                                :key="subItem.index"
                            >
                                <template slot="title">{{ subItem.title }}</template>
                                <el-menu-item
                                    v-for="(threeItem,i) in subItem.subs"
                                    :key="i"
                                    :index="threeItem.index"
                                >{{ threeItem.title }}</el-menu-item>
                            </el-submenu>
                            <el-menu-item
                                v-else
                                :index="subItem.index"
                                :key="subItem.index"
                            >{{ subItem.title }}</el-menu-item>
                        </template>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.index" v-if="(item.index==='News'&&userinfo.isadmin===true)||(item.index==='Cases'&&userinfo.isadmin===true)||(item.index!=='Cases'&& item.index!=='News')" :key="item.index">
                        <i :class="item.icon"></i>
                        <span slot="title">{{ item.title }}</span>
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
import bus from './bus';
export default {
    data() {
        return {
          userinfo:'',
            collapse: false,
            items: [
                {
                    icon: 'el-icon-s-home',
                    index: 'dashboard',
                    title: '首页'
                },
                {
                  icon: 'el-icon-bank-card',
                  index: 'CertificateOfEntitlement',
                  title: '权益存证'
                },
                {
                  icon: 'el-icon-monitor',
                  index: 'BlockChainVerification',
                  title: '链上核验'
                },
              {
                icon: 'el-icon-shopping-cart-1',
                index: 'ResourceCenter',
                title: '资源中心'
              },
              {
                icon: 'el-icon-user',
                index: 'Mine',
                title: '我的'
              },
              {
                icon: 'el-icon-bell',
                index: 'MessageBox',
                title: '消息盒子'
              },
              {
                icon: 'el-icon-s-finance',
                index: 'RechargeCenter',
                title: '充值中心'
              },
              {
                icon: 'el-icon-folder',
                index: 'Cases',
                title: '案例管理'
              },
              {
                icon: 'el-icon-notebook-2',
                index: 'News',
                title: '新闻管理'
              }
            ]
        };
    },
  methods:{
      handleSelect(val){
        const route = this.$route.path;
        console.log(route)
        if(route.indexOf('ResourceDetail')!==-1)
        {
          this.$router.replace({path:'/system/'+val})
        }
        else
        {
          this.$router.push(val)
        }

      }
  },
    computed: {
        onRoutes() {

          return this.$route.path.replace('/system/', '');


        }
    },
    created() {
      this.userinfo = JSON.parse(sessionStorage.getItem("userInfo"));
        // 通过 Event Bus 进行组件间通信，来折叠侧边栏
        bus.$on('collapse', msg => {
            this.collapse = msg;
            bus.$emit('collapse-content', msg);
        });
    }
};
</script>

<style scoped>
.sidebar {
    display: block;
    position: absolute;
    left: 0;
    top: 70px;
    bottom: 0;
    overflow-y: scroll;
}
.sidebar::-webkit-scrollbar {
    width: 0;
}
.sidebar-el-menu:not(.el-menu--collapse) {
    width: 250px;
}
.sidebar > ul {
    height: 100%;
}
</style>
