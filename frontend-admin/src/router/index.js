import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Layout from '../views/Layout.vue'
import Dashboard from '../views/Dashboard.vue'
import RoomManagement from '../views/RoomManagement.vue'
import ReservationManagement from '../views/ReservationManagement.vue'
import UserManagement from '../views/UserManagement.vue'
import { useUserStore } from '../stores/user'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    component: Layout,
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: Dashboard
      },
      {
        path: 'rooms',
        name: 'RoomManagement',
        component: RoomManagement
      },
      {
        path: 'reservations',
        name: 'ReservationManagement',
        component: ReservationManagement
      },
      {
        path: 'users',
        name: 'UserManagement',
        component: UserManagement
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    //next('/login')
    // 关键修改：把目标路径作为 query 参数传给登录页
    // 注意使用 to.fullPath 可以保留后面的 query 参数和 hash
    //此时，浏览器地址栏会变成：/login?redirect=/my-reservations
    next({ path: '/login', query: { redirect: to.fullPath } })
  } else {
    next()
  }
})

export default router