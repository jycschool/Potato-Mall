<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'
import { ElCarousel, ElCarouselItem, ElSkeleton, ElSkeletonItem } from 'element-plus'

const router = useRouter()

// 热门商品数据
const hotProducts = ref([
  {
    id: 1,
    name: "限定款盲盒 - 夏日特辑",
    price: 59.9,
    image: "https://via.placeholder.com/300x300?text=夏日特辑",
    soldCount: 1289
  },
  {
    id: 2,
    name: "传说稀有度盲盒 - S级",
    price: 99.9,
    image: "https://via.placeholder.com/300x300?text=传说稀有度",
    soldCount: 876
  },
  {
    id: 3,
    name: "动漫联名款 - 潮玩系列",
    price: 69.9,
    image: "https://via.placeholder.com/300x300?text=动漫联名",
    soldCount: 1043
  },
  {
    id: 4,
    name: "日常实用款 - 桌面小物",
    price: 39.9,
    image: "https://via.placeholder.com/300x300?text=桌面小物",
    soldCount: 1567
  }
])

// 用户评价数据
const userReviews = ref([
  {
    username: "happy_customer",
    avatar: null,
    rating: 5,
    content: "抽到了超稀有的手办！包装精美，物流也很快，非常满意！",
    date: "2025-07-15"
  },
  {
    username: "box_lover",
    avatar: null,
    rating: 4,
    content: "已经是第五次购买了，每次都有惊喜。这次的盲盒虽然没抽到稀有款，但普通款的品质也很好。",
    date: "2025-07-20"
  },
  {
    username: "surprise_hunter",
    avatar: null,
    rating: 5,
    content: "朋友推荐来买的，没想到一次就抽中了限定款！太幸运了，会继续支持的！",
    date: "2025-07-23"
  }
])

// 玩家秀精选
const playerShowcases = ref([
  {
    id: 1,
    username: "lucky_star",
    content: "终于集齐了整个系列！历时三个月，花费值得！",
    image: "https://via.placeholder.com/500x300?text=玩家秀1",
    likeCount: 256
  },
  {
    id: 2,
    username: "collection_master",
    content: "今天抽到了传说中的金色土豆！据说只有千分之一的概率！",
    image: "https://via.placeholder.com/500x300?text=玩家秀2",
    likeCount: 432
  },
  {
    id: 3,
    username: "box_enthusiast",
    content: "分享我的盲盒墙，满满的都是回忆~",
    image: "https://via.placeholder.com/500x300?text=玩家秀3",
    likeCount: 189
  }
])

// 页面加载状态
const loading = ref(true)

onMounted(() => {
  // 模拟数据加载
  setTimeout(() => {
    loading.value = false
  }, 800)
})

// 导航函数
const navigateToLogin = () => {
    router.push('/login')
}

const navigateToRegister = () => {
    router.push('/register')
}

const navigateToProducts = () => {
    router.push('/product')
}

const navigateToProductDetail = (productId) => {
    router.push(`/product/${productId}`)
}

const navigateToPlayerMoments = () => {
    router.push('/moments')
}

// 获取用户头像颜色和首字母
const getAvatarColor = (username) => {
  const colors = ['#f56a6a', '#6a5ef5', '#5ef56a', '#f5a623', '#50e3c2', '#9013fe']
  let sum = 0
  for (let i = 0; i < username.length; i++) {
    sum += username.charCodeAt(i)
  }
  const index = sum % colors.length
  return colors[index]
}

const getFirstLetter = (username) => {
  return username.charAt(0).toUpperCase()
}
</script>

<template>
    <div class="home-container">

        <div class="hero-section">
            <h1 class="hero-title">欢迎来到谜匣盲盒商城</h1>
            <p class="hero-subtitle">您的线上盲盒购买平台</p>
            <div class="hero-actions">
              <el-button type="primary" size="large" @click="navigateToProducts">
                浏览商城
              </el-button>
            </div>
        </div>

        <!-- 热门商品区域 -->
        <section class="hot-products-section">
            <div class="section-header">
                <h2><i class="el-icon-fire"></i> 热门盲盒</h2>
                <el-button type="text" @click="navigateToProducts">查看更多 <i class="el-icon-arrow-right"></i></el-button>
            </div>

            <el-skeleton :loading="loading" animated :count="1" :throttle="500">
                <template #default>
                    <div class="product-grid">
                        <div v-for="product in hotProducts" :key="product.id" class="product-card" @click="navigateToProductDetail(product.id)">
                            <div class="product-image">
                                <img :src="product.image" :alt="product.name">
                                <div class="sold-count">已售 {{ product.soldCount }}+</div>
                            </div>
                            <div class="product-info">
                                <h3>{{ product.name }}</h3>
                                <div class="product-price">¥{{ product.price }}</div>
                            </div>
                        </div>
                    </div>
                </template>
                <template #template>
                    <div class="skeleton-products">
                        <div v-for="i in 4" :key="i" class="skeleton-product">
                            <el-skeleton-item variant="image" style="width: 100%; height: 200px;" />
                            <el-skeleton-item variant="p" style="width: 60%" />
                            <el-skeleton-item variant="text" style="width: 30%" />
                        </div>
                    </div>
                </template>
            </el-skeleton>
        </section>

        <!-- 玩家秀精选 -->
        <section class="player-showcase-section">
            <div class="section-header">
                <h2><i class="el-icon-picture"></i> 玩家秀精选</h2>
                <el-button type="text" @click="navigateToPlayerMoments">全部玩家秀 <i class="el-icon-arrow-right"></i></el-button>
            </div>

            <el-skeleton :loading="loading" animated :count="1" :throttle="500">
                <template #default>
                    <el-carousel :interval="4000" type="card" height="300px">
                        <el-carousel-item v-for="item in playerShowcases" :key="item.id">
                            <div class="showcase-card">
                                <div class="showcase-image">
                                    <img :src="item.image" :alt="`玩家${item.username}的分享`">
                                </div>
                                <div class="showcase-overlay">
                                    <div class="showcase-user">
                                        <div class="user-avatar" :style="{ backgroundColor: getAvatarColor(item.username) }">
                                            <span class="avatar-letter">{{ getFirstLetter(item.username) }}</span>
                                        </div>
                                        <span>{{ item.username }}</span>
                                    </div>
                                    <p class="showcase-content">{{ item.content }}</p>
                                    <div class="like-count">
                                        <i class="el-icon-thumb"></i> {{ item.likeCount }}
                                    </div>
                                </div>
                            </div>
                        </el-carousel-item>
                    </el-carousel>
                </template>
                <template #template>
                    <div style="height: 300px">
                        <el-skeleton-item variant="image" style="width: 100%; height: 100%;" />
                    </div>
                </template>
            </el-skeleton>
        </section>

        <main class="content-section">
            <el-card class="intro-card">
                <h2>关于谜匣盲盒商城</h2>
                <p>在这个信息过载、选择疲劳的时代，每一天都仿佛被计划和算法填满，意外之喜变得越来越奢侈。谜匣盲盒商城正是为了对抗这份平淡而诞生。
                </p>

                <h2>我们为什么创建它？</h2>
                <p>
                  因为生活需要未知的乐趣！我们相信，拆开盲盒的瞬间，那份紧张与期待本身，就是一份珍贵的快乐源泉。谜匣盲盒商城致力于为你精心“种植”这份日常的小确幸——通过严选丰富多样、品质可靠的商品，从潮流趣物到实用好物，将它们深藏盒中。
                  每一次购买，都如同亲手埋下一颗惊喜的种子。你永远不知道打开后是时下炙手可热的爆款，还是贴心地解决某个小烦恼的生活用品，抑或是带来意外欢乐的奇妙小物。这种拆盒的惊喜感和可能带来的超高性价比，是我们创造的核心驱动力——让每一次开启都充满悸动，让每一次消费都像拆礼物一样充满期待。
                  不仅如此，谜匣盲盒还是一种情绪的出口和社交的货币。与朋友分享开盒的“战果”，无论是尖叫大笑还是幽默调侃，都能瞬间拉近距离，传递快乐。我们想建立的不是一个简单的购物平台，而是一个承载好奇、分享惊喜的社群乐园。
                </p>
            </el-card>

            <!-- 用户评价区域 -->
            <section class="reviews-section">
                <h2>用户评价</h2>
                <el-skeleton :loading="loading" animated :count="1">
                    <template #default>
                        <div class="reviews-container">
                            <div v-for="(review, index) in userReviews" :key="index" class="review-card">
                                <div class="review-header">
                                    <div class="user-avatar" :style="{ backgroundColor: getAvatarColor(review.username) }">
                                        <span class="avatar-letter">{{ getFirstLetter(review.username) }}</span>
                                    </div>
                                    <div class="review-user-info">
                                        <div class="review-username">{{ review.username }}</div>
                                        <div class="review-date">{{ review.date }}</div>
                                    </div>
                                </div>
                                <div class="review-rating">
                                    <i v-for="i in 5" :key="i" class="el-icon-star-on" :class="{ 'active': i <= review.rating }"></i>
                                </div>
                                <p class="review-content">{{ review.content }}</p>
                            </div>
                        </div>
                    </template>
                    <template #template>
                        <div class="skeleton-reviews">
                            <div v-for="i in 3" :key="i" class="skeleton-review">
                                <div style="display: flex; align-items: center; margin-bottom: 10px;">
                                    <el-skeleton-item variant="circle" style="width: 40px; height: 40px; margin-right: 10px;" />
                                    <div>
                                        <el-skeleton-item variant="text" style="width: 100px; margin-bottom: 5px;" />
                                        <el-skeleton-item variant="text" style="width: 80px;" />
                                    </div>
                                </div>
                                <el-skeleton-item variant="text" style="width: 100px; margin-bottom: 10px;" />
                                <el-skeleton-item variant="p" style="width: 100%;" />
                            </div>
                        </div>
                    </template>
                </el-skeleton>
            </section>

            <el-row :gutter="20" class="service-section">
                <el-col :xs="24" :sm="12">
                    <el-card class="service-card">
                        <div class="service-icon customer-icon">👤</div>
                        <h3>顾客服务</h3>
                        <ul>
                            <li>便捷的注册和登录系统</li>
                            <li>个人信息查看与修改</li>
                            <li>丰富的书籍浏览功能</li>
                            <li>简单高效的购买流程</li>
                            <li>订单跟踪与历史记录</li>
                        </ul>
                    </el-card>
                </el-col>

                <el-col :xs="24" :sm="12">
                    <el-card class="service-card">
                        <div class="service-icon admin-icon">👨‍💼</div>
                        <h3>管理员功能</h3>
                        <ul>
                            <li>全面的盲盒信息管理</li>
                            <li>库存更新与调整</li>
                            <li>订单处理与管理</li>
                            <li>支付流程监控</li>
                            <li>用户数据分析</li>
                        </ul>
                    </el-card>
                </el-col>
            </el-row>

            <el-card class="vision-card">
                <h2>我们的愿景</h2>
              <ul>
                <li>打破日常的刺激感</li>
                <li>超乎预期的获得感</li>
                <li>拆解神秘的仪式感</li>
                <li>易享的社交乐趣</li>
              </ul>
            </el-card>
        </main>

        <footer class="home-footer">
            <div class="footer-content">
                <div class="footer-section">
                    <h3>关于我们</h3>
                    <p>谜匣盲盒商城致力于为用户提供高品质、有趣味的盲盒购物体验</p>
                </div>
                <div class="footer-section">
                    <h3>快速链接</h3>
                    <ul>
                        <li><a @click="navigateToProducts">商品列表</a></li>
                        <li><a @click="navigateToPlayerMoments">玩家秀</a></li>
                        <li><a href="#">常见问题</a></li>
                        <li><a href="#">联系我们</a></li>
                    </ul>
                </div>
                <div class="footer-section">
                    <h3>联系方式</h3>
                    <p><i class="el-icon-location"></i> 南京市栖霞区仙林大道163号</p>
                    <p><i class="el-icon-phone"></i> 400-800-1234</p>
                    <p><i class="el-icon-message"></i> support@potatomall.com</p>
                </div>
            </div>
            <div class="footer-bottom">
                <p>© 2025 谜匣盲盒商城 - 南京大学Web开发</p>
            </div>
        </footer>
    </div>
</template>

<style scoped>
.home-container {
    min-height: 100vh;
    background: linear-gradient(135deg, #eff5f5 0%, #f0f7ff 100%);
}

.hero-section {
    text-align: center;
    padding: 60px 20px;
    background: url('/src/assets/background-pattern.svg') center/cover;
    position: relative;
    overflow: hidden;
}

.hero-title {
    font-size: 3rem;
    color: #333;
    margin-bottom: 20px;
    animation: fadeInUp 1s ease;
}

.hero-subtitle {
    font-size: 1.2rem;
    color: #666;
    margin-bottom: 30px;
    animation: fadeInUp 1.2s ease;
}

.hero-actions {
    animation: fadeInUp 1.4s ease;
}

/* 热门商品样式 */
.hot-products-section, .player-showcase-section {
    padding: 40px 20px;
    max-width: 1200px;
    margin: 0 auto;
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.section-header h2 {
    font-size: 1.8rem;
    color: #333;
    display: flex;
    align-items: center;
}

.section-header h2 i {
    margin-right: 10px;
    color: #f5a623;
}

.product-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 20px;
}

.product-card {
    background: white;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
    transition: transform 0.3s, box-shadow 0.3s;
    cursor: pointer;
}

.product-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 16px rgba(0,0,0,0.15);
}

.product-image {
    position: relative;
    height: 200px;
    overflow: hidden;
}

.product-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.5s;
}

.product-card:hover .product-image img {
    transform: scale(1.05);
}

.sold-count {
    position: absolute;
    bottom: 0;
    right: 0;
    background: rgba(0,0,0,0.7);
    color: white;
    padding: 4px 8px;
    font-size: 0.8rem;
    border-top-left-radius: 8px;
}

.product-info {
    padding: 15px;
}

.product-info h3 {
    margin: 0 0 10px;
    font-size: 1.1rem;
    color: #333;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.product-price {
    font-size: 1.2rem;
    color: #f56c6c;
    font-weight: bold;
}

/* 玩家秀样式 */
.showcase-card {
    position: relative;
    height: 100%;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 4px 12px rgba(0,0,0,0.2);
}

.showcase-image {
    height: 100%;
}

.showcase-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.showcase-overlay {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 15px;
    background: linear-gradient(transparent, rgba(0,0,0,0.7));
    color: white;
}

.showcase-user {
    display: flex;
    align-items: center;
    margin-bottom: 8px;
}

.user-avatar {
    width: 30px;
    height: 30px;
    border-radius: 50%;
    margin-right: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-weight: bold;
    font-size: 14px;
}

.avatar-letter {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
}

.showcase-content {
    margin-bottom: 10px;
    font-size: 0.9rem;
}

.like-count {
    font-size: 0.8rem;
    display: flex;
    align-items: center;
}

.like-count i {
    margin-right: 5px;
}

/* 用户评价样式 */
.reviews-section {
    margin: 40px 0;
}

.reviews-section h2 {
    margin-bottom: 20px;
    font-size: 1.5rem;
    color: #333;
    text-align: center;
}

.reviews-container {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 20px;
}

.review-card {
    background: white;
    border-radius: 8px;
    padding: 15px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.review-header {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
}

.review-user-info {
    flex: 1;
}

.review-username {
    font-weight: bold;
    color: #333;
}

.review-date {
    font-size: 0.8rem;
    color: #999;
}

.review-rating {
    margin-bottom: 10px;
    color: #dcdcdc;
}

.review-rating i.active {
    color: #f5a623;
}

.review-content {
    color: #666;
    font-size: 0.9rem;
    line-height: 1.5;
}

/* 内容部分样式 */
.content-section {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
}

/* 页脚样式 */
.home-footer {
    background: #2c3e50;
    color: #ecf0f1;
    padding-top: 40px;
}

.footer-content {
    max-width: 1200px;
    margin: 0 auto;
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 30px;
    padding: 0 20px;
}

.footer-section h3 {
    margin-bottom: 15px;
    position: relative;
    padding-bottom: 10px;
    color: #ecf0f1;
}

.footer-section h3:after {
    content: '';
    position: absolute;
    left: 0;
    bottom: 0;
    height: 2px;
    width: 40px;
    background: #f5a623;
}

.footer-section ul {
    list-style: none;
    padding: 0;
    margin: 0;
}

.footer-section ul li {
    margin-bottom: 8px;
}

.footer-section a {
    color: #bdc3c7;
    text-decoration: none;
    transition: color 0.3s;
    cursor: pointer;
}

.footer-section a:hover {
    color: #f5a623;
}

.footer-section p {
    margin-bottom: 10px;
    color: #bdc3c7;
}

.footer-section i {
    margin-right: 10px;
    color: #f5a623;
}

.footer-bottom {
    background: #1a252f;
    padding: 15px 0;
    margin-top: 40px;
    text-align: center;
    font-size: 0.9rem;
}

/* 骨架屏样式 */
.skeleton-products {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 20px;
}

.skeleton-product {
    padding: 15px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.skeleton-reviews {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 20px;
}

.skeleton-review {
    background: white;
    border-radius: 8px;
    padding: 15px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

/* 动画 */
@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* 响应式样式 */
@media (max-width: 768px) {
    .hero-title {
        font-size: 2.2rem;
    }

    .product-grid, .reviews-container {
        grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    }
}
</style>