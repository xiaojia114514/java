<template>
  <div class="content-wrapper">
    <el-scrollbar class="scrollbar-container">
      <el-table :data="tableData" class="custom-table-row" style="width: 100%">
        <el-table-column
          prop="afid"
          :label="$t(isTranslated ? 'afterSales.id_en' : 'afterSales.id_zh')"
        />
        <el-table-column
          prop="oid"
          :label="$t(isTranslated ? 'order.orderId_en' : 'order.orderId_zh')"
        />
        <el-table-column
          prop="afname"
          :label="$t(isTranslated ? 'afterSales.name_en' : 'afterSales.name_zh')"
        />
        <el-table-column prop="aftime" :label="$t(isTranslated ? 'order.appealTime_en' : 'order.appealTime_zh')">
          <template #default="{ row }">
            {{ formatDateTime(row.aftime) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t(isTranslated ? 'order.status_en' : 'order.status_zh')">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.afstate)">
              {{ getStatusText(row.afstate) }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-scrollbar>
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
import { ref, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import { ElMessage } from 'element-plus';
import { jwtDecode } from 'jwt-decode';
import http from '@/utils/re.js';

const { t } = useI18n();

const props = defineProps({
  isTranslated: {
    type: Boolean,
    default: false,
  },
});

const allData = ref([]);
const tableData = ref([]);
const pageSize = ref(10);
const pageNum = ref(1);
const total = ref(0);
const token = localStorage.getItem('token');
const claims = jwtDecode(token);

// 状态显示方法
const getStatusText = (state) => {
  const statusMap = {
    2: t(props.isTranslated ? 'afterSales.exchange_en' : 'afterSales.exchange_zh'),
    3: t(props.isTranslated ? 'afterSales.refundOnly_en' : 'afterSales.refundOnly_zh'),
    4: t(props.isTranslated ? 'afterSales.returnAndRefund_en' : 'afterSales.returnAndRefund_zh'),
    5: t(props.isTranslated ? 'afterSales.merchantRejected_en' : 'afterSales.merchantRejected_zh'),
  };
  return statusMap[state] || t(props.isTranslated ? 'common.none_en' : 'common.none_zh');
};

// 状态标签样式
const getStatusType = (state) => {
  switch (state) {
    case 2: return 'warning';
    case 3: return 'success';
    case 4: return 'info';
    case 5: return 'danger';
    default: return 'info';
  }
};

// 格式化日期时间
const formatDateTime = (dateString) => {
  return new Date(dateString)
    .toLocaleString(props.isTranslated ? 'en-US' : 'zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
      hour12: false,
    })
    .replace(/\//g, '-');
};

const fetchAfterSales = async () => {
  try {
    const afsale = { afstate: "" };
    const response = await http.post('/aftersale/list', afsale, {
      headers: { Authorization: `${token}` },
    });
    console.log(response.data);
    const filteredData = [];
    for (const afterSale of response.data) {
      if (afterSale.afstate < 2 || afterSale.afstate > 5) {
        continue; // 跳过不符合条件的记录
      }
      const data = { oid: afterSale.oid };
      const orderRes = await http.post('/order/list', data, {
        headers: { Authorization: token },
      });

      const data2 = { gid: orderRes.data[0]?.gid };
      const goodsRes = await http.post('/goods/list', data2, {
        headers: { Authorization: token },
      });

      if (goodsRes.data[0]?.gseller === claims.account) {
        filteredData.push(afterSale);
      }
    }

    allData.value = filteredData;
    total.value = filteredData.length;
    paginate(filteredData);
  } catch (error) {
    console.error('获取售后订单失败:', error);
    ElMessage.error(
      t(props.isTranslated ? 'order.messages.fetchAfterSalesFailed_en' : 'order.messages.fetchAfterSalesFailed_zh')
    );
  }
};

// 分页方法
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
/* 保持原有样式不变 */
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
