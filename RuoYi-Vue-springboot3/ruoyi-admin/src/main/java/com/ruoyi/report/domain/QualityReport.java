package com.ruoyi.report.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 课程教学质量分析报告对象 quality_report
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public class QualityReport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 报告ID */
    private Long reportId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 分析ID */
    @Excel(name = "分析ID")
    private Long analysisId;

    /** 报告名称 */
    @Excel(name = "报告名称")
    private String reportName;

    /** 报告内容 */
    @Excel(name = "报告内容")
    private String reportContent;

    /** 报告文件路径 */
    @Excel(name = "报告文件路径")
    private String reportFile;

    /** 报告生成日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报告生成日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reportDate;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setReportId(Long reportId) 
    {
        this.reportId = reportId;
    }

    public Long getReportId() 
    {
        return reportId;
    }

    public void setCourseId(Long courseId) 
    {
        this.courseId = courseId;
    }

    public Long getCourseId() 
    {
        return courseId;
    }

    public void setAnalysisId(Long analysisId) 
    {
        this.analysisId = analysisId;
    }

    public Long getAnalysisId() 
    {
        return analysisId;
    }

    public void setReportName(String reportName) 
    {
        this.reportName = reportName;
    }

    public String getReportName() 
    {
        return reportName;
    }

    public void setReportContent(String reportContent) 
    {
        this.reportContent = reportContent;
    }

    public String getReportContent() 
    {
        return reportContent;
    }

    public void setReportFile(String reportFile) 
    {
        this.reportFile = reportFile;
    }

    public String getReportFile() 
    {
        return reportFile;
    }

    public void setReportDate(Date reportDate) 
    {
        this.reportDate = reportDate;
    }

    public Date getReportDate() 
    {
        return reportDate;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("reportId", getReportId())
            .append("courseId", getCourseId())
            .append("analysisId", getAnalysisId())
            .append("reportName", getReportName())
            .append("reportContent", getReportContent())
            .append("reportFile", getReportFile())
            .append("reportDate", getReportDate())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
