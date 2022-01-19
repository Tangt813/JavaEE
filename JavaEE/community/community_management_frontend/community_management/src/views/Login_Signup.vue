<template>
  <div class="bg-img">
  <div class="user_options-container">
    <div class="user_options-text">
      <div class="user_options-unregistered">
        <h2 class="user_unregistered-title">初次见面</h2>
<!--        <p class="user_unregistered-text">Banjo tote bag bicycle rights, High Life sartorial cray craft beer whatever street art fap.</p>-->
        <button  class="user_unregistered-signup" id="signup-button">注 册</button>
      </div>

      <div class="user_options-registered">
        <h2 class="user_registered-title">已有账户</h2>
<!--        <p class="user_registered-text">Banjo tote bag bicycle rights, High Life sartorial cray craft beer whatever street art fap.</p>-->
        <button class="user_registered-login" id="login-button">登 录</button>
      </div>
    </div>

    <div class="user_options-forms" id="user_options-forms">
      <div class="user_forms-login">
        <h2 class="forms_title">登 录</h2>
        <form class="forms_form">
          <fieldset class="forms_fieldset">
            <div class="forms_field">
              <input id='loginemail' ref="loginemail" type="email" placeholder="Email" class="forms_field-input1" required autofocus />
            </div>
            <div class="forms_field">
              <input id='loginpassword' ref="loginpassword" type="password" placeholder="Password" class="forms_field-input1" required />
            </div>
          </fieldset>
          <div class="forms_buttons">
            <button type="button" class="forms_buttons-forgot" @click="forgetpassword()">忘记密码</button>
             <!-- eslint-disable-next-line vue/valid-v-on -->
            <button type='button' @click="Login()" style="width:140px;height:50px" class="forms_buttons-action" >登 录</button>
          </div>
        </form>
      </div>
      <div class="user_forms-signup">
        <h2 class="forms_title">注 册</h2>
        <el-upload
          class="avatar-uploader"
          action="http://150.158.47.16:521/upload/"
          :show-file-list="false"
          :on-preview="handlePictureCardPreview"
          :on-success="handleSuccess"
          :on-remove="handleRemove">
          <img v-if="imageUrl" :src="imageUrl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
        <el-tag style="margin-left: -6px; margin-bottom: 10px" type="danger" effect="plain">点击"+"上传头像</el-tag>
        <form class="forms_form">
          <fieldset class="forms_fieldset">
            <div class="forms_field">
              <input id='signupname' ref="signupname" type="text" placeholder="Full Name" class="forms_field-input2" required />
            </div>
            <div class="forms_field">
              <input id='signupemail' ref="signupemail" type="email" placeholder="Email" class="forms_field-input2" required />
            </div>
             <div class="forms_field">
              <input id='signupvertification' ref="signupvertification" type="code" placeholder="Verification Code" class="forms_field-input2" required />
            </div>
            <div class="forms_field">
              <input id='sinuppassword' ref="signuppassword" type="password" placeholder="Password" class="forms_field-input2" required />
            </div>
          </fieldset>
          <div class="forms_buttons">
            <el-button style="margin-right:20px;width:180px;height:40px;font-size:14px" class="forms_buttons-action" @click="getCountdown" :disabled="totalTime <60">{{content}}</el-button>
            <el-button v-if="ifsignup" type='button' style="width:140px;height:40px" class="forms_buttons-action" @click="trysignup()">注 册</el-button>
          </div>
        </form>
      </div>
    </div>
  </div>
  </div>
</template>

<script>
import AES from '@/common/AES.js'
import axios from 'axios'
export default {
  name: 'Login_Signup',
  mounted () {
    /**
     * Variables
     */
    // eslint-disable-next-line one-var
    const signupButton = document.getElementById('signup-button'),
      loginButton = document.getElementById('login-button'),
      userForms = document.getElementById('user_options-forms')

    /**
     * Add event listener to the "Sign Up" button
     */
    signupButton.addEventListener(
      'click',
      () => {
        userForms.classList.remove('bounceRight')
        userForms.classList.add('bounceLeft')
      },
      false
    )

    /**
     * Add event listener to the "Login" button
     */
    loginButton.addEventListener(
      'click',
      () => {
        userForms.classList.remove('bounceLeft')
        userForms.classList.add('bounceRight')
      },
      false
    )
  },
  data () {
    return {
      content: '发送验证码',
      imageUrl: '',
      returnimageUrl: '',
      email: '',
      password: '',
      enpassword: '',
      loginresult: 'incorrectPassword',
      user: {},
      totalTime: 60,
      signupname: '',
      signupemail: '',
      signupverification: '',
      signuppassword: '',
      verifycode: '',
      ifsignup: false,
      tokenvalue: ''
    }
  },
  methods: {
    forgetpassword () {
      this.$router.push({
        path: '/forgetpassword',
        query: {
        }
      })
    },
    trysignup () {
      // eslint-disable-next-line eqeqeq
      console.log(AES.encrypt(this.signupverification))
      console.log(this.verifycode)
      // eslint-disable-next-line eqeqeq
      if (this.$refs.signupemail.value != this.signupemail) {
        this.ifsignup = false
        this.$message.error('邮箱地址变更，请重新发送验证码！')
      } else {
        this.signupname = this.$refs.signupname.value
        this.signupemail = this.$refs.signupemail.value
        this.signupverification = this.$refs.signupvertification.value
        this.signuppassword = this.$refs.signuppassword.value
        // eslint-disable-next-line eqeqeq
        if (this.signupname == '') {
          this.$message.error('请输入姓名！')
          // eslint-disable-next-line eqeqeq
        } else if (this.signupemail == '') {
          this.$message.error('请输入邮箱！')
          // eslint-disable-next-line eqeqeq
        } else if (this.signupverification == '') {
          this.$message.error('请输入验证码！')
          // eslint-disable-next-line eqeqeq
        } else if (this.signuppassword == '') {
          this.$message.error('请输入密码！')
        } else {
          // eslint-disable-next-line eqeqeq
          if (AES.encrypt(this.signupverification) == this.verifycode) {
            axios({
              method: 'post',
              url: 'http://150.158.47.16:521/user/signup',
              params: {
                headFile: this.returnimageUrl,
                name: this.signupname,
                password: AES.encrypt(this.signuppassword),
                e_mail: this.signupemail
              }
            }).then((response) => {
              console.log(response.data)
            }).catch(Error)
            this.$message({
              message: '注册成功',
              type: 'success'
            })
          } else {
            this.$message.error('验证码错误！')
          }
        }
      }
    },
    getCountdown () {
      this.signupname = this.$refs.signupname.value
      this.signupemail = this.$refs.signupemail.value
      this.signupverification = this.$refs.signupvertification.value
      this.signuppassword = this.$refs.signuppassword.value
      // eslint-disable-next-line eqeqeq
      if (this.signupname == '') {
        this.$message.error('请输入姓名！')
      // eslint-disable-next-line eqeqeq
      } else if (this.signupemail == '') {
        this.$message.error('请输入邮箱！')
      // eslint-disable-next-line eqeqeq
      } else if (this.signuppassword == '') {
        this.$message.error('请输入密码！')
      } else {
        this.ifsignup = true
        axios({
          method: 'get',
          url: 'http://150.158.47.16:521/email/sendcode',
          params: {
            e_mail: this.signupemail
          }
        }).then((response) => {
          this.verifycode = response.data
          console.log(response)
        }).catch(Error)
        let clock = window.setInterval(() => {
          this.content = this.totalTime + 's后重新发送'
          this.totalTime--
          if (this.totalTime < 0) {
            this.totalTime = 60
            this.content = '重新发送验证码'
            window.clearInterval(clock)
          }
        }, 1000)
      }
    },
    handleSuccess (response, file, fileList) {
      this.imageUrl = URL.createObjectURL(file.raw)
      console.log(response.data)
      this.returnimageUrl = response.data
    },
    handleRemove (file, fileList) {
      console.log(file, fileList)
    },
    handlePictureCardPreview (file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    async Login () {
      console.log('登陆')
      this.email = this.$refs.loginemail.value
      this.password = this.$refs.loginpassword.value
      console.log('password', this.password)
      this.enpassword = AES.encrypt(this.password)
      console.log(this.enpassword)
      await axios({
        method: 'get',
        url: 'http://150.158.47.16:521/user/login',
        params: {
          e_mail: this.email,
          password: this.enpassword
        }
      }).then((response) => {
        this.loginresult = response.data.msg
        console.log(this.loginresult)
        // eslint-disable-next-line eqeqeq
        this.user = response.data.data.user
        this.tokenvalue = response.data.data.token.tokenValue
      }
      ).catch(Error)
      // eslint-disable-next-line eqeqeq
      // eslint-disable-next-line eqeqeq
      // eslint-disable-next-line eqeqeq
      console.log(this.loginresult)
      // eslint-disable-next-line eqeqeq
      if (this.loginresult == 'incorrectPassword') {
        this.$message.error('用户名或密码错误')
        return false
      } else {
        this.$message({
          message: '登录成功',
          type: 'success'
        })
        console.log(JSON.stringify(this.user))
        window.localStorage.setItem('user', JSON.stringify(this.user))
        window.localStorage.setItem('token', JSON.stringify(this.tokenvalue))
        this.$router.push({
          path: '/Home',
          query: {
            // user: JSON.stringify(this.user)
          }
        })
        return true
      }
    }
  }
}
</script>

<style scoped>
/**
 * * General variables
 * */
/**
 * * General configs
 * */
* {
  box-sizing: border-box;
}

.bg-img{
  content: "";
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-image: linear-gradient(to bottom right, #002f4b, #dc4225);
  opacity: 0.6;
}
body {
  font-family: "Montserrat", sans-serif;
  font-size: 12px;
  line-height: 1em;
}

button {
  background-color: transparent;
  padding: 0;
  border: 0;
  outline: 0;
  cursor: pointer;
}

input {
  background-color: transparent;
  padding: 0;
  border: 0;
  outline: 0;
}
input[type=submit] {
  cursor: pointer;
}
input::-moz-placeholder {
  font-size: 0.85rem;
  font-family: "Montserrat", sans-serif;
  font-weight: 300;
  letter-spacing: 0.1rem;
  color: #ccc;
}
input:-ms-input-placeholder {
  font-size: 0.85rem;
  font-family: "Montserrat", sans-serif;
  font-weight: 300;
  letter-spacing: 0.1rem;
  color: #ccc;
}
input::placeholder {
  font-size: 0.85rem;
  font-family: "Montserrat", sans-serif;
  font-weight: 300;
  letter-spacing: 0.1rem;
  color: #ccc;
}

/**
 * * Bounce to the left side
 * */
@-webkit-keyframes bounceLeft {
  0% {
    transform: translate3d(100%, -50%, 0);
  }
  50% {
    transform: translate3d(-30px, -50%, 0);
  }
  100% {
    transform: translate3d(0, -50%, 0);
  }
}
@keyframes bounceLeft {
  0% {
    transform: translate3d(100%, -50%, 0);
  }
  50% {
    transform: translate3d(-30px, -50%, 0);
  }
  100% {
    transform: translate3d(0, -50%, 0);
  }
}
/**
 * * Bounce to the left side
 * */
@-webkit-keyframes bounceRight {
  0% {
    transform: translate3d(0, -50%, 0);
  }
  50% {
    transform: translate3d(calc(100% + 30px), -50%, 0);
  }
  100% {
    transform: translate3d(100%, -50%, 0);
  }
}
@keyframes bounceRight {
  0% {
    transform: translate3d(0, -50%, 0);
  }
  50% {
    transform: translate3d(calc(100% + 30px), -50%, 0);
  }
  100% {
    transform: translate3d(100%, -50%, 0);
  }
}
/**
 * * Show Sign Up form
 * */
@-webkit-keyframes showSignUp {
  100% {
    opacity: 1;
    visibility: visible;
    transform: translate3d(0, 0, 0);
  }
}
@keyframes showSignUp {
  100% {
    opacity: 1;
    visibility: visible;
    transform: translate3d(0, 0, 0);
  }
}
/**
 * * Page background
 * */
.user {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
  background: #ccc;
  background-size: cover;
}
.user_options-container {
  position: relative;
  width: 100%;
  margin-top: 300px;
}
.user_options-text {
  display: flex;
  justify-content: space-between;
  width: 100%;
  background-color: rgba(34, 34, 34, 0.85);
  border-radius: 3px;
}

/**
 * * Registered and Unregistered user box and text
 * */
.user_options-registered,
.user_options-unregistered {
  width: 50%;
  padding: 75px 45px;
  color: #fff;
  font-weight: 300;
}

.user_registered-title,
.user_unregistered-title {
  margin-bottom: 15px;
  font-size: 1.66rem;
  line-height: 1em;
}

.user_unregistered-text,
.user_registered-text {
  font-size: 0.83rem;
  line-height: 1.4em;
}

.user_registered-login,
.user_unregistered-signup {
  margin-top: 30px;
  border: 1px solid #ccc;
  border-radius: 3px;
  padding: 10px 30px;
  color: #fff;
  text-transform: uppercase;
  line-height: 1em;
  letter-spacing: 0.2rem;
  transition: background-color 0.2s ease-in-out, color 0.2s ease-in-out;
}
.user_registered-login:hover,
.user_unregistered-signup:hover {
  color: rgba(34, 34, 34, 0.85);
  background-color: #ccc;
}

/**
 * * Login and signup forms
 * */
.user_options-forms {
  position: absolute;
  top: 50%;
  left: 30px;
  width: calc(50% - 30px);
  min-height: 420px;
  background-color: #fff;
  border-radius: 3px;
  box-shadow: 2px 0 15px rgba(0, 0, 0, 0.25);
  overflow: hidden;
  transform: translate3d(100%, -50%, 0);
  transition: transform 0.4s ease-in-out;
}
.user_options-forms .user_forms-login {
  transition: opacity 0.4s ease-in-out, visibility 0.4s ease-in-out;
}
.user_options-forms .forms_title {
  margin-top: -45px;
  font-size: 1.5rem;
  font-weight: 500;
  line-height: 1em;
  text-transform: uppercase;
  color: #e8716d;
  letter-spacing: 0.1rem;
}
.user_options-forms .forms_field:not(:last-of-type) {
  margin-bottom: 20px;
}
.user_options-forms .forms_field-input1 {
  width: 100%;
  border-bottom: 1px solid #ccc;
  padding: 6px 20px 6px 6px;
  font-family: "Montserrat", sans-serif;
  font-size: 1rem;
  font-weight: 300;
  color: gray;
  letter-spacing: 0.1rem;
  transition: border-color 0.2s ease-in-out;
}
.forms_field-input2{
  width: 100%;
  border-bottom: 1px solid #ccc;
  padding: 6px 20px 6px 6px;
  font-family: "Montserrat", sans-serif;
  font-size: 0.3rem;
  font-weight: 300;
  color: gray;
  letter-spacing: 0.1rem;
  transition: border-color 0.2s ease-in-out;
}
.user_options-forms .forms_field-input:focus {
  border-color: gray;
}
.user_options-forms .forms_buttons {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 35px;
}
.user_options-forms .forms_buttons-forgot {
  font-family: "Montserrat", sans-serif;
  letter-spacing: 0.1rem;
  color: #ccc;
  text-decoration: underline;
  transition: color 0.2s ease-in-out;
}
.user_options-forms .forms_buttons-forgot:hover {
  color: #b3b3b3;
}
.user_options-forms .forms_buttons-action {
  background-color: #e8716d;
  border-radius: 3px;
  padding: 10px 35px;
  font-size: 1rem;
  font-family: "Montserrat", sans-serif;
  font-weight: 300;
  color: #fff;
  text-transform: uppercase;
  letter-spacing: 0.1rem;
  transition: background-color 0.2s ease-in-out;
}
.user_options-forms .forms_buttons-action:hover {
  background-color: #e14641;
}
.user_options-forms .user_forms-signup,
.user_options-forms .user_forms-login {
  position: absolute;
  top: 70px;
  left: 40px;
  width: calc(100% - 80px);
  opacity: 0;
  visibility: hidden;
  transition: opacity 0.4s ease-in-out, visibility 0.4s ease-in-out, transform 0.5s ease-in-out;
}
.user_options-forms .user_forms-signup {
  transform: translate3d(120px, 0, 0);
}
.user_options-forms .user_forms-signup .forms_buttons {
  justify-content: flex-end;
}
.user_options-forms .user_forms-login {
  transform: translate3d(0, 0, 0);
  opacity: 1;
  visibility: visible;
}

/**
 * * Triggers
 * */
.user_options-forms.bounceLeft {
  -webkit-animation: bounceLeft 1s forwards;
  animation: bounceLeft 1s forwards;
}
.user_options-forms.bounceLeft .user_forms-signup {
  -webkit-animation: showSignUp 1s forwards;
  animation: showSignUp 1s forwards;
}
.user_options-forms.bounceLeft .user_forms-login {
  opacity: 0;
  visibility: hidden;
  transform: translate3d(-120px, 0, 0);
}
.user_options-forms.bounceRight {
  -webkit-animation: bounceRight 1s forwards;
  animation: bounceRight 1s forwards;
}

/**
 * * Responsive 990px
 * */
@media screen and (max-width: 990px) {
  .user_options-forms {
    min-height: 350px;
  }
  .user_options-forms .forms_buttons {
    flex-direction: column;
  }
  .user_options-forms .user_forms-login .forms_buttons-action {
    margin-top: 30px;
  }
  .user_options-forms .user_forms-signup,
  .user_options-forms .user_forms-login {
    top: 40px;
  }

  .user_options-registered,
  .user_options-unregistered {
    padding: 50px 45px;
  }
}
.avatar-uploader .el-upload {
  border: 3px dashed #050505;
  border-radius: 6px;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #e55858;
  border-width: 3px;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #e55858;
  width: 50px;
  height: 50px;
  line-height: 50px;
  text-align: center;
}
.avatar {
  width: 30px;
  height: 30px;
  display: block;
}
</style>
