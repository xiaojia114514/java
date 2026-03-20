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
          prop="afname"
          :label="$t(isTranslated ? 'order.problemTitle_en' : 'order.problemTitle_zh')"
        />
        <el-table-column
          prop="aftext"
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
          <template #default="{ row }">
            {{ formatDateTime(row.aftime) }}
          </template>
        </el-table-column>
        <el-table-column
          :label="$t(isTranslated ? 'order.status_en' : 'order.status_zh')"
        >
          <template #default="scope">
            <span
              class="status-processing"
              v-if="scope.row.afstate === 1"
            >
              {{ $t(isTranslated ? 'order.statuses.processing_en' : 'order.statuses.processing_zh') }}
            </span>
            <span
              class="status-final"
              v-else
            >
              {{ $t(isTranslated ? 'order.statuses.completed_en' : 'order.statuses.completed_zh') }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          :label="$t(isTranslated ? 'order.operations_en' : 'order.operations_zh')"
        >
          <template #default="scope">
            <div style="display: flex; gap: 8px;">
              <el-button
                type="primary"
                size="small"
                @click="handleEdit(scope.row)"
              >
                {{ $t(isTranslated ? 'order.actions.edit_en' : 'order.actions.edit_zh') }}
              </el-button>
              <el-button
                type="danger"
                size="small"
                @click="handleCancel(scope.row)"
                v-if="scope.row.afstate === 1"
              >
                {{ $t(isTranslated ? 'order.actions.cancel_en' : 'order.actions.cancel_zh') }}
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-scrollbar>

    <!-- 编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="$t(isTranslated ? 'order.editAppeal_en' : 'order.editAppeal_zh')"
      width="800px"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item :label="$t(isTranslated ? 'order.orderId_en' : 'order.orderId_zh')">
          <el-input v-model="formData.oid" readonly />
        </el-form-item>
        <el-form-item :label="$t(isTranslated ? 'order.problemTitle_en' : 'order.problemTitle_zh')">
          <el-input v-model="formData.afname" readonly />
        </el-form-item>
        <el-form-item :label="$t(isTranslated ? 'order.appealTime_en' : 'order.appealTime_zh')">
          <el-input :value="formatDateTime(formData.aftime)" readonly />
        </el-form-item>
        <el-form-item :label="$t(isTranslated ? 'order.status_en' : 'order.status_zh')">
          <el-input
            :value="formData.afstate === 1
              ? $t(isTranslated ? 'order.statuses.processing_en' : 'order.statuses.processing_zh')
              : $t(isTranslated ? 'order.statuses.completed_en' : 'order.statuses.completed_zh')"
            readonly
          />
        </el-form-item>
        <el-form-item
          :label="$t(isTranslated ? 'order.problemDetail_en' : 'order.problemDetail_zh')"
          prop="aftext"
        >
          <UpdateRichText
            :content="formData.aftext"
            :onContentChange="updateDescription"
          ></UpdateRichText>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">
            {{ $t(isTranslated ? 'order.actions.cancel_en' : 'order.actions.cancel_zh') }}
          </el-button>
          <el-button
            type="primary"
            @click="handleSubmit"
          >
            {{ $t(isTranslated ? 'order.actions.submit_en' : 'order.actions.submit_zh') }}
          </el-button>
        </span>
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
import { ref, onMounted } from "vue";
import { useI18n } from 'vue-i18n';
import axios from "axios";
import { jwtDecode } from "jwt-decode";
import { ElMessage, ElMessageBox } from "element-plus";
import UpdateRichText from "@/components/block-rich/UpdateRichText.vue";
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
const dialogVisible = ref(false);
const formRef = ref(null);
const formData = ref({
  afid: "",
  oid: "",
  afname: "",
  aftime: "",
  afstate: "",
  aftext: "",
});
const formRules = {
  aftext: [
    {
      required: true,
      message: t(props.isTranslated ? 'order.messages.descriptionRequired_en' : 'order.messages.descriptionRequired_zh'),
      trigger: "blur"
    },
    {
      min: 1,
      message: t(props.isTranslated ? 'order.messages.descriptionNotBlank_en' : 'order.messages.descriptionNotBlank_zh'),
      trigger: "blur"
    },
  ],
};
const token = localStorage.getItem("token");
let account = "";
const currentContent = ref("");

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

const fetchAfterSales = async (state) => {
  try {
    const response = await http.post(
      "/aftersale/list_by_account",
      {
        account: account,
        state: state,
      },
      {
        headers: { Authorization: token },
      }
    );

    allData.value = response.data;
    total.value = allData.value.length;
    paginate();
  } catch (error) {
    console.error("获取售后记录失败:", error);
    ElMessage.error(
      t(props.isTranslated ? 'order.messages.fetchAfterSalesFailed_en' : 'order.messages.fetchAfterSalesFailed_zh')
    );
  }
};

const handleEdit = (row) => {
  formData.value = { ...row };
  currentContent.value = row.aftext;
  dialogVisible.value = true;
};

const updateDescription = (content) => {
  formData.value.aftext = content;
};

const handleSubmit = async () => {
  try {
    const valid = await formRef.value.validate();
    if (!valid) return;

    await ElMessageBox.confirm(
      t(props.isTranslated ? 'order.messages.confirmSubmit_en' : 'order.messages.confirmSubmit_zh'),
      t(props.isTranslated ? 'order.messages.confirmTitle_en' : 'order.messages.confirmTitle_zh'),
      {
        confirmButtonText: t(props.isTranslated ? 'order.actions.confirm_en' : 'order.actions.confirm_zh'),
        cancelButtonText: t(props.isTranslated ? 'order.actions.cancel_en' : 'order.actions.cancel_zh'),
        type: "warning",
      }
    );

    const response = await http.post(
      "/aftersale/update",
      formData.value,
      {
        headers: { Authorization: token },
      }
    );

    if (response.data) {
      ElMessage.success(
        t(props.isTranslated ? 'order.messages.updateSuccess_en' : 'order.messages.updateSuccess_zh')
      );
      dialogVisible.value = false;
      await fetchAfterSales(1);
    }
  } catch (error) {
    if (error === "cancel") {
      ElMessage.info(
        t(props.isTranslated ? 'order.messages.updateCancelled_en' : 'order.messages.updateCancelled_zh')
      );
    } else {
      console.error("更新失败:", error);
      ElMessage.error(
        error.response?.data?.message ||
        t(props.isTranslated ? 'order.messages.updateFailed_en' : 'order.messages.updateFailed_zh')
      );
    }
  }
};

const handleCancel = async (row) => {
  try {
    await ElMessageBox.confirm(
      t(props.isTranslated ? 'order.messages.confirmCancel_en' : 'order.messages.confirmCancel_zh'),
      t(props.isTranslated ? 'order.messages.warning_en' : 'order.messages.warning_zh'),
      {
        confirmButtonText: t(props.isTranslated ? 'order.actions.confirm_en' : 'order.actions.confirm_zh'),
        cancelButtonText: t(props.isTranslated ? 'order.actions.cancel_en' : 'order.actions.cancel_zh'),
        type: "warning",
      }
    );

    const response = await http.delete(
      `/aftersale/delete/${row.afid}`,
      {
        headers: { Authorization: token },
      }
    );

    if (response.data) {
      ElMessage.success(
        t(props.isTranslated ? 'order.messages.cancelSuccess_en' : 'order.messages.cancelSuccess_zh')
      );
      await fetchAfterSales(1);
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("撤销失败:", error);
      ElMessage.error(
        error.response?.data?.message ||
        t(props.isTranslated ? 'order.messages.cancelFailed_en' : 'order.messages.cancelFailed_zh')
      );
    }
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

onMounted(() => {
  if (token) {
    const claims = jwtDecode(token);
    account = claims.account;
    fetchAfterSales(1);
  } else {
    window.location.href = "/";
  }
});
</script>

<style scoped>
.status-processing {
  color: #e6a23c;
  font-weight: 500;
}

.status-final {
  color: #909399;
  font-style: italic;
}

.content-wrapper {
  height: calc(100vh - 180px);
  padding: 20px;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.pagination-container {
  padding: 20px;
  background: #ffffff;
  text-align: center;
}

.el-table-column__cell {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
</style>
