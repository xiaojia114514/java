package com.ruoyi.score.service;

import java.util.List;
import com.ruoyi.score.domain.ScoreDetail;

/**
 * 成绩细目Service接口
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public interface IScoreDetailService 
{
    /**
     * 查询成绩细目
     * 
     * @param scoreDetailId 成绩细目主键
     * @return 成绩细目
     */
    public ScoreDetail selectScoreDetailByScoreDetailId(Long scoreDetailId);

    /**
     * 查询成绩细目列表
     * 
     * @param scoreDetail 成绩细目
     * @return 成绩细目集合
     */
    public List<ScoreDetail> selectScoreDetailList(ScoreDetail scoreDetail);

    /**
     * 新增成绩细目
     * 
     * @param scoreDetail 成绩细目
     * @return 结果
     */
    public int insertScoreDetail(ScoreDetail scoreDetail);

    /**
     * 修改成绩细目
     * 
     * @param scoreDetail 成绩细目
     * @return 结果
     */
    public int updateScoreDetail(ScoreDetail scoreDetail);

    /**
     * 批量删除成绩细目
     * 
     * @param scoreDetailIds 需要删除的成绩细目主键集合
     * @return 结果
     */
    public int deleteScoreDetailByScoreDetailIds(Long[] scoreDetailIds);

    /**
     * 删除成绩细目信息
     * 
     * @param scoreDetailId 成绩细目主键
     * @return 结果
     */
    public int deleteScoreDetailByScoreDetailId(Long scoreDetailId);
}
