<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item><i class="el-icon-monitor"></i> 链上核验</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <el-tabs type="border-card">
        <el-tab-pane label="交易哈希核验">
          <div>
            <el-alert
                title="交易哈希核验"
                type="info" :closable="false"
                description="交易哈希是版权区块链上存证的唯一证据，您可以核验您所持交易哈希是否是实际发生在链上的一笔交易从而得知您的版权信息是否运用区块链技术同步存储在树图区块链上，并可得到该笔交易的信息"
                show-icon>
            </el-alert>
            <el-form ref="form1" :model="form1" :rules="Rules1">
              <el-form-item label="交易哈希" prop="chainhash" >
                <el-input placeholder="请输入以0x开头的交易哈希" v-model="form1.chainhash"></el-input>
              </el-form-item>
            </el-form>
            <el-button type="primary" @click="handleChainhash" :loading="this.loading1">查询</el-button>
          </div>
          <div style="padding-top: 20px">
            <el-table
                :data="tableData1"
                style="width: 100% ">
              <el-table-column prop="name" label="名称" width="400%" >
                <template slot-scope="scope">
                  <el-popover trigger="hover" placement="top">
                    <p>{{ scope.row.decodeData }}</p>
                    <div slot="reference" class="name-wrapper">
                      <i class="el-icon-time"></i>
                      <span style="margin-left: 10px">{{ scope.row.name }}</span>
                    </div>
                  </el-popover>
                </template>
              </el-table-column>
              <el-table-column prop="value" label="值 " width="1000%" >
                <template slot-scope="scope">
                  <div slot="reference" class="name-wrapper">
                    {{ scope.row.value }}
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>

        </el-tab-pane>

        <el-tab-pane label="文件哈希核验">
          <div>
            <el-alert
                title="文件哈希核验"
                type="info" :closable="false"
                description="文件哈希值是由您上传的作品文件而得，它作为您版权信息的唯一标识，可用于与智能合约进行交互获取你在链上所存储的信息"
                show-icon>
            </el-alert>
            <el-form ref="form2" :model="form2" :rules="Rules2">
              <el-form-item label="文件哈希" prop="filehash">
                <el-input placeholder="请输入文件哈希" v-model="form2.filehash"></el-input>
              </el-form-item>
            </el-form>
            <el-button type="primary" @click="handleFilehash" :loading="this.loading2">查询</el-button>
          </div>

          <div style="padding-top: 20px">
            <el-table
                :data="tableData2"
                style="width: 100%">
              <el-table-column prop="name" label="名称" width="400%" >
                <template slot-scope="scope">
                  <el-popover trigger="hover" placement="top">
                    <p>{{ scope.row.decodeData }}</p>
                    <div slot="reference" class="name-wrapper">
                      <i class="el-icon-time"></i>
                      <span style="margin-left: 10px">{{ scope.row.name }}</span>
                    </div>
                  </el-popover>
                </template>
              </el-table-column>
              <el-table-column prop="value" label="值 " width="1000%" >
                <template slot-scope="scope">
                  <div slot="reference" class="name-wrapper">
                    {{ scope.row.value }}
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>


        </el-tab-pane>

        <el-tab-pane label="授权核验">
          <div>
            <el-alert
                title="授权核验"
                type="info" :closable="false"
                description="输入您的钱包地址以及您购买的版权文件哈希即可验证您是否拥有授权"
                show-icon>
            </el-alert>
            <el-form ref="form3" :model="form3" :rules="Rules3">
              <el-form-item label="文件哈希" prop="filehash">
                <el-input placeholder="请输入文件哈希" v-model="form3.filehash"></el-input>
              </el-form-item>
              <el-form-item label="钱包地址" prop="address">
                <el-input placeholder="请输入文件哈希" v-model="form3.address"></el-input>
              </el-form-item>
            </el-form>
            <el-button type="primary" @click="handleFilehash2" :loading="this.loading3">查询</el-button>
          </div>

          <div style="padding-top: 20px">
            <el-table
                :data="tableData3"
                style="width: 100%">
              <el-table-column prop="name" label="名称" width="400%" >
                <template slot-scope="scope">
                  <el-popover trigger="hover" placement="top">
                    <p>{{ scope.row.decodeData }}</p>
                    <div slot="reference" class="name-wrapper">
                      <i class="el-icon-time"></i>
                      <span style="margin-left: 10px">{{ scope.row.name }}</span>
                    </div>
                  </el-popover>
                </template>
              </el-table-column>
              <el-table-column prop="value" label="值 " width="1000%" >
                <template slot-scope="scope">
                  <div slot="reference" class="name-wrapper">
                    {{ scope.row.value }}
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>


        </el-tab-pane>
      </el-tabs>
    </div>

  </div>
</template>

<script>
import conflux from '../../conflux/conflux'
const {ContractHnuCoin,ContractCopyrightSystemTransactions,ContractCopyright} = require("@/conflux/conflux");
export default {
  name: "BlockChainVerification",
  data: function() {
    return {
      loading1:false,
      loading2:false,
      loading3:false,
      form1:{
        chainhash:'',
      },
      form2:{
        filehash:'',
      },
      form3:{
        filehash:'',
        address:'',
      },
      Rules1:{
        chainhash:[
          { required: true, message: '请输入交易哈希', trigger: 'blur' },
        ],
      },
      Rules2:{
        filehash:[
          { required: true, message: '请输入文件哈希', trigger: 'blur' },
        ],
      },
      Rules3:{
        filehash:[
          { required: true, message: '请输入文件哈希', trigger: 'blur' },
        ],
        address:[
          { required: true, message: '请输入钱包地址', trigger: 'blur' },
        ],
      },
      tableData1:[{
        name:"区块哈希",
        value:"暂无数据",
        decodeData:"区块哈希是一个唯一的66字符的标识符，该标识符在每次交易执行时都会生成"
      },{
        name:"纪元高度",
        value:"暂无数据",
        decodeData:"交易被实际执行的纪元高度"
      },{
        name:"发送方",
        value:"暂无数据",
        decodeData:"交易的发送方(可能来自合约地址)"
      },{
        name:"接收方",
        value:"暂无数据",
        decodeData:"交易的接收方(可以是合约地址)"
      },{
        name:"时间戳",
        value:"暂无数据",
        decodeData:"交易数据记录的日期和时间"
      }],

      tableData2:[{
        name:"著作权人链上地址",
        value:"暂无数据",
        decodeData:"该文件哈希对应的版权作品的著作权人在区块链中的地址，即公钥"
      },{
        name:"作品描述信息",
        value:"暂无数据",
        decodeData:"该文件哈希对应版权作品的文件描述信息，该链接为ipfs上的地址，可直接访问"
      },{
        name:"著作权人姓名",
        value:"暂无数据",
        decodeData:"该文件哈希对应版权作品的著作权人姓名"
      },{
        name:"著作权人证件号",
        value:"暂无数据",
        decodeData:"该文件哈希对应版权作品的著作权证件号"
      },
      {
        name:"购买著作权使用权列表",
        value:"暂无数据",
        decodeData:"该作品通过该买本著作权使用权的用户信息列表，可用于筛选某人是否通过正规渠道获得著作权使用权"
      }],

      tableData3:[{
        name:"是否得到授权",
        value:"暂无数据",
        decodeData:"true为得到授权，false为没得到授权"
      }],
    }
  },
  methods:{
    // 时间戳转换成时间
    timestampToTime (cjsj) {
      const date = new Date(cjsj * 1000); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
      const Y = date.getFullYear() + '-'
      const M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-'
      const D = date.getDate() + ' '
      const h = date.getHours() + ':'
      const m = date.getMinutes() + ':'
      const s = date.getSeconds()
      return Y+M+D+h+m+s;
    },
    handleChainhash(){
      this.$refs.form1.validate(async valid => {
        if (valid) {
          this.loading1 = true
          try {
            const result = await conflux.getTransactionByHash(this.form1.chainhash);
            console.log(result)
            console.log("adsads")

            const result1=await conflux.getBlockByHash(result.blockHash)
            console.log(result1)
            const timestamp=result1.timestamp;
            const date=this.timestampToTime(timestamp)
            this.tableData1=[{
              name:"区块哈希",
              value:result.blockHash,
              decodeData:"区块哈希是一个唯一的66字符的标识符，该标识符在每次交易执行时都会生成"
            },{
              name:"纪元高度",
              value:result.epochHeight,
              decodeData:"交易被实际执行的纪元高度"
            },{
              name:"发送方",
              value:result.from,
              decodeData:"交易的发送方(可能来自合约地址)"
            },{
              name:"接收方",
              value:result.to,
              decodeData:"交易的接收方(可以是合约地址)"
            },{
              name:"时间戳",
              value:timestamp+'('+date+')',
              decodeData:"交易数据记录的日期和时间"
            }]
            this.loading1=false;
          }catch (e)
          {
            console.log(e.message)
            this.$message({
              message: '查询失败，请检查交易哈希值是否正确',
              type: "error"
            });
            console.log(e.message)
            this.loading1=false;
          }

        } else {
          this.$message({
            message: "请输入合法的值",
            type: "error"
          });
          this.loading1=false;
        }
      });
    },
    handleFilehash(){
      this.$refs.form2.validate(async valid => {
        if (valid) {
          this.loading2 = true
          try {
            const workuri=await ContractCopyright.contract.workURI(this.form2.filehash);
            const owneraddress=await ContractCopyright.contract.ownerOf(this.form2.filehash);
            const ownername=await ContractCopyright.contract.workOwnername(this.form2.filehash);
            const ownerid=await ContractCopyright.contract.workOwnerid(this.form2.filehash);
            const downloadArr=await ContractCopyright.contract.workdownloaders(this.form2.filehash);
            let datalist=new Array();
            for(let i=0; i<downloadArr.length; i++)
            {
              const item = {
                address:downloadArr[i][0],
                name:downloadArr[i][1],
                idnumber:downloadArr[i][2]
              };
              datalist.push(item)
            }
            console.log(datalist)
            this.tableData2=[{
              name:"著作权人链上地址",
              value:owneraddress,
              decodeData:"该文件哈希对应的版权作品的著作权人在区块链中的地址，即公钥"
            },{
              name:"作品描述信息",
              value:workuri,
              decodeData:"该文件哈希对应版权作品的文件描述信息，该链接为ipfs上的地址，可直接访问"
            },{
              name:"著作权人姓名",
              value:ownername,
              decodeData:"该文件哈希对应版权作品的著作权人姓名"
            },{
              name:"著作权人证件号",
              value:ownerid,
              decodeData:"该文件哈希对应版权作品的著作权证件号"
            },
            {
              name:"购买著作权使用权列表",
              value:datalist,
              decodeData:"该作品通过该买本著作权使用权的用户信息列表，可用于筛选某人是否通过正规渠道获得著作权使用权"
            }]
            this.loading2=false;
          }catch (e)
          {
            console.log(e)
            this.$message({
              message: '查询失败，请检查交易哈希值是否正确',
              type: "error"
            });
            console.log(e.message)
            this.loading2=false;
          }

        } else {
          this.$message({
            message: "请输入合法的值",
            type: "error"
          });
          this.loading2=false;
        }
      });
      // const ret=await ContractCopyright.contract.workURI('test');
      // console.log(ret)
    },
    handleFilehash2(){
      this.$refs.form3.validate(async valid => {
        if (valid) {
          this.loading3 = true
          try {
            const isProve=await ContractCopyright.contract.isDownload(this.form3.filehash,this.form3.address);

            console.log(isProve)
            this.tableData3=[{
              name:"是否得到授权",
              value:isProve,
              decodeData:"true为得到授权，false为没得到授权"
            }]
            this.loading3=false;
          }catch (e)
          {
            console.log(e)
            this.$message({
              message: '查询失败，请检查文件哈希值及地址是否正确',
              type: "error"
            });
            console.log(e.message)
            this.loading3=false;
          }

        } else {
          this.$message({
            message: "请输入合法的值",
            type: "error"
          });
          this.loading3=false;
        }
      });
      // const ret=await ContractCopyright.contract.workURI('test');
      // console.log(ret)
    }

  },
}

</script>

<style scoped>
h1{
  text-align: center;
  margin: 30px 0;
}
p{
  line-height: 30px;
  margin-bottom: 10px;
  text-indent: 2em;
}
.logout{
  color: #409EFF;
}
</style>
