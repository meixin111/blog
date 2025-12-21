import { createRouter, createWebHashHistory } from "vue-router";

let routes = [
  {
    path: "/test",
    component: () => import("../views/test.vue"),
  },
  {
    path: "/login",
    component: () => import("../views/login.vue"),
  },
  {
    path: "/",
    component: () => import("../views/HomePage.vue"),
  },
  {
    path: "/detail",
    component: () => import("../views/Detail.vue"),
  },
  {
    path: "/dashboard",
    component: () => import("../views/dashboard/Dashboard.vue"),
    children: [
      {
        path: "category",
        component: () => import("../views/dashboard/Category.vue"),
      },
      {
        path: "article",
        component: () => import("../views/dashboard/Article.vue"),
      },
    ],
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export { router, routes };
