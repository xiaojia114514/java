package com.ruoyi.course.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 课程管理对象 course
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public class Course extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 课程ID */
    private Long courseId;

    /** 课程代码 */
    @Excel(name = "课程代码")
    private String courseCode;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String courseName;

    /** 英文名称 */
    @Excel(name = "英文名称")
    private String courseEnglishName;

    /** 课程性质 */
    @Excel(name = "课程性质")
    private String courseNature;

    /** 学时 */
    @Excel(name = "学时")
    private Long totalHours;

    /** 学分 */
    @Excel(name = "学分")
    private BigDecimal credit;

    /** 开课学期 */
    @Excel(name = "开课学期")
    private String semester;

    /** 授课班级 */
    @Excel(name = "授课班级")
    private String className;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setCourseId(Long courseId) 
    {
        this.courseId = courseId;
    }

    public Long getCourseId() 
    {
        return courseId;
    }

    public void setCourseCode(String courseCode) 
    {
        this.courseCode = courseCode;
    }

    public String getCourseCode() 
    {
        return courseCode;
    }

    public void setCourseName(String courseName) 
    {
        this.courseName = courseName;
    }

    public String getCourseName() 
    {
        return courseName;
    }

    public void setCourseEnglishName(String courseEnglishName) 
    {
        this.courseEnglishName = courseEnglishName;
    }

    public String getCourseEnglishName() 
    {
        return courseEnglishName;
    }

    public void setCourseNature(String courseNature) 
    {
        this.courseNature = courseNature;
    }

    public String getCourseNature() 
    {
        return courseNature;
    }

    public void setTotalHours(Long totalHours) 
    {
        this.totalHours = totalHours;
    }

    public Long getTotalHours() 
    {
        return totalHours;
    }

    public void setCredit(BigDecimal credit) 
    {
        this.credit = credit;
    }

    public BigDecimal getCredit() 
    {
        return credit;
    }

    public void setSemester(String semester) 
    {
        this.semester = semester;
    }

    public String getSemester() 
    {
        return semester;
    }

    public void setClassName(String className) 
    {
        this.className = className;
    }

    public String getClassName() 
    {
        return className;
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
            .append("courseId", getCourseId())
            .append("courseCode", getCourseCode())
            .append("courseName", getCourseName())
            .append("courseEnglishName", getCourseEnglishName())
            .append("courseNature", getCourseNature())
            .append("totalHours", getTotalHours())
            .append("credit", getCredit())
            .append("semester", getSemester())
            .append("className", getClassName())
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
