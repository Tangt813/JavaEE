<template>
  <el-card>
    <el-table ref="filterTable" :data="tableData2" style="width: 100%"  border stripe>
      <el-table-column
        type="index"
        width="50">
      </el-table-column>
      <el-table-column label="Logo" prop="logo">
        <template slot-scope="scope">
        <img :src="scope.row.logo" width="40" height="40" class="head_pic"/>
        </template>
      </el-table-column>
      <el-table-column
        prop="name"
        label="社团名称"
        width="180">
      </el-table-column>
      <el-table-column
        prop="phoneNumber"
        label="联系方式">
      </el-table-column>
      <el-table-column
        prop="tag"
        label="您的身份"
        width="100"
        :filters="[{ text: '成员', value: '成员' }, { text: '管理员', value: '管理员' },{ text: '社长', value: '社长' }]"
        :filter-method="filterTag"
        filter-placement="bottom-end">
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.buttontype"
            disable-transitions>{{scope.row.tag}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="270px">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="管理" placement="top">
            <el-button type="primary" icon="el-icon-edit" :disabled="scope.row.isAble" @click="judgeLevel(scope.row.clubId)"></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="删除社团" placement="top">
            <el-button type="danger" icon="el-icon-close" :disabled="scope.row.isdeleteAble" @click="judgedeleteLevel(scope.row.clubId)"></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="创建活动" placement="top">
            <el-button type="success" icon="el-icon-plus" :disabled="scope.row.iscreateAble" @click="judgecreateLevel(scope.row.clubId)"></el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      title="警告"
      :visible.sync="dialogVisible3"
      width="30%"
      :before-close="handleClose">
      <span>您将要删除一个社团，确认继续操作吗？</span>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible3 = false">取 消</el-button>
    <el-button type="primary" @click="sureDeleteLevel()">确 定</el-button>
  </span>
    </el-dialog>
    <el-dialog
      title="创建活动"
      :visible.sync="dialogVisible2"
      width="80%"
      :before-close="handleClose">
      <span>
               <el-form ref="ruleForm" :rules="rules"  :model="form" label-width="80px" >
      <el-form-item label="活动照片">
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
      </el-form-item>
      <el-form-item label="活动名称" prop="name">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="活动时间" prop="date1">
        <el-col :span="11">
          <el-date-picker type="date" placeholder="选择日期" v-model="form.date1" style="width: 100%;"></el-date-picker>
        </el-col>
      </el-form-item>
      <el-form-item label="对外开放">
        <el-switch
          v-model=form.isopen
          active-text="是"
          inactive-text="否">
        </el-switch>
      </el-form-item>
      <el-form-item label="活动详情" prop="desc">
        <el-input type="textarea" v-model="form.desc"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit(form)">立即创建</el-button>
        <el-button @click="dialogVisible2 = false">取消</el-button>
      </el-form-item>
    </el-form>
      </span>
      <span slot="footer" class="dialog-footer">
  </span>
    </el-dialog>
  </el-card>
</template>

<script>
import axios from 'axios'
export default {
  name: 'myCommunity',
  data () {
    return {
      user: {},
      userID: 4,
      clubId: 0,
      total: 0,
      tableData2: [],
      dialogVisible2: false,
      dialogVisible3: false,

      imageUrl: '',
      returnimageUrl: '',
      returnisopen: 0,
      dialogVisible: true,
      form: {
        name: '',
        date1: '',
        isopen: false,
        desc: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入活动名称', trigger: 'blur' },
          { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
        ],
        date1: [
          { type: 'date', required: true, message: '请选择日期', trigger: 'change' }
        ],
        desc: [
          { required: true, message: '请填写活动详情', trigger: 'blur' }
        ]
      }
    }
  },
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
      this.userId = JSON.parse(window.localStorage.getItem('user')).userId
    }
    this.getmyActivities()
  },
  methods: {
    handleClose (done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done()
        })
        .catch(_ => {})
    },
    async getmyActivities () {
      let that = this
      await axios({
        method: 'get',
        url: 'http://150.158.47.16:521/club/findbyuserid',
        params: {
          user_id: that.userID
        }
      }).then(function (resp) {
        that.total = resp.data.data.length
        for (let i = 0; i < that.total; i++) {
          that.tableData2.push(resp.data.data[i].club)
          // eslint-disable-next-line eqeqeq
          if (resp.data.data[i].level == 0) {
            that.tableData2[i].tag = '成员'
            that.tableData2[i].isAble = true
            that.tableData2[i].isdeleteAble = true
            that.tableData2[i].iscreateAble = true
            that.tableData2[i].buttontype = 'primary'
          // eslint-disable-next-line eqeqeq
          } else if (resp.data.data[i].level == 1) {
            that.tableData2[i].tag = '管理员'
            that.tableData2[i].isAble = false
            that.tableData2[i].isdeleteAble = true
            that.tableData2[i].iscreateAble = false
            that.tableData2[i].buttontype = 'warning'
          // eslint-disable-next-line eqeqeq
          } else if (resp.data.data[i].level == 2) {
            that.tableData2[i].tag = '社长'
            that.tableData2[i].isAble = false
            that.tableData2[i].isdeleteAble = false
            that.tableData2[i].iscreateAble = false
            that.tableData2[i].buttontype = 'danger'
          }
          console.log(that.tableData2[i])
        }
        console.log(that.total)
      })
    },
    judgecreateLevel (clubId) {
      this.clubId = clubId
      console.log(clubId)
      this.dialogVisible2 = true
    },
    judgedeleteLevel (clubId) {
      this.dialogVisible3 = true
      this.clubId = clubId
    },
    sureDeleteLevel () {
      let that = this
      axios({
        method: 'post',
        url: 'http://150.158.47.16:521/club/deleteclub',
        params: {
          club_id: that.clubId
        }
      }).then(function (resp) {
        window.location.reload()
        console.log(resp.data)
      })
    },
    judgeLevel (clubId) {
      this.clubId = clubId
      this.$router.push({
        path: '/Home/my_communities/EditOrganization',
        query: {
          clubId: this.clubId
        }
      })
    },
    resetDateFilter () {
      this.$refs.filterTable.clearFilter('date')
    },
    clearFilter () {
      this.$refs.filterTable.clearFilter()
    },
    formatter (row, column) {
      return row.address
    },
    filterTag (value, row) {
      return row.tag === value
    },
    filterHandler (value, row, column) {
      const property = column['property']
      return row[property] === value
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
      console.log(that.returnimageUrl)
      console.log(that.form.desc)
      console.log(that.form.date1)
      console.log(that.clubId)
      console.log(that.form.name)
      console.log(that.returnisopen)

      await axios({
        method: 'post',
        url: 'http://150.158.47.16:521/activity/addactivity',
        params: {
          file: that.returnimageUrl,
          description: that.form.desc,
          isPublic: that.returnisopen,
          club_id: that.clubId,
          name: that.form.name,
          date: that.form.date1.toString()
        }
      }).then(function (resp) {
        successful = true
        console.log(resp.data)
      })
      if (successful) {
        this.$alert('创建成功', '恭喜你！', {
          confirmButtonText: '确定',
          callback: action => {
            window.location.reload()
          }
        })
      } else {
        console.log(successful)
        this.$alert('创建失败', '请重试！', {
          confirmButtonText: '确定',
          callback: action => {
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
