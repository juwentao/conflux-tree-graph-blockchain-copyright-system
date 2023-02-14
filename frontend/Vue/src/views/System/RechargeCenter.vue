<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-s-finance"></i>充值
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <el-card shadow="hover">
        <div slot="header" class="clearfix">
          <i class="el-icon-s-finance" /><span style="margin-left: 5px">当前可用余额(HNUB)</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="handleRefresh">刷新</el-button>
        </div>
        <div>
          <ul class="role-info1">
            <li>
              <div class="role-left1">
                {{this.account===''?'未连接钱包':this.balance}}
              </div>
            </li>
          </ul>
        </div>
        <el-button @click="recharge">
          充值
        </el-button>
      </el-card>
    </div>
    <el-dialog title="充值" :visible.sync="rechargeFormVisible" center>
      <el-form :model="rechargeForm" :rules="rechargeRules" ref="rechargeForm" label-width="auto" label-position="top">
        <el-form-item label="充值金额(HNUB)" prop="amount">
          <el-input-number v-model="rechargeForm.amount" :precision="2" :min="0.01" :max="1000000" :step="1" ></el-input-number>
        </el-form-item>
        <el-form-item label="充值密钥" prop="key">
          <el-input v-model.trim="rechargeForm.key" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="rechargeFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveRecharge" v-loading.fullscreen.lock="loading">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import confluxPortal from '../../conflux/conflux-portal'
const {ContractHnuCoin,ContractCopyrightSystemTransactions,ContractCopyright} = require("@/conflux/conflux");
import {Drip} from "js-conflux-sdk";
import bus from '../../components/bus';

export default {
  name: "RechargeCenter",
  data(){
    return{
      loading:false,
      balance:'',
      userinfo: '',
      account:'',
      rechargeFormVisible:false,
      rechargeForm:{
        amount:'',
        key:'',
      },
      rechargeRules:{
        key:[
          { required: true, message: '请输入密钥', trigger: 'blur' },
        ],
        amount:[
          { required: true, message: '请输入金额', trigger: 'blur' },
        ],
      },
    };
  },
  methods:{
    async handleRefresh() {
      this.account = confluxPortal.getAccount();
      this.balance='加载中...';
      this.balance=Drip(await ContractHnuCoin.contract.balanceOf(this.account)).toCFX()+'HNUB';
    },
    async recharge(){
      if(this.userinfo.iscertified===false)
      {
        this.$confirm('你还未实名认证，请前往认证!', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$router.push({ name: "Verification" });
          return false;
        }).catch(() => {
          return false;
        });
      }
      else
      {
        await confluxPortal.enable();
        this.account =confluxPortal.getAccount();
        if (this.account === '')
        {
          this.$message({
            message: '您还未连接conflux钱包插件',
            type: 'warning'
          });
        }
        else{
          this.rechargeFormVisible=true;
        }
      }
    },
    saveRecharge(){
      this.$refs.rechargeForm.validate(async valid => {
        if(valid)
        {
          if(this.rechargeForm.key==='david666')
          {
            this.loading=true;
            let amount=Drip.fromCFX(this.rechargeForm.amount).toString()
            try {
              const called = ContractCopyrightSystemTransactions.contract.getMoney.call(amount)
              const transaction = await confluxPortal.sendTransaction({
                from: this.account,
                to: called.to,
                data: called.data,
              })
              const txHash = transaction.result;
              console.log(txHash)
              this.$message({
                message: "充值成功，请刷新",
                type: "success"
              });
              await this.handleRefresh();
              bus.$emit('refreshDashboard');
              this.loading=false;
              this.rechargeFormVisible=false;

            } catch (e) {
              //上链失败
              this.$message({
                message: "充值失败" ,
                type: "error"
              });
              this.loading=false;
            }
          }
          else
          {
            this.$message({
              message: "密钥错误",
              type: "error"
            });
          }
        }
        else
        {
          this.$message({
            message: "请输入合法的值",
            type: "error"
          });
          return false;
        }


      })
    }
  },
  async mounted() {
    this.account = confluxPortal.getAccount();
    this.balance = '加载中...';
    this.balance = Drip(await ContractHnuCoin.contract.balanceOf(this.account)).toCFX() + 'HNUB';
  },
  created() {
    this.userinfo = JSON.parse(sessionStorage.getItem("userInfo"));

    bus.$on('refreshRechargeCenter', () => {
      console.log("调用咯")
      this.handleRefresh();
    })
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
.role-info1 {
  margin-top: 0;
  padding-top: 0;
  padding-left: 0;
  list-style: none;
li {
  border-bottom: 1px solid #f0f3f4;
  padding: 11px 0;
  font-size: 20px;
}
.role-left1 {
  color: rgb(148, 137, 137);
  overflow: hidden;
  white-space: nowrap;
  text-align: left;
  text-overflow: ellipsis;
}

.line {
  width: 100%;
  height: 1px;
  border-top: 1px solid #ccc;
}
}
</style>