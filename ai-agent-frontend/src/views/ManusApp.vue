<template>
  <div class="manus-root">
    <header class="topbar manus-top">
      <div class="left"><router-link to="/" class="back">← 返回</router-link></div>
      <div class="center">AI超级智能体</div>
      <div class="right"></div>
    </header>

    <main class="chat-panel" ref="logRef">
      <div v-for="(m, idx) in messages" :key="m.id" :class="['message-row', m.sender]" :style="m.sender==='ai' ? { '--color-idx': idx % 4 } : {}">
        <div class="avatar" v-if="m.sender==='ai'">🤖</div>
        <div class="bubble" :class="m.sender==='ai' ? 'ai-bubble-' + (idx % 4) : ''">
          <div class="bubble-text">{{ m.text }}</div>
          <div class="time">{{ m.time }}</div>
        </div>
        <div class="avatar user-avatar" v-if="m.sender==='user'">我</div>
      </div>
      <div v-if="isStreaming" class="message-row ai streaming">
        <div class="avatar">🤖</div>
        <div class="bubble ai-bubble-0">
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
        { text: '❤️ 您好！我是Manus，您的AI超级智能体', delay: 0 },
        { text: '✍️ 我拥有自主规划能力，可以帮您解决大类专业问题', delay: 300 },
        { text: '🔧 实可以调用8种工具：网络搜索、文件处理、PDF生成、图片搜索等', delay: 600 },
        { text: '💡 请告诉我您需要什么帮助，我会尽快完整规划的执行计划', delay: 900 }
      ]
      
      welcomeMessages.forEach(msg => {
        setTimeout(() => {
          messages.value.push({
            id: genId(),
            sender: 'ai',
            text: msg.text,
            time: nowTime()
          })
          scrollToBottom()
        }, msg.delay)
      })
    })

    function scrollToBottom(){ nextTick(()=>{ const el=logRef.value; if(el) el.scrollTop=el.scrollHeight }) }

    function appendAiChunk(text){ isStreaming.value = true; const last = messages.value[messages.value.length-1]; if(last && last.sender==='ai'){ last.text += text; last.time = nowTime() } else { messages.value.push({ id: genId(), sender:'ai', text, time: nowTime() }) } scrollToBottom() }

    function finishStreaming(){ isStreaming.value = false }

    function send(){ const msg = input.value && input.value.trim(); if(!msg) return; messages.value.push({ id: genId(), sender:'user', text: msg, time: nowTime() }); input.value = '';
      if(es.value){ try{ es.value.close() }catch(e){} es.value = null }
      const url = `http://localhost:8123/api/ai/manus/chat?message=${encodeURIComponent(msg)}`
      try{ es.value = new EventSource(url) }catch(e){ appendAiChunk('\n[SSE 连接失败]'); return }
      es.value.onmessage = (ev)=>{ const data = ev.data; if(!data) return; if(data === '[DONE]'){ try{ es.value.close() }catch(e){} es.value = null; finishStreaming(); return } appendAiChunk(data) }
      es.value.onerror = ()=>{ try{ es.value.close() }catch(e){} es.value = null; isStreaming.value=false }
    }

    return { messages, input, send, isStreaming, logRef }
  }
}
</script>

<style scoped>
.manus-root{display:flex;flex-direction:column;height:100vh;background:#f5f7fb}
.manus-top{display:flex;align-items:center;justify-content:space-between;padding:10px 16px;background:#3b63d9;color:#fff}
.manus-top .center{font-weight:700}
.chat-panel{flex:1;overflow:auto;padding:18px;display:flex;flex-direction:column;gap:12px}
.message-row{display:flex;align-items:flex-end;gap:10px}
.message-row.ai{justify-content:flex-start}
.message-row.user{justify-content:flex-end}
.avatar{width:36px;height:36px;border-radius:50%;display:flex;align-items:center;justify-content:center;background:#fff;box-shadow:0 2px 6px rgba(0,0,0,0.06)}
.user-avatar{background:#4f9aff;color:#fff;font-weight:600}
.bubble{max-width:70%;padding:10px 14px;border-radius:12px;background:#fff;box-shadow:0 6px 18px rgba(14,30,80,0.04);position:relative}
.message-row.user .bubble{background:#2f8dfc;color:#fff}
.ai-bubble-0{background:linear-gradient(135deg,#a8f0a8,#7fddaa) !important;color:#333}
.ai-bubble-1{background:linear-gradient(135deg,#d4b5ff,#c8a2ff) !important;color:#333}
.ai-bubble-2{background:linear-gradient(135deg,#ffe5b4,#ffd699) !important;color:#333}
.ai-bubble-3{background:linear-gradient(135deg,#a8d8ff,#7fc5ff) !important;color:#333}
.bubble-text{white-space:pre-wrap}
.time{font-size:11px;color:rgba(0,0,0,0.35);margin-top:6px;text-align:right}
.typing{display:flex;gap:6px;padding:6px}
.typing span{display:inline-block;width:8px;height:8px;background:#dfe9ff;border-radius:50%;animation:blink 1s infinite}
.typing span:nth-child(2){animation-delay:0.15s}
.typing span:nth-child(3){animation-delay:0.3s}
@keyframes blink{0%{opacity:0.2;transform:translateY(0)}50%{opacity:1;transform:translateY(-4px)}100%{opacity:0.2;transform:translateY(0)}}
.composer{padding:12px 18px;border-top:1px solid rgba(0,0,0,0.04);background:transparent}
.composer-inner{max-width:1100px;margin:0 auto;display:flex;gap:10px}
.composer input{flex:1;padding:12px;border-radius:999px;border:1px solid #e6eefb;outline:none;background:#fff}
.composer .send{background:linear-gradient(90deg,#4f9aff,#6fb1ff);border:none;color:#fff;padding:10px 18px;border-radius:999px;cursor:pointer;box-shadow:0 8px 18px rgba(79,154,255,0.14)}
@media (max-width:880px){ .composer-inner{padding:0 12px} }
</style>
