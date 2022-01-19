/* eslint-disable vue/valid-v-model */
<template>
  <div>
    <el-card>
<!--      搜索与添加-->
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input
            placeholder="请输入名字" v-model="queryInfo.query">
            <el-button slot="append" icon="el-icon-search" @click="searchStudent"></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-popover placement="bottom" :width="1000" trigger="click">
            <template #reference>
              <el-button type="primary" >查看申请</el-button>
            </template>
            <el-table :data="applyData" border stripe>
              <el-table-column label="头像" prop="headPortrait">
                <template slot-scope="scope">
                  <img :src="scope.row.headPortrait" width="40" height="40" class="head_pic"/>
                  </template>
              </el-table-column>
              <el-table-column label="姓名" prop="name"></el-table-column>
              <el-table-column label="e-mail" prop="eMail"></el-table-column>
              <el-table-column label="操作" width="180px">
                <template slot-scope="scope">
                  <el-tooltip class="item" effect="dark" content="同意" placement="top">
                    <el-button type="success" icon="el-icon-check" @click="agree(scope.row.userId,true)"></el-button>
                  </el-tooltip>
                  <el-tooltip class="item" effect="dark" content="拒绝" placement="top">
                    <el-button type="danger" icon="el-icon-close" @click="agree(scope.row.userId,false)"></el-button>
                  </el-tooltip>
                </template>
              </el-table-column>
            </el-table>
          </el-popover>

        </el-col>
      </el-row>
<!--      用户列表-->
      <el-table :data="studentList" border stripe>
        <el-table-column type="index"></el-table-column>
        <el-table-column label="头像" prop="headPortrait">
          <template slot-scope="scope">
          <img :src="scope.row.headPortrait" width="40" height="40" class="head_pic"/>
          </template>
        </el-table-column>
        <el-table-column label="角色" prop="level"></el-table-column>
        <el-table-column label="姓名" prop="name"></el-table-column>
        <el-table-column label="e-mail" prop="eMail"></el-table-column>
        <el-table-column label="操作" width="180px">
          <template slot-scope="scope">
            <el-tooltip class="item" effect="dark" content="编辑职位" placement="top">
            <el-button type="primary" icon="el-icon-edit" @click="showEditDialog(scope.row.userId,scope.row.level)"></el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="踢出成员" placement="top">
            <el-button type="danger" icon="el-icon-delete" @click="deleteDialog(scope.row.userId)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model: currentPage="queryInfo.pagenum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="queryInfo.pagesize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      >
      </el-pagination>
    </el-card>
<!--    编辑职位对话框-->
    <el-dialog
      :visible.sync="editDialogVisible"
      title="编辑职位"
      width="50%"
    >
      <span>
        <el-select v-model="options.value" placeholder="选择职位">
    <el-option
      v-for="item in options"
      :key="item.value"
      :label="item.label"
      :value="item.value"
      :disabled="item.disabled"
    >
    </el-option>
  </el-select>
      </span>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancelEdit()">取消</el-button>
        <el-button type="primary" @click="sureEdit(options.value)"
        >确认</el-button
        >
      </span>
      </template>
    </el-dialog>
<!--    删除成员对话框-->
    <el-dialog :visible.sync="deleteDialogVisible" title="删除成员" width="50%" center>
    <span
    >确认要删除这个成员吗？</span
    >
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="sureDelete()"
        >确认</el-button
        >
      </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'EditOrganization',
  data () {
    return {
      localuser: {},
      deleteMemberId: 10,
      options: [
        {
          value: '0',
          label: '普通成员',
          disabled: false
        },
        {
          value: '1',
          label: '管理员',
          disabled: false
        },
        {
          value: '2',
          label: '社长',
          disabled: false
        }
      ],
      deleteDialogVisible: false,
      editDialogVisible: false,
      ifsearch: 0,
      queryInfo: {
        query: '',
        pagenum: 1,
        pagesize: 2
      },
      applyData: [],
      studentList: [],
      Level: [],
      total: 0,
      editForm: 0,
      editUserId: 0,
      clubId: ''
    }
  },
  created () {
    this.localuser = JSON.parse(window.localStorage.getItem('user'))
    //   console.log('读出来', this.user)
    // } else {
    // console.log('存咯')
    // this.user = JSON.parse(this.$route.query.user)
    // window.localStorage.setItem('user', JSON.stringify(this.user))
    // }
    if (this.localuser == null) {
      console.log('跳转')
      this.$router.push({
        path: '/error',
        query: {
        }
      })
    }
    this.clubId = this.$route.query.clubId
    this.getClubFindMember()
    this.getApplyMember()
  },
  methods: {
    agree (userId, isagree) {
      axios({
        method: 'post',
        url: 'http://150.158.47.16:521/club/deleteapplication',
        params: {
          user_id: userId,
          club_id: this.clubId,
          is_success: isagree
        }
      }).then(function (resp) {
        console.log(resp.data)
        window.location.reload()
      })
    },
    cancelEdit () {
      this.editDialogVisible = false
      for (let i = 0; i < 3; i++) {
        this.options[i].disabled = false
      }
    },
    sureEdit (ifsure) {
      // eslint-disable-next-line eqeqeq
      if (ifsure == undefined) {
        this.editDialogVisible = false
        for (let i = 0; i < 3; i++) {
          this.options[i].disabled = false
        }
        return
      }
      console.log(ifsure)
      let that = this
      console.log(that.deleteMemberId)
      axios({
        method: 'post',
        url: 'http://150.158.47.16:521/club/changemember',
        params: {
          user_id: that.editUserId,
          level: ifsure,
          club_id: that.clubId
        }
      }).then(function (resp) {

      })

      this.editDialogVisible = false
      for (let i = 0; i < 3; i++) {
        this.options[i].disabled = false
      }
      window.location.reload()
      this.studentList.splice(0, this.studentList.length)
      this.getClubFindMember()
    },
    sureDelete () {
      let that = this
      console.log(that.deleteMemberId)
      axios({
        method: 'post',
        url: 'http://150.158.47.16:521/club/deletemember',
        params: {
          user_id: that.deleteMemberId,
          club_id: that.clubId
        }
      }).then(function (resp) {

      })
      this.studentList.splice(0, this.studentList.length)
      this.getClubFindMember()
      window.location.reload()
      this.deleteDialogVisible = false
    },
    deleteDialog (userId) {
      this.deleteMemberId = userId
      this.deleteDialogVisible = true
    },
    showEditDialog (userId, level) {
      console.log(userId + level)
      let that = this
      axios({
        method: 'get',
        url: 'http://150.158.47.16:521/club/finduserlevel',
        params: {
          user_id: userId,
          club_id: that.clubId
        }
      }).then(function (resp) {
        console.log(resp.data)
        that.editForm = resp.data.data
        that.editUserId = userId
        // eslint-disable-next-line eqeqeq
        if (that.editForm == 0) {
          that.options[0].disabled = true
        // eslint-disable-next-line eqeqeq
        } else if (that.editForm == 1) {
          that.options[1].disabled = true
        // eslint-disable-next-line eqeqeq
        } else if (that.editForm == 2) {
          that.options[2].disabled = true
        }
      })
      that.editDialogVisible = true
    },
    searchStudent () {
      // eslint-disable-next-line eqeqeq
      if (this.queryInfo.query == '') {
        // eslint-disable-next-line eqeqeq
        if (this.ifsearch == 1) {
          this.ifsearch = 0
          this.studentList.splice(0, this.studentList.length)
          this.getClubFindMember()
        } else {

        }
      } else {
        this.ifsearch = 1
        for (let i = 0; i < this.studentList.length; i++) {
          console.log(this.queryInfo.query)
          console.log(this.studentList[i].name)
          // eslint-disable-next-line eqeqeq
          if (this.queryInfo.query != this.studentList[i].name) {
            this.studentList.splice(i, 1)
          }
        }
        console.log(this.studentList)
      }
    },
    handleSizeChange (newSize) {
      console.log(newSize)
      this.queryInfo.pagesize = newSize
    },
    handleCurrentChange (newPage) {
      console.log(newPage)
      this.queryInfo.pagenum = newPage
    },
    getMemberType (a) {
      if (a === 0) {
        return '普通成员'
      } else if (a === 1) {
        return '管理员'
      } else {
        return '社长'
      }
    },
    getApplyMember () {
      let that = this
      axios({
        method: 'get',
        url: 'http://150.158.47.16:521/club/findapplication',
        params: {
          club_id: that.clubId
        }
      }).then(function (resp) {
        console.log(resp.data.data[0])
        let total = resp.data.data.length
        for (let i = 0; i < total; i++) {
          that.applyData.push(resp.data.data[i])
        }
      })
    },
    getClubFindMember () {
      let that = this
      axios({
        method: 'get',
        url: 'http://150.158.47.16:521/club/findmember',
        params: {
          club_id: that.clubId
        }
      }).then(function (resp) {
        console.log(resp.data.data[0])
        that.total = resp.data.data.length
        for (let i = 0; i < resp.data.data.length; i++) {
          resp.data.data[i].user.level = that.getMemberType(resp.data.data[i].level)
          that.studentList.push(resp.data.data[i].user)
        }
      })
    }
  }
}
</script>

<style scoped>
.el-pagination{
  margin-top: 20px;
}
.el-table{
  margin-top: 15px;
  font-size: 12px;
}
</style>
