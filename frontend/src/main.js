import './assets/main.css'
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
//import store from './store'
import './assets/global.css'
import axios from 'axios'
import i18n from './locales'
const app = createApp(App)

import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import VueQuillEditor from 'vue-quill-editor';
import 'quill/dist/quill.core.css';
import 'quill/dist/quill.snow.css';
import 'quill/dist/quill.bubble.css';

// 定义点击特效的指令
const clickEffect = {
  mounted(el) {
    el.addEventListener('click', createClickEffect)
  },
  beforeUnmount(el) {
    el.removeEventListener('click', createClickEffect)
  }
}

function createClickEffect(e) {
  // 生成随机颜色
  const hue = Math.random() * 360;

  // 创建粒子容器
  const container = document.createElement('div');
  container.style.cssText = `
    position: fixed;
    left: ${e.clientX}px;
    top: ${e.clientY}px;
    pointer-events: none;
    z-index: 9999;
  `;

  for (let i = 0; i < 32; i++) {
    const particle = document.createElement('div');
    const angle = (i * 22.5) * (Math.PI / 180);  // 减小角度间隔到22.5度
    const distance = 40 + Math.random() * 20;

    particle.style.cssText = `
      position: absolute;
      width: 4px;
      height: 4px;
      background-color: hsl(${(hue + i * 10) % 360}, 100%, 50%);
      border-radius: 50%;
      transform: translate(-50%, -50%);
      animation: particle 0.6s ease-out forwards;
      will-change: transform, opacity;
    `;

    // 粒子动画
    particle.animate([
      {
        transform: 'translate(0, 0) scale(0.2)',
        opacity: 1
      },
      {
        transform: `translate(${Math.cos(angle) * distance}px, ${Math.sin(angle) * distance}px) scale(1)`,
        opacity: 0
      }
    ], {
      duration: 600 + Math.random() * 200,
      easing: 'cubic-bezier(0.25, 0.46, 0.45, 0.94)',
      fill: 'forwards'
    });

    container.appendChild(particle);
  }

  document.body.appendChild(container);

  // 动画结束后移除容器
  setTimeout(() => {
    container.remove();
  }, 1000);
}

// 定义CSS动画
const styleSheet = document.createElement('style');
styleSheet.textContent = `
  @keyframes rippleEffect {
    0% {
      transform: translate(-50%, -50%) scale(0.2) rotate(0deg);
      opacity: 1;
      box-shadow: 0 0 20px currentColor;
    }
    50% {
      transform: translate(-50%, -50%) scale(2) rotate(22.5deg);
      opacity: 0.8;
      box-shadow: 0 0 30px currentColor;
    }
    100% {
      transform: translate(-50%, -50%) scale(4) rotate(45deg);
      opacity: 0;
      box-shadow: 0 0 10px currentColor;
    }
  }

    @keyframes particle {
    0% {
      transform: translate(0, 0) scale(0.2);
      opacity: 1;
    }
    100% {
      opacity: 0;
    }
  }

  .click-effect {
    pointer-events: none;
    z-index: 9999;
  }

  .particle {
    pointer-events: none;
  }
`;
document.head.appendChild(styleSheet);

// 全局注册指令
app.directive('click-effect', clickEffect);

// 注册Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

// 使用Element Plus和其他插件
app.use(i18n);
app.use(VueQuillEditor);
app.use(router);
app.config.globalProperties.$axios = axios;
app.use(ElementPlus);

// 挂载应用
app.mount('#app');
