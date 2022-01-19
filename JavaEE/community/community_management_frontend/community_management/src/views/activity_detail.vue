<template>
<el-container style="margin-left:40px;margin-right:40px;margin-top:20px;margin-bottom:20px">
    <el-main style="overflow-y:hidden; box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);border-radius: 25px;background-image:url(https://img2.baidu.com/it/u=2886976471,2427061506&fm=26&fmt=auto)" >
    <p align=center><font style="font-size: 40pt; filter: shadow(color=black); width: 71.27%; color: #2F0000; line-height: 150%; font-family: 华文楷体; height: 60px"><b>{{this.activity.name}}</b></font></p>
    <h2 style="font-family:楷体">社团：{{club.name}}</h2>
    <img :src= activity.picture style=" box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);border-radius: 25px;width:60%;height:60%;align:center;">
    <div style="padding:10px"><h3 style="font-family:楷体">当前参与人数：{{activity.personNumber}}  <el-button v-if="ifinactivity()==false" style="margin-left:60px" round size="mini" @click="tryjoin()">报名参加</el-button></h3>
</div>
    <div>{{this.activity.description}}</div>
    </el-main>
</el-container>
</template>

<script>
// eslint-disable-next-line no-undef
import axios from 'axios'
export default {
  mounted: function () {
    this.activity = JSON.parse(this.$route.query.activity)
    console.log(this.activity.activityId)
    // this.user = JSON.parse(this.$route.query.user)
    this.user = JSON.parse(window.localStorage.getItem('user'))
    console.log(this.user)
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
    this.getClub(this.activity)
    if ((this.user == null) || (typeof this.user.userId === 'undefined')) {
      this.$router.push({
        path: '/error',
        query: {
        }
      })
    }
  },
  methods: {
    getClub (activity) {
      var ul = 'http://150.158.47.16:521/activity/getclub'
      let that = this
      axios({
        method: 'get',
        url: ul,
        params: {
          activity_id: activity.activityId
        }
      }).then((response) => {
        that.club = response.data.data
        console.log(this.club)
      }).catch(Error)
    },
    ifinactivity () {
      console.log(this.user.userId)
      console.log(this.activity.activityId)
      axios({
        method: 'get',
        url: 'http://150.158.47.16:521/activity/isinactivity',
        params: {
          user_id: this.user.userId,
          activity_id: this.activity.activityId
        }
      }).then((response) => {
        this.ifinac = response.data.data
      }).catch(Error)
      return this.ifinac
    },
    ifjoin () {
      // eslint-disable-next-line eqeqeq
      if (this.activity.isPublic == 1) { return true } else {
        axios({
          method: 'get',
          url: 'http://150.158.47.16:521/activity/isinclub',
          params: {
            user_id: this.user.userId,
            activity_id: this.activity.activityId
          }
        }).then((response) => {
          this.ifinclub = response.data.data
        }).catch(Error)
        // eslint-disable-next-line eqeqeq
        if (this.ifinclub == false) { return false } else return true
      }
    },
    tryjoin () {
      console.log(this.user.userId)
      console.log(this.activity.activityId)
      let tr = this.ifjoin()
      // eslint-disable-next-line eqeqeq
      if (tr == true) {
        this.$message({
          message: '您已成功加入活动',
          type: 'success'
        })
        axios({
          method: 'post',
          url: 'http://150.158.47.16:521/activity/participate',
          params: {
            user_id: this.user.userId,
            activity_id: this.activity.activityId
          }
        }).then((response) => {
          let data = response.data
          console.log(data)
        }).catch(Error)
      } else {
        this.$message({
          message: '本活动为社团内部活动，请先加入相应社团',
          type: 'warning'
        })
      }
    }
  },
  data () {
    return {
      ul: 'http://150.158.47.16:8888/down/ILDLD31E2oci',
      activity: {},
      club: '',
      ifinclub: false,
      user: {},
      ifinac: false
    }
  }
}
</script>
