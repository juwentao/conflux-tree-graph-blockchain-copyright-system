<template>
  <div>
    <el-card class="box-card">
      <el-alert :closable="false"
          title="*请确保您使用本人的真实身份进行验证，我们会保护您的个人信息安全"
          type="warning"
          show-icon>
      </el-alert>
      <div slot="header" class="clearfix">
        <span>实名认证</span>
      </div>
      <div>
        <el-form :model="Form" :rules="rules" ref="Form" label-position="top" class="demo-ruleForm">
          <el-row>
            <el-col :span="10">
              <el-form-item label="真实姓名" prop="name">
                <el-input v-model.trim="Form.name" placeholder="请输入真实姓名"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="10">
              <el-form-item label="证件类型" prop="type">
                <el-select v-model="Form.type" :disabled="true" placeholder="请选择证件类型">
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="10">
              <el-form-item label="证件号码" prop="idnumber" >
                <el-input v-model.trim="Form.idnumber" placeholder="请输入证件号码"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item>
            <el-button type="primary" @click="submitForm">提交认证</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script>
import { isIdCard } from '@/utils/validate'
export default {
  name: "Verification",
  data(){
    const validateIdNumber = (rule, value, callback) => {
      if(value === ''){
        callback(new Error('请输入证件号码'));
      }
      else {
        if (!isIdCard(value)) {
          callback(new Error('请输入正确的证件号码'));
        }else{
          callback();
        }
      }
    };
    return{
      userinfo:'',
      Form: {
        name: '',
        idnumber:'',
        type:'二代居民身份证',
      },
      rules: {
        name: [
          { required: true, message: '请输入真实姓名', trigger: 'blur' },
          { min: 2, max: 5, message: '长度在 2 到 5 个字符', trigger: 'blur' }
        ],
        idnumber:[
          { validator:validateIdNumber, trigger: 'blur' },
        ],
        type: [
          { required: true, message: '请选择证件类型', trigger: 'change' }
        ],
      },
    };
  },
  methods:{
    submitForm()
    {
      this.$refs.Form.validate(valid => {
        if (valid) {
          let formData = new FormData();
          formData.append("id", this.userinfo.id)
          formData.append("realname", this.Form.name)
          formData.append("idnumber", this.Form.idnumber)
          this.$axios.post(`/user/certify`,formData).then(response => {
            if (response.data.code==='200') {
              this.$message({
                message: response.data.message+'请重新登录',
                type: "success"
              });
              sessionStorage.removeItem("userInfo");
              sessionStorage.removeItem("token");
              this.$router.push('/login');

            } else {
              this.$message({
                message: response.data.message,
                type: "error"
              });
            }

          }).catch(e => {
            this.$message({
              message: "网络异常！",
              type: "error"
            });
          });
        } else {
          this.$message({
            message: "请输入合法的值",
            type: "error"
          });
          return false;
        }
      });
    }
  },
  created() {
    this.userinfo=JSON.parse(sessionStorage.getItem("userInfo"))
  }
}
</script>

<style scoped>
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}

</style>