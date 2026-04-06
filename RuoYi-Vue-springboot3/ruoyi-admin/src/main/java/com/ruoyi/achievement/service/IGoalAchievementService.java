package com.ruoyi.achievement.service;

import java.util.List;
import com.ruoyi.achievement.domain.GoalAchievement;

/**
 * 目标达成度Service接口
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public interface IGoalAchievementService 
{
    /**
     * 查询目标达成度
     * 
     * @param achievementId 目标达成度主键
     * @return 目标达成度
     */
    public GoalAchievement selectGoalAchievementByAchievementId(Long achievementId);

    /**
     * 查询目标达成度列表
     * 
     * @param goalAchievement 目标达成度
     * @return 目标达成度集合
     */
    public List<GoalAchievement> selectGoalAchievementList(GoalAchievement goalAchievement);

    /**
     * 新增目标达成度
     * 
     * @param goalAchievement 目标达成度
     * @return 结果
     */
    public int insertGoalAchievement(GoalAchievement goalAchievement);

    /**
     * 修改目标达成度
     * 
     * @param goalAchievement 目标达成度
     * @return 结果
     */
    public int updateGoalAchievement(GoalAchievement goalAchievement);

    /**
     * 批量删除目标达成度
     * 
     * @param achievementIds 需要删除的目标达成度主键集合
     * @return 结果
     */
    public int deleteGoalAchievementByAchievementIds(Long[] achievementIds);

    /**
     * 删除目标达成度信息
     * 
     * @param achievementId 目标达成度主键
     * @return 结果
     */
    public int deleteGoalAchievementByAchievementId(Long achievementId);
}
