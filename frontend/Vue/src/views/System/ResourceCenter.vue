<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-bank-card"></i> 资源中心
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container" v-loading="loading">
      <div class="handle-box">
        <el-select v-model="query.worktype" clearable placeholder="作品分类" @change="changeSelectType" class="handle-select mr10">
          <el-option
              v-for="item in workTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
        <el-input v-model="query.content" :clearable="true" placeholder="搜索作品名称" class="handle-input mr10"></el-input>
        <el-select v-model="query.saletype" clearable placeholder="购买类别" @change="changeSelectSaleType" class="handle-select mr10">
          <el-option
              v-for="item in saleTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
        <el-select v-model="query.sorttype" clearable placeholder="排序" @change="changeSelectSortType" class="handle-select mr10">
          <el-option
              v-for="item in sortTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>


        <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
        <el-button type="primary" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
        <el-tooltip placement="top">
          <div slot="content">
            1.所有作品由用户上传存证且授权，如若侵权请反馈系统<br/>
            2.虚拟产品一经售出概不退款（资源遇到问题，请及时反馈系统）
          </div>
          <i class="el-icon-question"></i>>
        </el-tooltip>
      </div>
      <div>
        <el-row class="row-box">
          <el-col
              v-for="item in salesData"
              :key="item.sid"
              :span="5"
              style="margin-bottom: 10px"
              :offset="1"
          >
            <el-card shadow="hover" class="el-card" @click.native="handleDetail(item.sid)">
              <div slot="header" class="clearfix">
                <i class="el-icon-folder" /><span style="margin-left: 5px">{{item.title }}</span>
                <div >
                  <el-tag effect="plain" type="success" style="margin-left: 6px" v-if="item.type===1">摄影作品</el-tag>
                  <el-tag effect="plain" type="success" style="margin-left: 6px" v-if="item.type===2">文字作品</el-tag>
                  <el-tag effect="plain" type="success" style="margin-left: 6px" v-if="item.type===3">影视作品</el-tag>
                  <el-tag effect="plain" type="success" style="margin-left: 6px" v-if="item.type===4">音乐作品</el-tag>
                  <el-tag effect="plain" type="success" style="margin-left: 6px" v-if="item.type===5">美术作品</el-tag>
                  <el-tag  effect="plain"  style="margin-left: 6px" v-if="item.status===1">使用权</el-tag>
                  <el-tag effect="plain" type="warning" style="margin-left: 6px" v-if="item.status===2">著作权</el-tag>
                </div>
                <div >

                </div>
              </div>
              <div>
                <ul class="role-info">
                  <li>
                    <div class="role-left">描述信息：{{ item.description }}</div>
                  </li>
                  <li>
                    <div class="role-left">
                      价格：{{item.price }} HNUB
                    </div>
                  </li>
                  <li>
                    <div class="role-left">
                      已授权数量：{{item.downloads }}
                    </div>
                  </li>
                </ul>
              </div>
              <div
                  style="display: inline-block; float: right; cursor: pointer"
              >
                <el-tooltip effect="dark" content="点击卡片查看详情" placement="bottom">
                  <i class="el-icon-menu" />
                </el-tooltip>
              </div>
            </el-card>
          </el-col>
        </el-row>
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
  </div>
</template>

<script>

import bus from '../../components/bus';
export default {
  name: "ResourceCenter",

  data() {

    return {
      pdfUrl:'',
      query: {
        worktype: '',
        content: '',
        pageIndex: 1,
        pageSize: 10,
        sorttype:'',
        saletype:'',
      },

      sortTypeOptions: [{
        value: '1',
        label: '按授权价格倒序排序'
      }, {
        value: '2',
        label: '按授权价格升序排序'
      }, {
        value: '3',
        label: '按已授权数量倒序排序'
      }, {
        value: '4',
        label: '按已授权数量升序排序'
      }],

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

      saleTypeOptions: [{
        value: '1',
        label: '我要购买使用权'
      }, {
        value: '2',
        label: '我要购买著作权'
      }],

      salesData: [],
      pageTotal: 0,
      loading:false,
    };
  },
  watch: {

  },
  created() {
    this.getData();
    bus.$on('refreshResourceCenter', () => {
      console.log("调用咯")
      this.getData();
    })
  },
  methods: {

    getData() {
      this.loading = true
      this.$axios.get('/sale/searchSale',{
        params:{
          currentPage:this.query.pageIndex,
          pageSize:this.query.pageSize,
          content:this.query.content,
          workType:this.query.worktype,
          sortType:this.query.sorttype,
          status:this.query.saletype
        }
      }).then(response => {
        if (response.data.code==='200') {
          this.salesData=response.data.result.record
          console.log(this.salesData)
          this.pageTotal=response.data.result.page.total
          this.loading = false
        } else {
          this.$message({
            message: "获取在售列表失败",
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





    // 触发搜索按钮
    handleSearch() {
      this.$set(this.query, 'pageIndex', 1);
      this.getData();
    },
    handleRefresh() {
      this.getData();
    },

    changeSelectType(){
      this.$set(this.query, 'pageIndex', 1);
      this.getData();
    },

    changeSelectSortType(){
      this.$set(this.query, 'pageIndex', 1);
      this.getData();
    },

    changeSelectSaleType(){
      this.$set(this.query, 'pageIndex', 1);
      this.getData();
    },

    handleDetail(sid){
      this.$router.push({name: 'resourceDetail', params: {sid: sid}})
    },

    // 分页导航
    handlePageChange(val) {
      this.$set(this.query, 'pageIndex', val);
      this.getData();
    },




  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
/deep/ .el-upload{
  width: 100%;
}
/deep/ .el-upload .el-upload-dragger{
  width: 100%;
}

.handle-box {
  margin-bottom: 20px;
}

.handle-select {
  width: 120px;
}
.row-box {
  display: flex;
  flex-flow: wrap;
}
.row-box .el-card {
  min-width: 100%;
  height: 100%;
  margin-right: 20px;
  border: 0px;
}
.handle-input {
  width: 200px;
  display: inline-block;
}
.table {
  width: 100%;
  font-size: 14px;
}
.red {
  color: #ff0000;
}
.mr10 {
  margin-right: 10px;
}
.table-td-thumb {
  display: block;
  margin: auto;
  width: 40px;
  height: 40px;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}
.role-info {
  margin-top: 0;
  padding-top: 0;
  padding-left: 0;
  list-style: none;
li {
  border-bottom: 1px solid #f0f3f4;
  padding: 11px 0;
  font-size: 12px;
}
.role-left {
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