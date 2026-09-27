<script setup lang="ts">
import { reactive } from 'vue'
import { Search, RefreshRight } from '@element-plus/icons-vue'

withDefaults(defineProps<{ placeholder?: string }>(), { placeholder: '搜索关键词…' })

const emit = defineEmits<{
  search: [keyword: string]
  reset: []
}>()

const form = reactive({ keyword: '' })

function onSearch() {
  emit('search', form.keyword.trim())
}

function onReset() {
  form.keyword = ''
  emit('reset')
}
</script>

<template>
  <div class="fb-search">
    <div class="fb-search__field">
      <el-icon class="fb-search__icon"><Search /></el-icon>
      <input
        v-model="form.keyword"
        class="fb-search__input"
        :placeholder="placeholder"
        @keyup.enter="onSearch"
      />
      <button v-if="form.keyword" type="button" class="fb-search__clear" @click="onReset">清除</button>
    </div>
    <slot name="filters" />
    <div class="fb-search__actions">
      <el-button type="primary" @click="onSearch">查询</el-button>
      <el-tooltip content="重置" :show-after="200">
        <button type="button" class="fb-icon-btn" @click="onReset">
          <el-icon><RefreshRight /></el-icon>
        </button>
      </el-tooltip>
      <slot />
    </div>
  </div>
</template>

<style scoped lang="scss">
.fb-search {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;

  &__field {
    flex: 1 1 260px;
    max-width: 420px;
    height: 36px;
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 0 12px;
    border-radius: var(--fb-radius-pill);
    background: var(--fb-surface-2);
    border: 1px solid transparent;
    transition: all var(--fb-dur) var(--fb-ease);

    &:focus-within {
      background: var(--fb-surface);
      border-color: var(--fb-ink);
    }
  }

  &__icon {
    color: var(--fb-muted);
    font-size: 15px;
    flex: none;
  }

  &__input {
    flex: 1;
    min-width: 0;
    border: none;
    outline: none;
    background: transparent;
    font: inherit;
    font-size: 13.5px;
    color: var(--fb-text);

    &::placeholder {
      color: var(--fb-muted);
    }
  }

  &__clear {
    border: none;
    background: transparent;
    color: var(--fb-muted);
    font: inherit;
    font-size: 12px;
    cursor: pointer;
    padding: 0 2px;

    &:hover {
      color: var(--fb-text);
    }
  }

  &__actions {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-left: auto;
  }
}
</style>
