<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCartList, updateCartItem, removeCartItem, clearCart } from '../../api/cart'
import { Delete, ShoppingCart } from '@element-plus/icons-vue'

const router = useRouter()
const cartItems = ref<any[]>([])
const loading = ref(true)

// 初始化
onMounted(() => {
  // 检查登录状态
  const token = sessionStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  // 加载购物车数据
  loadCartData()
})

// 加载购物车数据
const loadCartData = async () => {
  loading.value = true
  try {
    const response = await getCartList()
    if (response.data && response.data.code === '200') {
      cartItems.value = response.data.data || []
    } else {
      ElMessage.error('获取购物车数据失败')
    }
  } catch (error) {
    console.error('获取购物车错误:', error)
    ElMessage.error('获取购物车数据失败')
  } finally {
    loading.value = false
  }
}

// 更新商品数量
const updateQuantity = async (cartItem: any, quantity: number) => {
  if (quantity < 1) {
    ElMessage.warning('商品数量不能小于1')
    return
  }

  if (quantity > cartItem.product.stock) {
    ElMessage.warning('商品库存不足')
    return
  }

  try {
    const response = await updateCartItem(cartItem.id, quantity)
    if (response.data && response.data.code === '200') {
      cartItem.quantity = quantity
      ElMessage.success('更新成功')
    } else {
      ElMessage.error(response.data?.msg || '更新失败')
    }
  } catch (error) {
    console.error('更新商品数量错误:', error)
    ElMessage.error('更新商品数量失败')
  }
}

// 从购物车移除商品
const removeFromCart = async (id: number) => {
  try {
    const response = await removeCartItem(id)
    if (response.data && response.data.code === '200') {
      cartItems.value = cartItems.value.filter(item => item.id !== id)
      ElMessage.success('已从购物车移除')
    } else {
      ElMessage.error(response.data?.msg || '移除失败')
    }
  } catch (error) {
    console.error('移除购物车商品错误:', error)
    ElMessage.error('移除购物车商品失败')
  }
}

// 清空购物车
const clearCartHandler = () => {
  if (cartItems.value.length === 0) {
    ElMessage.info('购物车已经为空')
    return
  }

  ElMessageBox.confirm(
      '确定要清空购物车吗？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  ).then(async () => {
    try {
      const response = await clearCart()
      if (response.data && response.data.code === '200') {
        cartItems.value = []
        ElMessage.success('购物车已清空')
      } else {
        ElMessage.error(response.data?.msg || '操作失败')
      }
    } catch (error) {
      console.error('清空购物车错误:', error)
      ElMessage.error('清空购物车失败')
    }
  }).catch(() => {})
}

// 前往商品详情页
const goToProductDetail = (productId: number) => {
  router.push(`/product/${productId}`)
}

// 前往结算页面
const proceedToCheckout = () => {
  if (cartItems.value.length === 0) {
    ElMessage.warning('购物车为空，无法结算')
    return
  }
  router.push('/checkout')
}

// 计算总价
const totalAmount = computed(() => {
  return cartItems.value.reduce((total, item) => {
    return total + (item.quantity * item.product.price)
  }, 0).toFixed(2)
})

// 计算商品总数
const totalItems = computed(() => {
  return cartItems.value.reduce((total, item) => {
    return total + item.quantity
  }, 0)
})

</script>

<template>
  <div class="cart-container">
    <div class="cart-header">
      <h1 class="title">
        <el-icon class="icon"><ShoppingCart /></el-icon>
        我的购物车
      </h1>
      <el-button
          type="danger"
          plain
          :disabled="cartItems.length === 0"
          @click="clearCartHandler"
      >
        清空购物车
      </el-button>
    </div>

    <el-divider />

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="5" animated />
    </div>

    <!-- 购物车为空状态 -->
    <el-empty
        v-else-if="cartItems.length === 0"
        description="购物车为空"
        class="empty-cart"
    >
      <el-button type="primary" @click="router.push('/product')">去购物</el-button>
    </el-empty>

    <!-- 购物车列表 -->
    <template v-else>
      <div class="cart-list">
        <el-card v-for="item in cartItems" :key="item.id" class="cart-item">
          <div class="cart-item-content">
            <!-- 商品图片 -->
            <div class="product-image" @click="goToProductDetail(item.product.id)">
              <el-image
                  :src="item.product.image || '/placeholder.png'"
                  fit="cover"
                  class="image"
              />
            </div>

            <!-- 商品信息 -->
            <div class="product-info">
              <h3
                  class="product-name"
                  @click="goToProductDetail(item.product.id)"
              >
                {{ item.product.name }}
              </h3>
              <p class="product-category">{{ item.product.category }}</p>
            </div>

            <!-- 商品单价 -->
            <div class="product-price">
              ¥{{ item.product.price }}
            </div>

            <!-- 数量控制 -->
            <div class="quantity-control">
              <el-input-number
                  v-model="item.quantity"
                  :min="1"
                  :max="item.product.stock"
                  @change="(value) => updateQuantity(item, value)"
                  size="small"
              />
            </div>

            <!-- 商品小计 -->
            <div class="subtotal">
              ¥{{ (item.product.price * item.quantity).toFixed(2) }}
            </div>

            <!-- 删除按钮 -->
            <div class="actions">
              <el-button
                  type="danger"
                  circle
                  @click="removeFromCart(item.id)"
                  :icon="Delete"
              />
            </div>
          </div>
        </el-card>
      </div>

      <!-- 购物车汇总和结算区 -->
      <div class="cart-summary">
        <el-card>
          <div class="summary-content">
            <div class="summary-info">
              <p>商品数量：<span class="highlight">{{ totalItems }}</span> 件</p>
              <p>商品总价：<span class="highlight price">¥{{ totalAmount }}</span></p>
            </div>
            <div class="checkout-action">
              <el-button type="primary" size="large" @click="proceedToCheckout">
                去结算
              </el-button>
            </div>
          </div>
        </el-card>
      </div>
    </template>
  </div>
</template>

<style scoped>
.cart-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.cart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.title {
  color: #333;
  display: flex;
  align-items: center;
  margin: 0;
}

.title .icon {
  margin-right: 10px;
  font-size: 24px;
  color: #e78c3c;
}

.loading-container {
  padding: 40px;
}

.empty-cart {
  padding: 60px 0;
}

.cart-list {
  margin-bottom: 20px;
}

.cart-item {
  margin-bottom: 15px;
}

.cart-item-content {
  display: flex;
  align-items: center;
}

.product-image {
  width: 100px;
  height: 100px;
  overflow: hidden;
  border-radius: 4px;
  margin-right: 20px;
  cursor: pointer;
  border: 1px solid #eee;
}

.image {
  width: 100%;
  height: 100%;
}

.product-info {
  flex: 1;
}

.product-name {
  margin: 0 0 10px;
  cursor: pointer;
  color: #333;
}

.product-name:hover {
  color: #e78c3c;
}

.product-category {
  color: #999;
  margin: 0;
  font-size: 14px;
}

.product-price, .subtotal {
  width: 120px;
  text-align: center;
  color: #e78c3c;
  font-weight: bold;
  font-size: 16px;
}

.quantity-control {
  width: 150px;
  text-align: center;
}

.actions {
  width: 60px;
  text-align: right;
}

.cart-summary {
  margin-top: 30px;
}

.summary-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.summary-info {
  line-height: 1.8;
}

.highlight {
  color: #e78c3c;
  font-weight: bold;
}

.price {
  font-size: 24px;
}

@media (max-width: 768px) {
  .cart-item-content {
    flex-wrap: wrap;
  }

  .product-info {
    width: calc(100% - 120px);
  }

  .product-price, .quantity-control, .subtotal {
    margin-top: 15px;
    margin-left: 120px;
  }

  .actions {
    margin-top: 15px;
  }

  .summary-content {
    flex-direction: column;
  }

  .summary-info {
    margin-bottom: 15px;
  }
}
</style>