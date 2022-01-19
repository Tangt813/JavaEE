<template>
  <div class="container" style="height:80vh">
    <div class="wrapper" style="height:80vh">
      <div class="company-info" style="height:80vh">
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
        <el-tag style="margin-left: -6px">点击"+"上传头像</el-tag>
      </div>
      <div class="contact" style="height:80vh">
        <h3>基本信息</h3>
        <form action="">
          <p>
            <label for="">负责人姓名:</label>
            <input type="text" v-model="dueName" readonly="readonly"/>
          </p>
          <p>
            <label for="">负责人电话:</label>
            <input type="text" v-model="duePhone" required>
          </p>
          <p>
            <label for="">社团名称:</label>
            <input type="text" v-model="organizationName" required>
          </p>
          <p>
            <label for="">入会费:</label>
            <input type="text" v-model="cost">
          </p>
          <p class="full">
            <label for="">简介:</label>
            <textarea name="message" id="" cols="30" rows="10" v-model="description"></textarea>
          </p>
          <p class="full">
            <el-button @click="submit()">创建</el-button>
          </p>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  mounted () {
    this.user = JSON.parse(window.localStorage.getItem('user'))
    if (this.user == null) {
      console.log('跳转')
      this.$router.push({
        path: '/error',
        query: {
        }
      })
    } else {
      this.user_id = JSON.parse(window.localStorage.getItem('user')).userId
      this.dueName = JSON.parse(window.localStorage.getItem('user')).name
    }
  },
  data () {
    return {
      user: {},
      user_id: 4,
      dueName: 'default',
      duePhone: '',
      organizationName: '',
      cost: 0.0,
      description: '',
      imageUrl: '',
      returnimageUrl: '',
      formData: new FormData()
    }
  },
  name: 'AddOrganization',
  methods: {
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
    async submit () {
      let that = this
      let successful = false
      // eslint-disable-next-line eqeqeq
      if (that.duePhone == '' || that.organizationName == '') {
        this.$alert('负责人电话/社团名称不能为空', '警告！', {
          confirmButtonText: '确定',
          callback: action => {
          }
        })
        return
      }
      console.log(that.returnimageUrl)
      await that.$axios({
        method: 'post',
        url: 'http://150.158.47.16:521/club/addclub',
        params: {
          headFile: that.returnimageUrl,
          description: that.description,
          expenditure: that.cost,
          name: that.organizationName,
          phone_number: that.duePhone,
          user_id: that.user_id
        }
      }).then(function (resp) {
        console.log(resp.data)
        successful = true
      })

      if (successful) {
        this.$alert('创建成功', '恭喜你！', {
          confirmButtonText: '确定',
          callback: action => {
            window.location.reload()
          }
        })
      } else {
        this.$alert('创建失败', '请重试！', {
          confirmButtonText: '确定',
          callback: action => {
            window.location.reload()
          }
        })
      }

      // this.formData.append('detail', this.ruleForm.detail);
    }
  }
}
</script>

<style scoped>
* {
  box-sizing: border-box;
}

body {
  background: #92cde7;
  color: rgba(116, 72, 72, 0.15);
  line-height: 1.6;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  padding: 1em;
}

.container {
  max-width: 1170px;
  margin-left: auto;
  margin-right: auto;
  padding: 1em;
}

ul {
  list-style: none;
  padding: 0;
}

.brand {
  text-align: center;
}

.brand span {
  color: white;
}

.wrapper {
  box-shadow: 0 0 20px 0 rgba(72, 108, 116, 0.7)
}

.wrapper > * {
  padding: 1em;
}

.company-info {
  width: 150px;
  background: #c9e2ff;
}

.company-info h3, .company-info ul {
  text-align: center;
  margin: 0 0 1rem 0;
}

.contact {
  width: 1000px;
  background: #f9feff;
}

/* FORM STYLES*/

.contact form {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-gap: 20px;
}

.contact form label {
  display: block;
}

.contact form p {
}

.contact form .full {
  grid-column: 1 / 3;
}

.contact form button, .contact form input, .contact form textarea {
  width: 100%;
  padding: 1em;
  border: 1px solid #c9f1ff;
}

.contact form button {
  background: #c9e2ff;
  border: 0;
  text-transform: uppercase;
}

.contact form button:hover, .contact form button:focus {
  background: #92c6e7;
  color: #fff;
  outline: 0;
  transition: background-color 1s ease-out;
}

/* LARGE SCREENS  */

@media (min-width:700px) {
  .wrapper {
    display: grid;
    grid-template-columns: 1fr 2fr;
  }

  .wrapper > * {
    padding: 2em;
  }

  .company-info h3, .company-info ul, .brand {
    text-align: left;
  }
}
.con4 {
  width: 100px;
  height: auto;
  color: #a1deff;
}

.con4 .btn {
  width: 100%;
  height: 30px;
  line-height: 30px;
  text-align: center;
  background: #ffffff;
  display: block;
  font-size: 15px;
  border-radius: 5px;
}

.upload {
  float: left;
  position: relative;
}

.upload_pic {
  display: block;
  width: 100%;
  height: 100px;
  position: absolute;
  left: 0;
  top: 0;
  opacity: 0;
  border-radius: 5px;
}

#cvs {
  border: 2px solid #ffffff;
  border-radius: 15px;
}
.avatar-uploader .el-upload {
  border: 3px dashed #050505;
  border-radius: 6px;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #ffffff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #ffffff;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}
.avatar {
  width: 100px;
  height: 100px;
  display: block;
}
.head-img {
  text-align: center;
  margin-bottom: 0.2rem;
}
.head-img .border {
  display: inline-block;
  width: 7rem;
  height: 7rem;
  border-radius: 50%;
  border: 1px solid #c3c3c3;
  overflow: hidden;
  line-height: 7rem;
  text-align: center;
  position: relative;
}
.head-img .border .imgDiv {
  width: 100%;
  height: 100%;
  position: absolute;
  left: 0;
  top: 0;
  border-radius: 50%;
  -webkit-border-radius: 50%;
  -moz-border-radius: 50%;

}
</style>
