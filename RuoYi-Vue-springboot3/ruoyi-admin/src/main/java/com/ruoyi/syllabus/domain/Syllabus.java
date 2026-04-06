package com.ruoyi.syllabus.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 教学大纲对象 syllabus
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public class Syllabus extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 大纲ID */
    private Long syllabusId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 大纲名称 */
    @Excel(name = "大纲名称")
    private String syllabusName;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String syllabusFile;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private Long syllabusSize;

    /** 版本号 */
    @Excel(name = "版本号")
    private String syllabusVersion;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

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

    public void setSyllabusVersion(String syllabusVersion) 
    {
        this.syllabusVersion = syllabusVersion;
    }

    public String getSyllabusVersion() 
    {
        return syllabusVersion;
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
            .append("syllabusId", getSyllabusId())
            .append("courseId", getCourseId())
            .append("syllabusName", getSyllabusName())
            .append("syllabusFile", getSyllabusFile())
            .append("syllabusSize", getSyllabusSize())
            .append("syllabusVersion", getSyllabusVersion())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
