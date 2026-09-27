<script setup lang="ts">
/**
 * 页面头：小号 eyebrow + 大标题 + 描述 + 右侧操作区
 * 对应参考图 "Lender Workspace" / "Mortgage Command" 的头部排版。
 */
defineProps<{
  eyebrow?: string
  title: string
  description?: string
  /** 标题右侧的数量徽标 */
  count?: number | string
}>()
</script>

<template>
  <header class="ph">
    <div class="ph__main">
      <div v-if="eyebrow" class="fb-eyebrow">{{ eyebrow }}</div>
      <div class="ph__title-row">
        <h1 class="fb-title">{{ title }}</h1>
        <span v-if="count !== undefined" class="ph__count fb-num">{{ count }}</span>
        <slot name="title-extra" />
      </div>
      <p v-if="description" class="ph__desc">{{ description }}</p>
      <slot name="tabs" />
    </div>
    <div v-if="$slots.actions" class="ph__actions">
      <slot name="actions" />
    </div>
  </header>
</template>

<style scoped lang="scss">
.ph {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 20px;
  flex-wrap: wrap;

  &__main {
    display: flex;
    flex-direction: column;
    gap: 6px;
    min-width: 0;
  }

  &__title-row {
    display: flex;
    align-items: center;
    gap: 12px;
  }

  &__count {
    height: 26px;
    padding: 0 10px;
    border-radius: var(--fb-radius-pill);
    background: var(--fb-surface);
    border: 1px solid var(--fb-border-strong);
    font-size: 13px;
    font-weight: 600;
    color: var(--fb-text-2);
    display: inline-flex;
    align-items: center;
  }

  &__desc {
    margin: 0;
    font-size: 13.5px;
    color: var(--fb-text-2);
    max-width: 640px;
  }

  &__actions {
    display: flex;
    align-items: center;
    gap: 10px;
    flex-wrap: wrap;
  }
}
</style>
