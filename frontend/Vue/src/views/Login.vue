<template>
  <div class="login" v-loading="loading" element-loading-text="登陆中..."
       element-loading-spinner="el-icon-loading"
       element-loading-background="rgba(0, 0, 0, 0.8)" >
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>登录入口</span>
      </div>
      <div>
        <el-form
            class="demo-ruleForm"
            ref="lform"
            :model="loginForm"
            :rules="rules"
            label-width="80px"

        >
          <el-form-item label="用户名" prop="username">
            <el-input v-focus v-model.trim="loginForm.username" name="username" ></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password" @keyup.enter.native="login">
            <el-input v-model.trim="loginForm.password" name="password" type="password" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary"  @click="login">登录</el-button>
            <el-button type="text">
              <router-link to="/register">注册</router-link>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script>
import {isPassword} from "@/utils/validate";

export default {
  name: "Login",
  directives: {
    focus: {
      inserted(el) {
        el.querySelector('input').focus()
      },
    },
  },
  data() {
    const validateusername = (rule, value, callback) => {
      if ('' === value) {
        callback(new Error('用户名不能为空'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (!isPassword(value)) {
        callback(new Error('密码不能少于6位'))
      } else {
        callback()
      }
    }
    return {
      loading: false,
      loginForm: {
        username: "",
        password: ""
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { max: 20, trigger: 'blur', message: '最多不能超过20个字' },
          { validator: validateusername, trigger: 'blur' },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { validator: validatePassword, trigger: 'blur' },
        ]
      }
    };
  },
  methods: {
    login() {
      this.$refs.lform.validate(valid => {
        if (valid) {
          this.loading = true
          this.$axios.post(`/user/login`,this.loginForm).then(response => {
            if (response.data.code==='200') {
              this.$message({
                message: "登录成功了呢",
                type: "success"
              });
              const token=response.headers['authorization']
              sessionStorage.setItem("token", token)
              sessionStorage.setItem("userInfo", JSON.stringify(response.data.result))
              this.$router.push({ name: "system" });
              this.loading = false
            } else {
              this.$message({
                message: "账号或密码错误",
                type: "error"
              });
              this.loading = false
            }

          })
              .catch(e => {
                this.$message({
                  message: "网络异常！",
                  type: "error"
                });
                this.loading = false
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
    console.log("初始化咯")
    this.$emit('header',true);
    this.$emit('footer',true);
  }
}
</script>

<style lang="scss" scoped>
a {
  text-decoration: none;
  color: #409eff;
}

.login {
  /*display: flex;*/
  padding: 200px 0;
  width: 100%;
  height: 100%;
  background: url(../assets/img/loginbackground.jpg) no-repeat center;
  background-size: cover;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}

.box-card {
  width: 480px;
  margin: 0 auto;
}
.el-form-item__content {
  text-align: start;
}
</style>
