<template>
  <div class="layout" >
<el-menu
  :default-active="$route.path"
  class="el-menu-demo"
  mode="horizontal"
  @select="handleSelect"
  background-color="#000000"
  text-color="#fff"
  active-text-color="#00BFFF"
  router
  style="height:10%">
  <el-menu-item index="/Home/activities">活动广场</el-menu-item>
  <el-menu-item index="/Home/community_center" >社团中心</el-menu-item>
  <el-menu-item index="/Home/my_communities" >我的社团</el-menu-item>
  <el-menu-item v-if="ifadmin" index="/Home/system_management">系统管理</el-menu-item>
  <p align="right" style="margin:20px;height:100%">
  <el-dropdown @command="openuser">
     <font size="4">
  <span class="el-dropdown-link">
      <i class="el-icon-user-solid"></i>
    {{this.user.name}}<i class="el-icon-arrow-down el-icon--right"></i>
  </span>
  </font>
  <el-dropdown-menu slot="dropdown">
    <el-dropdown-item command="a"><font size="2">个人信息</font></el-dropdown-item>
    <el-dropdown-item command="b"><font size="2">退出登录</font></el-dropdown-item>
  </el-dropdown-menu>
</el-dropdown>
  </p>
</el-menu>
<el-dialog
  title='个人信息'
  :visible.sync="centerDialogVisible"
  width="30%"
  center
  >
      <el-dialog
      width="30%"
      title="编辑信息"
      :visible.sync="innerVisible2"
      append-to-body>
      <div style="text-align:center">
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
      </div>
      <div style="margin:30px"></div>
      <div style="text-align:center">
      <font style="width:20%;margin-left:10%">姓名</font>
      <el-input
      ref="name"
  placeholder="请输入内容"
  v-model="nameinput"
  style="width:60%;margin-left:10%"
  clearable>
</el-input>
      </div>
      <div style="text-align:center;width:100%;margin-top:30px">
        <el-button round @click="changeinfo()" >保存</el-button>
      </div>
    </el-dialog>
    <div style="text-align:center;width:100%;">
      <el-avatar :src= "user.headPortrait" :size="75"></el-avatar>
    </div>
    <div style="text-align:center;width:100%">
  <span>{{user.eMail}}</span>
  <br>
  <br>
  <span>{{user.name}}</span>
  <br>
  <span>{{getRole(user.role)}}</span>
    </div>
  <span slot="footer" class="dialog-footer">
    <el-button @click="clickChangeName()">编辑信息</el-button>
    <el-button type="primary" @click="clickChangePwd()">修改密码</el-button>
  </span>
</el-dialog>
  <el-dialog
      style="width:30%"
      title="修改密码"
      :visible.sync="innerVisible1"
      append-to-body>
  </el-dialog>
<!-- <router-view :user="user"></router-view> -->
<router-view></router-view>
  </div>
</template>
<script>
import axios from 'axios'
export default {
  mounted: function () {
    console.log('执行导航')
    // if (this.$route.query.user == null) {
    console.log('读咯')
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
    this.imageUrl = this.user.headPortrait
    this.nameinput = this.user.name
    console.log(this.user)
    // eslint-disable-next-line eqeqeq
    if (this.user.role == 'user') {
      this.ifadmin = false
    } else {
      this.ifadmin = true
    }
  },
  data () {
    return {
      ifadmin: false,
      dialogImageUrl: '',
      dialogVisible: false,
      imageUrl: '',
      returnimageUrl: '',
      nameinput: '',
      activeIndex: '1',
      activeIndex2: '1',
      centerDialogVisible: false,
      innerVisible1: false,
      innerVisible2: false,
      user: {},
      smsCode: '',
      code: new Array(5),
      codeId: ['first', 'second', 'third', 'forth', 'fifth']
    }
  },
  watch: {
    code: function (newValue, oldValue) {
      console.log('newValue.length' + newValue.length)
      let tempValue = ''
      for (let i = 0; i < newValue.length; i++) {
        // eslint-disable-next-line eqeqeq
        if (i == 5) {
          console.log(i)
          break
        }
        if (newValue[i]) {
          tempValue = tempValue + newValue[i]
        }
      }
      this.smsCode = tempValue
      console.log('smsCode ' + this.smsCode)
      let m = tempValue.split('')
      let location = 0
      for (let i = 0; i < newValue.length; i++) {
        if (m[i] && i < 5) {
          location++
          newValue[i] = m[i]
        } else {
          newValue[i] = ''
        }
      }
      // console.log(this.codeId[i])
      if (location < 1) {
        location = 1
      } else if (location > 5) {
        location = 5
      }
      document.getElementById(this.codeId[location - 1]).focus()
    }
  },
  methods: {
    changeinfo () {
      this.user.name = this.$refs.name.value
      this.user.headPortrait = this.returnimageUrl
      window.localStorage.setItem('user', JSON.stringify(this.user))
      // eslint-disable-next-line no-undef
      let s = JSON.parse(window.localStorage.getItem('token'))
      console.log(s)
      axios({
        method: 'post',
        url: 'http://150.158.47.16:521/user/changeinfo',
        headers: {
          satoken: s
        },
        params: {
          headFile: this.returnimageUrl,
          name: this.user.name
        }
      }).then((response) => {
        console.log(response.data)
      }).catch(Error)
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
    beforeAvatarUpload (file) {
      const isJPG = file.type === 'image/jpeg'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    },
    clickChangePwd () {
      this.centerDialogVisible = false
      console.log(this.user)
      this.$router.push({
        path: '/Home/user/changepassword',
        query: {
          // user: JSON.stringify(this.user)
        }
      }).catch(err => { console.log(err) })
    },
    clickChangeName () {
      this.innerVisible2 = true
    },
    handleSelect (key, keyPath) {
      console.log(key, keyPath)
    },
    openuser (command) {
      // eslint-disable-next-line eqeqeq
      if (command == 'a') {
        this.centerDialogVisible = true
      // eslint-disable-next-line eqeqeq
      } else if (command == 'b') {
        axios({
          method: 'get',
          url: 'http://150.158.47.16:521/user/logout',
          headers: {
            satoken: JSON.parse(window.localStorage.getItem('token'))
          }
        }).then((response) => {
          console.log(response)
        }).catch(Error)
        localStorage.clear()
        this.$router.push({
          path: '/Login',
          query: {
          }
        })
      }
    },
    getRole (role) {
      // eslint-disable-next-line eqeqeq
      if (role == 'user') {
        return '用户'
      } else {
        return '管理员'
      }
    }
  }
}
</script>
<style scoped>
  .el-dropdown-link {
    cursor: pointer;
    color: #fff;
  }
  .el-icon-arrow-down {
    font-size: 12px;
  }
  .inputStyle{
 width:20%;
 height:100%;
 text-align:center;
 border:none;
}
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
