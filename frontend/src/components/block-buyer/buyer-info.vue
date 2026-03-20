<template>
  <div>
    <!-- 显示加载中的提示 -->
    <div v-if="loading" class="loading">
      {{ $t(isTranslated ? 'order.messages.loading_en' : 'order.messages.loading_zh') }}
    </div>
    <!-- 错误提示信息 -->
    <div v-else-if="error" class="error">{{ error }}</div>
    <!-- 用户信息表单 -->
    <el-form :model="userInfo" label-width="120px" v-else>
      <!-- 用户名字段：显示不可编辑 -->
      <el-form-item :label="$t(isTranslated ? 'translate.buyer.account_en' : 'translate.buyer.account_zh')">
        <el-input v-model="this.account" :disabled="true" style="width: 300px;"></el-input>
      </el-form-item>
      <!-- 电话字段：不可编辑 -->
      <el-form-item :label="$t(isTranslated ? 'translate.buyer.phone_en' : 'translate.buyer.phone_zh')">
        <el-input v-model="this.phone" :disabled="true" style="width: 300px;"></el-input>
      </el-form-item>
      <!-- 地址字段：不可编辑 -->
      <el-form-item :label="$t(isTranslated ? 'translate.buyer.address_en' : 'translate.buyer.address_zh')">
        <el-input v-model="this.default_address" :disabled="true" style="width: 300px;"></el-input>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { jwtDecode } from "jwt-decode";
import http from "@/utils/re.js";

export default {
  props: {
    isTranslated: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      account: '',
      phone: '',
      default_address: '',
      loading: false,
      error: null,
    };
  },
  created() {
    const token = localStorage.getItem('token');
    if (token) {
      const claims = jwtDecode(token);
      this.account = claims.account;
      this.phone = claims.phone;
      this.fetchDefaultAddress(this.account);
    } else {
      this.$router.push({ path: '/' });
    }
  },
  methods: {
    async fetchDefaultAddress(account) {
      this.loading = true;
      try {
        const token = localStorage.getItem('token');
        const response = await http.post(
          "/address/list",
          { uaccount: account, adefault: 1 },
          { headers: { Authorization: `${token}` } }
        );

        if (response.data?.length > 0) {
          this.default_address = response.data[0].atext;
        } else {
          this.default_address = this.isTranslated
            ? 'No default address set'
            : '未设置默认地址';
        }
      } catch (error) {
        // 使用语言文件中已有的错误消息
        this.error = this.isTranslated
          ? this.$t('order.messages.error_en')
          : this.$t('order.messages.error_zh');
        console.error("获取地址失败:", error);
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>

<style scoped>
.el-form {
  margin-top: 20px;
}
.loading, .error {
  text-align: center;
  padding: 20px;
  font-size: 1.2em;
}
.error {
  color: red;
}
</style>
