<template>
  <buyer-header></buyer-header>
  <div style="display: flex">
    <ShopLogo></ShopLogo>
    <div class="info-header">
      <el-button type="default" @click="goBack">返回</el-button>
    </div>
  </div>
  <div v-if="isLoading" class="loading-overlay">
    {{ $t('order.messages.loading') }}
  </div>


  <div class="product-list">
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
        <el-tooltip effect="light" :content="isTranslated ? product.gname_en : product.gname" placement="top">
          <h3>
            {{
              (product.gname.length > 4 ? product.gname.substring(0,4)+'...' : product.gname)
            }}
          </h3>
        </el-tooltip>
        <p class="price">¥{{ product.gvalue }}</p>
        <p class="description">
          {{ product.cleanDesc }}
        </p>
        <p class="stock">
          {{ isTranslated ? 'Stock' : '库存' }}: {{ product.gstock }} {{ isTranslated ? '' : '件' }}
        </p>
      </div>
    </el-card>
  </div>
</template>

<script>
import axios from "axios";
import {onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import BuyerHeader from "@/components/block-buyer/buyer-header.vue";
import ShopLogo from "@/components/block/Shop-Logo.vue";
import router from "@/router/index.js";
import http from "@/utils/re.js";
export default {
  components: {ShopLogo, BuyerHeader},
  setup() {
    const route = useRoute();
    const query = route.params.query;
    const isLoading = ref(true);
    console.log('Search query:', query);
    const token = localStorage.getItem('token');
    const products = ref([]); // 用于存储商品数据
    const stripHtml = (html) => {
      const tmp = document.createElement('div');
      tmp.innerHTML = html;
      return tmp.textContent || tmp.innerText || '';
    };
    //缩写
    const fetchSummarizedDesc = async (text) => {
      try {
        const cleanText = stripHtml(text);
        // 添加长度判断
        if (cleanText.length <= 20) {
          return cleanText.slice(0, 50); // 直接返回处理后的短文本
        }

        const response = await http.post('/deepseek/summarize',
          { text: stripHtml(text) }, // 先去除HTML标签
          {
            headers: {
              'Authorization': `${token}`,
              'Content-Type': 'application/json'
            }
          }
        );
        return response.data.data;
      } catch (error) {
        console.error('文本缩写失败，使用本地处理:', error);
        return stripHtml(text).slice(0, 50) + '...';
      }
    };

    const fetchProductPictures = async (gid) => {
      try {
        const picMap = { gid: gid };
        const response = await http.post('/pic/list_pic', picMap, {
          headers: {
            'Authorization': `${token}`,
          }
        });
        // 遍历返回的Map对象并获取Base64编码图片数据
        return Object.entries(response.data).map(([picId, base64Data]) => {
          return `data:image/jpeg;base64,${base64Data}`;
        });
      } catch (error) {
        console.error('获取商品图片失败:', error);
        return [];
      }
    };

    const searchProducts = async () => {
      try {
        isLoading.value = true;
        const response = await http.post('/goods/list_by_name', {
          name: query // 传入搜索框中的内容
        }, {
          headers: {
            'Authorization': `${token}`,
          }
        });
        products.value = await Promise.all(response.data.map(async product => {
          const pictures = await fetchProductPictures(product.gid);
          const summarizedDesc = product.gdesc ? await fetchSummarizedDesc(product.gdesc) : '';

          return {
            ...product,
            pictures,
            cleanDesc: summarizedDesc
          };
        })); // 确保这里赋值给 products.value
        console.log("获得商品：", response.data);
      } catch (error) {
        console.error('搜索商品信息失败:', error);
      } finally {
        isLoading.value = false; // 结束加载
      }
    };
    const goToProductDetail = (gid) => {
      const route = {
        name: 'BProductDet', // 详情页面的路由名称
        params: { pid: gid }
      };
      router.push(route);
    };
    onMounted(() => {
      searchProducts();
    });
    const goBack = () => {
      router.go(-1);
    };
    return {
      products,
      isLoading,
      goToProductDetail,
      goBack
    };
  }
};
</script>

<style scoped>
.product-page {
  display: flex;
  flex-direction: column;
}

.product-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
  margin: 0 200px;
  position: relative;
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


.el-carousel {
  height: 200px; /* 保持与 template 中的高度一致 */
}

.el-carousel__item {
  display: flex;
  justify-content: center;
  align-items: center;
}



.product-info p {
  margin: 4px 0;  /* 重置默认边距 */
  padding: 0;
  font-size: 14px;
}

.info-header {
  padding: 10px;
  font-size: 16px;
  margin-left: auto;
}
.loading-overlay {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 20px 40px;
  border-radius: 8px;
  z-index: 9999;
  font-size: 18px;
}
</style>
