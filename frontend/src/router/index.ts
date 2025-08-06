import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

// 导入布局组件
// @ts-ignore
import Layout from '../components/layout/Layout.vue'
// @ts-ignore
import Checkout from "../views/checkout/Checkout.vue";

// 导入页面组件
// @ts-ignore
const Login = () => import('../views/user/Login.vue')
// @ts-ignore
const Register = () => import('../views/user/Register.vue')
// @ts-ignore
const Profile = () => import('../views/user/Profile.vue')
// @ts-ignore
const Home = () => import('../views/home/Home.vue')

// 路由配置
const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    component: Layout,
    children: [
      {
        path: '',
        redirect: '/home'
      },
      {
        path: 'home',
        name: 'Home',
        component: Home,
        meta: {
          title: '首页',
          requiresAuth: false
        }
      },
      {
        path: 'login',
        name: 'Login',
        component: Login,
        meta: {
          title: '登录',
          requiresAuth: false
        }
      },
      {
        path: 'register',
        name: 'Register',
        component: Register,
        meta: {
          title: '注册',
          requiresAuth: false
        }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: Profile,
        meta: {
          title: '个人资料',
          requiresAuth: true
        }
      },
      // 商品相关路由
      {
        path: '/product',
        name: 'Products',
        component: () => import('../views/product/ProductList.vue'),
        meta: { title: '商品列表' }
      },
      {
        path: '/product/:id',
        name: 'ProductDetail',
        component: () => import('../views/product/ProductDetail.vue'),
        meta: { title: '商品详情' },
        props: true
      },
      {
        path: 'cart',
        name: 'Cart',
        component: () => import('../views/cart/Cart.vue'),
        meta: {
          title: '购物车',
          requiresAuth: true
        }
      },
      {
        path: 'checkout',
        name: 'Checkout',
        component: Checkout,
        meta: {
          title: '订单结算',
          requiresAuth: true
        }
      },
      {
        path: 'repository',
        name: 'UserRepository',
        component: () => import('../views/repository/UserRepository.vue'),
        meta: {
          title: '我的商品库',
          requiresAuth: true
        }
      },
      {
        path: 'moments',
        name: 'PlayerMoment',
        component: () => import('../views/moment/PlayerMomentView.vue'),
        meta: {
          title: '玩家秀',
          requiresAuth: false
        }
      }
    ]
  },
  // 捕获所有未匹配路由
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  },
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局前置守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 土豆商城` : '土豆商城'
  
  // 需要登录权限的路由验证
  if (to.meta.requiresAuth) {
    const token = sessionStorage.getItem('token')
    if (token) {
      next()
    } else {
      next({ path: '/login' })
    }
  } else {
    next()
  }
})

export default router