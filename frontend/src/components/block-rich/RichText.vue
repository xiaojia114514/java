<template>
  <div>
    <div id="toolbar">
      <span class="ql-formats">
        <button class="ql-bold" title="文字加粗">Bold</button>
        <button class="ql-italic" title="文字倾斜">Italic</button>
        <button class="ql-underline" title="添加下划线">下划线</button>
        <button class="ql-link" title="添加链接">link</button>
        <button class="ql-strike" title="添加划线"></button>
        <button class="ql-script" title="添加为下标" value="sub"></button>
        <button class="ql-script" title="添加为上标" value="super"></button>
      </span>
      <span class="ql-formats">
        <button class="ql-list" title="添加数字序号" value="ordered"></button>
        <button class="ql-list" title="添加点序号" value="bullet"></button>
        <button class="ql-blockquote" title="添加引用"></button>
        <button class="ql-code-block" title="添加代码"></button>
        <button class="ql-image" title="上传图片"></button>
      </span>
      <span class="ql-formats" title="文字颜色">
        <select class="ql-color">
          <option selected></option>
          <option value="red"></option>
          <option value="orange"></option>
          <option value="yellow"></option>
          <option value="green"></option>
          <option value="blue"></option>
          <option value="purple"></option>
          <option value="grey"></option>
        </select>
        <select class="ql-background" title="文字背景颜色">
          <option selected></option>
          <option value="black"></option>
          <option value="red"></option>
          <option value="orange"></option>
          <option value="yellow"></option>
          <option value="green"></option>
          <option value="blue"></option>
          <option value="purple"></option>
        </select>
      </span>
      <span class="ql-formats" title="调整文字位置">
        <select class="ql-align">
          <option selected="selected"></option>
          <option value="center"></option>
          <option value="right"></option>
          <option value="justify"></option>
        </select>
      </span>
      <span class="ql-formats" title="调整文字大小">
        <select class="ql-size">
          <option value="10px">小字体</option>
          <option selected>中字体</option>
          <option value="18px">大字体</option>
          <option value="32px">超大字</option>
        </select>
      </span>
      <span class="ql-formats" title="切换文字">
        <select class="ql-font" style="width:150px">
          <option selected="selected"></option>
          <option value="SimHei"></option>
          <option value="Microsoft-YaHei"></option>
          <option value="SimSun"></option>
          <option value="KaiTi"></option>
          <option value="FangSong"></option>
          <option value="Arial"></option>
          <option value="Times-New-Roman"></option>
          <option value="sans-serif"></option>
        </select>
      </span>
    </div>

    <div id="editor" ref="editor">
      <p>在此输入详细介绍。</p>
    </div>
  </div>
</template>

<script>
import { onMounted, ref } from 'vue';
import Quill from 'quill';
import 'quill/dist/quill.snow.css';

export default {
  props: {
    onContentChange: Function // 从父组件接收的回调函数
  },
  setup(props) {
    const editor = ref(null);
    let quill;

    onMounted(() => {
      // 注册背景、颜色和大小
      const BackgroundClass = Quill.import('attributors/class/background');
      const ColorClass = Quill.import('attributors/class/color');
      const SizeStyle = Quill.import('attributors/style/size');
      Quill.register(BackgroundClass, true);
      Quill.register(ColorClass, true);
      Quill.register(SizeStyle, true);

      // 初始化 Quill 编辑器
      quill = new Quill(editor.value, {
        modules: {
          toolbar: '#toolbar',
        },
        theme: 'snow',
      });

      // 监听编辑器内容的变化，并实时更新
      quill.on('text-change', () => {
        props.onContentChange(quill.root.innerHTML); // 调用父组件传递的回调函数，传递内容
      });
    });

    return {
      editor,
    };
  },
};
</script>

<style scoped>
#editor {
  height: 400px;
}

#btn {
  background-color: #4CAF50;
  width: 150px;
  color: #fff;
  border: none;
  padding: 10px;
  border-radius: 3px;
  cursor: pointer;
}
</style>
