<template>
  <el-main class="product-list-container">
    <!-- Loading state in product list container -->
    <div v-if="loading" class="loading-overlay">
      <el-icon class="is-loading"><Loading /></el-icon>
      <span>{{ $t(isTranslated ? 'order.messages.loading_en' : 'order.messages.loading_zh') }}</span>
    </div>
    <!-- Container for search box and add product button -->
    <el-card class="search-card">
      <div class="search-container">
        <div class="search-wrapper">
          <goods-search @update:products="handleProductsUpdate" :is-translated="isTranslated"></goods-search>
        </div>
        <el-button
          type="primary"
          @click="goToAddProduct"
          class="action-button"
          icon="Plus">
          {{ $t(isTranslated ? 'order.actions.add_en' : 'order.actions.add_zh') }}
        </el-button>
      </div>
    </el-card>

    <!-- Scrollbar component wrapping product list -->
    <el-scrollbar class="product-list-scrollbar">
      <div class="product-list">
        <el-card v-for="product in products" :key="product.gid" class="product-card">
          <!-- Carousel component -->
          <el-carousel :interval="5000" arrow="always" height="200px">
            <el-carousel-item v-for="(pic, index) in product.pictures" :key="index">
              <img :src="pic" class="product-image" />
            </el-carousel-item>
          </el-carousel>
          <div class="product-info">
            <h3>{{ product.gname }}</h3>
            <p>
              {{ $t(isTranslated ? 'product.price_en' : 'product.price_zh') }}:
              {{ product.gvalue }}
              {{ $t(isTranslated ? 'product.unit_en' : 'product.unit_zh') }}
              {{ $t(isTranslated ? 'product.stock_en' : 'product.stock_zh') }}:
              {{ product.gstock }}
            </p>
            <p class="product-desc" v-if="product.cleanDesc">{{ product.cleanDesc }}</p>
          </div>
          <el-button type="text" @click="viewDetails(product)">
            {{ $t(isTranslated ? 'order.actions.view_en' : 'order.actions.view_zh') }}
          </el-button>
        </el-card>
      </div>
    </el-scrollbar>
  </el-main>
</template>

<script>
import { defineComponent, ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
import { ElMessage } from 'element-plus';
import GoodsSearch from "@/components/block-search/Goods-Search.vue";
import {jwtDecode} from "jwt-decode";
import {Loading} from "@element-plus/icons-vue";
import http from "@/utils/re.js";

export default defineComponent({
  components: {Loading, GoodsSearch },
  props: {
    isTranslated: {
      type: Boolean,
      default: false,
    },
  },
  setup() {
    const { t } = useI18n();
    return { t };
  },
  data() {
    return {
      products: [],
      token: localStorage.getItem('token'),
      claims: jwtDecode(localStorage.getItem('token')),
      loading: true
    };
  },
  methods: {
    stripHtml(html) {
      const tmp = document.createElement('div');
      tmp.innerHTML = html;
      return tmp.textContent || tmp.innerText || '';
    },

    async fetchSummarizedDesc(text) {
      try {
        const cleanText = this.stripHtml(text);
        if (cleanText.length <= 20) {
          return cleanText.slice(0, 50);
        }
        const response = await http.post('/deepseek/summarize',
          { text: this.stripHtml(text) },
          {
            headers: {
              'Authorization': `${this.token}`,
              'Content-Type': 'application/json'
            }
          }
        );
        return response.data.data;
      } catch (error) {
        console.error(this.t(
          this.isTranslated
            ? 'order.messages.operationFailed_en'
            : 'order.messages.operationFailed_zh'
        ), error);
        return this.stripHtml(text).slice(0, 50) + '...';
      }
    },

    async fetchProductPictures(gid) {
      try {
        const picMap = { gid: gid };
        const response = await http.post('/pic/list_pic', picMap, {
          headers: {
            'Authorization': `${this.token}`,
          }
        });

        return Object.entries(response.data).map(([picId, base64Data]) => {
          return `data:image/jpeg;base64,${base64Data}`;
        });

      } catch (error) {
        console.error(this.t(
          this.isTranslated
            ? 'order.messages.fetchProductsFailed_en'
            : 'order.messages.fetchProductsFailed_zh'
        ), error);
        ElMessage.error(
          this.t(
            this.isTranslated
              ? "order.messages.fetchProductsFailed_en"
              : "order.messages.fetchProductsFailed_zh"
          )
        );
        return [];
      }
    },

    async fetchProducts() {
      try {
        this.loading = true;
        const data = {
          gseller: this.claims.account,
        }
        const response = await http.post('/goods/list', data, {
          headers: {
            'Authorization': `${this.token}`,
          }
        });

        this.products = await Promise.all(response.data.map(async product => {
          const pictures = await this.fetchProductPictures(product.gid);
          const summarizedDesc = product.gdesc ? await this.fetchSummarizedDesc(product.gdesc) : '';
          return {
            ...product,
            pictures,
            cleanDesc: summarizedDesc
          };
        }));
      } catch (error) {
        console.error(this.t(
          this.isTranslated
            ? 'order.messages.fetchProductsFailed_en'
            : 'order.messages.fetchProductsFailed_zh'
        ), error);
        ElMessage.error(
          this.t(
            this.isTranslated
              ? "order.messages.fetchProductsFailed_en"
              : "order.messages.fetchProductsFailed_zh"
          )
        );
      } finally {
        this.loading = false;
      }
    },

    handleProductsUpdate(newProducts) {
      this.products = newProducts;
    },

    viewDetails(product) {
      const id = product.gid;
      this.$router.push({
        name: 'AProductDet',
        params: { pid: id }
      });
    },

    goToAddProduct() {
      this.$router.push({ path: '/aHome/newProduct', name: 'NewProduct' });
    }
  },
  created() {
    this.fetchProducts();
  }
});
</script>


<style scoped>
.product-list-container {
  height: calc(100vh - 60px);
  padding: 20px;
  background-color: #f5f7fa;
}

.product-list-scrollbar {
  height: calc(100% - 100px);
  overflow-y: auto;
  margin-top: 20px;
}

/* 搜索区域卡片样式 */
.search-card {
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border: none;
  margin-bottom: 0;
}

.search-container {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 5px 0;
}

.search-wrapper {
  flex-grow: 1;
}

/* 统一按钮样式 */
.action-button {
  border-radius: 24px;
  padding: 12px 28px;
  font-weight: 500;
  letter-spacing: 0.5px;
  box-shadow: 0 4px 6px rgba(64, 158, 255, 0.3);
  transition: all 0.3s ease;
  height: 42px;
}

.action-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(64, 158, 255, 0.4);
}

/* 美化搜索输入框 */
:deep(.el-input__wrapper) {
  border-radius: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08) !important;
  padding: 2px 15px;
  height: 42px;
  transition: all 0.3s;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 12px rgba(64, 158, 255, 0.2) !important;
}

:deep(.el-input__inner) {
  font-size: 14px;
  color: #333;
}

/* 产品列表样式 */
.product-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  padding: 5px;
}

.product-card {
  width: calc(33.333% - 20px);
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s, box-shadow 0.3s;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08);
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  display: block;
}

.product-info {
  padding: 15px;
}

.product-info h3 {
  margin: 0 0 10px;
  font-size: 16px;
  color: #333;
}

.product-info p {
  margin: 5px 0;
  font-size: 14px;
  color: #666;
}

.product-desc {
  color: #888;
  font-size: 13px;
  line-height: 1.5;
  margin-top: 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.detail-button {
  width: 100%;
  text-align: center;
  padding: 10px 0;
  color: #409eff;
  font-weight: 500;
  transition: color 0.3s;
}

.detail-button:hover {
  color: #66b1ff;
}

/* 加载动画 */
.loading-overlay {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  align-items: center;
  gap: 10px;
  background: rgba(255, 255, 255, 0.9);
  padding: 20px 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  z-index: 2000;
}

.el-icon.is-loading {
  animation: rotating 2s linear infinite;
  font-size: 24px;
}

@keyframes rotating {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .product-card {
    width: calc(50% - 20px);
  }
}

@media (max-width: 768px) {
  .search-container {
    flex-direction: column;
    align-items: stretch;
  }

  .action-button {
    width: 100%;
    margin-top: 10px;
  }

  .product-card {
    width: 100%;
  }
}
</style>
