<!-- src/views/product/ProductList.vue -->
<template>
  <div class="product-list-container">
    <!-- 筛选区域 -->
    <div class="filter-section">
      <el-input
          v-model="searchKeyword"
          placeholder="搜索商品"
          clearable
          @keyup.enter="handleSearch"
          class="search-input"
      />

      <el-select v-model="selectedCategory" placeholder="商品分类" clearable @change="handleCategoryChange">
        <el-option
            v-for="category in categories"
            :key="category.id"
            :label="category.name"
            :value="category.id"
        />
      </el-select>

      <el-select v-model="sortOption" placeholder="排序方式" @change="handleSortChange">
        <el-option label="价格从低到高" value="price_asc" />
        <el-option label="价格从高到低" value="price_desc" />
        <el-option label="最新上架" value="created_desc" />
      </el-select>
    </div>

    <!-- 商品卡片区域 -->
    <div class="product-grid">
      <el-empty v-if="products.length === 0" description="暂无商品" />
      <el-card
          v-for="product in products"
          :key="product.id"
          class="product-card"
          @click="goToDetail(product.id)"
      >
        <div class="product-image">
          <el-image :src="getProductImage(product.image)" fit="cover" lazy />
        </div>
        <div class="product-info">
          <h3 class="product-name">{{ product.name }}</h3>
          <div class="product-price">¥{{ product.price }}</div>
          <div class="product-stock">库存: {{ product.stock }}</div>
        </div>
      </el-card>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[12, 24, 36]"
          layout="total, sizes, prev, pager, next"
          :total="totalItems"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { getProductList, getCategoryList } from '../../api/product';
import { getProductImageUrl } from '../../utils/imageHelper';

const router = useRouter()

// 数据定义
const products = ref<any[]>([])
const categories = ref<any[]>([])
const searchKeyword = ref('')
const selectedCategory = ref('')
const sortOption = ref('')
const currentPage = ref(1)
const pageSize = ref(12)
const totalItems = ref(0)
const loading = ref(false)

// 初始化
onMounted(async () => {
  await Promise.all([
    fetchProducts(),
    fetchCategories()
  ])
})

// 获取商品列表
const fetchProducts = async () => {
  loading.value = true
  try {
    const [sortBy, sortDirection] = sortOption.value ? sortOption.value.split('_') : [null, null]

    const response = await getProductList({
      page: currentPage.value - 1,
      size: pageSize.value,
      categoryId: selectedCategory.value || undefined,
      keyword: searchKeyword.value || undefined,
      sortBy,
      sortDirection: sortDirection as 'asc' | 'desc'
    })

    products.value = response.data.data || []
    totalItems.value = products.value.length
  } catch (error) {
    console.error('获取商品列表失败:', error)
    ElMessage.error('获取商品列表失败')
  } finally {
    loading.value = false
  }
}

// 获取分类列表
const fetchCategories = async () => {
  try {
    const response = await getCategoryList()
    categories.value = response.data || []
  } catch (error) {
    console.error('获取分类列表失败:', error)
  }
}

// 获取商品图片的方法
const getProductImage = (imageUrl: string) => {
  return getProductImageUrl(imageUrl);
}

// 事件处理
const handleSearch = () => {
  currentPage.value = 1
  fetchProducts()
}

const handleCategoryChange = () => {
  currentPage.value = 1
  fetchProducts()
}

const handleSortChange = () => {
  fetchProducts()
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  fetchProducts()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  fetchProducts()
}

const goToDetail = (id: string) => {
  router.push(`/product/${id}`)
}
</script>

<style scoped>
.product-list-container {
  padding: 20px;
}

.filter-section {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.search-input {
  width: 300px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.product-card {
  cursor: pointer;
  transition: transform 0.3s;
}

.product-card:hover {
  transform: translateY(-5px);
}

.product-image {
  height: 200px;
  overflow: hidden;
}

.product-name {
  margin: 10px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-price {
  color: #ff6b6b;
  font-size: 18px;
  font-weight: bold;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}
</style>