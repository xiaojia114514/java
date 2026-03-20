<template>
  <div class="content-wrapper">
    <el-scrollbar class="scrollbar-container">
      <el-table :data="tableData" class="custom-table-row" style="width: 100%">
        <el-table-column
          prop="oid"
          :label="$t(isTranslated ? 'order.orderId_en' : 'order.orderId_zh')"
        />
        <el-table-column
          prop="afname"
          :label="$t(isTranslated ? 'order.problemTitle_en' : 'order.problemTitle_zh')"
        />
        <el-table-column
          :label="$t(isTranslated ? 'order.problemDetail_en' : 'order.problemDetail_zh')"
        >
          <template #default="{ row }">
            <div v-html="row.aftext"></div>
          </template>
        </el-table-column>
        <el-table-column
          prop="aftime"
          :label="$t(isTranslated ? 'order.appealTime_en' : 'order.appealTime_zh')"
        >
          <template #default="{row}">
            {{
              new Date(row.aftime)
                .toLocaleString('zh-CN', {
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
        <el-table-column
          :label="$t(isTranslated ? 'order.status_en' : 'order.status_zh')"
        >
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
import { ref, onMounted } from "vue";
import { useI18n } from 'vue-i18n';
import axios from "axios";
import { jwtDecode } from "jwt-decode";
import { ElMessage } from "element-plus";
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
const token = localStorage.getItem("token");
let account = "";

// 状态文本映射
const getStatusText = (state) => {
  const statusMap = {
    2: t(props.isTranslated ? 'order.afterSales.exchange_en' : 'order.afterSales.exchange_zh'),
    3: t(props.isTranslated ? 'order.afterSales.refundOnly_en' : 'order.afterSales.refundOnly_zh'),
    4: t(props.isTranslated ? 'order.afterSales.returnAndRefund_en' : 'order.afterSales.returnAndRefund_zh'),
    5: t(props.isTranslated ? 'order.afterSales.merchantRejected_en' : 'order.afterSales.merchantRejected_zh')
  };
  return statusMap[state] || t(props.isTranslated ? 'order.common.none_en' : 'order.common.none_zh');
};

// 状态标签样式映射
const getStatusType = (state) => {
  switch (state) {
    case 2:
      return "warning";
    case 3:
      return "success";
    case 4:
      return "info";
    case 5:
      return "danger";
    default:
      return "info";
  }
};

// 格式化日期时间
const formatDateTime = (dateString) => {
  return new Date(dateString)
    .toLocaleString(props.isTranslated ? 'en-US' : 'zh-CN', {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
      hour12: false,
    })
    .replace(/\//g, "-");
};

// 获取售后记录
const fetchAfterSales = async () => {
  try {
    const response = await http.post(
      "/aftersale/list_by_account",
      {
        account: account,
      },
      {
        headers: { Authorization: token },
      }
    );

    allData.value = response.data.filter((item) => item.afstate !== 1);
    total.value = allData.value.length;
    paginate();
  } catch (error) {
    console.error("获取售后记录失败:", error);
    ElMessage.error(
      t(props.isTranslated ? 'order.messages.fetchAfterSalesFailed_en' : 'order.messages.fetchAfterSalesFailed_zh')
    );
  }
};

// 分页处理
const paginate = () => {
  const startIndex = (pageNum.value - 1) * pageSize.value;
  const endIndex = startIndex + pageSize.value;
  tableData.value = allData.value.slice(startIndex, endIndex);
};

// 分页事件处理
const handleSizeChange = (val) => {
  pageSize.value = val;
  paginate();
};

const handleCurrentChange = (val) => {
  pageNum.value = val;
  paginate();
};

// 页面加载时初始化数据
onMounted(() => {
  if (token) {
    const claims = jwtDecode(token);
    account = claims.account;
    fetchAfterSales();
  } else {
    window.location.href = "/";
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

/* 限制表格中显示的富文本内容高度 */
.el-table-column__cell {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
</style>
