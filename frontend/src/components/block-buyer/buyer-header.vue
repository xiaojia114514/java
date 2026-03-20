<script setup>
import { useRouter } from 'vue-router';
import { defineProps } from 'vue';
import { useI18n } from 'vue-i18n';
import http from "@/utils/re.js";
const props = defineProps({
  isTranslated: {
    type: Boolean,
    default: false
  }
});
const router = useRouter();
const logout = () => {
  localStorage.removeItem('token');
  router.push('/');
};
</script>

<template>
  <header class="header">
    <a href="/bHome" class="bhome-link" style="color: #093b7f">
      {{ $t(props.isTranslated ? 'translate.buyer.home_en' : 'translate.buyer.home_zh') }}
    </a>
    <div class="header-left">
      <h3 class="welcome-message">
        {{ $t(props.isTranslated ? 'translate.buyer.welcome_message_en' : 'translate.buyer.welcome_message_zh') }}
      </h3>
    </div>
    <div class="header-right">
      <div class="user-management" style="padding-right: 20px">
        <img class="avatar" src="https://tse2-mm.cn.bing.net/th/id/OIP-C.qYeM02sjAeO7kgvR6TDA8AAAAA?w=211&h=211&c=7&r=0&o=5&dpr=1.5&pid=1.7" alt="Avatar">
        <a href="javascript:void(0);" @click="router.push('/bHome/collect')" class="user-link" style="color: #093b7f"><el-icon size="18"><Star /></el-icon>
          {{ $t(props.isTranslated ? 'translate.buyer.collect_en' : 'translate.buyer.collect_zh') }}
        </a>
        <a href="javascript:void(0);" @click="router.push('/bHome/shopCart')" class="user-link" style="color: #093b7f"><el-icon size="18"><ShoppingCartFull /></el-icon>
          {{ $t(props.isTranslated ? 'translate.buyer.cart_en' : 'translate.buyer.cart_zh') }}
        </a>
        <a href="/user-info" class="user-info-link" style="color: #093b7f"><el-icon size="18"><User /></el-icon>
          {{ $t(props.isTranslated ? 'translate.buyer.userinfo_en' : 'translate.buyer.userinfo_zh') }}
        </a>
        <button @click="logout" class="logout-button" style="background-color: #0c4388">
          {{ $t(props.isTranslated ? 'translate.buyer.logout_en' : 'translate.buyer.logout_zh') }}
        </button>
      </div>
    </div>
  </header>
</template>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  height: 60px;
  background-color: #e6f2ff; /* 淡蓝色背景 */
}

.header-left {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  font-family: "宋体", SimSun, sans-serif;
}

.welcome-message {
  margin: 0;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-management {
  display: flex;
  gap: 10px;
  align-items: center;
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
}

.user-management {
  display: flex;
  gap: 10px;
  align-items: center; /* 确保子元素在交叉轴上居中对齐 */
}

.user-link, .user-info-link {
  text-decoration: none;
  color: black;
  margin-right: 10px;
  display: flex; /* 使用flex布局 */
  align-items: center; /* 子元素在交叉轴上居中对齐 */
}

.el-icon {
  font-size: 18px; /* 设置图标大小 */
}

/* 如果你想要文字和图标大小完全一致，可以给文字也设置相同的font-size */
.user-link span, .user-info-link span {
  font-size: 18px; /* 设置文字大小 */
  line-height: 18px; /* 设置行高以确保文字垂直居中 */
  vertical-align: middle; /* 确保文字在行内元素中垂直居中 */
}


.user-info-link {
  text-decoration: none;
  color: black;
  margin-right: 10px;
}

.logout-button {
  padding: 5px 10px;
  background-color: #f44336;
  color: white;
  border: none;
  cursor: pointer;
}

.logout-button:hover {
  background-color: #d32f2f;
}
</style>
