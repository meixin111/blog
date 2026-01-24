<template>
  <div class="login-panel">
    <n-card title="登录页面">
      <n-form :rules="rules" :model="admin">
        <n-form-item path="account" label="账号">
          <n-input
            v-model:value="admin.account"
            type="account"
            placeholder="请输入账号"
          />
        </n-form-item>
        <n-form-item path="password" label="密码">
          <n-input
            v-model:value="admin.password"
            type="password"
            placeholder="请输入密码"
          />
        </n-form-item>
      </n-form>
      <template #footer>
        <n-checkbox v-model:checked="admin.rember" label="记住我" />
        <n-button @click="login">登录</n-button>
      </template>
    </n-card>
  </div>
</template>

<script setup>
import { ref, reactive, inject } from "vue";
import { AdminStore } from "../stores/AdminStore";
import { useRouter, useRoute } from "vue-router";

const message = inject("message");
const axios = inject("axios");
const adminStore = AdminStore();

const router = useRouter();
const route = useRoute();

let rules = {
  account: [
    { required: true, message: "请输入账号", trigger: "blur" },
    { min: 3, max: 12, message: "账号长度在3-12个字符", trigger: "blur" },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, max: 18, message: "密码长度在6-18个字符", trigger: "blur" },
  ],
};

const admin = reactive({
  account: localStorage.getItem("account") || "",
  password: localStorage.getItem("password") || "", // 修正：从password获取
  rember: localStorage.getItem("rember") == 1 || false, // 修正：字符串比较 如果后端写好后这里仍然无法remember那就把这里和下面的1都加上双引号
});

const login = async () => {
  let result = await axios.post("/admin/login", {
    account: admin.account,
    password: admin.password,
  });
  if (result.data.code === 200) {
    adminStore.token = result.data.data.token;
    adminStore.account = result.data.data.account;
    adminStore.id = result.data.data.id;

    if (admin.rember) {
      localStorage.setItem("account", admin.account); // 修正：使用admin.account
      localStorage.setItem("password", admin.password); // 存储密码
      localStorage.setItem("id", result.data.data.id); // 修正：使用正确的键名
      localStorage.setItem("rember", admin.rember ? 1 : 0); // 修正：使用正确的键名
    } else {
      // 如果不记住，清除保存的数据
      localStorage.removeItem("account");
      localStorage.removeItem("password");
      localStorage.removeItem("id");
      localStorage.removeItem("rember");
    }
    router.push("/dashboard");
    message.info("登录成功");
    localStorage.setItem("token", result.data.data.token);
    console.log("Token已存储到localStorage:", result.data.data.token);
  } else {
    message.error("登录失败");
  }
  console.log(result);
};
</script>

<style lang="scss" scoped>
.login-panel {
  width: 500px;
  margin: 0 auto;
  margin-top: 130px;
}
</style>
