<template>
  <div>
    <n-button @click="showAddModel = true">添加</n-button>
    <n-table :bordered="false" :single-line="false">
      <thead>
        <tr>
          <th>编号</th>
          <th>名字</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(category, index) in categoryList">
          <td>{{ category.id }}</td>
          <td>{{ category.name }}</td>
          <td>
            <n-space>
              <n-button @click="toUpdate(category)">修改</n-button>
              <n-button @click="deleteCategory(category)">删除</n-button>
            </n-space>
          </td>
        </tr>
      </tbody>
    </n-table>

    <n-modal v-model:show="showAddModel" preset="dialog" title="Dialog">
      <template #header>
        <div>添加分类</div>
      </template>
      <div>
        <n-input
          v-model:value="addCategory.name"
          type="text"
          placeholder="请输入文本"
        />
      </div>
      <template #action>
        <div>
          <n-button @click="add">提交</n-button>
        </div>
      </template>
    </n-modal>

    <n-modal v-model:show="showUpdateModel" preset="dialog" title="Dialog">
      <template #header>
        <div>修改分类</div>
      </template>
      <div>
        <n-input
          v-model:value="updateCategory.name"
          type="text"
          placeholder="请输入文本"
        />
      </div>
      <template #action>
        <div>
          <n-button @click="update">提交</n-button>
        </div>
      </template>
    </n-modal>
  </div>
</template>

<script setup>
import { AdminStore } from "../../stores/AdminStore";
import { ref, reactive, inject, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";

const message = inject("message");
const axios = inject("axios");
const dialog = inject("dialog");
const adminStore = AdminStore();
const router = useRouter();
const categoryList = ref([]);
const route = useRoute();
const showAddModel = ref(false);
const showUpdateModel = ref(false);
const addCategory = reactive({
  name: "",
});
const updateCategory = reactive({
  id: 0,
  name: "",
});

onMounted(() => {
  loadDatas();
});

const add = async () => {
  let res = await axios.post("/category/_token/add", {
    name: addCategory.name,
  });
  if (res.data.code == 200) {
    loadDatas();
    message.info(res.data.msg);
  } else {
    message.error(res.data.msg);
  }
  showAddModel.value = false;
};
const loadDatas = async () => {
  let res = await axios.get("/category/list");
  categoryList.value = res.data.rows;
  console.log(res);
};
const toUpdate = async (category) => {
  showUpdateModel.value = true;
  updateCategory.id = category.id;
  updateCategory.name = category.name;
};
const update = async () => {
  let res = await axios.put("/category/_token/update", {
    name: updateCategory.name,
    id: updateCategory.id,
  });
  if (res.data.code == 200) {
    loadDatas();
    message.info(res.data.msg);
  } else {
    message.error(res.data.msg);
  }
  showUpdateModel.value = false;
  console.log(res);
};

const deleteCategory = async (category) => {
  dialog.warning({
    title: "警告",
    content: "你确定要删除吗？",
    positiveText: "确定",
    negativeText: "取消",
    draggable: true,
    onPositiveClick: async () => {
      let res = await axios.post(`/category/_token/delete?id=${category.id}`);
      if (res.data.code == 200) {
        message.info(res.data.msg);
        loadDatas(); // 刷新列表
      } else {
        message.error(res.data.msg);
      }
    },
    onNegativeClick: () => {
      message.info("已取消删除");
    },
  });
};
</script>

<style lang="scss" scoped></style>
