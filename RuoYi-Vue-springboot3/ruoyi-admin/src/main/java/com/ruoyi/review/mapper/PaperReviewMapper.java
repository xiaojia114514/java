package com.ruoyi.review.mapper;

import java.util.List;
import com.ruoyi.review.domain.PaperReview;

/**
 * 试卷实质审查Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public interface PaperReviewMapper 
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
     * 删除试卷实质审查
     * 
     * @param reviewId 试卷实质审查主键
     * @return 结果
     */
    public int deletePaperReviewByReviewId(Long reviewId);

    /**
     * 批量删除试卷实质审查
     * 
     * @param reviewIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePaperReviewByReviewIds(Long[] reviewIds);
}
