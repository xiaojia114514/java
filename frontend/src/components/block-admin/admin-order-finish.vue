<template>
  <div class="content-wrapper">
    <!-- Scrollbar component wrapping content area -->
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
        <el-table-column :label="$t(isTranslated ? 'order.status_en' : 'order.status_zh')">
          <template #default="scope">
            <!-- Show status based on ostate value -->
            <span v-if="scope.row.ostate === -1" class="status-canceled">
              {{ $t(isTranslated ? 'order.statuses.canceled_en' : 'order.statuses.canceled_zh') }}
            </span>
            <span v-if="scope.row.ostate === 2" class="status-completed">
              {{ $t(isTranslated ? 'order.statuses.completed_en' : 'order.statuses.completed_zh') }}
            </span>
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
import { ref, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import { ElMessage } from 'element-plus';
import { jwtDecode } from "jwt-decode";
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
    const orders = response.data.filter(item => item.ostate === 2 || item.ostate === -1);

    // Add product info and filter by seller
    allData.value = (await Promise.all(orders.map(async (order) => {
      const goodsInfo = await fetchGnameByGid(order.gid);
      return {...order, gname: goodsInfo.gname, gseller: goodsInfo.gseller};
    }))).filter(item => item.gseller === claims.account);

    total.value = allData.value.length;
    paginate(allData.value);
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
      gname: t(props.isTranslated ? 'order.messages.unknownProduct_en' : 'order.messages.unknownProduct_zh'),
      gseller: ''
    };
  }
};

// Pagination functions
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
