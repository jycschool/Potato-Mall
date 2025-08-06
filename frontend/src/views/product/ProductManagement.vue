<!-- src/views/merchant/ProductManagement.vue -->
<template>
  <div class="product-management-container">
    <div class="page-header">
      <h1>商品管理</h1>
      <el-button type="primary" @click="openAddProductDialog">添加商品</el-button>
    </div>

    <div class="search-filters">
      <el-input
          v-model="keyword"
          placeholder="搜索商品名称"
          clearable
          style="width: 300px;"
          @keyup.enter="searchProducts"
      />
      <el-select v-model="categoryId" placeholder="选择分类" clearable>
        <el-option
            v-for="category in categories"
            :key="category.id"
            :label="category.name"
            :value="category.id"
        />
      </el-select>
      <el-button type="primary" @click="searchProducts">搜索</el-button>
      <el-button @click="resetFilters">重置</el-button>
    </div>

    <el-table :data="products" border v-loading="loading" style="width: 100%">
      <el-table-column label="商品图片" width="100">
        <template #default="scope">
          <el-image
              style="width: 60px; height: 60px"
              :src="scope.row.images[0] || '/placeholder.png'"
              fit="cover"
          />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="商品名称" />
      <el-table-column prop="price" label="价格" width="120">
        <template #default="scope">¥{{ scope.row.price }}</template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="100" />
      <el-table-column prop="categoryName" label="分类" width="120" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="editProduct(scope.row)">编辑</el-button>
          <el-popconfirm
              title="确认删除该商品?"
              @confirm="deleteProductItem(scope.row.id)"
          >
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="pagination"
    />

    <!-- 添加/编辑商品对话框 -->
    <el-dialog
        v-model="dialogVisible"
        :title="isEdit ? '编辑商品' : '添加商品'"
        width="70%"
    >
      <el-form :model="productForm" :rules="rules" ref="formRef" label-width="100px">
        <!-- 表单内容 -->
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="productForm.name" />
        </el-form-item>

        <el-form-item label="价格" prop="price">
          <el-input-number v-model="productForm.price" :min="0.01" :precision="2" />
        </el-form-item>

        <el-form-item label="库存" prop="stock">
          <el-input-number v-model="productForm.stock" :min="0" />
        </el-form-item>

        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="productForm.categoryId">
            <el-option
                v-for="category in categories"
                :key="category.id"
                :label="category.name"
                :value="category.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="商品图片" prop="images">
          <el-upload
              action="/api/upload"
              list-type="picture-card"
              :file-list="fileList"
              :on-success="handleUploadSuccess"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="商品描述" prop="description">
          <el-input v-model="productForm.description" type="textarea" rows="6" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import {
  getProductList,
  getCategoryList,
  createProduct,
  updateProduct,
  deleteProduct
} from '../../api/product'

const loading = ref(false)
const products = ref([])
const categories = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const keyword = ref('')
const categoryId = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const fileList = ref([])

const productForm = reactive({
  id: '',
  name: '',
  price: 0,
  stock: 0,
  categoryId: '',
  description: '',
  images: []
})

const rules = {
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  description: [{ required: true, message: '请输入商品描述', trigger: 'blur' }]
}

onMounted(async () => {
  await Promise.all([
    loadProducts(),
    loadCategories()
  ])
})

const loadProducts = async () => {
  loading.value = true
  try {
    const res = await getProductList({
      page: currentPage.value - 1,
      size: pageSize.value,
      keyword: keyword.value,
      categoryId: categoryId.value
    })
    products.value = res.data.content
    total.value = res.data.totalElements
  } catch (error) {
    console.error(error)
    ElMessage.error('获取商品列表失败')
  } finally {
    loading.value = false
  }
}

const loadCategories = async () => {
  try {
    const res = await getCategoryList()
    categories.value = res.data
  } catch (error) {
    console.error(error)
    ElMessage.error('获取分类列表失败')
  }
}

const searchProducts = () => {
  currentPage.value = 1
  loadProducts()
}

const resetFilters = () => {
  keyword.value = ''
  categoryId.value = ''
  currentPage.value = 1
  loadProducts()
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  loadProducts()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  loadProducts()
}

const openAddProductDialog = () => {
  isEdit.value = false
  Object.assign(productForm, {
    id: '',
    name: '',
    price: 0,
    stock: 0,
    categoryId: '',
    description: '',
    images: []
  })
  fileList.value = []
  dialogVisible.value = true
}

const editProduct = (row: any) => {
  isEdit.value = true
  Object.assign(productForm, row)
  fileList.value = row.images.map((url: string, index: number) => ({
    name: `image-${index}`,
    url
  }))
  dialogVisible.value = true
}

const handleUploadSuccess = (res: any) => {
  productForm.images.push(res.data.url)
}

const submitForm = async () => {
  try {
    if (isEdit.value) {
      await updateProduct(productForm.id, productForm)
      ElMessage.success('商品更新成功')
    } else {
      await createProduct(productForm)
      ElMessage.success('商品添加成功')
    }
    dialogVisible.value = false
    loadProducts()
  } catch (error) {
    console.error(error)
    ElMessage.error('操作失败')
  }
}

const deleteProductItem = async (id: string) => {
  try {
    await deleteProduct(id)
    ElMessage.success('商品删除成功')
    loadProducts()
  } catch (error) {
    console.error(error)
    ElMessage.error('删除失败')
  }
}
</script>

<style scoped>
.product-management-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-filters {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>