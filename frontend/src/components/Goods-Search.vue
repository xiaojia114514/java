<template>
  <!-- 搜索框 -->
  <el-input
    placeholder="搜索商品"
    v-model="searchQuery"
    style="flex-grow: 1; margin-right: 10px;">
    <template #append>
      <el-button icon="el-icon-search" @click="searchProducts">搜索</el-button>
    </template>
  </el-input>
</template>

<script>
import { defineComponent, ref } from 'vue';
import axios from 'axios';
import http from "@/utils/re.js";
export default defineComponent({
  setup(props, { emit }) {
    const searchQuery = ref(''); // 用于存储搜索查询
    const products = ref([]); // 用于存储商品数据

    // 获取商品信息的函数
    const searchProducts = async () => {
      try {
        const response = await http.post('/goods/get_info_by_name', {
          name: searchQuery.value // 传入搜索框中的内容
        });
        products.value = response.data; // 确保这里赋值给 products.value
        console.log("搜索到的商品：", response.data);
        // 触发自定义事件，传递搜索结果
        emit('update:products', products.value);
      } catch (error) {
        console.error('搜索商品信息失败:', error);
      }
    };

    return {
      products,
      searchQuery,
      searchProducts
    };
  }
});
</script>

<style scoped>
</style>
