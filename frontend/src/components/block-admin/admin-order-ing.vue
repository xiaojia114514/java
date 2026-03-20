<template>
  <div class="content-wrapper">
    <!-- Scrollbar component wrapping content area -->
    <el-scrollbar class="scrollbar-container">
      <el-table
        :data="tableData"
        @row-click="handleRowClick">
        <!-- First column with tooltip -->
        <el-table-column :label="$t(isTranslated ? 'order.orderId_en' : 'order.orderId_zh')">
          <template #default="scope">
            <el-tooltip
              :content="scope.row.opaid ? $t(isTranslated ? 'order.messages.clickForLogistics_en' : 'order.messages.clickForLogistics_zh') : ''"
              placement="top"
              :disabled="!scope.row.opaid">
              <span>{{ scope.row.oid }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column :label="$t(isTranslated ? 'order.productId_en' : 'order.productId_zh')" prop="gid"/>
        <el-table-column :label="$t(isTranslated ? 'order.productName_en' : 'order.productName_zh')" prop="gname"/>
        <el-table-column :label="$t(isTranslated ? 'order.userAccount_en' : 'order.userAccount_zh')" prop="uaccount"/>
        <el-table-column :label="$t(isTranslated ? 'order.quantity_en' : 'order.quantity_zh')" prop="onumber"/>
        <el-table-column :label="$t(isTranslated ? 'order.phone_en' : 'order.phone_zh')" prop="ophone"/>
        <el-table-column :label="$t(isTranslated ? 'order.address_en' : 'order.address_zh')" prop="oaddress"/>
        <el-table-column :label="$t(isTranslated ? 'order.remark_en' : 'order.remark_zh')" prop="oremark"/>
        <el-table-column :label="$t(isTranslated ? 'order.status_en' : 'order.status_zh')">
          <template #default="scope">
            <div style="justify-content: center;align-items: center;">
              <span v-if="scope.row.opaid === 0" class="status-upcompleted">
                {{ $t(isTranslated ? 'order.statuses.unpaid_en' : 'order.statuses.unpaid_zh') }}
              </span>
              <span v-if="scope.row.opaid === 1" class="status-completed">
                {{ $t(isTranslated ? 'order.statuses.paid_en' : 'order.statuses.paid_zh') }}
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t(isTranslated ? 'order.logistics_en' : 'order.logistics_zh')">
          <template #default="scope">
            <div v-if="scope.row.opaid === 0" class="status-upcompleted">
              {{ $t(isTranslated ? 'order.statuses.unpaid_en' : 'order.statuses.unpaid_zh') }}
            </div>
            <div v-else>
              <span v-if="scope.row.eid !== 0">{{ getExpressText(scope.row.eid) }}</span>
              <el-button
                v-else
                type="primary"
                size="small"
                @click.stop="openExpressDialog(scope.row)">
                {{ $t(isTranslated ? 'order.actions.selectLogistics' : 'order.actions.selectLogistics') }}
              </el-button>
            </div>
          </template>
        </el-table-column>
        <!-- Action buttons column -->
        <el-table-column :label="$t(isTranslated ? 'order.operations_en' : 'order.operations_zh')">
          <template #default="scope">
            <div style="justify-content: center;align-items: center;">
              <el-button type="primary" size="small" style="background-color: #ea4444" @click.stop="canselOrder(scope.row)">
                {{ $t(isTranslated ? 'order.actions.cancelAction' : 'order.actions.cancelAction') }}
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-scrollbar>
  </div>

  <!-- Express selection dialog -->
  <el-dialog
    v-model="expressDialogVisible"
    :title="$t(isTranslated ? 'order.messages.selectLogisticsCompany_en' : 'order.messages.selectLogisticsCompany_zh')"
    width="30%"
    append-to-body
  >
    <el-select
      v-model="selectedEid"
      :placeholder="$t(isTranslated ? 'order.messages.pleaseSelectLogistics_en' : 'order.messages.pleaseSelectLogistics_zh')"
      style="width: 100%"
    >
      <el-option
        v-for="item in expressList"
        :key="item.eid"
        :label="item.etext"
        :value="item.eid"
      />
    </el-select>
    <template #footer>
      <el-button @click="expressDialogVisible = false">
        {{ $t(isTranslated ? 'translate.detail.cancel_en' : 'translate.detail.cancel_zh') }}
      </el-button>
      <el-button type="primary" @click="confirmExpress">
        {{ $t(isTranslated ? 'order.actions.confirm_en' : 'order.actions.confirm_zh') }}
      </el-button>
    </template>
  </el-dialog>

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
      v-model="expressRecordDialogVisible"
      :title="$t(isTranslated ? 'order.logistics_en' : 'order.logistics_zh') + $t(isTranslated ? 'order.operations_en' : 'order.operations_zh')"
      width="50%"
      append-to-body
    >
      <el-table :data="expressRecords" style="width: 100%">
        <el-table-column :label="$t(isTranslated ? 'order.status_en' : 'order.status_zh')">
          <template #default="scope">
            <span :class="'status-tag status-' + scope.row.eostate">
              {{ formatExpressState(scope.row.eostate) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column :label="$t(isTranslated ? 'order.messages.time_en' : 'order.messages.time_zh')">
          <template #default="scope">
            {{
              new Date(scope.row.eotime)
                .toLocaleString(isTranslated ? 'en-US' : 'zh-CN', {
                  year: 'numeric',
                  month: '2-digit',
                  day: '2-digit',
                  hour: '2-digit',
                  minute: '2-digit',
                  second: '2-digit',
                  hour12: false
                })
                .replace(/\//g, '-')
            }}
          </template>
        </el-table-column>
        <el-table-column :label="$t(isTranslated ? 'order.address_en' : 'order.address_zh')" prop="eoaddress" />
      </el-table>
      <!-- Status update form -->
      <div class="status-update-form">
        <el-form :model="statusForm" label-width="80px">
          <el-form-item :label="$t(isTranslated ? 'order.status_en' : 'order.status_zh')">
            <el-tag>{{ formatExpressState(latestState) }}</el-tag>
          </el-form-item>
          <el-form-item :label="$t(isTranslated ? 'order.messages.newStatus_en' : 'order.messages.newStatus_zh')" required>
            <el-select v-model="statusForm.eostate" :placeholder="$t(isTranslated ? 'order.messages.selectStatus_en' : 'order.messages.selectStatus_zh')">
              <el-option
                v-for="state in availableStates"
                :key="state"
                :label="formatExpressState(state)"
                :value="state"
              />
            </el-select>
          </el-form-item>
          <el-form-item :label="$t(isTranslated ? 'order.messages.location_en' : 'order.messages.location_zh')" required>
            <el-input v-model="statusForm.eoaddress" :placeholder="$t(isTranslated ? 'order.messages.enterLocation_en' : 'order.messages.enterLocation_zh')"/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitStatusUpdate">
              {{ $t(isTranslated ? 'order.actions.submit_en' : 'order.actions.submit_zh') }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import { ElMessage, ElMessageBox } from "element-plus";
import { jwtDecode } from "jwt-decode";
import http from "@/utils/re.js";

const { t } = useI18n();
const props = defineProps({
  isTranslated: {
    type: Boolean,
    default: false
  }
});
const allData = ref([]);
const tableData = ref([]);
const pageSize = ref(10);
const pageNum = ref(1);
const total = ref(0);
const token = localStorage.getItem('token');
const claims = jwtDecode(token);
const statusForm = ref({
  eostate: null,
  eoaddress: ''
});
const latestState = ref(-1);
const availableStates = ref([]);
const expressDialogVisible = ref(false);
const expressList = ref([
  { eid: 1, etext: t(props.isTranslated ? 'express.yunda_en' : 'express.yunda_zh') },
  { eid: 2, etext: t(props.isTranslated ? 'express.sf_en' : 'express.sf_zh') },
  { eid: 3, etext: t(props.isTranslated ? 'express.yuantong_en' : 'express.yuantong_zh') },
  { eid: 4, etext: t(props.isTranslated ? 'express.zhongtong_en' : 'express.zhongtong_zh') },
  { eid: 5, etext: t(props.isTranslated ? 'express.post_en' : 'express.post_zh') }
]);
const selectedEid = ref('');
const currentOrderId = ref('');
const orderForm = ref({
  ophone: '',
  oaddress: '',
  onumber: '',
  oremark: ''
});
const expressRecordDialogVisible = ref(false);
const expressRecords = ref([]);
const loading = ref(false);

const formatExpressState = (state) => {
  const stateMap = {
    0: t(props.isTranslated ? 'expressState.pending_en' : 'expressState.pending_zh'),
    1: t(props.isTranslated ? 'expressState.received_en' : 'expressState.received_zh'),
    2: t(props.isTranslated ? 'expressState.transit_en' : 'expressState.transit_zh'),
    3: t(props.isTranslated ? 'expressState.delivering_en' : 'expressState.delivering_zh'),
    4: t(props.isTranslated ? 'expressState.waiting_en' : 'expressState.waiting_zh'),
    5: t(props.isTranslated ? 'expressState.signed_en' : 'expressState.signed_zh')
  };
  return stateMap[state] || t(props.isTranslated ? 'expressState.unknown_en' : 'expressState.unknown_zh');
};

// 获取物流公司名称
const getExpressText = (eid) => {
  const express = expressList.value.find(item => item.eid === eid);
  return express ? express.etext : '未知物流';
};

// 打开物流选择对话框
const openExpressDialog = (order) => {
  currentOrderId.value = order.oid;
  selectedEid.value = '';
  expressDialogVisible.value = true;
};

// 确认选择物流
const confirmExpress = async () => {
  if (!selectedEid.value) {
    ElMessage.warning('请选择物流公司');
    return;
  }

  try {
    // 添加确认对话框
    await ElMessageBox.confirm(
      t(props.isTranslated ? 'order.messages.confirmLogistics_en' : 'order.messages.confirmLogistics_zh'),
      t(props.isTranslated ? 'order.messages.confirmTitle_en' : 'order.messages.confirmTitle_zh'),
      {
        confirmButtonText: t(props.isTranslated ? 'translate.detail.cancel_en' : 'translate.detail.cancel_zh'),
        cancelButtonText: t(props.isTranslated ? 'translate.detail.confirm_en' : 'translate.detail.confirm_zh'),
        type: 'warning'
      }
    );

    loading.value = true;
    const currentOrder = allData.value.find(order => order.oid === currentOrderId.value);
    orderForm.value = {
      oid: currentOrderId.value,
      ostate: currentOrder.ostate,
      eid: selectedEid.value
    };

    const response = await http.post('/order/update', orderForm.value, {
      headers: {Authorization: token}
    });
    if (response.data.status === 200) {
      ElMessage.success('物流信息已更新');
      expressDialogVisible.value = false;
      // 直接更新本地数据避免重新请求
      const updatedOrder = allData.value.find(order => order.oid === currentOrderId.value);
      if (updatedOrder) {
        updatedOrder.eid = selectedEid.value;
        // 强制刷新表格数据
        paginate([...allData.value]);
      }
    }
  } catch (error) {
    // 用户点击取消时会触发reject，这里需要忽略取消操作的错误
    if (error !== 'cancel') {
      console.error('更新物流信息失败:', error);
      ElMessage.error('操作失败，请稍后重试');
    }
  } finally {
    loading.value = false;
  }
};

// 获取所有用户数据的函数
const fetchOrders = async () => {
  try {
    loading.value = true;
    const response = await http.get('/order/list', {
      headers: {
        'Authorization': `${token}`,
      }
    });
    console.log("数据 :",response.data);
    const orders = response.data.filter(item => item.ostate === 1 && item.eostate !== 5);
    // 添加商品信息和商家过滤
    allData.value = (await Promise.all(orders.map(async (order) => {
      const goodsInfo = await fetchGnameByGid(order.gid);
      return {...order, gname: goodsInfo.gname, gseller: goodsInfo.gseller};
    }))).filter(item => item.gseller === claims.account);  // 商家过滤
    total.value = allData.value.length;
    const totalPage = Math.ceil(total.value / pageSize.value);
    if (pageNum.value > totalPage && totalPage > 0) {
      pageNum.value = totalPage;
    }
    paginate(allData.value);
  } catch (error) {
    console.error('Error fetching orders:', error);
    ElMessage.error('获取订单列表失败');
  }
};

// 修改fetchGnameByGid方法
const fetchGnameByGid = async (gid) => {
  try {
    const response = await http.get(`/goods/get_by_id/${gid}`, {
      headers: {
        'Authorization': `${token}`,
      }
    });
    return {
      gname: response.data.gname,
      gseller: response.data.gseller  // 新增返回商家信息
    };
  } catch (error) {
    console.error('Error fetching gname:', error);
    return { gname: '未知商品', gseller: '' };  // 修改返回结构
  }
};
// 分页函数
const paginate = (data) => {
  const startIndex = (pageNum.value - 1) * pageSize.value;
  const endIndex = startIndex + pageSize.value;
  tableData.value = data.slice(startIndex, endIndex);
};

// 分页事件处理函数
const handleSizeChange = (val) => {
  pageSize.value = val;
  paginate(allData.value);
};

const handleCurrentChange = (val) => {
  pageNum.value = val;
  paginate(allData.value);
};

// 取消订单的方法
const canselOrder = async (order) => {
  try {
    const claims = jwtDecode(token);
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
      ElMessage.success("订单已取消");
      await fetchOrders();
    } else {
      ElMessage.error(response.data.message || '操作失败');
    }
  } catch (error) {
    ElMessage.error('操作失败，请稍后重试');
  }
};

// 处理行点击事件
const handleRowClick = async (row) => {
  try {
    const data = {
      oid: row.oid
    }
    console.log("订单号 :",row.oid)
    const response = await http.post(`/expressorder/list`, data, {
      headers: {Authorization: token}
    });
    expressRecords.value = response.data;
    expressRecordDialogVisible.value = true;
    console.log("物流记录 :",expressRecords.value)
    // 初始化状态更新表单
    initStatusUpdateForm(row.oid);
  } catch (error) {
    console.error('获取物流记录失败:', error);
    ElMessage.error('获取物流记录失败');
  }
};

// 初始化状态更新表单
const initStatusUpdateForm = (oid) => {
  const latestRecord = expressRecords.value.reduce((prev, current) =>
    prev.eoid > current.eoid ? prev : current, {eostate: -1});

  latestState.value = latestRecord.eostate;
  statusForm.value = {
    oid: oid,
    eostate: latestRecord.eostate,
    eoaddress: ''
  };

  availableStates.value = [latestRecord.eostate];
  if (latestRecord.eostate < 5) {
    availableStates.value.push(latestRecord.eostate + 1);
  }
};

// 提交状态更新
const submitStatusUpdate = async () => {
  if (!statusForm.value.eoaddress) {
    ElMessage.warning('请输入地点信息');
    return;
  }

  try {
    const newRecord = {
      oid: statusForm.value.oid,
      eostate: statusForm.value.eostate,
      eoaddress: statusForm.value.eoaddress,
      eotime: new Date().toISOString()
    };

    const response = await http.post('/expressorder/create', newRecord, {
      headers: {Authorization: token}
    });

    if (response.data) {
      ElMessage.success('物流状态已更新');
      await handleRowClick({oid: statusForm.value.oid});
    }
  } catch (error) {
    console.error('更新物流状态失败:', error);
    ElMessage.error('更新失败，请稍后重试');
  }
};

// 初始化加载订单数据
onMounted(() => {
  fetchOrders();
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

.status-0 { /* 待揽件 */
  background-color: #f0f0f0;
  color: #666;
  border: 1px solid #ddd;
}

.status-1, .status-3 { /* 已揽收 派送中 */
  background-color: #e6f7ff;
  color: #1890ff;
  border: 1px solid #91d5ff;
}

.status-2 { /* 运输中 */
  background-color: #fff7e6;
  color: #fa8c16;
  border: 1px solid #ffd591;
}

.status-4 { /* 待取件 */
  background-color: #fffbe6;
  color: #fadb14;
  border: 1px solid #ffe58f;
}

.status-5 { /* 已签收 */
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

.status-completed {
  color: #67c23a;
  font-weight: 500;
}

.status-upcompleted {
  color: #df2a59;
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

  /* 物流列样式 */
  .el-table .express-cell {
    .el-button {
      padding: 5px 10px;
      font-size: 12px;
    }
  }
}
</style>
