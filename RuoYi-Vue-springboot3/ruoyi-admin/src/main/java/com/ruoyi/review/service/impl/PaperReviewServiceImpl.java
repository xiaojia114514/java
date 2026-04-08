package com.ruoyi.review.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.review.mapper.PaperReviewMapper;
import com.ruoyi.review.domain.PaperReview;
import com.ruoyi.review.service.IPaperReviewService;

/**
 * 试卷实质审查Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
@Service
public class PaperReviewServiceImpl implements IPaperReviewService 
{
    @Autowired
    private PaperReviewMapper paperReviewMapper;

    /**
     * 查询试卷实质审查
     * 
     * @param reviewId 试卷实质审查主键
     * @return 试卷实质审查
     */
    @Override
    public PaperReview selectPaperReviewByReviewId(Long reviewId)
    {
        return paperReviewMapper.selectPaperReviewByReviewId(reviewId);
    }

    /**
     * 查询试卷实质审查列表
     * 
     * @param paperReview 试卷实质审查
     * @return 试卷实质审查
     */
    @Override
    public List<PaperReview> selectPaperReviewList(PaperReview paperReview)
    {
        return paperReviewMapper.selectPaperReviewList(paperReview);
    }

    /**
     * 新增试卷实质审查
     * 
     * @param paperReview 试卷实质审查
     * @return 结果
     */
    @Override
    public int insertPaperReview(PaperReview paperReview)
    {
        paperReview.setCreateTime(DateUtils.getNowDate());
        return paperReviewMapper.insertPaperReview(paperReview);
    }

    /**
     * 修改试卷实质审查
     * 
     * @param paperReview 试卷实质审查
     * @return 结果
     */
    @Override
    public int updatePaperReview(PaperReview paperReview)
    {
        paperReview.setUpdateTime(DateUtils.getNowDate());
        return paperReviewMapper.updatePaperReview(paperReview);
    }

    /**
     * 批量删除试卷实质审查
     * 
     * @param reviewIds 需要删除的试卷实质审查主键
     * @return 结果
     */
    @Override
    public int deletePaperReviewByReviewIds(Long[] reviewIds)
    {
        return paperReviewMapper.deletePaperReviewByReviewIds(reviewIds);
    }

    /**
     * 删除试卷实质审查信息
     * 
     * @param reviewId 试卷实质审查主键
     * @return 结果
     */
    @Override
    public int deletePaperReviewByReviewId(Long reviewId)
    {
        return paperReviewMapper.deletePaperReviewByReviewId(reviewId);
    }
}
