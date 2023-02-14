<template>
  <div class="cases">
    <el-button type="primary" @click="openDialog()">新增案例</el-button>

    <el-table border :data="tableData" v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="序号" width="180"></el-table-column>
      <el-table-column prop="title" label="案例标题" width="180"></el-table-column>
      <el-table-column prop="url" label="图片">
        <template slot-scope="scope">
          <img style="width:100%" :src="scope.row.url" alt />
        </template>
      </el-table-column>
      <el-table-column prop="content" label="案例内容"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
              type="primary"
              icon="el-icon-edit"
              @click="handleEdit(scope.$index, scope.row)"
          ></el-button>
          <el-button
              type="danger"
              icon="el-icon-delete"
              @click="handleDelete(scope.$index, scope.row)"
          ></el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="案例编辑" :visible.sync="dialogFormVisible" @close="closeDialog('formData')">
      <el-form :model="formData">
        <el-form-item label="案例标题" :label-width="formLabelWidth">
          <el-input v-model="formData.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="案例图片" :label-width="formLabelWidth">
          <!-- :before-upload="beforeAvatarUpload" -->
          <el-upload class="upload-demo"  style="width: 100%;height: 100%"  drag  :on-preview="handlePreview"
                     action="TODO"
                     :on-remove="handleRemove"
                     :before-remove="beforeRemove"
                     :limit="1"
                     :on-exceed="handleExceed"
                     :on-change="handleChange"
                     :file-list="fileList"
                     :auto-upload="false"
                     :show-file-list="true"
                     ref="uploadComponent">
            <img v-if="formData.url" :src="formData.url" class="avatar" />
            <i v-else class="el-icon-upload"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="案例内容" :label-width="formLabelWidth">
          <el-input v-model="formData.content" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleCreateOrModify()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Cases",
  data() {
    return {
      loading: true,
      dialogFormVisible: false,
      formLabelWidth: "120px",
      tableData: [],
      formData: {
        id: 0,
        url: "",
        title: "",
        content: "",
      },
      fileList: [],
      options: {},
      headers: {}
    };
  },
  mounted() {
    // let token = "Browser " + sessionStorage.getItem("token");
    // //window.console.log(token);
    // this.options = {
    //   headers: {
    //     Authorization: token
    //   }
    // };
    // this.headers = {
    //   Authorization: token
    // };

    this.loadData();
  },
  methods: {
    loadData() {
      this.loading = true;
      this.$axios.get('/case/getCaseInfo').then(response => {
        if (response.data.code==='200') {
          this.tableData = response.data.result;
          this.loading = false;
        } else {
          this.$message({
            message: "获取案例列表失败",
            type: "error"
          });
          this.loading = false;
        }
      }).catch(e => {
        this.loading = false;
        this.$message({
          message: "网络异常！",
          type: "error"
        });
      });
    },
    openDialog() {
      // 清除数据
      this.formData.id = 0;
      this.formData.url = "";
      this.formData.title = "";
      this.formData.content = "";
      this.fileList=[]
      this.dialogFormVisible = true;
    },
    // 新增
    handleCreateOrModify() {
      window.console.log(this.formData);
      if (!this.formData.id)
      {
        this.loading = true;
        let formData = new FormData();
        formData.append("img", this.fileList[0].raw)
        for (const key in this.formData) {
          formData.append(key, this.formData[key])
        }
        // ID 无效时 视为新增
        this.loading = true;
        this.$axios.post("/case/addCase", formData).then(response => {
          if (response.data.code === '200') {
            this.$message({
              message: "添加成功了呢",
              type: "success",
            });
            this.dialogFormVisible = false;
            this.loading = false;
            this.loadData();
          } else {
            this.loading = false;
            this.$message({
              message: response.data.message,
              type: "error"
            });
          }
        }).catch(e => {
          this.loading = false;
          this.$message({
            message: "网络异常！",
            type: "error"
          });
        });
      }
      else
      {
        this.loading = true;
        let formData = new FormData();
        if (this.fileList.length !== 0)
        {
          formData.append("img", this.fileList[0].raw)
        }
        formData.append("title", this.formData["title"])
        formData.append("content", this.formData["content"])
        formData.append("id", this.formData["id"])
        this.$axios.post("/case/editCase",formData).then(response => {
          if (response.data.code === '200') {
            this.$message({
              message: "修改成功了呢",
              type: "success",
            });
            this.loading = false;
            this.dialogFormVisible = false;
            this.loadData();
          } else {
            this.loading = false;
            this.$message({
              message: response.data.message,
              type: "error"
            });
          }
        }).catch(e => {
          this.loading = false;
          this.$message({
            message: "网络异常！",
            type: "error"
          });
        });
      }
    },
    handleEdit(index, row) {
      window.console.log(index, row);
      this.formData = row;
      this.dialogFormVisible = true;
    },
    handleDelete(index, row) {
      this.$confirm("此操作将永久此条数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
            // 已确认删除
            // 调接口删除
            this.loading = true;
            this.$axios.post(`/case/deleteCase`, {id:row.id}, ).then(response => {
                  this.loading = false;
                  this.$message({
                    message: "删除成功！",
                    type: "success"
                  });
                  this.loadData();
                })
                .catch(e => {
                  this.$message({
                    message: "网络异常！",
                    type: "error"
                  });
                });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除"
            });
          });
    },
    handleChange(file, fileList) {
      this.fileList = fileList;
    },
    handleRemove (file, fileList) {
      this.$refs['uploadComponent'].clearFiles();
    },
    handlePreview (file) {
      console.log("FILE"+file);
    },
    handleExceed (files, fileList) {
      this.$message.warning(
          `当前限制选择 1 个文件，本次选择了 ${files.length
          } 个文件，共选择了 ${files.length + fileList.length} 个文件`
      );
    },
    beforeRemove (file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    closeDialog(formData){
      this.$refs['uploadComponent'].clearFiles();
      this.editVisible=false;
    },
    //时间格式化
    dateFormat: function(row) {
      //row 表示一行数据, CreateTime 表示要格式化的字段名称
      let t = new Date(row.CreateTime);
      return t.getFullYear() + "-" + (t.getMonth() + 1) + "-" + t.getDate();
    }
  }
}
</script>
<style scoped>
  .el-table {
    margin-top: 20px;
  }
</style>