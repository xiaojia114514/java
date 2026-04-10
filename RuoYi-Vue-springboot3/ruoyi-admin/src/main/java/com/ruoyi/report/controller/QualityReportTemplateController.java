package com.ruoyi.report.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.report.domain.QualityReportTemplate;
import com.ruoyi.report.service.IQualityReportTemplateService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 课程教学质量分析报告模板Controller
 * 
 * @author ruoyi
 * @date 2026-04-10
 */
@RestController
@RequestMapping("/report/template")
public class QualityReportTemplateController extends BaseController
{
    @Autowired
    private IQualityReportTemplateService qualityReportTemplateService;

    /**
     * 查询课程教学质量分析报告模板列表
     */
    @PreAuthorize("@ss.hasPermi('report:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(QualityReportTemplate qualityReportTemplate)
    {
        startPage();
        List<QualityReportTemplate> list = qualityReportTemplateService.selectQualityReportTemplateList(qualityReportTemplate);
        return getDataTable(list);
    }

    /**
     * 导出课程教学质量分析报告模板列表
     */
    @PreAuthorize("@ss.hasPermi('report:template:export')")
    @Log(title = "课程教学质量分析报告模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QualityReportTemplate qualityReportTemplate)
    {
        List<QualityReportTemplate> list = qualityReportTemplateService.selectQualityReportTemplateList(qualityReportTemplate);
        ExcelUtil<QualityReportTemplate> util = new ExcelUtil<QualityReportTemplate>(QualityReportTemplate.class);
        util.exportExcel(response, list, "课程教学质量分析报告模板数据");
    }

    /**
     * 获取课程教学质量分析报告模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('report:template:query')")
    @GetMapping(value = "/{reportTemplateId}")
    public AjaxResult getInfo(@PathVariable("reportTemplateId") Long reportTemplateId)
    {
        return success(qualityReportTemplateService.selectQualityReportTemplateByReportTemplateId(reportTemplateId));
    }

    /**
     * 新增课程教学质量分析报告模板
     */
    @PreAuthorize("@ss.hasPermi('report:template:add')")
    @Log(title = "课程教学质量分析报告模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QualityReportTemplate qualityReportTemplate)
    {
        return toAjax(qualityReportTemplateService.insertQualityReportTemplate(qualityReportTemplate));
    }

    /**
     * 修改课程教学质量分析报告模板
     */
    @PreAuthorize("@ss.hasPermi('report:template:edit')")
    @Log(title = "课程教学质量分析报告模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QualityReportTemplate qualityReportTemplate)
    {
        return toAjax(qualityReportTemplateService.updateQualityReportTemplate(qualityReportTemplate));
    }

    /**
     * 删除课程教学质量分析报告模板
     */
    @PreAuthorize("@ss.hasPermi('report:template:remove')")
    @Log(title = "课程教学质量分析报告模板", businessType = BusinessType.DELETE)
	@DeleteMapping("/{reportTemplateIds}")
    public AjaxResult remove(@PathVariable Long[] reportTemplateIds)
    {
        return toAjax(qualityReportTemplateService.deleteQualityReportTemplateByReportTemplateIds(reportTemplateIds));
    }
}