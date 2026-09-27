<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  Bell,
  MagicStick,
  Search,
  ArrowDown,
  DArrowLeft,
  DArrowRight,
  SwitchButton,
  Postcard,
  Right,
} from '@element-plus/icons-vue'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'
import AiAssistantDrawer from '@/components/AiAssistantDrawer.vue'
import { menus, flatMenus, isGroup, findMenu } from './menus'

const route = useRoute()
const router = useRouter()
const app = useAppStore()
const user = useUserStore()

const appTitle = import.meta.env.VITE_APP_TITLE || 'FastBoot'
const active = computed(() => route.path)
const current = computed(() => findMenu(route.path))
const pageTitle = computed(() => (route.meta.title as string) || current.value?.title || '')

const displayName = computed(() => user.profile?.displayName || user.profile?.username || '用户')
const initials = computed(() => displayName.value.trim().slice(0, 1).toUpperCase())

function isGroupActive(paths: string[]) {
  return paths.some((p) => active.value.startsWith(p))
}

// ---------- 快速搜索 ----------
const searchEl = ref<HTMLInputElement>()
const query = ref('')
const searchOpen = ref(false)
const hoverIndex = ref(0)

const matches = computed(() => {
  const q = query.value.trim().toLowerCase()
  if (!q) return flatMenus.slice(0, 6)
  return flatMenus
    .filter((m) => `${m.group ?? ''}${m.title}${m.desc ?? ''}${m.path}`.toLowerCase().includes(q))
    .slice(0, 8)
})

function go(path: string) {
  router.push(path)
  searchOpen.value = false
  query.value = ''
  searchEl.value?.blur()
}

function onSearchKey(e: KeyboardEvent) {
  if (e.key === 'ArrowDown') {
    e.preventDefault()
    hoverIndex.value = (hoverIndex.value + 1) % Math.max(matches.value.length, 1)
  } else if (e.key === 'ArrowUp') {
    e.preventDefault()
    hoverIndex.value = (hoverIndex.value - 1 + matches.value.length) % Math.max(matches.value.length, 1)
  } else if (e.key === 'Enter') {
    const m = matches.value[hoverIndex.value]
    if (m) go(m.path)
  } else if (e.key === 'Escape') {
    searchOpen.value = false
    searchEl.value?.blur()
  }
}

function onGlobalKey(e: KeyboardEvent) {
  if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'k') {
    e.preventDefault()
    searchEl.value?.focus()
    searchOpen.value = true
  }
}

onMounted(() => window.addEventListener('keydown', onGlobalKey))
onBeforeUnmount(() => window.removeEventListener('keydown', onGlobalKey))

async function onLogout() {
  await user.logout()
  router.push('/login')
}
</script>

<template>
  <div class="admin" :class="{ 'is-collapsed': app.sidebarCollapsed }">
    <!-- ============ 侧边栏 ============ -->
    <aside class="sidebar">
      <div class="brand" @click="router.push('/dashboard')">
        <span class="brand__mark">
          <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M13 2 4 14h7l-1 8 9-12h-7l1-8Z" />
          </svg>
        </span>
        <span class="brand__text">
          <span class="brand__name">{{ appTitle }}</span>
          <span class="brand__tag">Admin Console</span>
        </span>
      </div>

      <nav class="nav">
        <template v-for="entry in menus" :key="entry.title">
          <!-- 分组 -->
          <div v-if="isGroup(entry)" class="nav__group">
            <template v-if="!app.sidebarCollapsed">
              <div class="nav__label">
                <el-icon><component :is="entry.icon" /></el-icon>
                <span>{{ entry.title }}</span>
              </div>
              <router-link
                v-for="child in entry.children"
                :key="child.path"
                :to="child.path"
                class="nav__item"
                :class="{ 'is-active': active === child.path }"
              >
                <el-icon class="nav__icon"><component :is="child.icon ?? entry.icon" /></el-icon>
                <span class="nav__text">{{ child.title }}</span>
              </router-link>
            </template>

            <!-- 折叠态：分组图标 + 悬浮弹出子菜单 -->
            <el-popover
              v-else
              placement="right-start"
              trigger="hover"
              :width="220"
              :show-arrow="false"
              :offset="10"
              popper-class="nav-flyout"
            >
              <template #reference>
                <button
                  type="button"
                  class="nav__item nav__item--rail"
                  :class="{ 'is-active': isGroupActive(entry.children.map((c) => c.path)) }"
                >
                  <el-icon class="nav__icon"><component :is="entry.icon" /></el-icon>
                </button>
              </template>
              <div class="flyout__title">{{ entry.title }}</div>
              <router-link
                v-for="child in entry.children"
                :key="child.path"
                :to="child.path"
                class="flyout__item"
                :class="{ 'is-active': active === child.path }"
              >
                <el-icon><component :is="child.icon ?? entry.icon" /></el-icon>
                <span>{{ child.title }}</span>
              </router-link>
            </el-popover>
          </div>

          <!-- 叶子 -->
          <el-tooltip
            v-else
            :content="entry.title"
            placement="right"
            :disabled="!app.sidebarCollapsed"
            :show-after="120"
          >
            <router-link
              :to="entry.path"
              class="nav__item"
              :class="{ 'is-active': active === entry.path, 'nav__item--rail': app.sidebarCollapsed }"
            >
              <el-icon class="nav__icon"><component :is="entry.icon" /></el-icon>
              <span v-if="!app.sidebarCollapsed" class="nav__text">{{ entry.title }}</span>
            </router-link>
          </el-tooltip>
        </template>
      </nav>

      <div class="sidebar__foot">
        <el-dropdown trigger="click" placement="top-start" :popper-options="{ modifiers: [{ name: 'offset', options: { offset: [0, 8] } }] }">
          <button type="button" class="me">
            <span class="me__avatar">{{ initials }}</span>
            <span v-if="!app.sidebarCollapsed" class="me__text">
              <span class="me__name">{{ displayName }}</span>
              <span class="me__role">{{ user.profile?.username || '—' }}</span>
            </span>
          </button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item :icon="Postcard" @click="router.push('/profile')">个人中心</el-dropdown-item>
              <el-dropdown-item :icon="SwitchButton" divided @click="onLogout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <button type="button" class="fb-icon-btn fb-icon-btn--ghost collapse-btn" @click="app.toggleSidebar">
          <el-icon><component :is="app.sidebarCollapsed ? DArrowRight : DArrowLeft" /></el-icon>
        </button>
      </div>
    </aside>

    <!-- ============ 主区域 ============ -->
    <div class="main">
      <header class="topbar">
        <div class="topbar__left">
          <div class="crumbs">
            <span v-if="current?.group" class="crumbs__group">{{ current.group }}</span>
            <el-icon v-if="current?.group" class="crumbs__sep"><Right /></el-icon>
            <span class="crumbs__cur">{{ pageTitle }}</span>
          </div>
        </div>

        <div class="topbar__center">
          <div class="search" :class="{ 'is-open': searchOpen }">
            <el-icon class="search__icon"><Search /></el-icon>
            <input
              ref="searchEl"
              v-model="query"
              class="search__input"
              placeholder="搜索页面、功能…"
              @focus="searchOpen = true; hoverIndex = 0"
              @blur="searchOpen = false"
              @input="hoverIndex = 0"
              @keydown="onSearchKey"
            />
            <span class="search__kbd">Ctrl K</span>

            <transition name="drop">
              <div v-if="searchOpen" class="search__panel" @mousedown.prevent>
                <div class="search__hint">{{ query ? `匹配 ${matches.length} 项` : '快速跳转' }}</div>
                <button
                  v-for="(m, i) in matches"
                  :key="m.path"
                  type="button"
                  class="search__row"
                  :class="{ 'is-hover': i === hoverIndex }"
                  @mouseenter="hoverIndex = i"
                  @click="go(m.path)"
                >
                  <span class="search__row-icon"><el-icon><component :is="m.icon" /></el-icon></span>
                  <span class="search__row-main">
                    <span class="search__row-title">{{ m.title }}</span>
                    <span class="search__row-desc">{{ m.desc }}</span>
                  </span>
                  <span v-if="m.group" class="fb-chip">{{ m.group }}</span>
                </button>
                <div v-if="!matches.length" class="search__empty">没有匹配的页面</div>
              </div>
            </transition>
          </div>
        </div>

        <div class="topbar__right">
          <el-tooltip content="通知" placement="bottom" :show-after="200">
            <button type="button" class="fb-icon-btn" @click="router.push('/infra/inbox')">
              <el-icon><Bell /></el-icon>
              <span class="badge-dot" />
            </button>
          </el-tooltip>
          <button type="button" class="ai-btn" @click="app.toggleAiDrawer">
            <el-icon><MagicStick /></el-icon>
            <span>AI 助手</span>
          </button>
          <el-dropdown trigger="click">
            <button type="button" class="avatar-btn">
              <span class="avatar-btn__avatar">{{ initials }}</span>
              <span class="avatar-btn__name">{{ displayName }}</span>
              <el-icon class="avatar-btn__caret"><ArrowDown /></el-icon>
            </button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item :icon="Postcard" @click="router.push('/profile')">个人中心</el-dropdown-item>
                <el-dropdown-item :icon="SwitchButton" divided @click="onLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <main class="content">
        <router-view v-slot="{ Component }">
          <transition name="page" mode="out-in">
            <component :is="Component" :key="route.path" />
          </transition>
        </router-view>
      </main>
    </div>

    <AiAssistantDrawer v-model="app.aiDrawerVisible" />
  </div>
</template>

<style scoped lang="scss">
.admin {
  --sw: var(--fb-sidebar-w);
  min-height: 100vh;
  display: grid;
  grid-template-columns: var(--sw) 1fr;
  transition: grid-template-columns var(--fb-dur) var(--fb-ease);
  background:
    radial-gradient(900px 400px at 100% -10%, rgba(165, 180, 252, 0.16), transparent 60%),
    radial-gradient(700px 300px at 0% 100%, rgba(34, 197, 94, 0.08), transparent 60%),
    var(--fb-bg);

  &.is-collapsed {
    --sw: var(--fb-sidebar-w-collapsed);
  }
}

// ================= 侧边栏 =================
.sidebar {
  position: sticky;
  top: 0;
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--fb-surface);
  border-right: 1px solid var(--fb-border);
  padding: 18px 14px 14px;
  overflow: hidden;
}

.brand {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 4px 6px 18px;
  cursor: pointer;
  user-select: none;

  &__mark {
    flex: none;
    width: 36px;
    height: 36px;
    border-radius: 12px;
    display: grid;
    place-items: center;
    background: var(--fb-ink);
    color: var(--fb-lime);
    box-shadow: 0 10px 22px -12px rgba(17, 24, 39, 0.8);
  }

  &__text {
    display: flex;
    flex-direction: column;
    line-height: 1.15;
    white-space: nowrap;
    overflow: hidden;
  }

  &__name {
    font-size: 16px;
    font-weight: 700;
    letter-spacing: -0.02em;
  }

  &__tag {
    font-size: 11px;
    color: var(--fb-muted);
    margin-top: 2px;
  }
}

.nav {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
  gap: 2px;
  margin: 0 -4px;
  padding: 0 4px;

  &__group {
    display: flex;
    flex-direction: column;
    gap: 2px;
    margin-top: 8px;
  }

  &__label {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 10px 10px 6px;
    font-size: 11px;
    font-weight: 600;
    letter-spacing: 0.06em;
    text-transform: uppercase;
    color: var(--fb-muted);

    .el-icon {
      font-size: 13px;
    }
  }

  &__item {
    display: flex;
    align-items: center;
    gap: 10px;
    height: 38px;
    padding: 0 10px;
    border-radius: 11px;
    color: var(--fb-text-2);
    font-size: 13.5px;
    font-weight: 500;
    cursor: pointer;
    border: none;
    background: transparent;
    width: 100%;
    text-align: left;
    position: relative;
    transition: all var(--fb-dur) var(--fb-ease);
    white-space: nowrap;

    &:hover {
      background: var(--fb-surface-3);
      color: var(--fb-text);
    }

    &.is-active {
      background: var(--fb-ink);
      color: var(--fb-on-ink);
      box-shadow: 0 10px 22px -12px rgba(17, 24, 39, 0.75);

      .nav__icon {
        color: var(--fb-lime);
      }
    }

    &--rail {
      justify-content: center;
      padding: 0;
      height: 42px;
      width: 44px;
      margin: 3px auto;
      border-radius: 13px;
    }
  }

  &__icon {
    font-size: 17px;
    flex: none;
    color: var(--fb-muted);
    transition: color var(--fb-dur) var(--fb-ease);
  }

  &__item:hover &__icon {
    color: var(--fb-text);
  }

  &__text {
    overflow: hidden;
    text-overflow: ellipsis;
  }
}

.sidebar__foot {
  margin-top: 10px;
  padding-top: 12px;
  border-top: 1px solid var(--fb-border);
  display: flex;
  align-items: center;
  gap: 8px;
}

.me {
  flex: 1;
  min-width: 0;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px;
  border-radius: 12px;
  border: none;
  background: transparent;
  cursor: pointer;
  text-align: left;
  transition: background var(--fb-dur) var(--fb-ease);

  &:hover {
    background: var(--fb-surface-3);
  }

  &__avatar {
    flex: none;
    width: 34px;
    height: 34px;
    border-radius: 11px;
    display: grid;
    place-items: center;
    background: linear-gradient(135deg, var(--fb-accent-soft), var(--fb-sky-soft));
    color: #4338ca;
    font-weight: 700;
    font-size: 14px;
  }

  &__text {
    display: flex;
    flex-direction: column;
    line-height: 1.2;
    min-width: 0;
  }

  &__name {
    font-size: 13px;
    font-weight: 600;
    color: var(--fb-text);
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  &__role {
    font-size: 11px;
    color: var(--fb-muted);
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.is-collapsed {
  .sidebar {
    padding-left: 12px;
    padding-right: 12px;
  }

  .brand {
    justify-content: center;
    padding-left: 0;
    padding-right: 0;

    &__text {
      display: none;
    }
  }

  .sidebar__foot {
    flex-direction: column;
    gap: 6px;
  }

  .me {
    justify-content: center;
    padding: 4px;
  }
}

.collapse-btn {
  flex: none;
}

// ================= 主区域 =================
.main {
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.topbar {
  position: sticky;
  top: 0;
  z-index: 20;
  height: var(--fb-topbar-h);
  padding: 0 28px;
  display: grid;
  grid-template-columns: 1fr minmax(280px, 460px) 1fr;
  align-items: center;
  gap: 16px;
  background: var(--fb-glass);
  backdrop-filter: blur(14px) saturate(1.4);
  border-bottom: 1px solid var(--fb-border);

  &__left {
    min-width: 0;
  }

  &__center {
    display: flex;
    justify-content: center;
  }

  &__right {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    gap: 10px;
  }
}

.crumbs {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  white-space: nowrap;
  overflow: hidden;

  &__group {
    color: var(--fb-muted);
  }

  &__sep {
    color: var(--fb-muted);
    font-size: 12px;
  }

  &__cur {
    color: var(--fb-text);
    font-weight: 600;
  }
}

// 搜索
.search {
  position: relative;
  width: 100%;
  height: 38px;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 0 12px;
  border-radius: var(--fb-radius-pill);
  background: var(--fb-surface);
  border: 1px solid var(--fb-border);
  transition: all var(--fb-dur) var(--fb-ease);

  &:hover {
    border-color: var(--fb-border-strong);
  }

  &.is-open {
    border-color: var(--fb-ink);
    box-shadow: var(--fb-shadow);
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

  &__kbd {
    flex: none;
    font-size: 11px;
    color: var(--fb-muted);
    border: 1px solid var(--fb-border-strong);
    border-radius: 6px;
    padding: 1px 6px;
    line-height: 1.4;
  }

  &__panel {
    position: absolute;
    top: calc(100% + 8px);
    left: 0;
    right: 0;
    background: var(--fb-surface);
    border: 1px solid var(--fb-border);
    border-radius: 18px;
    box-shadow: var(--fb-shadow-lg);
    padding: 8px;
    z-index: 50;
  }

  &__hint,
  &__empty {
    padding: 6px 10px 8px;
    font-size: 11px;
    font-weight: 600;
    letter-spacing: 0.05em;
    text-transform: uppercase;
    color: var(--fb-muted);
  }

  &__empty {
    text-transform: none;
    font-weight: 500;
    padding: 14px 10px;
    text-align: center;
  }

  &__row {
    width: 100%;
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 8px 10px;
    border-radius: 12px;
    border: none;
    background: transparent;
    cursor: pointer;
    text-align: left;
    transition: background var(--fb-dur) var(--fb-ease);

    &.is-hover {
      background: var(--fb-surface-3);
    }
  }

  &__row-icon {
    flex: none;
    width: 32px;
    height: 32px;
    border-radius: 10px;
    display: grid;
    place-items: center;
    background: var(--fb-surface-3);
    color: var(--fb-text-2);
    font-size: 15px;
  }

  &__row-main {
    flex: 1;
    min-width: 0;
    display: flex;
    flex-direction: column;
    line-height: 1.25;
  }

  &__row-title {
    font-size: 13.5px;
    font-weight: 600;
    color: var(--fb-text);
  }

  &__row-desc {
    font-size: 12px;
    color: var(--fb-muted);
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.badge-dot {
  position: absolute;
  top: 7px;
  right: 8px;
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: var(--fb-primary);
  box-shadow: 0 0 0 2px var(--fb-surface);
}

.ai-btn {
  height: 34px;
  padding: 0 14px 0 12px;
  display: inline-flex;
  align-items: center;
  gap: 7px;
  border-radius: var(--fb-radius-pill);
  border: none;
  background: var(--fb-ink);
  color: var(--fb-on-ink);
  font: inherit;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  box-shadow: 0 10px 22px -12px rgba(17, 24, 39, 0.8);
  transition: all var(--fb-dur) var(--fb-ease);

  .el-icon {
    color: var(--fb-lime);
  }

  &:hover {
    transform: translateY(-1px);
    background: #000;
  }
}

.avatar-btn {
  height: 38px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 0 10px 0 4px;
  border-radius: var(--fb-radius-pill);
  border: 1px solid var(--fb-border);
  background: var(--fb-surface);
  cursor: pointer;
  font: inherit;
  transition: all var(--fb-dur) var(--fb-ease);

  &:hover {
    border-color: var(--fb-border-strong);
    box-shadow: var(--fb-shadow-xs);
  }

  &__avatar {
    width: 30px;
    height: 30px;
    border-radius: 50%;
    display: grid;
    place-items: center;
    background: linear-gradient(135deg, var(--fb-accent-soft), var(--fb-sky-soft));
    color: #4338ca;
    font-weight: 700;
    font-size: 13px;
  }

  &__name {
    font-size: 13px;
    font-weight: 600;
    color: var(--fb-text);
    max-width: 120px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  &__caret {
    font-size: 12px;
    color: var(--fb-muted);
  }
}

.content {
  flex: 1;
  padding: 24px 28px 36px;
  min-width: 0;
}

// 过渡
.page-enter-active,
.page-leave-active {
  transition: opacity 0.18s ease, transform 0.18s var(--fb-ease);
}

.page-enter-from {
  opacity: 0;
  transform: translateY(6px);
}

.page-leave-to {
  opacity: 0;
}

.drop-enter-active,
.drop-leave-active {
  transition: opacity 0.16s ease, transform 0.16s var(--fb-ease);
}

.drop-enter-from,
.drop-leave-to {
  opacity: 0;
  transform: translateY(-4px);
}

@media (max-width: 1100px) {
  .topbar {
    grid-template-columns: 1fr auto;

    &__center {
      display: none;
    }
  }
}
</style>

<style lang="scss">
// 折叠态的弹出子菜单（非 scoped，popper 挂在 body 上）
.nav-flyout.el-popper {
  padding: 8px;
  border-radius: 16px;

  .flyout__title {
    padding: 6px 10px 8px;
    font-size: 11px;
    font-weight: 600;
    letter-spacing: 0.06em;
    text-transform: uppercase;
    color: var(--fb-muted);
  }

  .flyout__item {
    display: flex;
    align-items: center;
    gap: 10px;
    height: 36px;
    padding: 0 10px;
    border-radius: 10px;
    color: var(--fb-text-2);
    font-size: 13.5px;
    font-weight: 500;
    transition: all var(--fb-dur) var(--fb-ease);

    .el-icon {
      font-size: 16px;
      color: var(--fb-muted);
    }

    &:hover {
      background: var(--fb-surface-3);
      color: var(--fb-text);
    }

    &.is-active {
      background: var(--fb-ink);
      color: var(--fb-on-ink);

      .el-icon {
        color: var(--fb-lime);
      }
    }
  }
}
</style>
