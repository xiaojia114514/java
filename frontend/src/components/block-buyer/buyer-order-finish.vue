<template>
  <div class="content-wrapper">
    <el-scrollbar class="scrollbar-container">
      <el-table
        :data="tableData"
        class="custom-table-row"
        style="width: 100%"
        @row-click="handleRowClick"
      >
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
            <div style="display: flex; align-items: center">
              <span v-if="scope.row.ostate === -1" class="status-canceled">
                {{ $t(isTranslated ? 'order.statuses.canceled_en' : 'order.statuses.canceled_zh') }}
              </span>
              <span v-if="scope.row.ostate === 2" class="status-completed">
                {{ $t(isTranslated ? 'order.statuses.completed_en' : 'order.statuses.completed_zh') }}
              </span>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-scrollbar>

    <!-- 订单详情对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="$t(isTranslated ? 'order.orderDetail_en' : 'order.orderDetail_zh')"
      width="600px"
    >
      <el-descriptions :column="1" border>
        <el-descriptions-item :label="$t(isTranslated ? 'order.orderId_en' : 'order.orderId_zh')">
          {{ currentOrder.oid }}
        </el-descriptions-item>
        <el-descriptions-item :label="$t(isTranslated ? 'order.productId_en' : 'order.productId_zh')">
          {{ currentOrder.gid }}
        </el-descriptions-item>
        <el-descriptions-item :label="$t(isTranslated ? 'order.productName_en' : 'order.productName_zh')">
          {{ currentOrder.gname }}
        </el-descriptions-item>
        <el-descriptions-item :label="$t(isTranslated ? 'order.quantity_en' : 'order.quantity_zh')">
          {{ currentOrder.onumber }}
        </el-descriptions-item>
        <el-descriptions-item :label="$t(isTranslated ? 'order.phone_en' : 'order.phone_zh')">
          {{ currentOrder.ophone }}
        </el-descriptions-item>
        <el-descriptions-item :label="$t(isTranslated ? 'order.address_en' : 'order.address_zh')">
          {{ currentOrder.oaddress }}
        </el-descriptions-item>
        <el-descriptions-item :label="$t(isTranslated ? 'order.remark_en' : 'order.remark_zh')">
          {{ currentOrder.oremark || $t(isTranslated ? 'common.none_en' : 'common.none_zh') }}
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">
            {{ $t(isTranslated ? 'order.actions.close_en' : 'order.actions.close_zh') }}
          </el-button>
          <el-button
            v-if="currentOrder.ostate === 2"
            type="primary"
            @click="showAfterSaleForm"
          >
            {{ $t(isTranslated ? 'order.actions.applyAfterSale_en' : 'order.actions.applyAfterSale_zh') }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 售后申请对话框 -->
    <el-dialog
      v-model="afDialogVisible"
      :title="$t(isTranslated ? 'order.actions.applyAfterSale_en' : 'order.actions.applyAfterSale_zh')"
      width="800px"
    >
      <el-form :model="afForm" label-width="80px" :rules="rules" ref="afFormRef">
        <el-form-item :label="$t(isTranslated ? 'order.problemType_en' : 'order.problemType_zh')" prop="afname">
          <el-select v-model="afForm.afname" :placeholder="$t(isTranslated ? 'order.selectProblemType_en' : 'order.selectProblemType_zh')">
            <el-option
              :label="$t(isTranslated ? 'order.problemTypes.wrongItem_en' : 'order.problemTypes.wrongItem_zh')"
              value="货不对板/错拍"
            />
            <el-option
              :label="$t(isTranslated ? 'order.problemTypes.notNeeded_en' : 'order.problemTypes.notNeeded_zh')"
              value="不需要了"
            />
            <el-option
              :label="$t(isTranslated ? 'order.problemTypes.damaged_en' : 'order.problemTypes.damaged_zh')"
              value="运输损坏"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t(isTranslated ? 'order.problemDescription_en' : 'order.problemDescription_zh')" prop="aftext">
          <RichText :onContentChange="updateDescription"></RichText>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="afDialogVisible = false">
          {{ $t(isTranslated ? 'order.actions.cancel_en' : 'order.actions.cancel_zh') }}
        </el-button>
        <el-button type="primary" @click="submitAfterSale">
          {{ $t(isTranslated ? 'order.actions.submit_en' : 'order.actions.submit_zh') }}
        </el-button>
      </template>
    </el-dialog>
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
import { ref, reactive, onMounted, defineProps } from 'vue';
import axios from 'axios';
import { jwtDecode } from "jwt-decode";
import { ElMessage } from 'element-plus';
import RichText from '@/components/block-rich/RichText.vue';
import http from "@/utils/re.js";

// 定义props获取isTranslated
const props = defineProps({
  isTranslated: {
    type: Boolean,
    default: false
  }
});

// 数据相关
const allData = ref([]);
const tableData = ref([]);
const pageSize = ref(10);
const pageNum = ref(1);
const total = ref(0);
const token = localStorage.getItem('token');
let account = '';

// 对话框相关
const dialogVisible = ref(false);
const afDialogVisible = ref(false);
const currentOrder = ref({});
const afFormRef = ref(null);

// 售后表单
const afForm = reactive({
  afname: '',
  aftext: '',
  oid: '',
});

// 验证规则
const rules = reactive({
  afname: [{
    required: true,
    message: props.isTranslated ? 'Please select problem type' : '请选择问题类型',
    trigger: 'change'
  }],
});

// 获取订单数据
const fetchAllOrders = async () => {
  try {
    const response = await http.get('/order/list', {
      headers: {'Authorization': token}
    });

    const orders = response.data.filter(item =>
      item.uaccount === account && (item.ostate === 2 || item.ostate === -1)
    );

    const allDataWithGname = await Promise.all(orders.map(async (order) => {
      const gname = await fetchGnameByGid(order.gid);
      return {...order, gname};
    }));

    allData.value = allDataWithGname;
    total.value = allData.value.length;
    paginate();
  } catch (error) {
    console.error('获取订单失败:', error);
    ElMessage.error(props.isTranslated ? 'Failed to load orders' : '获取订单数据失败');
  }
};

// 获取商品名称
const fetchGnameByGid = async (gid) => {
  try {
    const data = {
      gid: gid,
    }
    const response = await http.post(`/goods/list`, data, {
      headers: {
        'Authorization': `${token}`,
      }
    });
    return response.data[0].gname;
  } catch (error) {
    console.error('获取商品名称失败:', error);
    return props.isTranslated ? 'Unknown product' : '未知商品';
  }
};

// 分页处理
const paginate = () => {
  const startIndex = (pageNum.value - 1) * pageSize.value;
  const endIndex = startIndex + pageSize.value;
  tableData.value = allData.value.slice(startIndex, endIndex);
};

// 分页事件
const handleSizeChange = (val) => {
  pageSize.value = val;
  paginate();
};

const handleCurrentChange = (val) => {
  pageNum.value = val;
  paginate();
};

// 行点击处理
const handleRowClick = (row) => {
  currentOrder.value = {...row};
  dialogVisible.value = true;
};

// 显示售后表单
const showAfterSaleForm = () => {
  afForm.oid = currentOrder.value.oid;
  dialogVisible.value = false;
  afDialogVisible.value = true;
};

// 更新富文本内容
const updateDescription = (content) => {
  afForm.aftext = content; // 更新afForm.aftext
};

// 提交售后申请
const submitAfterSale = async () => {
  try {
    await afFormRef.value.validate();

    const payload = {
      ...afForm,
      aftime: new Date().toISOString(),
      afstate: 1
    };

    const response = await http.post('/aftersale/creat', payload, {
      headers: {'Authorization': token}
    });

    if (response.data) {
      ElMessage.success(props.isTranslated ? 'After-sale application submitted' : '售后申请提交成功');
      afDialogVisible.value = false;
      afFormRef.value.resetFields();
    } else {
      ElMessage.error(props.isTranslated ? 'Submission failed' : '提交失败，请稍后重试');
    }
  } catch (error) {
    console.error('提交出错:', error);
    if (error.response) {
      ElMessage.error(props.isTranslated ? 'Submission failed' : '提交失败');
    } else {
      ElMessage.error(props.isTranslated ? 'Network error' : '网络错误，请检查连接');
    }
  }
};

// 初始化
onMounted(() => {
  if (token) {
    const claims = jwtDecode(token);
    account = claims.account;
    fetchAllOrders();
  } else {
    window.location.href = '/';
  }
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
}

.custom-table-row :deep(th) {
  font-weight: 600;
  color: #303133;
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
  padding: 16px 0;
  background: #ffffff;
  display: flex;
  justify-content: center;
}

.dialog-footer {
  display: flex;
  justify-content: space-between;
  padding: 10px 20px;
}

.el-descriptions {
  margin: 20px 0;
}

.el-select {
  width: 100%;
}

@media (max-width: 768px) {
  .content-wrapper {
    padding: 10px;
    height: calc(100vh - 160px);
  }

  .el-dialog {
    width: 90% !important;
  }
}
</style>
