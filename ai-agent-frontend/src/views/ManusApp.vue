<template>
  <div class="manus-root">
    <!-- 背景装饰 -->
    <div class="background-glow"></div>
    <div class="floating-bubbles">
      <div class="bubble-item" v-for="i in 3" :key="i" :style="{left: (30*i)+'%', animationDelay: (i*0.5)+'s'}"></div>
    </div>

    <header class="topbar manus-top">
      <div class="left"><router-link to="/" class="back">← 返回</router-link></div>
      <div class="center">
        <span class="skill-icon">✨</span>
        恋爱计划师
      </div>
      <div class="action-badge">
        <span class="action-dot">●</span>
        <span class="action-text">智能规划中</span>
      </div>
    </header>

    <main class="chat-panel" ref="logRef">
      <div v-for="(m, idx) in messages" :key="m.id" :class="['message-row', m.sender, {hasThinking: m.thinking}]" :style="m.sender==='ai' ? { '--color-idx': idx % 4 } : {}">
        <div class="avatar ai-avatar" v-if="m.sender==='ai'">
          <span class="avatar-inner">💖</span>
          <div class="avatar-glow"></div>
        </div>
        <div class="bubble" :class="m.sender==='ai' ? 'ai-bubble-' + (idx % 4) : ''">
          <!-- 思考过程 (可折叠) -->
          <div v-if="m.thinking" class="thinking-container">
            <div class="thinking-header" @click="toggleThinking(m.id)">
              <span class="thinking-icon" :class="{ 'opened': m.thinkingOpen }">▶</span>
              <span class="thinking-label">Thinking</span>
            </div>
            <div v-if="m.thinkingOpen" class="thinking-content">
              {{ m.thinking }}
            </div>
          </div>
          <!-- 最终结果（支持 HTML 格式化） -->
          <div class="bubble-text" v-html="m.html || (m.text && m.text.replace(/\n/g, '<br/>'))"></div>
          <div class="time">{{ m.time }}</div>
        </div>
        <div class="avatar user-avatar" v-if="m.sender==='user'">
          <span class="avatar-inner">😊</span>
        </div>
      </div>
      <div v-if="isStreaming" class="message-row ai streaming">
        <div class="avatar ai-avatar">
          <span class="avatar-inner">💖</span>
          <div class="avatar-glow"></div>
        </div>
        <div class="bubble ai-bubble-0">
          <div class="thinking-dots">
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
          </div>
          <div class="status-text">正在规划恋爱策略...</div>
        </div>
      </div>
    </main>

    <footer class="composer">
      <div class="composer-inner">
        <input v-model="input" @keyup.enter="send" placeholder="描述你的恋爱目标与困境..." />
        <button class="send" @click="send">
          <span class="send-icon">💝</span>
        </button>
      </div>
    </footer>
  </div>
</template>

<script>
import { ref, onMounted, nextTick } from 'vue'

function genId() { return Date.now().toString(36) + Math.random().toString(36).slice(2,8) }
function nowTime(){ const d=new Date(); return d.toLocaleTimeString([], {hour:'2-digit', minute:'2-digit'}) }

export default {
  setup(){
    const messages = ref([])
    const input = ref('')
    const es = ref(null)
    const isStreaming = ref(false)
    const logRef = ref(null)

    onMounted(() => {
      // 显示多条欢迎消息展示功能
      const welcomeMessages = [
        { text: '❤️ 您好！我是Manus，您的恋爱计划师', delay: 0 },
        { text: '✍️ 我拥有自主规划能力，可以帮您解决大类恋爱专业问题', delay: 300 },
        { text: '🔧 可以调用工具：网络搜索、文件处理、PDF生成、图片搜索等', delay: 600 },
        { text: '💡 请告诉我您需要什么帮助，我会尽快完整规划的执行计划', delay: 900 }
      ]
      
      welcomeMessages.forEach(msg => {
        setTimeout(() => {
          messages.value.push({
            id: genId(),
            sender: 'ai',
            text: msg.text,
            thinking: null,
            thinkingOpen: false,
            time: nowTime()
          })
          scrollToBottom()
        }, msg.delay)
      })
    })

    function scrollToBottom(){ nextTick(()=>{ const el=logRef.value; if(el) el.scrollTop=el.scrollHeight }) }

    function toggleThinking(msgId) {
      const msg = messages.value.find(m => m.id === msgId)
      if (msg) {
        msg.thinkingOpen = !msg.thinkingOpen
      }
    }

    // 将后端返回的文本按“步骤”或空行拆分为多条消息
    function splitIntoParts(text){
      if(!text) return []
      // 优先按 Step N: 或 Step N： 标记拆分
      const stepRegex = /(?=\bStep\s*\d+\s*[:：])/i
      if(stepRegex.test(text)){
        return text.split(stepRegex).map(s=>s.trim()).filter(Boolean)
      }
      // 再按双换行拆分为段落
      if(text.includes('\n\n')){
        return text.split(/\n{2,}/).map(s=>s.trim()).filter(Boolean)
      }
      // 否则作为单段返回
      return [text]
    }

    // 把原始文本转换成带 HTML 的消息，方便展示：
    function formatMessageHtml(text){
      if(!text) return ''
      let t = String(text)
      // 先还原常见的转义表示，便于阅读
      t = t.replace(/\\\"/g, '"').replace(/\\n/g, '\n').replace(/\\t/g, '\t')

      // 如果以 Step N: 开头，将标题和正文拆分为独立展示
      const headerMatch = t.match(/^\s*(Step\s*\d+\s*[:：][^\n\r]*)/i)
      if(headerMatch){
        const header = headerMatch[1].trim()
        const rest = t.slice(headerMatch[0].length).trim()
        const headerHtml = `<div class=\"step-header\">${escapeHtml(header)}</div>`
        if(rest){
          // 如果正文看起来像 JSON，尝试格式化
          const bodyHtml = tryFormatJsonOrPre(rest)
          return headerHtml + `<div class=\"step-body\">${bodyHtml}</div>`
        }
        return headerHtml
      }

      // 否则尽量格式化为 JSON 或保留换行的 pre-wrap 文本
      return tryFormatJsonOrPre(t)
    }

    function tryFormatJsonOrPre(str){
      const s = str.trim()
      // 尝试解析为 JSON（容忍首尾多余内容）
      try{
        const maybe = s.match(/(\{[\s\S]*\}|\[[\s\S]*\])$/)
        if(maybe){
          const parsed = JSON.parse(maybe[0])
          return `<pre class=\"result-pre\">${escapeHtml(JSON.stringify(parsed, null, 2))}</pre>`
        }
      }catch(e){}
      // 退回到保留换行和长行换行的 pre
      return `<pre class=\"result-pre\">${escapeHtml(s)}</pre>`
    }

    function escapeHtml(s){
      return String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/\"/g, '&quot;')
    }

    function createMessageFromRaw(text){
      const html = formatMessageHtml(text)
      return { text: String(text), html }
    }

    function appendAiChunk(text){ 
      isStreaming.value = true
      const parts = splitIntoParts(text)
      // 如果接收到多段内容，把每段作为独立的消息气泡
      if(parts.length > 1){
        parts.forEach(p => {
          messages.value.push(Object.assign({ id: genId(), sender: 'ai', thinking: null, thinkingOpen: false, time: nowTime() }, createMessageFromRaw(p)))
        })
      } else {
        // 单段：如果是连续流（没有明显步骤标记），则追加到最后一条 AI 消息
        const last = messages.value[messages.value.length-1]
        const p = parts[0]
        if(last && last.sender === 'ai' && !/\bStep\s*\d+\b/i.test(p) && p.length < 300){
          last.text = String(last.text || '') + p
          last.html = formatMessageHtml(last.text)
          last.time = nowTime()
        } else {
          messages.value.push(Object.assign({ id: genId(), sender: 'ai', thinking: null, thinkingOpen: false, time: nowTime() }, createMessageFromRaw(p)))
        }
      }
      scrollToBottom()
    }

    function finishStreaming(){ isStreaming.value = false }

    function send(){ 
      const msg = input.value && input.value.trim()
      if(!msg) return
      messages.value.push({ 
        id: genId(), 
        sender:'user', 
        text: msg, 
        time: nowTime() 
      })
      input.value = ''
      if(es.value){ try{ es.value.close() }catch(e){} es.value = null }
      const url = `http://localhost:8123/api/ai/manus/chat?message=${encodeURIComponent(msg)}`
      try{ es.value = new EventSource(url) }catch(e){ appendAiChunk('\n[SSE 连接失败]'); return }
      es.value.onmessage = (ev)=>{ 
        const data = ev.data
        if(!data) return
        if(data === '[DONE]'){ 
          try{ es.value.close() }catch(e){} 
          es.value = null
          finishStreaming()
          return 
        }
        appendAiChunk(data) 
      }
      es.value.onerror = ()=>{ 
        try{ es.value.close() }catch(e){} 
        es.value = null
        isStreaming.value=false 
      }
    }

    return { messages, input, send, isStreaming, logRef, toggleThinking }
  }
}
</script>

<style scoped>
/* ========== 颜色方案 ========== */
:root {
  --primary-warm: #d64055;     /* 深玫瑰红 */
  --primary-light: #f5a7b8;    /* 浅玫瑰 */
  --accent-gold: #f4b860;      /* 温暖金色 */
  --accent-purple: #d4a4c8;    /* 紫色调 */
  --bg-light: #faf6f3;         /* 温暖象牙白 */
  --text-dark: #3d3d3d;        /* 深灰色 */
}

/* ========== 根布局 ========== */
.manus-root {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: linear-gradient(135deg, #fef8f6 0%, #faf6f3 50%, #f5ede8 100%);
  font-family: 'Segoe UI', -apple-system, BlinkMacSystemFont, sans-serif;
  color: var(--text-dark);
  position: relative;
  overflow: hidden;
}

/* 背景光晕 */
.background-glow {
  position: fixed;
  top: -50%;
  right: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(244, 184, 96, 0.08) 0%, transparent 70%);
  pointer-events: none;
  z-index: 0;
  animation: glow-shift 10s ease-in-out infinite;
}

@keyframes glow-shift {
  0%, 100% { transform: translate(0, 0); }
  50% { transform: translate(30px, -30px); }
}

/* 浮动气泡 */
.floating-bubbles {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.bubble-item {
  position: absolute;
  bottom: -50px;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  opacity: 0.08;
  animation: float-bubble 10s ease-in infinite;
  border: 2px solid var(--primary-warm);
}

@keyframes float-bubble {
  0% {
    transform: translateY(0) translateX(0);
    opacity: 0;
  }
  10% {
    opacity: 0.08;
  }
  90% {
    opacity: 0.08;
  }
  100% {
    transform: translateY(-100vh) translateX(100px);
    opacity: 0;
  }
}

/* ========== 顶部栏 ========== */
.manus-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: linear-gradient(90deg, #d64055 0%, #e077a8 50%, #d64055 100%);
  color: #fff;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  position: relative;
  z-index: 10;
  box-shadow: 0 4px 15px rgba(214, 64, 85, 0.15);
}

.manus-top .left .back {
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s;
}

.manus-top .left .back:hover {
  opacity: 0.7;
}

.manus-top .center {
  font-weight: 700;
  font-size: 16px;
  letter-spacing: 0.5px;
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
  margin-left: 16px;
}

.skill-icon {
  font-size: 18px;
  animation: skill-twinkle 1.5s ease-in-out infinite;
}

@keyframes skill-twinkle {
  0%, 100% { opacity: 1; transform: scale(1); }
  50% { opacity: 0.7; transform: scale(1.1); }
}

.action-badge {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  background: rgba(255, 255, 255, 0.15);
  padding: 6px 12px;
  border-radius: 12px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  letter-spacing: 0.3px;
}

.action-dot {
  font-size: 8px;
  animation: dot-blink 1s ease-in-out infinite;
}

@keyframes dot-blink {
  0%, 100% { opacity: 0.4; }
  50% { opacity: 1; }
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

.message-row.hasThinking .bubble {
  padding: 0;
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
  position: relative;
}

.ai-avatar {
  background: linear-gradient(135deg, #d64055 0%, #f4b860 100%);
  box-shadow: 0 4px 12px rgba(214, 64, 85, 0.15);
}

.avatar-glow {
  position: absolute;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(244, 184, 96, 0.3) 0%, transparent 70%);
  opacity: 0;
  transition: opacity 0.3s;
}

.ai-avatar:hover .avatar-glow {
  opacity: 1;
  animation: glow-pulse 0.6s ease-in-out;
}

@keyframes glow-pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.2); }
}

.user-avatar {
  background: linear-gradient(135deg, #d4a4c8 0%, #f5a7b8 100%);
  box-shadow: 0 4px 12px rgba(212, 164, 200, 0.15);
}

.avatar-inner {
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
  transition: all 0.3s;
}

.ai-bubble {
  background: linear-gradient(135deg, #fff8f5, #fef1ed);
  color: var(--text-dark);
  border: 1px solid rgba(244, 184, 96, 0.2);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.message-row.user .bubble {
  background: linear-gradient(135deg, #d64055, #eb5c7d);
  color: #fff;
  border: none;
  box-shadow: 0 4px 12px rgba(214, 64, 85, 0.2);
}

.bubble:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.ai-bubble-0 {
  background: linear-gradient(135deg, #fff8f5, #fef1ed) !important;
  border-color: rgba(244, 184, 96, 0.2) !important;
}

.ai-bubble-1 {
  background: linear-gradient(135deg, #f5f0ff, #eeeaff) !important;
  border-color: rgba(212, 164, 200, 0.2) !important;
}

.ai-bubble-2 {
  background: linear-gradient(135deg, #fff5e6, #fff0d9) !important;
  border-color: rgba(244, 184, 96, 0.2) !important;
}

.ai-bubble-3 {
  background: linear-gradient(135deg, #f0f8ff, #e6f4ff) !important;
  border-color: rgba(212, 164, 200, 0.2) !important;
}

.bubble-text {
  font-size: 14px;
  margin-bottom: 6px;
  color: var(--text-dark);
}

.message-row.user .bubble-text {
  color: #fff;
}

.time {
  font-size: 11px;
  color: rgba(61, 61, 61, 0.5);
  text-align: right;
  margin-top: 6px;
}

.message-row.user .time {
  color: rgba(255, 255, 255, 0.7);
}

/* 思考容器 */
.thinking-container {
  margin-bottom: 12px;
  border-left: 2px solid rgba(214, 64, 85, 0.2);
  padding-left: 10px;
}

.thinking-header {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  user-select: none;
  color: rgba(61, 61, 61, 0.6);
  font-size: 13px;
  font-weight: 500;
  padding: 4px 0;
  transition: color 0.2s;
}

.thinking-header:hover {
  color: var(--primary-warm);
}

.thinking-icon {
  display: inline-block;
  width: 16px;
  text-align: center;
  transform: rotate(0deg);
  transition: transform 0.3s;
  color: var(--primary-warm);
}

.thinking-icon.opened {
  transform: rotate(90deg);
}

.thinking-content {
  margin-top: 8px;
  padding: 10px;
  background: rgba(214, 64, 85, 0.06);
  border-radius: 8px;
  color: rgba(61, 61, 61, 0.7);
  font-size: 12px;
  white-space: pre-wrap;
  word-break: break-word;
  max-height: 300px;
  overflow-y: auto;
  line-height: 1.5;
  border: 1px solid rgba(214, 64, 85, 0.1);
}

/* 规划中的加载动画 */
.thinking-dots {
  display: flex;
  gap: 6px;
  padding: 8px 4px;
}

.thinking-dots .dot {
  width: 8px;
  height: 8px;
  background: var(--primary-warm);
  border-radius: 50%;
  animation: dot-pulse 1.4s infinite;
  opacity: 0.6;
}

.thinking-dots .dot:nth-child(1) { animation-delay: 0s; }
.thinking-dots .dot:nth-child(2) { animation-delay: 0.2s; }
.thinking-dots .dot:nth-child(3) { animation-delay: 0.4s; }

@keyframes dot-pulse {
  0%, 60%, 100% {
    opacity: 0.6;
    transform: translateY(0);
  }
  30% {
    opacity: 1;
    transform: translateY(-8px);
  }
}

.status-text {
  font-size: 13px;
  color: rgba(61, 61, 61, 0.6);
  margin-top: 6px;
  font-style: italic;
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

/* ========== Step / Result 格式化样式 ========== */
.step-header {
  font-weight: 700;
  margin-bottom: 8px;
  color: var(--primary-warm);
  font-size: 14px;
}
.step-body {
  margin-top: 4px;
}
.result-pre {
  white-space: pre-wrap;
  word-break: break-word;
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, 'Roboto Mono', 'Helvetica Neue', monospace;
  background: rgba(0,0,0,0.03);
  padding: 10px;
  border-radius: 8px;
  border: 1px solid rgba(0,0,0,0.06);
  font-size: 13px;
  color: rgba(30,30,30,0.9);
  overflow: auto;
  max-height: 480px;
}

/* ========== 响应式 ========== */
@media (max-width: 768px) {
  .bubble {
    max-width: 85%;
  }

  .manus-top {
    padding: 10px 12px;
  }

  .manus-top .center {
    font-size: 14px;
    margin-left: 8px;
  }

  .action-badge {
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

  .bubble-item {
    width: 40px;
    height: 40px;
  }
}
</style>
