import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Home from '../views/Home.vue'
import RoomList from '../views/RoomList.vue'
import SysNotification from '../views/SysNotification.vue'
import MyReservations from '../views/MyReservations.vue'
import CreateReservation from '../views/CreateReservation.vue'
import TestPage from '../views/TestPage.vue'
import AdminInit from '../views/AdminInit.vue'
import { useUserStore } from '../stores/user'

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/home',
    name: 'Home',
    component: Home,
    meta: { requiresAuth: true }
  },
  {
    path: '/rooms',
    name: 'RoomList',
    component: RoomList,
    meta: { requiresAuth: true }
  },
  {
    path: '/my-reservations',
    name: 'MyReservations',
    component: MyReservations,
    meta: { requiresAuth: true }
  },
  {
    path: '/sys-notification',
    name: 'SysNotification',
    component: SysNotification,
    meta: { requiresAuth: true }
  },
  {
    path: '/create-reservation',
    name: 'CreateReservation',
    component: CreateReservation,
    meta: { requiresAuth: true }
  },
  {
    path: '/test',
    name: 'TestPage',
    component: TestPage
  },
  {
    path: '/admin-init',
    name: 'AdminInit',
    component: AdminInit
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