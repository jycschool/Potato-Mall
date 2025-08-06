<template>
  <div class="moment-page">
    <!-- 发布动态区域 -->
    <div class="create-moment-card">
      <h2 class="section-title"><i class="fas fa-pencil-alt"></i> 分享玩家秀</h2>
      <div class="input-area">
        <textarea
          v-model="newMoment.content"
          placeholder="分享你的收藏、体验或心情..."
          rows="4"
          class="content-textarea"></textarea>

        <!-- 图片预览区 -->
        <div class="preview-images" v-if="newMoment.imageList && newMoment.imageList.length > 0">
          <div class="image-item" v-for="(img, index) in newMoment.imageList" :key="index">
            <img :src="img" alt="预览图" />
            <div class="delete-image" @click="removeImage(index)">
              <i class="fas fa-times"></i>
            </div>
          </div>
          <div class="image-counter" v-if="newMoment.imageList.length > 0">
            {{ newMoment.imageList.length }}/9 张图片
          </div>
        </div>

        <!-- 操作栏 -->
        <div class="action-bar">
          <div class="action-left">
            <input
              type="file"
              accept="image/*"
              multiple
              @change="handleImageUpload"
              ref="fileInput"
              style="display: none" />
            <button class="action-btn" @click="triggerUpload" :disabled="newMoment.imageList.length >= 9">
              <i class="fas fa-image"></i> 添加图片
            </button>
          </div>
          <button
            class="publish-btn"
            @click="publishMoment"
            :disabled="!newMoment.content"
            :class="{'publish-btn-active': newMoment.content}">
            <i class="fas fa-paper-plane"></i> 发布
          </button>
        </div>
      </div>
    </div>

    <!-- 动态列表区域 -->
    <div class="moment-list-section">
      <h2 class="section-title"><i class="fas fa-stream"></i> 玩家秀动态</h2>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <div class="spinner"></div>
        <p>正在加载精彩内容...</p>
      </div>

      <!-- 无内容状态 -->
      <div v-else-if="moments.length === 0" class="empty-container">
        <i class="fas fa-camera-retro empty-icon"></i>
        <p>还没有玩家秀，成为第一个分享的人吧！</p>
      </div>

      <!-- 动态列表 -->
      <div v-else class="moments-container">
        <div class="moment-card" v-for="moment in moments" :key="moment.momentId">
          <!-- 用户信息 -->
          <div class="moment-header">
            <div class="user-avatar" :style="{ backgroundColor: getAvatarColor(moment.username) }">
              <img v-if="moment.userAvatar" :src="moment.userAvatar" alt="用户头像" />
              <span v-else class="avatar-letter">{{ getFirstLetter(moment.username) }}</span>
            </div>
            <div class="user-info">
              <div class="user-name">{{ moment.username }}</div>
              <div class="post-time">{{ formatTime(moment.createTime) }}</div>
            </div>
            <!-- 操作菜单 -->
            <div class="moment-menu" v-if="moment.username === currentUser">
              <button class="menu-btn" @click="deleteMoment(moment)" title="删除">
                <i class="fas fa-trash-alt"></i>
              </button>
            </div>
          </div>

          <!-- 内容 -->
          <div class="moment-body">
            <div class="moment-content">{{ moment.content }}</div>

            <!-- 图片展示 -->
            <div class="moment-images" v-if="moment.images && moment.imageList.length > 0">
              <div class="image-gallery" :class="getGalleryClass(moment.imageList.length)">
                <div
                  v-for="(img, index) in moment.imageList"
                  :key="index"
                  class="gallery-item"
                  @click="viewImage(img, moment.imageList)">
                  <img :src="img" alt="玩家秀图片" />
                </div>
              </div>
            </div>
          </div>

          <!-- 交互栏 -->
          <div class="moment-footer">
            <button class="interaction-btn like-btn" @click="likeMoment(moment)" :class="{'liked': moment.isLiked}">
              <i class="fas" :class="moment.isLiked ? 'fa-thumbs-up' : 'fa-thumbs-up'"></i>
              <span>{{ moment.likeCount || 0 }}</span>
              <span class="btn-text">点赞</span>
            </button>
            <button class="interaction-btn share-btn">
              <i class="fas fa-share"></i>
              <span class="btn-text">分享</span>
            </button>
          </div>
        </div>

        <!-- 分页控件 -->
        <div class="pagination-container" v-if="totalPages > 1">
          <button class="pagination-btn" @click="changePage(currentPage - 1)" :disabled="currentPage === 0">
            <i class="fas fa-chevron-left"></i> 上一页
          </button>
          <div class="pagination-info">
            <span>{{ currentPage + 1 }} / {{ totalPages }}</span>
          </div>
          <button class="pagination-btn" @click="changePage(currentPage + 1)" :disabled="currentPage >= totalPages - 1">
            下一页 <i class="fas fa-chevron-right"></i>
          </button>
        </div>
      </div>
    </div>

    <!-- 图片预览模态框 -->
    <div class="image-viewer" v-if="previewVisible" @click="closePreview">
      <div class="image-viewer-content" @click.stop>
        <img :src="currentPreviewImage" alt="预览图片" />
        <div class="image-viewer-close" @click="closePreview">
          <i class="fas fa-times"></i>
        </div>
        <div class="image-viewer-nav">
          <button @click="prevImage" class="nav-btn" v-if="previewImages.length > 1">
            <i class="fas fa-chevron-left"></i>
          </button>
          <div class="image-counter">{{ currentPreviewIndex + 1 }}/{{ previewImages.length }}</div>
          <button @click="nextImage" class="nav-btn" v-if="previewImages.length > 1">
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent, ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { getMoments, createMoment, deleteMoment as deleteMomentApi, likeMoment as likeMomentApi } from '@/api/moment';
import { ElMessage, ElMessageBox } from 'element-plus';

export default defineComponent({
  name: 'PlayerMomentView',
  setup() {
    const router = useRouter();
    const moments = ref([]);
    const loading = ref(true);
    const currentPage = ref(0);
    const totalPages = ref(0);
    const totalElements = ref(0);
    const pageSize = 10;
    const defaultAvatar = '/default-avatar.png';

    // 图片预览相关状态
    const previewVisible = ref(false);
    const previewImages = ref([]);
    const currentPreviewIndex = ref(0);
    const currentPreviewImage = computed(() => previewImages.value[currentPreviewIndex.value] || '');

    const newMoment = ref({
      content: '',
      imageList: [],
      images: ''
    });

    const currentUser = localStorage.getItem('username');

    onMounted(() => {
      loadMoments();
    });

    const loadMoments = async (page = 0) => {
      loading.value = true;
      try {
        const response = await getMoments(page, pageSize);
        console.log('动态数据响应:', response);

        // 检查响应结构，适配不同的响应格式
        let momentData = [];
        let paginationData = {};

        if (response && response.data) {
          if (response.data.data) {
            if (response.data.data.content) {
              momentData = response.data.data.content;
              paginationData = {
                totalPages: response.data.data.totalPages,
                totalElements: response.data.data.totalElements,
                currentPage: response.data.data.currentPage
              };
            } else if (Array.isArray(response.data.data)) {
              momentData = response.data.data;
            }
          } else if (response.data.content) {
            momentData = response.data.content;
            paginationData = {
              totalPages: response.data.totalPages,
              totalElements: response.data.totalElements,
              currentPage: response.data.currentPage
            };
          }
        }

        // 处理获取到的动态数据
        if (momentData.length > 0) {
          moments.value = momentData.map(moment => {
            // 转换图片字符串为数组
            if (moment.images) {
              moment.imageList = moment.images.split(',');
            } else {
              moment.imageList = [];
            }
            // 添加isLiked属性用于UI展示
            moment.isLiked = false;
            return moment;
          });

          // 设置分页信息
          if (paginationData.totalPages) {
            totalPages.value = paginationData.totalPages;
            totalElements.value = paginationData.totalElements;
            currentPage.value = paginationData.currentPage;
          }
        } else {
          moments.value = [];
          totalPages.value = 1;
          totalElements.value = 0;
          currentPage.value = 0;
        }
      } catch (error) {
        console.error('加载动态失败:', error);
        ElMessage.error('加载动态失败，请稍后重试');
        moments.value = [];
      } finally {
        loading.value = false;
      }
    };

    const changePage = (page) => {
      if (page >= 0 && page < totalPages.value) {
        loadMoments(page);
      }
    };

    const handleImageUpload = (event) => {
      const files = event.target.files;
      if (!files || files.length === 0) return;

      // 限制上传图片数量
      const remainingSlots = 9 - newMoment.value.imageList.length;
      const filesToProcess = Math.min(remainingSlots, files.length);

      if (filesToProcess === 0) {
        ElMessage.warning('最多只能上传9张图片');
        event.target.value = '';
        return;
      }

      for (let i = 0; i < filesToProcess; i++) {
        const file = files[i];
        // 验证文件类型
        if (!file.type.startsWith('image/')) {
          ElMessage.warning(`文件 "${file.name}" 不是有效的图片格式`);
          continue;
        }

        // 验证文件大小 (限制为5MB)
        if (file.size > 5 * 1024 * 1024) {
          ElMessage.warning(`图片 "${file.name}" 超过5MB，请选择更小的图片`);
          continue;
        }

        const reader = new FileReader();
        reader.onload = (e) => {
          newMoment.value.imageList.push(e.target.result);
          newMoment.value.images = newMoment.value.imageList.join(',');
        };
        reader.readAsDataURL(file);
      }

      event.target.value = '';
    };

    const triggerUpload = () => {
      if (newMoment.value.imageList.length >= 9) {
        ElMessage.warning('最多只能上传9张图片');
        return;
      }
      document.querySelector('input[type="file"]').click();
    };

    const removeImage = (index) => {
      newMoment.value.imageList.splice(index, 1);
      newMoment.value.images = newMoment.value.imageList.join(',');
    };

    const publishMoment = async () => {
      if (!newMoment.value.content) {
        ElMessage.warning('请输入动态内容');
        return;
      }

      try {
        loading.value = true;
        await createMoment(newMoment.value.content, newMoment.value.images);
        ElMessage.success('发布成功！');

        // 清空表单
        newMoment.value.content = '';
        newMoment.value.imageList = [];
        newMoment.value.images = '';

        // 重新加载动态
        loadMoments(0);
      } catch (error) {
        console.error('发布失败:', error);
        ElMessage.error('发布失败，请稍后重试');
      } finally {
        loading.value = false;
      }
    };

    const deleteMoment = async (moment) => {
      try {
        await ElMessageBox.confirm('确定要删除这条动态吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        loading.value = true;
        await deleteMomentApi(moment.momentId);
        ElMessage.success('删除成功！');

        // 重新加载动态
        loadMoments(currentPage.value);
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error);
          ElMessage.error('删除失败，请稍后重试');
        }
      } finally {
        loading.value = false;
      }
    };

    const likeMoment = async (moment) => {
      if (moment.isLiked) {
        ElMessage.info('您已经点赞过了');
        return;
      }

      try {
        await likeMomentApi(moment.momentId);
        // 本地更新点赞状态
        moment.likeCount = (moment.likeCount || 0) + 1;
        moment.isLiked = true;
        ElMessage.success('点赞成功！');
      } catch (error) {
        console.error('点赞失败:', error);
        ElMessage.error('点赞失败，请稍后重试');
      }
    };

    const formatTime = (time) => {
      if (!time) return '';

      const date = new Date(time);
      const now = new Date();
      const diff = (now.getTime() - date.getTime()) / 1000; // 差异（秒）

      if (diff < 60) {
        return '刚刚';
      } else if (diff < 3600) {
        return Math.floor(diff / 60) + '分钟前';
      } else if (diff < 86400) {
        return Math.floor(diff / 3600) + '小时前';
      } else if (diff < 2592000) {
        return Math.floor(diff / 86400) + '天前';
      } else {
        return date.toLocaleDateString();
      }
    };

    const getGalleryClass = (count) => {
      if (count === 1) return 'single-image';
      if (count === 2) return 'two-images';
      if (count === 3) return 'three-images';
      if (count === 4) return 'four-images';
      return 'multi-images';
    };

    // 图片预览功能
    const viewImage = (img, imageList) => {
      previewImages.value = [...imageList];
      currentPreviewIndex.value = imageList.indexOf(img);
      previewVisible.value = true;

      // 禁止背景滚动
      document.body.style.overflow = 'hidden';
    };

    const closePreview = () => {
      previewVisible.value = false;
      // 恢复背景滚动
      document.body.style.overflow = '';
    };

    const nextImage = () => {
      if (currentPreviewIndex.value < previewImages.value.length - 1) {
        currentPreviewIndex.value++;
      } else {
        currentPreviewIndex.value = 0; // 循环到第一张
      }
    };

    const prevImage = () => {
      if (currentPreviewIndex.value > 0) {
        currentPreviewIndex.value--;
      } else {
        currentPreviewIndex.value = previewImages.value.length - 1; // 循环到最后一张
      }
    };

    const getFirstLetter = (username) => {
      return username.charAt(0).toUpperCase();
    };

    const getAvatarColor = (username) => {
      const colors = ['#f56a6a', '#6a5ef5', '#5ef56a', '#f5a623', '#50e3c2', '#9013fe'];
      let sum = 0;
      for (let i = 0; i < username.length; i++) {
        sum += username.charCodeAt(i);
      }
      const index = sum % colors.length;
      return colors[index];
    };

    return {
      moments,
      loading,
      currentPage,
      totalPages,
      newMoment,
      currentUser,
      defaultAvatar,
      previewVisible,
      previewImages,
      currentPreviewIndex,
      currentPreviewImage,
      loadMoments,
      changePage,
      handleImageUpload,
      triggerUpload,
      removeImage,
      publishMoment,
      deleteMoment,
      likeMoment,
      formatTime,
      getGalleryClass,
      viewImage,
      closePreview,
      nextImage,
      prevImage,
      getFirstLetter,
      getAvatarColor
    };
  }
});
</script>

<style scoped>
.moment-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.create-moment-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 30px;
}

.section-title {
  font-size: 18px;
  color: #333;
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.section-title i {
  margin-right: 8px;
  color: #409eff;
}

.input-area {
  display: flex;
  flex-direction: column;
}

.content-textarea {
  width: 100%;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 10px;
  margin-bottom: 15px;
  font-size: 14px;
  resize: none;
}

.preview-images {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 15px;
}

.image-item {
  position: relative;
  width: 80px;
  height: 80px;
  border-radius: 4px;
  overflow: hidden;
}

.image-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.delete-image {
  position: absolute;
  top: 0;
  right: 0;
  width: 20px;
  height: 20px;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.image-counter {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.action-left {
  display: flex;
  align-items: center;
}

.action-btn {
  background-color: #f0f2f5;
  border: none;
  border-radius: 4px;
  padding: 8px 12px;
  cursor: pointer;
  font-size: 14px;
  color: #606266;
  display: flex;
  align-items: center;
}

.action-btn i {
  margin-right: 5px;
}

.action-btn:disabled {
  background-color: #e6e8eb;
  cursor: not-allowed;
}

.publish-btn {
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  font-size: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
}

.publish-btn i {
  margin-right: 5px;
}

.publish-btn:disabled {
  background-color: #a0cfff;
  cursor: not-allowed;
}

.publish-btn-active {
  background-color: #66b1ff;
}

.moment-list-section {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.loading-container, .empty-container {
  text-align: center;
  padding: 30px 0;
  color: #909399;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #409eff;
  border-radius: 50%;
  margin: 0 auto 15px;
  animation: spin 1s linear infinite;
}

.empty-icon {
  font-size: 50px;
  color: #409eff;
  margin-bottom: 10px;
}

.moments-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.moment-card {
  background-color: #f9f9f9;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.moment-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
  overflow: hidden;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-letter {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  color: white;
  font-weight: bold;
  font-size: 16px;
}

.user-info {
  flex-grow: 1;
}

.user-name {
  font-weight: bold;
  font-size: 16px;
  color: #303133;
  margin-bottom: 4px;
}

.post-time {
  color: #909399;
  font-size: 12px;
}

.moment-body {
  margin-bottom: 10px;
}

.moment-content {
  font-size: 14px;
  color: #303133;
  line-height: 1.6;
  margin-bottom: 10px;
  white-space: pre-wrap;
  word-break: break-word;
}

.moment-images {
  margin-bottom: 10px;
}

.image-gallery {
  display: grid;
  grid-gap: 5px;
}

.image-gallery img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
  cursor: pointer;
}

.single-image {
  grid-template-columns: 1fr;
}

.single-image img {
  max-height: 300px;
  width: auto;
  margin: 0 auto;
}

.two-images {
  grid-template-columns: 1fr 1fr;
}

.three-images {
  grid-template-columns: 1fr 1fr 1fr;
}

.four-images {
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
}

.multi-images {
  grid-template-columns: 1fr 1fr 1fr;
  grid-auto-rows: 120px;
}

.moment-footer {
  display: flex;
  gap: 15px;
}

.interaction-btn {
  background-color: transparent;
  border: none;
  color: #606266;
  cursor: pointer;
  font-size: 14px;
  padding: 5px 0;
  display: flex;
  align-items: center;
}

.interaction-btn i {
  margin-right: 5px;
}

.interaction-btn.liked {
  color: #409eff;
}

.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  gap: 15px;
}

.pagination-btn {
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 6px 12px;
  cursor: pointer;
}

.pagination-btn:disabled {
  background-color: #a0cfff;
  cursor: not-allowed;
}

.image-viewer {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.image-viewer-content {
  position: relative;
  max-width: 90%;
  max-height: 90%;
}

.image-viewer img {
  width: 100%;
  height: auto;
  border-radius: 4px;
}

.image-viewer-close {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 30px;
  height: 30px;
  background-color: rgba(255, 255, 255, 0.8);
  color: #333;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border-radius: 50%;
}

.image-viewer-nav {
  position: absolute;
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  align-items: center;
  gap: 10px;
}

.nav-btn {
  background-color: rgba(255, 255, 255, 0.8);
  color: #333;
  border: none;
  border-radius: 4px;
  padding: 8px 12px;
  cursor: pointer;
  display: flex;
  align-items: center;
}

.nav-btn i {
  margin: 0 5px;
}
</style>
