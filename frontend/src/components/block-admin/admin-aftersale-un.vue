<template>
  <div class="content-wrapper">
    <el-scrollbar class="scrollbar-container">
      <el-table :data="tableData" class="custom-table-row" style="width: 100%">
        <el-table-column :label="$t(isTranslated ? 'afterSales.id_en' : 'afterSales.id_zh')" prop="afid" />
        <el-table-column :label="$t(isTranslated ? 'order.orderId_en' : 'order.orderId_zh')" prop="oid" />
        <el-table-column :label="$t(isTranslated ? 'afterSales.name_en' : 'afterSales.name_zh')" prop="afname" />
        <el-table-column :label="$t(isTranslated ? 'order.messages.time_en' : 'order.messages.time_zh')" prop="aftime">
          <template #default="{ row }">
            {{ formatDateTime(row.aftime) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t(isTranslated ? 'order.operations_en' : 'order.operations_zh')">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewDetails(scope.row)">
              {{ $t(isTranslated ? 'order.actions.view_en' : 'order.actions.view_zh') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-scrollbar>

    <!-- Detail Dialog -->
    <el-dialog
      v-model="dialogVisible"
      :title="$t(isTranslated ? 'afterSales.detail_en' : 'afterSales.detail_zh')"
      width="800px"
    >
      <el-form :label-width="isTranslated ? '150px' : '120px'">
        <el-form-item :label="$t(isTranslated ? 'afterSales.id_en' : 'afterSales.id_zh') + '：'">
          <el-input v-model="currentAfterSale.afid" readonly />
        </el-form-item>
        <el-form-item :label="$t(isTranslated ? 'order.orderId_en' : 'order.orderId_zh') + '：'">
          <el-input v-model="currentAfterSale.oid" readonly />
        </el-form-item>
        <el-form-item :label="$t(isTranslated ? 'afterSales.name_en' : 'afterSales.name_zh') + '：'">
          <el-input v-model="currentAfterSale.afname" readonly />
        </el-form-item>
        <el-form-item :label="$t(isTranslated ? 'order.messages.time_en' : 'order.messages.time_zh') + '：'">
          <el-input :value="formatDateTime(currentAfterSale.aftime)" readonly />
        </el-form-item>
        <el-form-item :label="$t(isTranslated ? 'afterSales.reason_en' : 'afterSales.reason_zh') + '：'">
          <div v-html="currentAfterSale.aftext" style="border: 1px solid #ebeef5; padding: 10px; border-radius: 4px; min-height: 100px;"></div>
        </el-form-item>
        <el-form-item :label="$t(isTranslated ? 'order.status_en' : 'order.status_zh') + '：'">
          <el-select
            v-model="currentAfterSale.afstate"
            :placeholder="$t(isTranslated ? 'afterSales.pending_en' : 'afterSales.pending_zh')"
            style="width: 100%"
          >
            <el-option
              v-for="item in stateOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">
            {{ $t(isTranslated ? 'order.actions.cancel_en' : 'order.actions.cancel_zh') }}
          </el-button>
          <el-button type="primary" @click="handleConfirm">
            {{ $t(isTranslated ? 'order.actions.confirm_en' : 'order.actions.confirm_zh') }}
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>

  <div class="pagination-container">
    <el-pagination
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
import { ref, onMounted } from "vue";
import { useI18n } from 'vue-i18n';
import { ElMessage } from "element-plus";
import {jwtDecode} from "jwt-decode";
import http from "@/utils/re.js";

const { t } = useI18n();
const props = defineProps({
  isTranslated: {
    type: Boolean,
    default: false
  }
});

const token = localStorage.getItem("token");
const claims = jwtDecode(token);
const dialogVisible = ref(false);
const currentAfterSale = ref({});

const stateOptions = ref([
  { value: 1, label: t(props.isTranslated ? 'afterSales.pending_en' : 'afterSales.pending_zh') },
  { value: 2, label: t(props.isTranslated ? 'afterSales.exchange_en' : 'afterSales.exchange_zh') },
  { value: 3, label: t(props.isTranslated ? 'afterSales.refundOnly_en' : 'afterSales.refundOnly_zh') },
  { value: 4, label: t(props.isTranslated ? 'afterSales.returnAndRefund_en' : 'afterSales.returnAndRefund_zh') },
  { value: 5, label: t(props.isTranslated ? 'afterSales.merchantRejected_en' : 'afterSales.merchantRejected_zh') },
]);

const handleConfirm = async () => {
  try {
    const response = await http.put(
      "/aftersale/update",
      {
        afid: currentAfterSale.value.afid,
        afstate: currentAfterSale.value.afstate,
      },
      {
        headers: { Authorization: token },
      }
    );

    if (response.data) {
      ElMessage.success(t(props.isTranslated ? 'order.messages.updateSuccess_en' : 'order.messages.updateSuccess_zh'));
      await fetchAfterSales();
      dialogVisible.value = false;
    }
  } catch (error) {
    console.error(t(props.isTranslated ? 'order.messages.updateFailed_en' : 'order.messages.updateFailed_zh'), error);
    ElMessage.error(t(props.isTranslated ? 'order.messages.updateFailed_en' : 'order.messages.updateFailed_zh'));
  }
};

const viewDetails = (row) => {
  currentAfterSale.value = { ...row };
  dialogVisible.value = true;
};

const formatDateTime = (timestamp) => {
  if (!timestamp) return "";
  return new Date(timestamp)
    .toLocaleString(props.isTranslated ? 'en-US' : 'zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
      hour12: false,
    })
    .replace(/\//g, "-");
};

const allData = ref([]);
const tableData = ref([]);
const pageSize = ref(10);
const pageNum = ref(1);
const total = ref(0);

const fetchAfterSales = async () => {
  try {
    const afsale = {
      afstate: 1,
    }
    const response = await http.post("/aftersale/list",
      afsale,
      { headers: { Authorization: `${token}` } }
    );

    // Filter data by seller
    const filteredData = [];
    for (const afterSale of response.data) {
      const data = {
        oid: afterSale.oid,
      }
      const orderRes = await http.post(`/order/list`, data, {
        headers: { Authorization: token }
      });
      const data2 = {
        gid: orderRes.data[0].gid,
      }
      const goodsRes = await http.post(`/goods/list`, data2, {
        headers: { Authorization: token }
      });
      if (goodsRes.data[0].gseller === claims.account) {
        filteredData.push(afterSale);
      }
    }

    allData.value = filteredData;
    total.value = filteredData.length;
    paginate(filteredData);
  } catch (error) {
    console.error(t(props.isTranslated ? 'order.messages.fetchAfterSalesFailed_en' : 'order.messages.fetchAfterSalesFailed_zh'), error);
    ElMessage.error(t(props.isTranslated ? 'order.messages.fetchAfterSalesFailed_en' : 'order.messages.fetchAfterSalesFailed_zh'));
  }
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

onMounted(fetchAfterSales);
</script>

<style scoped>
.custom-table-row .el-table .el-table__body {
  --el-table-row-height: 60px;
}

.scrollbar-container {
  height: calc(100vh - 100px);
  overflow-y: auto;
}

.pagination-container {
  position: fixed;
  bottom: 0;
  width: 100%;
  background-color: #fff;
  z-index: 1000;
  padding: 15px 0;
  box-shadow: 0 -2px 12px rgba(0, 0, 0, 0.1);
}

.content-wrapper {
  padding-bottom: 60px;
}
</style>
