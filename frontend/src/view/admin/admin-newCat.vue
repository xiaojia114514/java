<template>
  <div style="height: 100%; display: flex;">
    <ShopLogo style="background-color: #ffffff"></ShopLogo>
    <el-header style="width: 82%;text-align: right; font-size: 12px; display: flex; align-items: center; height: 80px; background-color: var(--el-color-primary-light-9);">
      <admin-head :is-translated="isTranslated"></admin-head>
    </el-header>
  </div>
  <div class="order-form">
    <el-form :model="form" class="form-container">
      <h2>{{ $t(isTranslated ? 'translate.menu.categoryManagement_en' : 'translate.menu.categoryManagement_zh') }}</h2>
      <el-form-item :label="$t(isTranslated ? 'category.name_en' : 'category.name_zh') + '：'">
        <el-input v-model="form.cname" :placeholder="$t(isTranslated ? 'category.name_en' : 'category.name_zh')"></el-input>
      </el-form-item>
      <el-form-item :label="$t(isTranslated ? 'category.parentId_en' : 'category.parentId_zh')">
        <el-select v-model="form.cparentid">
          <el-option
            v-for="category in topCategories"
            :key="category.cid"
            :label="category.cname"
            :value="category.cid">
          </el-option>
        </el-select>
        <!-- 显示选中的分类信息 -->
        <div v-if="selectedTopCategory" class="selected-category-info">
        </div>
      </el-form-item>
      <el-form-item>
        <el-button @click="goBack">{{ $t(isTranslated ? 'order.actions.cancel_en' : 'order.actions.cancel_zh') }}</el-button>
        <el-button type="primary" @click="submit">{{ $t(isTranslated ? 'order.actions.submit_en' : 'order.actions.submit_zh') }}</el-button>
      </el-form-item>
    </el-form>
  </div>
  <TranslateButton
    v-model:isTranslated="isTranslated"
    class="floating-translate-btn"
  />
</template>

<script setup>
import { ElButton, ElFormItem, ElInput, ElForm, ElMessage } from "element-plus";
import axios from "axios";
import { jwtDecode } from "jwt-decode";
import {computed, onMounted, ref} from "vue";
import ShopLogo from "@/components/block/Shop-Logo.vue";
import AdminHead from "@/components/block-admin/adminHead.vue";
import http from "@/utils/re.js";
import TranslateButton from "@/components/block/translate.vue";
import { useI18n } from 'vue-i18n';

const { t: $t } = useI18n();

const token = localStorage.getItem('token');
const claims = jwtDecode(token);
const categories = ref([]); // 一级分类
const isTranslated = ref(false);
console.log("claim:", claims);
const form = ref({
  cname: '',
  cparentid: '',
});
const goBack = () => {
  window.history.back();
};
const fetchCategories = async () => {
  try {
    const response = await http.post('/cat/list_cid_by_parent', { id: [-1,0] }, {
      headers: {
        'Authorization': `${token}`,
      }
    });
    categories.value = await Promise.all(response.data.map(async (category) => {
      try {
        const infoResponse = await http.get(`/cat/get_by_cid/${category}`, {
          headers: {
            'Authorization': `${token}`,
          }
        });
        return { ...category, ...infoResponse.data };
      } catch (error) {
        console.error(`Error fetching info for category ${category.cid}:`, error);
        return category;
      }
    }));
  } catch (error) {
    console.error('Error fetching categories:', error);
  }
};

const topCategories = computed(() => categories.value);

const submit = async () => {
  try {
    console.log("form:", form.value);
    const response = await http.post('/cat/creat', form.value, {
      headers: {
        'Authorization': `${token}`,
      }
    });
    if (response.data) {
      ElMessage.success($t(isTranslated.value ? 'order.messages.addSuccess_en' : 'order.messages.addSuccess_zh'));
    } else {
      ElMessage.error($t(isTranslated.value ? 'order.messages.operationFailed_en' : 'order.messages.operationFailed_zh'));
    }
  } catch (error) {
    console.error('Error:', error);
    ElMessage.error($t(isTranslated.value ? 'order.messages.error_en' : 'order.messages.error_zh'));
  }
};
onMounted(() => {
  fetchCategories();
});
</script>

<style scoped>
.order-form {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* Use 100vh to make it full height of the viewport */
}

.form-container {
  width: 100%;
  max-width: 400px;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}
</style>
