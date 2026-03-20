<template>
  <div class="content-wrapper">
    <el-scrollbar class="scrollbar-container">
      <el-table :data="tableData" class="custom-table-row" style="width: 100%">
        <el-table-column
          prop="shid"
          :label="$t(isTranslated ? 'stockHistory.id_en' : 'stockHistory.id_zh')"
        />
        <el-table-column
          prop="shtime"
          :label="$t(isTranslated ? 'order.common.modifyTime_en' : 'order.common.modifyTime_zh')"
        >
          <template #default="{ row }">
            <span>{{ formatDateTime(row.shtime) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="shreason"
          :label="$t(isTranslated ? 'stockHistory.modifyReason_en' : 'stockHistory.modifyReason_zh')"
        >
          <template #default="{ row }">
            <span v-if="row.shreason === 1">{{ $t(isTranslated ? 'stockHistory.adminModify_en' : 'stockHistory.adminModify_zh') }}</span>
            <span v-else-if="row.shreason === 0">{{ $t(isTranslated ? 'stockHistory.orderComplete_en' : 'stockHistory.orderComplete_zh') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="gid"
          :label="$t(isTranslated ? 'order.productId_en' : 'order.productId_zh')"
        />
        <el-table-column
          prop="shstock_O"
          :label="$t(isTranslated ? 'stockHistory.originalStock_en' : 'stockHistory.originalStock_zh')"
        />
        <el-table-column
          prop="shstock_N"
          :label="$t(isTranslated ? 'stockHistory.newStock_en' : 'stockHistory.newStock_zh')"
        />
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

const formatDateTime = (dateTime) => {
  let date;
  if (typeof dateTime === 'string') {
    const regex = /(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2})/;
    const match = dateTime.match(regex);
    if (match) {
      date = new Date(match[1], match[2] - 1, match[3], match[4], match[5], match[6]);
    } else {
      date = new Date(dateTime);
    }
  } else {
    date = new Date(dateTime);
  }
  if (date.getTime()) {
    return date.toLocaleString(props.isTranslated ? 'en-US' : 'zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
      hour12: false,
    });
  } else {
    return dateTime;
  }
};

const fetchAllUsers = async () => {
  try {
    const response = await http.get('/history/list', {
      headers: {
        Authorization: `${token}`,
      },
    });
    allData.value = response.data;
    total.value = allData.value.length;
    paginate(allData.value);
  } catch (error) {
    console.error('Error fetching orders:', error);
    ElMessage.error(
      t(props.isTranslated ? 'messages.fetchStockHistoryFailed_en' : 'messages.fetchStockHistoryFailed_zh')
    );
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

onMounted(() => {
  fetchAllUsers();
});
</script>

<style scoped>
.custom-table-row .el-table .el-table__body {
  --el-table-row-height: 60px;
}

.scrollbar-container {
  height: calc(100vh - 60px);
  overflow-y: auto;
}

.pagination-container {
  position: fixed;
  bottom: 0;
  width: 100%;
  background-color: #fff;
  z-index: 1000;
}

.content-wrapper {
  padding-bottom: 60px;
}
</style>
