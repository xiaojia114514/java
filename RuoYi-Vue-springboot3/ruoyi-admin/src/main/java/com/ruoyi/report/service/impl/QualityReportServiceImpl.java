package com.ruoyi.report.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.report.mapper.QualityReportMapper;
import com.ruoyi.report.domain.QualityReport;
import com.ruoyi.report.service.IQualityReportService;

/**
 * 课程教学质量分析报告Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@Service
public class QualityReportServiceImpl implements IQualityReportService 
{
    @Autowired
    private QualityReportMapper qualityReportMapper;

    /**
     * 查询课程教学质量分析报告
     * 
     * @param reportId 课程教学质量分析报告主键
     * @return 课程教学质量分析报告
     */
    @Override
    public QualityReport selectQualityReportByReportId(Long reportId)
    {
        return qualityReportMapper.selectQualityReportByReportId(reportId);
    }

    /**
     * 查询课程教学质量分析报告列表
     * 
     * @param qualityReport 课程教学质量分析报告
     * @return 课程教学质量分析报告
     */
    @Override
    public List<QualityReport> selectQualityReportList(QualityReport qualityReport)
    {
        return qualityReportMapper.selectQualityReportList(qualityReport);
    }

    /**
     * 新增课程教学质量分析报告
     * 
     * @param qualityReport 课程教学质量分析报告
     * @return 结果
     */
    @Override
    public int insertQualityReport(QualityReport qualityReport)
    {
        qualityReport.setCreateTime(DateUtils.getNowDate());
        return qualityReportMapper.insertQualityReport(qualityReport);
    }

    /**
     * 修改课程教学质量分析报告
     * 
     * @param qualityReport 课程教学质量分析报告
     * @return 结果
     */
    @Override
    public int updateQualityReport(QualityReport qualityReport)
    {
        qualityReport.setUpdateTime(DateUtils.getNowDate());
        return qualityReportMapper.updateQualityReport(qualityReport);
    }

    /**
     * 批量删除课程教学质量分析报告
     * 
     * @param reportIds 需要删除的课程教学质量分析报告主键
     * @return 结果
     */
    @Override
    public int deleteQualityReportByReportIds(Long[] reportIds)
    {
        return qualityReportMapper.deleteQualityReportByReportIds(reportIds);
    }

    /**
     * 删除课程教学质量分析报告信息
     * 
     * @param reportId 课程教学质量分析报告主键
     * @return 结果
     */
    @Override
    public int deleteQualityReportByReportId(Long reportId)
    {
        return qualityReportMapper.deleteQualityReportByReportId(reportId);
    }
}
