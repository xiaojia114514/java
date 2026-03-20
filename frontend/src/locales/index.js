import { createI18n } from 'vue-i18n'
import en from './lang/en.json'
import zh from './lang/zh.json'

const messages = {
  en,
  zh
}

const i18n = createI18n({
  legacy: false,
  locale: localStorage.getItem('language') || 'zh', // 默认语言
  fallbackLocale: 'en',
  messages
})

export async function loadLocaleMessages(locale) {
  const response = await fetch(`${import.meta.env.BASE_URL}locales/${locale}.json`)
  const messages = await response.json()
  i18n.global.setLocaleMessage(locale, messages)
  return i18n
}

// 初始化加载默认语言
loadLocaleMessages('zh-CN')

export default createI18n({
  legacy: false,
  locale: 'zh-CN',
  messages: {
    'zh-CN': zh,
    'en-US': en
  }
})
