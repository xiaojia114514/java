<template>
  <div class="content-wrapper">
    <el-scrollbar class="scrollbar-container">
      <div style="display: flex; justify-content: flex-start; align-items: flex-start; margin-top: 20px;">
        <el-button type="primary" @click="addNewLabel">
          {{ $t(isTranslated ? 'order.actions.add_en' : 'order.actions.add_zh') }}
        </el-button>
      </div>
      <el-table :data="tableData" class="custom-table-row" style="width: 100%">
        <el-table-column
          prop="cid"
          :label="$t(isTranslated ? 'category.id_en' : 'category.id_zh')"
        />
        <el-table-column
          prop="cname"
          :label="$t(isTranslated ? 'category.name_en' : 'category.name_zh')"
        />
        <el-table-column
          prop="cparentid"
          :label="$t(isTranslated ? 'category.parentId_en' : 'category.parentId_zh')"
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
import { useRouter } from 'vue-router';
import http from '@/utils/re.js';
import {ElMessage} from "element-plus";

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
const router = useRouter();

const fetchProducts = async () => {
  try {
    const response = await http.get('/cat/list', {
      headers: {
        Authorization: `${token}`,
      },
    });
    allData.value = response.data;
    total.value = allData.value.length;
    paginate(allData.value);
  } catch (error) {
    console.error('获取分类信息失败:', error);
    ElMessage.error(
      t(props.isTranslated ? 'messages.fetchCategoriesFailed_en' : 'messages.fetchCategoriesFailed_zh')
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

onMounted(fetchProducts);

const addNewLabel = () => {
  router.push({ name: 'NewLabel' });
};
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
  padding: 15px 0;
  box-shadow: 0 -2px 12px rgba(0, 0, 0, 0.1);
}

.content-wrapper {
  padding-bottom: 60px;
}
</style>
