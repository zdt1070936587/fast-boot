<script setup lang="ts">
import { TopRight } from '@element-plus/icons-vue'
import FbSparkBars from './FbSparkBars.vue'

/**
 * 指标卡：标题 + 大数字 + 变化徽标 + 迷你趋势柱 + 底部子指标
 * 参考图中 "Submissions 49 / Expiration Date 0 / ..." 那一排卡片。
 */
withDefaults(
  defineProps<{
    title: string
    value: string | number
    /** 数字后缀，如 %、ms */
    unit?: string
    hint?: string
    /** 变化值，如 +12% / -3.2% */
    delta?: string
    deltaTone?: 'up' | 'down' | 'neutral'
    accent?: 'primary' | 'accent' | 'sky' | 'ink'
    trend?: number[]
    /** 底部小指标 */
    footer?: { label: string; value: string | number }[]
    /** 是否可点击（右上角显示箭头） */
    clickable?: boolean
    loading?: boolean
  }>(),
  { accent: 'accent', deltaTone: 'up', clickable: false, loading: false },
)

defineEmits<{ click: [] }>()
</script>

<template>
  <div class="stat fb-card fb-card--pad" :class="[`stat--${accent}`, { 'fb-card--hover': clickable }]" @click="$emit('click')">
    <div class="stat__head">
      <div class="stat__title">
        <span class="stat__dot" />
        {{ title }}
      </div>
      <span v-if="clickable" class="fb-icon-btn fb-icon-btn--sm fb-icon-btn--ghost stat__arrow">
        <el-icon><TopRight /></el-icon>
      </span>
    </div>

    <div class="stat__value-row">
      <div class="stat__value fb-num" :class="{ 'is-loading': loading }">
        {{ loading ? '' : value }}<span v-if="unit && !loading" class="stat__unit">{{ unit }}</span>
      </div>
      <span
        v-if="delta"
        class="fb-chip"
        :class="{ 'fb-chip--up': deltaTone === 'up', 'fb-chip--down': deltaTone === 'down' }"
      >
        {{ delta }}
      </span>
    </div>
    <div v-if="hint" class="stat__hint">{{ hint }}</div>

    <FbSparkBars v-if="trend?.length" :data="trend" :tone="accent" :height="40" class="stat__trend" />

    <div v-if="footer?.length" class="stat__footer">
      <div v-for="f in footer" :key="f.label" class="stat__foot-item">
        <span class="stat__foot-value fb-num">{{ f.value }}</span>
        <span class="stat__foot-label">{{ f.label }}</span>
      </div>
    </div>

    <slot />
  </div>
</template>

<style scoped lang="scss">
.stat {
  --dot: var(--fb-accent-strong);
  display: flex;
  flex-direction: column;
  gap: 6px;
  min-height: 132px;
  cursor: default;

  &--primary {
    --dot: var(--fb-primary);
  }

  &--sky {
    --dot: #3b82f6;
  }

  &--ink {
    --dot: var(--fb-ink);
  }

  &.fb-card--hover {
    cursor: pointer;
  }

  &__head {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 8px;
  }

  &__title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 13px;
    font-weight: 500;
    color: var(--fb-text-2);
  }

  &__dot {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: var(--dot);
  }

  &__arrow {
    margin: -6px -8px -6px 0;
  }

  &__value-row {
    display: flex;
    align-items: baseline;
    gap: 10px;
    margin-top: 6px;
  }

  &__value {
    font-size: 32px;
    font-weight: 600;
    line-height: 1;
    color: var(--fb-text);

    &.is-loading {
      width: 80px;
      height: 30px;
      border-radius: 8px;
      background: var(--fb-surface-3);
      animation: fb-pulse 1.4s ease-in-out infinite;
    }
  }

  &__unit {
    font-size: 15px;
    font-weight: 500;
    color: var(--fb-muted);
    margin-left: 3px;
  }

  &__hint {
    font-size: 12px;
    color: var(--fb-muted);
  }

  &__trend {
    margin-top: 10px;
  }

  &__footer {
    display: flex;
    gap: 22px;
    margin-top: 12px;
    padding-top: 12px;
    border-top: 1px dashed var(--fb-border-strong);
  }

  &__foot-item {
    display: flex;
    flex-direction: column;
    gap: 2px;
  }

  &__foot-value {
    font-size: 15px;
    font-weight: 600;
  }

  &__foot-label {
    font-size: 11.5px;
    color: var(--fb-muted);
  }
}
</style>
