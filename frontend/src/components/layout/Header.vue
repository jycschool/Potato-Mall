<template>
  <el-header class="header">
    <div class="header-container">
      <div class="logo-section" @click="goToHome">
        <el-icon class="logo-icon"><ShoppingBag /></el-icon>
        <h1 class="logo-text">谜匣盲盒商城</h1>
      </div>

      <div class="nav-section">
        <el-menu
            mode="horizontal"
            :ellipsis="false"
            :router="true"
            class="nav-menu"
        >
          <el-menu-item index="/home">首页</el-menu-item>
          <el-menu-item index="/product">商品</el-menu-item>
          <el-menu-item index="/moments">玩家秀</el-menu-item>
        </el-menu>
      </div>

      <div class="action-section">
        <template v-if="isLoggedIn">
          <el-button type="text" class="icon-button cart-button" @click="goToCart">
            <el-badge :value="cartCount" :max="99" class="cart-badge">
              <el-icon :size="22"><ShoppingCart /></el-icon>
            </el-badge>
          </el-button>

          <el-button type="text" class="icon-button repository-button" @click="goToRepository">
            <el-icon :size="22"><Box /></el-icon>
          </el-button>

          <el-dropdown trigger="click">
            <span class="user-dropdown">
              {{ username }}
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="goToProfile">个人资料</el-dropdown-item>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>

        <template v-else>
          <el-button type="text" @click="goToLogin">登录</el-button>
          <el-button type="primary" @click="goToRegister">注册</el-button>
        </template>
      </div>
    </div>

    <!-- 热门盲盒展示区 -->
    <div class="hot-products-container" v-if="hotProducts.length > 0">
      <div class="hot-products-header">
        <h3>热门盲盒</h3>
        <el-button type="text" @click="goToProduct">查看更多</el-button>
      </div>
      <div class="hot-products-list">
        <el-card
          v-for="product in hotProducts"
          :key="product.id"
          class="hot-product-item"
          shadow="hover"
          @click="viewProductDetail(product.id)"
        >
          <img :src="product.images[0]" class="hot-product-image" />
          <div class="hot-product-info">
            <h4 class="hot-product-name">{{ product.name }}</h4>
            <div class="hot-product-price">¥{{ product.price }}</div>
          </div>
        </el-card>
      </div>
    </div>
  </el-header>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ShoppingBag, ShoppingCart, ArrowDown, Box } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const username = ref('')
const cartCount = ref(0)
const userInfo = ref(null)
const hotProducts = ref([])

const isLoggedIn = computed(() => {
  return username.value !== ''
})

onMounted(() => {
  // 从sessionStorage获取用户名
  const storedUsername = sessionStorage.getItem('username')
  if (storedUsername) {
    username.value = storedUsername
    fetchUserInfo() // 获取用户信息
    fetchCartCount() // 获取购物车数量
  }

  // 获取热门盲盒数据
  fetchHotProducts()
})

// 监听路由变化，更新数据
watch(() => router.currentRoute.value.path, () => {
  if (sessionStorage.getItem('username')) {
    fetchUserInfo()
    fetchCartCount()
  }
})

import { getUserInfo } from '../../api/user'
import { getCartItemsCount } from '../../api/cart'
import { getHotProducts } from '../../api/product'

// 从数据库获取用户信息
const fetchUserInfo = async () => {
  try {
    const storedUsername = sessionStorage.getItem('username')
    if (storedUsername) {
      const response = await getUserInfo(storedUsername)
      if (response.data && response.data.code === '200') {
        userInfo.value = response.data.data
        // 确保使用数据库中的用户名
        username.value = response.data.data.username
      }
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

// 获取购物车数量
const fetchCartCount = async () => {
  try {
    const storedUsername = sessionStorage.getItem('username')
    if (storedUsername) {
      const response = await getCartItemsCount(storedUsername)
      if (response.data && response.data.code === '200') {
        cartCount.value = response.data.data || 0
      }
    }
  } catch (error) {
    console.error('获取购物车数量失败:', error)
    cartCount.value = 0
  }
}

// 获取热门盲盒数据
const fetchHotProducts = async () => {
  try {
    const response = await getHotProducts(4) // 获取4个热门盲盒
    if (response.data && response.data.code === '200') {
      hotProducts.value = response.data.data || []
    }
  } catch (error) {
    console.error('获取热门盲盒失败:', error)
    hotProducts.value = []
  }
}

// 查看商品详情
const viewProductDetail = (productId) => {
  router.push(`/product/${productId}`)
}

// 导航方法
const goToHome = () => {
  router.push('/home')
}

const goToCart = () => {
  router.push('/cart')
  // 导航到购物车页面后更新购物车数量
  fetchCartCount()
}

const goToRepository = () => {
  router.push('/repository')
}

const goToProfile = () => {
  router.push('/profile')
}

const goToLogin = () => {
  router.push('/login')
}

const goToRegister = () => {
  router.push('/register')
}

const goToProduct = () => {
  router.push('/product')
}

// 退出登录
const handleLogout = () => {
  // 清除会话存储中的用户数据
  sessionStorage.removeItem('username')
  sessionStorage.removeItem('token')
  username.value = ''
  userInfo.value = null
  ElMessage.success('退出登录成功')
  router.push('/home')
}
</script>

<style scoped>
.header {
  background-color: white;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
  padding: 0;
  flex-direction: column; /* 允许热门盲盒区域垂直排列 */
  height: auto; /* 自适应高度 */
}

.header-container {
  max-width: 1200px;
  margin: 0 auto;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.logo-section {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.logo-icon {
  font-size: 28px;
  color: #e78c3c;
  margin-right: 8px;
}

.logo-text {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.nav-section {
  flex: 1;
  display: flex;
  justify-content: center;
}

.nav-menu {
  border-bottom: none;
}

.action-section {
  display: flex;
  align-items: center;
  gap: 16px;
}

.cart-button {
  padding: 8px;
  margin-right: 4px;  /* 添加右边距，使购物车向右移动 */
}

.cart-badge {
  margin-top: -2px;
}

.repository-button {
  padding: 8px;
}

.user-dropdown {
  cursor: pointer;
  display: flex;
  align-items: center;
  color: #333;
}

.user-dropdown .el-icon {
  margin-left: 4px;
}

.icon-button {
  padding: 8px;
}

/* 热门盲盒样式 */
.hot-products-container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 10px 20px;
  background-color: #f8f8f8;
}

.hot-products-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.hot-products-header h3 {
  margin: 0;
  color: #333;
  font-size: 16px;
}

.hot-products-list {
  display: flex;
  gap: 16px;
  overflow-x: auto;
  padding-bottom: 10px;
}

.hot-product-item {
  width: 180px;
  flex-shrink: 0;
  cursor: pointer;
  transition: transform 0.2s;
}

.hot-product-item:hover {
  transform: translateY(-5px);
}

.hot-product-image {
  width: 100%;
  height: 120px;
  object-fit: cover;
  border-radius: 4px;
}

.hot-product-info {
  margin-top: 8px;
}

.hot-product-name {
  margin: 5px 0;
  font-size: 14px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.hot-product-price {
  color: #e78c3c;
  font-size: 16px;
  font-weight: bold;
}

@media (max-width: 768px) {
  .logo-text {
    display: none;
  }

  .nav-menu {
    font-size: 14px;
  }

  .hot-product-item {
    width: 140px;
  }

  .hot-product-image {
    height: 100px;
  }
}
</style>