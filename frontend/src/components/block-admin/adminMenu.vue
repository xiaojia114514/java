<script setup>
import {ChatDotRound, Coin, Edit, Fold, List, Menu, Promotion, Setting, User} from "@element-plus/icons-vue";
import {defineEmits, ref} from 'vue';
import { useI18n } from 'vue-i18n';

const { t: $t } = useI18n();
const emits = defineEmits(['menu-click']);
const props = defineProps({
  isTranslated: {
    type: Boolean,
    default: false
  }
});

const activeIndex = ref(''); // 当前激活菜单项
const menuOpenStates = ref({
  shelf: false,
  order: false,
  afterSale: false
});

const handleSelect = (index) => {
  activeIndex.value = index;
  emits('menu-click', index);
};
</script>

<template>
  <el-menu
    :default-openeds="['2', '5', '8']"
    style="background-color: var(--el-color-primary-light-9)"
    :default-active="activeIndex"
    @select="handleSelect"
  >
    <!-- 用户管理 -->
    <el-menu-item index="1">
      <el-icon>
        <User/>
      </el-icon>
      <span>{{ $t(props.isTranslated ? 'translate.menu.personalInfo_en' : 'translate.menu.personalInfo_zh') }}</span>
    </el-menu-item>

    <!-- 货架管理 -->
    <el-sub-menu index="2">
      <template #title>
        <el-icon>
          <Menu/>
        </el-icon>
        <span>{{ $t(props.isTranslated ? 'translate.menu.shelfManagement_en' : 'translate.menu.shelfManagement_zh') }}</span>
      </template>
      <el-menu-item index="2-1">{{ $t(props.isTranslated ? 'translate.menu.pendingShelf_en' : 'translate.menu.pendingShelf_zh') }}</el-menu-item>
      <el-menu-item index="2-2">{{ $t(props.isTranslated ? 'translate.menu.onShelf_en' : 'translate.menu.onShelf_zh') }}</el-menu-item>
      <el-menu-item index="2-3">{{ $t(props.isTranslated ? 'translate.menu.historicalShelf_en' : 'translate.menu.historicalShelf_zh') }}</el-menu-item>
    </el-sub-menu>

    <!-- 商品管理 -->
    <el-menu-item index="3">
      <el-icon>
        <Setting/>
      </el-icon>
      <span>{{ $t(props.isTranslated ? 'translate.menu.productManagement_en' : 'translate.menu.productManagement_zh') }}</span>
    </el-menu-item>

    <!-- 订单管理 -->
    <el-sub-menu index="4">
      <template #title>
        <el-icon>
          <Promotion/>
        </el-icon>
        <span>{{ $t(props.isTranslated ? 'translate.menu.orderManagement_en' : 'translate.menu.orderManagement_zh') }}</span>
      </template>
      <el-menu-item index="4-1">{{ $t(props.isTranslated ? 'translate.menu.pendingOrders_en' : 'translate.menu.pendingOrders_zh') }}</el-menu-item>
      <el-menu-item index="4-2">{{ $t(props.isTranslated ? 'translate.menu.acceptedOrders_en' : 'translate.menu.acceptedOrders_zh') }}</el-menu-item>
      <el-menu-item index="4-3">{{ $t(props.isTranslated ? 'translate.menu.completedOrders_en' : 'translate.menu.completedOrders_zh') }}</el-menu-item>
    </el-sub-menu>

    <!-- 售后管理 -->
    <el-sub-menu index="5">
      <template #title>
        <el-icon>
          <ChatDotRound/>
        </el-icon>
        <span>{{ $t(props.isTranslated ? 'translate.menu.afterSalesManagement_en' : 'translate.menu.afterSalesManagement_zh') }}</span>
      </template>
      <el-menu-item index="5-1">{{ $t(props.isTranslated ? 'translate.menu.pendingAppeals_en' : 'translate.menu.pendingAppeals_zh') }}</el-menu-item>
      <el-menu-item index="5-2">{{ $t(props.isTranslated ? 'translate.menu.processedAppeals_en' : 'translate.menu.processedAppeals_zh') }}</el-menu-item>
    </el-sub-menu>

    <!-- 分类管理 -->
    <el-menu-item index="6">
      <el-icon>
        <List/>
      </el-icon>
      <span>{{ $t(props.isTranslated ? 'translate.menu.categoryManagement_en' : 'translate.menu.categoryManagement_zh') }}</span>
    </el-menu-item>

    <!-- 库存历史管理 -->
    <el-menu-item index="7">
      <el-icon>
        <Coin/>
      </el-icon>
      <span>{{ $t(props.isTranslated ? 'translate.menu.stockHistoryManagement_en' : 'translate.menu.stockHistoryManagement_zh') }}</span>
    </el-menu-item>

    <!-- 账号管理 -->
    <el-menu-item index="8">
      <el-icon>
        <Fold/>
      </el-icon>
      <span>{{ $t(props.isTranslated ? 'translate.menu.accountManagement_en' : 'translate.menu.accountManagement_zh') }}</span>
    </el-menu-item>
  </el-menu>
</template>

<style scoped>
:deep(.el-menu-item.is-active) {
  background-color: var(--el-color-primary-light-5);
}
</style>
