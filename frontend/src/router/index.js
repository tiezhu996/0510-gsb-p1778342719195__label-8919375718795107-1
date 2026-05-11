import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/home/Index.vue'),
    meta: { tabBar: true }
  },
  {
    path: '/category',
    name: 'Category',
    component: () => import('@/views/category/Index.vue'),
    meta: { tabBar: true }
  },
  {
    path: '/category/:id',
    name: 'CategoryDetail',
    component: () => import('@/views/category/Index.vue')
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('@/views/search/Index.vue')
  },
  {
    path: '/cart',
    name: 'Cart',
    component: () => import('@/views/cart/Index.vue'),
    meta: { tabBar: true, auth: true }
  },
  {
    path: '/order/confirm',
    name: 'OrderConfirm',
    component: () => import('@/views/order/Confirm.vue'),
    meta: { auth: true }
  },
  {
    path: '/order/list',
    name: 'OrderList',
    component: () => import('@/views/order/List.vue'),
    meta: { auth: true }
  },
  {
    path: '/order/detail/:id',
    name: 'OrderDetail',
    component: () => import('@/views/order/Detail.vue'),
    meta: { auth: true }
  },
  {
    path: '/fruit/:id',
    name: 'FruitDetail',
    component: () => import('@/views/fruit/Detail.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/user/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/user/Register.vue')
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/user/Profile.vue'),
    meta: { tabBar: true, auth: true }
  },
  {
    path: '/address',
    name: 'Address',
    component: () => import('@/views/user/Address.vue'),
    meta: { auth: true }
  },
  {
    path: '/password',
    name: 'Password',
    component: () => import('@/views/user/Password.vue'),
    meta: { auth: true }
  },
  {
    path: '/agreement',
    name: 'Agreement',
    component: () => import('@/views/user/Agreement.vue')
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('@/views/user/About.vue')
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/error/404.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  if (to.meta.auth && !userStore.isLoggedIn) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
  } else {
    next()
  }
})

export default router
