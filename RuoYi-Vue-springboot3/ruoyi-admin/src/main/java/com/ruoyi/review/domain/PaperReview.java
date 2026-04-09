package com.ruoyi.review.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 试卷实质审查对象 paper_review
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public class PaperReview extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 审查ID */
    private Long reviewId;

    /** 课程ID（唯一约束） */
    @Excel(name = "课程ID", readConverterExp = "唯=一约束")
    private Long courseId;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String courseName;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String reviewName;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String reviewFile;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private Long reviewSize;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

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

    public void setCourseName(String courseName) 
    {
        this.courseName = courseName;
    }

    public String getCourseName() 
    {
        return courseName;
    }

    public void setReviewName(String reviewName) 
    {
        this.reviewName = reviewName;
    }

    public String getReviewName() 
    {
        return reviewName;
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

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("reviewId", getReviewId())
            .append("courseId", getCourseId())
            .append("courseName", getCourseName())
            .append("reviewName", getReviewName())
            .append("reviewFile", getReviewFile())
            .append("reviewSize", getReviewSize())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
