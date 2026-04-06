<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="课程ID" prop="courseId">
        <el-input
          v-model="queryParams.courseId"
          placeholder="请输入课程ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="大纲ID" prop="syllabusId">
        <el-input
          v-model="queryParams.syllabusId"
          placeholder="请输入大纲ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审查ID" prop="reviewId">
        <el-input
          v-model="queryParams.reviewId"
          placeholder="请输入审查ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="成绩单ID" prop="scoreId">
        <el-input
          v-model="queryParams.scoreId"
          placeholder="请输入成绩单ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分析日期" prop="analysisDate">
        <el-date-picker clearable
          v-model="queryParams.analysisDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择分析日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['analysis:analysis:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['analysis:analysis:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['analysis:analysis:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['analysis:analysis:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="analysisList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="分析ID" align="center" prop="analysisId" />
      <el-table-column label="课程ID" align="center" prop="courseId" />
      <el-table-column label="大纲ID" align="center" prop="syllabusId" />
      <el-table-column label="审查ID" align="center" prop="reviewId" />
      <el-table-column label="成绩单ID" align="center" prop="scoreId" />
      <el-table-column label="分析日期" align="center" prop="analysisDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.analysisDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['analysis:analysis:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['analysis:analysis:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改达成度分析对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课程ID" prop="courseId">
          <el-input v-model="form.courseId" placeholder="请输入课程ID" />
        </el-form-item>
        <el-form-item label="大纲ID" prop="syllabusId">
          <el-input v-model="form.syllabusId" placeholder="请输入大纲ID" />
        </el-form-item>
        <el-form-item label="审查ID" prop="reviewId">
          <el-input v-model="form.reviewId" placeholder="请输入审查ID" />
        </el-form-item>
        <el-form-item label="成绩单ID" prop="scoreId">
          <el-input v-model="form.scoreId" placeholder="请输入成绩单ID" />
        </el-form-item>
        <el-form-item label="分析日期" prop="analysisDate">
          <el-date-picker clearable
            v-model="form.analysisDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择分析日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAnalysis, getAnalysis, delAnalysis, addAnalysis, updateAnalysis } from "@/api/analysis/analysis"

export default {
  name: "Analysis",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 达成度分析表格数据
      analysisList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseId: null,
        syllabusId: null,
        reviewId: null,
        scoreId: null,
        analysisDate: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        courseId: [
          { required: true, message: "课程ID不能为空", trigger: "blur" }
        ],
        syllabusId: [
          { required: true, message: "大纲ID不能为空", trigger: "blur" }
        ],
        reviewId: [
          { required: true, message: "审查ID不能为空", trigger: "blur" }
        ],
        scoreId: [
          { required: true, message: "成绩单ID不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询达成度分析列表 */
    getList() {
      this.loading = true
      listAnalysis(this.queryParams).then(response => {
        this.analysisList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        analysisId: null,
        courseId: null,
        syllabusId: null,
        reviewId: null,
        scoreId: null,
        analysisDate: null,
        status: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.analysisId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加达成度分析"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const analysisId = row.analysisId || this.ids
      getAnalysis(analysisId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改达成度分析"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.analysisId != null) {
            updateAnalysis(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addAnalysis(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const analysisIds = row.analysisId || this.ids
      this.$modal.confirm('是否确认删除达成度分析编号为"' + analysisIds + '"的数据项？').then(function() {
        return delAnalysis(analysisIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('analysis/analysis/export', {
        ...this.queryParams
      }, `analysis_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
