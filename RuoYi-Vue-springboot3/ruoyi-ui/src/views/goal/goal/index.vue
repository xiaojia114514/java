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
      <el-form-item label="目标代码" prop="goalCode">
        <el-input
          v-model="queryParams.goalCode"
          placeholder="请输入目标代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="毕业要求" prop="graduationRequirement">
        <el-input
          v-model="queryParams.graduationRequirement"
          placeholder="请输入毕业要求"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="指标点" prop="indicatorPoint">
        <el-input
          v-model="queryParams.indicatorPoint"
          placeholder="请输入指标点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="目标重要程度" prop="goalLevel">
        <el-input
          v-model="queryParams.goalLevel"
          placeholder="请输入目标重要程度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="显示顺序" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入显示顺序"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['goal:goal:add']"
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
          v-hasPermi="['goal:goal:edit']"
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
          v-hasPermi="['goal:goal:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['goal:goal:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="goalList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="目标ID" align="center" prop="goalId" />
      <el-table-column label="课程ID" align="center" prop="courseId" />
      <el-table-column label="目标代码" align="center" prop="goalCode" />
      <el-table-column label="毕业要求" align="center" prop="graduationRequirement" />
      <el-table-column label="指标点" align="center" prop="indicatorPoint" />
      <el-table-column label="目标描述" align="center" prop="goalDescription" />
      <el-table-column label="目标重要程度" align="center" prop="goalLevel" />
      <el-table-column label="显示顺序" align="center" prop="orderNum" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['goal:goal:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['goal:goal:remove']"
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

    <!-- 添加或修改课程目标对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课程ID" prop="courseId">
          <el-input v-model="form.courseId" placeholder="请输入课程ID" />
        </el-form-item>
        <el-form-item label="目标代码" prop="goalCode">
          <el-input v-model="form.goalCode" placeholder="请输入目标代码" />
        </el-form-item>
        <el-form-item label="毕业要求" prop="graduationRequirement">
          <el-input v-model="form.graduationRequirement" placeholder="请输入毕业要求" />
        </el-form-item>
        <el-form-item label="指标点" prop="indicatorPoint">
          <el-input v-model="form.indicatorPoint" placeholder="请输入指标点" />
        </el-form-item>
        <el-form-item label="目标描述" prop="goalDescription">
          <el-input v-model="form.goalDescription" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="目标重要程度" prop="goalLevel">
          <el-input v-model="form.goalLevel" placeholder="请输入目标重要程度" />
        </el-form-item>
        <el-form-item label="显示顺序" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入显示顺序" />
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
import { listGoal, getGoal, delGoal, addGoal, updateGoal } from "@/api/goal/goal"

export default {
  name: "Goal",
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
      // 课程目标表格数据
      goalList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseId: null,
        goalCode: null,
        graduationRequirement: null,
        indicatorPoint: null,
        goalDescription: null,
        goalLevel: null,
        orderNum: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        courseId: [
          { required: true, message: "课程ID不能为空", trigger: "blur" }
        ],
        goalCode: [
          { required: true, message: "目标代码不能为空", trigger: "blur" }
        ],
        graduationRequirement: [
          { required: true, message: "毕业要求不能为空", trigger: "blur" }
        ],
        indicatorPoint: [
          { required: true, message: "指标点不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询课程目标列表 */
    getList() {
      this.loading = true
      listGoal(this.queryParams).then(response => {
        this.goalList = response.rows
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
        goalId: null,
        courseId: null,
        goalCode: null,
        graduationRequirement: null,
        indicatorPoint: null,
        goalDescription: null,
        goalLevel: null,
        orderNum: null,
        status: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
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
      this.ids = selection.map(item => item.goalId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加课程目标"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const goalId = row.goalId || this.ids
      getGoal(goalId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改课程目标"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.goalId != null) {
            updateGoal(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addGoal(this.form).then(response => {
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
      const goalIds = row.goalId || this.ids
      this.$modal.confirm('是否确认删除课程目标编号为"' + goalIds + '"的数据项？').then(function() {
        return delGoal(goalIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('goal/goal/export', {
        ...this.queryParams
      }, `goal_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
