<template>
<div style="height:100vh">
  <div style="height:100%;float:left;width:10%">
    <el-menu
      :default-active="$route.path"
      class="left-menu"
      @open="handleOpen"
      @close="handleClose"
      background-color="#000000"
      text-color="#fff"
      active-text-color="#00BFFF"
      router>
      <el-menu-item index='/Home/my_communities/my_activities'>
        <i></i>
        <span>我的活动</span>
      </el-menu-item>
            <el-menu-item index='/Home/my_communities/add_organization'>
        <i></i>
        <span slot="title">社团创建</span>
      </el-menu-item>
                  <el-menu-item index='/Home/my_communities/management'>
        <i></i>
        <span slot="title">社团管理</span>
      </el-menu-item>
    </el-menu>
  </div>
  <div style="height:100%;width:80%;margin-left:15%">
<router-view></router-view>
</div>

</div>
</template>
<script>
export default {
  // props: ['user'],
  data () {
    return {
      user: {},
      menuHeight: {
        height: ''
      }
    }
  },
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
    console.log(this.user)
    var docHeight = document.body.scrollHeight
    this.menuHeight.height = docHeight - 20 + 'px'
    console.log(this.menuHeight.height, 'this.containerHeight.height')
  },
  methods: {
    handleOpen (key, keyPath) {
      console.log(key, keyPath)
    },
    handleClose (key, keyPath) {
      console.log(key, keyPath)
    }
  }
}
</script>
<style scoped>
.left-menu{
    height: 100vh;
  }
</style>
