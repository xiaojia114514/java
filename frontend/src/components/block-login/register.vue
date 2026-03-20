<script>
</script>

<template>
  <!-- Registration Modal -->
  <div style="height: 100%; width: 100%">
    <h3>Registration</h3>
    <el-form ref="form" :model="newUser" label-width="100px">
      <el-form-item label="Username" prop="UAccount" required>
        <el-input v-model="newUser.UAccount" placeholder="Username"></el-input>
      </el-form-item>
      <el-form-item label="Password" prop="UPassword" required>
        <el-input type="password" v-model="newUser.UPassword" placeholder="Password"></el-input>
      </el-form-item>
      <el-form-item label="Phone" prop="UPhone" required>
        <el-input type="phone" v-model="newUser.UPhone" placeholder="Phone"></el-input>
      </el-form-item>
      <el-form-item label="Address" prop="UAddress" required>
        <el-input v-model="newUser.UAddress" placeholder="Address"></el-input>
      </el-form-item>
      <el-button @click="registerUser" style="margin: 5px">提交</el-button>
      <el-button @click="showRegistration = false" style="margin: 5px">返回</el-button>
    </el-form>
  </div>
</template>

<style scoped>
</style>
