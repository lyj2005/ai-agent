<template>
  <div class="home-paper">
    <div class="container">
      <header class="top">
        <h1 class="title">恋爱大师</h1>
        <p class="lead">情感咨询 · 温暖陪伴 · 聆听你的故事</p>
      </header>

      <section class="choices">
        <div class="card">
          <div class="icon-wrap"><div class="icon">👩‍⚕️</div></div>
          <h3>恋爱问诊</h3>
          <p class="muted">智能情感顾问，提供专业恋爱建议与陪伴</p>
          <router-link to="/love" class="btn">立即体验 →</router-link>
        </div>

        <div class="card">
          <div class="icon-wrap"><div class="icon">🤖</div></div>
          <h3>AI 超级智能体</h3>
          <p class="muted">多面向 AI 助手，帮助解决各类问题</p>
          <router-link to="/manus" class="btn">立即体验 →</router-link>
        </div>
      </section>

      <footer class="foot">
        <div class="foot-top">
          <div class="col">
            <div class="ft-title">AI恋爱大师应用平台</div>
            <div class="ft-links">
              <a href="#" @click.prevent="showModal='agreement'" class="link">《用户协议》</a>
              <span class="sep">·</span>
              <a href="#" @click.prevent="showModal='privacy'" class="link">《隐私政策》</a>
            </div>
          </div>
          <div class="col">
            <div class="ft-title">友情链接</div>
            <div class="ft-links">
              <a href="https://coder-live.top/" target="_blank" class="link">个人博客</a>
              <span class="sep">·</span>
              <a href="https://github.com/lyj2005" target="_blank" class="link">Github主页</a>
            </div>
          </div>
          <div class="col">
            <div class="ft-title">联系我们</div>
            <div class="ft-links">
              <span>商务合作</span>
              <span class="sep">·</span>
              <a href="https://github.com/lyj2005" target="_blank" class="link">站长：lyjames</a>
            </div>
          </div>
        </div>
        <div class="foot-bottom">© 2026 AI恋爱大师应用平台 - 温暖你的情感生活</div>
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
import { ref } from 'vue'
export default {
  setup(){
    const showModal = ref('')
    return { showModal }
  }
}
</script>
<style scoped>
/* 轻雅纸质风格主页，参考示例 */
.home-paper{background:#fff5f7;min-height:100vh;padding:48px 12px}
.container{max-width:1100px;margin:0 auto}
.top{padding:28px 0;text-align:center}
.title{font-size:40px;color:#b33a5a;margin:0;font-weight:700}
.lead{color:#7e616b;margin-top:10px}

.choices{display:flex;gap:30px;justify-content:center;padding:30px 0}
.card{width:360px;background:#ffffff;border-radius:10px;padding:34px 28px;box-shadow:0 14px 40px rgba(179,58,90,0.06);text-align:center}
.icon-wrap{width:86px;height:86px;border-radius:50%;margin:-70px auto 14px;background:linear-gradient(135deg,#ff9ab8,#ff5f87);display:flex;align-items:center;justify-content:center;box-shadow:0 10px 30px rgba(255,95,135,0.12)}
.icon{font-size:36px}
.card h3{margin:8px 0 6px;color:#7a2b43}
.muted{color:#8b6b74;font-size:14px;margin-bottom:18px}
.btn{display:inline-block;background:linear-gradient(90deg,#ff5f87,#ff9ab8);color:#fff;padding:10px 16px;border-radius:6px;text-decoration:none;box-shadow:0 12px 30px rgba(255,95,135,0.12)}

.foot{background:#f7f9fb;padding:32px 12px;margin-top:40px}
.foot-top{max-width:1100px;margin:0 auto;display:flex;justify-content:space-around;color:#21343b}
.col{text-align:center}
.ft-title{font-weight:700;margin-bottom:8px;color:#21343b;font-size:14px}
.ft-links{color:#6d7a7f;font-size:13px}
.link{color:#0066cc;cursor:pointer;text-decoration:none}
.link:hover{text-decoration:underline}
.sep{margin:0 6px;color:#ccc}
.foot-bottom{text-align:center;color:#999;font-size:12px;padding:12px 0;border-top:1px solid #e8eaed;margin-top:16px}

.modal-backdrop{position:fixed;top:0;left:0;right:0;bottom:0;background:rgba(0,0,0,0.5);display:flex;align-items:center;justify-content:center;z-index:999}
.modal-content{background:#fff;border-radius:8px;width:90%;max-width:600px;max-height:80vh;overflow:auto;box-shadow:0 20px 60px rgba(0,0,0,0.1)}
.modal-header{display:flex;justify-content:space-between;align-items:center;padding:16px 20px;border-bottom:1px solid #eee}
.modal-header h2{margin:0}
.close{background:none;border:none;font-size:28px;cursor:pointer;color:#999}
.close:hover{color:#333}
.modal-body{padding:20px;line-height:1.6;color:#333}
.modal-body p{margin-bottom:12px}
.modal-body strong{color:#21343b}

@media (max-width:880px){
  .choices{flex-direction:column;align-items:center}
  .card{width:90%}
  .foot-inner{flex-direction:column;gap:10px}
}
</style>

