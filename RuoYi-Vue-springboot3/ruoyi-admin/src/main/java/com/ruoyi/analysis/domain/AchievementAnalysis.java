package com.ruoyi.analysis.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 达成度分析对象 achievement_analysis
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public class AchievementAnalysis extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分析ID */
    private Long analysisId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 大纲ID */
    @Excel(name = "大纲ID")
    private Long syllabusId;

    /** 审查ID */
    @Excel(name = "审查ID")
    private Long reviewId;

    /** 成绩单ID */
    @Excel(name = "成绩单ID")
    private Long scoreId;

    /** 分析日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "分析日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date analysisDate;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setAnalysisId(Long analysisId) 
    {
        this.analysisId = analysisId;
    }

    public Long getAnalysisId() 
    {
        return analysisId;
    }

    public void setCourseId(Long courseId) 
    {
        this.courseId = courseId;
    }

    public Long getCourseId() 
    {
        return courseId;
    }

    public void setSyllabusId(Long syllabusId) 
    {
        this.syllabusId = syllabusId;
    }

    public Long getSyllabusId() 
    {
        return syllabusId;
    }

    public void setReviewId(Long reviewId) 
    {
        this.reviewId = reviewId;
    }

    public Long getReviewId() 
    {
        return reviewId;
    }

    public void setScoreId(Long scoreId) 
    {
        this.scoreId = scoreId;
    }

    public Long getScoreId() 
    {
        return scoreId;
    }

    public void setAnalysisDate(Date analysisDate) 
    {
        this.analysisDate = analysisDate;
    }

    public Date getAnalysisDate() 
    {
        return analysisDate;
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
            .append("analysisId", getAnalysisId())
            .append("courseId", getCourseId())
            .append("syllabusId", getSyllabusId())
            .append("reviewId", getReviewId())
            .append("scoreId", getScoreId())
            .append("analysisDate", getAnalysisDate())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
