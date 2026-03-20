<template>
  <el-card class="trade-info-change-card">
    <template v-slot:header>
      <div>{{ $t(isTranslated ? 'order.deliveryManagementTitle_en' : 'order.deliveryManagementTitle_zh') }}</div>
      <div class="account-info">
        {{ $t(isTranslated ? 'order.currentAccount_en' : 'order.currentAccount_zh') }}{{ account }}
      </div>
    </template>
    <el-form ref="Form" :model="Form" label-width="150px">
      <el-form-item :label="$t(isTranslated ? 'login.password_en' : 'login.password_zh')"
                    :rules="[{ required: true, message: $t(isTranslated ? 'order.messages.passwordRequired_en' : 'order.messages.passwordRequired_zh'), trigger: 'blur' }]">
        <el-input type="password" v-model="Form.pwd" autocomplete="off" required></el-input>
      </el-form-item>
      <el-form-item :label="$t(isTranslated ? 'login.phone_en' : 'login.phone_zh')"
                    :rules="[{ required: true, message: $t(isTranslated ? 'order.messages.phoneRequired_en' : 'order.messages.phoneRequired_zh'), trigger: 'blur' }]">
        <el-input type="text" v-model="Form.phone" autocomplete="phone" required></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitPasswordCheck">
          {{ $t(isTranslated ? 'order.actions.submit_en' : 'order.actions.submit_zh') }}
        </el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import { ElMessage } from 'element-plus';
import { defineComponent } from 'vue';
import { jwtDecode } from "jwt-decode";
import http from "@/utils/re.js";

export default defineComponent({
  props: {
    isTranslated: {
      type: Boolean,
      default: false
    }
  },
  created() {
    const token = localStorage.getItem('token');
    if (token) {
      const claims = jwtDecode(token);
      this.account = claims.account;
      this.password = claims.password;
      this.Form.phone = claims.phone;
    } else {
      this.$router.push({ path: '/'});
    }
  },
  data() {
    return {
      account: '',
      password:'',
      Form: {
        pwd:'',
        phone:''
      }
    };
  },
  methods: {
    async submitPasswordCheck() {
      if (this.Form.pwd !== this.password) {
        ElMessage.error(this.isTranslated ? 'Wrong password' : '密码错误');
        return;
      }
      const formData = {
        uaccount: this.account,
        uphone: this.Form.phone
      };

      try {
        const token = localStorage.getItem('token');
        const response = await http.post('/user/update', formData, {
          headers: {
            'Authorization': `${token}`,
          }
        });
        if (response.data) {
          ElMessage.success(this.isTranslated ? 'Update successful' : '修改成功');
        } else {
          ElMessage.error(this.isTranslated ? 'Update failed' : '修改失败');
        }
      } catch (error) {
        console.error('Error during update:', error);
        ElMessage.error(this.isTranslated ? 'Error occurred during update' : '修改过程中出现错误');
      }
    }
  }
});
</script>
<style scoped>
.trade-info-change-card {
  max-width: 400px;
  margin: 20px auto;
}

</style>
