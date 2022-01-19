<template>
<div>
<div v-for=" activity in this.activities" :key=activity.id style="float:left;width:21%;height:28%;margin:2%">
    <el-card :body-style="{ padding: '0px' }" span="8" shadow="hover" style=" box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);border-radius: 20px">
      <img :src= activity.picture class="image" style="width:100%;height:300px">
      <div style="padding: 14px;">
        <span>{{activity.name}}</span>
        <el-col class="bottom" align="left">
          <time class="time" align="left">{{ activity.date }}</time>
            <el-button type="text" class="button" align="right" @click="gotoDetail(activity)">查看详情</el-button>
        </el-col>
        </div>
    </el-card>
</div>
</div>

</template>
<script>
import axios from 'axios'
export default {
  mounted () {
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
    this.getAllActivites()
  },
  data () {
    return {
      user: {},
      activities: []
    }
  },
  methods: {
    seeuser () {
      console.log(this.user)
    },
    gotoDetail (activity) {
      console.log(activity)
      console.log(this.user)
      this.$router.push({
        path: '/Home/activities/Detail',
        query: {
          activity: JSON.stringify(activity)
          // user: JSON.stringify(this.user)
        }
      })
    },
    getAllActivites () {
      var url = 'http://150.158.47.16:521/activity/all'
      let that = this
      axios.get(url).then((response) => {
        that.activities = response.data.data
        console.log(this.activities[0].picture)
      }).catch(Error)
    }
  }
}
</script>
<style>
  .time {
    font-size: 13px;
    color: #999;
  }
  .bottom {
    margin-bottom: 12px;
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    width: 100%;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
      display: table;
      content: "";
  }
  .clearfix:after {
      clear: both
  }
</style>
