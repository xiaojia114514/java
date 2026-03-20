module.exports = {
  src: 'src/components/**/*.vue', // 组件文件的位置
  dest: 'docs/', // 生成的文档存放的位置
  plugins: [
    // 这里可以添加插件，例如：require.resolve('vue-docgen-api/tools/webpack-loader')
  ],
  docgenOptions: {
    // 这里可以添加docgen的选项
  },
  customBlocks: {
    md: {
      before: (block, doc) => {
        // 在Markdown块之前执行的代码
      },
      after: (block, doc) => {
        // 在Markdown块之后执行的代码
        return `# ${doc.displayName}\n\n${block.content}`;
      },
    },
  },
};
