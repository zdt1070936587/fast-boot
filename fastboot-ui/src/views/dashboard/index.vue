<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import {
  Refresh,
  TopRight,
  MagicStick,
  Right,
  User,
  Timer,
  Bell,
  Connection,
  Key,
  Document,
  Folder,
  UserFilled,
} from '@element-plus/icons-vue'
import FbPageHeader from '@/components/FbPageHeader.vue'
import FbStatCard from '@/components/FbStatCard.vue'
import FbSparkBars from '@/components/FbSparkBars.vue'
import { getData } from '@/api/http'
import { useUserStore } from '@/stores/user'
import { useAppStore } from '@/stores/app'

type Row = Record<string, unknown>

const router = useRouter()
const user = useUserStore()
const app = useAppStore()

// ---------- 问候 / 日期 ----------
const now = new Date()
const greeting = computed(() => {
  const h = now.getHours()
  if (h < 6) return '夜深了'
  if (h < 12) return '早上好'
  if (h < 14) return '中午好'
  if (h < 18) return '下午好'
  return '晚上好'
})
const dateText = computed(() => {
  const w = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'][now.getDay()]
  return `${now.getFullYear()} 年 ${now.getMonth() + 1} 月 ${now.getDate()} 日 · ${w}`
})
const displayName = computed(() => user.profile?.displayName || user.profile?.username || '')

// ---------- 数据 ----------
const loading = ref(false)
const stats = reactive({
  users: null as Row[] | null,
  roles: null as Row[] | null,
  online: null as Row[] | null,
  jobs: null as Row[] | null,
  notices: null as Row[] | null,
  loginLogs: null as Row[] | null,
  inbox: null as Row[] | null,
})

async function safe<T>(fn: () => Promise<T>): Promise<T | null> {
  try {
    return await fn()
  } catch {
    return null
  }
}

async function load() {
  loading.value = true
  const [users, roles, online, jobs, notices, logs, inbox] = await Promise.all([
    safe(() => getData<Row[]>('/users')),
    safe(() => getData<Row[]>('/authz/roles')),
    safe(() => getData<Row[]>('/system/online-users')),
    safe(() => getData<Row[]>('/infra/jobs')),
    safe(() => getData<Row[]>('/system/notices')),
    safe(() => getData<{ records: Row[] }>('/system/login-logs', { page: 1, size: 6 })),
    safe(() => getData<Row[]>('/infra/inbox')),
  ])
  stats.users = users
  stats.roles = roles
  stats.online = online
  stats.jobs = jobs
  stats.notices = notices
  stats.loginLogs = logs?.records ?? null
  stats.inbox = inbox
  loading.value = false
}

onMounted(load)

const count = (arr: Row[] | null) => (arr ? arr.length : '—')
const countBy = (arr: Row[] | null, pred: (r: Row) => boolean) => (arr ? arr.filter(pred).length : '—')

// 以数据量为种子生成稳定的装饰趋势（无历史接口时的占位）
function trendFrom(seed: number, n = 12) {
  const out: number[] = []
  let x = (seed * 9301 + 49297) % 233280
  for (let i = 0; i < n; i++) {
    x = (x * 9301 + 49297) % 233280
    out.push(30 + Math.round((x / 233280) * 70))
  }
  return out
}

const userTrend = computed(() => trendFrom((stats.users?.length ?? 3) + 1))
const roleTrend = computed(() => trendFrom((stats.roles?.length ?? 2) + 7))
const onlineTrend = computed(() => trendFrom((stats.online?.length ?? 1) + 13))
const jobTrend = computed(() => trendFrom((stats.jobs?.length ?? 1) + 21))

// 总览图：两组柱（淡紫 = 请求，绿色 = 成功）
const overviewTab = ref<'all' | 'req' | 'err'>('all')
const overviewTabs = [
  { key: 'all', label: '全部' },
  { key: 'req', label: '请求' },
  { key: 'err', label: '错误' },
] as const
const range = ref<'week' | 'month'>('week')
const hours = computed(() =>
  range.value === 'week'
    ? Array.from({ length: 14 }, (_, i) => `${String(i + 8).padStart(2, '0')}:00`)
    : Array.from({ length: 30 }, (_, i) => `${i + 1} 日`),
)
const reqSeries = computed(() => trendFrom(range.value === 'week' ? 42 : 77, hours.value.length))
const okSeries = computed(() => reqSeries.value.map((v, i) => Math.round(v * (0.78 + ((i * 7) % 5) / 25))))

const heroMetrics = computed(() => [
  {
    label: '在线用户',
    value: count(stats.online),
    unit: '',
    delta: stats.online ? '+2 今日' : '',
    tone: 'up' as const,
  },
  {
    label: '账号总数',
    value: count(stats.users),
    unit: '',
    delta: stats.users ? `${countBy(stats.users, (r) => !!r.enabled)} 启用` : '',
    tone: 'accent' as const,
  },
  {
    label: '未读消息',
    value: countBy(stats.inbox, (r) => !r.readFlag),
    unit: '',
    delta: stats.inbox ? `${stats.inbox.length} 总计` : '',
    tone: 'sky' as const,
  },
])

// 待处理清单
const todos = computed(() => [
  { label: '未读站内信', count: countBy(stats.inbox, (r) => !r.readFlag), path: '/infra/inbox', icon: Bell, tone: 'accent' },
  { label: '已暂停任务', count: countBy(stats.jobs, (r) => !r.enabled), path: '/infra/job', icon: Timer, tone: 'warn' },
  { label: '停用账号', count: countBy(stats.users, (r) => !r.enabled), path: '/system/user', icon: User, tone: '' },
  { label: '草稿公告', count: countBy(stats.notices, (r) => r.status === 'DRAFT'), path: '/system/notice', icon: Document, tone: 'sky' },
])

const quickLinks = [
  { label: '用户', path: '/system/user', icon: User },
  { label: '角色', path: '/system/role', icon: UserFilled },
  { label: '在线用户', path: '/system/online-user', icon: Connection },
  { label: '登录日志', path: '/system/login-log', icon: Key },
  { label: '操作日志', path: '/system/operation-log', icon: Document },
  { label: '文件', path: '/infra/file', icon: Folder },
]

function fmtTime(v: unknown) {
  if (!v) return '—'
  const d = new Date(v as string)
  if (Number.isNaN(d.getTime())) return String(v)
  const p = (n: number) => String(n).padStart(2, '0')
  return `${p(d.getMonth() + 1)}-${p(d.getDate())} ${p(d.getHours())}:${p(d.getMinutes())}`
}

const noticeStatus: Record<string, { label: string; tone: string }> = {
  PUBLISHED: { label: '已发布', tone: 'fb-chip--up' },
  DRAFT: { label: '草稿', tone: '' },
  ARCHIVED: { label: '已归档', tone: 'fb-chip--warn' },
}
</script>

<template>
  <div class="fb-page dash">
    <FbPageHeader :eyebrow="dateText" :title="`${greeting}${displayName ? '，' + displayName : ''}`" description="这是系统今天的运行概览，关键指标与待处理事项一目了然。">
      <template #actions>
        <div class="fb-pills">
          <button type="button" class="fb-pills__item" :class="{ 'is-active': range === 'week' }" @click="range = 'week'">本周</button>
          <button type="button" class="fb-pills__item" :class="{ 'is-active': range === 'month' }" @click="range = 'month'">本月</button>
        </div>
        <el-tooltip content="刷新数据" :show-after="200">
          <button type="button" class="fb-icon-btn" :disabled="loading" @click="load">
            <el-icon :class="{ 'is-spin': loading }"><Refresh /></el-icon>
          </button>
        </el-tooltip>
      </template>
    </FbPageHeader>

    <!-- ===== 第一行：总览大卡 + 待处理 ===== -->
    <div class="dash__row dash__row--a">
      <section class="fb-card fb-card--pad overview">
        <div class="overview__head">
          <div>
            <div class="fb-card__title">系统总览</div>
            <div class="fb-card__sub">更新于 {{ now.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' }) }} · 数据来自 system / infra 模块</div>
          </div>
          <div class="fb-tabs overview__tabs">
            <button
              v-for="t in overviewTabs"
              :key="t.key"
              type="button"
              class="fb-tabs__item"
              :class="{ 'is-active': overviewTab === t.key }"
              @click="overviewTab = t.key"
            >
              {{ t.label }}
            </button>
          </div>
        </div>

        <div class="overview__metrics">
          <div v-for="m in heroMetrics" :key="m.label" class="hero">
            <div class="hero__label">{{ m.label }}</div>
            <div class="hero__value fb-num">
              {{ m.value }}<span v-if="m.unit" class="hero__unit">{{ m.unit }}</span>
            </div>
            <span v-if="m.delta" class="fb-chip" :class="`fb-chip--${m.tone}`">{{ m.delta }}</span>
          </div>
        </div>

        <div class="chart">
          <div class="chart__legend">
            <span><i class="chart__swatch chart__swatch--req" />请求量</span>
            <span><i class="chart__swatch chart__swatch--ok" />成功</span>
            <span class="fb-muted">示意趋势，接入 Prometheus 后替换</span>
          </div>
          <div class="chart__bars">
            <div v-for="(v, i) in reqSeries" :key="i" class="chart__col">
              <span
                v-if="overviewTab !== 'err'"
                class="chart__bar chart__bar--req"
                :style="{ height: `${v}%` }"
              />
              <span
                v-if="overviewTab !== 'req'"
                class="chart__bar chart__bar--ok"
                :style="{ height: `${overviewTab === 'err' ? Math.max(v - okSeries[i], 4) : okSeries[i]}%` }"
              />
            </div>
          </div>
          <div class="chart__axis">
            <span v-for="(h, i) in hours" :key="h" :class="{ 'is-hidden': i % (range === 'week' ? 2 : 5) !== 0 }">{{ h }}</span>
          </div>
        </div>
      </section>

      <section class="fb-card fb-card--pad todo">
        <div class="fb-card__head">
          <div>
            <div class="fb-card__title">待处理</div>
            <div class="fb-card__sub">需要你关注的事项</div>
          </div>
          <span class="fb-chip fb-chip--ink">{{ todos.reduce((s, t) => s + (typeof t.count === 'number' ? t.count : 0), 0) }}</span>
        </div>
        <ul class="todo__list">
          <li v-for="t in todos" :key="t.label" class="todo__item" @click="router.push(t.path)">
            <span class="todo__icon" :class="t.tone ? `is-${t.tone}` : ''"><el-icon><component :is="t.icon" /></el-icon></span>
            <span class="todo__label">{{ t.label }}</span>
            <span class="todo__count fb-num">{{ t.count }}</span>
            <el-icon class="todo__arrow"><Right /></el-icon>
          </li>
        </ul>
        <div class="todo__quick">
          <div class="fb-eyebrow">快捷入口</div>
          <div class="todo__chips">
            <button v-for="q in quickLinks" :key="q.path" type="button" class="quick" @click="router.push(q.path)">
              <el-icon><component :is="q.icon" /></el-icon>{{ q.label }}
            </button>
          </div>
        </div>
      </section>
    </div>

    <!-- ===== 第二行：四张指标卡 ===== -->
    <div class="dash__row dash__row--b">
      <FbStatCard
        title="用户总数"
        :value="count(stats.users)"
        :loading="loading && !stats.users"
        accent="accent"
        :delta="stats.users ? `${countBy(stats.users, (r) => !!r.enabled)} 启用` : undefined"
        :trend="userTrend"
        :footer="[
          { label: '启用', value: countBy(stats.users, (r) => !!r.enabled) },
          { label: '停用', value: countBy(stats.users, (r) => !r.enabled) },
        ]"
        clickable
        @click="router.push('/system/user')"
      />
      <FbStatCard
        title="角色"
        :value="count(stats.roles)"
        :loading="loading && !stats.roles"
        accent="sky"
        :trend="roleTrend"
        :footer="[{ label: '权限模型', value: 'RBAC' }, { label: '数据范围', value: '组织' }]"
        clickable
        @click="router.push('/system/role')"
      />
      <FbStatCard
        title="在线会话"
        :value="count(stats.online)"
        :loading="loading && !stats.online"
        accent="primary"
        delta="实时"
        delta-tone="up"
        :trend="onlineTrend"
        :footer="[{ label: '认证', value: 'Sa-Token' }, { label: '存储', value: 'Redis' }]"
        clickable
        @click="router.push('/system/online-user')"
      />
      <FbStatCard
        title="定时任务"
        :value="count(stats.jobs)"
        :loading="loading && !stats.jobs"
        accent="ink"
        :trend="jobTrend"
        :footer="[
          { label: '运行中', value: countBy(stats.jobs, (r) => !!r.enabled) },
          { label: '已暂停', value: countBy(stats.jobs, (r) => !r.enabled) },
        ]"
        clickable
        @click="router.push('/infra/job')"
      />
    </div>

    <!-- ===== 第三行：最近登录 / 公告 / AI ===== -->
    <div class="dash__row dash__row--c">
      <section class="fb-card fb-card--pad recent">
        <div class="fb-card__head">
          <div>
            <div class="fb-card__title">最近登录</div>
            <div class="fb-card__sub">最新 6 条登录事件</div>
          </div>
          <button type="button" class="fb-icon-btn fb-icon-btn--sm" @click="router.push('/system/login-log')">
            <el-icon><TopRight /></el-icon>
          </button>
        </div>
        <table v-if="stats.loginLogs?.length" class="mini-table">
          <thead>
            <tr>
              <th>用户</th>
              <th>事件</th>
              <th>IP</th>
              <th class="ta-r">时间</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(l, i) in stats.loginLogs" :key="i">
              <td>
                <span class="mini-user">
                  <span class="mini-avatar">{{ String(l.username ?? '?').slice(0, 1).toUpperCase() }}</span>
                  {{ l.username ?? '—' }}
                </span>
              </td>
              <td>
                <span class="fb-chip" :class="l.success ? 'fb-chip--up' : 'fb-chip--down'">{{ l.eventType ?? (l.success ? '成功' : '失败') }}</span>
              </td>
              <td><code class="mono">{{ l.clientIp ?? '—' }}</code></td>
              <td class="ta-r fb-muted fb-num">{{ fmtTime(l.createdAt) }}</td>
            </tr>
          </tbody>
        </table>
        <div v-else class="empty-hint">{{ loading ? '加载中…' : '暂无登录记录' }}</div>
      </section>

      <section class="fb-card fb-card--pad notices">
        <div class="fb-card__head">
          <div>
            <div class="fb-card__title">通知公告</div>
            <div class="fb-card__sub">{{ stats.notices ? `${stats.notices.length} 条` : '—' }}</div>
          </div>
          <button type="button" class="fb-icon-btn fb-icon-btn--sm" @click="router.push('/system/notice')">
            <el-icon><TopRight /></el-icon>
          </button>
        </div>
        <ul v-if="stats.notices?.length" class="notice-list">
          <li v-for="n in stats.notices.slice(0, 5)" :key="String(n.id)" class="notice">
            <span class="notice__bar" />
            <div class="notice__main">
              <div class="notice__title">{{ n.title }}</div>
              <div class="notice__meta fb-num">{{ fmtTime(n.updatedAt ?? n.createdAt) }}</div>
            </div>
            <span class="fb-chip" :class="noticeStatus[String(n.status)]?.tone">
              {{ noticeStatus[String(n.status)]?.label ?? n.status ?? '—' }}
            </span>
          </li>
        </ul>
        <div v-else class="empty-hint">{{ loading ? '加载中…' : '暂无公告' }}</div>
      </section>

      <section class="fb-card fb-card--pad ai">
        <div class="fb-card__head">
          <div class="fb-card__title">
            <span class="ai__spark"><el-icon><MagicStick /></el-icon></span>
            AI 助手
          </div>
          <span class="fb-chip fb-chip--up"><span class="fb-dot fb-dot--ok" />在线</span>
        </div>
        <div class="ai__bubble ai__bubble--bot">
          你好{{ displayName ? '，' + displayName : '' }}。我可以帮你查用户、看日志、解释错误码，或者生成一段 CRUD 代码。
        </div>
        <div class="ai__bubble ai__bubble--me">今天有多少次登录失败？</div>
        <div class="ai__suggest">
          <button type="button" class="quick" @click="app.toggleAiDrawer">总结今日操作日志</button>
          <button type="button" class="quick" @click="app.toggleAiDrawer">解释错误码 40001</button>
        </div>
        <button type="button" class="ai__open" @click="app.toggleAiDrawer">
          打开对话
          <el-icon><Right /></el-icon>
        </button>
        <FbSparkBars :data="[3, 5, 4, 7, 6, 9, 8, 12, 10, 14]" tone="primary" :height="28" thin class="ai__trend" />
      </section>
    </div>
  </div>
</template>

<style scoped lang="scss">
.dash {
  &__row {
    display: grid;
    gap: 18px;

    &--a {
      grid-template-columns: minmax(0, 1.9fr) minmax(300px, 1fr);
    }

    &--b {
      grid-template-columns: repeat(4, minmax(0, 1fr));
    }

    &--c {
      grid-template-columns: minmax(0, 1.4fr) minmax(0, 1fr) minmax(280px, 0.9fr);
    }
  }
}

@media (max-width: 1360px) {
  .dash__row--b {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .dash__row--c {
    grid-template-columns: 1fr 1fr;

    .ai {
      grid-column: 1 / -1;
    }
  }
}

@media (max-width: 1000px) {
  .dash__row--a,
  .dash__row--c {
    grid-template-columns: 1fr;
  }
}

// ---- 总览 ----
.overview {
  display: flex;
  flex-direction: column;
  gap: 18px;

  &__head {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 12px;
  }

  &__tabs {
    border-bottom: none;
    gap: 16px;
  }

  &__metrics {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 12px;
  }
}

.hero {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 14px 16px;
  border-radius: var(--fb-radius);
  background: var(--fb-surface-2);
  border: 1px solid var(--fb-border);

  &__label {
    font-size: 12.5px;
    color: var(--fb-muted);
    font-weight: 500;
  }

  &__value {
    font-size: 34px;
    font-weight: 600;
    line-height: 1;
    letter-spacing: -0.03em;
  }

  &__unit {
    font-size: 16px;
    color: var(--fb-muted);
    margin-left: 4px;
    font-weight: 500;
  }

  .fb-chip {
    align-self: flex-start;
  }
}

.chart {
  display: flex;
  flex-direction: column;
  gap: 10px;

  &__legend {
    display: flex;
    align-items: center;
    gap: 16px;
    font-size: 12px;
    color: var(--fb-text-2);

    span {
      display: inline-flex;
      align-items: center;
      gap: 6px;
    }

    .fb-muted {
      margin-left: auto;
      font-size: 11.5px;
    }
  }

  &__swatch {
    width: 10px;
    height: 10px;
    border-radius: 3px;
    display: inline-block;

    &--req {
      background: var(--fb-accent);
    }

    &--ok {
      background: var(--fb-primary);
    }
  }

  &__bars {
    height: 168px;
    display: flex;
    align-items: flex-end;
    gap: 10px;
    padding-bottom: 8px;
    background-image: repeating-linear-gradient(to top, var(--fb-border) 0 1px, transparent 1px 42px);
  }

  &__col {
    flex: 1;
    height: 100%;
    display: flex;
    align-items: flex-end;
    justify-content: center;
    gap: 3px;
  }

  &__bar {
    flex: 1;
    max-width: 14px;
    border-radius: 6px 6px 3px 3px;
    transition: height 0.5s var(--fb-ease);

    &--req {
      background: linear-gradient(to top, var(--fb-accent-soft), var(--fb-accent));
    }

    &--ok {
      background: linear-gradient(to top, var(--fb-primary-soft), var(--fb-primary));
    }
  }

  &__axis {
    display: flex;
    justify-content: space-between;
    font-size: 11px;
    color: var(--fb-muted);

    span {
      flex: 1;
      text-align: center;

      &.is-hidden {
        visibility: hidden;
      }
    }
  }
}

// ---- 待处理 ----
.todo {
  display: flex;
  flex-direction: column;

  &__list {
    list-style: none;
    margin: 0;
    padding: 0;
    display: flex;
    flex-direction: column;
    gap: 4px;
  }

  &__item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 9px 10px;
    margin: 0 -10px;
    border-radius: 12px;
    cursor: pointer;
    transition: background var(--fb-dur) var(--fb-ease);

    &:hover {
      background: var(--fb-surface-2);

      .todo__arrow {
        transform: translateX(2px);
        color: var(--fb-text);
      }
    }
  }

  &__icon {
    width: 34px;
    height: 34px;
    border-radius: 11px;
    display: grid;
    place-items: center;
    background: var(--fb-surface-3);
    color: var(--fb-text-2);
    font-size: 15px;

    &.is-accent {
      background: var(--fb-accent-soft);
      color: #4f46e5;
    }

    &.is-warn {
      background: var(--fb-warning-soft);
      color: #b45309;
    }

    &.is-sky {
      background: var(--fb-sky-soft);
      color: #1d4ed8;
    }
  }

  &__label {
    flex: 1;
    font-size: 13.5px;
    font-weight: 500;
  }

  &__count {
    font-size: 16px;
    font-weight: 600;
  }

  &__arrow {
    color: var(--fb-muted);
    font-size: 13px;
    transition: all var(--fb-dur) var(--fb-ease);
  }

  &__quick {
    margin-top: auto;
    padding-top: 16px;
    display: flex;
    flex-direction: column;
    gap: 10px;
  }

  &__chips {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
  }
}

.quick {
  height: 30px;
  padding: 0 12px;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  border-radius: var(--fb-radius-pill);
  border: 1px solid var(--fb-border-strong);
  background: var(--fb-surface);
  color: var(--fb-text-2);
  font: inherit;
  font-size: 12.5px;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--fb-dur) var(--fb-ease);

  .el-icon {
    font-size: 13px;
    color: var(--fb-muted);
  }

  &:hover {
    border-color: var(--fb-ink);
    color: var(--fb-text);
  }
}

// ---- 最近登录 ----
.mini-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;

  th {
    text-align: left;
    font-size: 11.5px;
    font-weight: 500;
    color: var(--fb-muted);
    padding: 0 8px 10px;
    border-bottom: 1px solid var(--fb-border);
    letter-spacing: 0.02em;
  }

  td {
    padding: 10px 8px;
    border-bottom: 1px solid var(--fb-border);
    color: var(--fb-text-2);
  }

  tr:last-child td {
    border-bottom: none;
  }

  .ta-r {
    text-align: right;
  }
}

.mini-user {
  display: inline-flex;
  align-items: center;
  gap: 9px;
  font-weight: 600;
  color: var(--fb-text);
}

.mini-avatar {
  width: 26px;
  height: 26px;
  border-radius: 8px;
  display: grid;
  place-items: center;
  background: linear-gradient(135deg, var(--fb-accent-soft), var(--fb-sky-soft));
  color: #4338ca;
  font-size: 12px;
  font-weight: 700;
}

.mono {
  font-family: var(--fb-font-mono);
  font-size: 12px;
  background: var(--fb-surface-3);
  padding: 2px 7px;
  border-radius: 6px;
}

.empty-hint {
  padding: 28px 0;
  text-align: center;
  color: var(--fb-muted);
  font-size: 13px;
}

// ---- 公告 ----
.notice-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.notice {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 10px 10px 0;
  border-radius: 12px;

  &__bar {
    width: 3px;
    height: 30px;
    border-radius: 3px;
    background: linear-gradient(to bottom, var(--fb-accent), var(--fb-sky));
    flex: none;
  }

  &__main {
    flex: 1;
    min-width: 0;
  }

  &__title {
    font-size: 13.5px;
    font-weight: 600;
    color: var(--fb-text);
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  &__meta {
    font-size: 12px;
    color: var(--fb-muted);
    margin-top: 2px;
  }
}

// ---- AI ----
.ai {
  display: flex;
  flex-direction: column;
  gap: 10px;
  background:
    radial-gradient(160px 120px at 100% 0%, rgba(185, 240, 106, 0.35), transparent 70%),
    var(--fb-surface);

  &__spark {
    width: 26px;
    height: 26px;
    border-radius: 8px;
    display: grid;
    place-items: center;
    background: var(--fb-ink);
    color: var(--fb-lime);
    font-size: 13px;
  }

  &__bubble {
    padding: 10px 12px;
    border-radius: 14px;
    font-size: 13px;
    line-height: 1.5;
    max-width: 92%;

    &--bot {
      background: var(--fb-surface-2);
      border: 1px solid var(--fb-border);
      color: var(--fb-text-2);
      border-top-left-radius: 6px;
    }

    &--me {
      align-self: flex-end;
      background: var(--fb-ink);
      color: var(--fb-on-ink);
      border-top-right-radius: 6px;
    }
  }

  &__suggest {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
    margin-top: 4px;
  }

  &__open {
    margin-top: auto;
    height: 38px;
    border-radius: 12px;
    border: none;
    background: var(--fb-primary);
    color: #052e16;
    font: inherit;
    font-size: 13px;
    font-weight: 600;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: 6px;
    cursor: pointer;
    transition: all var(--fb-dur) var(--fb-ease);
    box-shadow: 0 10px 22px -12px rgba(34, 197, 94, 0.8);

    &:hover {
      background: var(--fb-primary-strong);
      color: #fff;
    }
  }

  &__trend {
    opacity: 0.6;
  }
}

.is-spin {
  animation: spin 0.9s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>
