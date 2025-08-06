<template>
  <div class="product-detail-container">
    <!-- 加载状态 -->
    <el-skeleton v-if="loading" :rows="10" animated />

    <!-- 商品详情内容 -->
    <template v-else>
      <!-- 商品主体信息 -->
      <div class="product-main">
        <div class="product-image-container">
          <el-image
              :src="getProductImage(product.image)"
              fit="contain"
              class="product-image"
          />
        </div>
        <div class="product-info">
          <h1 class="product-name">{{ product.name }}</h1>
          <div class="product-price">¥{{ product.price }}</div>

          <div class="product-meta">
            <div class="meta-item">
              <span class="label">分类：</span>
              <el-tag size="small">{{ product.category }}</el-tag>
            </div>
            <div class="meta-item">
              <span class="label">库存：</span>
              <span class="stock" :class="{ 'low-stock': product.stock < 10 }">{{ product.stock }}</span>
            </div>
          </div>

          <div class="product-actions">
            <div class="quantity-selector">
              <span class="label">数量：</span>
              <el-input-number
                  v-model="quantity"
                  :min="1"
                  :max="product.stock"
                  size="small"
              />
            </div>

            <div class="action-buttons">
              <el-button
                  type="primary"
                  :disabled="product.stock <= 0"
                  @click="addToCartHandler"
                  :loading="addingToCart"
              >
                加入购物车
              </el-button>
              <el-button
                  type="danger"
                  plain
                  :disabled="product.stock <= 0"
              >
                立即购买
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 商品详情和评论选项卡 -->
      <div class="product-tabs">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="商品详情" name="description">
            <div class="product-description">
              <p v-if="product.description">{{ product.description }}</p>
              <el-empty v-else description="暂无详细描述" />
            </div>
          </el-tab-pane>

          <el-tab-pane label="商品评论" name="comments">
            <div class="product-comments">
              <!-- 评论列表 -->
              <div v-if="comments.length" class="comment-list">
                <div v-for="comment in comments" :key="comment.id" class="comment-item">
                  <div class="comment-header">
                    <div class="user-info">
                      <span class="username">{{ comment.username }}</span>
                      <span class="comment-time">{{ formatDate(comment.createTime) }}</span>
                    </div>
                    <div class="rating">
                      <el-rate
                          v-model="comment.rating"
                          disabled
                          show-score
                          text-color="#ff9900"
                      />
                    </div>
                  </div>
                  <div class="comment-content">{{ comment.content }}</div>
                </div>
              </div>
              <el-empty v-else description="暂无评论" />
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProductDetail } from '../../api/product'
import { getCommentsByProductId } from '../../api/comment'
import { addToCart } from '../../api/cart'
import { getProductImageUrl } from '../../utils/imageHelper'

const route = useRoute()
const router = useRouter()

// 数据状态
const product = ref<any>({})
const comments = ref<any[]>([])
const quantity = ref(1)
const loading = ref(true)
const addingToCart = ref(false)
const activeTab = ref('description')

// 初始化
onMounted(async () => {
  const productId = route.params.id as string
  if (!productId) {
    ElMessage.error('商品ID不能为空')
    router.push('/product')
    return
  }

  try {
    await Promise.all([
      fetchProductDetail(productId),
      fetchComments(productId)
    ])
  } catch (error) {
    console.error('加载商品数据失败:', error)
  } finally {
    loading.value = false
  }
})

// 获取商品详情
const fetchProductDetail = async (id: string) => {
  try {
    const response = await getProductDetail(id)
    if (response.data && response.data.code === '200') {
      product.value = response.data.data || {}
    } else {
      ElMessage.error('获取商品详情失败')
    }
  } catch (error) {
    console.error('获取商品详情错误:', error)
    ElMessage.error('获取商品详情失败')
  }
}

// 获取商品评论
const fetchComments = async (productId: string) => {
  try {
    const response = await getCommentsByProductId(productId)
    if (response.data && response.data.code === '200') {
      comments.value = response.data.data || []
    }
  } catch (error) {
    console.error('获取商品评论错误:', error)
  }
}

// 添加到购物车
const addToCartHandler = async () => {
  const username = sessionStorage.getItem('username')
  const token = sessionStorage.getItem('token')

  if (!username || !token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  if (quantity.value <= 0) {
    ElMessage.warning('请选择有效的商品数量')
    return
  }

  if (quantity.value > product.value.stock) {
    ElMessage.warning('商品库存不足')
    return
  }

  addingToCart.value = true

  try {
    const response = await addToCart({
      productId: product.value.id,
      quantity: quantity.value
    })

    if (response.data && response.data.code === '200') {
      ElMessage.success('已添加到购物车')
    } else {
      ElMessage.error(response.data?.msg || '添加失败')
    }
  } catch (error) {
    console.error('添加购物车错误:', error)
    ElMessage.error('添加购物车失败')
  } finally {
    addingToCart.value = false
  }
}

// 获取商品图片
const getProductImage = (imageUrl: string) => {
  return getProductImageUrl(imageUrl);
};

// 格式化日期
const formatDate = (dateString: string) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}
</script>

<style scoped>
.product-detail-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.product-main {
  display: flex;
  gap: 30px;
  margin-bottom: 40px;
}

.product-image-container {
  flex: 1;
  max-width: 500px;
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
}

.product-image {
  width: 100%;
  height: 400px;
}

.product-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.product-name {
  font-size: 24px;
  margin-top: 0;
  margin-bottom: 16px;
  color: #333;
}

.product-price {
  font-size: 28px;
  color: #e78c3c;
  font-weight: bold;
  margin-bottom: 20px;
}

.product-meta {
  margin-bottom: 20px;
}

.meta-item {
  margin-bottom: 10px;
  display: flex;
  align-items: center;
}

.label {
  color: #666;
  margin-right: 8px;
}

.stock {
  font-weight: bold;
}

.low-stock {
  color: #f56c6c;
}

.product-actions {
  margin-top: 30px;
}

.quantity-selector {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.action-buttons {
  display: flex;
  gap: 15px;
}

.product-tabs {
  margin-top: 40px;
  border-top: 1px solid #eee;
  padding-top: 20px;
}

.product-description {
  padding: 20px;
  min-height: 150px;
  line-height: 1.8;
}

.comment-list {
  padding: 20px;
}

.comment-item {
  padding: 15px;
  border-bottom: 1px solid #eee;
  margin-bottom: 15px;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.user-info {
  display: flex;
  flex-direction: column;
}

.username {
  font-weight: bold;
  margin-bottom: 5px;
}

.comment-time {
  color: #999;
  font-size: 12px;
}

.comment-content {
  line-height: 1.6;
}

@media (max-width: 768px) {
  .product-main {
    flex-direction: column;
  }

  .product-image-container {
    max-width: 100%;
  }

  .action-buttons {
    flex-direction: column;
    gap: 10px;
  }

  .comment-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .rating {
    margin-top: 10px;
  }
}
</style>