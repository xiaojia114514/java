<template>
  <div class="search-container">
    <el-input
      :placeholder="$t(isTranslated ? 'translate.search.placeholder_en' : 'translate.search.placeholder_zh')"
      v-model="searchQuery"
      @keyup.enter="searchProducts"
      style="flex-grow: 1; margin-right: 10px;">
      <template #append>
        <el-button
          icon="el-icon-search"
          @click="searchProducts"
          :loading="loading">{{ $t(isTranslated ? 'translate.search.button_en' : 'translate.search.button_zh') }}
        </el-button>
      </template>
    </el-input>

    <!-- 新增全局加载状态 -->
    <div v-if="loading" class="loading-overlay">
      <el-icon class="is-loading"><Loading /></el-icon>
      <span>加载中...</span>
    </div>
  </div>
</template>

<script>
import { defineComponent, ref } from 'vue';
import axios from 'axios';
import http from "@/utils/re.js";
const token = localStorage.getItem('token');
const loading = ref(false);

export default defineComponent({
  props: {  // 新增 props 接收翻译状态
    isTranslated: {
      type: Boolean,
      default: false
    }
  },
  setup(props, { emit }) {
    const searchQuery = ref(''); // 用于存储搜索查询
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
        // 新增长度判断
        if (cleanText.length <= 20) {
          return cleanText.slice(0, 50); // 直接返回处理后的短文本
        }
        const response = await http.post('/deepseek/summarize',
          { text: stripHtml(text) },
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

    // 获取商品图片的函数
    const fetchProductPictures = async (gid) => {
      try {
        const picMap = { gid: gid };
        const response = await http.post('/pic/list_pic', picMap, {
          headers: {
            'Authorization': `${token}`,
          }
        });
        console.log('图片接口返回数据:', response.data);
        return Object.values(response.data).map(base64Data =>
          `data:image/jpeg;base64,${base64Data}`
        );
      } catch (error) {
        console.error('获取商品图片失败:', error);
        return [];
      }
    };
    // 获取商品信息的函数
    const searchProducts = async () => {
      loading.value = true;
      try {
        const response = await http.post('/goods/list_by_name', {
          name: searchQuery.value // 传入搜索框中的内容
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
        emit('update:products', products.value);
      } catch (error) {
        console.error('搜索商品信息失败:', error);
      } finally {
        loading.value = false;
      }
    };

    return {
      loading,
      products,
      searchQuery,
      searchProducts
    };
  }
});
</script>

<style scoped>
.search-container {
  position: relative;
  min-height: 60px;
}
</style>
