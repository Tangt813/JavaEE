import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)
export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      redirect: '/Login'
    },
    {
      path: '/Login',
      name: 'login',
      component: () => import('../views/Login_Signup.vue')
    },
    {
      path: '/forgetpassword',
      name: 'forget',
      component: () => import('../views/forgetpassword.vue')
    },
    {
      path: '/error',
      name: 'error',
      component: () => import('../views/error.vue')
    },
    {
      path: '/Home',
      name: 'Home',
      component: () => import('../components/NavTop.vue'), // 这里是核心： center作为容器组件，包含左右菜单和右侧的router-view，所以下面的子组件实际是显示在router-view
      redirect: '/Home/activities', // 输入路由center会重定向到a页面
      children:
        [
          {
            path: '/Home/activities',
            name: 'activities',
            component: () => import('../views/activities.vue')
          },
          {
            path: '/Home/user/changepassword',
            name: 'changepassword',
            component: () => import('../views/changepassword.vue')
          },
          {
            path: '/Home/community_center',
            name: 'community_center',
            component: () => import('../views/community_center.vue')
          },
          {
            path: '/Home/system_management',
            name: 'system_management',
            component: () => import('../views/system_management.vue')
          },
          {
            path: '/Home/my_communities',
            name: 'my_communities',
            component: () => import('../components/leftnav.vue'),
            redirect: '/Home/my_communities/my_activities',
            children:
            [
              {
                path: '/Home/my_communities/my_activities',
                name: 'my_activities',
                component: () => import('../views/my_activities.vue')

              },
              {
                path: '/Home/my_communities/add_organization',
                name: 'add_organization',
                component: () => import('../views/add_organization.vue')
              },
              {
                path: '/Home/my_communities/management',
                name: 'management',
                component: () => import('../views/mycommunitiesmanagement.vue')
              }
            ]

          },
          {
            path: '/Home/my_communities/EditOrganization',
            name: 'EditOrganization',
            component: () => import('../views/EditOrganization.vue')
          },
          {
            path: '/Home/activities/Detail',
            name: 'activities_detail',
            component: () => import('../views/activity_detail.vue')

          }
        ]
    }]})
