<template>
  <div class="repository-container">
    <h1 class="page-title">我的商品库</h1>

    <!-- 加载状态 -->
    <el-skeleton v-if="loading" :rows="10" animated />

    <!-- 商品库内容 -->
    <template v-else>
      <!-- 过滤器 -->
      <div class="filter-bar">
        <el-select v-model="filter" placeholder="筛选条件" class="filter-select">
          <el-option label="全部商品" value="all" />
          <el-option label="未评价商品" value="unrated" />
          <el-option label="已评价商品" value="rated" />
          <el-option label="盲盒物品" value="mysterybox" />
          <el-option label="抽取获得" value="drawn" />
        </el-select>

        <el-select v-model="sortBy" placeholder="排序方式" class="filter-select">
          <el-option label="最近购买" value="newest" />
          <el-option label="最早购买" value="oldest" />
        </el-select>
      </div>

      <!-- 商品列表 -->
      <el-empty v-if="filteredItems.length === 0" description="暂无购买记录" />

      <div v-else class="repository-items">
        <el-card v-for="item in filteredItems" :key="item.id" class="repository-item">
          <!-- 已抽取盲盒的结果标记 -->
          <div v-if="item.status === '已抽取' && item.remark" class="drawn-result-badge">
            <el-tooltip :content="item.remark">
              <el-tag type="info" size="small" effect="dark">
                {{ getDrawnResultText(item) }}
              </el-tag>
            </el-tooltip>
          </div>

          <!-- 抽取获得的物品标记 -->
          <div v-if="item.status === '抽取获得'" class="prize-badge">
            <el-tooltip :content="item.remark || '通过抽取盲盒获得'">
              <el-tag type="success" size="small" effect="dark">盲盒奖品</el-tag>
            </el-tooltip>
          </div>

          <div class="item-content">
            <!-- 商品图片 -->
            <div class="item-image-container">
              <el-image
                  :src="item.productImage || '/placeholder.png'"
                  fit="contain"
                  class="item-image"
              />
            </div>

            <!-- 商品信息 -->
            <div class="item-info">
              <h3 class="item-name">{{ item.productName }}</h3>
              <div class="item-meta">
                <p class="item-price">¥{{ item.price }} × {{ item.quantity }} 件</p>
                <p class="item-order">订单号: {{ item.orderId }}</p>
                <p class="item-time">购买时间: {{ formatDate(item.purchaseTime) }}</p>
                <p class="item-status">
                  <el-tag :type="getStatusType(item.status)">
                    {{ item.status }}
                  </el-tag>
                  <el-tag v-if="item.isRated" type="info" style="margin-left: 5px">已评价</el-tag>
                  <el-tag v-else type="warning" style="margin-left: 5px">未评价</el-tag>

                  <!-- 显示盲盒抽取结果 -->
                  <el-tag
                    v-if="item.status === '已抽取'"
                    type="success"
                    style="margin-left: 5px"
                  >
                    {{ getDrawnResultText(item) }}
                  </el-tag>
                </p>
              </div>
            </div>

            <!-- 操作按钮 -->
            <div class="item-actions">
              <el-button
                  v-if="!item.isRated"
                  type="primary"
                  size="small"
                  @click="openRateDialog(item)"
              >
                评价商品
              </el-button>
              <el-button
                  v-else-if="item.isRated"
                  type="info"
                  size="small"
                  plain
                  disabled
              >
                已评价
              </el-button>
              <el-button
                  type="primary"
                  size="small"
                  plain
                  @click="viewProduct(item.productId)"
              >
                查看商品
              </el-button>
              <el-button
                  v-if="item.status !== '已抽取' && item.status !== '抽取获得'"
                  type="danger"
                  size="small"
                  @click="handleDrawMysteryBox(item)"
                  :loading="item.drawing"
              >
                抽取盲盒
              </el-button>
              <el-button
                  v-else-if="item.status === '已抽取'"
                  type="success"
                  size="small"
                  disabled
              >
                已抽取: {{ getDrawnResultShortText(item) }}
              </el-button>
            </div>
          </div>
        </el-card>
      </div>
    </template>

    <!-- 评价对话�� -->
    <el-dialog
        v-model="rateDialogVisible"
        title="评价商品"
        width="500px"
    >
      <div class="rate-form">
        <div class="rate-product-info" v-if="currentItem">
          <h4>{{ currentItem.productName }}</h4>
          <p class="order-info">订单号: {{ currentItem.orderId }}</p>
        </div>

        <el-form :model="rateForm" label-width="80px">
          <el-form-item label="评分">
            <el-rate
                v-model="rateForm.rating"
                :max="5"
                show-score
                required
            />
          </el-form-item>
          <el-form-item label="评价内容">
            <el-input
                v-model="rateForm.content"
                type="textarea"
                :rows="4"
                placeholder="请输入您对商品的评价..."
            />
          </el-form-item>
        </el-form>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="rateDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitRate" :loading="submitting">提交评价</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 抽取结��对话框 -->
    <el-dialog
        v-model="drawResultVisible"
        title="抽取结果"
        width="500px"
        center
    >
      <div class="draw-result" v-if="drawResult">
        <div class="draw-result-icon">
          <el-icon size="48" color="#67C23A"><el-icon-gift /></el-icon>
        </div>
        <h2 class="draw-result-title">恭喜您获得了</h2>
        <div class="draw-result-content">
          <div v-if="drawResult.type === 'product'" class="physical-prize">
            <el-image
                :src="drawResult.image || '/placeholder.png'"
                fit="contain"
                class="prize-image"
            />
            <h3>{{ drawResult.name }}</h3>
            <p>{{ drawResult.description }}</p>
          </div>
          <div v-else class="virtual-prize">
            <h3>{{ drawResult.name }}</h3>
            <p v-if="drawResult.value">价值: {{ drawResult.value }}</p>
            <p>{{ drawResult.description }}</p>
          </div>
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="drawResultVisible = false">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getUserPurchases, rateProduct, drawMysteryBox } from '../../api/repository'

const router = useRouter()
const loading = ref(true)
const repositoryItems = ref<any[]>([])
const filter = ref('all')
const sortBy = ref('newest')
const rateDialogVisible = ref(false)
const drawResultVisible = ref(false)
const currentItem = ref<any>(null)
const drawResult = ref<any>(null)
const submitting = ref(false)

const rateForm = ref({
  rating: 5,
  content: ''
})

// 获取用户仓库数据
const fetchRepositoryItems = async () => {
  loading.value = true
  try {
    const response = await getUserPurchases()
    if (response.data && response.data.code === '200') {
      repositoryItems.value = response.data.data || []
      // 为每个项目添加抽取状态
      repositoryItems.value.forEach(item => {
        item.drawing = false
      })
    } else {
      ElMessage.error(response.data?.msg || '获取数据失败')
    }
  } catch (error) {
    console.error('获取用户仓库数据错误:', error)
    ElMessage.error('获取用户仓库数据失败')
  } finally {
    loading.value = false
  }
}

// 获取抽取结果的文本
const getDrawnResultText = (item) => {
  if (!item.remark) return '已抽取'

  // 如果备注包含"抽取结果:"，则取后面的内容
  if (item.remark.includes('抽取结果:')) {
    return item.remark.split('抽取结果:')[1].trim()
  }

  // 尝试解析扩展信息JSON
  if (item.extInfo) {
    try {
      const extInfo = JSON.parse(item.extInfo)
      if (extInfo.name) {
        return `抽中: ${extInfo.name}`
      }
    } catch (e) {
      // JSON解析失败，忽略
    }
  }

  return item.remark
}

// 获取抽取结果的简短文本(按钮用)
const getDrawnResultShortText = (item) => {
  const text = getDrawnResultText(item)
  if (text.length > 5) {
    return text.substring(0, 5) + '..'
  }
  return text
}

// 根据状态获取标签类型
const getStatusType = (status: string) => {
  switch (status) {
    case '已付款': return 'success'
    case '已抽取': return 'info'
    case '抽取获得': return 'warning'
    default: return 'info'
  }
}

// 过滤和排序后的商品列表
const filteredItems = computed(() => {
  let items = [...repositoryItems.value]

  // 先按条件过滤
  if (filter.value === 'rated') {
    items = items.filter(item => item.isRated)
  } else if (filter.value === 'unrated') {
    items = items.filter(item => !item.isRated)
  } else if (filter.value === 'mysterybox') {
    items = items.filter(item => item.status === '已付款' || item.status === '已抽取')
  } else if (filter.value === 'drawn') {
    items = items.filter(item => item.status === '抽取获得')
  }

  // 再按条件排序
  items.sort((a, b) => {
    const dateA = new Date(a.purchaseTime).getTime()
    const dateB = new Date(b.purchaseTime).getTime()

    if (sortBy.value === 'newest') {
      return dateB - dateA
    } else {
      return dateA - dateB
    }
  })

  return items
})

// 打开评价对话框
const openRateDialog = (item: any) => {
  currentItem.value = item
  rateForm.value = {
    rating: 5,
    content: ''
  }
  rateDialogVisible.value = true
}

// 抽取盲盒
const handleDrawMysteryBox = async (item: any) => {
  // 二次确认
  if (!confirm(`确定要抽取 "${item.productName}" 吗？抽取后盲盒将无法再次抽取。`)) {
    return
  }

  item.drawing = true

  try {
    console.log('开始调用抽取盲盒API, itemId:', item.id)
    const response = await drawMysteryBox(item.id)
    console.log('抽取盲盒API返回结果:', response)

    if (response && response.data && response.data.code === '200') {
      drawResult.value = response.data.data
      drawResultVisible.value = true

      // 不再从仓库列表中移除该物品，而是更新其状态
      const index = repositoryItems.value.findIndex(i => i.id === item.id)
      if (index !== -1) {
        // 更新盲盒状态
        repositoryItems.value[index].status = '已抽取'
        // ���存抽取结果
        repositoryItems.value[index].remark = `抽取结果: ${drawResult.value.name}`
      }

      ElMessage.success('抽取成功')

      // 刷新仓库列表���取新的抽取结果物品
      fetchRepositoryItems()
    } else {
      // 显示更详细的错误信息
      console.error('API返回错误:', response)
      ElMessage.error(response && response.data ? response.data.msg || '抽取失败' : '服务器返回异常')
    }
  } catch (error) {
    console.error('抽取盲盒错误:', error)
    // 显示更具体的错误信息
    if (error.response) {
      console.error('错误响应数据:', error.response.data)
      console.error('错误状态码:', error.response.status)
      ElMessage.error(`抽取失败: ${error.response.data?.msg || error.message || '未知错误'}`)
    } else if (error.request) {
      console.error('未收到响应:', error.request)
      ElMessage.error('服务器未响应，请稍后再试')
    } else {
      console.error('请求配置错误:', error.message)
      ElMessage.error(`请求错误: ${error.message}`)
    }
  } finally {
    item.drawing = false
  }
}

// 提交评价
const submitRate = async () => {
  if (!currentItem.value) return

  if (!rateForm.value.rating) {
    ElMessage.warning('请选择评分')
    return
  }

  if (!rateForm.value.content.trim()) {
    ElMessage.warning('请填写评价内容')
    return
  }

  submitting.value = true

  try {
    const response = await rateProduct(currentItem.value.id, rateForm.value)

    if (response.data && response.data.code === '200') {
      ElMessage.success('评价成功')
      rateDialogVisible.value = false

      // 更新当前项的评价状态
      const index = repositoryItems.value.findIndex(item => item.id === currentItem.value.id)
      if (index !== -1) {
        repositoryItems.value[index].isRated = true
      }
    } else {
      ElMessage.error(response.data?.msg || '评价失败')
    }
  } catch (error) {
    console.error('提交评价错误:', error)
    ElMessage.error('提交评价失败')
  } finally {
    submitting.value = false
  }
}

// 查看商品详情
const viewProduct = (productId: number) => {
  router.push(`/product/${productId}`)
}

// 格式���日期
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

onMounted(() => {
  fetchRepositoryItems()
})
</script>

<style scoped>
.repository-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.page-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.filter-bar {
  display: flex;
  margin-bottom: 20px;
  gap: 15px;
}

.filter-select {
  width: 150px;
}

.repository-items {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.repository-item {
  transition: transform 0.2s;
  position: relative;
}

.repository-item:hover {
  transform: translateY(-3px);
}

/* 抽取结果标记样式 */
.drawn-result-badge {
  position: absolute;
  top: 10px;
  left: 10px;
  z-index: 10;
  max-width: 150px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 抽取奖品标记样式 */
.prize-badge {
  position: absolute;
  top: 10px;
  left: 10px;
  z-index: 10;
}

.item-content {
  display: flex;
  align-items: center;
}

.item-image-container {
  width: 100px;
  height: 100px;
  flex-shrink: 0;
  margin-right: 20px;
}

.item-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.item-info {
  flex: 1;
}

.item-name {
  font-size: 18px;
  margin: 0 0 10px 0;
}

.item-meta p {
  margin: 5px 0;
  color: #666;
}

.item-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-left: 20px;
}

.rate-product-info {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.draw-result {
  text-align: center;
  padding: 20px 0;
}

.draw-result-icon {
  margin-bottom: 15px;
}

.draw-result-title {
  margin-bottom: 20px;
  color: #67C23A;
}

.prize-image {
  max-width: 200px;
  max-height: 200px;
  margin-bottom: 15px;
}

.physical-prize, .virtual-prize {
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>