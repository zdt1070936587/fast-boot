<script setup lang="ts" generic="T extends Record<string, unknown>">
import { computed } from 'vue'
import { Box } from '@element-plus/icons-vue'

export type FbColumnType = 'text' | 'mono' | 'tag' | 'bool' | 'datetime' | 'bytes' | 'id'

export interface FbColumn {
  prop: string
  label: string
  width?: number | string
  minWidth?: number | string
  type?: FbColumnType
  /** tag 类型：值到 {label,tone} 的映射 */
  tagMap?: Record<string, { label: string; tone?: 'up' | 'down' | 'accent' | 'sky' | 'warn' | 'ink' | '' }>
  /** bool 类型：真/假的文案 */
  boolLabels?: [string, string]
  formatter?: (row: Record<string, unknown>, value: unknown) => string
  align?: 'left' | 'center' | 'right'
}

const props = withDefaults(
  defineProps<{
    columns: FbColumn[]
    data: T[]
    loading?: boolean
    emptyText?: string
    rowKey?: string
  }>(),
  { loading: false, emptyText: '暂无数据', rowKey: 'id' },
)

const rows = computed(() => props.data ?? [])

function fmtDate(v: unknown) {
  if (!v) return '—'
  const d = new Date(v as string)
  if (Number.isNaN(d.getTime())) return String(v)
  const p = (n: number) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())} ${p(d.getHours())}:${p(d.getMinutes())}`
}

function fmtBytes(v: unknown) {
  const n = Number(v)
  if (!Number.isFinite(n)) return '—'
  if (n < 1024) return `${n} B`
  if (n < 1024 ** 2) return `${(n / 1024).toFixed(1)} KB`
  if (n < 1024 ** 3) return `${(n / 1024 ** 2).toFixed(1)} MB`
  return `${(n / 1024 ** 3).toFixed(2)} GB`
}

function cell(row: T, col: FbColumn) {
  const value = row[col.prop]
  if (col.formatter) return col.formatter(row, value)
  if (value === null || value === undefined || value === '') return '—'
  return String(value)
}

function tagMeta(row: T, col: FbColumn) {
  const key = String(row[col.prop])
  return col.tagMap?.[key] ?? { label: key, tone: '' as const }
}
</script>

<template>
  <div class="fb-table-wrap">
    <el-table
      v-loading="loading"
      :data="rows"
      :row-key="rowKey"
      class="fb-table"
      style="width: 100%"
      :header-cell-class-name="() => 'fb-th'"
    >
      <el-table-column
        v-for="col in columns"
        :key="col.prop"
        :prop="col.prop"
        :label="col.label"
        :width="col.width"
        :min-width="col.minWidth"
        :align="col.align"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          <template v-if="col.type === 'id'">
            <span class="cell-id fb-num">#{{ row[col.prop] }}</span>
          </template>
          <template v-else-if="col.type === 'mono'">
            <code v-if="cell(row, col) !== '—'" class="cell-mono">{{ cell(row, col) }}</code>
            <span v-else class="cell-dim">—</span>
          </template>
          <template v-else-if="col.type === 'bool'">
            <span class="cell-bool" :class="row[col.prop] ? 'is-yes' : 'is-no'">
              <span class="fb-dot" :class="row[col.prop] ? 'fb-dot--ok' : ''" />
              {{ row[col.prop] ? (col.boolLabels?.[0] ?? '启用') : (col.boolLabels?.[1] ?? '停用') }}
            </span>
          </template>
          <template v-else-if="col.type === 'tag'">
            <span class="fb-chip" :class="tagMeta(row, col).tone ? `fb-chip--${tagMeta(row, col).tone}` : ''">
              {{ tagMeta(row, col).label }}
            </span>
          </template>
          <template v-else-if="col.type === 'datetime'">
            <span class="cell-dim fb-num">{{ fmtDate(row[col.prop]) }}</span>
          </template>
          <template v-else-if="col.type === 'bytes'">
            <span class="fb-num">{{ fmtBytes(row[col.prop]) }}</span>
          </template>
          <template v-else>
            <span :class="{ 'cell-primary': col === columns[1] }">{{ cell(row, col) }}</span>
          </template>
        </template>
      </el-table-column>
      <slot />
      <template #empty>
        <div class="fb-empty">
          <span class="fb-empty__icon"><el-icon><Box /></el-icon></span>
          <div class="fb-empty__title">{{ emptyText }}</div>
          <div class="fb-empty__sub">尝试更换关键词，或稍后刷新</div>
        </div>
      </template>
    </el-table>
  </div>
</template>

<style scoped lang="scss">
.fb-table-wrap {
  border-radius: var(--fb-radius);
  overflow: hidden;
}

.fb-table {
  :deep(.fb-th) {
    background: var(--fb-surface-2) !important;
    border-bottom: 1px solid var(--fb-border);
  }

  :deep(.el-table__row) {
    transition: background var(--fb-dur) var(--fb-ease);
  }

  :deep(.el-table__row:last-child td.el-table__cell) {
    border-bottom: none;
  }
}

.cell-id {
  color: var(--fb-muted);
  font-size: 12.5px;
}

.cell-primary {
  color: var(--fb-text);
  font-weight: 600;
}

.cell-dim {
  color: var(--fb-muted);
  font-size: 12.5px;
}

.cell-mono {
  font-family: var(--fb-font-mono);
  font-size: 12px;
  color: var(--fb-text-2);
  background: var(--fb-surface-3);
  padding: 2px 7px;
  border-radius: 6px;
}

.cell-bool {
  display: inline-flex;
  align-items: center;
  gap: 7px;
  font-weight: 500;
  color: var(--fb-text-2);

  &.is-yes {
    color: var(--fb-primary-strong);
  }
}

.fb-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 24px 0;
  line-height: 1.4;

  &__icon {
    width: 48px;
    height: 48px;
    border-radius: 16px;
    display: grid;
    place-items: center;
    background: var(--fb-surface-3);
    color: var(--fb-muted);
    font-size: 22px;
    margin-bottom: 8px;
  }

  &__title {
    font-size: 14px;
    font-weight: 600;
    color: var(--fb-text-2);
  }

  &__sub {
    font-size: 12px;
    color: var(--fb-muted);
  }
}
</style>
