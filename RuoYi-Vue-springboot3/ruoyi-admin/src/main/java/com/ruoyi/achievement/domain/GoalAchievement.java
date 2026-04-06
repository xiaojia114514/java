package com.ruoyi.achievement.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 目标达成度对象 goal_achievement
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public class GoalAchievement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 达成度ID */
    private Long achievementId;

    /** 分析ID */
    @Excel(name = "分析ID")
    private Long analysisId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 目标ID */
    @Excel(name = "目标ID")
    private Long goalId;

    /** 达成度（百分比） */
    @Excel(name = "达成度", readConverterExp = "百=分比")
    private BigDecimal achievementRate;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setAchievementId(Long achievementId) 
    {
        this.achievementId = achievementId;
    }

    public Long getAchievementId() 
    {
        return achievementId;
    }

    public void setAnalysisId(Long analysisId) 
    {
        this.analysisId = analysisId;
    }

    public Long getAnalysisId() 
    {
        return analysisId;
    }

    public void setCourseId(Long courseId) 
    {
        this.courseId = courseId;
    }

    public Long getCourseId() 
    {
        return courseId;
    }

    public void setGoalId(Long goalId) 
    {
        this.goalId = goalId;
    }

    public Long getGoalId() 
    {
        return goalId;
    }

    public void setAchievementRate(BigDecimal achievementRate) 
    {
        this.achievementRate = achievementRate;
    }

    public BigDecimal getAchievementRate() 
    {
        return achievementRate;
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
            .append("achievementId", getAchievementId())
            .append("analysisId", getAnalysisId())
            .append("courseId", getCourseId())
            .append("goalId", getGoalId())
            .append("achievementRate", getAchievementRate())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
