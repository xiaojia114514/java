<script setup>

</script>

<template>
  <input type="text" v-model="user.UAccount" placeholder="Username">
  <input type="password" v-model="user.UPassword" placeholder="Password">
  <button @click.prevent="login('user')" style="background-color: #83b3f3">用户登录</button>
  <p v-if="role === 'user'" style="margin-top: 20px;color: #174e90">
    <a href="#" @click="showRegistration = true">注册账号</a>
  </p>
</template>

<style scoped>

</style>
