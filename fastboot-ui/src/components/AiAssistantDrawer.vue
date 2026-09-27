<script setup lang="ts">
import { nextTick, ref } from 'vue'
import { MagicStick, Promotion, Close, Delete } from '@element-plus/icons-vue'

defineProps<{ modelValue: boolean }>()
const emit = defineEmits<{ 'update:modelValue': [boolean] }>()

interface Msg {
  role: 'bot' | 'me'
  text: string
  time: string
}

const prompt = ref('')
const typing = ref(false)
const listEl = ref<HTMLElement>()
const messages = ref<Msg[]>([
  {
    role: 'bot',
    text: '你好，我是 FastBoot AI 助手。目前为占位模式，接入 fastboot-modules-ai 后即可基于系统数据回答问题。',
    time: stamp(),
  },
])

const suggestions = ['总结今天的操作日志', '哪些定时任务被暂停了？', '解释错误码 40001', '生成一个用户 CRUD 的接口示例']

function stamp() {
  const d = new Date()
  return `${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}

function close() {
  emit('update:modelValue', false)
}

async function scrollBottom() {
  await nextTick()
  listEl.value?.scrollTo({ top: listEl.value.scrollHeight, behavior: 'smooth' })
}

async function send(text?: string) {
  const content = (text ?? prompt.value).trim()
  if (!content || typing.value) return
  messages.value.push({ role: 'me', text: content, time: stamp() })
  prompt.value = ''
  typing.value = true
  await scrollBottom()
  // 占位回复
  setTimeout(async () => {
    messages.value.push({
      role: 'bot',
      text: `（占位）已收到：「${content}」。接入 AI 模块后，这里会返回基于真实数据的回答。`,
      time: stamp(),
    })
    typing.value = false
    await scrollBottom()
  }, 600)
}

function clear() {
  messages.value = messages.value.slice(0, 1)
}

function onKey(e: KeyboardEvent) {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    send()
  }
}
</script>

<template>
  <el-drawer
    :model-value="modelValue"
    size="420px"
    :with-header="false"
    class="ai-drawer"
    @update:model-value="emit('update:modelValue', $event)"
    @close="close"
  >
    <div class="ai">
      <header class="ai__head">
        <span class="ai__spark"><el-icon><MagicStick /></el-icon></span>
        <div class="ai__head-text">
          <div class="ai__title">AI 助手</div>
          <div class="ai__sub"><span class="fb-dot fb-dot--ok" />占位模式 · 未接入模型</div>
        </div>
        <el-tooltip content="清空对话" :show-after="200">
          <button type="button" class="fb-icon-btn fb-icon-btn--ghost" @click="clear">
            <el-icon><Delete /></el-icon>
          </button>
        </el-tooltip>
        <button type="button" class="fb-icon-btn fb-icon-btn--ghost" @click="close">
          <el-icon><Close /></el-icon>
        </button>
      </header>

      <div ref="listEl" class="ai__list">
        <div v-for="(m, i) in messages" :key="i" class="msg" :class="`msg--${m.role}`">
          <span v-if="m.role === 'bot'" class="msg__avatar"><el-icon><MagicStick /></el-icon></span>
          <div class="msg__body">
            <div class="msg__bubble">{{ m.text }}</div>
            <div class="msg__time fb-num">{{ m.time }}</div>
          </div>
        </div>
        <div v-if="typing" class="msg msg--bot">
          <span class="msg__avatar"><el-icon><MagicStick /></el-icon></span>
          <div class="msg__body">
            <div class="msg__bubble msg__bubble--typing"><i /><i /><i /></div>
          </div>
        </div>
      </div>

      <div class="ai__suggest">
        <button v-for="s in suggestions" :key="s" type="button" class="ai__chip" @click="send(s)">{{ s }}</button>
      </div>

      <footer class="ai__composer">
        <textarea
          v-model="prompt"
          class="ai__input"
          rows="1"
          placeholder="问点什么… (Enter 发送，Shift+Enter 换行)"
          @keydown="onKey"
        />
        <button type="button" class="ai__send" :disabled="!prompt.trim() || typing" @click="send()">
          <el-icon><Promotion /></el-icon>
        </button>
      </footer>
    </div>
  </el-drawer>
</template>

<style scoped lang="scss">
.ai {
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 14px;

  &__head {
    display: flex;
    align-items: center;
    gap: 12px;
  }

  &__spark {
    width: 40px;
    height: 40px;
    border-radius: 13px;
    display: grid;
    place-items: center;
    background: var(--fb-ink);
    color: var(--fb-lime);
    font-size: 18px;
    box-shadow: 0 10px 22px -12px rgba(17, 24, 39, 0.8);
  }

  &__head-text {
    flex: 1;
    min-width: 0;
  }

  &__title {
    font-size: 16px;
    font-weight: 700;
    letter-spacing: -0.01em;
  }

  &__sub {
    display: flex;
    align-items: center;
    gap: 7px;
    font-size: 12px;
    color: var(--fb-muted);
    margin-top: 2px;
  }

  &__list {
    flex: 1;
    min-height: 0;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
    gap: 14px;
    padding: 6px 2px;
  }

  &__suggest {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
  }

  &__chip {
    height: 30px;
    padding: 0 12px;
    border-radius: var(--fb-radius-pill);
    border: 1px solid var(--fb-border-strong);
    background: var(--fb-surface);
    color: var(--fb-text-2);
    font: inherit;
    font-size: 12.5px;
    font-weight: 500;
    cursor: pointer;
    transition: all var(--fb-dur) var(--fb-ease);

    &:hover {
      border-color: var(--fb-ink);
      color: var(--fb-text);
    }
  }

  &__composer {
    display: flex;
    align-items: flex-end;
    gap: 8px;
    padding: 8px 8px 8px 14px;
    border-radius: 18px;
    background: var(--fb-surface-2);
    border: 1px solid var(--fb-border);
    transition: all var(--fb-dur) var(--fb-ease);

    &:focus-within {
      background: var(--fb-surface);
      border-color: var(--fb-ink);
    }
  }

  &__input {
    flex: 1;
    resize: none;
    border: none;
    outline: none;
    background: transparent;
    font: inherit;
    font-size: 13.5px;
    line-height: 1.5;
    padding: 8px 0;
    max-height: 120px;
    color: var(--fb-text);

    &::placeholder {
      color: var(--fb-muted);
    }
  }

  &__send {
    width: 36px;
    height: 36px;
    border-radius: 12px;
    border: none;
    background: var(--fb-ink);
    color: var(--fb-lime);
    display: grid;
    place-items: center;
    cursor: pointer;
    font-size: 15px;
    transition: all var(--fb-dur) var(--fb-ease);
    flex: none;

    &:disabled {
      opacity: 0.35;
      cursor: not-allowed;
    }

    &:not(:disabled):hover {
      transform: translateY(-1px);
    }
  }
}

.msg {
  display: flex;
  gap: 10px;
  align-items: flex-end;

  &--me {
    flex-direction: row-reverse;

    .msg__body {
      align-items: flex-end;
    }

    .msg__bubble {
      background: var(--fb-ink);
      color: var(--fb-on-ink);
      border-bottom-right-radius: 6px;
    }
  }

  &__avatar {
    width: 28px;
    height: 28px;
    border-radius: 9px;
    display: grid;
    place-items: center;
    background: var(--fb-primary-soft);
    color: var(--fb-primary-strong);
    font-size: 13px;
    flex: none;
  }

  &__body {
    display: flex;
    flex-direction: column;
    gap: 4px;
    max-width: 82%;
  }

  &__bubble {
    padding: 10px 13px;
    border-radius: 16px;
    border-bottom-left-radius: 6px;
    background: var(--fb-surface-2);
    border: 1px solid var(--fb-border);
    font-size: 13.5px;
    line-height: 1.55;
    color: var(--fb-text);
    white-space: pre-wrap;
    word-break: break-word;

    &--typing {
      display: inline-flex;
      gap: 4px;
      padding: 12px 14px;

      i {
        width: 6px;
        height: 6px;
        border-radius: 50%;
        background: var(--fb-muted);
        animation: fb-pulse 1s ease-in-out infinite;

        &:nth-child(2) {
          animation-delay: 0.15s;
        }

        &:nth-child(3) {
          animation-delay: 0.3s;
        }
      }
    }
  }

  &__time {
    font-size: 11px;
    color: var(--fb-muted);
    padding: 0 4px;
  }
}
</style>

<style lang="scss">
.ai-drawer.el-drawer {
  .el-drawer__body {
    padding: 20px 20px 18px;
    height: 100%;
  }
}
</style>
