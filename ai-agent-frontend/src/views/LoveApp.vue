<template>
  <div class="love-root">
    <header class="love-header topbar">
      <div class="left">
        <router-link to="/" class="back">← 返回</router-link>
      </div>
      <div class="center">AI恋爱大师</div>
      <div class="right">会话ID：<span class="chatid">{{ chatId }}</span></div>
    </header>

    <main class="chat-panel" ref="logRef">
      <div v-for="m in messages" :key="m.id" :class="['message-row', m.sender]">
        <div class="avatar" v-if="m.sender==='ai'">💬</div>
        <div class="bubble">
          <div class="bubble-text" v-html="m.text"></div>
          <div class="time">{{ m.time }}</div>
        </div>
        <div class="avatar user-avatar" v-if="m.sender==='user'">😊</div>
      </div>
      <div v-if="isStreaming" class="message-row ai streaming">
        <div class="avatar">💬</div>
        <div class="bubble">
          <div class="typing">
            <span></span><span></span><span></span>
          </div>
        </div>
      </div>
    </main>

    <footer class="composer">
      <div class="composer-inner">
        <input v-model="input" @keyup.enter="send" placeholder="请输入消息..." />
        <button class="send" @click="send">发送</button>
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
.root, :root{
  /* 更强的恋爱色调：粉色渐变 + 柔和卡片 */
  --bg1: #ff6b93; /* 深粉 */
  --bg2: #ffd3e6; /* 浅粉 */
  --card: rgba(255,250,252,0.96);
  --ai-bubble: #fff;
  --user-bubble: linear-gradient(135deg,#ffd1e6,#ff7aa0);
}
.love-root{
  display:flex;flex-direction:column;height:100vh;background:linear-gradient(180deg,var(--bg1),var(--bg2));font-family:Segoe UI,Roboto,Arial,sans-serif;color:#222;
}
.love-header.topbar{display:flex;align-items:center;justify-content:space-between;padding:10px 16px;background:linear-gradient(90deg,#ff6b93,#ff9ab8);color:#fff}
.love-header .left .back{color:rgba(255,255,255,0.95);text-decoration:none}
.love-header .center{font-weight:700;font-size:18px}
.love-header .right{font-size:13px;opacity:0.95}

.chat-panel{flex:1;overflow:auto;padding:20px 18px;display:flex;flex-direction:column;gap:14px;background:linear-gradient(#fff,#fff);}
.message-row{display:flex;align-items:flex-end;gap:10px}
.message-row.ai{justify-content:flex-start}
.message-row.user{justify-content:flex-end}
.avatar{width:40px;height:40px;border-radius:50%;display:flex;align-items:center;justify-content:center;background:rgba(255,255,255,0.6);box-shadow:0 2px 6px rgba(0,0,0,0.08)}
.user-avatar{background:linear-gradient(135deg,#fff 0%, #ffeef2 100%)}
.bubble{max-width:70%;padding:10px 14px;border-radius:12px;background:#f3f4f6;box-shadow:none;position:relative}
.message-row.user .bubble{background:#2f8dfc;color:#fff;border-radius:12px}
.bubble-text{white-space:pre-wrap;word-wrap:break-word}
.time{font-size:11px;color:rgba(0,0,0,0.45);margin-top:8px;text-align:right}

.composer{padding:12px 18px;border-top:1px solid rgba(0,0,0,0.06);background:transparent}
.composer-inner{max-width:1100px;margin:0 auto;display:flex;gap:10px}
.composer input{flex:1;padding:12px;border-radius:999px;border:1px solid #eee;outline:none;background:#fff}
.composer .send{background:linear-gradient(90deg,#ff5f87,#ff9ab8);border:none;color:#fff;padding:10px 18px;border-radius:999px;cursor:pointer;box-shadow:0 8px 18px rgba(255,95,135,0.12)}

.streaming .bubble{background:rgba(255,255,255,0.95)}
.typing{display:flex;gap:6px;padding:6px}
.typing span{display:inline-block;width:8px;height:8px;background:#e6e6e6;border-radius:50%;animation:blink 1s infinite}
.typing span:nth-child(2){animation-delay:0.15s}
.typing span:nth-child(3){animation-delay:0.3s}
@keyframes blink{0%{opacity:0.2;transform:translateY(0)}50%{opacity:1;transform:translateY(-4px)}100%{opacity:0.2;transform:translateY(0)}}

/* small screens */
@media (max-width:600px){
  .bubble{max-width:85%}
}
</style>
