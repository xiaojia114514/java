package com.ruoyi.score.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 考试成绩单对象 exam_score
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public class ExamScore extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 成绩单ID */
    private Long scoreId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String scoreFile;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private Long scoreSize;

    /** 考试日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "考试日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date examDate;

    /** 学生总数 */
    @Excel(name = "学生总数")
    private Long totalStudents;

    /** 平均成绩 */
    @Excel(name = "平均成绩")
    private BigDecimal averageScore;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

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

    public void setExamDate(Date examDate) 
    {
        this.examDate = examDate;
    }

    public Date getExamDate() 
    {
        return examDate;
    }

    public void setTotalStudents(Long totalStudents) 
    {
        this.totalStudents = totalStudents;
    }

    public Long getTotalStudents() 
    {
        return totalStudents;
    }

    public void setAverageScore(BigDecimal averageScore) 
    {
        this.averageScore = averageScore;
    }

    public BigDecimal getAverageScore() 
    {
        return averageScore;
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
            .append("scoreId", getScoreId())
            .append("courseId", getCourseId())
            .append("scoreFile", getScoreFile())
            .append("scoreSize", getScoreSize())
            .append("examDate", getExamDate())
            .append("totalStudents", getTotalStudents())
            .append("averageScore", getAverageScore())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
