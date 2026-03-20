<script setup>

</script>

<template>
  <div class="search-bar">
    <input type="text" placeholder="搜索商品..." v-model="searchQuery">
    <el-button type="primary" :icon="Search" style="width: 100px;round: true;height: 50px">搜索</el-button>
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
