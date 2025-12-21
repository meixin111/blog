import { createApp } from "vue";
import "./style.css";
import { createPinia } from "pinia";
import App from "./App.vue";
import naive from "naive-ui";
import { darkTheme } from "naive-ui";
import { lightTheme } from "naive-ui";
import { createDiscreteApi } from "naive-ui";
import { router } from "./common/router";
import axios from "axios";
import { AdminStore } from "./stores/AdminStore";

axios.defaults.baseURL = "http://localhost:25565";

const { message, notification, dialog } = createDiscreteApi([
  "message",
  "dialog",
  "notification",
]);
const pinia = createPinia();
const app = createApp(App);
app.use(pinia);
const adminStore = AdminStore();

app.provide("axios", axios);
app.provide("message", message);
app.provide("dialog", dialog);
app.provide("server_url", axios.defaults.baseURL);
app.provide("notification", notification);
app.use(naive);
app.use(router);
app.mount("#app");

/**
 * axios
 * pinia
 * sass
 * vue-router
 * naive-ui
 * wangeditor\
 * 

 */
axios.interceptors.request.use((config) => {
  config.headers.token = adminStore.token;
  return config;
});
