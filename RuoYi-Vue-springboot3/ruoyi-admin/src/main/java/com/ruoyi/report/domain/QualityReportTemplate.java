package com.ruoyi.report.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 课程教学质量分析报告模板对象 quality_report_template
 * 
 * @author ruoyi
 * @date 2026-04-10
 */
public class QualityReportTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 报告模板ID */
    private Long reportTemplateId;

    /** 报告模板名称 */
    @Excel(name = "报告模板名称")
    private String reportTemplateName;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String reportTemplateFile;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private Long reportTemplateSize;

    /** 解析内容 */
    @Excel(name = "解析内容")
    private String parseContent;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setReportTemplateId(Long reportTemplateId)
    {
        this.reportTemplateId = reportTemplateId;
    }

    public Long getReportTemplateId()
    {
        return reportTemplateId;
    }

    public void setReportTemplateName(String reportTemplateName)
    {
        this.reportTemplateName = reportTemplateName;
    }

    public String getReportTemplateName()
    {
        return reportTemplateName;
    }

    public void setReportTemplateFile(String reportTemplateFile)
    {
        this.reportTemplateFile = reportTemplateFile;
    }

    public String getReportTemplateFile()
    {
        return reportTemplateFile;
    }

    public void setReportTemplateSize(Long reportTemplateSize)
    {
        this.reportTemplateSize = reportTemplateSize;
    }

    public Long getReportTemplateSize()
    {
        return reportTemplateSize;
    }

    public void setParseContent(String parseContent)
    {
        this.parseContent = parseContent;
    }

    public String getParseContent()
    {
        return parseContent;
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
            .append("reportTemplateId", getReportTemplateId())
            .append("reportTemplateName", getReportTemplateName())
            .append("reportTemplateFile", getReportTemplateFile())
            .append("reportTemplateSize", getReportTemplateSize())
            .append("parseContent", getParseContent())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}