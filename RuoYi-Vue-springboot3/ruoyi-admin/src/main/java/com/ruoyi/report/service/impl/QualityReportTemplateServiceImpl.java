package com.ruoyi.report.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.report.domain.QualityReportTemplate;
import com.ruoyi.report.mapper.QualityReportTemplateMapper;
import com.ruoyi.report.service.IQualityReportTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 课程教学质量分析报告模板Service实现
 * 
 * @author ruoyi
 * @date 2026-04-10
 */
@Service
public class QualityReportTemplateServiceImpl implements IQualityReportTemplateService
{
    @Autowired
    private QualityReportTemplateMapper qualityReportTemplateMapper;

    /**
     * 查询课程教学质量分析报告模板
     * 
     * @param reportTemplateId 课程教学质量分析报告模板ID
     * @return 课程教学质量分析报告模板
     */
    @Override
    public QualityReportTemplate selectQualityReportTemplateByReportTemplateId(Long reportTemplateId)
    {
        return qualityReportTemplateMapper.selectQualityReportTemplateByReportTemplateId(reportTemplateId);
    }

    /**
     * 查询课程教学质量分析报告模板列表
     * 
     * @param qualityReportTemplate 课程教学质量分析报告模板
     * @return 课程教学质量分析报告模板集合
     */
    @Override
    public List<QualityReportTemplate> selectQualityReportTemplateList(QualityReportTemplate qualityReportTemplate)
    {
        return qualityReportTemplateMapper.selectQualityReportTemplateList(qualityReportTemplate);
    }

    /**
     * 新增课程教学质量分析报告模板
     * 
     * @param qualityReportTemplate 课程教学质量分析报告模板
     * @return 结果
     */
    @Override
    public int insertQualityReportTemplate(QualityReportTemplate qualityReportTemplate)
    {
        qualityReportTemplate.setCreateTime(DateUtils.getNowDate());
        return qualityReportTemplateMapper.insertQualityReportTemplate(qualityReportTemplate);
    }

    /**
     * 修改课程教学质量分析报告模板
     * 
     * @param qualityReportTemplate 课程教学质量分析报告模板
     * @return 结果
     */
    @Override
    public int updateQualityReportTemplate(QualityReportTemplate qualityReportTemplate)
    {
        qualityReportTemplate.setUpdateTime(DateUtils.getNowDate());
        return qualityReportTemplateMapper.updateQualityReportTemplate(qualityReportTemplate);
    }

    /**
     * 删除课程教学质量分析报告模板
     * 
     * @param reportTemplateId 课程教学质量分析报告模板ID
     * @return 结果
     */
    @Override
    public int deleteQualityReportTemplateByReportTemplateId(Long reportTemplateId)
    {
        return qualityReportTemplateMapper.deleteQualityReportTemplateByReportTemplateId(reportTemplateId);
    }

    /**
     * 批量删除课程教学质量分析报告模板
     * 
     * @param reportTemplateIds 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteQualityReportTemplateByReportTemplateIds(Long[] reportTemplateIds)
    {
        return qualityReportTemplateMapper.deleteQualityReportTemplateByReportTemplateIds(reportTemplateIds);
    }
}