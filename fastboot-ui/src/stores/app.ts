import { defineStore } from 'pinia'
import { ref, watch } from 'vue'

const SIDEBAR_KEY = 'fastboot_sidebar_collapsed'

export const useAppStore = defineStore('app', () => {
  const sidebarCollapsed = ref(localStorage.getItem(SIDEBAR_KEY) === '1')
  const aiDrawerVisible = ref(false)

  watch(sidebarCollapsed, (v) => localStorage.setItem(SIDEBAR_KEY, v ? '1' : '0'))

  function toggleSidebar() {
    sidebarCollapsed.value = !sidebarCollapsed.value
  }

  function toggleAiDrawer() {
    aiDrawerVisible.value = !aiDrawerVisible.value
  }

  return { sidebarCollapsed, aiDrawerVisible, toggleSidebar, toggleAiDrawer }
})
