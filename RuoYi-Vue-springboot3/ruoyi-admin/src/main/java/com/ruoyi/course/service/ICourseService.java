package com.ruoyi.course.service;

import java.util.List;
import com.ruoyi.course.domain.Course;

/**
 * 课程管理Service接口
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public interface ICourseService 
{
    /**
     * 查询课程管理
     * 
     * @param courseId 课程管理主键
     * @return 课程管理
     */
    public Course selectCourseByCourseId(Long courseId);

    /**
     * 查询课程管理列表
     * 
     * @param course 课程管理
     * @return 课程管理集合
     */
    public List<Course> selectCourseList(Course course);

    /**
     * 新增课程管理
     * 
     * @param course 课程管理
     * @return 结果
     */
    public int insertCourse(Course course);

    /**
     * 修改课程管理
     * 
     * @param course 课程管理
     * @return 结果
     */
    public int updateCourse(Course course);

    /**
     * 批量删除课程管理
     * 
     * @param courseIds 需要删除的课程管理主键集合
     * @return 结果
     */
    public int deleteCourseByCourseIds(Long[] courseIds);

    /**
     * 删除课程管理信息
     * 
     * @param courseId 课程管理主键
     * @return 结果
     */
    public int deleteCourseByCourseId(Long courseId);

    /**
     * 生成教学质量分析报告
     * 
     * @param courseId 课程ID
     * @param templateId 模板ID
     * @return 报告生成结果
     * @throws Exception 异常信息
     */
    public String generateQualityReport(Long courseId, Long templateId) throws Exception;
}
