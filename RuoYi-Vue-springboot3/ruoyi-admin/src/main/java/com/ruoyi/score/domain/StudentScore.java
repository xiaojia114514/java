package com.ruoyi.score.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学生成绩对象 student_score
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public class StudentScore extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 学生成绩ID */
    private Long studentScoreId;

    /** 成绩单ID */
    @Excel(name = "成绩单ID")
    private Long scoreId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 学生ID */
    @Excel(name = "学生ID")
    private String studentId;

    /** 学生姓名 */
    @Excel(name = "学生姓名")
    private String studentName;

    /** 总分 */
    @Excel(name = "总分")
    private BigDecimal totalScore;

    /** 成绩详情（JSON格式） */
    @Excel(name = "成绩详情", readConverterExp = "J=SON格式")
    private String scoreDetails;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setStudentScoreId(Long studentScoreId) 
    {
        this.studentScoreId = studentScoreId;
    }

    public Long getStudentScoreId() 
    {
        return studentScoreId;
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

    public void setStudentId(String studentId) 
    {
        this.studentId = studentId;
    }

    public String getStudentId() 
    {
        return studentId;
    }

    public void setStudentName(String studentName) 
    {
        this.studentName = studentName;
    }

    public String getStudentName() 
    {
        return studentName;
    }

    public void setTotalScore(BigDecimal totalScore) 
    {
        this.totalScore = totalScore;
    }

    public BigDecimal getTotalScore() 
    {
        return totalScore;
    }

    public void setScoreDetails(String scoreDetails) 
    {
        this.scoreDetails = scoreDetails;
    }

    public String getScoreDetails() 
    {
        return scoreDetails;
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
            .append("studentScoreId", getStudentScoreId())
            .append("scoreId", getScoreId())
            .append("courseId", getCourseId())
            .append("studentId", getStudentId())
            .append("studentName", getStudentName())
            .append("totalScore", getTotalScore())
            .append("scoreDetails", getScoreDetails())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
