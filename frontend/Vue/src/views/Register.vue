<template>
  <div class="register">
    <el-card class="register-card">
      <div slot="header" class="clearfix">
        <span>注册</span>
      </div>
      <div>
        <el-form
            ref="registerForm"
            :model="form"
            class="register-form"
            :rules="registerRules"


        >
          <el-form-item prop="username">
            <el-input
                v-model.trim="form.username"
                v-focus
                style="margin-top: 20px"
                type="text"
                placeholder="请输入用户名"
                auto-complete="off"
            >
            </el-input>
          </el-form-item>
          <el-form-item prop="phone">
            <el-input
                v-model.trim="form.phone"
                type="text"
                placeholder="请输入手机号"
                maxlength="11"
                show-word-limit
                autocomplete="off"
            >
            </el-input>
          </el-form-item>
          <el-form-item prop="phoneCode" style="position: relative">
            <el-input
                v-model.trim="form.phoneCode"
                type="text"
                placeholder="手机验证码"
                style="width: 50%"
            >
            </el-input>
            <el-button
                type="primary"
                class="phone-code"
                :disabled="isGetphone"
                @click="getPhoneCode"

            >
              {{ phoneCode }}
            </el-button>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
                v-model.trim="form.password"
                type="password"
                placeholder="设置密码"
                autocomplete="new-password"
            >
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button
                class="register-btn"
                type="primary"
                @click="handleRegister"
            >
              注册
            </el-button>
            <router-link to="/login">
              <div style="margin-top: 10px">登录</div>
            </router-link>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>
<script>
import { isPassword, isPhone } from '@/utils/validate'
export default {
  name: 'Register',
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
    const validatePhone = (rule, value, callback) => {
      if (!isPhone(value)) {
        callback(new Error('请输入正确的手机号'))
      } else {
        callback()
      }
    }
    return {
      isGetphone: false,
      getPhoneIntval: null,
      phoneCode: '获取验证码',
      showRegister: false,
      form: {
        username:"",
        phone:"",
        password:"",
        phoneCode:""
      },
      registerRules: {
        username: [
          { required: true, trigger: 'blur', message: '请输入用户名' },
          { max: 20, trigger: 'blur', message: '最多不能超过20个字' },
          { validator: validateusername, trigger: 'blur' },
        ],
        phone: [
          { required: true, trigger: 'blur', message: '请输入手机号码' },
          { validator: validatePhone, trigger: 'blur' },
        ],
        password: [
          { required: true, trigger: 'blur', message: '请输入密码' },
          { validator: validatePassword, trigger: 'blur' },
        ],
        phoneCode: [
          { required: true, trigger: 'blur', message: '请输入手机验证码' },
        ],
      },
      loading: false,
      passwordType: 'password',
    }
  },
  created() {
    this.$emit('footer',true);
    this.$emit('header',true);
  },
  beforeDestroy() {
  },
  methods: {
    getPhoneCode() {

      this.$axios.post('/user/sendMsg', this.form).then(response => {
        if (response.data.code==='200') {
          this.$message({
            message: "发送验证码成功！",
            type: "success"
          });
        } else {
          this.$message({
            message: response.data.message,
            type: "error"
          });
        }
      })
      if (!isPhone(this.form.phone)) {
        this.$refs['registerForm'].validateField('phone')
        return
      }
      this.isGetphone = true
      let n = 60
      this.getPhoneIntval = setInterval(() => {
        if (n > 0) {
          n--
          this.phoneCode = '重新获取(' + n + 's)'
        } else {
          // this.getPhoneIntval = null
          clearInterval(this.getPhoneIntval)
          this.phoneCode = '获取验证码'
          this.isGetphone = false
        }
      }, 1000)
    },
    handleRegister() {
      this.$refs['registerForm'].validate(async (valid) => {
        if (valid) {
          this.$axios.post(`/user/register`,this.form).then(response => {
            window.console.log(response);
            if (response.data.code==='200') {
              this.$message({
                message: "注册成功了呢",
                type: "success"
              });
            } else {
              this.$message({
                message: response.data.message,
                type: "error"
              });
            }
          })
          .catch(e => {
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
      })
    },
  },
}
</script>
<style lang="scss" scoped>
a {
  text-decoration: none;
  color: #409eff;
}
.register {
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

.register-card {
  width: 480px;
  margin: 0 auto;

}
.el-form-item__content {
  text-align: start;
}

</style>