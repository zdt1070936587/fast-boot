import type { FbColumn } from '@/components/FbTable.vue'

/**
 * 各列表页的列定义，字段与后端 View / Entity 对齐。
 */
const id: FbColumn = { prop: 'id', label: 'ID', width: 90, type: 'id' }
const createdAt: FbColumn = { prop: 'createdAt', label: '创建时间', width: 160, type: 'datetime' }
const updatedAt: FbColumn = { prop: 'updatedAt', label: '更新时间', width: 160, type: 'datetime' }

export const moduleColumns: Record<string, FbColumn[]> = {
  user: [
    id,
    { prop: 'username', label: '用户名', minWidth: 140 },
    { prop: 'displayName', label: '显示名', minWidth: 140 },
    { prop: 'orgUnitId', label: '组织', width: 110, formatter: (_r, v) => (v ? `#${v}` : '—') },
    { prop: 'enabled', label: '状态', width: 110, type: 'bool', boolLabels: ['启用', '停用'] },
  ],
  role: [
    id,
    { prop: 'name', label: '角色名称', minWidth: 160 },
    { prop: 'code', label: '角色编码', minWidth: 160, type: 'mono' },
  ],
  menu: [
    id,
    { prop: 'name', label: '菜单名称', minWidth: 150 },
    { prop: 'path', label: '路由', minWidth: 160, type: 'mono' },
    { prop: 'component', label: '组件', minWidth: 160, type: 'mono' },
    { prop: 'permCode', label: '权限编码', minWidth: 160, type: 'mono' },
    { prop: 'parentId', label: '上级', width: 90, formatter: (_r, v) => (v ? `#${v}` : '根') },
  ],
  org: [
    id,
    { prop: 'name', label: '组织名称', minWidth: 180 },
    { prop: 'parentId', label: '上级组织', width: 110, formatter: (_r, v) => (v ? `#${v}` : '根') },
    { prop: 'path', label: '物化路径', minWidth: 200, type: 'mono' },
  ],
  post: [
    id,
    { prop: 'name', label: '岗位名称', minWidth: 160 },
    { prop: 'code', label: '岗位编码', minWidth: 140, type: 'mono' },
    { prop: 'sortNo', label: '排序', width: 90, align: 'center' },
    updatedAt,
  ],
  tenant: [
    id,
    { prop: 'name', label: '租户名称', minWidth: 160 },
    { prop: 'code', label: '租户编码', minWidth: 130, type: 'mono' },
    {
      prop: 'status',
      label: '状态',
      width: 110,
      type: 'tag',
      tagMap: {
        ACTIVE: { label: '正常', tone: 'up' },
        DISABLED: { label: '停用', tone: 'down' },
        EXPIRED: { label: '已过期', tone: 'warn' },
      },
    },
    { prop: 'packageId', label: '套餐', width: 100, formatter: (_r, v) => (v ? `#${v}` : '—') },
    { prop: 'contact', label: '联系人', minWidth: 120 },
    { prop: 'expireAt', label: '到期时间', width: 160, type: 'datetime' },
  ],
  dict: [
    id,
    { prop: 'name', label: '字典名称', minWidth: 160 },
    { prop: 'code', label: '字典编码', minWidth: 150, type: 'mono' },
    { prop: 'remark', label: '备注', minWidth: 200 },
    updatedAt,
  ],
  config: [
    id,
    { prop: 'configKey', label: '参数键', minWidth: 180, type: 'mono' },
    { prop: 'configValue', label: '参数值', minWidth: 200 },
    { prop: 'refreshable', label: '热更新', width: 100, type: 'bool', boolLabels: ['支持', '不支持'] },
    { prop: 'remark', label: '备注', minWidth: 160 },
  ],
  notice: [
    id,
    { prop: 'title', label: '标题', minWidth: 220 },
    {
      prop: 'status',
      label: '状态',
      width: 110,
      type: 'tag',
      tagMap: {
        PUBLISHED: { label: '已发布', tone: 'up' },
        DRAFT: { label: '草稿', tone: '' },
        ARCHIVED: { label: '已归档', tone: 'warn' },
      },
    },
    createdAt,
    updatedAt,
  ],
  'error-code': [
    id,
    { prop: 'code', label: '错误码', width: 120, type: 'mono' },
    { prop: 'locale', label: '语言', width: 100, type: 'tag', tagMap: { zh_CN: { label: '中文', tone: 'sky' }, en: { label: 'English', tone: 'accent' } } },
    { prop: 'message', label: '消息', minWidth: 260 },
  ],
  'operation-log': [
    id,
    { prop: 'action', label: '操作', minWidth: 160 },
    { prop: 'method', label: '方法', minWidth: 200, type: 'mono' },
    { prop: 'userId', label: '操作人', width: 100, formatter: (_r, v) => (v ? `#${v}` : '—') },
    { prop: 'success', label: '结果', width: 100, type: 'bool', boolLabels: ['成功', '失败'] },
    { prop: 'durationMs', label: '耗时', width: 100, align: 'right', formatter: (_r, v) => (v == null ? '—' : `${v} ms`) },
    { prop: 'clientIp', label: 'IP', width: 140, type: 'mono' },
    createdAt,
  ],
  'login-log': [
    id,
    { prop: 'username', label: '用户名', minWidth: 140 },
    {
      prop: 'eventType',
      label: '事件',
      width: 110,
      type: 'tag',
      tagMap: { LOGIN: { label: '登录', tone: 'up' }, LOGOUT: { label: '登出', tone: '' }, FAIL: { label: '失败', tone: 'down' } },
    },
    { prop: 'success', label: '结果', width: 100, type: 'bool', boolLabels: ['成功', '失败'] },
    { prop: 'clientIp', label: 'IP', width: 140, type: 'mono' },
    { prop: 'message', label: '说明', minWidth: 180 },
    createdAt,
  ],
  'online-user': [
    { prop: 'userId', label: '用户', width: 110, formatter: (_r, v) => `#${v}` },
    { prop: 'tenantId', label: '租户', width: 110, formatter: (_r, v) => (v ? `#${v}` : '—') },
    { prop: 'token', label: '会话 Token', minWidth: 260, type: 'mono', formatter: (_r, v) => (v ? `${String(v).slice(0, 8)}…${String(v).slice(-6)}` : '—') },
  ],
  file: [
    id,
    { prop: 'name', label: '文件名', minWidth: 220 },
    { prop: 'contentType', label: '类型', minWidth: 140, type: 'mono' },
    { prop: 'sizeBytes', label: '大小', width: 110, align: 'right', type: 'bytes' },
    { prop: 'path', label: '存储路径', minWidth: 200, type: 'mono' },
    createdAt,
  ],
  job: [
    id,
    { prop: 'name', label: '任务名称', minWidth: 180 },
    { prop: 'handler', label: '处理器', minWidth: 200, type: 'mono' },
    { prop: 'cronExpression', label: 'Cron', minWidth: 150, type: 'mono' },
    { prop: 'enabled', label: '状态', width: 110, type: 'bool', boolLabels: ['运行中', '已暂停'] },
    updatedAt,
  ],
  inbox: [
    id,
    { prop: 'title', label: '标题', minWidth: 220 },
    { prop: 'content', label: '内容', minWidth: 260 },
    { prop: 'readFlag', label: '状态', width: 100, type: 'bool', boolLabels: ['已读', '未读'] },
    createdAt,
  ],
}
