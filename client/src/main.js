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

// ✅ 修复：合并为一个请求拦截器
axios.interceptors.request.use(
  (config) => {
    console.log("请求拦截器:", config.method, config.url);

    // // 1. 处理_token接口 - 去掉_token前缀
    // if (config.url && config.url.includes("/_token/")) {
    //   const newUrl = config.url.replace("/_token/", "/");
    //   console.log(`修改URL: ${config.url} -> ${newUrl}`);
    //   config.url = newUrl;
    // }

    // 2. 添加token（如果需要）
    const token = adminStore.token || localStorage.getItem("token");
    if (token) {
      console.log("添加Authorization头");
      config.headers.Authorization = `Bearer ${token}`;
    } else {
      console.log("未找到token");
    }

    // 3. 确保有必要的头部
    if (!config.headers["Content-Type"]) {
      config.headers["Content-Type"] = "application/json";
    }

    // 4. 确保cancelToken存在（修复你遇到的错误）
    if (!config.cancelToken) {
      const CancelToken = axios.CancelToken;
      const source = CancelToken.source();
      config.cancelToken = source.token;
    }

    console.log("最终请求配置:", {
      url: config.url,
      headers: config.headers,
      method: config.method,
    });

    return config;
  },
  (error) => {
    console.error("请求拦截器错误:", error);
    return Promise.reject(error);
  },
);

// ✅ 修复：添加响应拦截器处理字段映射
axios.interceptors.response.use(
  (response) => {
    console.log("响应拦截器:", response.status, response.config.url);

    // 将后端的message字段映射为前端的msg字段
    if (response.data && response.data.message !== undefined) {
      response.data.msg = response.data.message;
    }

    // 打印响应数据用于调试
    console.log("响应数据:", response.data);

    return response;
  },
  (error) => {
    console.error("响应错误:", error);
    console.error("状态码:", error.response?.status);
    console.error("错误数据:", error.response?.data);

    if (error.response?.status === 401) {
      // token过期，跳转到登录页
      console.log("Token过期，跳转登录页");
      localStorage.removeItem("token");
      router.push("/login");
    }

    return Promise.reject(error);
  },
);

app.provide("axios", axios);
app.provide("message", message);
app.provide("dialog", dialog);
app.provide("server_url", axios.defaults.baseURL);
app.provide("notification", notification);

app.use(naive);
app.use(router);
app.mount("#app");
