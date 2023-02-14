<template>
  <div class="error-page">
      <div class="error-code">4<span>0</span>4</div>
      <div class="error-desc">啊哦~ 你所访问的页面不存在</div>
      <div class="error-handle">
          <router-link to="/">
            <el-button type="primary" size="large">返回首页</el-button>
          </router-link>
          <el-button class="error-btn" type="primary" size="large" @click="handleConflux">返回上一页</el-button>
      </div>
  </div>
</template>

<script>
import confluxPortal from '../../conflux/conflux-portal'
const {ContractHnuCoin,ContractCopyrightSystemTransactions,ContractCopyright} = require("@/conflux/conflux");
import {Drip} from "js-conflux-sdk";


export default {
  methods: {
      goBack() {
        this.$router.go(-1);
      },
    async handleConflux() {
      //chainhash为空 说明未上链验证，需要进行上链验证的步骤及上链成功后数据写入数据库的
      console.log("开始上链咯")
      //开始上链
      try {
        console.log("第1部分")
        let amount = Drip.fromCFX(10).toString()
        console.log("第2部分")
        let filehash="1EB90A5DD8DF993856DB9C53EFC6BA19"
        let workuri="https://ipfs.io/ipfs/QmXVRdYeyhriQm2xNxSX5wHehvmDa3cPV8h3cP3t2pMdYG"
        let copyrightownername="吕斐"
        let copyrightownerid="10"
        let account="net1024:aattfkz7a61z9rsgz847rv08ycapv6brpjz3yr1bah"
        const called = ContractCopyrightSystemTransactions.contract.addWorks.call(filehash,workuri, copyrightownername, copyrightownerid, amount)
        console.log(account)
        console.log("asdadsadadsas")
        console.log(called.to)
        console.log("asdadsadadsas")
        console.log(called.data)
        const transaction = await confluxPortal.sendTransaction({
          from: account,
          to: called.to,
          data: called.data,
        })
        console.log("第4部分")
        const txHash = transaction.result;
        console.log(txHash)
      } catch (e) {
        //上链失败
        this.$message({
          message: "权益存证上链失败，请重试",
          type: "error"
        });
        console.log(e.message)
        this.loading = false;
      }
    }
  }
}
</script>


<style scoped>
    .error-page{
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        width: 100%;
        height: 100%;
        background: #f3f3f3;
        box-sizing: border-box;
    }
    .error-code{
        line-height: 1;
        font-size: 250px;
        font-weight: bolder;
        color: #2d8cf0;
    }
    .error-code span{
        color: #00a854;
    }
    .error-desc{
        font-size: 30px;
        color: #777;
    }
    .error-handle{
        margin-top: 30px;
        padding-bottom: 200px;
    }
    .error-btn{
        margin-left: 100px;
    }
</style>
