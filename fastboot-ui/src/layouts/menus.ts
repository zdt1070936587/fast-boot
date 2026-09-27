import type { Component } from 'vue'
import {
  Odometer,
  Setting,
  Tools,
  User,
  UserFilled,
  Menu,
  OfficeBuilding,
  Suitcase,
  Shop,
  Collection,
  SetUp,
  Bell,
  Warning,
  Document,
  Key,
  Connection,
  Folder,
  Timer,
  Message,
  MagicStick,
  Postcard,
} from '@element-plus/icons-vue'

export interface MenuLeaf {
  path: string
  title: string
  icon?: Component
  desc?: string
}

export interface MenuGroup {
  title: string
  icon: Component
  children: MenuLeaf[]
}

export type MenuEntry = (MenuLeaf & { icon: Component }) | MenuGroup

export function isGroup(entry: MenuEntry): entry is MenuGroup {
  return 'children' in entry
}

export const menus: MenuEntry[] = [
  { path: '/dashboard', title: '工作台', icon: Odometer, desc: '系统概览与关键指标' },
  {
    title: '系统管理',
    icon: Setting,
    children: [
      { path: '/system/user', title: '用户', icon: User, desc: '账号、状态与所属组织' },
      { path: '/system/role', title: '角色', icon: UserFilled, desc: '角色编码与权限分配' },
      { path: '/system/menu', title: '菜单', icon: Menu, desc: '路由、组件与权限编码' },
      { path: '/system/org', title: '组织', icon: OfficeBuilding, desc: '组织架构树' },
      { path: '/system/post', title: '岗位', icon: Suitcase, desc: '岗位编码与排序' },
      { path: '/system/tenant', title: '租户', icon: Shop, desc: '租户套餐与到期时间' },
      { path: '/system/dict', title: '字典', icon: Collection, desc: '字典类型与数据' },
      { path: '/system/config', title: '参数', icon: SetUp, desc: '系统运行参数' },
      { path: '/system/notice', title: '通知', icon: Bell, desc: '通知公告' },
      { path: '/system/error-code', title: '错误码', icon: Warning, desc: '多语言错误码' },
      { path: '/system/operation-log', title: '操作日志', icon: Document, desc: '操作审计' },
      { path: '/system/login-log', title: '登录日志', icon: Key, desc: '登录 / 登出事件' },
      { path: '/system/online-user', title: '在线用户', icon: Connection, desc: '当前会话' },
    ],
  },
  {
    title: '基础设施',
    icon: Tools,
    children: [
      { path: '/infra/file', title: '文件', icon: Folder, desc: '对象存储文件' },
      { path: '/infra/job', title: '任务', icon: Timer, desc: '定时任务调度' },
      { path: '/infra/inbox', title: '站内信', icon: Message, desc: '站内消息' },
    ],
  },
  { path: '/bpm', title: '流程中心', icon: MagicStick, desc: '流程设计与审批' },
  { path: '/profile', title: '个人中心', icon: Postcard, desc: '账号信息' },
]

export interface FlatMenu extends MenuLeaf {
  group?: string
  icon: Component
}

export const flatMenus: FlatMenu[] = menus.flatMap((entry) =>
  isGroup(entry)
    ? entry.children.map((c) => ({ ...c, icon: c.icon ?? entry.icon, group: entry.title }))
    : [{ ...entry, icon: entry.icon }],
)

export function findMenu(path: string): FlatMenu | undefined {
  return flatMenus.find((m) => m.path === path)
}
