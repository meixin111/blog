<template>
  <div class="container">
    <n-button @click="back">返回</n-button>
    <!--标题-->
    <n-h1>{{ blogInfo.title }}</n-h1>
    <!--文章内容-->
    <div class="blog-content">
      <div v-html="blogInfo.content"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, inject, computed, onMounted } from "vue";
import { AdminStore } from "../stores/AdminStore";
import { useRouter, useRoute } from "vue-router";

const message = inject("message");
const axios = inject("axios");
const adminStore = AdminStore();
const blogInfo = ref({});
const router = useRouter();
const route = useRoute();

onMounted(() => {
  loadBlogs();
});

const loadBlogs = async () => {
  let res = await axios.get("/blog/detail?id=" + route.query.id);
  blogInfo.value = res.data.data;
};

const back = () => {
  router.push("/");
};
</script>
<style>
.blog-content img {
  max-width: 100% !important;
}
</style>
<style lang="scss" scoped>
.container {
  width: 1200px;
  margin: 0 auto;
}
</style>
