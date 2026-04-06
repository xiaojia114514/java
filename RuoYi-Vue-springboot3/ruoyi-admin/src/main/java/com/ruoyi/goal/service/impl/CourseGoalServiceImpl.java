package com.ruoyi.goal.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.goal.mapper.CourseGoalMapper;
import com.ruoyi.goal.domain.CourseGoal;
import com.ruoyi.goal.service.ICourseGoalService;

/**
 * 课程目标Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@Service
public class CourseGoalServiceImpl implements ICourseGoalService 
{
    @Autowired
    private CourseGoalMapper courseGoalMapper;

    /**
     * 查询课程目标
     * 
     * @param goalId 课程目标主键
     * @return 课程目标
     */
    @Override
    public CourseGoal selectCourseGoalByGoalId(Long goalId)
    {
        return courseGoalMapper.selectCourseGoalByGoalId(goalId);
    }

    /**
     * 查询课程目标列表
     * 
     * @param courseGoal 课程目标
     * @return 课程目标
     */
    @Override
    public List<CourseGoal> selectCourseGoalList(CourseGoal courseGoal)
    {
        return courseGoalMapper.selectCourseGoalList(courseGoal);
    }

    /**
     * 新增课程目标
     * 
     * @param courseGoal 课程目标
     * @return 结果
     */
    @Override
    public int insertCourseGoal(CourseGoal courseGoal)
    {
        courseGoal.setCreateTime(DateUtils.getNowDate());
        return courseGoalMapper.insertCourseGoal(courseGoal);
    }

    /**
     * 修改课程目标
     * 
     * @param courseGoal 课程目标
     * @return 结果
     */
    @Override
    public int updateCourseGoal(CourseGoal courseGoal)
    {
        courseGoal.setUpdateTime(DateUtils.getNowDate());
        return courseGoalMapper.updateCourseGoal(courseGoal);
    }

    /**
     * 批量删除课程目标
     * 
     * @param goalIds 需要删除的课程目标主键
     * @return 结果
     */
    @Override
    public int deleteCourseGoalByGoalIds(Long[] goalIds)
    {
        return courseGoalMapper.deleteCourseGoalByGoalIds(goalIds);
    }

    /**
     * 删除课程目标信息
     * 
     * @param goalId 课程目标主键
     * @return 结果
     */
    @Override
    public int deleteCourseGoalByGoalId(Long goalId)
    {
        return courseGoalMapper.deleteCourseGoalByGoalId(goalId);
    }
}
