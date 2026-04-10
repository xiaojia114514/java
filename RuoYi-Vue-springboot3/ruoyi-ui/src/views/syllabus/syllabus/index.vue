<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="课程名称" prop="courseName">
        <el-input
          v-model="queryParams.courseName"
          placeholder="请输入课程名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件名称" prop="syllabusName">
        <el-input
          v-model="queryParams.syllabusName"
          placeholder="请输入文件名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
        </el-select>
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
          v-hasPermi="['syllabus:syllabus:add']"
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
          v-hasPermi="['syllabus:syllabus:edit']"
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
          v-hasPermi="['syllabus:syllabus:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['syllabus:syllabus:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="syllabusList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="大纲ID" align="center" prop="syllabusId" width="80" />
      <el-table-column label="课程信息" align="center">
        <template slot-scope="scope">
          {{ getCourseInfo(scope.row.courseId) }}
        </template>
      </el-table-column>
      <el-table-column label="文件名称" align="center" prop="syllabusName" />
      <el-table-column label="文件路径" align="center" prop="syllabusFile" width="150" show-overflow-tooltip />
      <el-table-column label="文件大小" align="center" width="100">
        <template slot-scope="scope">
          {{ formatFileSize(scope.row.syllabusSize) }}
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
            {{ scope.row.status === '0' ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="是否解析" align="center" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.syllabusTaskId ? 'success' : 'warning'">
            {{ scope.row.syllabusTaskId ? '已解析' : '未解析' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" width="120" show-overflow-tooltip />
      <el-table-column label="操作" align="center" width="240" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['syllabus:syllabus:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['syllabus:syllabus:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="handleDownload(scope.row)"
          >下载</el-button>
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

    <!-- 添加或修改教学大纲对话框 -->
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
        <el-form-item label="文件名称" prop="syllabusName">
          <el-input v-model="form.syllabusName" placeholder="请输入文件名称" />
        </el-form-item>
        <el-form-item label="文件路径" prop="syllabusFile">
          <file-upload v-model="form.syllabusFile" @change="handleFileChange"/>
        </el-form-item>
        <el-form-item label="文件大小" prop="syllabusSize">
          <el-input v-model="formattedSyllabusSize" placeholder="请选择文件" readonly disabled />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
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

    <!-- 查看教学大纲详情对话框 -->
    <el-dialog title="查看教学大纲" :visible.sync="viewOpen" width="500px" append-to-body>
      <el-form ref="viewForm" :model="viewForm" label-width="80px">
        <el-form-item label="课程信息">
          <el-input :value="getCourseInfo(viewForm.courseId)" disabled />
        </el-form-item>
        <el-form-item label="文件名称">
          <el-input v-model="viewForm.syllabusName" disabled />
        </el-form-item>
        <el-form-item label="文件路径">
          <el-input v-model="viewForm.syllabusFile" disabled />
        </el-form-item>
        <el-form-item label="文件大小">
          <el-input :value="formatFileSize(viewForm.syllabusSize)" disabled />
        </el-form-item>
        <el-form-item label="状态">
          <el-input :value="viewForm.status === '0' ? '正常' : '停用'" disabled />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="viewForm.remark" type="textarea" disabled />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listSyllabus, getSyllabus, delSyllabus, addSyllabus, updateSyllabus } from "@/api/syllabus/syllabus"
import download from '@/plugins/download'
import { listCourse } from "@/api/course/course"

export default {
  name: "Syllabus",
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
      // 教学大纲表格数据
      syllabusList: [],
      // 课程列表
      courseList: [],
      // 课程信息映射
      courseMap: {},
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示查看弹出层
      viewOpen: false,
      // 查看表单参数
      viewForm: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseId: null,
        courseName: null,
        syllabusName: null,
        syllabusFile: null,
        syllabusSize: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        syllabusName: [
          { required: true, message: "文件名称不能为空", trigger: "blur" }
        ],
        syllabusFile: [
          { required: true, message: "文件路径不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
    this.getCourseList()
  },
  computed: {
    /** 格式化后的文件大小 */
    formattedSyllabusSize() {
      return this.formatFileSize(this.form.syllabusSize)
    }
  },
  methods: {
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
    /** 获取课程信息 */
    getCourseInfo(courseId) {
      return this.courseMap[courseId] || courseId
    },
    /** 下载文件 */
    handleDownload(row) {
      if (row.syllabusFile) {
        // 下载文件，使用download插件
        download.resource(row.syllabusFile)
      }
    },
    /** 格式化文件大小 */
    formatFileSize(size) {
      if (!size || size === 0) return ''
      const units = ['B', 'KB', 'MB', 'GB']
      let index = 0
      let fileSize = size
      while (fileSize >= 1024 && index < units.length - 1) {
        fileSize /= 1024
        index++
      }
      return fileSize.toFixed(2) + ' ' + units[index]
    },
    /** 文件上传变更处理 */
    handleFileChange(fileInfo) {
      if (fileInfo !== null && fileInfo !== undefined) {
        // 存储原始字节数到form.syllabusSize
        this.form.syllabusSize = fileInfo.size
        // 从fileInfo中获取文件名并填充到文件名称文本框
        if (fileInfo.name) {
          this.form.syllabusName = fileInfo.name
        }
      } else {
        this.form.syllabusSize = null
        this.form.syllabusName = ''
      }
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.viewForm = { ...row }
      this.viewOpen = true
    },
    /** 查询教学大纲列表 */
    getList() {
      this.loading = true
      listSyllabus(this.queryParams).then(response => {
        this.syllabusList = response.rows
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
        syllabusId: null,
        courseId: null,
        syllabusName: null,
        syllabusFile: null,
        syllabusSize: null,
        status: "0",
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
      this.ids = selection.map(item => item.syllabusId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加教学大纲"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const syllabusId = row.syllabusId || this.ids
      getSyllabus(syllabusId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改教学大纲"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.syllabusId != null) {
            updateSyllabus(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addSyllabus(this.form).then(response => {
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
      const syllabusIds = row.syllabusId || this.ids
      this.$modal.confirm('是否确认删除教学大纲编号为"' + syllabusIds + '"的数据项？').then(function() {
        return delSyllabus(syllabusIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('syllabus/syllabus/export', {
        ...this.queryParams
      }, `syllabus_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
