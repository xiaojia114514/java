package com.ruoyi.score.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 考试成绩单对象 exam_score
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public class ExamScore extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 成绩单ID */
    private Long scoreId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String scoreName;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String scoreFile;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private Long scoreSize;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

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

    public void setScoreName(String scoreName) 
    {
        this.scoreName = scoreName;
    }

    public String getScoreName() 
    {
        return scoreName;
    }

    public void setScoreFile(String scoreFile) 
    {
        this.scoreFile = scoreFile;
    }

    public String getScoreFile() 
    {
        return scoreFile;
    }

    public void setScoreSize(Long scoreSize) 
    {
        this.scoreSize = scoreSize;
    }

    public Long getScoreSize() 
    {
        return scoreSize;
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
            .append("scoreId", getScoreId())
            .append("courseId", getCourseId())
            .append("scoreName", getScoreName())
            .append("scoreFile", getScoreFile())
            .append("scoreSize", getScoreSize())
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
