<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { Refresh, Download, Clock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import FbPageHeader from '@/components/FbPageHeader.vue'
import FbSearchBar from '@/components/FbSearchBar.vue'
import FbTable, { type FbColumn } from '@/components/FbTable.vue'
import { http } from '@/api/http'
import { moduleColumns } from '../_columns'
import { findMenu } from '@/layouts/menus'

type Row = Record<string, unknown>

const props = withDefaults(
  defineProps<{
    title: string
    apiPath: string
    /** 列配置 key（对应 _columns.ts），不传则按路由末段推断 */
    module?: string
    description?: string
    /** 搜索框占位 */
    searchPlaceholder?: string
  }>(),
  { module: undefined, description: undefined, searchPlaceholder: undefined },
)

const fallbackColumns: FbColumn[] = [
  { prop: 'id', label: 'ID', width: 90, type: 'id' },
  { prop: 'name', label: '名称' },
]

const route = useRoute()
// 列配置 key：默认取路由末段（/system/error-code → error-code）
const moduleKey = computed(() => props.module ?? route.path.split('/').filter(Boolean).pop() ?? '')
const columns = computed(() => moduleColumns[moduleKey.value] ?? fallbackColumns)

// 从菜单表推断分组 + 描述
const menuMeta = computed(() => findMenu(route.path))

const loading = ref(false)
const all = ref<Row[]>([])
const keyword = ref('')
const page = ref(1)
const pageSize = ref(10)
const lastLoaded = ref<Date | null>(null)

const filtered = computed(() => {
  const k = keyword.value.trim().toLowerCase()
  if (!k) return all.value
  return all.value.filter((r) => JSON.stringify(r).toLowerCase().includes(k))
})

const total = computed(() => filtered.value.length)
const rows = computed(() => {
  const start = (page.value - 1) * pageSize.value
  return filtered.value.slice(start, start + pageSize.value)
})

async function load() {
  loading.value = true
  try {
    const res = await http.get(props.apiPath, { params: { page: 1, size: 500 } })
    const data = res.data.data
    if (Array.isArray(data)) {
      all.value = data as Row[]
    } else if (data && typeof data === 'object' && 'records' in data) {
      all.value = (data as { records: Row[] }).records
    } else {
      all.value = []
    }
    lastLoaded.value = new Date()
  } catch {
    all.value = []
  } finally {
    loading.value = false
  }
}

function onSearch(k: string) {
  keyword.value = k
  page.value = 1
}

function onReset() {
  keyword.value = ''
  page.value = 1
}

function exportCsv() {
  if (!filtered.value.length) {
    ElMessage.info('没有可导出的数据')
    return
  }
  const cols = columns.value
  const esc = (v: unknown) => `"${String(v ?? '').replace(/"/g, '""')}"`
  const lines = [
    cols.map((c) => esc(c.label)).join(','),
    ...filtered.value.map((r) => cols.map((c) => esc(r[c.prop])).join(',')),
  ]
  const blob = new Blob([`\uFEFF${lines.join('\n')}`], { type: 'text/csv;charset=utf-8' })
  const a = document.createElement('a')
  a.href = URL.createObjectURL(blob)
  a.download = `${props.title}-${new Date().toISOString().slice(0, 10)}.csv`
  a.click()
  URL.revokeObjectURL(a.href)
  ElMessage.success(`已导出 ${filtered.value.length} 条`)
}

const lastLoadedText = computed(() => {
  if (!lastLoaded.value) return '—'
  const p = (n: number) => String(n).padStart(2, '0')
  return `${p(lastLoaded.value.getHours())}:${p(lastLoaded.value.getMinutes())}:${p(lastLoaded.value.getSeconds())}`
})

onMounted(load)
</script>

<template>
  <div class="fb-page module">
    <FbPageHeader
      :eyebrow="menuMeta?.group ?? '数据管理'"
      :title="title"
      :count="loading ? undefined : total"
      :description="description ?? menuMeta?.desc"
    >
      <template #actions>
        <span class="module__stamp">
          <el-icon><Clock /></el-icon>
          更新于 {{ lastLoadedText }}
        </span>
        <el-tooltip content="刷新" :show-after="200">
          <button type="button" class="fb-icon-btn" :disabled="loading" @click="load">
            <el-icon :class="{ 'is-spin': loading }"><Refresh /></el-icon>
          </button>
        </el-tooltip>
        <el-button :icon="Download" @click="exportCsv">导出 CSV</el-button>
        <slot name="actions" />
      </template>
    </FbPageHeader>

    <section class="fb-card module__card">
      <div class="module__toolbar">
        <FbSearchBar :placeholder="searchPlaceholder ?? `搜索${title}…`" @search="onSearch" @reset="onReset">
          <slot name="toolbar" />
        </FbSearchBar>
      </div>

      <FbTable :columns="columns" :data="rows" :loading="loading" :empty-text="keyword ? '没有匹配的记录' : '暂无数据'">
        <slot />
      </FbTable>

      <footer class="module__foot">
        <span class="module__foot-info">
          共 <b class="fb-num">{{ total }}</b> 条<span v-if="keyword">，筛选自 {{ all.length }} 条</span>
        </span>
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="sizes, prev, pager, next"
          background
          small
        />
      </footer>
    </section>
  </div>
</template>

<style scoped lang="scss">
.module {
  &__stamp {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    font-size: 12px;
    color: var(--fb-muted);
    margin-right: 4px;
  }

  &__card {
    overflow: hidden;
  }

  &__toolbar {
    padding: 16px 18px;
    border-bottom: 1px solid var(--fb-border);
  }

  &__foot {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 12px;
    padding: 12px 18px;
    border-top: 1px solid var(--fb-border);
    background: var(--fb-surface-2);
  }

  &__foot-info {
    font-size: 12.5px;
    color: var(--fb-muted);

    b {
      color: var(--fb-text);
      font-weight: 600;
    }
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
