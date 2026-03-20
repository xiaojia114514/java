<template>
  <el-container class="layout-container-demo">
    <buyer-header :is-translated="isTranslated"></buyer-header>
    <div class="header-middle">
      <ShopLogo class="shop-logo"></ShopLogo>
      <buyer-search :is-translated="isTranslated"></buyer-search>
    </div>
    <div class="main-container">
      <!-- 侧边栏 -->
      <el-aside width="300px" class="left-aside">
        <!-- 传递 activeIndex 到菜单组件 -->
        <buyer-menu
          :is-translated="isTranslated"
          :active-index="activeIndex"
          @menu-click="handleMenuClick"
        ></buyer-menu>
      </el-aside>

      <!-- 主要内容区域 -->
      <el-main class="right-main">
        <keep-alive>
          <component v-if="!isLoading"
                     :is-translated="isTranslated"
                     :is="currentComponent"
                     :key="activeIndex"
          ></component>
          <div v-else class="loading-indicator">{{ $t(isTranslated ? 'order.messages.loading_en' : 'order.messages.loading_zh') }}</div>
        </keep-alive>
      </el-main>
    </div>
    <TranslateButton
      v-model:isTranslated="isTranslated"
      class="floating-translate-btn"
    />
  </el-container>
</template>

<script lang="ts" setup>
import BuyerOrderFinish from "@/components/block-buyer/buyer-order-finish.vue";
import BuyerOrderIng from "@/components/block-buyer/buyer-order-ing.vue";
import BuyerOrderUn from "@/components/block-buyer/buyer-order-un.vue";
import BuyerInfo from "@/components/block-buyer/buyer-info.vue";
import BuyerAccountInfo from "@/components/block-buyer/buyer-account-info.vue";
import BuyerTradeInfo from "@/components/block-buyer/buyer-trade-info.vue";
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import { jwtDecode } from "jwt-decode";
import BuyerMenu from "@/components/block-buyer/buyer-menu.vue";
import BuyerSearch from "@/components/block-search/buyer-search.vue";
import ShopLogo from "@/components/block/Shop-Logo.vue";
import BuyerHeader from "@/components/block-buyer/buyer-header.vue";
import BuyerAddressInfo from "@/components/block-buyer/buyer-address-info.vue";
import BuyerAftersaleUn from "@/components/block-buyer/buyer-aftersale-un.vue";
import BuyerAftersaleFinish from "@/components/block-buyer/buyer-aftersale-finish.vue";
import axios from "axios";
import { ElMessage } from "element-plus";
import http from "@/utils/re.js";
import TranslateButton from "@/components/block/translate.vue";
import { useI18n } from 'vue-i18n';
// 使用菜单索引作为组件唯一标识
const activeIndex = ref('1');
const currentComponent = ref(BuyerInfo);
const isSwitching = ref(false);
const isLoading = ref(false);
const isTranslated = ref(false);

const handleMenuClick = async (index: string) => {
  if (isSwitching.value) return;
  isSwitching.value = true;
  isLoading.value = true; // 显示加载状态
  try {
    activeIndex.value = index;
    switch (index) {
      case '1':
        currentComponent.value = BuyerInfo;
        break;
      case '2':
        currentComponent.value = BuyerOrderUn;
        break;
      case '3':
        currentComponent.value = BuyerOrderIng;
        break;
      case '4':
        currentComponent.value = BuyerOrderFinish;
        break;
      case '5':
        currentComponent.value = BuyerTradeInfo;
        break;
      case '6':
        currentComponent.value = BuyerAddressInfo;
        break;
      case '7':
        currentComponent.value = BuyerAftersaleUn;
        break;
      case '8':
        currentComponent.value = BuyerAftersaleFinish;
        break;
      case '9':
        currentComponent.value = BuyerAccountInfo;
        break;
    }
    // 模拟数据加载延迟
    await new Promise(resolve => setTimeout(resolve, 300));
  } finally {
    isLoading.value = false; // 隐藏加载状态
    setTimeout(() => {
      isSwitching.value = false;
    }, 300);
  }
};

const route = useRoute();
const token = ref(route.query.token);
const decodedToken = ref({});

onMounted(async () => {
  if (token.value) {
    try {
      decodedToken.value = jwtDecode(token.value as string);
    } catch (error) {
      console.error("Failed to decode token:", error);
    }
  }

  if (route.query.status === 'success') {
    isLoading.value = true; // 显示加载状态
    await handleMenuClick('3');
    ElMessage.success(isTranslated.value ? 'Order payment successful' : '订单支付已成功');
  } else if (route.query.status === 'failed') {
    await handleMenuClick('3');
    ElMessage.error(isTranslated.value ? 'Order payment failed' : '订单支付失败');
  } else if (route.query.status === 'error') {
    await handleMenuClick('3');
    ElMessage.error(isTranslated.value ? 'Order payment error, please contact the merchant!' : '订单支付异常，请联系商家！');
  } else if (route.query.status === 'cancel') {
    await handleMenuClick('3');
    ElMessage.error(isTranslated.value ? 'Payment cancelled!' : '支付取消！');
  }
});
</script>

<style scoped>
.layout-container-demo {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.header-middle {
  display: flex;
  padding: 10px 20px;
  background: #ffffff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.shop-logo {
  width: 300px;
}

.main-container {
  flex: 1;
  display: flex;
  min-height: 0;
}

.left-aside {
  background: var(--el-color-primary-light-8);
}

.right-main {
  flex: 1;
  background-color: #f5f7fa;
  padding: 20px;
  overflow: auto;
  position: relative;
}

.loading-indicator {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  color: #909399;
  font-size: 14px;
}

/* 翻译按钮样式 */
.floating-translate-btn {
  position: fixed;
  bottom: 30px;
  right: 30px;
  z-index: 1000;
}
</style>
