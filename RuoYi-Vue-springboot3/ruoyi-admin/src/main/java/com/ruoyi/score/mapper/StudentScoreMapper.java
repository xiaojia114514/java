package com.ruoyi.score.mapper;

import java.util.List;
import com.ruoyi.score.domain.StudentScore;

/**
 * 学生成绩Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public interface StudentScoreMapper 
{
    /**
     * 查询学生成绩
     * 
     * @param studentScoreId 学生成绩主键
     * @return 学生成绩
     */
    public StudentScore selectStudentScoreByStudentScoreId(Long studentScoreId);

    /**
     * 查询学生成绩列表
     * 
     * @param studentScore 学生成绩
     * @return 学生成绩集合
     */
    public List<StudentScore> selectStudentScoreList(StudentScore studentScore);

    /**
     * 新增学生成绩
     * 
     * @param studentScore 学生成绩
     * @return 结果
     */
    public int insertStudentScore(StudentScore studentScore);

    /**
     * 修改学生成绩
     * 
     * @param studentScore 学生成绩
     * @return 结果
     */
    public int updateStudentScore(StudentScore studentScore);

    /**
     * 删除学生成绩
     * 
     * @param studentScoreId 学生成绩主键
     * @return 结果
     */
    public int deleteStudentScoreByStudentScoreId(Long studentScoreId);

    /**
     * 批量删除学生成绩
     * 
     * @param studentScoreIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStudentScoreByStudentScoreIds(Long[] studentScoreIds);
}
