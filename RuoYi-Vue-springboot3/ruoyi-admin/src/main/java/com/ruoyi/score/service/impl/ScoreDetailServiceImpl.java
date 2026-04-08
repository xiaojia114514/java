package com.ruoyi.score.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.score.mapper.ScoreDetailMapper;
import com.ruoyi.score.domain.ScoreDetail;
import com.ruoyi.score.service.IScoreDetailService;

/**
 * 成绩细目Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
@Service
public class ScoreDetailServiceImpl implements IScoreDetailService 
{
    @Autowired
    private ScoreDetailMapper scoreDetailMapper;

    /**
     * 查询成绩细目
     * 
     * @param scoreDetailId 成绩细目主键
     * @return 成绩细目
     */
    @Override
    public ScoreDetail selectScoreDetailByScoreDetailId(Long scoreDetailId)
    {
        return scoreDetailMapper.selectScoreDetailByScoreDetailId(scoreDetailId);
    }

    /**
     * 查询成绩细目列表
     * 
     * @param scoreDetail 成绩细目
     * @return 成绩细目
     */
    @Override
    public List<ScoreDetail> selectScoreDetailList(ScoreDetail scoreDetail)
    {
        return scoreDetailMapper.selectScoreDetailList(scoreDetail);
    }

    /**
     * 新增成绩细目
     * 
     * @param scoreDetail 成绩细目
     * @return 结果
     */
    @Override
    public int insertScoreDetail(ScoreDetail scoreDetail)
    {
        scoreDetail.setCreateTime(DateUtils.getNowDate());
        return scoreDetailMapper.insertScoreDetail(scoreDetail);
    }

    /**
     * 修改成绩细目
     * 
     * @param scoreDetail 成绩细目
     * @return 结果
     */
    @Override
    public int updateScoreDetail(ScoreDetail scoreDetail)
    {
        scoreDetail.setUpdateTime(DateUtils.getNowDate());
        return scoreDetailMapper.updateScoreDetail(scoreDetail);
    }

    /**
     * 批量删除成绩细目
     * 
     * @param scoreDetailIds 需要删除的成绩细目主键
     * @return 结果
     */
    @Override
    public int deleteScoreDetailByScoreDetailIds(Long[] scoreDetailIds)
    {
        return scoreDetailMapper.deleteScoreDetailByScoreDetailIds(scoreDetailIds);
    }

    /**
     * 删除成绩细目信息
     * 
     * @param scoreDetailId 成绩细目主键
     * @return 结果
     */
    @Override
    public int deleteScoreDetailByScoreDetailId(Long scoreDetailId)
    {
        return scoreDetailMapper.deleteScoreDetailByScoreDetailId(scoreDetailId);
    }
}
