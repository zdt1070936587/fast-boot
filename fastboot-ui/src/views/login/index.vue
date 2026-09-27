<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { User, Lock, Right } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import FbSparkBars from '@/components/FbSparkBars.vue'

const router = useRouter()
const route = useRoute()
const user = useUserStore()
const loading = ref(false)
const appTitle = import.meta.env.VITE_APP_TITLE || 'FastBoot'

const form = reactive({
  username: 'admin',
  password: '',
  remember: true,
})

async function submit() {
  if (!form.username || !form.password) return
  loading.value = true
  try {
    await user.login(form.username, form.password)
    const redirect = (route.query.redirect as string) || '/dashboard'
    router.push(redirect)
  } finally {
    loading.value = false
  }
}

const features = [
  { title: '模块化单体', desc: '按业务域拆分，可平滑演进到微服务' },
  { title: '多租户 · 数据权限', desc: '租户隔离与组织级数据范围开箱即用' },
  { title: '可观测', desc: 'Prometheus / Grafana / Trace 一体化' },
]
</script>

<template>
  <div class="login">
    <!-- 左侧品牌区（参考 Clario 浅蓝渐变 Hero） -->
    <section class="hero">
      <div class="hero__brand">
        <span class="hero__mark">
          <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M13 2 4 14h7l-1 8 9-12h-7l1-8Z" />
          </svg>
        </span>
        <span>{{ appTitle }}</span>
      </div>

      <div class="hero__copy">
        <div class="fb-eyebrow hero__eyebrow">Enterprise Admin Console</div>
        <h1 class="hero__title">让后台<br />也有产品级的质感。</h1>
        <p class="hero__desc">一个面向企业级应用的快速开发底座：认证授权、组织架构、多租户、审计与调度，全部就绪。</p>
      </div>

      <!-- 漂浮的玻璃指标卡 -->
      <div class="float float--a">
        <div class="float__label">平均响应</div>
        <div class="float__value fb-num">46<span>ms</span></div>
        <FbSparkBars :data="[6, 9, 7, 12, 10, 14, 11, 16, 13, 18]" tone="primary" :height="30" thin />
      </div>
      <div class="float float--b">
        <div class="float__label">今日请求</div>
        <div class="float__value fb-num">12.4<span>k</span></div>
        <span class="fb-chip fb-chip--up">+8.2%</span>
      </div>
      <div class="float float--c">
        <div class="float__row">
          <span class="fb-dot fb-dot--ok" />
          <span class="float__label">全部服务正常</span>
        </div>
        <div class="float__pills">
          <span class="fb-chip">auth</span>
          <span class="fb-chip">system</span>
          <span class="fb-chip">infra</span>
          <span class="fb-chip fb-chip--accent">bpm</span>
        </div>
      </div>

      <ul class="hero__features">
        <li v-for="f in features" :key="f.title">
          <span class="hero__feature-title">{{ f.title }}</span>
          <span class="hero__feature-desc">{{ f.desc }}</span>
        </li>
      </ul>
    </section>

    <!-- 右侧登录表单 -->
    <section class="form-side">
      <div class="panel">
        <div class="panel__head">
          <h2>欢迎回来</h2>
          <p>使用账号密码登录管理控制台</p>
        </div>

        <el-form label-position="top" size="large" @submit.prevent="submit">
          <el-form-item label="用户名">
            <el-input v-model="form.username" :prefix-icon="User" placeholder="请输入用户名" autocomplete="username" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input
              v-model="form.password"
              :prefix-icon="Lock"
              type="password"
              show-password
              placeholder="请输入密码"
              autocomplete="current-password"
            />
          </el-form-item>

          <div class="panel__row">
            <el-checkbox v-model="form.remember">记住我</el-checkbox>
            <a class="panel__link" href="javascript:void(0)">忘记密码？</a>
          </div>

          <el-button type="primary" size="large" native-type="submit" :loading="loading" class="panel__submit">
            登录
            <el-icon v-if="!loading" class="panel__submit-icon"><Right /></el-icon>
          </el-button>
        </el-form>

        <div class="panel__foot">
          <span class="fb-muted">默认账号 admin，密码见 .env.example</span>
        </div>
      </div>

      <div class="form-side__copyright fb-muted">© {{ new Date().getFullYear() }} {{ appTitle }} · Made with care</div>
    </section>
  </div>
</template>

<style scoped lang="scss">
.login {
  min-height: 100vh;
  display: grid;
  grid-template-columns: minmax(0, 1.15fr) minmax(420px, 0.85fr);
  background: var(--fb-bg);
}

// ================= 左侧 Hero =================
.hero {
  position: relative;
  overflow: hidden;
  padding: 40px 56px;
  display: flex;
  flex-direction: column;
  background:
    radial-gradient(700px 500px at 80% 10%, rgba(147, 197, 253, 0.55), transparent 65%),
    radial-gradient(600px 500px at 10% 90%, rgba(185, 240, 106, 0.35), transparent 60%),
    radial-gradient(500px 400px at 60% 70%, rgba(165, 180, 252, 0.4), transparent 60%),
    linear-gradient(160deg, #eef4ff 0%, #f3f6fb 55%, #f6f7fa 100%);

  &::after {
    // 细网格纹理
    content: '';
    position: absolute;
    inset: 0;
    background-image:
      linear-gradient(rgba(15, 23, 42, 0.035) 1px, transparent 1px),
      linear-gradient(90deg, rgba(15, 23, 42, 0.035) 1px, transparent 1px);
    background-size: 48px 48px;
    mask-image: radial-gradient(closest-side at 50% 40%, #000 30%, transparent 100%);
    pointer-events: none;
  }

  > * {
    position: relative;
    z-index: 1;
  }

  &__brand {
    display: flex;
    align-items: center;
    gap: 10px;
    font-size: 17px;
    font-weight: 700;
    letter-spacing: -0.02em;
  }

  &__mark {
    width: 36px;
    height: 36px;
    border-radius: 12px;
    display: grid;
    place-items: center;
    background: var(--fb-ink);
    color: var(--fb-lime);
    box-shadow: 0 10px 22px -12px rgba(17, 24, 39, 0.8);
  }

  &__copy {
    margin-top: 72px;
    max-width: 520px;
  }

  &__eyebrow {
    margin-bottom: 14px;
  }

  &__title {
    font-size: 44px;
    line-height: 1.12;
    font-weight: 600;
    letter-spacing: -0.03em;
  }

  &__desc {
    margin: 18px 0 0;
    font-size: 15px;
    line-height: 1.7;
    color: var(--fb-text-2);
    max-width: 440px;
  }

  &__features {
    list-style: none;
    margin: auto 0 0;
    padding: 0;
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 14px;

    li {
      padding: 14px 16px;
      border-radius: var(--fb-radius);
      background: var(--fb-glass);
      border: 1px solid rgba(255, 255, 255, 0.7);
      backdrop-filter: blur(10px);
      display: flex;
      flex-direction: column;
      gap: 4px;
    }
  }

  &__feature-title {
    font-size: 13.5px;
    font-weight: 600;
  }

  &__feature-desc {
    font-size: 12px;
    color: var(--fb-text-2);
    line-height: 1.5;
  }
}

.float {
  position: absolute;
  padding: 14px 16px;
  border-radius: var(--fb-radius-lg);
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(255, 255, 255, 0.9);
  box-shadow: var(--fb-shadow);
  backdrop-filter: blur(14px);
  display: flex;
  flex-direction: column;
  gap: 6px;
  animation: floaty 7s ease-in-out infinite;

  &__label {
    font-size: 12px;
    color: var(--fb-muted);
    font-weight: 500;
  }

  &__value {
    font-size: 30px;
    font-weight: 600;
    line-height: 1;
    letter-spacing: -0.03em;

    span {
      font-size: 14px;
      color: var(--fb-muted);
      margin-left: 3px;
      font-weight: 500;
    }
  }

  &__row {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  &__pills {
    display: flex;
    gap: 6px;
    flex-wrap: wrap;
  }

  &--a {
    right: 12%;
    top: 30%;
    width: 200px;
  }

  &--b {
    right: 30%;
    top: 52%;
    width: 170px;
    animation-delay: -2.5s;

    .fb-chip {
      align-self: flex-start;
    }
  }

  &--c {
    right: 8%;
    top: 62%;
    width: 230px;
    animation-delay: -4.5s;
  }
}

@keyframes floaty {
  0%,
  100% {
    transform: translateY(0);
  }

  50% {
    transform: translateY(-8px);
  }
}

// ================= 右侧表单 =================
.form-side {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 48px;
  position: relative;

  &__copyright {
    position: absolute;
    bottom: 24px;
    font-size: 12px;
  }
}

.panel {
  width: min(400px, 100%);
  padding: 36px 34px 28px;
  border-radius: var(--fb-radius-xl);
  background: var(--fb-surface);
  border: 1px solid var(--fb-border);
  box-shadow: var(--fb-shadow-lg);
  animation: fb-fade-up 0.4s var(--fb-ease) both;

  &__head {
    margin-bottom: 26px;

    h2 {
      font-size: 24px;
      letter-spacing: -0.02em;
    }

    p {
      margin: 6px 0 0;
      color: var(--fb-muted);
      font-size: 13.5px;
    }
  }

  &__row {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin: -4px 0 18px;
    font-size: 13px;
  }

  &__link {
    color: var(--fb-text-2);
    font-weight: 500;

    &:hover {
      color: var(--fb-text);
    }
  }

  &__submit {
    width: 100%;
    font-size: 15px;
    border-radius: 14px;

    &-icon {
      margin-left: 6px;
      transition: transform var(--fb-dur) var(--fb-ease);
    }

    &:hover &-icon {
      transform: translateX(3px);
    }
  }

  &__foot {
    margin-top: 22px;
    text-align: center;
    font-size: 12px;
  }

  :deep(.el-input--large .el-input__wrapper) {
    padding: 4px 14px;
    background: var(--fb-surface-2);
    box-shadow: 0 0 0 1px transparent inset;

    &.is-focus {
      background: var(--fb-surface);
      box-shadow: 0 0 0 1.5px var(--fb-ink) inset;
    }
  }

  :deep(.el-input__prefix) {
    color: var(--fb-muted);
  }

  :deep(.el-checkbox__label) {
    color: var(--fb-text-2);
    font-size: 13px;
  }

  :deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
    background: var(--fb-ink);
    border-color: var(--fb-ink);
  }
}

@media (max-width: 960px) {
  .login {
    grid-template-columns: 1fr;
  }

  .hero {
    display: none;
  }
}
</style>
