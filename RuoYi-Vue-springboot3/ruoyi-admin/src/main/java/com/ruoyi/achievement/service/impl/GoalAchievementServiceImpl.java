package com.ruoyi.achievement.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.achievement.mapper.GoalAchievementMapper;
import com.ruoyi.achievement.domain.GoalAchievement;
import com.ruoyi.achievement.service.IGoalAchievementService;

/**
 * 目标达成度Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@Service
public class GoalAchievementServiceImpl implements IGoalAchievementService 
{
    @Autowired
    private GoalAchievementMapper goalAchievementMapper;

    /**
     * 查询目标达成度
     * 
     * @param achievementId 目标达成度主键
     * @return 目标达成度
     */
    @Override
    public GoalAchievement selectGoalAchievementByAchievementId(Long achievementId)
    {
        return goalAchievementMapper.selectGoalAchievementByAchievementId(achievementId);
    }

    /**
     * 查询目标达成度列表
     * 
     * @param goalAchievement 目标达成度
     * @return 目标达成度
     */
    @Override
    public List<GoalAchievement> selectGoalAchievementList(GoalAchievement goalAchievement)
    {
        return goalAchievementMapper.selectGoalAchievementList(goalAchievement);
    }

    /**
     * 新增目标达成度
     * 
     * @param goalAchievement 目标达成度
     * @return 结果
     */
    @Override
    public int insertGoalAchievement(GoalAchievement goalAchievement)
    {
        goalAchievement.setCreateTime(DateUtils.getNowDate());
        return goalAchievementMapper.insertGoalAchievement(goalAchievement);
    }

    /**
     * 修改目标达成度
     * 
     * @param goalAchievement 目标达成度
     * @return 结果
     */
    @Override
    public int updateGoalAchievement(GoalAchievement goalAchievement)
    {
        goalAchievement.setUpdateTime(DateUtils.getNowDate());
        return goalAchievementMapper.updateGoalAchievement(goalAchievement);
    }

    /**
     * 批量删除目标达成度
     * 
     * @param achievementIds 需要删除的目标达成度主键
     * @return 结果
     */
    @Override
    public int deleteGoalAchievementByAchievementIds(Long[] achievementIds)
    {
        return goalAchievementMapper.deleteGoalAchievementByAchievementIds(achievementIds);
    }

    /**
     * 删除目标达成度信息
     * 
     * @param achievementId 目标达成度主键
     * @return 结果
     */
    @Override
    public int deleteGoalAchievementByAchievementId(Long achievementId)
    {
        return goalAchievementMapper.deleteGoalAchievementByAchievementId(achievementId);
    }
}
