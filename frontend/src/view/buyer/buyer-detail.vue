<template>
  <div style="height: 100%; display: flex; flex-direction: column;">
    <buyer-header :is-translated="isTranslated"></buyer-header>
    <div style="display: flex">
      <ShopLogo></ShopLogo>
      <Goodssearch :is-translated="isTranslated"></Goodssearch>
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
              <div class="action-buttons">
                <el-button type="primary" @click="buyProduct(product)">{{ $t(isTranslated ? 'translate.detail.buy_en' : 'translate.detail.buy_zh') }}</el-button>
                <el-button type="success" @click="showAddToCartDialog = true">
                  <el-icon size="20"><ShoppingCart /></el-icon>
                  {{ $t(isTranslated ? 'translate.detail.addcart_en' : 'translate.detail.addcart_zh') }}
                </el-button>
                <el-button
                  :type="'warning'"
                  @click="toggleCollect">
                  <el-icon v-if="isCollected" size="20"><StarFilled /></el-icon>
                  <el-icon v-if="!isCollected" size="20"><Star /></el-icon>
                  {{ $t(isTranslated ? (isCollected ? 'translate.detail.saved_en' : 'translate.detail.save_en') : (isCollected ? 'translate.detail.saved_zh' : 'translate.detail.save_zh')) }}
                </el-button>
              </div>
              <div v-if="showAddToCartDialog" class="modal">
                <div class="modal-content">
                  <span class="close" @click="showAddToCartDialog = false">&times;</span>
                  <el-form :model="addToCartForm">
                    <el-form-item :label="$t(isTranslated ? 'translate.detail.quantity_en' : 'translate.detail.quantity_zh')">
                      <el-input-number v-model="addToCartForm.scnumber" :min="1"></el-input-number>
                    </el-form-item>
                  </el-form>
                  <div class="modal-footer">
                    <el-button @click="showAddToCartDialog = false">{{ $t(isTranslated ? 'translate.detail.cancel_en' : 'translate.detail.cancel_zh') }}</el-button>
                    <el-button type="primary" @click="addToCart(product)">{{ $t(isTranslated ? 'translate.detail.confirm_en' : 'translate.detail.confirm_zh') }}</el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="preview-container">
          <img v-for="(pic, index) in product.pictures" :key="index" :src="pic" class="preview-image" @click="selectPicture(index)" />
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
import { defineComponent, ref, reactive, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import ShopLogo from "@/components/block/Shop-Logo.vue";
import BuyerHeader from "@/components/block-buyer/buyer-header.vue";
import Goodssearch from "@/components/block-search/buyer-search.vue";
import { jwtDecode } from "jwt-decode";
import {ShoppingCart, Star, StarFilled, Loading} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import http from "@/utils/re.js";

export default defineComponent({
  components: {StarFilled, ShoppingCart, Star, Loading, Goodssearch, BuyerHeader, ShopLogo },
  setup() {
    const token = localStorage.getItem('token');
    const claims = jwtDecode(token);
    const product = ref(null);
    const currentPicture = ref('');
    const route = useRoute();
    const router = useRouter();
    const showAddToCartDialog = ref(false);
    const addToCartForm = reactive({
      scnumber: 1
    });
    const isCollected = ref(false);
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

    const checkCollectStatus = async (gid) => {
      try {
        const response = await http.post('/collect/check_collect', {
          uaccount: claims.account,
          gid: gid
        }, {
          headers: {
            'Authorization': `${token}`,
          }
        });
        isCollected.value = response.data;
      } catch (error) {
        console.error('检查收藏状态失败:', error);
      }
    };

    const toggleCollect = async () => {
      if (isCollected.value) {
        const confirmDelete = confirm(isTranslated.value ? 'Are you sure you want to remove this item from your favorites?' : '您确定要取消收藏吗？');
        if (confirmDelete) {
          try {
            const data = {
              uaccount: claims.account,
              gid: gid
            }
            const response = await http.post('/collect/delete', data, {
              headers: {
                'Authorization': `${token}`,
              }
            });
            if (response.data) {
              ElMessage.success(isTranslated.value ? "Removed from favorites!" : "取消收藏成功！")
              isCollected.value = false;
            } else {
              ElMessage.error(isTranslated.value ? 'Failed to remove' : '取消收藏失败');
            }
          } catch (error) {
            console.error('取消收藏失败:', error);
            ElMessage.error(isTranslated.value ? 'Failed to remove' : '取消收藏失败');
          }
        }
      } else {
        addFavorite(product.value);
      }
    };

    const fetchProductData = async (gid) => {
      try {
        const response = await http.get(`/goods/get_by_id/${gid}`, {
          headers: {
            'Authorization': `${token}`,
          }
        });
        const fetchedProduct = response.data;
        const pictures = await fetchProductPictures(fetchedProduct.gid);
        product.value = { ...fetchedProduct, pictures };
        currentPicture.value = pictures[0];
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
      router.go(-1);
    };

    const buyProduct = (product) => {
      const gid = product.gid;
      if (gid) {
        const route = {
          name: 'NewOrder',
          params: { id: gid }
        };
        router.push(route);
      } else {
        console.error('商品信息未找到');
      }
    };

    const addToCart = (product) => {
      const gid = product.gid;
      if (gid) {
        const data = {
          uaccount: claims.account,
          gid: gid,
          scnumber: addToCartForm.scnumber
        };
        http.post('/shoppingcart/create', data, {
          headers: {
            'Authorization': `${token}`,
          }
        }).then(response => {
          if (response.data) {
            ElMessage.success(isTranslated.value ? 'Added to cart successfully!' : '加入购物车成功');
            showAddToCartDialog.value = false;
          } else {
            ElMessage.error(isTranslated.value ? 'Failed to add to cart' : '加入购物车失败');
          }
        }).catch(error => {
          console.error('加入购物车失败:', error);
          ElMessage.error(isTranslated.value ? 'Failed to add to cart' : '加入购物车失败');
        });
      } else {
        console.error('商品信息未找到');
      }
    };

    const addFavorite = (product) => {
      const gid = product.gid;
      if (gid) {
        const data = {
          uaccount: claims.account,
          gid: gid
        };
        http.post('/collect/create', data, {
          headers: {
            'Authorization': `${token}`,
          }
        }).then(response => {
          if (response.data) {
            isCollected.value = true
            ElMessage.success(isTranslated.value ? "Saved to favorites!" : "收藏成功！")
          } else {
            ElMessage.error(isTranslated.value ? 'Failed to save' : '收藏失败');
          }
        }).catch(error => {
          console.error('收藏失败:', error);
          ElMessage.error(isTranslated.value ? 'Failed to save' : '收藏失败');
        });
      } else {
        console.error('商品信息未找到');
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
        checkCollectStatus(gid);
      } else {
        console.error('商品ID无效');
      }
    });

    return {
      product,
      currentPicture,
      goBack,
      buyProduct,
      addToCart,
      showAddToCartDialog,
      addToCartForm,
      selectPicture,
      addFavorite,
      toggleCollect,
      isCollected,
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
  margin: 0;
  padding: 0;
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
  margin: 0 0 15px 0;
  line-height: 1.4;
}

.product-info p {
  font-size: 18px;
  color: #666;
  margin: 10px 0;
}

.product-description {
  margin-top: 30px;
  padding: 25px;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  background-color: #f9fafc;
  font-size: 16px;
  color: #444;
  line-height: 1.8;
}

.product-description >>> p {
  margin-bottom: 15px;
}

.product-description >>> h3 {
  font-size: 20px;
  color: #333;
  margin-top: 20px;
  margin-bottom: 15px;
}

.preview-container {
  display: flex;
  overflow-x: auto;
  gap: 15px;
  margin-top: 15px;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 8px;
}

.preview-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 6px;
  cursor: pointer;
  border: 2px solid transparent;
  transition: all 0.3s ease;
}

.preview-image:hover, .preview-image.active {
  border: 2px solid #409EFF;
  transform: scale(1.05);
}

.el-button {
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
  transition: all 0.3s ease;
}

.el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.action-buttons {
  display: flex;
  gap: 15px;
  margin-top: 20px;
  flex-wrap: wrap;
  justify-content: center;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: #fff;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 5px 15px rgba(0,0,0,0.2);
  width: 90%;
  max-width: 400px;
  position: relative;
}

.close {
  position: absolute;
  top: 15px;
  right: 20px;
  font-size: 28px;
  font-weight: bold;
  cursor: pointer;
  color: #666;
  transition: color 0.3s;
}

.close:hover {
  color: #333;
}

.modal-footer {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 15px;
}

/* 翻译按钮样式 */
.floating-translate-btn {
  position: fixed;
  right: 100px;
  bottom: 200px;
  width: 56px;
  height: 56px;
  z-index: 9999;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.floating-translate-btn:hover {
  transform: scale(1.1) translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
}

.floating-translate-btn.is-translated {
  background-color: #ffc107;
  border-color: #ffc107;
}

.floating-translate-btn:deep(.el-icon) {
  transition: transform 0.3s ease;
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

  .action-buttons {
    flex-direction: column;
    align-items: stretch;
  }

  .product-info h3 {
    font-size: 20px;
  }

  .product-info p {
    font-size: 16px;
  }

  .translate-btn {
    padding: 8px 15px;
    font-size: 14px;
  }
}
</style>
