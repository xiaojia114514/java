package com.ruoyi.report.mapper;

import com.ruoyi.report.domain.QualityReportTemplate;

import java.util.List;

/**
 * 课程教学质量分析报告模板Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-10
 */
public interface QualityReportTemplateMapper
{
    /**
     * 查询课程教学质量分析报告模板
     * 
     * @param reportTemplateId 课程教学质量分析报告模板ID
     * @return 课程教学质量分析报告模板
     */
    public QualityReportTemplate selectQualityReportTemplateByReportTemplateId(Long reportTemplateId);

    /**
     * 查询课程教学质量分析报告模板列表
     * 
     * @param qualityReportTemplate 课程教学质量分析报告模板
     * @return 课程教学质量分析报告模板集合
     */
    public List<QualityReportTemplate> selectQualityReportTemplateList(QualityReportTemplate qualityReportTemplate);

    /**
     * 新增课程教学质量分析报告模板
     * 
     * @param qualityReportTemplate 课程教学质量分析报告模板
     * @return 结果
     */
    public int insertQualityReportTemplate(QualityReportTemplate qualityReportTemplate);

    /**
     * 修改课程教学质量分析报告模板
     * 
     * @param qualityReportTemplate 课程教学质量分析报告模板
     * @return 结果
     */
    public int updateQualityReportTemplate(QualityReportTemplate qualityReportTemplate);

    /**
     * 删除课程教学质量分析报告模板
     * 
     * @param reportTemplateId 课程教学质量分析报告模板ID
     * @return 结果
     */
    public int deleteQualityReportTemplateByReportTemplateId(Long reportTemplateId);

    /**
     * 批量删除课程教学质量分析报告模板
     * 
     * @param reportTemplateIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteQualityReportTemplateByReportTemplateIds(Long[] reportTemplateIds);
}