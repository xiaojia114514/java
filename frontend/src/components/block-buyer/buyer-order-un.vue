<template>
  <div class="content-wrapper">
    <el-scrollbar class="scrollbar-container">
      <el-table :data="tableData" class="custom-table-row" style="width: 100%">
        <el-table-column
          :label="$t(isTranslated ? 'order.orderId_en' : 'order.orderId_zh')"
          prop="oid"
        ></el-table-column>
        <el-table-column
          :label="$t(isTranslated ? 'order.productId_en' : 'order.productId_zh')"
          prop="gid"
        ></el-table-column>
        <el-table-column
          :label="$t(isTranslated ? 'order.productName_en' : 'order.productName_zh')"
          prop="gname"
        ></el-table-column>
        <el-table-column
          :label="$t(isTranslated ? 'order.quantity_en' : 'order.quantity_zh')"
          prop="onumber"
        ></el-table-column>
        <el-table-column
          :label="$t(isTranslated ? 'order.phone_en' : 'order.phone_zh')"
          prop="ophone"
        ></el-table-column>
        <el-table-column
          :label="$t(isTranslated ? 'order.address_en' : 'order.address_zh')"
          prop="oaddress"
        ></el-table-column>
        <el-table-column
          :label="$t(isTranslated ? 'order.remark_en' : 'order.remark_zh')"
          prop="oremark"
        ></el-table-column>
        <el-table-column
          :label="$t(isTranslated ? 'order.operations_en' : 'order.operations_zh')"
        >
          <template #default="scope">
            <el-dialog
              :title="$t(isTranslated ? 'translate.buyer.edit_en' : 'translate.buyer.edit_zh')"
              v-model="dialogVisible"
              width="30%"
              :modal="false"
              :show-close="false"
              @close="resetForm"
            >
              <el-form
                :model="orderForm"
                :rules="rules"
                ref="formRef"
              >
                <el-form-item
                  :label="$t(isTranslated ? 'order.phone_en' : 'order.phone_zh')"
                  prop="ophone"
                >
                  <el-input v-model="orderForm.ophone"></el-input>
                </el-form-item>
                <el-form-item
                  :label="$t(isTranslated ? 'order.address_en' : 'order.address_zh')"
                  prop="oaddress"
                >
                  <el-input v-model="orderForm.oaddress"></el-input>
                </el-form-item>
                <el-form-item
                  :label="$t(isTranslated ? 'order.quantity_en' : 'order.quantity_zh')"
                  prop="onumber"
                >
                  <el-input v-model="orderForm.onumber"></el-input>
                </el-form-item>
                <el-form-item
                  :label="$t(isTranslated ? 'order.remark_en' : 'order.remark_zh')"
                >
                  <el-input
                    type="textarea"
                    v-model="orderForm.oremark"
                  ></el-input>
                </el-form-item>
              </el-form>
              <template v-slot:footer>
                <span class="dialog-footer">
                  <el-button
                    type="primary"
                    @click="submitOrder"
                  >
                    {{ $t(isTranslated ? 'order.actions.submit_en' : 'order.actions.submit_zh') }}
                  </el-button>
                </span>
              </template>
            </el-dialog>
            <el-button
              type="primary"
              size="small"
              @click="editOrder(scope.row)"
            >
              {{ $t(isTranslated ? 'order.actions.edit_en' : 'order.actions.edit_zh') }}
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
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { jwtDecode } from "jwt-decode";
import { ElForm, ElMessage } from "element-plus";
import http from "@/utils/re.js";
import { useI18n } from 'vue-i18n';

const { t: $t } = useI18n();

// 从父组件接收 isTranslated 属性
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
let account = '';

// 根据语言设置验证规则提示
const rules = ref({
  ophone: [
    {
      required: true,
      message: props.isTranslated ? 'Phone is required' : '电话不能为空',
      trigger: 'blur'
    }
  ],
  oaddress: [
    {
      required: true,
      message: props.isTranslated ? 'Address is required' : '地址不能为空',
      trigger: 'blur'
    }
  ]
});

const formRef = ref(null);

const fetchAllOrders = async () => {
  try {
    const response = await http.get('/order/list', {
      headers: {
        'Authorization': `${token}`,
      }
    });
    const orders = response.data.filter(item => item.ostate === 0 && item.uaccount === account);
    allData.value = await Promise.all(orders.map(async (order) => {
      const gname = await fetchGnameByGid(order.gid);
      return {...order, gname};
    }));
    total.value = allData.value.length;
    paginate();
  } catch (error) {
    console.error('Error fetching orders:', error);
    ElMessage.error(props.isTranslated ? 'Failed to load orders' : '加载订单失败');
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
    console.error('Error fetching gname:', error);
    return props.isTranslated ? 'Unknown product' : '未知商品';
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

const dialogVisible = ref(false);
const orderForm = ref({
  ophone: '',
  oaddress: '',
  onumber: '',
  oremark: ''
});

const editOrder = (row) => {
  orderForm.value = {
    ostate: 0,
    oid: row.oid,
    ophone: row.ophone,
    oaddress: row.oaddress,
    oremark: row.oremark,
    onumber: row.onumber
  };
  dialogVisible.value = true;
};

const submitOrder = async () => {
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const response = await http.post('/order/update', orderForm.value, {
          headers: {
            'Authorization': `${token}`,
          }
        });
        if (response.data.message === "修改成功") {
          await fetchAllOrders();
          ElMessage.success(props.isTranslated ? 'Order updated successfully' : '订单更新成功');
          dialogVisible.value = false;
        } else {
          ElMessage.error(props.isTranslated ? 'Failed to update order' : '订单更新失败');
        }
      } catch (error) {
        console.error('Error updating order:', error);
        ElMessage.error(props.isTranslated ? 'Operation failed' : '操作失败');
      }
    } else {
      ElMessage.error(props.isTranslated ? 'Please fill in required fields' : '请填写必填字段');
    }
  });
};

const resetForm = () => {
  orderForm.value = {
    ophone: '',
    oaddress: '',
    onumber: '',
    oremark: ''
  };
};

onMounted(() => {
  fetchAllOrders();
  if (token) {
    const claims = jwtDecode(token);
    account = claims.account;
  } else {
    this.$router.push({ path: '/' });
  }
});
</script>

<style scoped>
/* 样式保持不变 */
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

.custom-table-row :deep(.el-table__row) {
  height: 65px;
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
