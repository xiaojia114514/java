<template>
  <el-tooltip
    effect="light"
    :content="$t(isTranslated ? 'translate.detail.switch_zh' : 'translate.detail.translate_en')"
    placement="left"
  >
    <el-button
      class="floating-translate-btn"
      :class="{ 'is-translated': isTranslated }"
      @click="$emit('update:isTranslated', !isTranslated)"
      circle
    >
      {{ isTranslated ? 'ZH' : 'EN' }}
    </el-button>
  </el-tooltip>
</template>

<script>
import { defineComponent } from 'vue'
import { ElTooltip, ElButton } from 'element-plus'
import { useI18n } from 'vue-i18n';
export default defineComponent({
  components: {
    ElTooltip,
    ElButton
  },
  props: {
    isTranslated: {
      type: Boolean,
      default: false
    }
  },
  emits: ['update:isTranslated']
})
</script>

<style scoped>
.floating-translate-btn {
  /* 保持原有样式不变 */
  position: fixed;
  right: 30px;
  bottom: 80px;
  width: 56px;
  height: 56px;
  z-index: 9999;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  font-weight: bold;
  font-size: 18px;
}

.floating-translate-btn:hover {
  transform: scale(1.1) translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
}

.floating-translate-btn.is-translated {
  background-color: #ffc107;
  border-color: #ffc107;
  color: #333;
}

@media (max-width: 768px) {
  .floating-translate-btn {
    width: 48px;
    height: 48px;
    font-size: 16px;
    right: 20px;
    bottom: 60px;
  }
}
</style>
