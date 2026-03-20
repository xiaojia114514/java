<template>
  <div style="height: 100%; display: flex;">
    <ShopLogo style="background-color: #ffffff"></ShopLogo>
    <el-header style="width: 82%;text-align: right; font-size: 12px; display: flex; align-items: center; height: 80px; background-color: var(--el-color-primary-light-9);">
      <admin-head :is-translated="isTranslated"></admin-head>
    </el-header>
  </div>
  <el-main>
    <div v-if="pro" class="edit-product-info">
      <el-form :model="pro">
        <el-form-item :label="$t(!isTranslated ? 'product.name_zh' : 'product.name_en')">
          <el-input v-model="pro.gname"></el-input>
        </el-form-item>
        <!-- 商品图片展示和上传 -->
        <el-form-item :label="$t(!isTranslated ? 'translate.favorites.previewImage_zh' : 'translate.favorites.previewImage_en')">
          <div v-if="pro.pictures" class="image-list">
            <div v-for="(picObj, index) in pro.pictures" :key="index" class="image-item">
              <img :src="picObj.url" class="product-image" />
              <el-button type="text"  @click="handleRemovePic(picObj.id, index)">
                <el-icon size="20"><Close /></el-icon>
              </el-button>
            </div>
          </div>
          <el-upload
            class="image-uploader"
            action="/pic/add"
            :on-success="handleSuccess"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :before-upload="beforeUpload"
            :headers="headers"
          >
            <div class="icon-container">
              <el-icon><Plus /></el-icon>
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item :label="$t(!isTranslated ? 'product.price_zh' : 'product.price_en')">
          <el-input type="number" v-model="pro.gvalue"></el-input>
        </el-form-item>
        <el-form-item :label="$t(!isTranslated ? 'product.stock_zh' : 'product.stock_en')">
          <el-input type="number" v-model="pro.gstock"></el-input>
        </el-form-item>

        <upRichText :content="pro.gdesc" :onContentChange="updateDescription"></upRichText>

        <el-form-item>
          <el-button type="primary" @click="submitProductInfo">
            {{ $t(isTranslated ? 'order.actions.submit_en' : 'order.actions.submit_zh') }}
          </el-button>
          <el-button type="primary" @click="goBack">
            {{ $t(isTranslated ? 'order.actions.back_en' : 'order.actions.back_zh') }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-main>
  <TranslateButton
    v-model:isTranslated="isTranslated"
    class="floating-translate-btn"
  />
</template>

<script lang="ts" setup>
import {Close, Plus} from "@element-plus/icons-vue";
import {onMounted, ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {ElMessage, ElMessageBox} from 'element-plus';
import axios from 'axios';
import ShopLogo from "@/components/block/Shop-Logo.vue";
import AdminHead from "@/components/block-admin/adminHead.vue";
import upRichText from "@/components/block-rich/UpdateRichText.vue";
import http from "@/utils/re.js";
import TranslateButton from "@/components/block/translate.vue";
import { useI18n } from 'vue-i18n';

const { t: $t } = useI18n();

defineProps({
  content: String
});

const token = localStorage.getItem('token');
const { t } = useI18n();
const route = useRoute();
const router = useRouter();
const gid = Number(route.params.pid);
const pro = ref();
const newDesc = ref('');
const isTranslated = ref(false);
const deletedPicIds = ref<number[]>([]); // 待删除的图片ID
const addedPictures = ref<Array<{ tempId: number; data: string }>>([]);  // 待增加的图片
const updateDescription = (content) => {
  newDesc.value = content; // 更新 newDesc 的值
  pro.value.desc = content; // 同时更新 pro 的 desc 属性
};

// 图片上传相关
const headers = {
  'Authorization': token
};

const handleSuccess = (response, file, fileList) => {
  console.log('上传成功:', response);
  pro.value.pictures = [...pro.value.pictures, `data:image/jpeg;base64,${response}`];
};

const handlePreview = (file) => {
  console.log('预览文件:', file);
};

const handleRemove = (file, fileList) => {
  console.log('移除文件:', file);
};

const beforeUpload = (file) => {
  return new Promise((resolve) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      const base64Data = reader.result?.split(',')[1];
      const tempId = -Date.now(); // 生成临时ID（负数表示未保存）

      // 添加到待添加列表和显示列表
      addedPictures.value.push({
        tempId,
        data: base64Data
      });

      pro.value.pictures.push({
        id: tempId,
        url: `data:image/jpeg;base64,${base64Data}`
      });
      ElMessage.success($t(!isTranslated ? 'order.messages.addSuccess_en' : 'order.messages.addSuccess_zh'));
      resolve(false); // 阻止自动上传
    };
  });
};

// 删除图片
const handleRemovePic = (picId, index) => {
  ElMessageBox.confirm(
    t(isTranslated.value ? 'product.deleteConfirm_en' : 'product.deleteConfirm_zh'),
    t(isTranslated.value ? 'order.messages.confirmTitle_en' : 'order.messages.confirmTitle_zh'), {
      confirmButtonText: t(isTranslated.value ? 'order.actions.confirm_en' : 'order.actions.confirm_zh'),
      cancelButtonText: t(isTranslated.value ? 'order.actions.cancel_en' : 'order.actions.cancel_zh'),
      type: 'warning'
    }).then(() => {
    deletedPicIds.value.push(picId); // 添加到待删除列表
    pro.value.pictures.splice(index, 1); // 从显示中移除
    ElMessage.success($t(isTranslated.value ? 'order.messages.deleteSuccess_en' : 'order.messages.deleteSuccess_zh'));
  }).catch(() => {});
};

// 从后端获取商品信息
const fetchProductData = async (gid) => {
  try {
    const data = {
      gid: gid
    };
    const response = await http.post(`/goods/list`, data,{
      headers: {
        'Authorization': `${token}`,
      }
    });
    if (response.data.length > 0) {
      const fetchedProduct = response.data[0];
      const pictures = await fetchProductPictures(fetchedProduct.gid);
      pro.value = { ...fetchedProduct, pictures };
    }
  } catch (error) {
    console.error($t(!isTranslated ? 'order.messages.fetchProductsFailed_en' : 'order.messages.fetchProductsFailed_zh'), error);
  }
};

const fetchProductPictures = async (gid) => {
  try {
    const response = await http.post('/pic/list_pic', { gid }, {
      headers: {
        'Authorization': `${token}`,
      }
    });
    console.log("返回：",response.data);
    // 遍历返回的Map对象并获取Base64编码图片数据
    return Object.entries(response.data).map(([picId, base64Data]) => {
      return {id: picId, url: `data:image/jpeg;base64,${base64Data}`};
    });
  } catch (error) {
    console.error($t(!isTranslated ? 'order.messages.fetchFailed_en' : 'order.messages.fetchFailed_zh'), error);
    return [];
  }
};

// 提交商品信息修改
const submitProductInfo = async () => {
  try {
    // 提交商品基本信息
    const product = {
      gid: gid,
      name: pro.value.gname,
      value: parseInt(pro.value.gvalue),
      stock: parseInt(pro.value.gstock),
      cid: pro.value.cid,
      desc: newDesc.value || pro.value.gdesc
    };

    // 先更新商品基本信息
    const infoResponse = await http.post(
      '/goods/update_Ginfo',
      product,
      { headers: { 'Authorization': token } }
    );

    if (infoResponse.data) {
      // 处理删除的图片
      if (deletedPicIds.value.length > 0) {
        await Promise.all(
          deletedPicIds.value.map(picId =>
            http.delete(`/pic/delete/${picId}`, {
              headers: { 'Authorization': token }
            })
          )
        );
        deletedPicIds.value = [];
      }

      // 处理新增的图片
      if (addedPictures.value.length > 0) {
        const uploadPromises = addedPictures.value.map(async (pic) => {
          const response = await http.post(
            '/pic/create',
            { gid, data: pic.data },
            { headers: { 'Authorization': token } }
          );

          // 更新图片ID映射
          const index = pro.value.pictures.findIndex(p => p.id === pic.tempId);
          if (index !== -1) {
            pro.value.pictures[index].id = response.data; // 假设返回新图片ID
          }
        });

        await Promise.all(uploadPromises);
        addedPictures.value = [];
      }

      ElMessage.success($t(!isTranslated ? 'order.messages.updateSuccess_en' : 'order.messages.updateSuccess_zh'));
      await router.push({name: 'AProductDet', params: {pid: gid}});
    }
  } catch (error) {
    console.error($t(!isTranslated ? 'order.messages.updateFailed_en' : 'order.messages.updateFailed_zh'), error);
    ElMessage.error($t(!isTranslated ? 'order.messages.updateFailed_en' : 'order.messages.updateFailed_zh') + ': ' + (error.response?.data?.message || error.message));
  }
};

// 在组件挂载时获取商品信息
onMounted(() => {
  const gid = Number(route.params.pid);
  console.log("得到gid：",gid);
  fetchProductData(gid);
});

const goBack = () => {
  history.back();
};
</script>

<style scoped>
.edit-product-info {
  max-width: 600px;
  margin: 20px auto;
}

.image-list {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 10px;
}

.image-item {
  display: inline-block;
  margin-right: 10px;
  position: relative;
}

.product-image {
  width: 50px; /* 设置与添加方框等大的宽度 */
  height: 50px; /* 设置与添加方框等大的高度 */
  object-fit: cover;
  border: 1px solid #ccc;
}

.image-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 50px; /* 设置与商品图片等大的宽度 */
  height: 50px; /* 设置与商品图片等大的高度 */
  line-height: 50px; /* 保持图标垂直居中 */
  text-align: center;
  margin-bottom: 10px; /* 添加底部外边距 */
}
.image-uploader-content {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 50px; /* 设置与商品图片等大的宽度 */
  height: 50px; /* 设置与商品图片等大的高度 */
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  overflow: hidden;
  line-height: 50px; /* 保持图标垂直居中 */
  text-align: center;
  margin-bottom: 10px; /* 添加底部外边距 */
}

.image-uploader-content .el-icon {
  font-size: 20px; /* 修改图标大小以符合添加按钮大小 */
  color: #000; /* 修改图标颜色为黑色 */
}
.image-uploader .el-icon-plus {
  font-size: 20px; /* 修改图标大小以符合添加按钮大小 */
  color: #ca0909; /* 修改图标颜色为黑色 */
}

.el-button.el-button--text {
  border: none;
  background: none;
  padding: 0;
  position: absolute;
  bottom: 0; /* 调整为底部对齐 */
  right: 0; /* 调整为右侧对齐 */
  border-radius: 50%;
  width: 20px;
  height: 20px;
  line-height: 20px;
  color: #fff;
  background-color: #f56c6c; /* 保持红色背景 */
  cursor: pointer;
  text-align: center; /* 确保文本居中 */
}

.el-button.el-button--text .el-icon {
  margin: 0; /* 移除图标的默认外边距 */
}
</style>
