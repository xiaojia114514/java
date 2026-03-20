<template>
  <div class="content-wrapper">
    <el-scrollbar class="scrollbar-container">
      <el-table :data="tableData" class="custom-table-row" style="width: 100%">
        <el-table-column :label="$t(isTranslated ? 'order.orderId_en' : 'order.orderId_zh')" prop="oid"/>
        <el-table-column :label="$t(isTranslated ? 'order.productId_en' : 'order.productId_zh')" prop="gid"/>
        <el-table-column :label="$t(isTranslated ? 'order.productName_en' : 'order.productName_zh')" prop="gname"/>
        <el-table-column :label="$t(isTranslated ? 'order.userAccount_en' : 'order.userAccount_zh')" prop="uaccount"/>
        <el-table-column :label="$t(isTranslated ? 'order.quantity_en' : 'order.quantity_zh')" prop="onumber"/>
        <el-table-column :label="$t(isTranslated ? 'order.phone_en' : 'order.phone_zh')" prop="ophone"/>
        <el-table-column :label="$t(isTranslated ? 'order.address_en' : 'order.address_zh')" prop="oaddress"/>
        <el-table-column :label="$t(isTranslated ? 'order.remark_en' : 'order.remark_zh')" prop="oremark"/>
        <!-- Action buttons column -->
        <el-table-column :label="$t(isTranslated ? 'order.operations_en' : 'order.operations_zh')">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="acceptOrder(scope.row)">
              {{ $t(isTranslated ? 'order.actions.accept_en' : 'order.actions.accept_zh') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-scrollbar>
  </div>
  <div class="pagination-container">
    <el-pagination
      background
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
import {ref, onMounted} from 'vue';
import { useI18n } from 'vue-i18n';
import {ElMessage} from "element-plus";
import {jwtDecode} from "jwt-decode";
import http from "@/utils/re.js";

const { t } = useI18n();
const props = defineProps({
  isTranslated: {
    type: Boolean,
    default: false
  }
});

// Reactive variables
const allData = ref([]);
const tableData = ref([]);
const pageSize = ref(10);
const pageNum = ref(1);
const total = ref(0);
const token = localStorage.getItem('token');
const claims = jwtDecode(token);

// Fetch orders function
const fetchOrders = async () => {
  try {
    const response = await http.get('/order/list', {
      headers: {
        'Authorization': `${token}`,
      }
    });

    const orders = response.data.filter(item => item.ostate === 0);
    const allDataWithGname = await Promise.all(orders.map(async (order) => {
      const goodsInfo = await fetchGnameByGid(order.gid);
      return {
        ...order,
        gname: goodsInfo.gname,
        gseller: goodsInfo.gseller
      };
    }));

    allData.value = allDataWithGname.filter(item => item.gseller === claims.account);
    total.value = allData.value.length;
    paginate();
  } catch (error) {
    console.error(t(props.isTranslated ? 'order.messages.fetchOrdersFailed_en' : 'order.messages.fetchOrdersFailed_zh'), error);
    ElMessage.error(t(props.isTranslated ? 'order.messages.fetchOrdersFailed_en' : 'order.messages.fetchOrdersFailed_zh'));
  }
};

const fetchGnameByGid = async (gid) => {
  try {
    const response = await http.get(`/goods/get_by_id/${gid}`, {
      headers: {
        'Authorization': `${token}`,
      }
    });
    return {
      gname: response.data.gname,
      gseller: response.data.gseller
    };
  } catch (error) {
    console.error(t(props.isTranslated ? 'order.messages.fetchProductsFailed_en' : 'order.messages.fetchProductsFailed_zh'), error);
    return {
      gname: t(props.isTranslated ? 'order.messages.fetchProductsFailed_en' : 'order.messages.fetchProductsFailed_zh'),
      gseller: ''
    };
  }
};

// Pagination functions
const paginate = () => {
  const startIndex = (pageNum.value - 1) * pageSize.value;
  const endIndex = startIndex + pageSize.value;
  tableData.value = allData.value.slice(startIndex, endIndex);
};

const handleSizeChange = (val) => {
  pageSize.value = val;
  paginate();
};

const handleCurrentChange = (val) => {
  pageNum.value = val;
  paginate();
};

// Accept order method
const acceptOrder = async (order) => {
  try {
    const response = await http.post('/order/update', {oid: order.oid, ostate: 1}, {
      headers: {
        'Authorization': `${token}`,
      }
    });

    if (response.data.message === "修改成功") {
      ElMessage.success(t(props.isTranslated ? 'order.messages.updateSuccess_en' : 'order.messages.updateSuccess_zh'));
      await fetchOrders();
    } else {
      console.error(t(props.isTranslated ? 'order.messages.updateFailed_en' : 'order.messages.updateFailed_zh'), response.data.message);
      ElMessage.error(t(props.isTranslated ? 'order.messages.updateFailed_en' : 'order.messages.updateFailed_zh'));
    }
  } catch (error) {
    console.error(t(props.isTranslated ? 'order.messages.updateFailed_en' : 'order.messages.updateFailed_zh'), error);
    ElMessage.error(t(props.isTranslated ? 'order.messages.updateFailed_en' : 'order.messages.updateFailed_zh'));
  }
};

// Component mounted hook
onMounted(() => {
  fetchOrders();
});
</script>

<style scoped>
.content-wrapper {
  height: calc(100vh - 180px);
  padding: 20px;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.scrollbar-container {
  height: 100% !important;
}

.custom-table-row {
  --el-table-header-bg-color: #f5f7fa;
  --el-table-border-color: #ebeef5;
  --el-table-row-hover-bg-color: #f5f7fa;
  margin: 0 auto;
}

.custom-table-row :deep(th) {
  font-weight: 600;
  color: #303133;
  background: var(--el-table-header-bg-color);
}

.custom-table-row :deep(td) {
  padding: 12px 0;
  color: #606266;
}

.status-completed {
  color: #67c23a;
  font-weight: 500;
}

.status-canceled {
  color: #f56c6c;
  font-weight: 500;
}

.pagination-container {
  position: sticky;
  bottom: 0;
  background: #ffffff;
  padding: 16px 0;
  margin-top: 16px;
  border-top: 1px solid #ebeef5;
  display: flex;
  justify-content: center;
  z-index: 1;
}

/* 操作按钮统一样式 */
.el-button {
  min-width: 80px;
  margin: 4px;
}

/* 弹窗统一样式 */
.el-dialog {
  border-radius: 8px;
}

.el-dialog__header {
  border-bottom: 1px solid #ebeef5;
}

/* 表格行高统一设置 */
.custom-table-row :deep(.el-table__row) {
  height: 65px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .content-wrapper {
    padding: 10px;
  }

  .custom-table-row :deep(.el-table__row) {
    height: auto;
    padding: 8px 0;
  }

  .el-button {
    min-width: 60px;
    padding: 8px 12px;
  }
}
</style>
