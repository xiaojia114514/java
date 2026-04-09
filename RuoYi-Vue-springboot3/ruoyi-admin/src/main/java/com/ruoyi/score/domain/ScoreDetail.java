package com.ruoyi.score.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 成绩细目对象 score_detail
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public class ScoreDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 细目表ID */
    private Long scoreDetailId;

    /** 成绩单ID */
    @Excel(name = "成绩单ID")
    private Long scoreId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String courseName;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String detailName;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String detailFile;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private Long detailSize;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setScoreDetailId(Long scoreDetailId) 
    {
        this.scoreDetailId = scoreDetailId;
    }

    public Long getScoreDetailId() 
    {
        return scoreDetailId;
    }

    public void setScoreId(Long scoreId) 
    {
        this.scoreId = scoreId;
    }

    public Long getScoreId() 
    {
        return scoreId;
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

    public void setDetailName(String detailName) 
    {
        this.detailName = detailName;
    }

    public String getDetailName() 
    {
        return detailName;
    }

    public void setDetailFile(String detailFile) 
    {
        this.detailFile = detailFile;
    }

    public String getDetailFile() 
    {
        return detailFile;
    }

    public void setDetailSize(Long detailSize) 
    {
        this.detailSize = detailSize;
    }

    public Long getDetailSize() 
    {
        return detailSize;
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
            .append("scoreDetailId", getScoreDetailId())
            .append("scoreId", getScoreId())
            .append("courseId", getCourseId())
            .append("courseName", getCourseName())
            .append("detailName", getDetailName())
            .append("detailFile", getDetailFile())
            .append("detailSize", getDetailSize())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
