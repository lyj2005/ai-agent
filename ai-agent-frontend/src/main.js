import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'

// 全局 axios 默认配置（方便未来使用）
axios.defaults.baseURL = 'http://localhost:8123/api'

const app = createApp(App)
app.config.globalProperties.$axios = axios
app.use(router)
app.mount('#app')
