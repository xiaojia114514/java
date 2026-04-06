package com.ruoyi.review.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 试卷实质审查对象 paper_review
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public class PaperReview extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 审查ID */
    private Long reviewId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 试卷名称 */
    @Excel(name = "试卷名称")
    private String paperName;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String reviewFile;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private Long reviewSize;

    /** 审查日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审查日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reviewDate;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setReviewId(Long reviewId) 
    {
        this.reviewId = reviewId;
    }

    public Long getReviewId() 
    {
        return reviewId;
    }

    public void setCourseId(Long courseId) 
    {
        this.courseId = courseId;
    }

    public Long getCourseId() 
    {
        return courseId;
    }

    public void setPaperName(String paperName) 
    {
        this.paperName = paperName;
    }

    public String getPaperName() 
    {
        return paperName;
    }

    public void setReviewFile(String reviewFile) 
    {
        this.reviewFile = reviewFile;
    }

    public String getReviewFile() 
    {
        return reviewFile;
    }

    public void setReviewSize(Long reviewSize) 
    {
        this.reviewSize = reviewSize;
    }

    public Long getReviewSize() 
    {
        return reviewSize;
    }

    public void setReviewDate(Date reviewDate) 
    {
        this.reviewDate = reviewDate;
    }

    public Date getReviewDate() 
    {
        return reviewDate;
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
            .append("reviewId", getReviewId())
            .append("courseId", getCourseId())
            .append("paperName", getPaperName())
            .append("reviewFile", getReviewFile())
            .append("reviewSize", getReviewSize())
            .append("reviewDate", getReviewDate())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
