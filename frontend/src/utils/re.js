import axios from 'axios';

// 添加环境变量配置
const API_BASE = import.meta.env.VITE_API_BASE_URL || 'https://localhost:8443/api';

const http = axios.create({
  baseURL: API_BASE,  // 修改此处
  timeout: 30000,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json'
  }
});

// 请求拦截器
http.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `${token}`;
  }
  return config;
});

export default http;
