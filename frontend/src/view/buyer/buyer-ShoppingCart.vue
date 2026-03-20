<template>
  <buyer-header :is-translated="isTranslated"></buyer-header>
  <div style="display: flex">
    <ShopLogo></ShopLogo>
    <div class="info-header">
      <el-button type="default" @click="goBack"><el-icon size="32"><HomeFilled /></el-icon></el-button>
    </div>
  </div>
  <div class="product-list">
    <div class="button-container">
      <el-button type="success" @click="openOrder">
        {{ $t(isTranslated ? 'translate.favorites.submitOrder_en' : 'translate.favorites.submitOrder_zh') }}
      </el-button>
      <el-button type="primary" @click="batchCollect">
        {{ $t(isTranslated ? 'translate.favorites.batchCollect_en' : 'translate.favorites.batchCollect_zh') }}
      </el-button>
      <el-button type="danger" @click="deleteSelectedProducts">
        {{ $t(isTranslated ? 'translate.favorites.batchDelete_en' : 'translate.favorites.batchDelete_zh') }}
      </el-button>
    </div>
    <div
      title="下单"
      v-if="orderDialog"
      class="order-dialog"
      @close="orderDialog = false"
    >
      <el-form class="form-container">
        <el-form-item :label="$t(isTranslated ? 'order.phone_en' : 'order.phone_zh')" class="form-item">
          <input v-model="orderForm.ophone" class="input-field" />
        </el-form-item>
        <el-form-item :label="$t(isTranslated ? 'order.address_en' : 'order.address_zh')" class="form-item">
          <input v-model="orderForm.oaddress" class="input-field" />
        </el-form-item>
        <el-form-item :label="$t(isTranslated ? 'order.remark_en' : 'order.remark_zh')" class="form-item">
          <input
            type="text"
            v-model="orderForm.oremark"
            class="input-field"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <div class="button-container">
        <el-button @click="orderDialog = false">
          {{ $t(isTranslated ? 'translate.detail.cancel_en' : 'translate.detail.cancel_zh') }}
        </el-button>
        <el-button type="primary" @click="subOrder">
          {{ $t(isTranslated ?  'translate.favorites.submitOrder_en' : 'translate.favorites.submitOrder_zh') }}
        </el-button>
      </div>
    </div>
    <el-table :data="products" style="width: 100%" border>
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
      <el-table-column
        :label="$t(isTranslated ? 'translate.favorites.productId_en' : 'translate.favorites.productId_zh')"
        width="180"
        min-width="180"
        prop="gid"
      ></el-table-column>
      <el-table-column
        :label="$t(isTranslated ? 'translate.favorites.productName_en' : 'translate.favorites.productName_zh')"
        width="180"
        min-width="180"
        prop="productInfo[0].gname"
      ></el-table-column>
      <el-table-column
        :label="$t(isTranslated ? 'translate.favorites.previewImage_en' : 'translate.favorites.previewImage_zh')"
      >
        <template #default="{row}">
          <img :src="row.pictures[0]" class="product-image" />
        </template>
      </el-table-column>
      <el-table-column
        :label="$t(isTranslated ? 'translate.favorites.description_en' : 'translate.favorites.description_zh')"
        width="120"
        min-width="120"
      >
        <template #default="{row}">
          <div class="product-description" v-html="row.productInfo[0].gdesc"></div>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t(isTranslated ? 'translate.favorites.unitPrice_en' : 'translate.favorites.unitPrice_zh')"
        width="120"
        min-width="120"
        prop="productInfo[0].gvalue"
      ></el-table-column>
      <el-table-column
        :label="$t(isTranslated ? 'translate.favorites.quantity_en' : 'translate.favorites.quantity_zh')"
        width="175"
        min-width="120"
      >
        <template #default="{row}">
          <el-input-number v-model="row.scnumber" @change="updateSCnumber(row.scid, $event)" :min="1" :max="999"></el-input-number>
        </template>
      </el-table-column>
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
          <el-button type="text" @click="delet(row.scid)">
            <el-icon><Remove /></el-icon>
          </el-button>
          <el-button type="text" @click="toggleCollect(row.gid)">
            <el-icon><Star /></el-icon>
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
import {onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import BuyerHeader from "@/components/block-buyer/buyer-header.vue";
import ShopLogo from "@/components/block/Shop-Logo.vue";
import router from "@/router/index.js";
import {jwtDecode} from "jwt-decode";
import {ElMessage} from 'element-plus';
import {HomeFilled, Remove, Star} from "@element-plus/icons-vue";
import http from "@/utils/re.js";
import TranslateButton from "@/components/block/translate.vue";
import {useI18n} from "vue-i18n";

export default {
  components: {HomeFilled, Remove, Star, ShopLogo, BuyerHeader, TranslateButton},
  setup() {
    const token = localStorage.getItem('token');
    const claims = jwtDecode(token);
    const route = useRoute();
    const { t: $t } = useI18n();
    const products = ref([]); // 用于存储商品数据
    const isAllSelected = ref(false);
    const orderDialog = ref(null);
    const isTranslated = ref(false);
    const orderForm = {
      ophone : claims.phone,
      oaddress: claims.address,
      ostate: 0,
      oremark: '',
      uaccount: claims.account,
    }

    const toggleTranslation = () => {
      isTranslated.value = !isTranslated.value;
    };

    const openOrder = () => {
      if (products.value.some(product => product.selected)) {
        orderDialog.value = true;
      } else {
        ElMessage.error($t(isTranslated.value ? 'translate.favorites.pleaseSelectItem_en' : 'translate.favorites.pleaseSelectItem_zh'));
      }
    };

    const subOrder = async () => {
      if (!orderForm.oaddress || !orderForm.ophone) {
        ElMessage.error($t(isTranslated.value ? 'translate.favorites.phoneAddressRequired_en' : 'translate.favorites.phoneAddressRequired_zh'));
        return;
      }
      // 创建订单数据
      const ordersData = products.value.map(product => {
        return {
          gid: product.gid,
          uaccount:orderForm.uaccount,
          ophone: orderForm.ophone,
          oaddress: orderForm.oaddress,
          oremark: orderForm.oremark,
          onumber: product.scnumber,
          ostate: 0
        };
      });
      for (const order of ordersData) {
        console.log("当前订单：",order);
        await http.post('/order/creat', order, {
          headers: {
            'Authorization': `${token}`,
          }
        });
      }
      ElMessage.success($t(isTranslated.value ? 'translate.favorites.orderSuccess_en' : 'translate.favorites.orderSuccess_zh'));
      const selectedProducts = products.value.filter(product => product.selected);
      for (const product of selectedProducts) {
        await deleteach(product.scid);
      }
      ElMessage.success($t(isTranslated.value ? 'translate.favorites.removedFromCart_en' : 'translate.favorites.removedFromCart_zh'));
      orderDialog.value = false;
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

    const batchCollect = async () => {
      const selectedProducts = products.value.filter(product => product.selected);
      for (const product of selectedProducts) {
        await Collect(product.gid);
        await http.delete(`/shoppingcart/delete/${product.scid}`, {
          headers: {
            'Authorization': `${token}`,
          }
        });
        products.value = products.value.filter(p => p.scid !== product.scid);
      }
      ElMessage({
        message: $t(isTranslated.value ? 'translate.favorites.collectedAndRemoved_en' : 'translate.favorites.collectedAndRemoved_zh'),
        type: 'success'
      });
    };

    const toggleCollect = async (gid) => {
      try {
        const collect = { uaccount: claims.account, gid: gid };
        const isCollected = await http.post('/collect/check_collect', collect, {
          headers: {
            'Authorization': `${token}`,
          }
        });
        if (isCollected.data) {
          const confirmDelete = confirm($t(isTranslated.value ? 'translate.favorites.uncollectedConfirm_en' : 'translate.favorites.uncollectedConfirm_zh'));
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
                ElMessage.success($t(isTranslated.value ? 'translate.favorites.uncollectedSuccess_en' : 'translate.favorites.uncollectedSuccess_zh'));
                // isCollected.value = false; // 更新收藏状态
              } else {
                ElMessage.error($t(isTranslated.value ? 'translate.favorites.uncollectedFail_en' : 'translate.favorites.uncollectedFail_zh'));
              }
            } catch (error) {
              console.error('取消收藏失败:', error);
              ElMessage.error($t(isTranslated.value ? 'translate.favorites.uncollectedFail_en' : 'translate.favorites.uncollectedFail_zh'));
            }
          }
        } else {
          const response = await http.post('/collect/create', collect, {
            headers: {
              'Authorization': `${token}`,
            }
          });
          if (response.data) {
            ElMessage({
              message: $t(isTranslated.value ? 'translate.favorites.collectedSuccess_en' : 'translate.favorites.collectedSuccess_zh'),
              type: 'success'
            });
          } else {
            ElMessage({
              message: $t(isTranslated.value ? 'translate.favorites.collectedFail_en' : 'translate.favorites.collectedFail_zh'),
              type: 'error'
            });
          }
        }
      } catch (error) {
        console.error("收藏操作失败:", error);
        ElMessage({
          message: $t(isTranslated.value ? 'translate.favorites.operationFail_en' : 'translate.favorites.operationFail_zh'),
          type: 'error'
        });
      }
    };

    const Collect = async (gid) => {
      try {
        const collect = { uaccount: claims.account, gid: gid };
        const isCollected = await http.post('/collect/check_collect', collect, {
          headers: {
            'Authorization': `${token}`,
          }
        });
        if (!isCollected.data){
          const response = await http.post('/collect/create', collect, {
            headers: {
              'Authorization': `${token}`,
            }
          });
          if (!response.data){
            ElMessage({
              message: $t(isTranslated.value ? 'translate.favorites.collectedFail_en' : 'translate.favorites.collectedFail_zh'),
              type: 'error'
            });
          }
        }
      } catch (error) {
        console.error("收藏操作失败:", error);
        ElMessage({
          message: $t(isTranslated.value ? 'translate.favorites.operationFail_en' : 'translate.favorites.operationFail_zh'),
          type: 'error'
        });
      }
    };

    const updateSCnumber = async (scid, newQuantity) => {
      try {
        const data = { scid, scnumber: newQuantity };
        const response = await http.post('/shoppingcart/update', data, {
          headers: {
            'Authorization': `${token}`,
          }
        });
        if (response.data) {
          const index = products.value.findIndex(product => product.scid === scid);
          if (index !== -1) {
            products.value[index].scnumber = newQuantity;
          }
        } else {
          ElMessage.error($t(isTranslated.value ? 'translate.favorites.updateFail_en' : 'translate.favorites.updateFail_zh'));
        }
      } catch (error) {
        console.error('更新商品数量失败:', error);
        ElMessage.error($t(isTranslated.value ? 'translate.favorites.updateFail_en' : 'translate.favorites.updateFail_zh'));
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
        console.log("商品：:", response.data);
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
        const response = await http.post('/shoppingcart/list', data, {
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
      goToProductDetail(gid);
    };

    const delet = async (scid) => {
      const confirmDelete = confirm($t(isTranslated.value ? 'translate.favorites.removeFromCart_en' : 'translate.favorites.removeFromCart_zh'));
      if (confirmDelete) {
        try {
          const response = await http.delete(`/shoppingcart/delete/${scid}`, {
            headers: {
              'Authorization': `${token}`,
            }
          });
          if (response.data) {
            products.value = products.value.filter(p => p.scid !== scid);
            ElMessage({
              message: $t(isTranslated.value ? 'translate.favorites.deleteSuccess_en' : 'translate.favorites.deleteSuccess_zh'),
              type: 'success'
            }); // 弹出成功消息
          } else {
            ElMessage({
              message: $t(isTranslated.value ? 'translate.favorites.deleteFail_en' : 'translate.favorites.deleteFail_zh'),
              type: 'error'
            }); // 弹出失败消息
          }
        } catch (error) {
          console.error("删除商品失败:", error);
          ElMessage({
            message: $t(isTranslated.value ? 'translate.favorites.deleteFail_en' : 'translate.favorites.deleteFail_zh'),
            type: 'error'
          }); // 弹出失败消息
        }
      }
    };
    const deleteSelectedProducts = async () => {
      const confirmDelete = confirm( $t(isTranslated.value ? 'translate.favorites.removeFromCart_en' : 'translate.favorites.removeFromCart_zh'));
      if (confirmDelete) {
        const selectedProducts = products.value.filter(product => product.selected);
        for (const product of selectedProducts) {
          await deleteach(product.scid);
        }
      }
      ElMessage({
        message: $t(isTranslated.value ? 'translate.favorites.deleteSuccess_en' : 'translate.favorites.deleteSuccess_zh'),
        type: 'success'
      });
    };
    const deleteach = async (scid) => {
      try {
        const response = await http.delete(`/shoppingcart/delete/${scid}`, {
          headers: {
            'Authorization': `${token}`,
          }
        });
        if (response.data) {
          products.value = products.value.filter(p => p.scid !== scid);

        } else {
          ElMessage({
            message: $t(isTranslated.value ? 'translate.favorites.deleteFail_en' : 'translate.favorites.deleteFail_zh'),
            type: 'error'
          }); // 弹出失败消息
        }
      } catch (error) {
        console.error("删除商品失败:", error);
        ElMessage({
          message: $t(isTranslated.value ? 'translate.favorites.deleteFail_en' : 'translate.favorites.deleteFail_zh'),
          type: 'error'
        }); // 弹出失败消息
      }
    };
    onMounted(() => {
      fetchProductsFromCart();
    });

    const goBack = () => {
      router.go(-1);
    };

    return {
      products,
      updateSCnumber,
      goToProductDetail,
      goBack,
      viewDetail,
      deleteSelectedProducts,
      delet,
      toggleCollect,
      batchCollect,
      Collect,
      toggleAllSelection,
      orderDialog,
      subOrder,
      openOrder,
      isAllSelected,
      orderForm,
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
.input-field {
  width: 100%;
  padding: 10px;
  border: 1px solid #dcdfe6; /* 输入框边框颜色 */
  border-radius: 4px; /* 输入框圆角 */
  box-sizing: border-box; /* 确保padding不影响宽度 */
}
.form-container {
  width: 100%;
  max-width: 400px; /* 可以根据实际情况调整 */
}

.form-item {
  margin-bottom: 15px; /* 输入框之间的间距 */
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
