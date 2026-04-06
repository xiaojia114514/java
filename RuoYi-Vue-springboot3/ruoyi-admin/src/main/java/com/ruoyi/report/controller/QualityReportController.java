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
import com.ruoyi.report.domain.QualityReport;
import com.ruoyi.report.service.IQualityReportService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 课程教学质量分析报告Controller
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/report/report")
public class QualityReportController extends BaseController
{
    @Autowired
    private IQualityReportService qualityReportService;

    /**
     * 查询课程教学质量分析报告列表
     */
    @PreAuthorize("@ss.hasPermi('report:report:list')")
    @GetMapping("/list")
    public TableDataInfo list(QualityReport qualityReport)
    {
        startPage();
        List<QualityReport> list = qualityReportService.selectQualityReportList(qualityReport);
        return getDataTable(list);
    }

    /**
     * 导出课程教学质量分析报告列表
     */
    @PreAuthorize("@ss.hasPermi('report:report:export')")
    @Log(title = "课程教学质量分析报告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QualityReport qualityReport)
    {
        List<QualityReport> list = qualityReportService.selectQualityReportList(qualityReport);
        ExcelUtil<QualityReport> util = new ExcelUtil<QualityReport>(QualityReport.class);
        util.exportExcel(response, list, "课程教学质量分析报告数据");
    }

    /**
     * 获取课程教学质量分析报告详细信息
     */
    @PreAuthorize("@ss.hasPermi('report:report:query')")
    @GetMapping(value = "/{reportId}")
    public AjaxResult getInfo(@PathVariable("reportId") Long reportId)
    {
        return success(qualityReportService.selectQualityReportByReportId(reportId));
    }

    /**
     * 新增课程教学质量分析报告
     */
    @PreAuthorize("@ss.hasPermi('report:report:add')")
    @Log(title = "课程教学质量分析报告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QualityReport qualityReport)
    {
        return toAjax(qualityReportService.insertQualityReport(qualityReport));
    }

    /**
     * 修改课程教学质量分析报告
     */
    @PreAuthorize("@ss.hasPermi('report:report:edit')")
    @Log(title = "课程教学质量分析报告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QualityReport qualityReport)
    {
        return toAjax(qualityReportService.updateQualityReport(qualityReport));
    }

    /**
     * 删除课程教学质量分析报告
     */
    @PreAuthorize("@ss.hasPermi('report:report:remove')")
    @Log(title = "课程教学质量分析报告", businessType = BusinessType.DELETE)
	@DeleteMapping("/{reportIds}")
    public AjaxResult remove(@PathVariable Long[] reportIds)
    {
        return toAjax(qualityReportService.deleteQualityReportByReportIds(reportIds));
    }
}
