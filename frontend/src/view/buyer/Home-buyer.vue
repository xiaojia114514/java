<template>
  <buyer-header :is-translated="isTranslated"></buyer-header>
  <div style="display: flex">
    <ShopLogo></ShopLogo>
    <Goodssearch :is-translated="isTranslated"></Goodssearch>
  </div>

  <!-- 一级分类 -->
  <div class="filters">
    <div class="category-filter">
      <div
        class="filter-tag"
        v-for="category in topCategories"
        :key="category.cid"
        :class="{ active: selectedCategory === category.cid }"
        @click="selectCategory(category.cid)"
      >
        {{ category.cname }}
      </div>
    </div>
  </div>

  <!-- 二级分类 -->
  <div class="subfilters" v-if="selectedSubCategory.length">
    <div class="category-filter">
      <div
        class="filter-subtag"
        v-for="(subcategory, index) in selectedSubCategory"
        :key="subcategory.cid"
        :class="{ active: currentSubCategory === subcategory.cid }"
        @click="selectSubCategory(subcategory.cid)"
      >
        {{
            (index === 0 ? '全部' : subcategory.cname)
        }}
      </div>
    </div>
  </div>

  <!-- 商品列表 -->
  <div class="product-list">
    <div v-if="isLoading" class="loading-overlay">
      <div class="loader"></div>
    </div>
    <el-card
      v-for="product in products"
      :key="product.gid"
      class="product-card"
      @click="goToProductDetail(product.gid)"
    >
      <el-carousel :interval="5000" arrow="always" height="200px">
        <el-carousel-item v-for="(pic, index) in product.pictures" :key="index">
          <img :src="pic" class="product-image" />
        </el-carousel-item>
      </el-carousel>
      <div class="product-info">
        <el-tooltip effect="light" :content="product.gname" placement="top">
          <h3>
            {{
                (product.gname.length > 12 ? product.gname.substring(0,8)+'...' : product.gname)
            }}
          </h3>
        </el-tooltip>
        <p class="price">¥{{ product.gvalue }}</p>
        <p class="description">
          {{ product.cleanDesc }}
        </p>
        <p class="stock">
          {{ $t(isTranslated ? 'product.stock_en' : 'product.stock_zh' ) }}: {{ product.gstock }} {{ $t(isTranslated ? 'product.unit_en' : 'product.unit_zh' ) }}
        </p>
      </div>
    </el-card>
  </div>

  <TranslateButton
    v-model:isTranslated="isTranslated"
    class="floating-translate-btn"
  />
</template>

<script>
import ShopLogo from "@/components/block/Shop-Logo.vue";
import BuyerHeader from "@/components/block-buyer/buyer-header.vue";
import Goodssearch from "@/components/block-search/buyer-search.vue";
import TranslateButton from "@/components/block/translate.vue";
import { defineComponent, onMounted, ref, computed } from "vue";
import { useRouter } from "vue-router";
import { ElTooltip } from 'element-plus';
import 'element-plus/dist/index.css';
import http from "@/utils/re.js";
import { useI18n } from 'vue-i18n';
export default defineComponent({
  components: {
    Goodssearch,
    BuyerHeader,
    ShopLogo,
    ElTooltip,
    TranslateButton
  },
  setup() {
    const token = localStorage.getItem('token');
    const router = useRouter();
    const isLoading = ref(true);
    // 响应式数据
    const categories = ref([]);
    const selectedCategory = ref(null);
    const selectedSubCategory = ref([]);
    const currentSubCategory = ref(null);
    const products = ref([]);
    const isTranslated = ref(false);

    // 计算属性
    const topCategories = computed(() => categories.value);

    // 方法
    const goToProductDetail = (gid) => {
      router.push({ name: 'BProductDet', params: { pid: gid } });
    };

    // 获取商品图片
    const fetchProductPictures = async (gid) => {
      try {
        const picMap = { gid: gid };
        const response = await http.post('/pic/list_pic', picMap, {
          headers: { 'Authorization': `${token}` }
        });

        return Object.entries(response.data).map(([picId, base64Data]) => {
          return `data:image/jpeg;base64,${base64Data}`;
        });
      } catch (error) {
        console.error('获取商品图片失败:', error);
        return [];
      }
    };

    // 获取分类
    const fetchCategories = async () => {
      try {
        const response = await http.post(
          '/cat/list_cid_by_parent',
          { id: [-1, 0] },
          { headers: { Authorization: token } }
        );

        const detailedCategories = await Promise.all(
          response.data.map(async cid => {
            const info = await http.get(
              `/cat/get_by_cid/${cid}`,
              { headers: { Authorization: token } }
            );
            return info.data;
          })
        );

        categories.value = detailedCategories;
        if (detailedCategories.length) {
          await selectCategory(detailedCategories[0].cid);
        }
      } catch (error) {
        console.error('获取分类失败:', error);
      }
    };

    // 选择分类
    const selectCategory = async (cid) => {
      selectedCategory.value = cid;
      currentSubCategory.value = null;
      await fetchSubCategories(cid);
    };

    // 获取子分类
    const fetchSubCategories = async (cid) => {
      try {
        const response = await http.post(
          '/cat/list_all_child',
          { id: [cid] },
          { headers: { Authorization: token } }
        );

        const subCats = await Promise.all(
          response.data.map(async subCid => {
            const info = await http.get(
              `/cat/get_by_cid/${subCid}`,
              { headers: { Authorization: token } }
            );
            return info.data;
          })
        );

        const currentCategory = categories.value.find(cat => cat.cid === cid);
        selectedSubCategory.value = currentCategory.cname === '全部' ?
          subCats :
          [{ cid: 'all', cname: '全部', cname_en: 'All' }, ...subCats];

        if (subCats.length) {
          selectSubCategory('all');
        }
      } catch (error) {
        console.error('获取子分类失败:', error);
      }
    };

    // 选择子分类
    const selectSubCategory = (sid) => {
      currentSubCategory.value = sid;
      fetchProducts(sid === 'all' ? selectedCategory.value : sid);
    };

    // 获取商品
    const fetchProducts = async (cid) => {
      try {
        const response = await http.post(
          '/goods/list_By_Category',
          { cid: [cid] },
          { headers: { Authorization: token } }
        );

        products.value = await Promise.all(
          response.data.filter(p => p.gshelf === 1).map(async product => ({
            ...product,
            pictures: await fetchProductPictures(product.gid),
            cleanDesc: await fetchSummarizedDesc(product)
          }))
        );
        isLoading.value = false; // 加载完成
      } catch (error) {
        console.error('获取商品失败:', error);
      }
    };

    const stripHtml = (html) => {
      const tmp = document.createElement('div');
      tmp.innerHTML = html;
      return tmp.textContent || tmp.innerText || '';
    };

    const fetchSummarizedDesc = async (product) => {
      try {
        const cleanText = stripHtml(product.gdesc || '');
        // 生成唯一缓存标识（商品ID+处理后的文本前20字符）
        const cacheKey = `desc_${product.gid}_${cleanText.slice(0,20)}`;
        // 尝试获取缓存
        const cached = sessionStorage.getItem(cacheKey);
        if (cached) {
          return JSON.parse(cached).result;
        }
        // 新增长度判断
        if (cleanText.length <= 20) {
          const result = cleanText.slice(0, 50);
          sessionStorage.setItem(cacheKey, JSON.stringify({
            result,
            timestamp: Date.now()
          }));
          return result;
        }
        const response = await http.post('/deepseek/summarize',
          { text: stripHtml(product.gdesc) },
          {
            headers: {
              'Authorization': `${token}`,
              'Content-Type': 'application/json'
            }
          }
        );
        sessionStorage.setItem(cacheKey, JSON.stringify({
          result: response.data.data,
          timestamp: Date.now()
        }));
        isLoading.value = false; // 加载完成
        return response.data.data;
      } catch (error) {
        console.error('文本缩写失败，使用本地处理:', error);
        return stripHtml(product.gdesc).slice(0, 50) + '...';
      }
    };

    // 切换翻译状态
    const toggleTranslation = () => {
      isTranslated.value = !isTranslated.value;
    };

    // 初始化
    onMounted(() => {
      const now = Date.now();
      Object.keys(sessionStorage).forEach(key => {
        if (key.startsWith('desc_')) {
          const data = JSON.parse(sessionStorage.getItem(key));
          if (now - data.timestamp > 3600000) { // 1小时有效期
            sessionStorage.removeItem(key);
          }
        }
      });
      fetchCategories();
    });

    return {
      products,
      topCategories,
      selectedCategory,
      selectedSubCategory,
      currentSubCategory,
      selectCategory,
      selectSubCategory,
      goToProductDetail,
      isTranslated,
      toggleTranslation,
      isLoading
    };
  }
});
</script>

<style scoped>
.product-page {
  max-width: 1280px;
  margin: 0 auto;
  padding: 20px;
}

.filters, .subfilters {
  padding: 16px 24px;
  background: #ffffff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  border-radius: 8px;
  margin: 20px 0;
}

.category-filter {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.filter-tag {
  padding: 8px 20px;
  border-radius: 20px;
  background: #f5f6f7;
  color: #606266;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid transparent;
}

.filter-tag:hover {
  background: #e1e3e7;
  transform: translateY(-1px);
}

.filter-tag.active {
  background: #409eff;
  color: white;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
}

.filter-subtag {
  padding: 6px 16px;
  border-radius: 16px;
  background: #f8f9fa;
  color: #909399;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s ease;
  border: 1px solid #ebeef5;
}

.filter-subtag:hover {
  color: #409eff;
  border-color: #c6e2ff;
  background: #ecf5ff;
}

.filter-subtag.active {
  background: #409eff;
  color: white;
  border-color: #409eff;
}

.product-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
  margin: 0 200px;
  position: relative; /* 添加相对定位 */
  min-height: 400px;
}

.product-card {
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.1);
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.product-info {
  padding: 16px;
}

.product-info h3 {
  font-size: 16px;
  margin: 0 0 8px;
  color: #303133;
  height: 24px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: 600;
  margin: 8px 0;
}

.stock {
  color: #909399;
  font-size: 12px;
  margin: 4px 0;
}

.el-carousel__arrow {
  background: rgba(255, 255, 255, 0.8) !important;
  color: #606266 !important;
}

.loading-overlay {
  position: absolute; /* 改为绝对定位 */
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
  border-radius: 12px; /* 添加圆角匹配商品列表 */
}

.loader {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #409eff;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@media (max-width: 768px) {
  .product-list {
    margin: 0 20px;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 15px;
  }

  .filter-tag {
    padding: 6px 15px;
    font-size: 13px;
  }

  .filter-subtag {
    padding: 5px 12px;
    font-size: 12px;
  }
}
</style>
