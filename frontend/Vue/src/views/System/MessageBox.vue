<template>
  <div class="container">
    <div class="handle-box">
      <el-button type="primary" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
      <el-button type="info" icon="el-icon-message" @click="handleFeedback">反馈系统</el-button>
    </div>
    <div style="margin-top: 10px">
      <el-timeline>
        <el-timeline-item v-for="(item, index) in chats" :key = "index" :timestamp = "item.sendTime.toLocaleString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')" placement = "top">
          <el-card class="cards">
            <span class="btn-bell-badge" v-if="item['isRead']===0"></span>
            <div style="display: flex;align-items: center">
              <el-tag type="warning" v-if="item['sendName']==='admin'">系统消息</el-tag>
              <el-tag size="mini" v-if="item['sendName']!=='admin'">用户消息</el-tag>
            </div>
            <div style="display: flex;align-items: center;margin-left: 5px" >{{item['sendName']}}:{{item.content}}</div>
            <div style="margin-left: auto">
              <el-button @click="chat(item)" size="medium">聊天</el-button>
            </div>
          </el-card>

        </el-timeline-item>
      </el-timeline>

      <el-empty v-if="this.haveContent===false" :key="this.haveContent" description="你很孤独，没有消息"></el-empty>
    </div>

    <!--    聊天框-->
    <el-dialog
        id="diag"
        :title="chatPerson"
        :visible.sync="centerDialogVisible"
        width="61.8%"
        center>
      <hr/>
      <div id='messages'>
        <div v-for="(item,index) in chatList" :key="index"
             :class="item['sendName']===chatPerson?'lines':'lines_right'">
          <div class="inLines"
               v-if="item['sendName']===chatPerson">
            {{item['sendName']}}: {{item.content}}</div>
          <div class="inLines"
               v-if="item['sendName']!==chatPerson">
            {{item.content}} :{{item['sendName']}}</div>
        </div>
      </div>
      <hr/>
      <div slot="footer" class="web__msg" @keyup.enter="handleSend">
          <textarea
              v-model="currentMsg"
              rows="3"
              placeholder="谈点大事..."
              class="web__msg-input"
              ref="msgBox"
          />
        <div class="web__msg-menu">
          <el-button
              class="web__msg-submit"
              type="primary"
              size="medium"
              @click="handleSend"
          >发送</el-button>
          <el-button
              class="web__msg-submit"
              size="medium"
              @click="closeChat"
          >关闭</el-button>
        </div>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import bus from "@/components/bus";

export default {

  name: "MessageBox",
  data(){
    return{
      message: 'first',
      unread: [],
      read: [],
      recycle: [],
      chats:[],
      //对话
      chatList : [],
      //对话人
      chatPerson:'',
      //控制对话框打开
      centerDialogVisible: false,
      //对话框输入的内容
      currentMsg:'',
      haveContent:false,
    }

  },
  computed: {

  },
  methods:{
    chat(item){
      this.chatPerson=item['sendName']
      this.$axios.post('/notice/getHistory',{
        'sendName':item['receiveName'],
        'receiveName':item['sendName'],

      }).then(
          res=>{
            this.chatList=res.data.result
          }
      )
      this.centerDialogVisible=true
    },
    handleSend(){
      let username = JSON.parse(sessionStorage.getItem("userInfo")).username;
      this.$axios.post('/notice/send',{
        'sendName':username,
        'receiveName':this.chatPerson,
        'content':this.currentMsg
      }).then(
          res=>{
            let item={
              'sendName':username,
              'content':this.currentMsg
            }
            this.chatList.push(item)
            this.currentMsg=''
          }
      )
    },
    handleFeedback(){
      let username = JSON.parse(sessionStorage.getItem("userInfo")).username;
      this.$prompt('请输入反馈内容', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: '',
        inputErrorMessage: ''
      }).then(({ value }) => {
        this.$axios.post('/notice/send',{
          'sendName':username,
          'receiveName':'admin',
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
    removeChat(item){
      this.$confirm("确定要删除该聊天吗").then(
          ()=>{
            this.$axios.post('/notice/deleteHistory',{
              'sendName':item['receiveName'],
              'receiveName':item['sendName'],

            }).then(
                res=>{
                  var index = this.chats.indexOf(item);
                  if (index > -1) {
                    this.chats.splice(index, 1);
                  }
                  this.handleRefresh();
                }
            )
          }
      )
    },
    closeChat(){
      this.centerDialogVisible = false;
      let username = JSON.parse(sessionStorage.getItem("userInfo")).username;
      this.$axios.post('/notice/setIsRead',{
        'sendName':this.chatPerson,
        'receiveName':username,
      }).then(
          ()=>{
            this.handleRefresh();
          }
      )
    },
    getChats(){
      let username = JSON.parse(sessionStorage.getItem("userInfo")).username;
      this.$axios.post('/notice/getLatestInfo',{
        'username':username,
      }).then(
          res=>{
            this.chats=res.data.result
            if(this.chats.length!==0)
            {
              this.haveContent=true;
            }
            else
            {
              this.haveContent=false;
            }
          }
      )
    },
    handleClick(tab, event) {
      console.log(this.message)
      switch (this.message) {
        case "first":this.getUnRead();break;
        case "second":this.getRead();break;
        case "third":this.getDel();break;
      }
    },
    getRead(){
      let username = JSON.parse(sessionStorage.getItem("userInfo")).username;
      this.$axios.post('/notice/getNotice', {
            'username':username,
            'type':1
          }
      ).then(res => {
        console.log(res)
        this.read = res.data.result
        console.log(this.read)
      })
    },
    getUnRead(){
      let username = JSON.parse(sessionStorage.getItem("userInfo")).username;
      this.$axios.post('/notice/getNotice', {
            'username':username,
            'type':0
          }
      ).then(res => {
        console.log(res)
        this.unread = res.data.result
        bus.$emit('message_change',this.unread.length)
        console.log(this.unread)
      })
    },
    getDel(){
      let username = JSON.parse(sessionStorage.getItem("userInfo")).username;
      this.$axios.post('/notice/getNotice', {
            'username':username,
            'type':2
          }
      ).then(res => {
        console.log(res)
        this.recycle = res.data.result
        console.log(this.recycle)
      })
    },
    handleRead(index) {
      const item = this.unread.splice(index, 1);
      console.log(item[0]);
      this.$axios.post('/notice/changeState', {
            'id':item[0].id,
            'type':1
          }
      ).then(res => {
        this.read = item.concat(this.read);
        this.$message({
          type:"success",
          message:'消息已读'
        })
      })

    },
    handleDel(index) {
      const item = this.read.splice(index, 1);
      this.$axios.post('/notice/changeState', {
            'id':item[0].id,
            'type':2
          }
      ).then(res => {
        this.recycle = item.concat(this.recycle);
        this.$message({
          type:"success",
          message:'消息删除'
        })
      })
    },
    handleRestore(index) {
      const item = this.recycle.splice(index, 1);
      this.$axios.post('/notice/changeState', {
            'id':item[0].id,
            'type':1
          }
      ).then(res => {
        this.read = item.concat(this.read);
        this.$message({
          type:"success",
          message:'消息还原'
        })
      })
    },
    handleRefresh()
    {
      this.getChats();
      this.getUnRead();

    }
  },
  created() {
    this.getChats();
    this.getUnRead();
    // this.getRead();
    // this.getDel();
  },
}
</script>

<style scoped lang="scss">
.web__msg {
  padding: 0 10px;
  height: auto;
  overflow: hidden;
  &-input {
    display: block;
    width: 100%;
    height: 45px;
    overflow-x: hidden;
    overflow-y: auto;
    box-sizing: border-box;
    resize: none;
    outline: 0;
    background-color: #fff;
    border: 0;
    word-break: break-all;
    font-size: 17px;
    line-height: 17px;
    -webkit-appearance: none;
  }
  &-menu {
    text-align: right;
  }
  &-submit {
    display: inline-block;
    outline: none;
    cursor: pointer;
    text-align: center;
  }
}
.el-dialog__wrapper /deep/ .el-dialog{
  height: 61.8%;
  .el-dialog__header{
    height: 6%;
    padding: 15px 20px 5px;
  }
  .el-dialog__body{
    height: 65%;
    padding: 10px;
    #messages{
      height: 100%;
      overflow-y:auto;
      .lines {
        height: 40px;
        display: flex;
        align-items: center;
      }
      .lines_right {
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: right;
        margin-right: 10px;
      }
      .inLines{
        font-size: medium;
      }
    }
    //滚动条尺寸
    #messages::-webkit-scrollbar {
      width: 4px; /*高宽分别对应横竖滚动条的尺寸*/
    }
    //滚动条块
    #messages::-webkit-scrollbar-thumb {
      border-radius: 10px;
      -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
      background: rgb(134, 194, 255);
    }
    //滚动条轨道
    #messages::-webkit-scrollbar-track {
      -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
      border-radius: 10px;
      background: rgba(0,0,0,0.1);
    }
  }
  .el-dialog__footer{
    padding-top: 0;
    padding-bottom: 0;
  }
}
hr{
  border: 0;
  height: 1px;
  background: rgba(123, 151, 156, 0.6);
}
.cards{
  /deep/ .el-card__body{
    display: flex;
    flex-direction: row;
  }
}
.btn-bell-badge {
  width: 8px;
  height: 8px;
  border-radius: 4px;
  background: #f56c6c;
  color: #fff;
}
</style>
