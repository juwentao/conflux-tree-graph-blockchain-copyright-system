<template>
  <div>
    <button @click="handleConflux">sadadd</button>
  </div>
</template>

<script>
import confluxPortal from "@/conflux/conflux-portal";
import {Drip} from "js-conflux-sdk";
import conflux, {ContractCopyrightSystemTransactions, ContractHnuCoin} from "@/conflux/conflux";
import bus from "@/components/bus";

export default {
  name: "test",
  methods: {
    //上链存证
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
        let copyrightownerid=10

        const called = ContractCopyrightSystemTransactions.contract.addWorks.call(filehash,workuri, copyrightownername, copyrightownerid, amount)
        console.log(this.account)
        console.log("asdadsadadsas")
        console.log(called.to)
        console.log("asdadsadadsas")
        console.log(called.data)
        const transaction = await confluxPortal.sendTransaction({
          from: this.account,
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

</style>