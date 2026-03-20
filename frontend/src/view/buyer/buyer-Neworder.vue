<template>
  <buyer-header :is-translated="isTranslated"></buyer-header>
  <div style="display: flex">
    <ShopLogo></ShopLogo>
  </div>
  <div class="order-form">
    <el-form-item :label="$t(isTranslated ? 'translate.favorites.consignee_en' : 'translate.favorites.consignee_zh')">
      <el-input v-model="form.uaccount" :placeholder="$t(isTranslated ? 'translate.favorites.consignee_en' : 'translate.favorites.consignee_zh')" disabled></el-input>
    </el-form-item>
    <el-form-item :label="$t(isTranslated ? 'translate.favorites.quantity_en' : 'translate.favorites.quantity_zh')">
      <el-input-number v-model="form.onumber" :placeholder="$t(isTranslated ? 'translate.favorites.quantity_en' : 'translate.favorites.quantity_zh')" :min="1"></el-input-number>
    </el-form-item>
    <el-form-item :label="$t(isTranslated ? 'order.phone_en' : 'order.phone_zh')">
      <el-input v-model="form.ophone" :placeholder="$t(isTranslated ? 'order.phone_en' : 'order.phone_zh')"></el-input>
    </el-form-item>
    <el-form-item :label="$t(isTranslated ? 'order.address_en' : 'order.address_zh')">
      <el-input v-model="form.oaddress" :placeholder="$t(isTranslated ? 'order.address_en' : 'order.address_zh')"></el-input>
    </el-form-item>
    <el-form-item :label="$t(isTranslated ? 'order.remark_en' : 'order.remark_zh')">
      <el-input
        type="textarea"
        v-model="form.oremark"
        :placeholder="$t(isTranslated ? 'order.remark_en' : 'order.remark_zh')"
        :rows="2"
      ></el-input>
    </el-form-item>
    <el-form-item>
      <el-button @click="goBack">
        {{ $t(isTranslated ? 'translate.detail.cancel_en' : 'translate.detail.cancel_zh') }}
      </el-button>
      <el-button type="primary" @click="submitForm">
        {{ $t(isTranslated ? 'translate.favorites.submitOrder_en' : 'translate.favorites.submitOrder_zh') }}
      </el-button>
    </el-form-item>
  </div>
  <TranslateButton
    v-model:isTranslated="isTranslated"
    class="floating-translate-btn"
  />
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElFormItem, ElInput, ElButton, ElMessage } from 'element-plus';
import ShopLogo from '@/components/block/Shop-Logo.vue';
import BuyerHeader from '@/components/block-buyer/buyer-header.vue';
import { jwtDecode } from 'jwt-decode';
import axios from 'axios';
import { useRoute } from 'vue-router';
import router from '@/router/index.js';
import http from '@/utils/re.js';
import TranslateButton from '@/components/block/translate.vue';
import { useI18n } from 'vue-i18n';

const { t: $t } = useI18n();

const route = useRoute();
const gid = Number(route.params.id);
console.log('商品id:', gid);
const token = localStorage.getItem('token');
const claims = jwtDecode(token);
console.log('claim:', claims);
const form = ref({
  gid: Number(route.params.id),
  uaccount: '',
  onumber: '',
  ophone: '',
  oaddress: '',
  oremark: '',
  ostate: 0,
  opaid: 0,
  eid: 0
});

const isTranslated = ref(false);

const toggleTranslation = () => {
  isTranslated.value = !isTranslated.value;
};

const submitForm = async () => {
  if (!form.value.ophone || !form.value.oaddress) {
    ElMessage.error($t(isTranslated.value ? 'translate.favorites.phoneAddressRequired_en' : 'translate.favorites.phoneAddressRequired_zh'));
    return;
  }
  try {
    console.log('form:', form.value);
    const response = await http.post('/order/creat', form.value, {
      headers: {
        'Authorization': `${token}`,
      }
    });
    if (response.data) {
      ElMessage.success($t(isTranslated.value ? 'translate.favorites.orderSuccess_en' : 'translate.favorites.orderSuccess_zh'));
      const backid = Number(route.params.id);
      if (backid) {
        console.log('id:', backid);
        const route = {
          name: 'BProductDet',
          params: { pid: backid }
        };
        await router.push(route);
      } else {
        console.error('商品信息未找到');
      }
    } else {
      ElMessage.error($t(isTranslated.value ? 'translate.favorites.orderFail_en' : 'translate.favorites.orderFail_zh'));
    }
  } catch (error) {
    console.error('Error:', error);
    ElMessage.error($t(isTranslated.value ? 'translate.favorites.operationFail_en' : 'translate.favorites.operationFail_zh'));
  }
};

const goBack = () => {
  window.history.back();
};

onMounted(() => {
  form.value.uaccount = claims.account || '';
  form.value.ophone = claims.phone || '';
  form.value.oaddress = claims.address || '';
});
</script>

<style scoped>
.order-form {
  max-width: 400px;
  margin: auto;
  padding: 20px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-textarea {
  width: 100%;
}
</style>
