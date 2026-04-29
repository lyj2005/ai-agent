<template>
  <div class="love-root">
    <!-- 背景装饰元素 -->
    <div class="background-glow"></div>
    <div class="floating-hearts">
      <div class="heart" v-for="i in 5" :key="i" :style="{left: (20*i)+'%', animationDelay: (i*0.3)+'s'}">♥</div>
    </div>

    <header class="love-header topbar">
      <div class="left">
        <router-link to="/" class="back">← 返回</router-link>
      </div>
      <div class="center">
        <span class="title-heart">❤</span>
        恋爱大师
      </div>
      <div class="right privacy-indicator">
        <span class="lock-icon">🔒</span>
        <span class="private-text">私密对话</span>
      </div>
    </header>

    <main class="chat-panel" ref="logRef">
      <div v-for="m in messages" :key="m.id" :class="['message-row', m.sender]">
        <div class="avatar" v-if="m.sender==='ai'">
          <span class="avatar-icon">💕</span>
        </div>
        <div class="bubble" :class="m.sender + '-bubble'">
          <div class="bubble-text" v-html="m.text"></div>
          <div class="time">{{ m.time }}</div>
        </div>
        <div class="avatar user-avatar" v-if="m.sender==='user'">
          <span class="avatar-icon">�</span>
        </div>
      </div>
      <div v-if="isStreaming" class="message-row ai streaming">
        <div class="avatar"><span class="avatar-icon">💕</span></div>
        <div class="bubble ai-bubble">
          <div class="typing">
            <span></span><span></span><span></span>
          </div>
        </div>
      </div>
    </main>

    <footer class="composer">
      <div class="composer-inner">
        <input v-model="input" @keyup.enter="send" placeholder="倾诉你的故事..." />
        <button class="send" @click="send">
          <span class="send-icon">💌</span>
        </button>
      </div>
    </footer>
  </div>
</template>

<script>
import { ref, onMounted, nextTick } from 'vue'

function genId() {
  return Date.now().toString(36) + Math.random().toString(36).slice(2,8)
}

function nowTime() {
  const d = new Date()
  return d.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
}

export default {
  setup() {
    const chatIdKey = 'love_chat_id'
    const chatId = ref('')
    const messages = ref([])
    const input = ref('')
    const es = ref(null)
    const logRef = ref(null)
    const isStreaming = ref(false)

    onMounted(() => {
      let v = localStorage.getItem(chatIdKey)
      if (!v) {
        v = genId()
        localStorage.setItem(chatIdKey, v)
      }
      chatId.value = v      
      // 显示欢迎消息
      messages.value.push({
        id: genId(),
        sender: 'ai',
        text: '欢迎来到AI恋爱大师，请诉说你的恋爱困惑，我会尽力给予帮助和建议。',
        html: '欢迎来到AI恋爱大师，请诉说你的恋爱困惑，我会尽力给予帮助和建议。',
        time: nowTime()
      })
    })

    function scrollToBottom() {
      nextTick(() => {
        const el = logRef.value
        if (el) el.scrollTop = el.scrollHeight
      })
    }

    function appendAiChunk(text) {
      isStreaming.value = true
      const last = messages.value[messages.value.length - 1]
      if (last && last.sender === 'ai') {
        last.text += text
        last.html = escapeHtml(last.text)
      } else {
        const obj = { id: genId(), sender: 'ai', text, html: escapeHtml(text), time: nowTime() }
        messages.value.push(obj)
      }
      scrollToBottom()
    }

    function finishStreaming() {
      isStreaming.value = false
      const last = messages.value[messages.value.length - 1]
      if (last && last.sender === 'ai') {
        last.time = nowTime()
      }
    }

    function escapeHtml(str) {
      return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/\n/g, '<br/>')
    }

    function send() {
      const msg = input.value && input.value.trim()
      if (!msg) return
      messages.value.push({ id: genId(), sender: 'user', text: msg, html: escapeHtml(msg), time: nowTime() })
      input.value = ''

      if (es.value) {
        try { es.value.close() } catch (e) {}
        es.value = null
      }

      const url = `http://localhost:8123/api/ai/love_app/chat/sse?message=${encodeURIComponent(msg)}&chatId=${encodeURIComponent(chatId.value)}`
      try {
        es.value = new EventSource(url)
      } catch (e) {
        appendAiChunk('\n[SSE 连接失败]')
        isStreaming.value = false
        return
      }

      es.value.onmessage = (ev) => {
        const data = ev.data
        if (!data) return
        if (data === '[DONE]') {
          try { es.value.close() } catch (e) {}
          es.value = null
          finishStreaming()
          return
        }
        appendAiChunk(data)
      }

      es.value.onerror = (err) => {
        try { es.value.close() } catch (e) {}
        es.value = null
        isStreaming.value = false
      }
    }

    return { chatId, messages, input, send, logRef, isStreaming }
  }
}
</script>

<style scoped>
/* ========== 色彩方案 ========== */
:root {
  --primary-warm: #d64055;     /* 深玫瑰红 */
  --primary-light: #f5a7b8;    /* 浅玫瑰 */
  --accent-gold: #f4b860;      /* 温暖金色 */
  --accent-purple: #d4a4c8;    /* 紫色调 */
  --bg-light: #faf6f3;         /* 温暖象牙白 */
  --text-dark: #3d3d3d;        /* 深灰色 */
}

/* ========== 根布局 ========== */
.love-root {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: linear-gradient(135deg, #faf6f3 0%, #f5e8e3 50%, #faf6f3 100%);
  font-family: 'Segoe UI', -apple-system, BlinkMacSystemFont, sans-serif;
  color: var(--text-dark);
  position: relative;
  overflow: hidden;
}

/* 背景光晕 */
.background-glow {
  position: fixed;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(244, 184, 96, 0.1) 0%, transparent 70%);
  pointer-events: none;
  z-index: 0;
  animation: glow-shift 8s ease-in-out infinite;
}

@keyframes glow-shift {
  0%, 100% { transform: translate(0, 0); }
  50% { transform: translate(20px, 20px); }
}

/* 浮动爱心 */
.floating-hearts {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.heart {
  position: absolute;
  bottom: -50px;
  font-size: 24px;
  opacity: 0.15;
  animation: float-heart 8s ease-in infinite;
  color: #d64055;
}

@keyframes float-heart {
  0% {
    transform: translateY(0) translateX(0) rotate(0deg);
    opacity: 0;
  }
  10% {
    opacity: 0.15;
  }
  90% {
    opacity: 0.15;
  }
  100% {
    transform: translateY(-100vh) translateX(50px) rotate(360deg);
    opacity: 0;
  }
}

/* ========== 顶部栏 ========== */
.love-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: linear-gradient(90deg, #d64055 0%, #e077a8 50%, #d64055 100%);
  color: #fff;
  box-shadow: 0 4px 15px rgba(214, 64, 85, 0.2);
  position: relative;
  z-index: 10;
}

.love-header .left .back {
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s;
}

.love-header .left .back:hover {
  opacity: 0.7;
}

.love-header .center {
  font-weight: 700;
  font-size: 18px;
  letter-spacing: 0.5px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.title-heart {
  display: inline-block;
  animation: heart-beat 1.2s ease-in-out infinite;
}

@keyframes heart-beat {
  0%, 100% { transform: scale(1); }
  25% { transform: scale(1.15); }
  50% { transform: scale(1); }
}

.privacy-indicator {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  background: rgba(255, 255, 255, 0.15);
  padding: 6px 12px;
  border-radius: 12px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.lock-icon {
  font-size: 14px;
}

.private-text {
  letter-spacing: 0.5px;
}

/* ========== 聊天面板 ========== */
.chat-panel {
  flex: 1;
  overflow-y: auto;
  padding: 24px 18px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  background: transparent;
  position: relative;
  z-index: 5;
}

.chat-panel::-webkit-scrollbar {
  width: 6px;
}

.chat-panel::-webkit-scrollbar-track {
  background: transparent;
}

.chat-panel::-webkit-scrollbar-thumb {
  background: rgba(214, 64, 85, 0.2);
  border-radius: 3px;
}

.chat-panel::-webkit-scrollbar-thumb:hover {
  background: rgba(214, 64, 85, 0.4);
}

/* 消息行 */
.message-row {
  display: flex;
  align-items: flex-end;
  gap: 10px;
  animation: message-enter 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
}

@keyframes message-enter {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.message-row.ai {
  justify-content: flex-start;
}

.message-row.user {
  justify-content: flex-end;
}

/* 头像 */
.avatar {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: all 0.3s;
}

.message-row.ai .avatar {
  background: linear-gradient(135deg, #d64055 0%, #f4b860 100%);
  box-shadow: 0 4px 12px rgba(214, 64, 85, 0.15);
}

.user-avatar {
  background: linear-gradient(135deg, #d4a4c8 0%, #f5a7b8 100%);
  box-shadow: 0 4px 12px rgba(212, 164, 200, 0.15);
}

.avatar-icon {
  font-size: 20px;
}

/* 消息气泡 */
.bubble {
  max-width: 75%;
  padding: 12px 16px;
  border-radius: 18px;
  position: relative;
  word-wrap: break-word;
  white-space: pre-wrap;
  line-height: 1.5;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: all 0.3s;
}

.ai-bubble {
  background: linear-gradient(135deg, #fff8f5, #fef1ed);
  color: var(--text-dark);
  border: 1px solid rgba(244, 184, 96, 0.2);
}

.user-bubble {
  background: linear-gradient(135deg, #d64055, #eb5c7d);
  color: #fff;
  border: none;
}

.bubble:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.bubble-text {
  font-size: 14px;
  margin-bottom: 6px;
}

.time {
  font-size: 11px;
  color: rgba(61, 61, 61, 0.5);
  text-align: right;
}

.message-row.user .time {
  color: rgba(255, 255, 255, 0.7);
}

/* 加载动画 */
.typing {
  display: flex;
  gap: 6px;
  padding: 8px 4px;
}

.typing span {
  display: inline-block;
  width: 8px;
  height: 8px;
  background: var(--text-dark);
  border-radius: 50%;
  animation: blink 1.4s infinite;
  opacity: 0.4;
}

.typing span:nth-child(1) { animation-delay: 0s; }
.typing span:nth-child(2) { animation-delay: 0.2s; }
.typing span:nth-child(3) { animation-delay: 0.4s; }

@keyframes blink {
  0%, 60%, 100% {
    opacity: 0.4;
    transform: translateY(0);
  }
  30% {
    opacity: 1;
    transform: translateY(-8px);
  }
}

/* ========== 消息框 ========== */
.composer {
  padding: 16px 18px;
  border-top: 1px solid rgba(214, 64, 85, 0.1);
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(10px);
  position: relative;
  z-index: 10;
}

.composer-inner {
  max-width: 1100px;
  margin: 0 auto;
  display: flex;
  gap: 10px;
}

.composer input {
  flex: 1;
  padding: 12px 16px;
  border-radius: 24px;
  border: 1.5px solid rgba(214, 64, 85, 0.2);
  outline: none;
  background: #fff;
  color: var(--text-dark);
  font-size: 14px;
  transition: all 0.3s;
}

.composer input:focus {
  border-color: rgba(214, 64, 85, 0.5);
  box-shadow: 0 0 0 3px rgba(214, 64, 85, 0.1);
  background: #fff;
}

.composer input::placeholder {
  color: rgba(61, 61, 61, 0.4);
}

.composer .send {
  width: 44px;
  height: 44px;
  border: none;
  border-radius: 50%;
  background: linear-gradient(135deg, #d64055, #eb5c7d);
  color: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  box-shadow: 0 4px 12px rgba(214, 64, 85, 0.2);
  font-size: 0;
}

.send-icon {
  font-size: 18px;
}

.composer .send:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 18px rgba(214, 64, 85, 0.3);
}

.composer .send:active {
  transform: scale(0.95);
}

/* ========== 响应式 ========== */
@media (max-width: 768px) {
  .bubble {
    max-width: 85%;
  }
  
  .love-header {
    padding: 10px 12px;
  }

  .love-header .center {
    font-size: 16px;
  }

  .privacy-indicator {
    font-size: 11px;
    padding: 4px 8px;
  }

  .chat-panel {
    padding: 16px 12px;
  }
}

@media (max-width: 480px) {
  .bubble {
    max-width: 90%;
  }

  .heart {
    font-size: 18px;
  }
}
</style>
