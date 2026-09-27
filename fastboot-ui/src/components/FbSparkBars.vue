<script setup lang="ts">
import { computed } from 'vue'

/**
 * 纯 CSS 迷你柱状图 —— 参考图中卡片底部那一排淡紫 / 薄荷绿的小柱子。
 * 不依赖图表库，适合作为指标卡的装饰性趋势展示。
 */
const props = withDefaults(
  defineProps<{
    data: number[]
    /** 高亮的柱子索引（默认最后一根） */
    highlight?: number | null
    /** 柱子高度（px） */
    height?: number
    /** 主色调：accent=淡紫，primary=薄荷绿，sky=淡蓝，ink=深色 */
    tone?: 'accent' | 'primary' | 'sky' | 'ink'
    /** 圆润柱状（参考图风格）还是细线 */
    thin?: boolean
  }>(),
  { highlight: null, height: 44, tone: 'accent', thin: false },
)

const max = computed(() => Math.max(...props.data, 1))
const hi = computed(() => (props.highlight === null ? props.data.length - 1 : props.highlight))
</script>

<template>
  <div class="spark" :class="[`spark--${tone}`, { 'spark--thin': thin }]" :style="{ height: `${height}px` }">
    <span
      v-for="(v, i) in data"
      :key="i"
      class="spark__bar"
      :class="{ 'is-hi': i === hi }"
      :style="{ height: `${Math.max((v / max) * 100, 6)}%` }"
    />
  </div>
</template>

<style scoped lang="scss">
.spark {
  --c: var(--fb-accent-soft);
  --c-hi: var(--fb-accent-strong);
  display: flex;
  align-items: flex-end;
  gap: 4px;
  width: 100%;

  &--primary {
    --c: var(--fb-primary-soft);
    --c-hi: var(--fb-primary);
  }

  &--sky {
    --c: var(--fb-sky-soft);
    --c-hi: #3b82f6;
  }

  &--ink {
    --c: var(--fb-surface-3);
    --c-hi: var(--fb-ink);
  }

  &__bar {
    flex: 1;
    min-width: 3px;
    border-radius: 4px 4px 2px 2px;
    background: var(--c);
    transition: height 0.5s var(--fb-ease), background var(--fb-dur) var(--fb-ease);

    &.is-hi {
      background: var(--c-hi);
    }
  }

  &:hover &__bar:not(.is-hi) {
    opacity: 0.85;
  }

  &--thin {
    gap: 3px;

    .spark__bar {
      min-width: 2px;
      border-radius: 2px;
    }
  }
}
</style>
