<template>
  <div style="height: 100%; display: flex; flex-direction: column;">
    <buyer-header></buyer-header>
    <div style="display: flex">
      <ShopLogo></ShopLogo>
      <Goodssearch></Goodssearch>
    </div>
    <el-main class="product-detail-container">
      <el-card v-if="product" class="product-card">
        <div class="product-layout">
          <div class="product-image-container">
            <img :src="currentPicture"/>
          </div>
          <div class="product-info">
            <div class="info-header">
              <el-button type="default" @click="goBack">返回</el-button>
            </div>
            <div class="info-content">
              <h3>{{ product.gname }}</h3>
              <p>价格: ￥{{ product.gvalue }}</p>
              <p>库存: {{ product.gstock }}</p>
              <div class="action-buttons">
                <el-button type="primary" @click="buyProduct(product)">购买</el-button>
                <el-button type="success" @click="showAddToCartDialog = true"><el-icon size="20"><ShoppingCart /></el-icon>加入购物车</el-button>
                <el-button
                  :type="'warning'"
                  @click="toggleCollect">
                  <el-icon v-if="isCollected" size="2 0"><StarFilled /></el-icon>
                  <el-icon v-if="!isCollected" size="20"><Star /></el-icon>
                  {{ isCollected ? '已收藏' : '收藏' }}
                </el-button>
              </div>
              <div v-if="showAddToCartDialog" class="modal">
                <div class="modal-content">
                  <span class="close" @click="showAddToCartDialog = false">&times;</span>
                  <el-form :model="addToCartForm">
                    <el-form-item label="数量">
                      <el-input-number v-model="addToCartForm.scnumber" :min="1"></el-input-number>
                    </el-form-item>
                  </el-form>
                  <div class="modal-footer">
                    <el-button @click="showAddToCartDialog = false">取消</el-button>
                    <el-button type="primary" @click="addToCart(product)">确定</el-button>
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
          <div v-html="product.gdesc"></div>
        </div>
      </el-card>
    </el-main>
  </div>
</template>

<script>
import { defineComponent, ref, reactive, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import ShopLogo from "@/components/block/Shop-Logo.vue";
import BuyerHeader from "@/components/block-buyer/buyer-header.vue";
import Goodssearch from "@/components/block-search/buyer-search.vue";
import { jwtDecode } from "jwt-decode";
import {ShoppingCart, Star, StarFilled} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import http from "@/utils/re.js";
export default defineComponent({
  components: {StarFilled, ShoppingCart, Star, Goodssearch, BuyerHeader, ShopLogo },
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
        isCollected.value = response.data; // 根据接口返回值设置收藏状态
        console.log("寻找收藏商品：",isCollected.value)
      } catch (error) {
        console.error('检查收藏状态失败:', error);
      }
    };
    const toggleCollect = async () => {
      if (isCollected.value) {
        // 如果已收藏，则询问用户是否要取消收藏
        const confirmDelete = confirm('您确定要取消收藏吗？');
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
              ElMessage.success("取消收藏成功！")
              isCollected.value = false; // 更新收藏状态
            } else {
              ElMessage.error('取消收藏失败');
            }
          } catch (error) {
            console.error('取消收藏失败:', error);
            ElMessage.error('取消收藏失败');
          }
        }
      } else {
        // 如果未收藏，则直接调用 addFavorite 方法
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
        if (response.data.length > 0) {
          const fetchedProduct = response.data[0];
          const pictures = await fetchProductPictures(fetchedProduct.gid);
          product.value = { ...fetchedProduct, pictures };
          currentPicture.value = pictures[0]; // 设置默认显示的第一张图片
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
            ElMessage.success('加入购物车成功');
            showAddToCartDialog.value = false;
          } else {
            ElMessage.error('加入购物车失败');
          }
        }).catch(error => {
          console.error('加入购物车失败:', error);
          ElMessage.error('加入购物车失败');
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
            ElMessage.success("收藏成功！")
          } else {
            ElMessage.error('收藏失败');
          }
        }).catch(error => {
          console.error('收藏失败:', error);
          ElMessage.error('收藏失败');
        });
      } else {
        console.error('商品信息未找到');
      }
    };

    const resetAddToCartForm = () => {
      addToCartForm.scnumber = 1;
    };

    const selectPicture = (index) => {
      currentPicture.value = product.value.pictures[index];
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
      resetAddToCartForm,
      selectPicture,
      addFavorite,
      toggleCollect,
      isCollected
    };
  }
});
</script>

<style scoped>
body {
  font-family: 'Helvetica', 'Arial', sans-serif;
  margin: 0; /* 移除默认的边距 */
  padding: 0; /* 移除默认的内边距 */
}

.product-image-container {
  position: relative;
  width: 100%; /* 铺满整行 */
  height: 500px; /* 固定大图容器高度 */
  overflow: hidden; /* 隐藏超出容器的图片部分 */
}

.product-image {
  width: 100%; /* 宽度自适应 */
  height: 100%; /* 高度自适应 */
  object-fit: cover; /* 保持图片比例 */
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0,0,0,0.1); /* 添加阴影效果 */
}

.product-card {
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 100%; /* 铺满整行 */
}

.product-layout {
  display: flex;
  align-items: flex-start; /* 修改为顶部对齐 */
  justify-content: space-between; /* 保持两端对齐 */
  width: 100%; /* 铺满整行 */
  gap: 20px; /* 添加间距 */
}

.product-image-container {
  flex: 1; /* 使图片容器占据可用空间 */
}

.product-info {
  flex: 1; /* 使信息容器占据可用空间 */
  padding: 20px; /* 保持原有内边距 */
}

.info-header {
  margin-bottom: 10px; /* 增加按钮与商品信息的间距 */
  display: flex;
  justify-content: flex-end; /* 返回按钮靠右 */
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

.preview-container {
  display: flex;
  overflow-x: auto; /* 允许横向滚动 /
gap: 10px; / 预览图之间的间隙 /
margin-top: 10px; / 大图与预览图之间的间隙 /
padding: 0 10px; / 预览图滚动时的内边距 /
width: 100%; / 铺满整行 */
}

.preview-image {
  width: 100px; /* 预览图宽度 /
height: 100px; / 预览图高度 /
object-fit: cover;
border-radius: 4px;
cursor: pointer;
border: 2px solid transparent; / 添加透明边框 /
transition: border 0.3s; / 添加边框变化动画 */
}

.preview-image:hover, .preview-image.active {
  border: 2px solid #409EFF; /* 鼠标悬停或选中时显示蓝色边框 */
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
  text-align: center; /* 文本居中 */
}

.action-buttons {
  display: flex;
  gap: 10px; /* 按钮之间的间隙 */
}

@media (max-width: 768px) {
.product-layout {
flex-direction: column;
}

.product-image-container {
  height: 300px; /* 在小屏幕上减小高度 */
}

.preview-container {
  justify-content: center; /* 在小屏幕上居中显示 */
}
}
</style>
