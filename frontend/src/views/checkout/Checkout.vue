<template>
  <div class="checkout-container">
    <el-card class="checkout-card">
      <div class="checkout-header">
        <h2 class="title">
          <el-icon class="icon"><ShoppingBag /></el-icon> 订单结算
        </h2>
        <el-button plain @click="goBackToCart">返回购物车</el-button>
      </div>

      <div class="checkout-content" v-loading="loading">
        <template v-if="selectedItems.length === 0">
          <div class="empty-checkout">
            <el-empty description="没有可结算的商品" />
            <el-button type="primary" @click="goBackToCart">返回购物车</el-button>
          </div>
        </template>

        <template v-else>
          <div class="checkout-section">
            <h3 class="section-title">确认商品信息</h3>
            <el-table :data="selectedItems" border style="width: 100%">
              <el-table-column label="商品">
                <template #default="{ row }">
                  <div class="product-info-cell">
                    <div class="product-image-mini">
                      <el-image :src="row.product.image" fit="cover" />
                    </div>
                    <div class="product-name-cell">{{ row.product.name }}</div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="product.price" label="单价" width="120">
                <template #default="{ row }">
                  <span class="price-text">￥{{ row.product.price }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="quantity" label="数量" width="100" />
              <el-table-column label="小计" width="150">
                <template #default="{ row }">
                  <span class="price-text">￥{{ (row.product.price * row.quantity).toFixed(2) }}</span>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <div class="checkout-section">
            <h3 class="section-title">收货信息</h3>
            <el-form :model="deliveryInfo" :rules="rules" ref="formRef">
              <el-form-item prop="address" label="收货地址">
                <el-input v-model="deliveryInfo.address" placeholder="请输入详细收货地址" />
              </el-form-item>
              <el-form-item prop="contactPhone" label="联系电话">
                <el-input v-model="deliveryInfo.contactPhone" placeholder="请输入联系电话" />
              </el-form-item>
            </el-form>
          </div>

          <div class="checkout-section">
            <h3 class="section-title">支付方式</h3>
            <el-radio-group v-model="paymentMethod">
              <el-radio label="wechat">微信支付</el-radio>
              <el-radio label="alipay">支付宝</el-radio>
              <el-radio label="creditcard">银行卡支付</el-radio>
            </el-radio-group>
          </div>

          <div class="checkout-summary">
            <div class="summary-row">
              <span>商品总额：</span>
              <span class="price-text">￥{{ totalAmount.toFixed(2) }}</span>
            </div>
            <div class="summary-row">
              <span>运费：</span>
              <span class="price-text">￥{{ shippingFee.toFixed(2) }}</span>
            </div>
            <div class="summary-row total">
              <span>实付金额：</span>
              <span class="price-text">￥{{ (totalAmount + shippingFee).toFixed(2) }}</span>
            </div>

            <el-button
                type="primary"
                size="large"
                @click="submitOrder"
                :loading="submitting"
                :disabled="submitting"
                class="submit-button"
            >
              提交订单
            </el-button>
          </div>
        </template>
      </div>
    </el-card>

    <!-- 支付成功对话框 -->
    <el-dialog v-model="successDialogVisible" title="订单提交成功" width="30%" center>
      <div class="success-dialog-content">
        <el-icon class="success-icon"><CircleCheck /></el-icon>
        <p>您的订单已成功提交!</p>
        <p v-if="orderNumber">订单号: {{ orderNumber }}</p>
      </div>
      <template #footer>
        <span>
          <el-button @click="goToHome">返回首页</el-button>
          <el-button type="primary" @click="goToUserRepository">查看我的订单</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ShoppingBag, CircleCheck } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getSelectedCartItems } from '../../api/cart'
import { purchaseFromCart } from '@/api/repository'

const router = useRouter()
const loading = ref(false)
const submitting = ref(false)
const selectedItems = ref([])
const totalAmount = ref(0)
const shippingFee = ref(10) // 设置默认运费
const paymentMethod = ref('wechat')
const successDialogVisible = ref(false)
const orderNumber = ref('')
const formRef = ref(null)

const deliveryInfo = reactive({
  address: '',
  contactPhone: ''
})

const rules = {
  address: [
    { required: true, message: '请输入收货地址', trigger: 'blur' }
  ],
  contactPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

onMounted(() => {
  // 检查用户登录状态
  const token = sessionStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  // 获取已选中的购物车商品
  fetchSelectedItems()
})

// 获取已选中的购物车商品
const fetchSelectedItems = async () => {
  loading.value = true
  try {
    const username = sessionStorage.getItem('username')
    if (!username) {
      ElMessage.warning('请先登录')
      router.push('/login')
      return
    }

    const response = await getSelectedCartItems()
    if (response.data && response.data.code === '200') {
      selectedItems.value = response.data.data || []
      calculateTotal()
    } else {
      ElMessage.error(response.data?.msg || '获取结算商品失败')
    }
  } catch (error) {
    console.error('获取结算商品失败', error)
    ElMessage.error('获取结算商品失败')
  } finally {
    loading.value = false
  }
}

// 计算总金额
const calculateTotal = () => {
  totalAmount.value = selectedItems.value.reduce((sum, item) =>
      sum + (item.product.price * item.quantity), 0)
}

// 表单验证
const validateForm = async () => {
  if (!formRef.value) return false

  try {
    await formRef.value.validate()
    return true
  } catch (error) {
    return false
  }
}

// 提交订单
const submitOrder = async () => {
  // 表单验证
  if (!await validateForm()) {
    return // 表单验证失败
  }

  // 如果没有选中商品，提示用户
  if (selectedItems.value.length === 0) {
    ElMessage.warning('没有可结算的商品')
    return
  }

  submitting.value = true

  try {
    // 使用封装好的API调用方法
    const response = await purchaseFromCart({
      address: deliveryInfo.address,
      contactPhone: deliveryInfo.contactPhone
    })

    if (response.data && response.data.code === '200') {
      // 订单提交成功
      // 从返回信息中提取订单号
      const message = response.data.data
      const match = message.match(/订单号:\s*(\d+)/)
      if (match && match[1]) {
        orderNumber.value = match[1]
      }

      // 显示成功对话框
      successDialogVisible.value = true
    } else {
      ElMessage.error(response.data?.msg || '订单提交失败')
    }
  } catch (error) {
    console.error('提交订单失败', error)
    ElMessage.error('提交订单失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

// 返回购物车
const goBackToCart = () => {
  router.push('/cart')
}

// 返回首页
const goToHome = () => {
  router.push('/home')
  successDialogVisible.value = false
}

// 前往用户仓库/订单列表
const goToUserRepository = () => {
  router.push('/repository')
  successDialogVisible.value = false
}
</script>

<style scoped>
.checkout-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.checkout-card {
  margin-bottom: 20px;
}

.checkout-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
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

.checkout-section {
  margin-bottom: 30px;
}

.section-title {
  font-size: 18px;
  margin-bottom: 15px;
  color: #333;
  border-left: 4px solid #e78c3c;
  padding-left: 10px;
}

.product-info-cell {
  display: flex;
  align-items: center;
}

.product-image-mini {
  width: 50px;
  height: 50px;
  margin-right: 10px;
}

.product-name-cell {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.price-text {
  color: #e78c3c;
  font-weight: bold;
}

.checkout-summary {
  background-color: #f8f8f8;
  padding: 20px;
  border-radius: 4px;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.summary-row.total {
  font-size: 18px;
  font-weight: bold;
  margin-top: 20px;
  padding-top: 10px;
  border-top: 1px solid #ddd;
}

.submit-button {
  width: 100%;
  margin-top: 20px;
}

.empty-checkout {
  text-align: center;
  padding: 40px 0;
}

.success-dialog-content {
  text-align: center;
  padding: 20px 0;
}

.success-icon {
  font-size: 60px;
  color: #67c23a;
  margin-bottom: 20px;
}
</style>