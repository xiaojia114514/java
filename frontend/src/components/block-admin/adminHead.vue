<script setup>
import router from "@/router/index.js";
import { defineProps } from 'vue';

const props = defineProps({
  isTranslated: {
    type: Boolean,
    default: false
  }
});
const outLog = () => {
  localStorage.removeItem('token');
  router.push('/');
};
</script>

<template>
  <!-- 工具栏 -->
  <div class="toolbar" style="flex-grow: 1;display: contents;height: 80px;width: 100%">
    <div style="font-size: 20px;font-family: SimSun, sans-serif;">
      {{ $t(props.isTranslated ? 'translate.admin.welcome_en' : 'translate.admin.welcome_zh') }}
    </div>
    <el-dropdown class="admin-dropdown">
      <el-icon class="admin-info-wrapper">
        <span class="admin-name">
          <el-icon class="arrow-icon"><ArrowDown /></el-icon>
          {{ $t(props.isTranslated ? 'translate.admin.manager_en' : 'translate.admin.manager_zh') }}
        </span>
      </el-icon>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click="outLog">
            {{ $t(props.isTranslated ? 'translate.admin.logout_en' : 'translate.admin.logout_zh') }}
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<style scoped>
.admin-dropdown {
  position: absolute;
  right: 20px;
}

.admin-info-wrapper {
  display: flex;
  align-items: center;
  max-width: 400px;
  min-width: 100px;
}

.admin-name {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 15px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 400px;
  height: 20px;
}

.arrow-icon {
  transition: transform 0.2s;
  flex-shrink: 0;
}

.admin-dropdown:hover .arrow-icon {
  transform: rotate(180deg);
}

@media (max-width: 768px) {
  .admin-name {
    max-width: 400px;
  }
}
</style>
