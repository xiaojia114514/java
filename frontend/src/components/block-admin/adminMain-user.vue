<template>
  <div class="content-wrapper">
    <el-scrollbar>
      <el-table :data="tableData" class="custom-table-row" style="width: 100%">
        <el-table-column prop="uaccount" label="用户名"/>
        <el-table-column prop="upassword" label="密码"/>
        <el-table-column prop="uphone" label="联系电话"/>
        <el-table-column label="默认地址">
          <template #default="{ row }">
            <div v-if="row.loading">加载中...</div>
            <div v-else-if="row.addressError">地址加载失败</div>
            <div v-else>{{ row.defaultAddress || '未设置默认地址' }}</div>
          </template>
        </el-table-column>
        <el-table-column label="角色">
          <template #default="{ row }">
            {{ roleByCategory(row.ucategory) }}
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="primary" size="small" @click="fetchAllOrders(scope.row)">查看订单</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-scrollbar>
    <el-pagination
      class="pagination"
      v-model:current-page="pageNum"
      v-model:page-size="pageSize"
      :page-sizes="[5, 10, 20]"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import router from "@/router/index.js";
import http from "@/utils/re.js";
const allData = ref([]);
const tableData = ref([]);
const pageSize = ref(10);
const pageNum = ref(1);
const total = ref(0);
const token = localStorage.getItem('token');

const roleByCategory = computed(() => (category) => {
  return category === 0 ? '管理员' : category === 1 ? '买家用户' : '未知角色';
});

// 获取用户默认地址
const fetchDefaultAddress = async (user) => {
  try {
    user.loading = true;
    const response = await http.post(
      "/address/list",
      { uaccount: user.uaccount, adefault: 1 },
      {
        headers: {
          'Authorization': `${token}`,
        }
      }
    );

    // 保持对数组结构的判断
    if (response.data?.length > 0) {
      user.defaultAddress = response.data[0].atext;
    } else {
      user.defaultAddress = null;
    }
  } catch (error) {
    console.error("获取地址失败:", error);
    user.addressError = true;
  } finally {
    user.loading = false;
  }
};

// 获取所有用户并处理地址
const fetchAllUsers = async () => {
  try {
    const response = await http.post('/user/list', {
      headers: {
        'Authorization': `${token}`,
      }
    });

    allData.value = await Promise.all(
      response.data.map(async user => {
        const userWithAddress = {
          ...user,
          loading: false,
          addressError: false,
          defaultAddress: null
        };
        await fetchDefaultAddress(userWithAddress);
        return userWithAddress;
      })
    );

    total.value = allData.value.length;
    paginate(allData.value);
  } catch (error) {
    console.error('Error fetching users:', error);
  }
};

const fetchAllOrders = async (user) => {
  await router.push({name: 'OrderDet', params: {uid: user.uaccount}});
};

const paginate = (data) => {
  const startIndex = (pageNum.value - 1) * pageSize.value;
  const endIndex = startIndex + pageSize.value;
  tableData.value = data.slice(startIndex, endIndex);
};

const handleSizeChange = (val) => {
  pageSize.value = val;
  paginate(allData.value);
};

const handleCurrentChange = (val) => {
  pageNum.value = val;
  paginate(allData.value);
};

onMounted(() => {
  fetchAllUsers();
});
</script>

<style scoped>
.custom-table-row .el-table .el-table__body {
  --el-table-row-height: 100px;
}

.content-wrapper {
  position: relative;
  overflow: auto;
  height: calc(100vh - 50px);
}

.pagination {
  position: fixed;
  bottom: 0;
  width: 100%;
  background-color: white;
  box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}
</style>
