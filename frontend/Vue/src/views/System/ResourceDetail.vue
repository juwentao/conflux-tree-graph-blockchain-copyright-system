<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-bank-card"></i> 资源详情
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container" v-loading="loading">
      <el-row :gutter="10">
        <el-col :span="18">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>{{resourceData.title}}</span>
              <div style="display: inline-block;">
                <el-tag effect="plain" type="success" style="margin-left: 6px" v-if="resourceData.type===1">摄影作品</el-tag>
                <el-tag effect="plain" type="success" style="margin-left: 6px" v-if="resourceData.type===2">文字作品</el-tag>
                <el-tag effect="plain" type="success" style="margin-left: 6px" v-if="resourceData.type===3">影视作品</el-tag>
                <el-tag effect="plain" type="success" style="margin-left: 6px" v-if="resourceData.type===4">音乐作品</el-tag>
                <el-tag effect="plain" type="success" style="margin-left: 6px" v-if="resourceData.type===5">美术作品</el-tag>
              </div>
              <div style="display: inline-block;margin-left: 5px">
                <el-tooltip placement="top">
                  <div slot="content">
                    该作品已由该用户权益存证并授权售卖，若有疑问请反馈系统<br/>
                  </div>
                  <el-button type="primary" size="small "> 版权</el-button>>
                </el-tooltip>
              </div>
            </div>
            <div class="detail">
              <el-row :gutter="5">
                <el-col >
                  <span>介绍:{{resourceData.description}}</span>
                </el-col>
              </el-row>
              <el-row :gutter="5" style="margin-top: 10px" >
                <el-col >
                  <span>作品名称:{{resourceData.workname}}</span>
                </el-col>
              </el-row>
              <el-row :gutter="5" style="margin-top: 10px" >
                <el-col>
                  <span>文件名称:{{resourceData.filename}}</span>
                </el-col>
              </el-row>
              <el-row :gutter="5" style="margin-top: 10px" >
                <el-col :span="5">
                  <span>已授权数量:{{resourceData.downloads}}</span>
                </el-col>

                <el-col :span="5">
                  <span>文件大小:{{resourceData.filesize}} 字节</span>
                </el-col>
              </el-row>
              <el-row :gutter="5" style="margin-top: 10px">
                <el-col :span="12">
                  <span>该作品已存证上链,在第 {{resourceData.blockheight}} 区块,链上时间戳为 {{resourceData.chaintime}} </span>
                </el-col>
              </el-row>
              <el-row :gutter="5" style="margin-top: 10px">
                <el-col :span="12">
                  <span>作品文件哈希值: {{resourceData.filehash}}</span>
                </el-col>
              </el-row>
              <el-row :gutter="5" style="margin-top: 10px">
                <el-col :span="5">
                  <span>著作权人姓名 {{resourceData.ownername}}  </span>
                </el-col>
                <el-col :span="10">
                  <span>著作权人证件号 {{resourceData.ownerid}}  </span>
                </el-col>
              </el-row>
            </div>
            <div style="margin-top: 15px">
              <el-tooltip v-if="resourceData.status===1" placement="top">
                <div slot="content">购买该版权作品的许可使用权<br/>即获得该作品作者的授权使用</div>
                <el-button  icon="el-icon-question" round type="primary" @click="buyUsageRights" v-loading.fullscreen.lock="buyLoading">{{resourceData.price}} HNUB 购买使用权</el-button>
              </el-tooltip>
              <el-tooltip v-if="resourceData.status===2" placement="top">
                <div slot="content">购买该版权作品的著作权<br/>即购买后您将成为该版权作品的著作权人</div>
                <i class="el-icon-question"></i>>
                <el-button  icon="el-icon-question" round type="warning" @click="buyCopyright" v-loading.fullscreen.lock="buyLoading">{{resourceData.price}} HNUB 购买著作权</el-button>
              </el-tooltip>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>作品用户</span>
            </div>
            <div>
              <div style="text-align: center">
                <div class="el-upload">
                  <img src="../../assets/img/img.jpg" />
                </div>
              </div>
              <div style="text-align: center">
                <ul class="user-info">
                  <li>
                    <div style="height: 100%">
                      <i class="el-icon-user" /> {{ resourceData.username }}
                      <el-tag size="mini">已实名</el-tag>
                    </div>
                  </li>
                  <li>
                    <el-button @click="sendMessage" round >私信</el-button>
                  </li>
                </ul>
              </div>

            </div>
          </el-card>
        </el-col>
      </el-row>

    </div>
    <div style="margin-top: 10px" class="main" v-loading="loading1">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>评论</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="getComment">刷新</el-button>
        </div>
        <div class="item" v-for="(item,i) in commentData" :key="i">
          <div class="comment">
            <div class="user-popover-box">
              <el-avatar class="header-img" :src="defaultAvatar"></el-avatar>
<!--              <img src="../../assets/img/img.jpg" />-->
            </div>
            <div class="content-box1">
              <div class="meta-box">
                <span class="username">{{item.username}}</span>
              </div>
              <div class="content1">{{item.content}}</div>
              <div class="reply-stat">
                <span class="author-time">{{item.date}}</span>
              </div>
            </div>
          </div>
        </div>

      </el-card>
    </div>
    <div class="pagination">
      <el-pagination
          background
          layout="total, prev, pager, next"
          :current-page="query.pageIndex"
          :page-size="query.pageSize"
          :total="pageTotal"
          @current-change="handlePageChange">
      </el-pagination>
    </div>


  </div>
</template>

<script>
import confluxPortal from '../../conflux/conflux-portal'
import bus from '../../components/bus';
const {ContractHnuCoin,ContractCopyrightSystemTransactions,ContractCopyright} = require("@/conflux/conflux");
import conflux from '../../conflux/conflux'
const {Drip} = require("js-conflux-sdk");
export default {
  name: "ResourceDetail",
  data(){
    return{
      query: {
        saleid: '',
        pageIndex: 1,
        pageSize: 10
      },
      pageTotal: 0,
      commentData:'',
      loading:false,
      loading1:false,
      sid:'',
      resourceData:'',
      defaultAvatar: "http://120.48.51.66:8083/copyright/works/downloadWorks?fileLocation=C%3A%5Capache-tomcat-8.5.68%5Cwebapps%5Ccopyright&fileName=defaultAvatar.jpg",
      account:'',
      buyLoading:false,
      userinfo:'',
      isBuy:'',
    };
  },
  methods:{
    sendMessage()
    {
      if(this.userinfo.username===this.resourceData.username)
      {
        this.$message({
          type: 'warning',
          message: '不能给自己发私信噢'
        })
        return false;
      }
      this.$prompt('请输入私信内容', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: '',
        inputErrorMessage: ''
      }).then(({ value }) => {
        let username = this.userinfo.username;
        this.$axios.post('/notice/send',{
          'sendName':username,
          'receiveName':this.resourceData.username,
          'content':value
        }).then(
            this.$message({
              type: 'success',
              message: '发送成功，请在消息盒子中等待回复'
            })
        )
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消发送'
        });
      });
    },

    async buyUsageRights(){
      console.log(111)
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
        if(this.resourceData.userid===this.userinfo.id)
        {
          this.$message({
            message: '不能购买自己的作品哦',
            type: 'warning'
          });
          return false;
        }
        if(this.isBuy===true)
        {
          this.$message({
            message: '已经购买过该作品了哦',
            type: 'warning'
          });
          return false;
        }
        await confluxPortal.enable();
        this.account = confluxPortal.getAccount();
        if (this.account === '') {
          this.$message({
            message: '您还未连接conflux钱包插件',
            type: 'warning'
          });
        }
        else
        {
          this.buyLoading=true;
          const balance=Drip(await ContractHnuCoin.contract.balanceOf(this.account)).toCFX();
          if(balance<this.resourceData.price)
          {
            this.$confirm('余额不足', '提示', {
              confirmButtonText: '去充值',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.buyLoading=false;
              this.$router.push({ name: "RechargeCenter" });
              return false;
            }).catch(() => {
              this.buyLoading=false;
              return false;
            });
          }
          else
          {
            try {
              const price=Drip.fromCFX(this.resourceData.price).toString();
              console.log(this.resourceData.filehash)
              const called = ContractCopyrightSystemTransactions.contract.download.call(this.resourceData.filehash,price,this.userinfo.realname,this.userinfo.idnumber)
              const transaction = await confluxPortal.sendTransaction({
                from: this.account,
                to: called.to,
                data: called.data,
              })
              const txHash=transaction.result;
              console.log(txHash)

              //上链成功,将交易哈希值存入数据库
              const params =  new FormData()
              params.append('saleid',  this.resourceData.sid)
              params.append('buyid',  this.userinfo.id)
              params.append('sellid',  this.resourceData.userid)
              params.append('workid',this.resourceData.workid)
              params.append('tradeprice',this.resourceData.price)
              params.append('txhash',txHash)
              params.append('status',this.resourceData.status)
              this.$axios.post(`/trade/buyUsageRights`,params).then(response => {
                if (response.data.code==='200') {
                  this.$message({
                    message: "购买成功，请前往 我的-我的购买 中查看",
                    type: "success"
                  });
                  this.buyLoading=false;
                  bus.$emit('refreshResourceCenter');
                  bus.$emit('refreshGetTradeData');
                  this.getData();
                } else {
                  this.$message({
                    message: "失败，请重试",
                    type: "error"
                  });
                  this.buyLoading=false;
                }
              }).catch(e => {
                this.$message({
                  message: "网络异常！",
                  type: "error"
                });
                this.buyLoading=false
              });
            }catch (e){
              //上链失败
              this.$message({
                message: "购买失败，请重试",
                type: "error"
              });
              console.log(e.message)
              this.buyLoading=false;
            }
          }
        }
      }
    },
    sleep(time) {
      return new Promise(resolve =>
          setTimeout(resolve,time)
      )},
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
    async buyCopyright(){
      console.log(111)
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
        if(this.resourceData.userid===this.userinfo.id)
        {
          this.$message({
            message: '不能购买自己的作品哦',
            type: 'warning'
          });
          return false;
        }
        await confluxPortal.enable();
        this.account = confluxPortal.getAccount();
        if (this.account === '') {
          this.$message({
            message: '您还未连接conflux钱包插件',
            type: 'warning'
          });
        }
        else
        {
          this.buyLoading=true;
          const balance=Drip(await ContractHnuCoin.contract.balanceOf(this.account)).toCFX();
          if(balance<this.resourceData.price)
          {
            this.$confirm('余额不足', '提示', {
              confirmButtonText: '去充值',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.buyLoading=false;
              this.$router.push({ name: "RechargeCenter" });
              return false;
            }).catch(() => {
              this.buyLoading=false;
              return false;
            });
          }
          else
          {
            try {
              const price=Drip.fromCFX(this.resourceData.price).toString();
              console.log(this.resourceData.filehash)
              const called = ContractCopyrightSystemTransactions.contract.dealDone.call(this.resourceData.filehash,price,this.userinfo.realname,this.userinfo.idnumber)
              const transaction = await confluxPortal.sendTransaction({
                from: this.account,
                to: called.to,
                data: called.data,
              })
              const txHash=transaction.result;

              console.log(txHash)
              let out = await this.sleep(10000);
              console.log("延时10s");
              const result = await conflux.getTransactionByHash(txHash);
              console.log(result);
              const result1=await conflux.getBlockByHash(result.blockHash)
              console.log(result.epochHeight);
              const blockheight=result.epochHeight;
              const timestamp=result1.timestamp;
              const chaintime=timestamp+"("+this.timestampToTime(timestamp)+")"
              //上链成功,将交易哈希值存入数据库
              const params =  new FormData()
              params.append('saleid',  this.resourceData.sid)
              params.append('buyid',  this.userinfo.id)
              params.append('sellid',  this.resourceData.userid)
              params.append('workid',this.resourceData.workid)
              params.append('tradeprice',this.resourceData.price)
              params.append('txhash',txHash)
              params.append('status',this.resourceData.status)
              params.append('blockheight',blockheight)
              params.append('chaintime',chaintime)
              this.$axios.post(`/trade/buyCopyright`,params).then(response => {
                if (response.data.code==='200') {
                  this.$message({
                    message: "购买成功，请前往 我的-我的购买 中查看",
                    type: "success"
                  });
                  this.buyLoading=false;
                  bus.$emit('refreshResourceCenter');
                  bus.$emit('close_current_tags');
                  bus.$emit('refreshGetTradeData');
                } else {
                  this.$message({
                    message: "失败，请重试",
                    type: "error"
                  });
                  this.buyLoading=false;
                }
              }).catch(e => {
                this.$message({
                  message: "网络异常！",
                  type: "error"
                });
                this.buyLoading=false
              });
            }catch (e){
              //上链失败
              this.$message({
                message: "购买失败，请重试",
                type: "error"
              });
              console.log(e.message)
              this.buyLoading=false;
            }
          }
        }
      }
    },

    getData() {
      console.log(this.$route.params.sid)
      this.loading = true
      this.$axios.get('/sale/getDetailById',{
        params:{
          sid:this.sid
        }
      }).then(response => {
        if (response.data.code==='200') {
          this.resourceData=response.data.result
          this.getIsBuy();
          this.loading = false
        } else {
          this.$message({
            message: "获取详情失败",
            type: "error"
          });
          this.loading = false
        }
      }).catch(e => {
        this.$message({
          message: "网络异常！",
          type: "error"
        });
        this.loading = false
      });
    },
    getComment()
    {
      this.loading1 = true
      this.$axios.get('/comment/getCommentList',{
        params:{
          saleid:this.sid,
          currentPage:this.query.pageIndex,
          pageSize:this.query.pageSize
        }
      }).then(response => {
        if (response.data.code==='200') {
          this.commentData=response.data.result.record
          this.pageTotal=response.data.result.page.total
          this.loading1 = false
        } else {
          this.$message({
            message: "获取评论列表失败",
            type: "error"
          });
          this.loading1 = false
        }
      }).catch(e => {
        this.$message({
          message: "网络异常！",
          type: "error"
        });
        this.loading1 = false
      });
    },
    getIsBuy()
    {
      console.log("workid",this.resourceData.workid)
      this.$axios.get(`/trade/isBuy`,{
        params:{
          userid:this.userinfo.id,
          workid:this.resourceData.workid
        }
      }).then(response => {
        if (response.data.code==='200') {
          this.isBuy=response.data.result;
        } else {
          console.log(response.data)
        }
      }).catch(e => {
        this.$message({
          message: "网络异常！",
          type: "error"
        });
      });
    },
    handlePageChange(val) {
      this.$set(this.query, 'pageIndex', val);
      this.getComment();
    },
  },
  created () {
    console.log('created', this.$route)
    this.sid=this.$route.params.sid;
    this.userinfo=JSON.parse(sessionStorage.getItem("userInfo"))
    this.getData();
    this.getComment();
    bus.$on('refreshGetComment', () => {
      console.log("调用咯")
      this.getComment();
    })
  },
  mounted () {
    console.log('mounted', this.$route.params.sid)
  }
}

</script>

<style lang="stylus" scoped>
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}
.detail{
  font-size: 12px;
  color: rgb(148, 137, 137);
}

li{
  list-style-type :none;
}
.main {
  margin: 0 auto;

.item {
  margin-bottom: 15px;
  padding: 10px;
  .comment {
  display: flex;

  .user-popover-box {
  height: 40px;
  }

  .content-box1 {
    border-bottom: 1px solid #f1f1f1;
    margin-left: 15px;
    flex: 1 1 auto;
    text-align: left;

    .meta-box {
      display: flex;
      align-items: center;
      font-size: 14px;
      line-height: 1.2;
      white-space: nowrap;
    }
    .content1 {
      margin-top: 5px;
      font-size: 16px;
      line-height: 1.8;
      word-wrap: break-word;
      white-space: pre-wrap;
      word-break: break-all;
      color: #505050;
    a{
      display:inline-block;
      margin:0 4px;
      font-size: 14px;
      font-weight: 400;
      color: #406599;
      cursor:pointer;
    }
}
.reply-stat {
  display: flex;
  margin-top: 10px;
  font-weight: 400;
.author-time {
  font-size: 14px;
  color: #8a9aa9;
  cursor: default;
}
}
}
}
}
}
</style>