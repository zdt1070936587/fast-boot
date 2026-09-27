<script setup lang="ts">
import { ref } from 'vue'
import { MagicStick, Right, Connection, Document, Timer, CircleCheck } from '@element-plus/icons-vue'
import FbPageHeader from '@/components/FbPageHeader.vue'

const tab = ref<'todo' | 'done' | 'mine'>('todo')

const steps = [
  { title: '流程设计', desc: 'BPMN 可视化编排、表单绑定', icon: Document, tone: 'accent' },
  { title: '审批流转', desc: '会签 / 或签 / 条件分支 / 超时提醒', icon: Connection, tone: 'sky' },
  { title: '定时催办', desc: '与 infra 调度联动，超时自动提醒', icon: Timer, tone: 'warn' },
  { title: '归档追溯', desc: '全链路审计与流程回放', icon: CircleCheck, tone: 'up' },
]
</script>

<template>
  <div class="fb-page bpm">
    <FbPageHeader eyebrow="流程" title="流程中心" description="流程设计、审批待办与流转追踪。模块骨架已就绪，尚未纳入 monolith 依赖。">
      <template #tabs>
        <div class="fb-pills" style="margin-top: 10px">
          <button type="button" class="fb-pills__item" :class="{ 'is-active': tab === 'todo' }" @click="tab = 'todo'">我的待办</button>
          <button type="button" class="fb-pills__item" :class="{ 'is-active': tab === 'done' }" @click="tab = 'done'">已办</button>
          <button type="button" class="fb-pills__item" :class="{ 'is-active': tab === 'mine' }" @click="tab = 'mine'">我发起的</button>
        </div>
      </template>
      <template #actions>
        <el-button type="primary" :icon="MagicStick" disabled>设计流程</el-button>
      </template>
    </FbPageHeader>

    <section class="fb-card hero">
      <div class="hero__copy">
        <span class="fb-chip fb-chip--accent">即将上线</span>
        <h2 class="hero__title">把审批流程画出来，<br />而不是写出来。</h2>
        <p class="hero__desc">fastboot-modules-bpm 提供基于 BPMN 的流程引擎能力，可独立部署后通过 API 替换此页面。</p>
        <a class="hero__link" href="javascript:void(0)">查看接入文档 <el-icon><Right /></el-icon></a>
      </div>
      <!-- 抽象流程示意 -->
      <div class="flow">
        <div class="flow__node flow__node--start"><span class="fb-dot fb-dot--ok" />发起</div>
        <span class="flow__edge" />
        <div class="flow__node">部门审批<small>会签 2/3</small></div>
        <span class="flow__edge" />
        <div class="flow__node flow__node--active">财务复核<small>处理中</small></div>
        <span class="flow__edge flow__edge--dash" />
        <div class="flow__node flow__node--ghost">归档</div>
      </div>
    </section>

    <div class="bpm__grid">
      <section v-for="s in steps" :key="s.title" class="fb-card fb-card--pad fb-card--hover step">
        <span class="step__icon" :class="`is-${s.tone}`"><el-icon><component :is="s.icon" /></el-icon></span>
        <div class="step__title">{{ s.title }}</div>
        <div class="step__desc">{{ s.desc }}</div>
      </section>
    </div>
  </div>
</template>

<style scoped lang="scss">
.hero {
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(0, 1.2fr);
  gap: 24px;
  padding: 32px 34px;
  align-items: center;
  overflow: hidden;
  background:
    radial-gradient(500px 240px at 100% 0%, rgba(165, 180, 252, 0.28), transparent 65%),
    radial-gradient(400px 220px at 70% 100%, rgba(185, 240, 106, 0.28), transparent 65%),
    var(--fb-surface);

  &__copy {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  &__title {
    font-size: 28px;
    line-height: 1.2;
    letter-spacing: -0.03em;
  }

  &__desc {
    margin: 0;
    color: var(--fb-text-2);
    font-size: 13.5px;
    line-height: 1.7;
    max-width: 420px;
  }

  &__link {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    font-size: 13px;
    font-weight: 600;
    color: var(--fb-text);
    margin-top: 6px;

    .el-icon {
      transition: transform var(--fb-dur) var(--fb-ease);
    }

    &:hover .el-icon {
      transform: translateX(3px);
    }
  }
}

.flow {
  display: flex;
  align-items: center;
  gap: 8px;
  justify-content: flex-end;
  flex-wrap: wrap;

  &__node {
    display: inline-flex;
    flex-direction: column;
    align-items: center;
    gap: 2px;
    padding: 12px 16px;
    border-radius: 14px;
    background: var(--fb-surface);
    border: 1px solid var(--fb-border-strong);
    font-size: 13px;
    font-weight: 600;
    box-shadow: var(--fb-shadow-sm);
    white-space: nowrap;

    small {
      font-size: 11px;
      font-weight: 500;
      color: var(--fb-muted);
    }

    &--start {
      flex-direction: row;
      gap: 8px;
      border-radius: var(--fb-radius-pill);
    }

    &--active {
      background: var(--fb-ink);
      color: var(--fb-on-ink);
      border-color: var(--fb-ink);
      box-shadow: 0 12px 26px -12px rgba(17, 24, 39, 0.7);

      small {
        color: var(--fb-lime);
      }
    }

    &--ghost {
      border-style: dashed;
      color: var(--fb-muted);
      box-shadow: none;
      background: transparent;
    }
  }

  &__edge {
    width: 26px;
    height: 2px;
    background: var(--fb-border-strong);
    border-radius: 2px;

    &--dash {
      background: repeating-linear-gradient(90deg, var(--fb-border-strong) 0 4px, transparent 4px 8px);
    }
  }
}

.bpm__grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 18px;
}

@media (max-width: 1200px) {
  .bpm__grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .hero {
    grid-template-columns: 1fr;
  }

  .flow {
    justify-content: flex-start;
  }
}

.step {
  display: flex;
  flex-direction: column;
  gap: 10px;

  &__icon {
    width: 40px;
    height: 40px;
    border-radius: 13px;
    display: grid;
    place-items: center;
    font-size: 18px;
    background: var(--fb-surface-3);
    color: var(--fb-text-2);

    &.is-accent {
      background: var(--fb-accent-soft);
      color: #4f46e5;
    }

    &.is-sky {
      background: var(--fb-sky-soft);
      color: #1d4ed8;
    }

    &.is-warn {
      background: var(--fb-warning-soft);
      color: #b45309;
    }

    &.is-up {
      background: var(--fb-primary-soft);
      color: var(--fb-primary-strong);
    }
  }

  &__title {
    font-size: 15px;
    font-weight: 600;
    margin-top: 4px;
  }

  &__desc {
    font-size: 12.5px;
    color: var(--fb-muted);
    line-height: 1.55;
  }
}
</style>
