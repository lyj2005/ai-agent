<template>
  <div class="home-container">
    <!-- 动态背景元素 -->
    <div class="background-grid"></div>
    <div class="floating-orbs">
      <div class="orb orb-1"></div>
      <div class="orb orb-2"></div>
      <div class="orb orb-3"></div>
    </div>

    <div class="container">
      <!-- 顶部品牌展示区 -->
      <header class="top">
        <div class="brand-wrapper">
          <div class="glow-badge">LOVE MASTER</div>
          <h1 class="title">恋爱大师</h1>
          <p class="subtitle">AI 驱动的情感智能平台</p>
          <p class="lead">
            <span class="highlight-text">智能情感顾问</span> · 
            <span class="highlight-text">温暖陪伴</span> · 
            <span class="highlight-text">聪慧助手</span>
          </p>
        </div>
      </header>

      <!-- 数据统计条 -->
      <section class="stats-bar">
        <div class="stat-item">
          <div class="stat-number">10M+</div>
          <div class="stat-label">情感对话</div>
        </div>
        <div class="stat-divider"></div>
        <div class="stat-item">
          <div class="stat-number">99.8%</div>
          <div class="stat-label">用户满意度</div>
        </div>
        <div class="stat-divider"></div>
        <div class="stat-item">
          <div class="stat-number">24/7</div>
          <div class="stat-label">智能陪伴</div>
        </div>
      </section>

      <!-- 功能卡片区 -->
      <section class="choices">
        <div class="card" :style="{animationDelay: '0.1s'}">
          <div class="card-header">
            <div class="icon-wrap">
              <div class="icon">👩‍⚕️</div>
              <div class="icon-glow"></div>
            </div>
            <div class="card-badge">LOVE</div>
          </div>
          <h3>恋爱问诊</h3>
          <p class="card-desc">专业的 AI 情感顾问，深入理解你的内心世界</p>
          <ul class="feature-list">
            <li>✨ 实时情感分析</li>
            <li>💭 个性化建议</li>
            <li>🎯 深层心理洞察</li>
          </ul>
          <router-link to="/love" class="btn btn-primary">开启恋爱之旅</router-link>
        </div>

        <div class="card" :style="{animationDelay: '0.2s'}">
          <div class="card-header">
            <div class="icon-wrap">
              <div class="icon">✨</div>
              <div class="icon-glow"></div>
            </div>
            <div class="card-badge">PLAN</div>
          </div>
          <h3>恋爱计划师</h3>
          <p class="card-desc">智能规划你的恋爱策略，解决情感难题</p>
          <ul class="feature-list">
            <li>💡 智能分析与推理</li>
            <li>🎯 自主行动规划</li>
            <li>🔄 实时反馈调整</li>
          </ul>
          <router-link to="/manus" class="btn btn-secondary">开启规划之旅</router-link>
        </div>
      </section>

      <!-- 底部品牌与链接区 -->
      <footer class="foot">
        <div class="foot-top">
          <div class="col">
            <div class="ft-title">🚀 关于平台</div>
            <div class="ft-links">
              <a href="#" @click.prevent="showModal='agreement'" class="link">服务协议</a>
              <span class="sep">·</span>
              <a href="#" @click.prevent="showModal='privacy'" class="link">隐私保护</a>
            </div>
          </div>
          <div class="col">
            <div class="ft-title">💻 友情链接</div>
            <div class="ft-links">
              <a href="https://coder-live.top/" target="_blank" class="link">智答工坊</a>
              <span class="sep">·</span>
              <a href="https://github.com/lyj2005" target="_blank" class="link">GitHub</a>
            </div>
          </div>
          <div class="col">
            <div class="ft-title">📧 联系我们</div>
            <div class="ft-links">
              <span>商务合作</span>
              <span class="sep">·</span>
              <a href="https://github.com/lyj2005" target="_blank" class="link">@lyjames</a>
            </div>
          </div>
        </div>
        <div class="foot-bottom">
          <div class="pulse-dot"></div>
          © 2026 AI恋爱大师 - 温暖你的情感生活 | Powered by AI
        </div>
      </footer>
    </div>

    <!-- 用户协议 Modal -->
    <div v-if="showModal==='agreement'" class="modal-backdrop" @click="showModal=''">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h2>用户协议</h2>
          <button class="close" @click="showModal=''">×</button>
        </div>
        <div class="modal-body">
          <p><strong>1. 服务概述</strong></p>
          <p>本平台为用户提供基于 AI 的恋爱咨询和情感辅导服务。通过使用本服务，用户同意遵守以下条款。</p>
          
          <p><strong>2. 用户义务</strong></p>
          <p>用户承诺不将本服务用于违法、违规或伤害他人的目的。用户应真实、准确地提供个人信息。</p>
          
          <p><strong>3. 知识产权</strong></p>
          <p>本平台上的所有内容（包括文本、图片、代码等）均为平台方或其许可方的财产，受著作权法保护。</p>
          
          <p><strong>4. 免责声明</strong></p>
          <p>本平台提供的建议仅供参考，用户应自主判断。平台对用户因使用本服务所产生的任何直接或间接损失不承担责任。</p>
          
          <p><strong>5. 服务中断</strong></p>
          <p>平台方保留随时修改、中断或终止服务的权利，恕不另行通知。</p>
          
          <p><strong>6. 协议修订</strong></p>
          <p>平台方有权随时修改本协议，修改后的协议自发布之日起生效。继续使用服务表示接受修改内容。</p>
        </div>
      </div>
    </div>

    <!-- 隐私政策 Modal -->
    <div v-if="showModal==='privacy'" class="modal-backdrop" @click="showModal=''">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h2>隐私政策</h2>
          <button class="close" @click="showModal=''">×</button>
        </div>
        <div class="modal-body">
          <p><strong>1. 信息收集</strong></p>
          <p>我们收集您提供的聊天信息、用户名等必要信息，用于改进服务和提供个性化体验。</p>
          
          <p><strong>2. 信息使用</strong></p>
          <p>我们仅使用您的信息来：提供和改进服务；分析用户行为和偏好；满足法律和安全要求。</p>
          
          <p><strong>3. 信息保护</strong></p>
          <p>我们采取合理的技术措施保护您的个人信息安全，包括加密、访问控制等。</p>
          
          <p><strong>4. 信息共享</strong></p>
          <p>我们不会向第三方出售或共享您的个人信息，除非法律要求或获得您的明确同意。</p>
          
          <p><strong>5. Cookie 使用</strong></p>
          <p>本平台使用 Cookie 来存储用户偏好和登录状态。您可以通过浏览器设置禁用 Cookie。</p>
          
          <p><strong>6. 用户权利</strong></p>
          <p>您有权查询、更新或删除您的个人信息。如有疑问，请联系我们。</p>
          
          <p><strong>7. 联系方式</strong></p>
          <p>如对本隐私政策有任何疑问，请通过 Github (https://github.com/lyj2005) 联系我们。</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'

export default {
  setup(){
    const showModal = ref('')
    const mouseX = ref(0)
    const mouseY = ref(0)

    onMounted(() => {
      // 鼠标跟踪光标效果
      document.addEventListener('mousemove', (e) => {
        mouseX.value = e.clientX / window.innerWidth
        mouseY.value = e.clientY / window.innerHeight
        
        // 动态更新背景光照
        const root = document.documentElement
        root.style.setProperty('--mouse-x', mouseX.value)
        root.style.setProperty('--mouse-y', mouseY.value)
      })

      // 滚动视差效果
      window.addEventListener('scroll', () => {
        const cards = document.querySelectorAll('.card')
        cards.forEach((card, index) => {
          const rect = card.getBoundingClientRect()
          const scrollPercent = 1 - (rect.top / window.innerHeight)
          card.style.transform = `translateY(${Math.max(0, scrollPercent * 30)}px)`
        })
      })
    })

    return { showModal, mouseX, mouseY }
  }
}
</script>
<style scoped>
/* ========== 根样式和CSS变量 ========== */
:root {
  --primary-gradient: linear-gradient(135deg, #d64055 0%, #eb5c7d 50%, #f5a7b8 100%);
  --secondary-gradient: linear-gradient(135deg, #e8c4d8 0%, #f5a7b8 100%);
  --glow-color: #d64055;
  --text-dark: #3d3d3d;
  --text-light: #8b7a81;
  --mouse-x: 0;
  --mouse-y: 0;
}

/* ========== 全局布局 ========== */
.home-container {
  position: relative;
  min-height: 100vh;
  background: linear-gradient(180deg, #fff5f8 0%, #fef0f4 50%, #fde8ef 100%);
  color: #3d3d3d;
  overflow: hidden;
}

/* 背景网格效果 */
.background-grid {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    linear-gradient(rgba(214, 64, 85, 0.04) 1px, transparent 1px),
    linear-gradient(90deg, rgba(214, 64, 85, 0.04) 1px, transparent 1px);
  background-size: 50px 50px;
  pointer-events: none;
  z-index: 0;
  animation: grid-drift 20s linear infinite;
}

@keyframes grid-drift {
  0% { transform: translate(0, 0); }
  100% { transform: translate(50px, 50px); }
}

/* 浮动光球背景 */
.floating-orbs {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(60px);
  opacity: 0.4;
  animation: float 15s ease-in-out infinite;
}

.orb-1 {
  width: 300px;
  height: 300px;
  background: linear-gradient(135deg, #f5a7b8, #f9d5e0);
  top: -150px;
  left: 10%;
  animation-delay: 0s;
}

.orb-2 {
  width: 250px;
  height: 250px;
  background: linear-gradient(135deg, #e8c4d8, #f4d4e5);
  bottom: -125px;
  right: 10%;
  animation-delay: 2s;
}

.orb-3 {
  width: 200px;
  height: 200px;
  background: linear-gradient(135deg, #f5a7b8, #fcc5d1);
  top: 50%;
  right: 5%;
  animation-delay: 4s;
}

@keyframes float {
  0%, 100% { transform: translate(0, 0) rotate(0deg); }
  25% { transform: translate(30px, -30px) rotate(90deg); }
  50% { transform: translate(0, 50px) rotate(180deg); }
  75% { transform: translate(-30px, -20px) rotate(270deg); }
}

/* ========== 容器 ========== */
.container {
  position: relative;
  z-index: 10;
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px 60px;
}

/* ========== 顶部品牌展示区 ========== */
.top {
  text-align: center;
  margin-bottom: 20px;
  animation: slide-down 0.8s cubic-bezier(0.34, 1.56, 0.64, 1) forwards;
  opacity: 0;
  animation-delay: 0.2s;
}

@keyframes slide-down {
  to { opacity: 1; transform: translateY(0); }
  from { opacity: 0; transform: translateY(-30px); }
}

.brand-wrapper {
  padding: 15px 0;
}

.glow-badge {
  display: inline-block;
  padding: 6px 12px;
  background: rgba(214, 64, 85, 0.12);
  border: 1px solid rgba(214, 64, 85, 0.4);
  border-radius: 20px;
  font-size: 11px;
  letter-spacing: 2px;
  color: #d64055;
  margin-bottom: 12px;
  animation: badge-glow 2s ease-in-out infinite;
}

@keyframes badge-glow {
  0%, 100% { 
    box-shadow: 0 0 10px rgba(214, 64, 85, 0.2);
    transform: scale(1);
  }
  50% { 
    box-shadow: 0 0 20px rgba(214, 64, 85, 0.4);
    transform: scale(1.05);
  }
}

.title {
  font-size: 42px;
  color: #d64055;
  margin: 12px 0;
  font-weight: 700;
  letter-spacing: 2px;
  background: linear-gradient(135deg, #d64055, #eb5c7d);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-shadow: 0 0 30px rgba(214, 64, 85, 0.1);
}

.subtitle {
  font-size: 14px;
  color: #8b5a6d;
  margin: 8px 0 12px;
  font-weight: 300;
  letter-spacing: 1px;
}

.lead {
  font-size: 13px;
  color: #9d6b7f;
  margin: 10px 0;
  letter-spacing: 0.5px;
}

.highlight-text {
  color: #d64055;
  font-weight: 600;
}

/* ========== 数据统计条 ========== */
.stats-bar {
  display: none;
  justify-content: center;
  align-items: center;
  gap: 50px;
  padding: 30px 0 60px;
  border-top: 1px solid rgba(214, 64, 85, 0.2);
  border-bottom: 1px solid rgba(214, 64, 85, 0.2);
  margin-bottom: 60px;
  animation: fade-in 0.8s ease-out 0.4s forwards;
  opacity: 0;
}

@keyframes fade-in {
  to { opacity: 1; }
}

.stat-item {
  text-align: center;
  flex: 1;
}

.stat-number {
  font-size: 28px;
  font-weight: 700;
  background: linear-gradient(135deg, #d64055, #eb5c7d);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.stat-label {
  font-size: 12px;
  color: #9d6b7f;
  margin-top: 6px;
  letter-spacing: 1px;
}

.stat-divider {
  width: 1px;
  height: 40px;
  background: linear-gradient(to bottom, transparent, rgba(214, 64, 85, 0.3), transparent);
}

/* ========== 功能卡片 ========== */
.choices {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 30px;
  justify-items: center;
  padding: 0;
  margin-bottom: 0;
}

.card {
  width: 100%;
  max-width: 400px;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.9), rgba(255, 250, 252, 0.95));
  border: 1px solid rgba(214, 64, 85, 0.25);
  border-radius: 16px;
  padding: 36px 28px;
  position: relative;
  overflow: hidden;
  backdrop-filter: blur(10px);
  transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
  animation: card-enter 0.6s cubic-bezier(0.34, 1.56, 0.64, 1) forwards;
  opacity: 0;
  transform: translateY(30px);
}

@keyframes card-enter {
  to { 
    opacity: 1; 
    transform: translateY(0);
  }
}

.card:hover {
  border-color: rgba(214, 64, 85, 0.5);
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(255, 245, 248, 1));
  transform: translateY(-10px);
  box-shadow: 
    0 20px 60px rgba(214, 64, 85, 0.15),
    0 0 40px rgba(214, 64, 85, 0.08);
}

/* 卡片内部装饰 */
.card::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -50%;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, rgba(214, 64, 85, 0.08) 0%, transparent 70%);
  opacity: 0;
  transition: opacity 0.4s;
}

.card:hover::before {
  opacity: 1;
}

/* 卡片头部 */
.card-header {
  position: relative;
  margin-bottom: 24px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.icon-wrap {
  position: relative;
  width: 70px;
  height: 70px;
  border-radius: 12px;
  background: linear-gradient(135deg, rgba(214, 64, 85, 0.15), rgba(245, 167, 184, 0.1));
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid rgba(214, 64, 85, 0.2);
  transition: all 0.4s;
}

.card:hover .icon-wrap {
  border-color: rgba(214, 64, 85, 0.4);
  background: linear-gradient(135deg, rgba(214, 64, 85, 0.25), rgba(245, 167, 184, 0.2));
}

.icon {
  font-size: 32px;
  position: relative;
  z-index: 2;
}

.icon-glow {
  position: absolute;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, rgba(214, 64, 85, 0.2) 0%, transparent 70%);
  border-radius: 12px;
  opacity: 0;
  transition: opacity 0.4s;
}

.card:hover .icon-glow {
  opacity: 1;
  animation: pulse-glow 0.6s ease-in-out infinite;
}

@keyframes pulse-glow {
  0%, 100% { transform: scale(1); opacity: 0.3; }
  50% { transform: scale(1.2); opacity: 0.15; }
}

.card-badge {
  padding: 6px 12px;
  background: linear-gradient(135deg, #d64055, #eb5c7d);
  border-radius: 6px;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 1px;
  box-shadow: 0 4px 15px rgba(214, 64, 85, 0.2);
}

.card h3 {
  margin: 16px 0 12px;
  font-size: 22px;
  color: #d64055;
  font-weight: 700;
  letter-spacing: 0.5px;
}

.card-desc {
  color: #8b7a81;
  font-size: 14px;
  margin-bottom: 20px;
  line-height: 1.6;
  min-height: 42px;
}

.feature-list {
  list-style: none;
  padding: 0;
  margin: 20px 0 28px;
  border-top: 1px solid rgba(214, 64, 85, 0.15);
  border-bottom: 1px solid rgba(214, 64, 85, 0.15);
  padding: 16px 0;
}

.feature-list li {
  color: #9d7e86;
  font-size: 13px;
  padding: 6px 0;
  transition: all 0.3s;
}

.feature-list li:hover {
  color: #d64055;
  padding-left: 8px;
}

/* 按钮 */
.btn {
  display: inline-block;
  padding: 12px 24px;
  border: 2px solid transparent;
  border-radius: 8px;
  text-decoration: none;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  letter-spacing: 0.5px;
  position: relative;
  overflow: hidden;
  width: 100%;
  text-align: center;
}

.btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: inherit;
  transition: left 0.3s;
  z-index: -1;
}

.btn-primary {
  background: linear-gradient(135deg, #d64055, #eb5c7d);
  color: #fff;
  box-shadow: 0 4px 20px rgba(214, 64, 85, 0.3);
  border-color: rgba(214, 64, 85, 0.4);
}

.btn-primary:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 30px rgba(214, 64, 85, 0.5);
  border-color: rgba(214, 64, 85, 0.6);
}

.btn-secondary {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.8), rgba(255, 250, 252, 0.9));
  color: #d64055;
  box-shadow: 0 4px 20px rgba(214, 64, 85, 0.15);
  border-color: rgba(214, 64, 85, 0.3);
}

.btn-secondary:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 30px rgba(214, 64, 85, 0.25);
  border-color: rgba(214, 64, 85, 0.5);
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.95), #fff);
}

/* ========== 底部区域 ========== */
.foot {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  border-top: 1px solid rgba(214, 64, 85, 0.2);
  padding: 40px 20px 20px;
  margin-top: 60px;
  animation: slide-up 0.8s ease-out 0.6s forwards;
  opacity: 0;
}

@keyframes slide-up {
  to { opacity: 1; transform: translateY(0); }
  from { opacity: 0; transform: translateY(30px); }
}

.foot-top {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 40px;
  color: #8b7a81;
  margin-bottom: 30px;
}

.col {
  text-align: center;
}

.ft-title {
  font-weight: 700;
  margin-bottom: 12px;
  color: #d64055;
  font-size: 14px;
  letter-spacing: 0.5px;
}

.ft-links {
  color: #9d7e86;
  font-size: 13px;
  line-height: 1.8;
}

.link {
  color: #d64055;
  cursor: pointer;
  text-decoration: none;
  transition: all 0.3s;
  font-weight: 500;
}

.link:hover {
  color: #eb5c7d;
  text-decoration: underline;
  text-shadow: 0 0 10px rgba(214, 64, 85, 0.3);
}

.sep {
  margin: 0 8px;
  color: #d4b5c8;
}

.foot-bottom {
  text-align: center;
  color: #b8a5b3;
  font-size: 12px;
  padding: 20px 0 0;
  border-top: 1px solid rgba(214, 64, 85, 0.15);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  letter-spacing: 0.5px;
}

.pulse-dot {
  width: 6px;
  height: 6px;
  background: #d64055;
  border-radius: 50%;
  animation: pulse 1.5s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.3; }
}

/* ========== Modal 样式 ========== */
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
  animation: fade-in-modal 0.3s ease-out;
}

@keyframes fade-in-modal {
  from { 
    opacity: 0;
    backdrop-filter: blur(0);
  }
  to { 
    opacity: 1;
    backdrop-filter: blur(4px);
  }
}

.modal-content {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.98), rgba(255, 250, 252, 0.95));
  border: 1px solid rgba(214, 64, 85, 0.2);
  border-radius: 12px;
  width: 90%;
  max-width: 600px;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(214, 64, 85, 0.15);
  animation: slide-down-modal 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}

@keyframes slide-down-modal {
  from {
    opacity: 0;
    transform: translateY(-30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid rgba(214, 64, 85, 0.15);
}

.modal-header h2 {
  margin: 0;
  color: #d64055;
  font-size: 20px;
  font-weight: 700;
}

.close {
  background: none;
  border: none;
  font-size: 32px;
  cursor: pointer;
  color: #d4b5c8;
  transition: all 0.3s;
  padding: 0;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close:hover {
  color: #d64055;
  transform: rotate(90deg);
}

.modal-body {
  padding: 24px;
  line-height: 1.8;
  color: #6b5b60;
}

.modal-body p {
  margin-bottom: 16px;
  font-size: 14px;
}

.modal-body strong {
  color: #d64055;
  font-weight: 600;
}

/* ========== 响应式设计 ========== */
@media (max-width: 768px) {
  .top {
    margin-bottom: 40px;
  }

  .title {
    font-size: 40px;
  }

  .subtitle {
    font-size: 14px;
  }

  .lead {
    font-size: 13px;
  }

  .choices {
    gap: 20px;
  }

  .card {
    padding: 24px 20px;
  }

  .card h3 {
    font-size: 18px;
  }

  .stats-bar {
    flex-direction: column;
    gap: 20px;
  }

  .stat-divider {
    width: 100%;
    height: 1px;
  }

  .foot-top {
    gap: 20px;
  }

  .orb {
    filter: blur(40px);
  }

  .background-grid {
    background-size: 30px 30px;
  }
}

@media (max-width: 480px) {
  .container {
    padding: 40px 12px;
  }

  .title {
    font-size: 32px;
  }

  .brand-wrapper {
    padding: 20px 0;
  }

  .card {
    max-width: 100%;
    padding: 20px 16px;
  }

  .btn {
    padding: 10px 16px;
    font-size: 13px;
  }

  .stats-bar {
    padding: 20px 0 40px;
  }

  .stat-number {
    font-size: 24px;
  }

  .stat-label {
    font-size: 11px;
  }

  .foot-top {
    grid-template-columns: 1fr;
    gap: 16px;
  }
}
</style>

