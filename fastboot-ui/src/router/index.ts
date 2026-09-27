import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import AdminLayout from '@/layouts/AdminLayout.vue'
import { useUserStore } from '@/stores/user'

const systemViewLoaders: Record<string, () => Promise<unknown>> = {
  user: () => import('@/views/system/user/index.vue'),
  role: () => import('@/views/system/role/index.vue'),
  menu: () => import('@/views/system/menu/index.vue'),
  org: () => import('@/views/system/org/index.vue'),
  post: () => import('@/views/system/post/index.vue'),
  tenant: () => import('@/views/system/tenant/index.vue'),
  dict: () => import('@/views/system/dict/index.vue'),
  config: () => import('@/views/system/config/index.vue'),
  notice: () => import('@/views/system/notice/index.vue'),
  'error-code': () => import('@/views/system/error-code/index.vue'),
  'operation-log': () => import('@/views/system/operation-log/index.vue'),
  'login-log': () => import('@/views/system/login-log/index.vue'),
  'online-user': () => import('@/views/system/online-user/index.vue'),
}

const infraViewLoaders: Record<string, () => Promise<unknown>> = {
  file: () => import('@/views/infra/file/index.vue'),
  job: () => import('@/views/infra/job/index.vue'),
  inbox: () => import('@/views/infra/inbox/index.vue'),
}

const systemRoutes: RouteRecordRaw[] = [
  ['user', '用户管理'],
  ['role', '角色管理'],
  ['menu', '菜单管理'],
  ['org', '组织管理'],
  ['post', '岗位管理'],
  ['tenant', '租户管理'],
  ['dict', '字典管理'],
  ['config', '参数配置'],
  ['notice', '通知公告'],
  ['error-code', '错误码'],
  ['operation-log', '操作日志'],
  ['login-log', '登录日志'],
  ['online-user', '在线用户'],
].map(([path, title]) => ({
  path: `/system/${path}`,
  name: `system-${path}`,
  component: systemViewLoaders[path],
  meta: { title },
}))

const infraRoutes: RouteRecordRaw[] = [
  ['file', '文件管理'],
  ['job', '定时任务'],
  ['inbox', '站内信'],
].map(([path, title]) => ({
  path: `/infra/${path}`,
  name: `infra-${path}`,
  component: infraViewLoaders[path],
  meta: { title },
}))

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/login/index.vue'),
      meta: { public: true },
    },
    {
      path: '/',
      component: AdminLayout,
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'dashboard',
          component: () => import('@/views/dashboard/index.vue'),
          meta: { title: '仪表盘' },
        },
        ...systemRoutes,
        ...infraRoutes,
        {
          path: 'bpm',
          name: 'bpm',
          component: () => import('@/views/bpm/index.vue'),
          meta: { title: '流程中心' },
        },
        {
          path: 'profile',
          name: 'profile',
          component: () => import('@/views/profile/index.vue'),
          meta: { title: '个人中心' },
        },
      ],
    },
  ],
})

router.beforeEach(async (to) => {
  if (to.meta.public) return true
  const user = useUserStore()
  if (!user.token) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }
  if (!user.profile) {
    try {
      await user.fetchProfile()
    } catch {
      await user.logout()
      return { path: '/login' }
    }
  }
  return true
})

export default router
