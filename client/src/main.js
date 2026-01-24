import { createApp } from "vue";
import "./style.css";
import { createPinia } from "pinia";
import App from "./App.vue";
import naive from "naive-ui";
import { createDiscreteApi } from "naive-ui";
import { router } from "./common/router";
import axios from "axios";
import { AdminStore } from "./stores/AdminStore";

// ✅ 修复：端口改为8080
axios.defaults.baseURL = "http://localhost:8080";
axios.defaults.timeout = 10000;

const { message, notification, dialog } = createDiscreteApi([
    "message",
    "dialog",
    "notification",
]);
const pinia = createPinia();
const app = createApp(App);
app.use(pinia);
const adminStore = AdminStore();

// ✅ 修复：添加响应拦截器处理字段映射
axios.interceptors.response.use(
    response => {
        // 将后端的message字段映射为前端的msg字段
        if (response.data && response.data.message !== undefined) {
            response.data.msg = response.data.message;
        }
        return response;
    },
    error => {
        if (error.response?.status === 401) {
            // token过期，跳转到登录页
            localStorage.removeItem('token');
            router.push('/login');
        }
        return Promise.reject(error);
    }
);

// ✅ 修复：使用标准JWT Authorization头
axios.interceptors.request.use((config) => {
    const token = adminStore.token || localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

app.provide("axios", axios);
app.provide("message", message);
app.provide("dialog", dialog);
app.provide("server_url", axios.defaults.baseURL);
app.provide("notification", notification);

app.use(naive);
app.use(router);
app.mount("#app");