# FastBoot UI

Vue 3 + Vite + TypeScript + Element Plus + Pinia 管理端。

## 设计令牌

- 背景 `#F4F6F8`
- 主色 `#22C55E`
- 辅色 `#0EA5E9`
- 圆角 `16px`

## 开发

```bash
npm install
npm run dev
```

后端默认 `http://localhost:8080`，可在 `.env.development` 调整 `VITE_API_PROXY_TARGET`。

## 构建

```bash
npm run build
```

## Docker

```bash
docker build -t fastboot/ui:local .
```
