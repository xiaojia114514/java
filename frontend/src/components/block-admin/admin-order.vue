<template>
  <div style="height: 100%; display: flex;">
    <ShopLogo style="background-color: #ffffff"></ShopLogo>
    <el-header style="width: 82%; text-align: right; font-size: 12px; display: flex; align-items: center; height: 80px; background-color: var(--el-color-primary-light-9);">
      <admin-head></admin-head>
    </el-header>
  </div>
  <el-table :data="orderData" style="width: 100%">
    <el-table-column prop="oid" label="订单号"/>
    <el-table-column prop="gid" label="商品号"/>
    <el-table-column prop="gname" label="商品名称"/>
    <el-table-column prop="ophone" label="联系电话"/>
    <el-table-column prop="oaddress" label="地址"/>
    <el-table-column prop="oremark" label="备注"/>
    <el-table-column label="状态">
      <template #default="scope">
        <span v-if="scope.row.ostate === 4" class="status-completed">已完成</span>
        <span v-if="scope.row.ostate === 3" class="status-completed">已发货</span>
        <span v-if="scope.row.ostate === 2" class="status-completed">已备货，待发货</span>
        <span v-if="scope.row.ostate === 1" class="status-completed">已接受，备货中</span>
        <span v-if="scope.row.ostate === 0" class="status-completed">未接受</span>
        <span v-if="scope.row.ostate === -1" class="status-completed">已撤回订单</span>
      </template>
    </el-table-column>
  </el-table>
  <div style="display: flex; justify-content: space-between; height: 80px">
    <el-button @click="goBack" style="margin-right: auto;">返回</el-button>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';  // 导入 useRouter
import ShopLogo from "@/components/block/Shop-Logo.vue";
import AdminHead from "@/components/block-admin/adminHead.vue";
import axios from "axios";
import http from "@/utils/re.js";
const token = localStorage.getItem('token');
const orderData = ref([]);
const route = useRoute();
const router = useRouter();  // 初始化 router
const uid = route.params.uid;
console.log("搜索用户：", uid);

const fetchAllOrders = async (uid) => {
  try {
    const data ={
      uaccount : uid
    }
    console.log("信息：",data)
    const response = await http.post('/order/list',{
      headers: {
        'Authorization': `${token}`,
      }
    });
    const orders = response.data.filter(item =>
      item.uaccount === uid
    );
    orderData.value = await Promise.all(orders.map(async (order) => {
      const gname = await fetchGnameByGid(order.gid);
      return {...order, gname};
    }));
    console.log("订单：", orderData.value);
  } catch (error) {
    console.error('Error fetching orders:', error);
  }
};

const fetchGnameByGid = async (gid) => {
  try {
    const response = await http.get(`/goods/get_by_id/${gid}`, {
      headers: {
        'Authorization': `${token}`,
      }
    });
    return response.data.gname;
  } catch (error) {
    console.error('获取商品名称失败:', error);
    return '未知商品';
  }
};

const goBack = () => {
  router.go(-1);  // 返回上一页面
};

onMounted(() => {
  fetchAllOrders(uid);
});
</script>

<style scoped>
.status-completed {
  color: green;
}
</style>
