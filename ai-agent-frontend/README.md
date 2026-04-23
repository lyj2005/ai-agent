# AI Agent Frontend

本项目为最小 Vue3 + Vite 示例，包含两个基于 SSE 的聊天应用页面：AI 恋爱大师（/ai/love_app/chat/sse）和 AI 超级智能体（/ai/manus/chat）。

运行（Windows PowerShell）：

```powershell
cd e:\code\ai-agent\ai-agent-frontend
npm install
npm run dev
```

打开浏览器访问 http://localhost:3000

说明：
- 项目使用 `EventSource` 直接消费后端 SSE 接口，`axios` 已设置默认 `baseURL` 为 `http://localhost:8123/api`。
- 若后端需要跨域，请在 Spring Boot 后端启用 CORS 或在代理中配置。
