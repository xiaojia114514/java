<template>
  <div class="login-container">
    <ShopLogo class="logo" style="width: 300px"></ShopLogo>
    <!-- Card -->
    <div class="card" style="height: 400px;width: 500px;">
      <select v-model="role" style="width: 80px;height: 40px;font-size:16px">
        <option value="user">{{ $t(isTranslated ? 'login.role_user_en' : 'login.role_user_zh' ) }}</option>
        <option value="admin">{{ $t(isTranslated ? 'login.role_admin_en' : 'login.role_admin_zh' ) }}</option>
      </select>

      <form v-if="role === 'user'">
        <input type="text" v-model="user.UAccount" :placeholder="t(isTranslated ? 'login.username_en' : 'login.username_zh' )">
        <input type="password" v-model="user.UPassword" :placeholder="t(isTranslated ? 'login.password_en' : 'login.password_zh' )">
        <button @click.prevent="login('user')" style="background-color: #83b3f3">{{ $t(isTranslated ? 'order.actions.login_en' : 'order.actions.login_zh' )  }}</button>
        <p v-if="role === 'user'" style="margin-top: 20px;color: #174e90">
          <a href="#" @click="showRegistration = true">{{ $t(isTranslated ? 'order.actions.registration_en' : 'order.actions.registration_zh' ) }}</a>
        </p>
      </form>

      <form v-if="role === 'admin'">
        <input type="text" v-model="admin.UAccount" :placeholder="t(isTranslated ? 'login.username_en' : 'login.username_zh' )">
        <input type="password" v-model="admin.UPassword" :placeholder="t(isTranslated ? 'login.password_en' : 'login.password_zh' )">
        <button @click.prevent="login('admin')" style="background-color: #83b3f3">{{ $t(isTranslated ? 'order.actions.login_en' : 'order.actions.login_zh' ) }}</button>
      </form>

      <!-- Registration Modal -->
      <div v-if="showRegistration" class="registration-modal">
        <h3>{{ $t(isTranslated ? 'login.registration_title_en' : 'login.registration_title_zh' ) }}</h3>
        <el-form ref="form" :model="newUser" label-width="100px">
          <el-form-item :label="t(isTranslated ? 'login.username_en' : 'login.username_zh' )" prop="UAccount" required>
            <el-input v-model="newUser.UAccount" :placeholder="t(isTranslated ? 'login.username_en' : 'login.username_zh' )"></el-input>
          </el-form-item>
          <el-form-item :label="t(isTranslated ? 'login.password_en' : 'login.password_zh' )" prop="UPassword" required>
            <el-input type="password" v-model="newUser.UPassword" :placeholder="t(isTranslated ? 'login.password_en' : 'login.password_zh' )"></el-input>
          </el-form-item>
          <el-form-item label="Phone" prop="UPhone" required>
            <el-input type="text" v-model="newUser.UPhone" :placeholder="t(isTranslated ? 'login.phone_en' : 'login.phone_zh' )"></el-input>
          </el-form-item>
          <el-form-item label="Address" prop="UAddress" required>
            <el-input v-model="newUser.UAddress" :placeholder="t(isTranslated ? 'login.address_en' : 'login.address_zh' )"></el-input>
          </el-form-item>
          <el-button @click="registerUser" style="margin: 5px">{{ $t(isTranslated ? 'order.actions.submit_en' : 'order.actions.submit_zh' ) }}</el-button>
          <el-button @click="showRegistration = false" style="margin: 5px">{{ $t(isTranslated ? 'order.actions.back_en' : 'order.actions.back_zh' ) }}</el-button>
        </el-form>
      </div>
    </div>
    <TranslateButton
      v-model:isTranslated="isTranslated"
      class="floating-translate-btn"
    />
  </div>
</template>

<script>
import TranslateButton from "@/components/block/translate.vue";
import { useI18n } from 'vue-i18n';

import axios from 'axios'; // 引入 Axios
import { jwtDecode } from 'jwt-decode'; // 正确导入jwt-decode
import ShopLogo from "@/components/block/Shop-Logo.vue";
import http from "@/utils/re.js";
import {ref} from "vue";
export default {
  components: { ShopLogo,TranslateButton },
  setup() {
    const { t } = useI18n();
    const isTranslated = ref(false);
    return { isTranslated, t };
  },
  data() {
    return {
      role: 'user',
      user: {
        UAccount: '',
        UPassword: ''
      },
      admin: {
        UAccount: '',
        UPassword: ''
      },
      showRegistration: false,
      rUser: {
        username: '',
        password: '',
        ucategory: ''
      },
      newUser: {
        UAccount: '',
        UPassword: '',
        UPhone: '',
        UAddress: ''
      }
    };
  },
  methods: {
    async login(role) {
      const formData = {
        uaccount: role === 'user' ? this.user.UAccount : this.admin.UAccount,
        upassword: role === 'user' ? this.user.UPassword : this.admin.UPassword
      };
      try {
        const response = await http.post('/login', formData);
        const token = response.data.data;
        if (token) {
          localStorage.setItem('token', token);
        }
        const data = response.data;
        console.log("res:",data);
        if (data.message === "Success" && data.code === 200) {
          this.rUser = data;
          const token = this.rUser.data;
          const claims = jwtDecode(token);
          console.log("role:",claims)
          if (role === 'admin') {
            if (claims['Ucategory'] === 0) {
              // Navigate to admin home and pass UAccount
              this.$router.push({ name: 'AHome', query: { token } });
            } else {
              alert('登录失败，权限不足');
            }
          } else {
            // Navigate to user home and pass UAccount
            this.$router.push({ name: 'BHome', query: { token } });
          }

        } else {
          alert('登录失败，请检查用户名和密码是否正确');
        }
      } catch (error) {
        console.error('登录请求失败:', error);
        if (error.response) {
          alert('登录请求失败，错误信息: ' + error.response.data.message);
        } else {
          alert('登录请求失败，错误信息: ' + error.message);
        }
      }
    },
    async registerUser() {
      const formData = {
        uaccount: this.newUser.UAccount,
        upassword: this.newUser.UPassword,
        uphone: this.newUser.UPhone,
        uaddress: this.newUser.UAddress
      };
      try {
        if (formData.uaccount === ''){
          alert('请输入账户！');
        }else if(formData.upassword === ''){
          alert('请输入密码！');
        }else if(formData.uphone === ''){
          alert('请输入联系电话！');
        }else if(formData.uaddress === ''){
          alert('请输入地址');
        }else{
          const response = await http.post('/user/create', formData);
          if (response.data) {
            this.$router.push('/');
            alert('注册成功！请登录。');
          }else{
            alert('账号已存在！');
          }
        }
      } catch (error) {
        console.error('注册请求失败:', error);
        // 检查是否有响应数据
        if (error.response) {
          alert('注册请求失败，错误信息: ' + error.response.data.message);
        } else {
          alert('注册请求失败，错误信息: ' + error.message);
        }
      }
    }
  }
};
</script>

<style scoped>
/* 样式部分保持不变 */
.login-container {
  background-image: url('https://tse1-mm.cn.bing.net/th/id/OIP-C.KknZ82d9g6mi2ISfFEK7IgHaEK?w=309&h=180&c=7&r=0&o=5&dpr=1.5&pid=1.7&#39;');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  font-family: Arial, sans-serif;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  margin: 0;
  background-color: #f4f4f4;
}
.card {
  background: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 300px;
}
.logo {
  position: absolute;
  top: 0;
  left: 0;
}
input[type="text"], input[type="password"], input[type="email"], input[type="address"] {
  width: 100%;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ddd;
  border-radius: 5px;
}
button {
  width: 100%;
  padding: 10px;
  border: none;
  border-radius: 5px;
  background-color: #60bde1;
  color: white;
  cursor: pointer;
}
button:hover {
  background-color: #2dacda;
}
.registration-modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  width: 500px;
  height: 400px;
}
</style>
