package com.ruoyi.analysis.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.analysis.mapper.AchievementAnalysisMapper;
import com.ruoyi.analysis.domain.AchievementAnalysis;
import com.ruoyi.analysis.service.IAchievementAnalysisService;

/**
 * 达成度分析Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@Service
public class AchievementAnalysisServiceImpl implements IAchievementAnalysisService 
{
    @Autowired
    private AchievementAnalysisMapper achievementAnalysisMapper;

    /**
     * 查询达成度分析
     * 
     * @param analysisId 达成度分析主键
     * @return 达成度分析
     */
    @Override
    public AchievementAnalysis selectAchievementAnalysisByAnalysisId(Long analysisId)
    {
        return achievementAnalysisMapper.selectAchievementAnalysisByAnalysisId(analysisId);
    }

    /**
     * 查询达成度分析列表
     * 
     * @param achievementAnalysis 达成度分析
     * @return 达成度分析
     */
    @Override
    public List<AchievementAnalysis> selectAchievementAnalysisList(AchievementAnalysis achievementAnalysis)
    {
        return achievementAnalysisMapper.selectAchievementAnalysisList(achievementAnalysis);
    }

    /**
     * 新增达成度分析
     * 
     * @param achievementAnalysis 达成度分析
     * @return 结果
     */
    @Override
    public int insertAchievementAnalysis(AchievementAnalysis achievementAnalysis)
    {
        achievementAnalysis.setCreateTime(DateUtils.getNowDate());
        return achievementAnalysisMapper.insertAchievementAnalysis(achievementAnalysis);
    }

    /**
     * 修改达成度分析
     * 
     * @param achievementAnalysis 达成度分析
     * @return 结果
     */
    @Override
    public int updateAchievementAnalysis(AchievementAnalysis achievementAnalysis)
    {
        achievementAnalysis.setUpdateTime(DateUtils.getNowDate());
        return achievementAnalysisMapper.updateAchievementAnalysis(achievementAnalysis);
    }

    /**
     * 批量删除达成度分析
     * 
     * @param analysisIds 需要删除的达成度分析主键
     * @return 结果
     */
    @Override
    public int deleteAchievementAnalysisByAnalysisIds(Long[] analysisIds)
    {
        return achievementAnalysisMapper.deleteAchievementAnalysisByAnalysisIds(analysisIds);
    }

    /**
     * 删除达成度分析信息
     * 
     * @param analysisId 达成度分析主键
     * @return 结果
     */
    @Override
    public int deleteAchievementAnalysisByAnalysisId(Long analysisId)
    {
        return achievementAnalysisMapper.deleteAchievementAnalysisByAnalysisId(analysisId);
    }
}
