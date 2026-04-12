<template>
  <div class="markdown-viewer" v-html="renderedMarkdown"></div>
</template>

<script>
import { marked } from 'marked'

export default {
  name: "MarkdownViewer",
  props: {
    value: {
      type: String,
      default: ""
    }
  },
  computed: {
    renderedMarkdown() {
      return marked(this.value, {
        breaks: true,
        gfm: true,
        headerIds: true,
        mangle: false,
        sanitize: false
      })
    }
  }
}
</script>

<style scoped>
.markdown-viewer {
  font-size: 14px;
  line-height: 1.6;
  color: #333;
}

.markdown-viewer h1,
.markdown-viewer h2,
.markdown-viewer h3,
.markdown-viewer h4,
.markdown-viewer h5,
.markdown-viewer h6 {
  margin-top: 24px;
  margin-bottom: 16px;
  font-weight: 600;
  line-height: 1.25;
}

.markdown-viewer h1 {
  font-size: 2em;
  padding-bottom: 0.3em;
  border-bottom: 1px solid #eaecef;
}

.markdown-viewer h2 {
  font-size: 1.5em;
  padding-bottom: 0.3em;
  border-bottom: 1px solid #eaecef;
}

.markdown-viewer h3 {
  font-size: 1.25em;
}

.markdown-viewer h4 {
  font-size: 1em;
}

.markdown-viewer h5 {
  font-size: 0.875em;
}

.markdown-viewer h6 {
  font-size: 0.85em;
  color: #6a737d;
}

.markdown-viewer p {
  margin-top: 0;
  margin-bottom: 16px;
}

.markdown-viewer ul,
.markdown-viewer ol {
  margin-top: 0;
  margin-bottom: 16px;
  padding-left: 2em;
}

.markdown-viewer li {
  margin-top: 0.25em;
}

.markdown-viewer code {
  padding: 0.2em 0.4em;
  font-size: 85%;
  background-color: rgba(27, 31, 35, 0.05);
  border-radius: 3px;
  font-family: SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
}

.markdown-viewer pre {
  padding: 16px;
  overflow: auto;
  font-size: 85%;
  line-height: 1.45;
  background-color: #f6f8fa;
  border-radius: 3px;
  font-family: SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
}

.markdown-viewer pre code {
  padding: 0;
  font-size: 100%;
  background-color: transparent;
  border: 0;
}

.markdown-viewer blockquote {
  padding: 0 1em;
  color: #6a737d;
  border-left: 0.25em solid #dfe2e5;
  margin: 0 0 16px 0;
}

.markdown-viewer table {
  display: block;
  width: 100%;
  overflow: auto;
  margin-bottom: 16px;
  border-spacing: 0;
  border-collapse: collapse;
}

.markdown-viewer table th {
  font-weight: 600;
  padding: 6px 13px;
  border: 1px solid #dfe2e5;
  background-color: #f6f8fa;
}

.markdown-viewer table td {
  padding: 6px 13px;
  border: 1px solid #dfe2e5;
}

.markdown-viewer table tr {
  background-color: #fff;
  border-top: 1px solid #c6cbd1;
}

.markdown-viewer table tr:nth-child(2n) {
  background-color: #f6f8fa;
}

.markdown-viewer a {
  color: #0366d6;
  text-decoration: none;
}

.markdown-viewer a:hover {
  text-decoration: underline;
}
</style>