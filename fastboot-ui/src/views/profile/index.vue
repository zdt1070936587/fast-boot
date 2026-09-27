<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { Key, OfficeBuilding, User, SwitchButton, Connection, Lock } from '@element-plus/icons-vue'
import FbPageHeader from '@/components/FbPageHeader.vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const user = useUserStore()

const displayName = computed(() => user.profile?.displayName || user.profile?.username || '用户')
const initials = computed(() => displayName.value.trim().slice(0, 1).toUpperCase())

const fields = computed(() => [
  { label: '用户名', value: user.profile?.username ?? '—', icon: User },
  { label: '显示名', value: user.profile?.displayName ?? '—', icon: User },
  { label: '用户 ID', value: user.profile?.userId ?? '—', icon: Key, mono: true },
  { label: '所属组织', value: user.profile?.orgUnitId ? `#${user.profile.orgUnitId}` : '未分配', icon: OfficeBuilding },
])

async function onLogout() {
  await user.logout()
  router.push('/login')
}
</script>

<template>
  <div class="fb-page profile">
    <FbPageHeader eyebrow="账号" title="个人中心" description="查看你的账号信息与会话状态。" />

    <div class="profile__grid">
      <!-- 身份卡 -->
      <section class="fb-card fb-card--pad idcard">
        <div class="idcard__bg" />
        <div class="idcard__avatar">{{ initials }}</div>
        <div class="idcard__name">{{ displayName }}</div>
        <div class="idcard__sub">@{{ user.profile?.username ?? '—' }}</div>
        <div class="idcard__chips">
          <span class="fb-chip fb-chip--up"><span class="fb-dot fb-dot--ok" />会话有效</span>
          <span class="fb-chip fb-chip--accent">管理员</span>
        </div>
        <div class="idcard__actions">
          <el-button :icon="Lock" disabled>修改密码</el-button>
          <el-button type="danger" plain :icon="SwitchButton" @click="onLogout">退出登录</el-button>
        </div>
      </section>

      <!-- 信息 -->
      <section class="fb-card fb-card--pad info">
        <div class="fb-card__head">
          <div>
            <div class="fb-card__title">基本信息</div>
            <div class="fb-card__sub">来自 /auth/info</div>
          </div>
        </div>
        <dl class="info__list">
          <div v-for="f in fields" :key="f.label" class="info__item">
            <dt>
              <span class="info__icon"><el-icon><component :is="f.icon" /></el-icon></span>
              {{ f.label }}
            </dt>
            <dd :class="{ 'is-mono': f.mono }">{{ f.value }}</dd>
          </div>
        </dl>
      </section>

      <!-- 会话 -->
      <section class="fb-card fb-card--pad session">
        <div class="fb-card__head">
          <div>
            <div class="fb-card__title"><el-icon><Connection /></el-icon>当前会话</div>
            <div class="fb-card__sub">Sa-Token · Bearer</div>
          </div>
          <button type="button" class="fb-icon-btn fb-icon-btn--sm" @click="router.push('/system/online-user')">
            <el-icon><Connection /></el-icon>
          </button>
        </div>
        <div class="session__token">
          <span class="fb-eyebrow">Token</span>
          <code class="session__code">{{ user.token ? `${user.token.slice(0, 10)}…${user.token.slice(-8)}` : '—' }}</code>
        </div>
        <div class="session__meta">
          <div><span class="fb-muted">存储</span><b>localStorage</b></div>
          <div><span class="fb-muted">请求头</span><b>Authorization</b></div>
        </div>
      </section>
    </div>
  </div>
</template>

<style scoped lang="scss">
.profile__grid {
  display: grid;
  grid-template-columns: minmax(280px, 0.9fr) minmax(0, 1.4fr) minmax(260px, 0.9fr);
  gap: 18px;
}

@media (max-width: 1200px) {
  .profile__grid {
    grid-template-columns: 1fr 1fr;

    .session {
      grid-column: 1 / -1;
    }
  }
}

.idcard {
  position: relative;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding-top: 56px;

  &__bg {
    position: absolute;
    inset: 0 0 auto;
    height: 96px;
    background:
      radial-gradient(240px 120px at 20% 0%, rgba(185, 240, 106, 0.5), transparent 70%),
      radial-gradient(260px 140px at 90% 20%, rgba(165, 180, 252, 0.6), transparent 70%),
      var(--fb-sky-tint);
  }

  &__avatar {
    position: relative;
    width: 84px;
    height: 84px;
    border-radius: 26px;
    display: grid;
    place-items: center;
    font-size: 32px;
    font-weight: 700;
    color: #4338ca;
    background: linear-gradient(135deg, var(--fb-accent-soft), var(--fb-sky-soft));
    border: 4px solid var(--fb-surface);
    box-shadow: var(--fb-shadow);
  }

  &__name {
    margin-top: 14px;
    font-size: 20px;
    font-weight: 700;
    letter-spacing: -0.02em;
  }

  &__sub {
    color: var(--fb-muted);
    font-size: 13px;
    margin-top: 2px;
  }

  &__chips {
    display: flex;
    gap: 6px;
    margin-top: 14px;
  }

  &__actions {
    margin-top: 24px;
    display: flex;
    gap: 8px;
    width: 100%;

    .el-button {
      flex: 1;
      margin: 0;
    }
  }
}

.info__list {
  margin: 0;
  display: flex;
  flex-direction: column;
}

.info__item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 14px 0;
  border-bottom: 1px solid var(--fb-border);

  &:last-child {
    border-bottom: none;
  }

  dt {
    display: flex;
    align-items: center;
    gap: 10px;
    font-size: 13px;
    color: var(--fb-text-2);
    font-weight: 500;
  }

  dd {
    margin: 0;
    font-size: 14px;
    font-weight: 600;
    color: var(--fb-text);

    &.is-mono {
      font-family: var(--fb-font-mono);
      font-size: 13px;
      background: var(--fb-surface-3);
      padding: 2px 8px;
      border-radius: 6px;
    }
  }
}

.info__icon {
  width: 30px;
  height: 30px;
  border-radius: 9px;
  display: grid;
  place-items: center;
  background: var(--fb-surface-3);
  color: var(--fb-text-2);
  font-size: 14px;
}

.session {
  &__token {
    display: flex;
    flex-direction: column;
    gap: 6px;
    padding: 12px 14px;
    border-radius: 14px;
    background: var(--fb-ink);
    color: var(--fb-on-ink);

    .fb-eyebrow {
      color: rgba(255, 255, 255, 0.55);
    }
  }

  &__code {
    font-family: var(--fb-font-mono);
    font-size: 13px;
    color: var(--fb-lime);
    word-break: break-all;
  }

  &__meta {
    margin-top: 14px;
    display: flex;
    flex-direction: column;
    gap: 8px;
    font-size: 13px;

    div {
      display: flex;
      justify-content: space-between;
    }

    b {
      font-weight: 600;
    }
  }
}
</style>
