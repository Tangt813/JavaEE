<template>
  <el-card>
    <el-table
      ref="filterTable"
      :data="tableData"
      style="width: 100%"
      border
      stripe
    >
      <el-table-column type="index" width="50"> </el-table-column>
      <el-table-column label="图片" prop="picture">
        <template slot-scope="scope">
          <img
            :src="scope.row.picture"
            width="40"
            height="40"
            class="head_pic"
          />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="活动名称"> </el-table-column>
      <el-table-column prop="date" label="活动时间"> </el-table-column>
      <el-table-column prop="personNumber" label="参与人数"> </el-table-column>
      <el-table-column prop="description" label="活动介绍" width="180">
      </el-table-column>
      <!--      可能是tag的问题？？？？-->
      <el-table-column
        prop="tag"
        label="操作权限"
        width="200"
        :filters="[
          { text: '管理员', value: '管理员' },
          { text: '参加者', value: '参加者' },
        ]"
        :filter-method="filterTag"
        filter-placement="bottom-end"
      >
        <template slot-scope="scope">
          <el-tag :type="scope.row.buttonType" disable-transitions>{{
            scope.row.tag
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180px">
        <template slot-scope="scope">
          <el-tooltip
            class="item"
            effect="dark"
            content="编辑活动"
            placement="top"
          >
            <el-button
              type="primary"
              icon="el-icon-edit"
              :disabled="scope.row.isEditAble"
              @click="judgeLevel(scope.row.activityId)"
            ></el-button>
          </el-tooltip>
          <el-tooltip
            class="item"
            effect="dark"
            content="退出活动"
            placement="top"
          >
            <el-button
              type="danger"
              icon="el-icon-close"
              @click="judgequitLevel(scope.row.activityId)"
            ></el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      title="创建活动"
      :visible.sync="dialogVisible2"
      width="80%"
      :before-close="handleClose"
    >
      <span>
        <el-form ref="ruleForm" :rules="rules" :model="form" label-width="80px">
          <el-form-item label="活动照片">
            <el-upload
              class="avatar-uploader"
              action="http://150.158.47.16:521/upload/"
              :show-file-list="false"
              :on-preview="handlePictureCardPreview"
              :on-success="handleSuccess"
              :on-remove="handleRemove"
            >
              <img v-if="imageUrl" :src="imageUrl" class="avatar" />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
          <el-form-item label="活动名称" prop="name">
            <el-input v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="活动时间" prop="date1">
            <el-col :span="11">
              <el-date-picker
                type="date"
                placeholder="选择日期"
                v-model="form.date1"
                style="width: 100%"
              ></el-date-picker>
            </el-col>
          </el-form-item>
          <el-form-item label="对外开放">
            <el-switch
              v-model="form.isopen"
              active-text="是"
              inactive-text="否"
            >
            </el-switch>
          </el-form-item>
          <el-form-item label="活动详情" prop="desc">
            <el-input type="textarea" v-model="form.desc"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="warning" @click="onDelete(form)">删除</el-button>
            <el-button type="primary" @click="onSubmit(form)">修改</el-button>
            <el-button @click="dialogVisible2 = false">取消</el-button>
          </el-form-item>
        </el-form>
      </span>
      <span slot="footer" class="dialog-footer"> </span>
    </el-dialog>
    <el-dialog
      title="提示"
      :visible.sync="dialogVisible3"
      width="40%"
      :before-close="handleClose"
    >
      <span>确定要退出该活动吗？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible3 = false">取 消</el-button>
        <el-button type="primary" @click="sureQuit()">确 定</el-button>
      </span>
    </el-dialog>
  </el-card>
</template>

<script>
import axios from 'axios'
export default {
  name: 'myActivities',
  data () {
    return {
      user: {},
      userID: 3,

      isin: [],
      editForm: 0,
      returnimageUrl: '',
      total: 0,
      tableData: [],
      tag: [],
      imageUrl: '',
      dialogVisible2: false,
      dialogVisible3: false,
      prepareQuit: '',
      prepareEdit: '',
      form: {
        name: '',
        date1: '',
        isopen: false,
        desc: '',
        clubId: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入活动名称', trigger: 'blur' },
          {
            min: 1,
            max: 100,
            message: '长度在 1 到 100 个字符',
            trigger: 'blur'
          }
        ],
        date1: [
          {
            type: 'date',
            required: true,
            message: '请选择日期',
            trigger: 'change'
          }
        ],
        desc: [{ required: true, message: '请填写活动详情', trigger: 'blur' }]
      }
    }
  },
  created () {
    this.user = JSON.parse(window.localStorage.getItem('user'))
    //   console.log('读出来', this.user)
    // } else {
    // console.log('存咯')
    // this.user = JSON.parse(this.$route.query.user)
    // window.localStorage.setItem('user', JSON.stringify(this.user))
    // }
    console.log(this.user)
    if (this.user == null) {
      console.log('跳转')
      this.$router.push({
        path: '/error',
        query: {
        }
      })
    }
    this.userID = this.user.userId
    this.getallActivities()
  },

  methods: {
    handleClose (done) {
      this.$confirm('确认关闭？')
        .then((_) => {
          done()
        })
        .catch((_) => {})
    },
    handleSuccess (response, file, fileList) {
      this.imageUrl = URL.createObjectURL(file.raw)
      console.log(response)
      this.returnimageUrl = response.data
    },
    handleRemove (file, fileList) {
      console.log(file, fileList)
    },
    handlePictureCardPreview (file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    judgequitLevel (activityId) {
      this.prepareQuit = activityId
      this.dialogVisible3 = true
    },
    filterTag (value, row) {
      return row.tag === value
    },
    sureQuit () {
      let that = this
      axios({
        method: 'post',
        url: 'http://150.158.47.16:521/activity/quitactivity',
        params: {
          user_id: that.userID,
          activity_id: that.prepareQuit
        }
      })
        .then(function (resp) {
          window.location.reload()
          console.log(resp.data)
        })
      that.dialogVisible3 = false
    },
    judgeLevel (activityId) {
      let that = this
      that.prepareEdit = activityId
      axios({
        method: 'get',
        url: 'http://150.158.47.16:521/activity/findbyid',
        params: {
          activity_id: activityId
        }
      })
        .then(function (resp) {
          console.log(resp.data)
          that.imageUrl = resp.data.data.picture
          that.form.name = resp.data.data.name
          that.form.date1 = resp.data.data.date
          that.form.desc = resp.data.data.description
          that.form.clubId = resp.data.data.clubId
          if (resp.data.data.isPublic === 1) {
            that.form.isopen = true
          } else {
            that.form.isopen = false
          }
        })
      that.dialogVisible2 = true
    },
    async getallActivities () {
      let that = this
      console.log(that.userID)
      await axios({
        method: 'get',
        url: 'http://150.158.47.16:521/activity/findbyuserid',
        params: {
          user_id: that.userID
        }
      })
        .then(function (resp) {
          console.log(resp.data)
          that.total = resp.data.data.length
          for (let i = 0; i < that.total; i++) {
            that.tableData.push(resp.data.data[i].activity)
            // eslint-disable-next-line eqeqeq
            if (resp.data.data[i].level == -1 || resp.data.data[i].level == 0) {
              that.tableData[i].tag = '参加者'
              that.tableData[i].buttonType = 'success'
              that.tableData[i].isEditAble = true
            } else {
              that.tableData[i].tag = '管理员'
              that.tableData[i].buttonType = 'danger'
              that.tableData[i].isEditAble = false
            }
          }
          console.log(that.tableData)
        })
    },
    async onDelete (form) {
      let successful = false
      let that = this
      console.log(that.prepareEdit)
      await axios({
        method: 'post',
        url: 'http://150.158.47.16:521/activity/deleteactivity',
        params: {
          activity_id: that.prepareEdit
        }
      })
        .then(function (resp) {
          successful = true
          console.log(resp.data)
        })
      if (successful) {
        this.$alert('删除成功', '', {
          confirmButtonText: '确定',
          callback: (action) => {
            window.location.reload()
          }
        })
      }
    },
    async onSubmit (formName) {
      let successful = false
      let that = this
      // that.$refs[formName].validate((valid) => {
      //   if (valid) {
      //     alert('submit!');
      //   } else {
      //     console.log('error submit!!');
      //     return false;
      //   }
      // });
      if (that.form.isopen) {
        that.returnisopen = 1
      } else {
        that.returnisopen = 0
      }
      console.log(typeof that.form.date1.toString())
      await axios({
        method: 'post',
        url: 'http://150.158.47.16:521/activity/changeactivity',
        params: {
          activity_id: that.prepareEdit,
          file: that.returnimageUrl,
          description: that.form.desc,
          isPublic: that.returnisopen,
          club_id: that.form.clubId,
          name: that.form.name,
          date: that.form.date1.toString()
        }
      })
        .then(function (resp) {
          successful = true
          console.log(resp.data)
        })
      if (successful) {
        this.$alert('修改成功', '', {
          confirmButtonText: '确定',
          callback: (action) => {
            window.location.reload()
          }
        })
      } else {
        console.log(successful)
        this.$alert('修改失败', '请稍后重试！', {
          confirmButtonText: '确定',
          callback: (action) => {
            window.location.reload()
          }
        })
      }
      console.log('submit!')
    }
  }
}
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
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
