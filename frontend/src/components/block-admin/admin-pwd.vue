<template>
  <el-card class="password-change-card">
    <template v-slot:header>
      <div>{{ $t(isTranslated ? 'login.changePassword_en' : 'login.changePassword_zh') }}</div>
      <div class="account-info">
        {{ $t(isTranslated ? 'order.currentAccount_en' : 'order.currentAccount_zh') }}
        {{ account }}
      </div>
    </template>
    <el-form
      ref="passwordForm"
      :model="passwordForm"
      label-width="150px"
      @submit.native.prevent="submitPasswordChange"
    >
      <el-form-item
        :label="$t(isTranslated ? 'login.oldPassword_en' : 'login.oldPassword_zh')"
        :rules="[{ required: true, message: $t(isTranslated ? 'order.messages.passwordRequired_en' : 'order.messages.passwordRequired_zh'), trigger: 'blur' }]"
      >
        <el-input
          type="password"
          v-model="passwordForm.old_pwd"
          autocomplete="off"
          required
        ></el-input>
      </el-form-item>
      <el-form-item
        :label="$t(isTranslated ? 'login.newPassword_en' : 'login.newPassword_zh')"
        :rules="[{ required: true, message: $t(isTranslated ? 'order.messages.newPasswordRequired_en' : 'order.messages.newPasswordRequired_zh'), trigger: 'blur' }]"
      >
        <el-input
          type="password"
          v-model="passwordForm.new_pwd"
          autocomplete="new-password"
          required
        ></el-input>
      </el-form-item>
      <el-form-item
        :label="$t(isTranslated ? 'login.confirmPassword_en' : 'login.confirmPassword_zh')"
        :rules="[{ required: true, message: $t(isTranslated ? 'order.messages.confirmPasswordRequired_en' : 'order.messages.confirmPasswordRequired_zh'), trigger: 'blur' }]"
      >
        <el-input
          type="password"
          v-model="passwordForm.confirmPassword"
          autocomplete="new-password"
          required
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitPasswordChange">
          {{ $t(isTranslated ? 'order.actions.submit_en' : 'order.actions.submit_zh') }}
        </el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import {defineComponent, ref} from 'vue';
import {useI18n} from 'vue-i18n';
import {ElMessage} from 'element-plus';
import {jwtDecode} from "jwt-decode";
import http from "@/utils/re.js";

export default defineComponent({
  props: {
    isTranslated: {
      type: Boolean,
      default: false
    }
  },
  setup() {
    const {t} = useI18n();
    return {t};
  },
  data() {
    const token = localStorage.getItem('token');
    const claims = jwtDecode(token);
    return {
      account: '',
      passwordForm: {
        account: '',
        old_pwd: '',
        new_pwd: '',
        confirmPassword: ''
      },
      token,  // 作为组件数据存储
      claims  // 作为组件数据存储
    };
  },
  created() {
    if (this.token) {
      this.account = this.claims.account;
      this.passwordForm.account = this.claims.account;
    } else {
      this.$router.push({ path: "/" });
    }
  },
  methods: {
    async submitPasswordChange() {
      console.log("new:", this.passwordForm.new_pwd)
      console.log("old:", this.claims.password)
      console.log("confirmPassword:", this.passwordForm.old_pwd)

      if (this.passwordForm.new_pwd !== this.passwordForm.confirmPassword) {
        console.log("1")
        ElMessage.error(
          this.t(
            this.isTranslated
              ? "order.messages.passwordMismatch_en"
              : "order.messages.passwordMismatch_zh"
          )
        );
        return;
      }else if (this.passwordForm.old_pwd !== this.claims.password){
        console.log("2")
        ElMessage.error(
          this.t(
            this.isTranslated
              ? "order.messages.passwordMismatch_en"
              : "order.messages.passwordMismatch_zh"
          )
        );
        return;
      }
      const formData = {
        uaccount: this.passwordForm.account,
        upassword: this.passwordForm.new_pwd
      };
      try {
        const response = await http.post('/user/update', formData, {
          headers: {
            'Authorization': `${localStorage.getItem('token')}`,
          }
        });

        if (response.data) {
          ElMessage.success(
            this.t(
              this.isTranslated
                ? 'order.messages.passwordChangeSuccess_en'
                : 'order.messages.passwordChangeSuccess_zh'
            )
          );
          this.$router.push('/');
        } else {
          ElMessage.error(
            this.t(
              this.isTranslated
                ? 'order.messages.passwordChangeFailed_en'
                : 'order.messages.passwordChangeFailed_zh'
            )
          );
        }
      } catch (error) {
        console.error('密码修改请求出错:', error);
        ElMessage.error(
          this.t(
            this.isTranslated
              ? 'order.messages.passwordChangeError_en'
              : 'order.messages.passwordChangeError_zh'
          )
        );
      }
    }
  }
});
</script>

<style scoped>
.password-change-card {
  max-width: 400px;
  margin: 20px auto;
}

.el-form-item {
  margin-bottom: 20px;
}
</style>
