<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { Search as SearchIcon } from '@element-plus/icons-vue';
import { defineProps } from 'vue';
import http from "@/utils/re.js";
const props = defineProps({
  isTranslated: {
    type: Boolean,
    default: false
  }
});
const searchQuery = ref('');
const router = useRouter();

const handleSearch = () => {
  console.log("sousuo:", searchQuery.value);
  const query = searchQuery.value;
  const route = {
    name: 'SearchRes',
    params: { query: query }
  };
  router.push(route);
};
</script>

<template>
  <div class="search-bar">
    <el-input
      :placeholder="$t(props.isTranslated ? 'translate.search.placeholder_en' : 'translate.search.placeholder_zh')"
      v-model="searchQuery"
      @keyup.enter="handleSearch"
    >
      <template #append>
        <el-button type="primary" @click="handleSearch">
          {{ $t(props.isTranslated ? 'translate.search.button_en' : 'translate.search.button_zh') }}
        </el-button>
      </template>
    </el-input>
  </div>
</template>

<style scoped>
.search-bar {
  height: 50px;
  display: flex;
  margin-top: 10px;
  justify-content: center;
  width: 100%;
}

.search-bar input {
  padding: 10px;
  flex: 1;
  border: 1px solid black; /* 添加黑色边框 */
  border-radius: 4px; /* 可以添加圆角 */
}

.search-bar button {
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
}

.search-bar button:hover {
  background-color: #0056b3;
}
</style>
