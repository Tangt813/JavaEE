<template>
    <div class="vcode" id="vcode" style=" box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);border-radius: 20px;margin-left:20%;margin-right:20%;margin-top:5%;margin-bottom:20%;height:500px">
        <h2>修改密码</h2>
      <div style="padding-left:20px;padding-right:20px">
 <!-- eslint-disable-next-line vue/no-parsing-error -->
<el-steps :active="active" finish-status="success">
  <el-step></el-step>
  <el-step></el-step>
<!-- eslint-disable-next-line vue/no-parsing-error -->
</el-steps>
</div>
<div v-if="this.active == 0">
<div>
<h3>输入验证码</h3>
    <input type="phone" ref="l" @keyup="nextinput" @keyup.delete="backspace" class="vcode-input1" maxlength="1" id="vcode1">
    <input type="phone" ref="h" @keyup="nextinput" @keyup.delete="backspace" class="vcode-input2" maxlength="1">
    <input type="phone" ref="b" @keyup="nextinput" @keyup.delete="backspace" class="vcode-input3" maxlength="1">
    <input type="phone" ref="n" @keyup="nextinput" @keyup.delete="backspace" class="vcode-input4" maxlength="1">
      </div>
      <el-button @click="getCountdown" :disabled="totalTime <60">{{content}}</el-button>
    <el-button style="margin-top: 12px;" @click="next">下一步</el-button>
</div>
<div v-if="this.active == 1">
    <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
  <el-form-item label="密码" prop="pass">
    <el-input type="password" v-model="ruleForm.pass" autocomplete="off" ref="password"></el-input>
  </el-form-item>
  <el-form-item label="确认密码" prop="checkPass" ref="checkpass">
    <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
  </el-form-item>
  <el-form-item>
    <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
    <el-button @click="resetForm('ruleForm')">重置</el-button>
  </el-form-item>
</el-form>
</div>
<div v-if="this.active==2">
    <el-result icon="success" title="更改成功" subTitle="点击导航栏返回主页面"></el-result>
</div>

  </div>
</template>
<script>
import axios from 'axios'
import AES from '@/common/AES.js'
export default {
  mounted () {
    // this.user = JSON.parse(this.$route.query.user)
    this.user = JSON.parse(window.localStorage.getItem('user'))
    //   console.log('读出来', this.user)
    // } else {
    // console.log('存咯')
    // this.user = JSON.parse(this.$route.query.user)
    // window.localStorage.setItem('user', JSON.stringify(this.user))
    // }
    if (this.user == null) {
      console.log('跳转')
      this.$router.push({
        path: '/error',
        query: {
        }
      })
    }
    console.log(JSON.stringify(this.user))
    if ((this.user == null) || (typeof this.user.userId === 'undefined')) {
      this.$router.push({
        path: '/error',
        query: {
        }
      })
    }
  },
  methods: {

    getCountdown () {
      axios({
        method: 'get',
        url: 'http://150.158.47.16:521/email/sendcode',
        params: {
          e_mail: this.user.eMail
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
    },
    nextinput (e) {
    // eslint-disable-next-line eqeqeq
      if (e.target.value.length == 1) {
      // eslint-disable-next-line eqeqeq
        if (e.target.className == 'vcode-input1') {
          this.$refs.h.focus() // 月输入框获取焦点
          // eslint-disable-next-line eqeqeq
        } else if (e.target.className == 'vcode-input2') {
          this.$refs.b.focus()
          // eslint-disable-next-line eqeqeq
        } else if (e.target.className == 'vcode-input3') {
          this.$refs.n.focus()
        }
        // eslint-disable-next-line eqeqeq
      }
    },

    backspace (e) {
    // eslint-disable-next-line eqeqeq
      if (e.target.value == '') {
      // eslint-disable-next-line eqeqeq
        if (e.target.className == 'month') {
          this.$refs.year.focus()
          // eslint-disable-next-line eqeqeq
        } else if (e.target.className == 'day') {
          this.$refs.month.focus()
        }
      }
    },
    next () {
      this.input = this.$refs.l.value + this.$refs.h.value + this.$refs.b.value + this.$refs.n.value
      let eninput = AES.encrypt(this.input)
      console.log(eninput)
      console.log(this.verifycode)
      // eslint-disable-next-line eqeqeq
      if (eninput != this.verifycode) {
        this.$message({
          showClose: true,
          message: '验证码错误',
          type: 'error'
        })
      } else { this.active++ }

    //   if (this.active++ > 2) this.active = 0
    },
    submitForm (formName) {
      let inputpass = this.$refs.password.value
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios({
            method: 'post',
            url: 'http://150.158.47.16:521/user/changepassword',
            params: {
              user_id: this.user.userId,
              password: inputpass,
              code: AES.encrypt(this.input),
              encode: this.verifycode
            }
          }).then((response) => {
            console.log(this.input)
            console.log(this.verifycode)
            let info = response.data
            console.log(info)
            this.active++
          }).catch(Error)
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    }
  },
  data () {
    var checkAge = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('年龄不能为空'))
      }
      setTimeout(() => {
        if (!Number.isInteger(value)) {
          callback(new Error('请输入数字值'))
        } else {
          if (value < 18) {
            callback(new Error('必须年满18岁'))
          } else {
            callback()
          }
        }
      }, 1000)
    }
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.ruleForm.pass) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      active: 0,
      ruleForm: {
        pass: '',
        checkPass: '',
        age: ''
      },
      rules: {
        pass: [
          { validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          { validator: validatePass2, trigger: 'blur' }
        ],
        age: [
          { validator: checkAge, trigger: 'blur' }
        ]
      },
      totalTime: 60,
      content: '发送验证码',
      user: {},
      verifycode: '',
      input: ''
    }
  }
}
</script>

<style scoped>
.vcode-input1 {
  width: 40px;
  height: 40px;
  text-align: center;
  font-size: 18px;
}
.vcode-input2 {
  width: 40px;
  height: 40px;
  text-align: center;
  font-size: 18px;
}
.vcode-input3{
  width: 40px;
  height: 40px;
  text-align: center;
  font-size: 18px;
}
.vcode-input4 {
  width: 40px;
  height: 40px;
  text-align: center;
  font-size: 18px;
}
.container {
  width: 800px;
  margin: 0 auto;
  padding: 100px;
  background: #FFF;
}
</style>
