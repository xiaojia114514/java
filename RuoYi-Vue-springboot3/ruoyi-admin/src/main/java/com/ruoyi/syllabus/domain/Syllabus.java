package com.ruoyi.syllabus.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 教学大纲对象 syllabus
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public class Syllabus extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 大纲ID */
    private Long syllabusId;

    /** 课程ID（唯一约束，允许为空） */
    @Excel(name = "课程ID", readConverterExp = "唯=一约束，允许为空")
    private Long courseId;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String courseName;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String syllabusName;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String syllabusFile;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private Long syllabusSize;

    /** 解析内容 */
    @Excel(name = "解析内容")
    private String parseContent;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setSyllabusId(Long syllabusId) 
    {
        this.syllabusId = syllabusId;
    }

    public Long getSyllabusId() 
    {
        return syllabusId;
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

    public void setSyllabusName(String syllabusName) 
    {
        this.syllabusName = syllabusName;
    }

    public String getSyllabusName() 
    {
        return syllabusName;
    }

    public void setSyllabusFile(String syllabusFile) 
    {
        this.syllabusFile = syllabusFile;
    }

    public String getSyllabusFile() 
    {
        return syllabusFile;
    }

    public void setSyllabusSize(Long syllabusSize) 
    {
        this.syllabusSize = syllabusSize;
    }

    public Long getSyllabusSize() 
    {
        return syllabusSize;
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
            .append("syllabusId", getSyllabusId())
            .append("courseId", getCourseId())
            .append("courseName", getCourseName())
            .append("syllabusName", getSyllabusName())
            .append("syllabusFile", getSyllabusFile())
            .append("syllabusSize", getSyllabusSize())
            .append("parseContent", getParseContent())
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