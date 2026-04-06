package com.ruoyi.score.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.score.mapper.ExamScoreMapper;
import com.ruoyi.score.domain.ExamScore;
import com.ruoyi.score.service.IExamScoreService;

/**
 * 考试成绩单Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@Service
public class ExamScoreServiceImpl implements IExamScoreService 
{
    @Autowired
    private ExamScoreMapper examScoreMapper;

    /**
     * 查询考试成绩单
     * 
     * @param scoreId 考试成绩单主键
     * @return 考试成绩单
     */
    @Override
    public ExamScore selectExamScoreByScoreId(Long scoreId)
    {
        return examScoreMapper.selectExamScoreByScoreId(scoreId);
    }

    /**
     * 查询考试成绩单列表
     * 
     * @param examScore 考试成绩单
     * @return 考试成绩单
     */
    @Override
    public List<ExamScore> selectExamScoreList(ExamScore examScore)
    {
        return examScoreMapper.selectExamScoreList(examScore);
    }

    /**
     * 新增考试成绩单
     * 
     * @param examScore 考试成绩单
     * @return 结果
     */
    @Override
    public int insertExamScore(ExamScore examScore)
    {
        examScore.setCreateTime(DateUtils.getNowDate());
        return examScoreMapper.insertExamScore(examScore);
    }

    /**
     * 修改考试成绩单
     * 
     * @param examScore 考试成绩单
     * @return 结果
     */
    @Override
    public int updateExamScore(ExamScore examScore)
    {
        examScore.setUpdateTime(DateUtils.getNowDate());
        return examScoreMapper.updateExamScore(examScore);
    }

    /**
     * 批量删除考试成绩单
     * 
     * @param scoreIds 需要删除的考试成绩单主键
     * @return 结果
     */
    @Override
    public int deleteExamScoreByScoreIds(Long[] scoreIds)
    {
        return examScoreMapper.deleteExamScoreByScoreIds(scoreIds);
    }

    /**
     * 删除考试成绩单信息
     * 
     * @param scoreId 考试成绩单主键
     * @return 结果
     */
    @Override
    public int deleteExamScoreByScoreId(Long scoreId)
    {
        return examScoreMapper.deleteExamScoreByScoreId(scoreId);
    }
}
