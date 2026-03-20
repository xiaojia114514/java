<template>
  <el-form ref="orderForm" :model="form" label-width="120px">
    <el-form-item v-for="product in selectedProducts" :key="product.gid" label="商品信息">
      <el-input v-model="product.phone" placeholder="联系电话" :disabled="true"></el-input>
      <el-input v-model="product.address" placeholder="地址" :disabled="true"></el-input>
      <el-input-number v-model="product.quantity" :min="1" label="数量"></el-input-number>
      <el-input v-model="product.remark" placeholder="备注"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm">提交</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  props: {
    selectedProducts: Array,
    claims: Object
  },
  data() {
    return {
      form: this.selectedProducts.map(product => ({
        gid: product.gid,
        phone: product.phone || this.claims.phone,
        address: product.address || this.claims.address,
        quantity: product.scnumber,
        remark: ''
      }))
    };
  },
  methods: {
    submitForm() {
      this.$emit('submit', this.form);
    }
  }
};
</script>
