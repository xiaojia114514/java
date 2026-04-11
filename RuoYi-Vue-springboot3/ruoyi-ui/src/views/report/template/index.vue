<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="模板名称" prop="reportTemplateName">
        <el-input
          v-model="queryParams.reportTemplateName"
          placeholder="请输入模板名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择状态"
          clearable
        >
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
          v-hasPermi="['report:template:add']"
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
          v-hasPermi="['report:template:edit']"
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
          v-hasPermi="['report:template:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['report:template:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="templateList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="模板ID" align="center" prop="reportTemplateId" width="100" />
      <el-table-column label="模板名称" align="center" prop="reportTemplateName" />
      <el-table-column label="文件路径" align="center" prop="reportTemplateFile" show-overflow-tooltip />
      <el-table-column label="文件大小" align="center" prop="reportTemplateSize" width="120">
        <template slot-scope="scope">
          {{ formatFileSize(scope.row.reportTemplateSize) }}
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
            {{ scope.row.status === '0' ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="是否解析" align="center" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.reportTemplateTaskId ? 'success' : 'warning'">
            {{ scope.row.reportTemplateTaskId ? '已解析' : '未解析' }}
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
            v-hasPermi="['report:template:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['report:template:remove']"
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

    <!-- 添加或修改课程教学质量分析报告模板管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="模板名称" prop="reportTemplateName">
          <el-input v-model="form.reportTemplateName" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="文件路径" prop="reportTemplateFile">
          <file-upload 
            v-model="form.reportTemplateFile" 
            @change="handleFileChange" 
          />
        </el-form-item>
        <el-form-item label="文件大小" prop="reportTemplateSize">
          <el-input v-model="formattedReportTemplateSize" placeholder="请选择文件" disabled />
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

    <!-- 查看课程教学质量分析报告模板管理对话框 -->
    <el-dialog title="查看模板详情" :visible.sync="viewOpen" width="500px" append-to-body>
      <el-form ref="viewForm" :model="viewForm" label-width="80px">
        <el-form-item label="模板名称">
          <el-input v-model="viewForm.reportTemplateName" disabled />
        </el-form-item>
        <el-form-item label="文件路径">
          <el-input v-model="viewForm.reportTemplateFile" disabled />
        </el-form-item>
        <el-form-item label="文件大小">
          <el-input :value="formatFileSize(viewForm.reportTemplateSize)" disabled />
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
import { listTemplate, getTemplate, delTemplate, addTemplate, updateTemplate } from "@/api/report/template"
import download from '@/plugins/download'

export default {
  name: "Template",
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
      // 课程教学质量分析报告模板管理表格数据
      templateList: [],
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
        reportTemplateName: null,
        status: null,
      },
      // 表单参数
      form: {
        reportTemplateId: null,
        reportTemplateName: null,
        reportTemplateFile: null,
        reportTemplateSize: null,
        status: "0",
        remark: null
      },
      // 表单校验
      rules: {
        reportTemplateName: [
          { required: true, message: "报告模板名称不能为空", trigger: "blur" }
        ],
        reportTemplateFile: [
          { required: true, message: "文件路径不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "blur" }
        ]
      }
    }
  },
  computed: {
    // 格式化文件大小
    formattedReportTemplateSize() {
      return this.formatFileSize(this.form.reportTemplateSize)
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询课程教学质量分析报告模板管理列表 */
    getList() {
      this.loading = true
      listTemplate(this.queryParams).then(response => {
        this.templateList = response.rows
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
        reportTemplateId: null,
        reportTemplateName: null,
        reportTemplateFile: null,
        reportTemplateSize: null,
        status: "0",
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
      this.ids = selection.map(item => item.reportTemplateId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加课程教学质量分析报告模板管理"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const reportTemplateId = row.reportTemplateId || this.ids
      getTemplate(reportTemplateId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改课程教学质量分析报告模板管理"
      })
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.viewForm = { ...row }
      this.viewOpen = true
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.reportTemplateId != null) {
            updateTemplate(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addTemplate(this.form).then(response => {
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
      const reportTemplateIds = row.reportTemplateId || this.ids
      this.$modal.confirm('是否确认删除课程教学质量分析报告模板管理编号为"' + reportTemplateIds + '"的数据项？').then(function() {
        return delTemplate(reportTemplateIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('report/template/export', {
        ...this.queryParams
      }, `template_${new Date().getTime()}.xlsx`)
    },
    /** 下载文件 */
    handleDownload(row) {
      if (row.reportTemplateFile) {
        // 下载文件，使用download插件
        download.resource(row.reportTemplateFile)
      }
    },
    /** 文件上传后处理 */
    handleFileChange(fileData) {
      if (fileData) {
        // 从file-upload组件传递的对象中获取数据
        if (fileData.name) {
          // 使用文件名称作为模板名称（去除扩展名）
          const templateName = fileData.name.substring(0, fileData.name.lastIndexOf('.'))
          this.form.reportTemplateName = templateName
        }
        
        if (fileData.size) {
          // 设置文件大小（原始字节数）
          this.form.reportTemplateSize = fileData.size
        }
      } else {
        // 清空文件信息
        this.form.reportTemplateSize = null
        this.form.reportTemplateName = ''
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
    }
  }
}
</script>