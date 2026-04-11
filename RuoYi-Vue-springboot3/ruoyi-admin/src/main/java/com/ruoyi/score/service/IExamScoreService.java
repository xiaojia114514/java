package com.ruoyi.score.service;

import java.util.List;
import com.ruoyi.score.domain.ExamScore;

/**
 * 考试成绩单Service接口
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public interface IExamScoreService 
{
    /**
     * 查询考试成绩单
     * 
     * @param scoreId 考试成绩单主键
     * @return 考试成绩单
     */
    public ExamScore selectExamScoreByScoreId(Long scoreId);

    /**
     * 查询考试成绩单列表
     * 
     * @param examScore 考试成绩单
     * @return 考试成绩单集合
     */
    public List<ExamScore> selectExamScoreList(ExamScore examScore);

    /**
     * 新增考试成绩单
     * 
     * @param examScore 考试成绩单
     * @return 结果
     */
    public int insertExamScore(ExamScore examScore);

    /**
     * 修改考试成绩单
     * 
     * @param examScore 考试成绩单
     * @return 结果
     */
    public int updateExamScore(ExamScore examScore);

    /**
     * 批量删除考试成绩单
     * 
     * @param scoreIds 需要删除的考试成绩单主键集合
     * @return 结果
     */
    public int deleteExamScoreByScoreIds(Long[] scoreIds);

    /**
     * 删除考试成绩单信息
     * 
     * @param scoreId 考试成绩单主键
     * @return 结果
     */
    public int deleteExamScoreByScoreId(Long scoreId);

    /**
     * 生成成绩细目表
     * 
     * @param scoreId 考试成绩单主键
     * @return 下载链接
     * @throws Exception 异常信息
     */
    public String generateDetail(Long scoreId) throws Exception;
}
