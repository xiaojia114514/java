<template>
  <el-main class="product-list-container">
    <!-- 滚动条组件包裹产品列表 -->
    <el-scrollbar class="product-list-scrollbar">
      <div class="product-list">
        <el-card v-for="product in products" :key="product.gid" class="product-card">
          <!-- 轮播图组件 -->
          <el-carousel :interval="5000" arrow="always" height="200px">
            <el-carousel-item v-for="(pic, index) in product.pictures" :key="index">
              <img :src="pic" class="product-image" />
            </el-carousel-item>
          </el-carousel>
          <div class="product-info">
            <h3>{{ product.gname }}</h3>
            <p>
              {{ $t(isTranslated ? 'product.price_en' : 'product.price_zh') }}:
              {{ product.gvalue }}
              {{ $t(isTranslated ? 'product.unit_en' : 'product.unit_zh') }}
              {{ $t(isTranslated ? 'product.stock_en' : 'product.stock_zh') }}:
              {{ product.gstock }}
            </p>
          </div>
          <!-- 上架按钮 -->
          <el-button type="success" @click="shelveProduct(product)">
            {{ $t(isTranslated ? 'product.reshelf_en' : 'product.reshelf_zh') }}
          </el-button>
        </el-card>
      </div>
    </el-scrollbar>
  </el-main>
</template>

<script>
import { defineComponent, ref, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import { ElMessage } from 'element-plus';
import {jwtDecode} from "jwt-decode";
import http from "@/utils/re.js";
export default defineComponent({
  props: {
    isTranslated: {
      type: Boolean,
      default: false,
    },
  },
  setup() {
    const { t } = useI18n();
    return { t };
  },
  data() {
    const token = localStorage.getItem('token');
    const claims = jwtDecode(token);
    return {
      products: [],
      token,  // 作为组件数据存储
      claims  // 作为组件数据存储
    };
  },
  created() {
    this.fetchProducts();
  },
  methods: {
    async fetchProductPictures(gid) {
      try {
        const picMap = { gid: gid };
        const response = await http.post('/pic/list_pic', picMap, {
          headers: {
            'Authorization': `${this.token}`,
          }
        });
        // 遍历返回的Map对象并获取Base64编码图片数据
        return Object.entries(response.data).map(([picId, base64Data]) => {
          return `data:image/jpeg;base64,${base64Data}`;
        });
      } catch (error) {
        console.error('获取商品图片失败:', error);
        ElMessage.error(
          this.t(
            this.isTranslated
              ? "order.messages.fetchProductsFailed_en"
              : "order.messages.fetchProductsFailed_zh"
          )
        );
        return [];
      }
    },

    async fetchProducts() {
      try {
        const data = {
          gseller: this.claims.account,  // 使用当前账户
          gshelf: 2  // 直接请求下架商品
        };
        const response = await http.post('/goods/list', data, {
          headers: {
            'Authorization': `${this.token}`,
          }
        });
        console.log("后端返回的商品数据：", response.data);

        // 移除前端筛选逻辑，直接处理接口返回的已筛选数据
        this.products = await Promise.all(response.data.map(async product => {
          const pictures = await this.fetchProductPictures(product.gid);
          return {...product, pictures};
        }));
      } catch (error) {
        console.error('获取商品信息失败:', error);
        ElMessage.error(
          this.t(
            this.isTranslated
              ? "order.messages.fetchProductsFailed_en"
              : "order.messages.fetchProductsFailed_zh"
          )
        );
      }
    },

    // 上架商品的函数
    async shelveProduct(product) {
      try {
        const updateMap = {
          gid: product.gid,
          shelf: 1
        };
        const response = await http.post('/goods/update_Gshelf', updateMap, {
          headers: {
            'Authorization': `${this.token}`,
          }
        });
        if (response.data) {
          ElMessage.success(
            this.t(
              this.isTranslated
                ? "order.messages.updateSuccess_en"
                : "order.messages.updateSuccess_zh"
            )
          );
          await this.fetchProducts();
        } else {
          ElMessage.error(
            this.t(
              this.isTranslated
                ? "order.messages.updateFailed_en"
                : "order.messages.updateFailed_zh"
            )
          );
        }
      } catch (error) {
        console.error('商品上架失败:', error);
        ElMessage.error(
          this.t(
            this.isTranslated
              ? "order.messages.updateFailed_en"
              : "order.messages.updateFailed_zh"
          )
        );
      }
    }
  }
});
</script>

<style scoped>
.product-list-container {
  height: calc(100vh - 60px); /* 设置固定高度，减去顶部导航栏的高度 */
}

.product-list-scrollbar {
  height: 100%; /* 设置滚动条容器高度为100% */
  overflow-y: auto; /* 启用垂直滚动 */
}

.product-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}
.product-card {
  width: calc(33.333% - 20px); /* 一行三个 */
}
.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}
</style>
