<template>
  <buyer-header></buyer-header>
  <div style="display: flex">
    <ShopLogo></ShopLogo>
  </div>
  <div class="order-form">
    <el-form-item label="收货人：">
      <el-input v-model="form.uaccount" placeholder="收货人" aria-required="true" disabled></el-input>
    </el-form-item>
    <el-form-item label="数量：">
      <el-input-number v-model="form.onumber" placeholder="下单数量" aria-required="true" :min="1"></el-input-number>
    </el-form-item>
    <el-form-item label="联系电话：">
      <el-input v-model="form.ophone" placeholder="联系电话" aria-required="true"></el-input>
    </el-form-item>
    <el-form-item label="地址：">
      <el-input v-model="form.oaddress" placeholder="地址" aria-required="true"></el-input>
    </el-form-item>
    <el-form-item label="订单备注：">
      <el-input
        type="textarea"
        v-model="form.oremark"
        placeholder="请输入订单备注"
        :rows="2"
      ></el-input>
    </el-form-item>
    <el-form-item>
      <el-button @click="goBack">取消</el-button>
      <el-button type="primary" @click="submitForm">提交订单</el-button>
    </el-form-item>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue';
import {ElFormItem, ElInput, ElButton, ElMessage} from 'element-plus';
import ShopLogo from "@/components/block/Shop-Logo.vue";
import BuyerHeader from "@/components/block-buyer/buyer-header.vue";
import {jwtDecode} from "jwt-decode";
import axios from 'axios';
import {useRoute} from "vue-router";
import router from "@/router/index.js";
import http from "@/utils/re.js";
const route = useRoute();
const gid = Number(route.params.id);
console.log("商品id:", gid);
const token = localStorage.getItem('token');
const claims = jwtDecode(token);
console.log("claim:", claims);
const form = ref({
  gid: Number(route.params.id),
  uaccount: '',
  onumber: '',
  ophone: '',
  oaddress: '',
  oremark: '',
  ostate: 0
});

const submitForm = async () => {
  if (!form.value.ophone || !form.value.oaddress) {
    ElMessage.error('电话和地址不能为空！');
    return;
  }
  try {
    console.log("form:", form.value)
    const response = await http.post('/order/creat', form.value, {
      headers: {
        'Authorization': `${token}`,
      }
    });
    if (response.data) {
      ElMessage.success('订单创建成功！');
      const backid = Number(route.params.id);
      if (backid) {
        console.log("id:",backid)
        const route = {
          name: 'BProductDet',
          params: { pid: backid }
        };
        await router.push(route);
      } else {
        console.error('商品信息未找到');
      }
    } else {
      ElMessage.error('订单错误');
    }
  } catch (error) {
    console.error('Error:', error);
    ElMessage.error('订单错误');
  }
};

const goBack = () => {
  window.history.back();
};

onMounted(() => {
  // 假设 claims 中的属性名是 account, phone, address
  // 请根据实际的属性名进行调整
  form.value.uaccount = claims.account || '';
  form.value.ophone = claims.phone || '';
  form.value.oaddress = claims.address || '';
});
</script>

<style scoped>
/* 你可以在这里添加一些样式来美化你的表单 */
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
