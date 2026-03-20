import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import WindiCSS from 'vite-plugin-windicss'
import mkcert from 'vite-plugin-mkcert' // 新增证书插件

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    WindiCSS(),
    mkcert({
      source: 'coding'  // 新增配置参数
    })
  ],
  server: {
    https: true, // 启用HTTPS
    host: true,   // 允许外部访问
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})
