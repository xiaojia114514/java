package com.ruoyi.review.service;

import java.util.List;
import com.ruoyi.review.domain.PaperReview;

/**
 * 试卷实质审查Service接口
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public interface IPaperReviewService 
{
    /**
     * 查询试卷实质审查
     * 
     * @param reviewId 试卷实质审查主键
     * @return 试卷实质审查
     */
    public PaperReview selectPaperReviewByReviewId(Long reviewId);

    /**
     * 查询试卷实质审查列表
     * 
     * @param paperReview 试卷实质审查
     * @return 试卷实质审查集合
     */
    public List<PaperReview> selectPaperReviewList(PaperReview paperReview);

    /**
     * 新增试卷实质审查
     * 
     * @param paperReview 试卷实质审查
     * @return 结果
     */
    public int insertPaperReview(PaperReview paperReview);

    /**
     * 修改试卷实质审查
     * 
     * @param paperReview 试卷实质审查
     * @return 结果
     */
    public int updatePaperReview(PaperReview paperReview);

    /**
     * 批量删除试卷实质审查
     * 
     * @param reviewIds 需要删除的试卷实质审查主键集合
     * @return 结果
     */
    public int deletePaperReviewByReviewIds(Long[] reviewIds);

    /**
     * 删除试卷实质审查信息
     * 
     * @param reviewId 试卷实质审查主键
     * @return 结果
     */
    public int deletePaperReviewByReviewId(Long reviewId);
}
