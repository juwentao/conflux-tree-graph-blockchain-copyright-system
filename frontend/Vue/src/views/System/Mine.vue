<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item><i class="el-icon-user"></i>我的</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <el-tabs type="border-card">
        <el-tab-pane label="我的出售">
          <div class="handle-box">
            <el-select v-model="query1.status" clearable placeholder="状态" @change="changeSelect1" class="handle-select mr10">
              <el-option
                  v-for="item in saleTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
            <el-button type="primary" icon="el-icon-refresh" @click="handleRefresh1">刷新</el-button>
            <el-button type="primary" icon="el-icon-circle-plus-outline" @click="handleSale" v-loading.fullscreen.lock="loading3">我要出售</el-button>
            <el-tooltip placement="top">
              <div slot="content">
                售卖您的版权作品需要您授权，所以请先连接钱包授权，否则任何人都无法获取您的版权作品<br/>
              </div>
              <i class="el-icon-question"></i>>
            </el-tooltip>
          </div>
          <el-table
              v-loading="loading1"
              :data="saleData"
              border
              class="table"
              ref="multipleTable"
              header-cell-class-name="table-header"
          >
            <el-table-column prop="title" label="售单标题" ></el-table-column>
            <el-table-column prop="description" label="售单描述"  ></el-table-column>
            <el-table-column prop="status" label="状态" align="center">
              <template slot-scope="scope">
                <el-tag
                    :type="scope.row.status===0?'danger':(scope.row.status===1?'success':scope.row.status===2?'warning':'info')"
                >{{scope.row.status===0?'停售':scope.row.status===1?'在售使用权':scope.row.status===2?'在售著作权':'已售出著作权'}}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="downloads" label="已授权数量" align="center"></el-table-column>
            <el-table-column prop="price" label="价格(HNUB)" align="center"></el-table-column>
            <el-table-column label="操作" align="center">
              <template slot-scope="scope">
                <el-button
                    type="text"
                    v-if="scope.row.status!==0" :disabled="scope.row.status===3" icon="el-icon-video-pause"
                    @click="handleStatus(scope.$index, scope.row)"
                >停售</el-button>
                <el-button
                    type="text"
                    v-if="scope.row.status===0" icon="el-icon-video-play"
                    @click="handleEdit(scope.$index, scope.row)"
                >开售</el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="pagination">
            <el-pagination
                background
                layout="total, prev, pager, next"
                :current-page="query1.pageIndex"
                :page-size="query1.pageSize"
                :total="pageTotal1"
                @current-change="handlePageChange1">
            </el-pagination>

            <el-dialog title="添加售单" center :visible.sync="saleFormVisible" :before-close="handleSaleDialogClose" @close="closeSaleDialog('saleForm')">
              <el-form :model="saleForm" :rules="saleRules" ref="saleForm" label-width="auto" label-position="top">
                <el-form-item label="标题" prop="title" >
                  <el-input placeholder="请输入标题" v-model.trim="saleForm.title" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                  <el-input placeholder="请输入描述内容" type="textarea" v-model.trim="saleForm.description" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="选择权益存证" prop="workid">
                  <el-select v-model="saleForm.workid" style="width:100%" clearable placeholder="请选择一个权益存证">
                    <el-option
                        v-for="item in worksData"
                        :key="item.id"
                        :label="item.workname"
                        :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="选择出售类别" prop="status">
                  <el-radio v-model="saleForm.status" label=1>出售许可使用权</el-radio>
                  <el-radio v-model="saleForm.status" label=2>出售著作权</el-radio>
                </el-form-item>
                <el-form-item label="价格(HNUB) 最低0.01最高1000000" prop="price">
                  <el-input-number v-model="saleForm.price" :precision="2" :min="0.01" :max="1000000" :step="1" ></el-input-number>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="saleFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveSale">确 定</el-button>
              </div>
            </el-dialog>


            <el-dialog title="编辑" center :visible.sync="editFormVisible" :before-close="handleEditDialogClose" @close="closeEditDialog('editForm')">
              <el-form :model="editForm" :rules="editRules" ref="editForm" label-width="auto" label-position="top">
                <el-form-item label="标题" prop="title" >
                  <el-input placeholder="请输入标题" v-model.trim="editForm.title" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                  <el-input placeholder="请输入描述内容" type="textarea" v-model.trim="editForm.description" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="选择权益存证" >
                  <el-select v-model="workid" style="width:100%" :disabled="true" clearable placeholder="请选择一个权益存证">
                    <el-option
                        v-for="item in worksData"
                        :key="item.id"
                        :label="item.workname"
                        :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="选择出售类别" prop="status">
                  <el-radio v-model="editForm.status" label=1>出售许可使用权</el-radio>
                  <el-radio v-model="editForm.status" label=2>出售著作权</el-radio>
                </el-form-item>
                <el-form-item label="价格(HNUB) 最低0.01最高1000000" prop="price">
                  <el-input-number v-model="editForm.price" :precision="2" :min="0.01" :max="1000000" :step="1" ></el-input-number>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="editFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
              </div>
            </el-dialog>
          </div>

        </el-tab-pane>

        <el-tab-pane label="我的购买">
          <div class="handle-box">
            <el-select v-model="query2.type" clearable placeholder="作品分类" @change="changeSelect2" class="handle-select mr10">
              <el-option
                  v-for="item in workTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
            <el-input v-model="query2.worknname" :clearable="true" placeholder="搜索作品名称" class="handle-input mr10"></el-input>
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            <el-button type="primary" icon="el-icon-refresh" @click="handleRefresh2">刷新</el-button>
            <el-tooltip placement="top">
              <div slot="content">
                请您根据您所购买的版权作品权利类别合法使用作品，违者自负<br/>
              </div>
              <i class="el-icon-question"></i>>
            </el-tooltip>
          </div>
          <el-table
              v-loading="loading2"
              :data="tradeData"
              border
              class="table"
              ref="multipleTable"
              header-cell-class-name="table-header"
          >
            <el-table-column prop="workname" label="作品名称"  align="center"></el-table-column>
            <el-table-column prop="type" label="作品类别"  align="center">
              <template slot-scope="scope">
                <p v-if="scope.row.type===1">摄影作品</p>
                <p v-if="scope.row.type===2">文字作品</p>
                <p v-if="scope.row.type===3">影视作品</p>
                <p v-if="scope.row.type===4">音乐作品</p>
                <p v-if="scope.row.type===5">美术作品</p>
              </template>
            </el-table-column>
            <el-table-column label="购买类别" align="center">
              <template slot-scope="scope">
                <el-tag
                    :type="scope.row.status===1?'success':'warning'"
                >{{scope.row.status===1?'使用权':'著作权'}}</el-tag>
              </template>
            </el-table-column>
            <el-table-column  label="购买时间" align="center">
              <template slot-scope="scope">
                <span>{{scope.row.tradetime.toLocaleString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="filedownloadurl" label="作品文件" align="center" >
              <template slot-scope="scope">
                <el-link
                    type="primary"
                    :underline="false"
                    :href="scope.row.filedownloadurl"
                >
                  {{ scope.row.filename }}
                </el-link>
              </template>
            </el-table-column>
            <el-table-column prop="tradeprice" label="价格(HNUB)" align="center"></el-table-column>
            <el-table-column label="操作" align="center">
              <template slot-scope="scope">
                <el-button
                    type="text"
                    icon="el-icon-reading"
                    @click="handleDetail(scope.$index, scope.row)"
                >查看详情</el-button>

                <el-button
                    type="text"
                    icon="el-icon-chat-dot-square"
                    v-if="scope.row.iscomment===false && scope.row.status===1"
                    @click="handleComment(scope.$index, scope.row)"
                >评论</el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="pagination">
            <el-pagination
                background
                layout="total, prev, pager, next"
                :current-page="query2.pageIndex"
                :page-size="query2.pageSize"
                :total="pageTotal2"
                @current-change="handlePageChange2">
            </el-pagination>
          </div>

          <el-drawer
              title="查看详情"
              :visible.sync="drawer"
              direction="rtl"
              size="50%"
              :before-close="handleCloseDrawer">
            <el-tabs type="border-card">
              <el-tab-pane label="购买详情">
                <div>
                  <el-card>
                    <div slot="header" class="clearfix">
                      <span>购买</span>
                    </div>
                    <div>
                      <el-row :gutter="2">
                        <el-col :span="5">
                          <span >购买人：{{this.userinfo.realname}}</span>
                        </el-col>
                        <el-col :span="12">
                          <span >购买人证件号码：{{this.userinfo.idnumber}}</span>
                        </el-col>
                      </el-row>
                      <el-row :gutter="2" style="margin-top: 20px">
                        <el-col :span="20">
                          <span >交易哈希：{{this.drawerMessage.txhash}}</span>
                        </el-col>
                      </el-row>
                      <el-row :gutter="2" style="margin-top: 20px">
                        <el-col :span="20">
                          <span >文件哈希：{{this.drawerMessage.filehash}}</span>
                        </el-col>
                      </el-row>
                    </div>
                  </el-card>
                </div>
              </el-tab-pane>

              <el-tab-pane label="购买证明">
                <div>
                  <el-card style="height: 50%">
                    <div slot="header" class="clearfix">
                      <span>在线预览</span>
                      <el-link  :disabled="this.drawerMessage.tradecertification===null" style="float: right; padding: 3px 0" :href="this.drawerMessage.tradecertification" type="primary">下载证明</el-link>
                    </div>
                  </el-card>
                </div>
                <pdf
                    ref="pdf"
                    :src="this.drawerMessage.tradecertification">
                </pdf>
              </el-tab-pane>
            </el-tabs>
          </el-drawer>

        </el-tab-pane>
      </el-tabs>
    </div>

    <el-dialog title="评论" :visible.sync="dialogVisible" center>
      <el-form :model="commentForm" :rules="commentRules" ref="commentForm">
        <el-form-item label="评论内容" prop="comment">
          <el-input type="textarea" placeholder="请输入评论内容" v-model.trim="commentForm.comment" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="comment">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import confluxPortal from '../../conflux/conflux-portal'
import conflux from '../../conflux/conflux'
import pdf from 'vue-pdf'
import bus from '../../components/bus';
const {ContractHnuCoin,ContractCopyrightSystemTransactions,ContractCopyright} = require("@/conflux/conflux");
export default {
  name: "Mine",
  components:{
    pdf
  },
  data(){
    return{
      account:'',
      userid:'',
      query1: {
        status:'',
        pageIndex: 1,
        pageSize: 10
      },
      pageTotal1: 0,
      loading1:false,
      saleData:[],
      worksData:[],
      editFormVisible:false,
      saleFormVisible:false,
      saleForm:{
        status:'',
        title:'',
        description:'',
        price:3.00,
        workid:'',
        userid:'',
      },
      workid:'',
      saleRules:{
        title:[
          { required: true, message: '请输入标题', trigger: 'blur' },
          { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入描述内容', trigger: 'blur' },
          { min: 0, max: 500, message: '描述需要在 0 到 500 个字符', trigger: 'blur' }
        ],
        workid:[
          { required: true, message: '请选择一个您的权益存证', trigger: 'change' },
        ],
        status:[
          { required: true, message: '请选择一个类别', trigger: 'change' },
        ],
        price:[
          { required: true, message: '请输入价格', trigger: 'blur' },
        ],
      },

      editRules:{
        title:[
          { required: true, message: '请输入标题', trigger: 'blur' },
          { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入描述内容', trigger: 'blur' },
          { min: 0, max: 500, message: '描述需要在 0 到 500 个字符', trigger: 'blur' }
        ],
        status:[
          { required: true, message: '请选择一个类别', trigger: 'change' },
        ],
        price:[
          { required: true, message: '请输入价格', trigger: 'blur' },
        ],
      },
      editForm:{
        status:'',
        title:'',
        description:'',
        price:'',
        sid:'',
      },
      saleTypeOptions: [{
        value: 1,
        label: '在售'
      }, {
        value: 0,
        label: '停售'
      }],


      tradeData:[],
      loading2:false,
      query2: {
        type: '',
        workname: '',
        pageIndex: 1,
        pageSize: 10
      },
      pageTotal2: 0,
      drawer:false,
      drawerMessage: {},
      workTypeOptions: [{
        value: '1',
        label: '摄影作品'
      }, {
        value: '2',
        label: '文字作品'
      }, {
        value: '3',
        label: '影视作品'
      }, {
        value: '4',
        label: '音乐作品'
      }, {
        value: '5',
        label: '美术作品'
      }],
      dialogVisible:false,
      commentForm:{
        comment:''
      },
      commentData:{},
      commentRules:{
        comment:[
          { required: true, message: '请输入评论内容', trigger: 'blur' },
          { min: 1, max: 500, message: '长度在 1 到 500 个字符', trigger: 'blur' }
        ]
      },
      loading3:false,
    };
  },
  methods:{
    comment(){
      this.$refs.commentForm.validate(async valid => {
        if(valid)
        {
          let formData = new FormData();
          formData.append("saleid", this.commentData.saleid);
          formData.append("userid", this.userinfo.id);
          formData.append("content", this.commentForm.comment);
          formData.append("tradeid", this.commentData.tradeid);
          this.$axios.post(`/comment/addComment`, formData).then(async response => {
            if (response.data.code === '200') {
              this.$message({
                message: "评论成功",
                type: "success",
              });
              bus.$emit('refreshGetComment');
              this.dialogVisible = false
              this.getTradeData();
            } else {
              this.$message({
                message: "评论失败",
                type: "error"
              });
            }
          }).catch(e => {
            this.$message({
              message: "网络异常！",
              type: "error"
            });
          });
        }
        else
        {
          this.$message({
            message: "请输入合法的值",
            type: "error"
          });
          return false;
        }

      });
    },
    handleDetail(index, row){
      this.drawerMessage=row;
      console.log(this.drawerMessage)
      this.drawer=true;

    },
    handleComment(index, row){
      this.dialogVisible=true;
      this.commentData=row;
      console.log(this.commentData)

    },
    handleCloseDrawer(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
    },
    changeSelect1(){
      this.$set(this.query1, 'pageIndex', 1);
      this.getSaleData();
    },
    // 分页导航
    handlePageChange1(val) {
      this.$set(this.query1, 'pageIndex', val);
      this.getSaleData();
    },


    handleRefresh2() {
      this.getTradeData();
    },

    changeSelect2(){
      this.$set(this.query2, 'pageIndex', 1);
      this.getTradeData();
    },
    // 分页导航
    handlePageChange2(val) {
      this.$set(this.query2, 'pageIndex', val);
      this.getTradeData();
    },

    handleSearch() {
      this.$set(this.query2, 'pageIndex', 1);
      this.getTradeData();
    },


    handleRefresh1() {
      this.getSaleData();
      this.getWorksList();
    },

    getSaleData(){
      this.loading1 = true

      this.$axios.get('/sale/getSalesPage',{
        params:{
          currentPage:this.query1.pageIndex,
          pageSize:this.query1.pageSize,
          userid:this.userid,
          status:this.query1.status
        }
      }).then(response => {
        if (response.data.code==='200') {
          this.saleData=response.data.result.records
          console.log(this.saleData)
          this.pageTotal1=response.data.result.total
          this.loading1 = false
        } else {
          this.$message({
            message: "获取我的售卖列表失败",
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

    getTradeData(){
      this.loading2 = true

      this.$axios.get('/trade/search',{
        params:{
          currentPage:this.query2.pageIndex,
          pageSize:this.query2.pageSize,
          userId:this.userid,
          workname:this.query2.workname,
          type:this.query2.type
        }
      }).then(response => {
        if (response.data.code==='200') {
          this.tradeData=response.data.result.record
          console.log(this.tradeData)
          this.pageTotal2=response.data.result.total
          this.loading2 = false
        } else {
          this.$message({
            message: "获取我的购买列表失败",
            type: "error"
          });
          this.loading2 = false
        }
      }).catch(e => {
        this.$message({
          message: "网络异常！",
          type: "error"
        });
        this.loading2 = false
      });
    },

    getWorksList(){
      this.$axios.get('/works/getWorksById',{
        params:{
          userId:this.userid
        }
      }).then(response => {
        this.worksData=response.data.result
      }).catch(e => {
        console.log(e.message);
      });
    },

    async saveSale(){
      await confluxPortal.enable();
      this.account = confluxPortal.getAccount();
      if (this.account === '')
      {
        this.$message({
          message: '您还未连接conflux钱包插件',
          type: 'warning'
        });
      }
      else {
         await this.$refs.saleForm.validate(async valid => {
           if (valid) {
             this.loading3 = true
             console.log(this.saleForm.workid)
             let filehash;
             for(let i=0; i<this.saleData.length; i++)
             {
               let form=this.saleData[i];
               if(form.workid===this.saleForm.workid)
               {
                 this.$message({
                   message: "该作品已添加售卖，请勿重复添加！",
                   type: "error"
                 });
                 this.saleFormVisible = false
                 this.loading3 = false
                 return false;
               }
             }
             for(let i=0; i<this.worksData.length; i++)
             {
               let form=this.worksData[i];
               if(form.id===this.saleForm.workid)
               {
                 filehash=form.filehash;
               }
             }
             console.log(filehash)
             try {
               const called = ContractCopyright.contract.approve.call(ContractCopyrightSystemTransactions.contract.address, filehash)
               const transaction = await confluxPortal.sendTransaction({
                 from: this.account,
                 to: called.to,
                 data: called.data,
               })
               const txHash = transaction.result;
               console.log(txHash)

               //上链成功
               this.saleForm.userid = this.userid;
               let formData = new FormData();
               for (const key in this.saleForm) {
                 formData.append(key, this.saleForm[key])
               }
               console.log(formData);
               this.$axios.post(`/sale/addSale`, formData).then(async response => {
                 if (response.data.code === '200') {
                   this.$message({
                     message: "添加成功了呢",
                     type: "success",
                   });
                   bus.$emit('refreshResourceCenter');
                   bus.$emit('refreshDashboard');
                   this.saleFormVisible = false
                   this.loading3 = false;
                 } else {
                   this.$message({
                     message: "添加失败,请检查输入是否正确",
                     type: "error"
                   });
                   this.loading3 = false
                 }
               }).catch(e => {
                 this.$message({
                   message: "该作品已添加售卖，请勿重复添加！",
                   type: "error"
                 });
                 this.loading3 = false
               });
             } catch (e) {
               //上链失败
               this.$message({
                 message: "授权失败" ,
                 type: "error"
               });
               console.log(e.message)
               this.loading3 = false
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
         });
      }
    },

    saveEdit(){
      this.$refs.editForm.validate( async valid => {
        if (valid) {
          this.loading1 = true
          console.log(this.editForm)
          let formData = new FormData();
          for (const key in this.editForm) {
            formData.append(key, this.editForm[key])
          }
          this.$axios.post(`/sale/editSale`, formData).then(async response => {
            if (response.data.code === '200') {
              this.$message({
                message: "操作成功",
                type: "success",
              });
              bus.$emit('refreshResourceCenter');
              bus.$emit('refreshDashboard');
              this.getSaleData();
              this.editFormVisible=false;
              this.loading1=false;
            } else {
              this.$message({
                message: "操作失败",
                type: "error"
              });
              this.loading1 = false
            }
          }).catch(e => {
            this.$message({
              message: "网络或程序异常",
              type: "error"
            });
            this.loading1 = false
          });
        } else {
          this.$message({
            message: "请输入合法的值",
            type: "error"
          });
          return false;
        }
      });
    },

    // 编辑操作
    handleEdit(index, row) {
      this.idx = index;
      this.editForm.title=row.title;
      this.editForm.description=row.description;
      this.editForm.price=row.price;
      this.editForm.sid=row.sid;
      this.workid=row.workid;
      this.editFormVisible = true;

      console.log(this.editForm)
    },

    handleStatus(index, row){
      this.loading1=true;
      let formData = new FormData();
      let status=null;
      console.log(row.status)
      if(row.status!==0)
      {
        status=0;
      }
      formData.append("status",status)
      formData.append("sid",row.sid)
      this.$axios.post(`/sale/editSale`, formData).then(async response => {
        if (response.data.code === '200') {
          this.$message({
            message: "操作成功",
            type: "success",
          });
          bus.$emit('refreshResourceCenter');
          bus.$emit('refreshDashboard');
          this.getSaleData();
          this.loading1=false;
        } else {
          this.$message({
            message: "操作失败",
            type: "error"
          });
          this.loading1 = false
        }
      }).catch(e => {
        this.$message({
          message: "网络或程序异常",
          type: "error"
        });
        this.loading1 = false
      });

    },

    async handleSale(){
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
        this.account = confluxPortal.getAccount();
        if (this.account === '')
        {
          this.$message({
            message: '您还未连接conflux钱包插件',
            type: 'warning'
          });
        }
        else
        {
          this.saleFormVisible=true
        }
      }
    },

    handleSaleDialogClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
    },
    closeSaleDialog(saleForm){
      this.saleFormVisible=false;
      this.$refs[saleForm].resetFields();
      this.getSaleData();
    },

    handleEditDialogClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
    },
    closeEditDialog(editForm){
      this.editFormVisible=false;
      console.log(this.editForm)
      this.$refs[editForm].resetFields();
      console.log(this.editForm)
      this.getSaleData();
    },
  },
  created() {
    this.userinfo=JSON.parse(sessionStorage.getItem("userInfo"))
    this.userid=JSON.parse(sessionStorage.getItem("userInfo")).id
    this.getWorksList();
    this.getTradeData();
    this.getSaleData();
    bus.$on('refreshGetTradeData', () => {
      console.log("调用咯")
      this.getTradeData();
    })
    bus.$on('refreshGetWorksList', () => {
      console.log("调用咯")
      this.getWorksList();
    })
  }
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
.handle-box {
  margin-bottom: 20px;
}
.mr10 {
  margin-right: 10px;
}

.handle-select {
  width: 120px;
}

.handle-input {
  width: 200px;
  display: inline-block;
}
.table {
  width: 100%;
  font-size: 14px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}
</style>