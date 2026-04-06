package com.ruoyi.goal.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 课程目标对象 course_goal
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public class CourseGoal extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 目标ID */
    private Long goalId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 目标代码 */
    @Excel(name = "目标代码")
    private String goalCode;

    /** 毕业要求 */
    @Excel(name = "毕业要求")
    private String graduationRequirement;

    /** 指标点 */
    @Excel(name = "指标点")
    private String indicatorPoint;

    /** 目标描述 */
    @Excel(name = "目标描述")
    private String goalDescription;

    /** 目标重要程度（high高 medium中 normal低） */
    @Excel(name = "目标重要程度", readConverterExp = "h=igh高,m=edium中,n=ormal低")
    private String goalLevel;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Long orderNum;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setGoalId(Long goalId) 
    {
        this.goalId = goalId;
    }

    public Long getGoalId() 
    {
        return goalId;
    }

    public void setCourseId(Long courseId) 
    {
        this.courseId = courseId;
    }

    public Long getCourseId() 
    {
        return courseId;
    }

    public void setGoalCode(String goalCode) 
    {
        this.goalCode = goalCode;
    }

    public String getGoalCode() 
    {
        return goalCode;
    }

    public void setGraduationRequirement(String graduationRequirement) 
    {
        this.graduationRequirement = graduationRequirement;
    }

    public String getGraduationRequirement() 
    {
        return graduationRequirement;
    }

    public void setIndicatorPoint(String indicatorPoint) 
    {
        this.indicatorPoint = indicatorPoint;
    }

    public String getIndicatorPoint() 
    {
        return indicatorPoint;
    }

    public void setGoalDescription(String goalDescription) 
    {
        this.goalDescription = goalDescription;
    }

    public String getGoalDescription() 
    {
        return goalDescription;
    }

    public void setGoalLevel(String goalLevel) 
    {
        this.goalLevel = goalLevel;
    }

    public String getGoalLevel() 
    {
        return goalLevel;
    }

    public void setOrderNum(Long orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() 
    {
        return orderNum;
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
            .append("goalId", getGoalId())
            .append("courseId", getCourseId())
            .append("goalCode", getGoalCode())
            .append("graduationRequirement", getGraduationRequirement())
            .append("indicatorPoint", getIndicatorPoint())
            .append("goalDescription", getGoalDescription())
            .append("goalLevel", getGoalLevel())
            .append("orderNum", getOrderNum())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
