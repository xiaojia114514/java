// styleguide.config.cjs
module.exports = {
  title: '管理员后台组件',
  components: 'src/components/block-admin/*.vue', // 指定组件文件夹
  ignore: ['src/components/**/Example.vue'], // 忽略不需要文档化的组件
  styleguideDir: 'styleguide-dist', // 构建输出目录
  copyCodeButton: true, // 复制代码按钮
  pagePerSection: true, // 每个章节一个页面
  usageMode: 'expand', // 展开/折叠模式
  tocMode: 'expand', // 目录展开/折叠模式
  displayOrigins: true, // 显示组件来源
};
