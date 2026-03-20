<script setup>
import { Edit, Menu, Setting, User, Warning } from "@element-plus/icons-vue";
import { defineEmits, defineProps, ref } from 'vue';
import { useI18n } from 'vue-i18n';

const { t: $t } = useI18n();

const emits = defineEmits(['menu-click']);
const props = defineProps({
  activeIndex: {
    type: String,
    default: '1'
  },
  isTranslated: {
    type: Boolean,
    default: false
  }
});

// 子菜单展开状态
const isOrderMenuOpen = ref(false);
const isDeliveryInfoOpen = ref(false);
const isAfterSaleMenuOpen = ref(false);

const handleSelect = (index) => {
  emits('menu-click', index);
};
</script>

<template>
  <el-menu
    :default-active="props.activeIndex"
    :default-openeds="['1', '4', '5', '6', 'after-sale']"
    style="flex:1; background-color: var(--el-color-primary-light-8)"
    @select="handleSelect"
  >
    <!-- 个人信息 -->
    <el-menu-item-group>
      <el-menu-item index="1">
        <el-icon><User /></el-icon>
        {{ $t(props.isTranslated ? 'translate.menu.personalInfo_en' : 'translate.menu.personalInfo_zh') }}
      </el-menu-item>
    </el-menu-item-group>

    <!-- 我的订单 -->
    <el-sub-menu index="2" v-model:opened="isOrderMenuOpen">
      <template #title>
        <el-icon><Menu /></el-icon>
        {{ $t(props.isTranslated ? 'translate.menu.myOrders_en' : 'translate.menu.myOrders_zh') }}
      </template>
      <el-menu-item-group>
        <el-menu-item index="2">
          {{ $t(props.isTranslated ? 'translate.menu.pendingOrders_en' : 'translate.menu.pendingOrders_zh') }}
        </el-menu-item>
        <el-menu-item index="3">
          {{ $t(props.isTranslated ? 'translate.menu.acceptedOrders_en' : 'translate.menu.acceptedOrders_zh') }}
        </el-menu-item>
        <el-menu-item index="4">
          {{ $t(props.isTranslated ? 'translate.menu.historicalOrders_en' : 'translate.menu.historicalOrders_zh') }}
        </el-menu-item>
      </el-menu-item-group>
    </el-sub-menu>

    <!-- 收货管理 -->
    <el-sub-menu index="5" v-model:opened="isDeliveryInfoOpen">
      <template #title>
        <el-icon><Setting /></el-icon>
        {{ $t(props.isTranslated ? 'translate.menu.deliveryManagement_en' : 'translate.menu.deliveryManagement_zh') }}
      </template>
      <el-menu-item-group>
        <el-menu-item index="5">
          {{ $t(props.isTranslated ? 'translate.menu.phoneManagement_en' : 'translate.menu.phoneManagement_zh') }}
        </el-menu-item>
        <el-menu-item index="6">
          {{ $t(props.isTranslated ? 'translate.menu.addressManagement_en' : 'translate.menu.addressManagement_zh') }}
        </el-menu-item>
      </el-menu-item-group>
    </el-sub-menu>

    <!-- 售后管理 -->
    <el-sub-menu index="after-sale" v-model:opened="isAfterSaleMenuOpen">
      <template #title>
        <el-icon><Warning /></el-icon>
        {{ $t(props.isTranslated ? 'translate.menu.afterSalesManagement_en' : 'translate.menu.afterSalesManagement_zh') }}
      </template>
      <el-menu-item-group>
        <el-menu-item index="7">
          {{ $t(props.isTranslated ? 'translate.menu.pendingAppeals_en' : 'translate.menu.pendingAppeals_zh') }}
        </el-menu-item>
        <el-menu-item index="8">
          {{ $t(props.isTranslated ? 'translate.menu.processedAppeals_en' : 'translate.menu.processedAppeals_zh') }}
        </el-menu-item>
      </el-menu-item-group>
    </el-sub-menu>

    <!-- 账号管理 -->
    <el-menu-item-group>
      <el-menu-item index="9">
        <el-icon><Edit /></el-icon>
        {{ $t(props.isTranslated ? 'translate.menu.accountManagement_en' : 'translate.menu.accountManagement_zh') }}
      </el-menu-item>
    </el-menu-item-group>
  </el-menu>
</template>

<style scoped>
:deep(.el-menu) {
  background-color: inherit;
}

:deep(.el-menu-item.is-active) {
  background-color: var(--el-color-primary-light-5);
}

:deep(.el-menu-item:hover) {
  background-color: var(--el-color-primary-light-5);
}
</style>
