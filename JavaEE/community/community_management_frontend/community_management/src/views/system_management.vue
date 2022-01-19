<template>
<div>
<el-container style=" box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);border-radius: 20px;margin-left:8%;margin-right:8%;margin-top:10px;margin-bottom:15px">
  <el-dialog
  :title= "JSON.parse(JSON.stringify(this.dialogData)).name"
  :visible.sync="centerDialogVisible"
  width="30%"
  center>
<img :src= JSON.parse(JSON.stringify(this.dialogData)).logo style="width:50%;margin-left:25%;margin-right:25%">
<div style="text-align:center;padding:20px"><font style="margin-right:50px">成员数：{{JSON.parse(JSON.stringify(this.dialogData)).memberNumber}}</font><font style="margin-left:50px">会费：{{JSON.parse(JSON.stringify(this.dialogData)).expenditure}}</font></div>
<div style="text-align:center;"><font>介绍</font></div>
<div style="text-align:center;">{{JSON.parse(JSON.stringify(this.dialogData)).description}}</div>
<el-button v-if="judgeisinclub() == false" style="width:100%" type="success" plain @click="tryjoinclub()">加入社团</el-button>
</el-dialog>
  <el-table
    :data="tableData.filter(data => !search || data.name.toLowerCase().includes(search.toLowerCase()))"
    style="width: 100%">
    <el-table-column style="width:20%"
      label=""
      prop="logo">
      <template slot="header" slot-scope="scope">
        <el-button size="mini" icon="el-icon-refresh" circle @click="Resort()"></el-button>
      </template>
      <template slot-scope="scope">
      <img :src="scope.row.logo" style="width:50px;heght:50px;align-text:center">
      </template>
    </el-table-column>
    <el-table-column style="width:20%"
      label="社团名称"
      prop="name">
    </el-table-column>
    <el-table-column style="width:20%"
    label="会费"
    prop="expenditure">
    </el-table-column>
    <el-table-column style="width:20%"
    label="现有成员"
    prop="memberNumber">
    </el-table-column>
    <el-table-column style="width:20%"
      align="right"
      prop="clubId">
      <template slot="header" slot-scope="scope">
        <el-input
          v-model="search"
          size="mini"
          placeholder="输入名称搜索"/>
      </template>
      <template slot-scope="scope">
        <el-button
          size="mini"
          @click="seeClubInfo(scope.row.clubId)">详细信息</el-button>
          <el-button size="mini" type="danger" plain @click="open(scope.row.clubId)">删除社团</el-button>
      </template>
    </el-table-column>
  </el-table>
  </el-container>
</div>
</template>

<script>
import axios from 'axios'
export default {
  // props: ['user'],
  mounted () {
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
    this.getTableData()
    // if ((this.user == null) || (typeof this.user.userId === 'undefined')) {
    //   this.user = JSON.parse(localStorage.getItem('user'))
    // } else {
    //   window.localStorage.setItem('user', JSON.parse(this.user))
    // }
    // if ((this.user == null) || (typeof this.user.userId === 'undefined')) {
    //   this.$router.push({
    //     path: '/error',
    //     query: {
    //     }
    //   })
    // }
    console.log('user', this.user)
  },
  data () {
    return {
      user: {},
      dialogData: {},
      tableData: [],
      search: '',
      centerDialogVisible: false,
      ifinclub: false
    }
  },
  methods: {
    getTableData () {
      var url = 'http://150.158.47.16:521/club/all'
      let that = this
      axios.get(url).then((response) => {
        that.tableData = response.data.data
      }).catch(Error)
    },
    judgeisinclub () {
      console.log('lhb', JSON.parse(JSON.stringify(this.user)).userId)
      console.log('lhb', JSON.parse(JSON.stringify(this.dialogData)).clubId)
      axios({
        method: 'get',
        url: 'http://150.158.47.16:521/club/isinclub',
        params: {
          user_id: JSON.parse(JSON.stringify(this.user)).userId,
          club_id: JSON.parse(JSON.stringify(this.dialogData)).clubId
        }
      }).then((response) => {
        console.log(response.data.data)
        this.ifinclub = response.data.data
      }).catch(Error)
      console.log(this.ifinclub)
      return this.ifinclub
    },
    Resort () {
      let len = this.tableData.length
      for (var i = 0; i < len; i++) {
        var a = parseInt(Math.random() * len)
        var temp = this.tableData[a]
        this.$set(this.tableData, a, this.tableData[i])
        // this.tableData[a] = this.tableData[i]
        this.$set(this.tableData, i, temp)
        // this.tableData[i] = temp
      }
      // eslint-disable-next-line no-undef
      console.log(this.tableData)
    },
    seeClubInfo (clubId) {
      this.findClub(clubId)
      this.centerDialogVisible = true
    },
    tryjoinclub () {
      console.log(this.user.userId)
      console.log(this.dialogData.clubId)
      axios({
        method: 'post',
        url: 'http://150.158.47.16:521/club/application',
        params: {
          user_id: this.user.userId,
          club_id: this.dialogData.clubId
        }
      }).then((response) => {
        let data = response.data
        console.log(data)
      }).catch(Error)
      this.$message({
        message: '您的请求已发送',
        type: 'success'
      })
    },
    findClub (clubId) {
      console.log(clubId)
      console.log(this.tableData)
      for (var i = 0; i < this.tableData.length; i++) {
        // eslint-disable-next-line eqeqeq
        if (clubId == this.tableData[i].clubId) {
          this.dialogData = this.tableData[i]
          console.log('找到咯')
          console.log(this.dialogData)
          console.log(this.tableData[i])
          break
        }
      }
      console.log('sss', this.dialogData)
    },
    open (clubid) {
      this.$confirm('此操作将永久删除该社团, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios({
          method: 'post',
          url: 'http://150.158.47.16:521/club/deleteclub',
          params: {
            club_id: clubid
          }
        }).then((response) => {
          console.log(response.data)
        }).catch(Error)
        this.$message({
          type: 'success',
          message: '删除成功!'
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    }
  }
}
</script>

<style scoped>
/*最外层透明*/
.el-table, .el-table__expanded-cell{
    background-color: transparent;
}
/* 表格内背景颜色 */
.el-table th,
.el-table tr,
.el-table td {
    background-color: transparent;
}
/* 删除表格下最底层的横线 */
.el-table::before {
  left: 0;
  bottom: 0;
  width: 100%;
  height: 0px;
}
/* 表格表头字体颜色 */
.el-table thead {
  color: #ffffff;
  font-weight: 500;
  background-color: transparent;
}

</style>
