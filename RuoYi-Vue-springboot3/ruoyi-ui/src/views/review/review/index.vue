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
      <el-form-item label="试卷名称" prop="paperName">
        <el-input
          v-model="queryParams.paperName"
          placeholder="请输入试卷名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件大小" prop="reviewSize">
        <el-input
          v-model="queryParams.reviewSize"
          placeholder="请输入文件大小"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审查日期" prop="reviewDate">
        <el-date-picker clearable
          v-model="queryParams.reviewDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择审查日期">
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
          v-hasPermi="['review:review:add']"
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
          v-hasPermi="['review:review:edit']"
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
          v-hasPermi="['review:review:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['review:review:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="reviewList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="审查ID" align="center" prop="reviewId" />
      <el-table-column label="课程代码" align="center">
        <template slot-scope="scope">
          {{ getCourseCode(scope.row.courseId) }}
        </template>
      </el-table-column>
      <el-table-column label="课程名称" align="center">
        <template slot-scope="scope">
          {{ getCourseName(scope.row.courseId) }}
        </template>
      </el-table-column>
      <el-table-column label="试卷名称" align="center" prop="paperName" />
      <el-table-column label="文件路径" align="center" prop="reviewFile" />
      <el-table-column label="文件大小" align="center">
        <template slot-scope="scope">
          {{ formatFileSize(scope.row.reviewSize) }}
        </template>
      </el-table-column>
      <el-table-column label="审查日期" align="center" prop="reviewDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.reviewDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.status === '0'">正常</span>
          <span v-else-if="scope.row.status === '1'">停用</span>
          <span v-else>{{ scope.row.status }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['review:review:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['review:review:remove']"
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

    <!-- 添加或修改试卷实质审查对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课程信息" prop="courseId">
          <el-select v-model="form.courseId" placeholder="请选择课程" style="width: 100%">
            <el-option
              v-for="course in courseList"
              :key="course.courseId"
              :label="course.courseCode + ' - ' + course.courseName"
              :value="course.courseId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="试卷名称" prop="paperName">
          <el-input v-model="form.paperName" placeholder="请输入试卷名称" />
        </el-form-item>
        <el-form-item label="文件路径" prop="reviewFile">
          <file-upload v-model="form.reviewFile" @change="handleFileChange"/>
        </el-form-item>
        <el-form-item label="文件大小" prop="reviewSize">
          <el-input :value="formatFileSize(form.reviewSize)" placeholder="请输入文件大小" disabled />
        </el-form-item>
        <el-form-item label="审查日期" prop="reviewDate">
          <el-date-picker clearable
            v-model="form.reviewDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择审查日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
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
import { listReview, getReview, delReview, addReview, updateReview } from "@/api/review/review"
import { listCourse } from "@/api/course/course"

export default {
  name: "Review",
  dicts: ['sys_normal_disable'],
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
      // 试卷实质审查表格数据
      reviewList: [],
      // 课程列表
      courseList: [],
      // 课程信息映射
      courseMap: {},
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseId: null,
        paperName: null,
        reviewFile: null,
        reviewSize: null,
        reviewDate: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        paperName: [
          { required: true, message: "试卷名称不能为空", trigger: "blur" }
        ],
        reviewFile: [
          { required: true, message: "文件路径不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
    this.getCourseList()
  },
  methods: {
    /** 查询试卷实质审查列表 */
    getList() {
      this.loading = true
      listReview(this.queryParams).then(response => {
        this.reviewList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 查询课程列表 */
    getCourseList() {
      listCourse({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.courseList = response.rows
        // 构建课程信息映射
        this.courseMap = {}
        response.rows.forEach(course => {
          this.courseMap[course.courseId] = course.courseCode + ' - ' + course.courseName
        })
      })
    },
    /** 获取课程代码 */
    getCourseCode(courseId) {
      const course = this.courseList.find(c => c.courseId === courseId)
      return course ? course.courseCode : courseId
    },
    /** 获取课程名称 */
    getCourseName(courseId) {
      const course = this.courseList.find(c => c.courseId === courseId)
      return course ? course.courseName : courseId
    },
    /** 处理文件选择变化 */
    handleFileChange(fileInfo) {
      if (fileInfo) {
        let filePaths = fileInfo
        let fileSize = 0
        
        // 处理传递的对象格式
        if (typeof fileInfo === 'object') {
          filePaths = fileInfo.paths
          fileSize = fileInfo.size
        }
        
        // 从文件路径中提取文件名
        const fileName = filePaths.split('/').pop()
        // 自动填充试卷名称（如果需要）
        if (!this.form.paperName) {
          this.form.paperName = fileName
        }
        // 填充文件大小（存储原始字节值）
        this.form.reviewSize = fileSize
      }
    },
    /** 格式化文件大小 */
    formatFileSize(bytes) {
      if (bytes === 0) return '0 B'
      const k = 1024
      const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
      const i = Math.floor(Math.log(bytes) / Math.log(k))
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        reviewId: null,
        courseId: null,
        paperName: null,
        reviewFile: null,
        reviewSize: null,
        reviewDate: null,
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
      this.ids = selection.map(item => item.reviewId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加试卷实质审查"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const reviewId = row.reviewId || this.ids
      getReview(reviewId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改试卷实质审查"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.reviewId != null) {
            updateReview(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addReview(this.form).then(response => {
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
      const reviewIds = row.reviewId || this.ids
      this.$modal.confirm('是否确认删除试卷实质审查编号为"' + reviewIds + '"的数据项？').then(function() {
        return delReview(reviewIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('review/review/export', {
        ...this.queryParams
      }, `review_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
