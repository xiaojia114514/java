<template>
  <div style="height: 100%; display: flex;">
    <ShopLogo style="background-color: #ffffff"></ShopLogo>
    <el-header style="width: 82%; text-align: right; font-size: 12px; display: flex; align-items: center; height: 80px; background-color: var(--el-color-primary-light-9);">
      <admin-head :is-translated="isTranslated"></admin-head>
    </el-header>
  </div>
  <el-main class="product-detail-container">
    <el-card v-if="product" class="product-card">
      <div class="product-layout">
        <div class="product-image-container">
          <img :src="currentPicture"/>
        </div>
        <div class="product-info">
          <div class="info-header">
            <el-button type="default" @click="goBack">{{ $t(isTranslated ? 'translate.detail.back_en' : 'translate.detail.back_zh') }}</el-button>
          </div>
          <div class="info-content">
            <h3>{{ displayName }}</h3>
            <p>{{ $t(isTranslated ? 'translate.detail.price_en' : 'translate.detail.price_zh') }}: ￥{{ product.gvalue }}</p>
            <p>{{ $t(isTranslated ? 'translate.detail.stock_en' : 'translate.detail.stock_zh') }}: {{ product.gstock }}</p>
            <el-button type="primary" @click="editProduct(product.gid)">
              {{ $t(isTranslated ? 'translate.detail.edit_en' : 'translate.detail.edit_zh') }}
            </el-button>
          </div>
        </div>
      </div>
      <div class="preview-container">
        <img
          v-for="(pic, index) in product.pictures"
          :key="index"
          :src="pic"
          class="preview-image"
          :class="{ active: currentPicture === pic }"
          @click="selectPicture(index)"
        />
      </div>
      <div class="product-description">
        <div v-html="displayDescription"></div>
      </div>
    </el-card>
  </el-main>

  <!-- 翻译状态提示 -->
  <div v-if="translationLoading" class="translation-loading">
    <el-icon class="is-loading"><Loading /></el-icon>
    <span>{{ $t(isTranslated ? 'translate.detail.translating_en' : 'translate.detail.translating_zh') }}</span>
  </div>

  <el-tooltip effect="light" :content="$t(isTranslated ? 'translate.detail.switch_zh' : 'translate.detail.translate_en')" placement="left">
    <el-button
      class="floating-translate-btn"
      :class="{ 'is-translated': isTranslated }"
      @click="toggleTranslation"
      :loading="translationLoading"
      circle
    >
      {{ isTranslated ? 'ZH' : 'EN' }}
    </el-button>
  </el-tooltip>
</template>

<script>
import { useI18n } from 'vue-i18n';
import { defineComponent, ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import http from '@/utils/re.js';
import ShopLogo from "@/components/block/Shop-Logo.vue";
import AdminHead from "@/components/block-admin/adminHead.vue";
import {Loading} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";

export default defineComponent({
  components: {Loading, AdminHead, ShopLogo },
  setup() {
    const product = ref(null);
    const currentPicture = ref('');
    const route = useRoute();
    const router = useRouter();
    const token = localStorage.getItem('token');
    const gid = Number(route.params.pid);

    // 翻译相关状态
    const isTranslated = ref(false);
    const translationLoading = ref(false);
    const translatedName = ref('');
    const translatedDescription = ref('');

    // 计算显示的商品名称
    const displayName = computed(() => {
      if (isTranslated.value && translatedName.value) {
        return translatedName.value;
      }
      return product.value?.gname || '';
    });

    // 计算显示的商品描述
    const displayDescription = computed(() => {
      if (isTranslated.value && translatedDescription.value) {
        return translatedDescription.value;
      }
      return product.value?.gdesc || '';
    });

    const fetchProductData = async (gid) => {
      try {
        const data = {
          gid: gid
        }
        const response = await http.post(`/goods/list`, data, {
          headers: {
            'Authorization': `${token}`,
          }
        });
        if (response.data.length > 0) {
          const fetchedProduct = response.data[0];
          const pictures = await fetchProductPictures(fetchedProduct.gid);
          product.value = { ...fetchedProduct, pictures };
          currentPicture.value = pictures[0];
        }
      } catch (error) {
        console.error("获取商品详情失败:", error);
      }
    };

    const fetchProductPictures = async (gid) => {
      try {
        const response = await http.post('/pic/list_pic', { gid }, {
          headers: {
            'Authorization': `${token}`,
          }
        });
        return Object.entries(response.data).map(([picId, base64Data]) => {
          return `data:image/jpeg;base64,${base64Data}`;
        });
      } catch (error) {
        console.error('获取商品图片失败:', error);
        return [];
      }
    };

    const goBack = () => {
      router.push({ name: 'AHome', query: { token } });
    };

    const editProduct = (gid) => {
      if (gid) {
        router.push({ name: 'UpdateGood', params: { pid: gid } });
      } else {
        console.error('商品ID未找到');
      }
    };

    const selectPicture = (index) => {
      currentPicture.value = product.value.pictures[index];
    };

    // 翻译文本
    const translateText = async (html) => {
      if (!html) return '';

      try {
        // 创建临时DOM元素来解析HTML
        const tempDiv = document.createElement('div');
        tempDiv.innerHTML = html;

        // 提取所有文本节点
        const textNodes = [];
        const walker = document.createTreeWalker(
          tempDiv,
          NodeFilter.SHOW_TEXT,
          null,
          false
        );

        let node;
        while (node = walker.nextNode()) {
          if (node.nodeValue.trim()) {
            textNodes.push(node);
          }
        }

        // 逐个翻译文本节点
        for (const node of textNodes) {
          const response = await http.post(
            '/deepseek/translate',
            { text: node.nodeValue },
            { headers: { Authorization: token } }
          );
          if (response.data.code === 200) {
            console.log('翻译成功:', response.data.data)
            node.nodeValue = response.data.data;
          }
        }

        return tempDiv.innerHTML;
      } catch (error) {
        console.error('翻译失败:', error);
        return html;
      }
    };

    // 切换翻译状态
    const toggleTranslation = async () => {
      if (translationLoading.value) return;

      translationLoading.value = true;

      try {
        if (!isTranslated.value) {
          // 首次翻译：获取翻译结果
          if (product.value) {
            if (!translatedName.value) {
              translatedName.value = await translateText(product.value.gname);
            }
            if (!translatedDescription.value) {
              translatedDescription.value = await translateText(product.value.gdesc);
            }
          }
        }

        isTranslated.value = !isTranslated.value;
      } catch (error) {
        console.error('翻译切换失败:', error);
        ElMessage.error(isTranslated.value ? 'Translation failed' : '翻译失败');
      } finally {
        translationLoading.value = false;
      }
    };

    onMounted(() => {
      if (!isNaN(gid)) {
        fetchProductData(gid);
      } else {
        console.error('商品ID无效');
      }
    });

    return {
      product,
      currentPicture,
      goBack,
      editProduct,
      selectPicture,
      isTranslated,
      translationLoading,
      toggleTranslation,
      displayName,
      displayDescription
    };
  }
});
</script>

<style scoped>
body {
  font-family: 'Helvetica', 'Arial', sans-serif;
}

.product-image-container {
  position: relative;
  width: 100%;
  height: 500px;
  overflow: hidden;
}

.product-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

.product-card {
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 100%;
}

.product-layout {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  width: 100%;
  gap: 20px;
}

.product-image-container {
  flex: 1;
}

.product-info {
  flex: 1;
  padding: 20px;
}

.info-header {
  margin-bottom: 10px;
  display: flex;
  justify-content: flex-end;
}

.product-info h3 {
  font-size: 24px;
  color: #333;
  margin: 0;
}

.product-info p {
  font-size: 16px;
  color: #666;
  margin: 5px 0;
}

.product-description {
  margin-top: 20px;
  padding: 20px;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  background-color: #fff;
  font-size: 16px;
  color: #444;
  line-height: 1.6;
}

.back-button-container {
  margin-left: auto;
}

.preview-container {
  display: flex;
  overflow-x: auto;
  gap: 10px;
  margin-top: 10px;
  padding: 0 10px;
}

.preview-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 4px;
  cursor: pointer;
  border: 2px solid transparent;
  transition: border 0.3s;
}

.preview-image:hover, .preview-image.active {
  border: 2px solid #409EFF;
}

.el-button {
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  transition: background-color 0.3s, box-shadow 0.3s;
}

.el-button:hover {
  background-color: #0056b3;
  box-shadow: 0 4px 8px rgba(0,0,0,0.2);
}

.el-card {
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.product-info .info-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.product-info .info-content p {
  text-align: center;
}

/* 翻译按钮样式 */
.floating-translate-btn {
  position: fixed;
  right: 30px;
  bottom: 30px;
  width: 56px;
  height: 56px;
  z-index: 9999;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  font-weight: bold;
  font-size: 16px;
}

.floating-translate-btn:hover {
  transform: scale(1.1) translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
}

.floating-translate-btn.is-translated {
  background-color: #ffc107;
  border-color: #ffc107;
  color: #333;
}

/* 翻译加载状态 */
.translation-loading {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 20px 30px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 15px;
  font-size: 18px;
  z-index: 2000;
}

.translation-loading .el-icon {
  font-size: 24px;
  animation: rotating 2s linear infinite;
}

@keyframes rotating {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 768px) {
  .product-layout {
    flex-direction: column;
  }

  .product-image-container {
    height: 350px;
  }

  .preview-image {
    width: 80px;
    height: 80px;
  }

  .product-info h3 {
    font-size: 20px;
  }

  .product-info p {
    font-size: 16px;
  }

  .floating-translate-btn {
    width: 50px;
    height: 50px;
    font-size: 14px;
    right: 20px;
    bottom: 20px;
  }
}
</style>
