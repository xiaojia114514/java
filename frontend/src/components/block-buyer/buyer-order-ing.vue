<template>
  <div class="content-wrapper">
    <el-scrollbar class="scrollbar-container">
      <el-table :data="tableData" class="custom-table-row" style="width: 100%">
        <el-table-column
          prop="oid"
          :label="$t(isTranslated ? 'order.orderId_en' : 'order.orderId_zh')"
        />
        <el-table-column
          prop="gid"
          :label="$t(isTranslated ? 'order.productId_en' : 'order.productId_zh')"
        />
        <el-table-column
          prop="gname"
          :label="$t(isTranslated ? 'order.productName_en' : 'order.productName_zh')"
        />
        <el-table-column
          prop="onumber"
          :label="$t(isTranslated ? 'order.quantity_en' : 'order.quantity_zh')"
        />
        <el-table-column
          prop="ophone"
          :label="$t(isTranslated ? 'order.phone_en' : 'order.phone_zh')"
        />
        <el-table-column
          prop="oaddress"
          :label="$t(isTranslated ? 'order.address_en' : 'order.address_zh')"
        />
        <el-table-column
          prop="oremark"
          :label="$t(isTranslated ? 'order.remark_en' : 'order.remark_zh')"
        />
        <el-table-column
          :label="$t(isTranslated ? 'order.status_en' : 'order.status_zh')"
        >
          <template #default="scope">
            <div v-if="scope.row.opaid === 1">
              <div v-if="scope.row.latestLogistics">
                <span :class="'status-tag status-' + scope.row.latestLogistics.eostate">
                  {{ formatExpressState(scope.row.latestLogistics.eostate) }}
                </span>
              </div>
              <div v-else>
                <span class="status-tag status-1">
                  {{ isTranslated ? 'Packing...' : '商家打包中...' }}
                </span>
              </div>
            </div>
            <div v-else>
              <el-button
                type="primary"
                size="small"
                @click="toPayPage(scope.row)"
              >
                {{ $t(isTranslated ? 'order.actions.pay_en' : 'order.actions.pay_zh') }}
              </el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          :label="$t(isTranslated ? 'order.operations_en' : 'order.operations_zh')"
        >
          <template #default="scope">
            <div style="justify-content: center;align-items: center;">
              <el-button
                v-if="!scope.row.latestLogistics || [0, 1].includes(scope.row.latestLogistics?.eostate)"
                type="primary"
                size="small"
                style="background-color: #df6767"
                @click="canselOrder(scope.row)"
              >
                {{ $t(isTranslated ? 'order.actions.cancel_en' : 'order.actions.cancel_zh') }}
              </el-button>
              <el-button
                v-if="scope.row.latestLogistics?.eostate === 4"
                type="success"
                size="small"
                style="background-color: #67c23a"
                @click="confirmReceipt(scope.row)"
              >
                {{ isTranslated ? 'Confirm Receipt' : '确认收货' }}
              </el-button>
              <el-button
                type="info"
                size="small"
                @click="showLogisticsDetail(scope.row)"
              >
                {{ isTranslated ? 'Logistics Detail' : '物流详情' }}
              </el-button>
            </div>
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
    <el-dialog
      v-model="dialogVisible"
      :title="isTranslated ? 'Logistics Detail' : '物流详情'"
      width="800px"
      class="logistics-dialog"
      :modal-class="'logistics-modal'"
    >
      <div v-if="logisticsList.length > 0">
        <el-table :data="logisticsList" style="width: 100%">
          <el-table-column
            prop="eostate"
            :label="$t(isTranslated ? 'order.status_en' : 'order.status_zh')"
            width="120"
          >
            <template #default="scope">
              <span :class="'status-tag status-' + scope.row.eostate">
                {{ formatExpressState(scope.row.eostate) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="eotime"
            :label="isTranslated ? 'Time' : '时间'"
          >
            <template #default="scope">
              {{
                new Date(scope.row.eotime).toLocaleString(isTranslated ? 'en-US' : 'zh-CN', {
                  year: 'numeric',
                  month: '2-digit',
                  day: '2-digit',
                  hour: '2-digit',
                  minute: '2-digit',
                  second: '2-digit',
                  hour12: false
                }).replace(/\//g, '-')
              }}
            </template>
          </el-table-column>
          <el-table-column
            prop="eoaddress"
            :label="isTranslated ? 'Location' : '位置信息'"
          />
        </el-table>
      </div>
      <div v-else class="empty-tips">
        <el-empty :description="isTranslated ? 'No logistics records' : '暂无物流记录'"/>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, defineProps } from 'vue';
import axios from 'axios';
import { jwtDecode } from "jwt-decode";
import { ElMessage } from "element-plus";
import { useRouter } from 'vue-router';
import http from "@/utils/re.js";

const props = defineProps({
  isTranslated: {
    type: Boolean,
    default: false
  }
});

const allData = ref([]);
const tableData = ref([]);
const logisticsList = ref([]);
const dialogVisible = ref(false);

const pageSize = ref(10);
const pageNum = ref(1);
const total = ref(0);

const router = useRouter();
const token = localStorage.getItem('token');
let account = '';
const claims = token ? jwtDecode(token) : {};
console.log(claims);

const fetchAllOrders = async () => {
  try {
    const response = await http.get('/order/list', {
      headers: {
        'Authorization': `${token}`,
      }
    });

    const processedOrders = await Promise.all(
      response.data.map(async (item) => {
        const goodsInfo = await fetchGoodsInfo(item.gid);
        return {
          ...item,
          gname: goodsInfo.gname,
          gvalue: goodsInfo.gvalue,
          latestLogistics: await fetchLatestLogistics(item.oid)
        };
      })
    );

    allData.value = processedOrders.filter(item =>
      [1].includes(item.ostate) &&
      item.uaccount === account
    );

    total.value = allData.value.length;
    paginate();
  } catch (error) {
    console.error('Error fetching orders:', error);
    ElMessage.error(props.isTranslated ? "Failed to load orders" : "获取订单数据失败，请检查网络连接");
  }
};

const fetchGoodsInfo = async (gid) => {
  try {
    const data = {
      gid: gid,
    }
    const response = await http.post(`/goods/list`, data, {
      headers: {
        'Authorization': `${token}`,
      }
    });
    return {
      gname: response.data[0]?.gname || (props.isTranslated ? 'Unknown product' : '未知商品'),
      gvalue: response.data[0]?.gvalue || 0
    };
  } catch (error) {
    console.error('Error fetching goods info:', error);
    return {
      gname: props.isTranslated ? 'Unknown product' : '未知商品',
      gvalue: 0
    };
  }
};

const fetchLatestLogistics = async (oid) => {
  try {
    const data = {
      oid: oid,
    }
    const response = await http.post(`/expressorder/list`, data,{
      headers: {
        'Authorization': `${token}`,
      }
    });

    if (response.data && response.data.length > 0) {
      const sortedLogistics = [...response.data].sort((a, b) =>
        new Date(b.eotime) - new Date(a.eotime)
      );
      return sortedLogistics[0];
    }
    return null;
  } catch (error) {
    console.error('Error fetching logistics:', error);
    return null;
  }
};

// 修改后的支付方法，跳转到支付页面
const toPayPage = (order) => {
  const data = {
    oid: order.oid,
    totalAmount: order.gvalue * order.onumber,
    subject: order.gname,
  };

  // 跳转到支付页面并携带支付数据
  router.push({
    name: 'Pay',
    query: data // 使用query传递参数
  });
};

const canselOrder = async (order) => {
  try {
    const data = {
      oid: order.oid,
      ucategory: claims.Ucategory
    };
    const response = await http.post('/order/cancel', data, {
      headers: {
        'Authorization': `${token}`,
      }
    });
    if (response.data) {
      ElMessage.success(props.isTranslated ? "Order canceled" : "订单已取消");
      await fetchAllOrders();
    } else {
      ElMessage.error(props.isTranslated ? "Cancel failed" : "取消失败");
    }
  } catch (error) {
    console.error('Error canceling order:', error);
    ElMessage.error(props.isTranslated ? "Operation failed" : "操作失败");
  }
};

const confirmReceipt = async (order) => {
  try {
    const latest = order.latestLogistics;
    if (!latest) {
      ElMessage.error(props.isTranslated ? "Logistics info not found" : "找不到物流信息");
      return;
    }

    const newLogistics = {
      oid: order.oid,
      eostate: 5,
      eotime: new Date().toISOString(),
      eoaddress: latest.eoaddress
    };

    const createResponse = await http.post(
      "/expressorder/create",
      newLogistics,
      {headers: {Authorization: token}}
    );

    if (createResponse.data) {
      ElMessage.success(props.isTranslated ? "Receipt confirmed" : "确认收货成功");
      await fetchAllOrders();
    } else {
      ElMessage.error(props.isTranslated ? "Operation failed" : "操作失败");
    }
  } catch (error) {
    console.error("确认收货失败:", error);
    ElMessage.error(props.isTranslated ? "Operation failed, check network" : "操作失败，请检查网络连接");
  }
};

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

const showLogisticsDetail = async (row) => {
  try {
    const data = {
      oid: row.oid,
    }
    const response = await http.post(`/expressorder/list`, data, {
      headers: {
        'Authorization': `${token}`,
      }
    });
    if (response.data?.length > 0) {
      logisticsList.value = response.data;
      dialogVisible.value = true;
    } else {
      logisticsList.value = [];
      dialogVisible.value = true;
      ElMessage.warning(props.isTranslated ? 'No logistics records' : '该订单暂无物流记录');
    }
  } catch (error) {
    console.error('获取物流信息失败:', error);
    ElMessage.error(props.isTranslated ? 'Failed to get logistics info' : '获取物流信息失败');
  }
};

const formatExpressState = (state) => {
  const stateMap = props.isTranslated
    ? {
      0: 'Pending Pickup',
      1: 'Picked Up',
      2: 'In Transit',
      3: 'Out for Delivery',
      4: 'Ready for Pickup',
      5: 'Delivered'
    }
    : {
      0: '待揽件',
      1: '已揽收',
      2: '运输中',
      3: '派送中',
      4: '待取件',
      5: '已签收'
    };

  return stateMap[state] || (props.isTranslated ? 'Unknown status' : '未知状态');
};

onMounted(() => {
  if (token) {
    account = claims.account;
    fetchAllOrders();
  } else {
    window.location.href = '/';
  }
});
</script>

<style scoped>
.status-tag {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  min-width: 60px;
  text-align: center;
}

.status-0 {
  background-color: #f0f0f0;
  color: #666;
  border: 1px solid #ddd;
}

.status-1, .status-3 {
  background-color: #e6f7ff;
  color: #1890ff;
  border: 1px solid #91d5ff;
}

.status-2 {
  background-color: #fff7e6;
  color: #fa8c16;
  border: 1px solid #ffd591;
}

.status-4 {
  background-color: #fffbe6;
  color: #fadb14;
  border: 1px solid #ffe58f;
}

.status-5 {
  background-color: #f6ffed;
  color: #52c41a;
  border: 1px solid #b7eb8f;
}

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

.el-button {
  min-width: 80px;
  margin: 4px;
}

.el-dialog {
  border-radius: 8px;
}

.el-dialog__header {
  border-bottom: 1px solid #ebeef5;
}

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
