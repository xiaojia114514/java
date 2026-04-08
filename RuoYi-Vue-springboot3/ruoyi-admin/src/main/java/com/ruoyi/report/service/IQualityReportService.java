package com.ruoyi.report.service;

import java.util.List;
import com.ruoyi.report.domain.QualityReport;

/**
 * 课程教学质量分析报告Service接口
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public interface IQualityReportService 
{
    /**
     * 查询课程教学质量分析报告
     * 
     * @param reportId 课程教学质量分析报告主键
     * @return 课程教学质量分析报告
     */
    public QualityReport selectQualityReportByReportId(Long reportId);

    /**
     * 查询课程教学质量分析报告列表
     * 
     * @param qualityReport 课程教学质量分析报告
     * @return 课程教学质量分析报告集合
     */
    public List<QualityReport> selectQualityReportList(QualityReport qualityReport);

    /**
     * 新增课程教学质量分析报告
     * 
     * @param qualityReport 课程教学质量分析报告
     * @return 结果
     */
    public int insertQualityReport(QualityReport qualityReport);

    /**
     * 修改课程教学质量分析报告
     * 
     * @param qualityReport 课程教学质量分析报告
     * @return 结果
     */
    public int updateQualityReport(QualityReport qualityReport);

    /**
     * 批量删除课程教学质量分析报告
     * 
     * @param reportIds 需要删除的课程教学质量分析报告主键集合
     * @return 结果
     */
    public int deleteQualityReportByReportIds(Long[] reportIds);

    /**
     * 删除课程教学质量分析报告信息
     * 
     * @param reportId 课程教学质量分析报告主键
     * @return 结果
     */
    public int deleteQualityReportByReportId(Long reportId);
}
