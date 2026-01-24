<template>
  <div class="container">
    <div class="nav">
      <div @click="homePage">首页</div>
      <div>
        <n-popselect
          @update:value="searchByCategory"
          v-model:value="selectedCategory"
          :options="categoryOptions"
          trigger="click"
        >
          <div>
            分类
            <span>{{ categoryName }}</span>
          </div>
        </n-popselect>
      </div>
      <div @click="dashboard">后台</div>
    </div>
    <n-pagination
      @update:page="loadBlogs"
      v-model:page="pageInfo.page"
      :page-count="pageInfo.pageCount"
    />

    <n-divider />

    <n-space class="search">
      <n-input
        v-model:value="pageInfo.keyword"
        style="width: 500px"
        placeholder="请输入关键词"
      />
      <n-button type="primary" ghost @click="loadBlogs(0)">搜索</n-button>
    </n-space>
    <div
      v-for="(blog, index) in blogListInfo"
      style="margin-bottom: 15px; cursor: pointer"
    >
      <n-card :title="blog.title" @click="toDetail(blog)">
        {{ blog.content }}
        <template #footer>
          <n-space align="center">
            <div>发布时间：{{ blog.create_time }}</div>
          </n-space>
        </template>
      </n-card>
    </div>
    <n-divider />
    <div class="footer">
      <div>Power by TCY</div>
      <div>没有备案纯爱写</div>
    </div>
  </div>
</template>

<script setup>
import { AdminStore } from "../stores/AdminStore";
import { ref, reactive, inject, onMounted, computed } from "vue";
import { useRouter, useRoute } from "vue-router";
import RichTextEditor from "../components/RichTextEditor.vue";
import Category from "./dashboard/Category.vue";

const message = inject("message");
const axios = inject("axios");
const dialog = inject("dialog");
const adminStore = AdminStore();
const router = useRouter();
const route = useRoute();

const selectedCategory = ref(0);
const categoryOptions = ref([]);
const blogListInfo = ref([]);

const pageInfo = reactive({
  page: 1,
  pagesize: 3,
  pageCount: 0,
  count: 0,
  keyword: "",
  categoryId: 0,
});

onMounted(() => {
  loadCategorys();
  loadBlogs();
});

const searchByCategory = (category) => {
  pageInfo.categoryId = category;
  loadBlogs();
};
const toDetail = (blog) => {
  router.push({ path: "/detail", query: { id: blog.id } });
};

const homePage = () => {
  router.push("/");
};

const dashboard = () => {
  router.push("/login");
};

const categoryName = computed(() => {
  let selectedOption = categoryOptions.value.find((option) => {
    return option.value == selectedCategory.value;
  });
  return selectedOption ? selectedCategory.label : "";
});

const loadCategorys = async () => {
  let res = await axios.get("/category/list");
  categoryOptions.value = res.data.rows.map((item) => {
    return {
      label: item.name,
      value: item.id,
    };
  });
  console.log(categoryOptions.value);
};

const loadBlogs = async (page = 0) => {
  if (page != 0) {
    pageInfo.page = page;
  }
  let res = await axios.get(
    `/blog/search?keyword=${pageInfo.keyword}&page=${pageInfo.page}&pagesize=${pageInfo.pagesize}&categoryId=${pageInfo.categoryId}`,
  );
  let temp_rows = res.data.data.rows;
  for (let rows of temp_rows) {
    row.content += "...";
    let d = new Date(rows.create_time);
    rows.create_time = `${d.getFullYear()}年${d.getMonth + 1}月${d.getDate()}日`;
  }
  blogListInfo.value = temp_rows;
  pageInfo.count = res.data.data.count;
  pageInfo.pageCount =
    parseInt(pageInfo.count / pageInfo.pagesize) +
      (pageInfo.count % pageInfo.pagesize) >
    0
      ? 1
      : 0;
  console.log(res);
};
</script>

<style lang="scss" scoped>
.search {
  margin-bottom: 50px;
}
.container {
  width: 1200px;
  margin: 0 auto;
}
.nav {
  display: flex;
  font-size: 20px;
  padding-top: 20px;
  color: #64676a;
  div {
    cursor: pointer;
    margin-right: 15px;

    &:hover {
      color: #f60;
    }
    span {
      font-size: 12px;
    }
  }
}
.footer {
  text-align: center;
  line-height: 25px;
  color: #64676a;
}
</style>
