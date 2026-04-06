package com.ruoyi.score.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.score.mapper.StudentScoreMapper;
import com.ruoyi.score.domain.StudentScore;
import com.ruoyi.score.service.IStudentScoreService;

/**
 * 学生成绩Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@Service
public class StudentScoreServiceImpl implements IStudentScoreService 
{
    @Autowired
    private StudentScoreMapper studentScoreMapper;

    /**
     * 查询学生成绩
     * 
     * @param studentScoreId 学生成绩主键
     * @return 学生成绩
     */
    @Override
    public StudentScore selectStudentScoreByStudentScoreId(Long studentScoreId)
    {
        return studentScoreMapper.selectStudentScoreByStudentScoreId(studentScoreId);
    }

    /**
     * 查询学生成绩列表
     * 
     * @param studentScore 学生成绩
     * @return 学生成绩
     */
    @Override
    public List<StudentScore> selectStudentScoreList(StudentScore studentScore)
    {
        return studentScoreMapper.selectStudentScoreList(studentScore);
    }

    /**
     * 新增学生成绩
     * 
     * @param studentScore 学生成绩
     * @return 结果
     */
    @Override
    public int insertStudentScore(StudentScore studentScore)
    {
        studentScore.setCreateTime(DateUtils.getNowDate());
        return studentScoreMapper.insertStudentScore(studentScore);
    }

    /**
     * 修改学生成绩
     * 
     * @param studentScore 学生成绩
     * @return 结果
     */
    @Override
    public int updateStudentScore(StudentScore studentScore)
    {
        studentScore.setUpdateTime(DateUtils.getNowDate());
        return studentScoreMapper.updateStudentScore(studentScore);
    }

    /**
     * 批量删除学生成绩
     * 
     * @param studentScoreIds 需要删除的学生成绩主键
     * @return 结果
     */
    @Override
    public int deleteStudentScoreByStudentScoreIds(Long[] studentScoreIds)
    {
        return studentScoreMapper.deleteStudentScoreByStudentScoreIds(studentScoreIds);
    }

    /**
     * 删除学生成绩信息
     * 
     * @param studentScoreId 学生成绩主键
     * @return 结果
     */
    @Override
    public int deleteStudentScoreByStudentScoreId(Long studentScoreId)
    {
        return studentScoreMapper.deleteStudentScoreByStudentScoreId(studentScoreId);
    }
}
