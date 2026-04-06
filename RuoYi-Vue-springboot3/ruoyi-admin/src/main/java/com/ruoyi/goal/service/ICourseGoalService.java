package com.ruoyi.goal.service;

import java.util.List;
import com.ruoyi.goal.domain.CourseGoal;

/**
 * 课程目标Service接口
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public interface ICourseGoalService 
{
    /**
     * 查询课程目标
     * 
     * @param goalId 课程目标主键
     * @return 课程目标
     */
    public CourseGoal selectCourseGoalByGoalId(Long goalId);

    /**
     * 查询课程目标列表
     * 
     * @param courseGoal 课程目标
     * @return 课程目标集合
     */
    public List<CourseGoal> selectCourseGoalList(CourseGoal courseGoal);

    /**
     * 新增课程目标
     * 
     * @param courseGoal 课程目标
     * @return 结果
     */
    public int insertCourseGoal(CourseGoal courseGoal);

    /**
     * 修改课程目标
     * 
     * @param courseGoal 课程目标
     * @return 结果
     */
    public int updateCourseGoal(CourseGoal courseGoal);

    /**
     * 批量删除课程目标
     * 
     * @param goalIds 需要删除的课程目标主键集合
     * @return 结果
     */
    public int deleteCourseGoalByGoalIds(Long[] goalIds);

    /**
     * 删除课程目标信息
     * 
     * @param goalId 课程目标主键
     * @return 结果
     */
    public int deleteCourseGoalByGoalId(Long goalId);
}
