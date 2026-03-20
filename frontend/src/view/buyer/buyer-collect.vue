<template>
  <buyer-header :is-translated="isTranslated"></buyer-header>
  <div style="display: flex">
    <ShopLogo></ShopLogo>
    <div class="info-header">
      <el-button type="default" @click="goBack">
        <el-icon size="32"><HomeFilled /></el-icon>
      </el-button>
    </div>
  </div>
  <div class="product-list">
    <div class="button-container">
      <el-button type="danger" @click="deleteSelectedProducts">
        {{ $t(isTranslated ? 'translate.favorites.batchDelete_en' : 'translate.favorites.batchDelete_zh') }}
      </el-button>
    </div>
    <el-table :data="products" style="width: 100%" border>
      <!-- 选择列 -->
      <el-table-column
        :label="$t(isTranslated ? 'translate.favorites.select_en' : 'translate.favorites.select_zh')"
        width="120"
        min-width="120"
      >
        <template #header>
          <el-checkbox v-model="isAllSelected" @change="toggleAllSelection"></el-checkbox>
        </template>
        <template #default="{row}">
          <el-checkbox v-model="row.selected" class="product-checkbox"></el-checkbox>
        </template>
      </el-table-column>

      <!-- 商品码列 -->
      <el-table-column
        :label="$t(isTranslated ? 'translate.favorites.productId_en' : 'translate.favorites.productId_zh')"
        width="180"
        min-width="180"
        prop="gid"
      ></el-table-column>

      <!-- 商品名称列 -->
      <el-table-column
        :label="$t(isTranslated ? 'translate.favorites.productName_en' : 'translate.favorites.productName_zh')"
        width="180"
        min-width="180"
        prop="productInfo[0].gname"
      ></el-table-column>

      <!-- 商品预览图列 -->
      <el-table-column
        :label="$t(isTranslated ? 'translate.favorites.previewImage_en' : 'translate.favorites.previewImage_zh')"
      >
        <template #default="{row}">
          <img :src="row.pictures[0]" class="product-image" />
        </template>
      </el-table-column>

      <!-- 商品介绍列 -->
      <el-table-column
        :label="$t(isTranslated ? 'translate.favorites.description_en' : 'translate.favorites.description_zh')"
        width="120"
        min-width="120"
      >
        <template #default="{row}">
          <div class="product-description" v-html="row.productInfo[0].gdesc"></div>
        </template>
      </el-table-column>

      <!-- 单价列 -->
      <el-table-column
        :label="$t(isTranslated ? 'translate.favorites.unitPrice_en' : 'translate.favorites.unitPrice_zh')"
        width="120"
        min-width="120"
        prop="productInfo[0].gvalue"
      ></el-table-column>

      <!-- 操作列 -->
      <el-table-column
        :label="$t(isTranslated ? 'translate.favorites.operations_en' : 'translate.favorites.operations_zh')"
        width="180"
        min-width="180"
        align="center"
      >
        <template #default="{row}">
          <el-button @click="viewDetail(row.gid)">
            {{ $t(isTranslated ? 'translate.favorites.viewDetail_en' : 'translate.favorites.viewDetail_zh') }}
          </el-button>
          <el-button type="text" @click="delet(row)">
            <el-icon><Remove /></el-icon>
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <TranslateButton
    v-model:isTranslated="isTranslated"
    class="floating-translate-btn"
  />
</template>

<script>
import axios from "axios";
import { useI18n } from 'vue-i18n';
import {onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import BuyerHeader from "@/components/block-buyer/buyer-header.vue";
import ShopLogo from "@/components/block/Shop-Logo.vue";
import router from "@/router/index.js";
import {jwtDecode} from "jwt-decode";
import { ElMessage } from 'element-plus';
import http from "@/utils/re.js";
import TranslateButton from "@/components/block/translate.vue";
import {HomeFilled} from "@element-plus/icons-vue";
export default {
  components: {HomeFilled, TranslateButton, ShopLogo, BuyerHeader},
  setup() {
    const route = useRoute();
    const token = localStorage.getItem('token');
    const claims = jwtDecode(token);
    const products = ref([]);
    const isAllSelected = ref(false);
    const isTranslated = ref(false);
    const { t } = useI18n();
    // 切换翻译状态
    const toggleTranslation = () => {
      isTranslated.value = !isTranslated.value;
    };

    const toggleAllSelection = () => {
      if (isAllSelected.value) {
        products.value.forEach(product => {
          product.selected = true;
        });
      } else {
        products.value.forEach(product => {
          product.selected = false;
        });
      }
    };

    const fetchProductPictures = async (gid) => {
      try {
        const picMap = {gid: gid};
        const response = await http.post('/pic/list_pic', picMap, {
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

    const fetchProductInfo = async (gid) => {
      try {
        const response = await http.get(`/goods/get_by_id/${gid}`, {
          headers: {
            'Authorization': `${token}`,
          }
        });
        return response.data;
      } catch (error) {
        console.error("获取商品详情失败:", error);
        return {};
      }
    };

    const fetchProductsFromCart = async () => {
      try {
        const data = {
          uaccount: claims.account
        };
        console.log("u:", data);
        const response = await http.post('/collect/list', data, {
          headers: {
            'Authorization': `${token}`,
          }
        });
        products.value = await Promise.all(response.data.map(async product => {
          const productInfo = [await fetchProductInfo(product.gid)];
          const pictures = await fetchProductPictures(product.gid);
          return {...product, productInfo, pictures, selected: false};
        }));
        console.log("返回购物车：", products.value);
      } catch (error) {
        console.error('从购物车获取商品信息失败:', error);
      }
    };

    const goToProductDetail = (gid) => {
      const route = {
        name: 'BProductDet',
        params: {pid: gid}
      };
      router.push(route);
    };

    const viewDetail = (gid) => {
      const route = {
        name: 'BProductDet',
        params: {pid: gid}
      };
      router.push(route);
    };

    const delet = async (row) => {
      if (confirm(t(isTranslated.value ? 'translate.favorites.deleteConfirm_en' : 'translate.favorites.deleteConfirm_zh'))) {
        try {
          const data = {
            uaccount: claims.account,
            gid: row.gid
          }
          const response = await http.post(`/collect/delete`, data,{
            headers: {
              'Authorization': `${token}`,
            }
          });
          if (response.data) {
            await fetchProductsFromCart();
            ElMessage({
              message: t(isTranslated.value ? 'translate.favorites.deleteSuccess_en' : 'translate.favorites.deleteSuccess_zh'),
              type: 'success'
            });
          } else {
            ElMessage({
              message: t(isTranslated.value ? 'translate.favorites.deleteFail_en' : 'translate.favorites.deleteFail_zh'),
              type: 'error'
            });
          }
        } catch (error) {
          console.error("删除商品失败:", error);
          ElMessage({
            message: t(isTranslated.value ? 'translate.favorites.deleteFail_en' : 'translate.favorites.deleteFail_zh'),
            type: 'error'
          });
        }
      }
    };

    const deleteSelectedProducts = async () => {
      const selectedProducts = products.value.filter(product => product.selected);
      for (const product of selectedProducts) {
        try {
          const response = await http.post('/collect/delete', {
            uaccount: claims.account,
            gid: product.gid
          }, {
            headers: {
              'Authorization': `${token}`,
            }
          });
          if (response.data) {
            products.value = products.value.filter(p => p.gid !== product.gid);
          } else {
            ElMessage({
              message: t(isTranslated.value ? 'translate.favorites.deleteFail_en' : 'translate.favorites.deleteFail_zh'),
              type: 'error'
            });
          }
        } catch (error) {
          console.error("删除商品失败:", error);
          ElMessage({
            message: t(isTranslated.value ? 'translate.favorites.deleteFail_en' : 'translate.favorites.deleteFail_zh'),
            type: 'error'
          });
        }
      }
      ElMessage({
        message: t(isTranslated.value ? 'translate.favorites.batchDeleteSuccess_en' : 'translate.favorites.batchDeleteSuccess_zh'),
        type: 'success'
      });
    };

    onMounted(() => {
      fetchProductsFromCart();
    });

    const goBack = () => {
      router.go(-1);
    };

    return {
      products,
      goToProductDetail,
      goBack,
      viewDetail,
      delet,
      deleteSelectedProducts,
      toggleAllSelection,
      isAllSelected,
      isTranslated,
      toggleTranslation
    };
  }
};
</script>

<style scoped>
.product-list {
  margin: 0 100px;
  gap:10px;
  display: flex;
  flex-direction: column;
}

.button-container {
  display: flex;
}

.product-image {
  width: 50px;
  height: 50px;
  object-fit: cover;
  display: block;
}

.info-header {
  padding: 10px;
  font-size: 16px;
  margin-left: auto;
}

.product-description {
  max-height: 40px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>
