<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  value: string | number | boolean
  map?: Record<string, { label: string; type?: 'success' | 'info' | 'warning' | 'danger' }>
}>()

const meta = computed(() => {
  const key = String(props.value)
  return props.map?.[key] ?? { label: key, type: 'info' as const }
})
</script>

<template>
  <el-tag :type="meta.type" effect="light" round>{{ meta.label }}</el-tag>
</template>
