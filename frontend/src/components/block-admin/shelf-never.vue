<template>
  <el-main class="product-list-container">
    <!-- Scroller component wrapping product list -->
    <el-scrollbar class="product-list-scrollbar">
      <div class="product-list">
        <el-card v-for="product in products" :key="product.gid" class="product-card">
          <!-- Carousel component -->
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
          <!-- Shelve button -->
          <el-button type="success" @click="shelveProduct(product)">
            {{ $t(isTranslated ? 'product.onshelf_en' : 'product.onshelf_zh') }}
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
        const picArray = Object.entries(response.data).map(([picId, base64Data]) => {
          return `data:image/jpeg;base64,${base64Data}`;
        });
        return picArray;
      } catch (error) {
        console.error(this.t(
          this.isTranslated
            ? 'order.messages.fetchProductsFailed_en'
            : 'order.messages.fetchProductsFailed_zh'
        ), error);
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
          gseller: this.claims.account,
          gshelf: 0
        };
        const response = await http.post('/goods/list', data, {
          headers: {
            'Authorization': `${this.token}`,
          }
        });

        this.products = await Promise.all(response.data.map(async product => {
          const pictures = await this.fetchProductPictures(product.gid);
          return {...product, pictures};
        }));
      } catch (error) {
        console.error(this.t(
          this.isTranslated
            ? 'order.messages.fetchProductsFailed_en'
            : 'order.messages.fetchProductsFailed_zh'
        ), error);
        ElMessage.error(
          this.t(
            this.isTranslated
              ? "order.messages.fetchProductsFailed_en"
              : "order.messages.fetchProductsFailed_zh"
          )
        );
      }
    },

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
                ? "order.messages.updateFailednoPic_en"
                : "order.messages.updateFailednoPic_zh"
            )
          );
        }
      } catch (error) {
        console.error(this.t(
          this.isTranslated
            ? 'order.messages.updateFailed_en'
            : 'order.messages.updateFailed_zh'
        ), error);
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
  height: calc(100vh - 60px);
}

.product-list-scrollbar {
  height: 100%;
  overflow-y: auto;
}

.product-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}
.product-card {
  width: calc(33.333% - 20px);
}
.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}
</style>
