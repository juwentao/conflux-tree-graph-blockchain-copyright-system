<template>
    <div>
      <el-row :gutter="0" >
        <el-card shadow="hover" class="mgb20" style="height:100%;">
          <div class="user-info">
            <img src="../../assets/img/img.jpg" class="user-avator" alt />
            <div class="user-info-cont">
              <div class="user-info-name">{{this.userinfo.username}}</div>
              <div>{{this.userinfo.isadmin===true?'管理员':'普通用户'}}</div>
            </div>
          </div>
          <el-descriptions class="margin-top" title="个人信息" :column="3"  border>
            <template slot="extra">
              <el-button type="primary" size="small" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            </template>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-user"></i>
                账号类型
              </template>
              <el-tag v-if="userinfo.type===true" >个人账号</el-tag>
              <el-tag v-if="userinfo.type===false">机构账号</el-tag>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class=el-icon-s-open></i>
                是否实名
              </template>
              <el-tag type="success" v-if="userinfo.iscertified===true" >已认证</el-tag>
              <el-tag type="danger" v-if="userinfo.iscertified===false">未认证</el-tag>
              <el-button v-if="userinfo.iscertified===false" type="text" size="small" style="margin-left: 10px" @click="certification">去实名</el-button>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-s-custom"></i>
                {{this.userinfo.type===true?' 姓名':'机构名称'}}
              </template>
              {{this.userinfo.realname}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-tickets"></i>
                {{this.userinfo.type===true?' 身份证号':'统一社会信用代码证书'}}
              </template>
              {{this.userinfo.idnumber}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-phone"></i>
                手机号
              </template>
              {{this.userinfo.phone}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-alarm-clock"></i>
                注册时间
              </template>
              {{this.userinfo.created}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-s-cooperation"></i>
                湖大币余额
              </template>
              {{this.account===''?'未连接钱包':this.balance}}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-row>

      <el-row :gutter="20" class="mgb20">
        <el-col :span="8">
          <el-card shadow="hover" :body-style="{padding: '0px'}">
            <div class="grid-content grid-con-1">
              <i class="el-icon-bank-card grid-con-icon"></i>
              <div class="grid-cont-right">
                <div class="grid-num">{{this.worksnum}}</div>
                <div style="margin-top: 10px">权益存证</div>
                <div style="margin-top: 10px">
                  <el-button type="primary">
                    <router-link to="/system/CertificateOfEntitlement">查看全部</router-link>
                  </el-button>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" :body-style="{padding: '0px'}">
            <div class="grid-content grid-con-2">
              <i class="el-icon-shopping-cart-1 grid-con-icon"></i>
              <div class="grid-cont-right">
                <div class="grid-num">{{this.salenum}}</div>
                <div style="margin-top: 10px">资源中心</div>
                <div style="margin-top: 10px">
                  <el-button type="success">
                    <router-link to="/system/ResourceCenter">前往查看</router-link>
                  </el-button>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" :body-style="{padding: '0px'}">
            <div class="grid-content grid-con-3">
              <i class="el-icon-plus grid-con-icon"></i>
              <div class="grid-cont-right">
                <div class="grid-num">{{this.income}} HNUB</div>
                <div style="margin-top: 10px">今日收益</div>
                <div style="margin-top: 10px">
                  <el-button type="danger">
                    <router-link to="/system/Mine">查看详情</router-link>
                  </el-button>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
</template>

<script>
import confluxPortal from '../../conflux/conflux-portal'
import bus from '../../components/bus';
import {Drip} from "js-conflux-sdk";

const {ContractHnuCoin} = require("@/conflux/conflux");
export default {
    name: 'dashboard',
    components: {

    },
    data() {
      return {
        userinfo: '',
        message: 'first',
        showHeader: false,
        account:'',
        balance:'',
        worksnum:'',
        salenum:'',
        income:'',
      };
    },

  computed: {

  },
  created() {
      console.log("dashboard created")
    this.userinfo = JSON.parse(sessionStorage.getItem("userInfo"));
    this.userinfo.created=this.userinfo.created.toLocaleString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '');
    if(this.userinfo.iscertified===false)
    {
      this.open();
    }
    console.log(this.userinfo);
    bus.$on('refreshDashboard', () => {
      console.log("调用咯")
      this.handleRefresh();
    })
    this.getWorkNum();
    this.getOnSaleNum();
    this.getIncome();
  },
  mounted() {
  },
  methods: {
    getWorkNum()
    {
      this.$axios.get(`/works/getWorksNum`,{
        params:{
          userId:this.userinfo.id
        }
      }).then(response => {
        if (response.data.code==='200') {
          this.worksnum=response.data.result.num;
        }
      }).catch(e => {

      });
    },
    getOnSaleNum()
    {
      this.$axios.get(`/sale/getOnSaleNum`,{
        params:{
        }
      }).then(response => {
        if (response.data.code==='200') {
          this.salenum=response.data.result.num;
        }
      }).catch(e => {

      });
    },
    getIncome()
    {
      this.$axios.get(`/trade/getIncome`,{
        params:{
          userid:this.userinfo.id
        }
      }).then(response => {
        if (response.data.code==='200') {
          this.income=response.data.result.income;
        }
      }).catch(e => {

      });
    },
    open() {
      this.$confirm('你还未实名认证，请前往认证，否则无法正常使用本系统的功能', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.certification();
      }).catch(() => {

      });
    },
    certification()
    {
      this.$router.push({ name: "Verification" });
    },
    async handleRefresh() {
      this.getWorkNum();
      this.getOnSaleNum();
      this.getIncome();
      await confluxPortal.enable();
      this.account = confluxPortal.getAccount();
      this.balance='加载中...';
      this.balance=Drip(await ContractHnuCoin.contract.balanceOf(this.account)).toCFX()+'HNUB';
    },
  }
};
</script>


<style scoped>
.el-row {
    margin-bottom: 20px;
}



.grid-content {
  display: flex;
  align-items: center;
  height: 200px;
}

.grid-cont-right {
  flex: 1;
  text-align: center;
  font-size: 20px;
  color: #999;
}

.grid-num {
  font-size: 30px;
  font-weight: bold;
}

.grid-con-icon {
  font-size: 50px;
  width: 200px;
  height: 200px;
  text-align: center;
  line-height: 200px;
  color: #fff;
}

.grid-con-1 .grid-con-icon {
  background: rgb(45, 140, 240);
}

.grid-con-1 .grid-num {
  color: rgb(45, 140, 240);
}

.grid-con-2 .grid-con-icon {
  background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
  color: rgb(100, 213, 114);
}

.grid-con-3 .grid-con-icon {
  background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
  color: rgb(242, 94, 67);
}

.user-info {
    display: flex;
    align-items: center;
    padding-bottom: 20px;
    border-bottom: 2px solid #ccc;
    margin-bottom: 20px;
}

.user-avator {
    width: 120px;
    height: 120px;
    border-radius: 50%;
}

.user-info-cont {
    padding-left: 20px;
    flex: 1;
    font-size: 14px;
    color: #999;
}

.user-info-cont div:first-child {
    font-size: 30px;
    color: #222;
}



.user-info-list span {
    margin-left: 70px;
}

.mgb20 {
    margin-bottom: 20px;
}

.message-title{
  cursor: pointer;
}
.handle-row{
  margin-top: 30px;
}
</style>
