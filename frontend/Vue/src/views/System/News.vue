<template>
  <div class="news">
    <el-button type="primary" @click="openDialog()">新增</el-button>

    <el-table :data="tableData" border style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="序号" width="180"></el-table-column>
      <el-table-column prop="title" label="新闻标题" width="180"></el-table-column>
      <el-table-column prop="url" label="图片">
        <template slot-scope="scope">
          <img style="width:100%" :src="scope.row.url" alt />
        </template>
      </el-table-column>
      <el-table-column prop="content" label="新闻内容">
        <template slot-scope="scope">
          <p v-if="scope.row.content.length > 100">{{scope.row.content.substring(0,100)}} ...</p>
          <p v-else>{{scope.row.content}}</p>
        </template>
      </el-table-column>
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
    <!--  -->
    <el-dialog title="新闻编辑" :visible.sync="dialogFormVisible" @close="closeDialog('formData')">
      <el-form :model="formData">
        <el-form-item label="新闻名称" :label-width="formLabelWidth">
          <el-input v-model="formData.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="新闻图片" :label-width="formLabelWidth">
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
        <el-form-item label="新闻内容" :label-width="formLabelWidth">
          <el-input type="textarea" :rows="10" v-model="formData.content" autocomplete="off"></el-input>
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
  name: "loginNews",
  data() {
    return {
      options: {},
      headers: {},
      tableData: [],
      fileList: [],
      formData: {
        id: 0,
        title: "",
        url: "",
        content: "",
      },
      dialogFormVisible: false,
      formLabelWidth: "120px",
      loading: true
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
      this.$axios.post('/new/getNewsInfo',{num:''}).then(response => {
        if (response.data.code==='200') {
          this.tableData = response.data.result;
          this.loading = false;
          console.log(response.data.result)
        } else {
          this.$message({
            message: "获取新闻失败",
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
      this.formData.title = "";
      this.formData.url = "";
      this.formData.content = "";
      this.fileList=[];
      this.dialogFormVisible = true;
    },
    handleCreateOrModify() {
      if (!this.formData.id) {
        this.loading = true;
        let formData = new FormData();
        formData.append("img", this.fileList[0].raw)
        for (const key in this.formData) {
          formData.append(key, this.formData[key])
        }
        // ID 无效时 视为新增
        this.loading = true;
        this.$axios.post("/new/addNews", formData).then(response => {
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
      } else {
        this.loading = true;
        let formData = new FormData();
        if (this.fileList.length !== 0)
        {
          formData.append("img", this.fileList[0].raw)
        }
        formData.append("title", this.formData["title"])
        formData.append("content", this.formData["content"])
        formData.append("id", this.formData["id"])
        this.$axios.post("/new/editNew",formData).then(response => {
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
    //编辑
    handleEdit(index, row) {
      //index:第几行   row:这一行的数据
      window.console.log(index, row);
      this.formData = row;
      this.dialogFormVisible = true;
    },
    handleDelete(index, row) {
      this.$confirm("此操作将永久删除该文件, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
          .then(() => {
            // 已确认删除
            // 调接口删除
            this.loading = true;
            this.$axios.post(`/new/deleteNews`, {id:row.id}, ).then(response => {
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
  }
};
</script>

<style lang="scss" scoped>
.el-table {
  margin-top: 20px;
}
</style>