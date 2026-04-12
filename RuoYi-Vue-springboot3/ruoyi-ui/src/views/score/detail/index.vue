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
      <el-form-item label="关联成绩单" prop="scoreId">
        <el-select
          v-model="queryParams.scoreId"
          placeholder="请选择关联成绩单"
          clearable
          style="width: 200px"
        >
          <el-option
            v-for="score in scoreList"
            :key="score.scoreId"
            :label="score.scoreName"
            :value="score.scoreId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="文件名称" prop="detailName">
        <el-input
          v-model="queryParams.detailName"
          placeholder="请输入文件名称"
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
          v-hasPermi="['score:detail:add']"
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
          v-hasPermi="['score:detail:edit']"
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
          v-hasPermi="['score:detail:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['score:detail:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="detailList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="细目表ID" align="center" prop="scoreDetailId" width="80" />
      <el-table-column label="关联成绩单" align="center" width="150">
        <template slot-scope="scope">
          {{ getScoreName(scope.row.scoreId) }}
        </template>
      </el-table-column>
      <el-table-column label="课程信息" align="center">
        <template slot-scope="scope">
          {{ getCourseInfo(scope.row.courseId) }}
        </template>
      </el-table-column>
      <el-table-column label="文件名称" align="center" prop="detailName" />
      <el-table-column label="文件路径" align="center" prop="detailFile" width="150" show-overflow-tooltip />
      <el-table-column label="文件大小" align="center" width="100">
        <template slot-scope="scope">
          <span>{{ formatFileSize(scope.row.detailSize) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
            {{ scope.row.status === '0' ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>

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
            v-hasPermi="['score:detail:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['score:detail:remove']"
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

    <!-- 添加或修改成绩细目对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联成绩单" prop="scoreId">
          <el-select v-model="form.scoreId" placeholder="请选择关联成绩单" style="width: 100%" @change="handleScoreChange">
            <el-option
              v-for="score in scoreList"
              :key="score.scoreId"
              :label="score.scoreName"
              :value="score.scoreId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="课程信息" prop="courseId">
          <el-select v-model="form.courseId" placeholder="请选择关联成绩单" style="width: 100%" disabled>
            <el-option
              v-for="course in courseList"
              :key="course.courseId"
              :label="course.courseCode + ' - ' + course.courseName"
              :value="course.courseId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="文件名称" prop="detailName">
          <el-input v-model="form.detailName" placeholder="请输入文件名称" />
        </el-form-item>
        <el-form-item label="文件路径" prop="detailFile">
          <file-upload v-model="form.detailFile" @change="handleFileChange"/>
        </el-form-item>
        <el-form-item label="文件大小" prop="detailSize">
          <el-input v-model="formattedDetailSize" placeholder="请选择文件" readonly disabled />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看成绩细目详情对话框 -->
    <el-dialog title="查看成绩细目" :visible.sync="viewOpen" width="500px" append-to-body>
      <el-form ref="viewForm" :model="viewForm" label-width="80px">
        <el-form-item label="关联成绩单">
          <el-input :value="getScoreName(viewForm.scoreId)" disabled />
        </el-form-item>
        <el-form-item label="课程信息">
          <el-input :value="getCourseInfo(viewForm.courseId)" disabled />
        </el-form-item>
        <el-form-item label="文件名称">
          <el-input v-model="viewForm.detailName" disabled />
        </el-form-item>
        <el-form-item label="文件路径">
          <el-input v-model="viewForm.detailFile" disabled />
        </el-form-item>
        <el-form-item label="文件大小">
          <el-input :value="formatFileSize(viewForm.detailSize)" disabled />
        </el-form-item>
        <el-form-item label="状态">
          <el-input :value="viewForm.status === '0' ? '正常' : '停用'" disabled />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDetail, getDetail, delDetail, addDetail, updateDetail } from "@/api/score/detail"
import download from '@/plugins/download'
import { listCourse } from "@/api/course/course"
import { listScore } from "@/api/score/score"

export default {
  name: "Detail",
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
      // 成绩细目表格数据
      detailList: [],
      // 课程列表
      courseList: [],
      // 课程信息映射
      courseMap: {},
      // 成绩单列表
      scoreList: [],
      // 成绩单信息映射
      scoreMap: {},
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
        scoreId: null,
        courseId: null,
        courseName: null,
        detailName: null,
        detailFile: null,
        detailSize: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        scoreId: [
          { required: true, message: "成绩单ID不能为空", trigger: "blur" }
        ],
        courseId: [
          { required: true, message: "课程ID不能为空", trigger: "blur" }
        ],
        detailName: [
          { required: true, message: "文件名称不能为空", trigger: "blur" }
        ],
        detailFile: [
          { required: true, message: "文件路径不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
    this.getCourseList()
    this.getScoreList()
  },
  computed: {
    /** 格式化后的文件大小 */
    formattedDetailSize() {
      return this.formatFileSize(this.form.detailSize)
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
    /** 查询成绩单列表 */
    getScoreList() {
      listScore({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.scoreList = response.rows
        // 构建成绩单信息映射
        this.scoreMap = {}
        response.rows.forEach(score => {
          this.scoreMap[score.scoreId] = {
            scoreName: score.scoreName,
            courseId: score.courseId
          }
        })
      })
    },
    /** 成绩单选择变更处理 */
    handleScoreChange(scoreId) {
      if (scoreId) {
        const scoreInfo = this.scoreMap[scoreId]
        if (scoreInfo) {
          // 自动填充课程信息
          this.form.courseId = scoreInfo.courseId
        } else {
          this.form.courseId = null
        }
      } else {
        this.form.courseId = null
      }
    },
    /** 获取课程信息 */
    getCourseInfo(courseId) {
      return this.courseMap[courseId] || courseId
    },
    /** 获取成绩单名称 */
    getScoreName(scoreId) {
      return this.scoreMap[scoreId] ? this.scoreMap[scoreId].scoreName : scoreId
    },
    /** 下载文件 */
    handleDownload(row) {
      if (row.detailFile) {
        // 下载文件，使用download插件
        download.resource(row.detailFile)
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
        // 存储原始字节数到form.detailSize
        this.form.detailSize = fileInfo.size
        // 从fileInfo中获取文件名并填充到文件名称文本框
        if (fileInfo.name) {
          this.form.detailName = fileInfo.name
        }
      } else {
        this.form.detailSize = null
        this.form.detailName = ''
      }
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.viewForm = { ...row }
      this.viewOpen = true
    },
    /** 查询成绩细目列表 */
    getList() {
      this.loading = true
      listDetail(this.queryParams).then(response => {
        this.detailList = response.rows
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
        scoreDetailId: null,
        scoreId: null,
        courseId: null,
        detailName: null,
        detailFile: null,
        detailSize: null,
        status: "0",
        createTime: null,
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
      this.ids = selection.map(item => item.scoreDetailId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加成绩细目"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const scoreDetailId = row.scoreDetailId || this.ids
      getDetail(scoreDetailId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改成绩细目"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.scoreDetailId != null) {
            updateDetail(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addDetail(this.form).then(response => {
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
      const scoreDetailIds = row.scoreDetailId || this.ids
      this.$modal.confirm('是否确认删除成绩细目编号为"' + scoreDetailIds + '"的数据项？').then(function() {
        return delDetail(scoreDetailIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('score/detail/export', {
        ...this.queryParams
      }, `detail_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
