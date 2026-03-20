<template>
  <div class="add-product-container">
    <!-- 表单字段保持不变 -->
    <el-form-item :label="$t(isTranslated ? 'product.name_en' : 'product.name_zh') + '：'" >
      <el-input v-model="productForm.gname" :placeholder="$t(isTranslated ? 'product.name_en' : 'product.name_zh')"></el-input>
    </el-form-item>
    <el-form-item :label="$t(isTranslated ? 'product.price_en' : 'product.price_zh') + '：'">
      <el-input type="number" v-model="productForm.gvalue" :placeholder="$t(isTranslated ? 'product.price_en' : 'product.price_zh')"></el-input>
    </el-form-item>
    <el-form-item :label="$t(isTranslated ? 'product.stock_en' : 'product.stock_zh') + '：'">
      <el-input type="number" v-model="productForm.gstock" :placeholder="$t(isTranslated ? 'product.stock_en' : 'product.stock_zh')"></el-input>
    </el-form-item>
    <el-form-item :label="$t(isTranslated ? 'category.parentId_en' : 'category.parentId_zh')">
      <el-select v-model="productForm.categoryId1" @change="handleTopCategoryChange">
        <el-option
          v-for="category in topCategories"
          :key="category.cid"
          :label="category.cname"
          :value="category.cid">
        </el-option>
      </el-select>
      <div v-if="selectedTopCategory" class="selected-category-info">
      </div>
    </el-form-item>
    <el-form-item :label="$t(isTranslated ? 'category.Subcategories_en' : 'category.Subcategories_zh')">
      <el-select v-model="productForm.categoryId2" @change="handleSubCategoryChange">
        <el-option
          v-for="category in subCategories"
          :key="category.cid"
          :label="category.cname"
          :value="category.cid">
        </el-option>
      </el-select>
      <div v-if="selectedSubCategory" class="selected-category-info">
      </div>
    </el-form-item>

    <RichText :onContentChange="updateDescription"></RichText>

    <el-form-item>
      <el-button type="primary" @click="submitProduct">
        {{ $t(isTranslated ? 'order.actions.add_en' : 'order.actions.add_zh') }}
      </el-button>
      <el-button type="primary" @click="goBack">
        {{ $t(isTranslated ? 'order.actions.cancel_en' : 'order.actions.cancel_zh') }}
      </el-button>
    </el-form-item>
  </div>
  <TranslateButton
    v-model:isTranslated="isTranslated"
    class="floating-translate-btn"
  />
</template>

<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import RichText from "@/components/block-rich/RichText.vue";
import axios from 'axios';
import http from "@/utils/re.js";
import { useI18n } from 'vue-i18n';
import TranslateButton from "@/components/block/translate.vue";
import {jwtDecode} from "jwt-decode";

const { t: $t } = useI18n();
const token = localStorage.getItem('token');
const claims = jwtDecode(token);
console.log("claims:",claims);
const categories = ref([]);
const subCategories = ref([]);
const selectedTopCategory = ref(null);
const selectedSubCategory = ref(null);
const isTranslated = ref(false);
const router = useRouter();
const productForm = reactive({
  gname: '',
  gvalue: '',
  gstock: '',
  categoryId1: '',
  categoryId2: '',
  cid: '',
  desc: ''
});
const goBack = () => {
  router.back();
};

const updateDescription = (content) => {
  productForm.desc = content;
  console.log("desc:",productForm.desc);
};

const fetchCategories = async () => {
  try {
    const response = await http.post('/cat/list_cid_by_parent', { id: [0] }, {
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

const fetchCategoryInfoById = async (subCat) => {
  try {
    const response = await http.get(`/cat/get_by_cid/${subCat}`, {
      headers: {
        'Authorization': `${token}`,
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching category info:', error);
    return null;
  }
};

const fetchSubCategories = async (categoryId1) => {
  try {
    const response = await http.post('/cat/list_all_child', { id: [categoryId1] }, {
      headers: {
        'Authorization': `${token}`,
      }
    });
    subCategories.value = await Promise.all(response.data.map(async (subCat) => {
      return await fetchCategoryInfoById(subCat);
    }));
  } catch (error) {
    console.error('Error fetching subcategories:', error);
  }
};

const handleTopCategoryChange = async (categoryId1) => {
  selectedTopCategory.value = topCategories.value.find(cat => cat.cid === categoryId1);
  await fetchSubCategories(categoryId1);
};

const handleSubCategoryChange = (categoryId2) => {
  selectedSubCategory.value = subCategories.value.find(cat => cat.cid === categoryId2);
};

// 新增：表单验证函数
const validateForm = () => {
  const requiredFields = [
    { field: productForm.gname, message: 'product.name' },
    { field: productForm.gvalue, message: 'product.price' },
    { field: productForm.gstock, message: 'product.stock' },
    { field: productForm.categoryId1, message: 'category.parentId' },
    { field: productForm.categoryId2, message: 'category.Subcategories' },
    { field: productForm.desc, message: 'product.description' }
  ];

  for (const item of requiredFields) {
    if (!item.field) {
      const fieldName = $t(isTranslated.value ? `${item.message}_en` : `${item.message}_zh`);
      ElMessage.error(
        $t(isTranslated.value ? 'order.messages.fieldRequired_en' : 'order.messages.fieldRequired_zh',
          { field: fieldName })
      );
      return false;
    }
  }
  return true;
};

const submitProduct = async () => {
  // 先进行表单验证
  if (!validateForm()) {
    return;
  }

  try {
    const data = {
      gname: productForm.gname,
      gvalue: productForm.gvalue,
      gstock: productForm.gstock,
      cid: productForm.categoryId2,
      gdesc: productForm.desc,
      gseller: claims.account
    }
    console.log("新增商品的描述",data.gdesc);
    const response = await http.post('/goods/create', data, {
      headers: {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      }
    });
    console.log("新增商品的描述",response.data);
    if (response.data) {
      ElMessage.success($t(isTranslated.value ? 'order.messages.addSuccess_en' : 'order.messages.addSuccess_zh'));
      await router.push({name: 'AHome', query: {token}});
    } else {
      ElMessage.error($t(isTranslated.value ? 'order.messages.addFailed_en' : 'order.messages.addFailed_zh'));
    }
  } catch (error) {
    console.error('Error submitting product:', error);
    ElMessage.error($t(isTranslated.value ? 'order.messages.addFailed_en' : 'order.messages.addFailed_zh'));
  }
};

onMounted(() => {
  fetchCategories();
});
</script>

<style scoped>
/* 样式保持不变 */
.add-product-container {
  max-width: 600px;
  margin: 20px auto;
  padding: 20px;
}
.el-button {
  margin-right: 10px;
}
.selected-category-info {
  margin-top: 10px;
  font-size: 14px;
  color: #333;
}
</style>
