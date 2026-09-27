import { defineConfig } from 'vitepress'

export default defineConfig({
  title: 'FastBoot',
  description: 'Spring Boot 3 DDD 脚手架文档',
  themeConfig: {
    nav: [
      { text: '首页', link: '/' },
      { text: '快速开始', link: '/getting-started' },
      { text: '架构', link: '/architecture' },
      { text: '对比', link: '/compare' },
    ],
    sidebar: [
      {
        text: '指南',
        items: [
          { text: '快速开始', link: '/getting-started' },
          { text: '架构摘要', link: '/architecture' },
          { text: '对比', link: '/compare' },
        ],
      },
    ],
  },
})
