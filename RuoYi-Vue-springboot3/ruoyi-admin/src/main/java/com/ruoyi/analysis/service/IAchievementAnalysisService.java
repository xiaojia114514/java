package com.ruoyi.analysis.service;

import java.util.List;
import com.ruoyi.analysis.domain.AchievementAnalysis;

/**
 * 达成度分析Service接口
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public interface IAchievementAnalysisService 
{
    /**
     * 查询达成度分析
     * 
     * @param analysisId 达成度分析主键
     * @return 达成度分析
     */
    public AchievementAnalysis selectAchievementAnalysisByAnalysisId(Long analysisId);

    /**
     * 查询达成度分析列表
     * 
     * @param achievementAnalysis 达成度分析
     * @return 达成度分析集合
     */
    public List<AchievementAnalysis> selectAchievementAnalysisList(AchievementAnalysis achievementAnalysis);

    /**
     * 新增达成度分析
     * 
     * @param achievementAnalysis 达成度分析
     * @return 结果
     */
    public int insertAchievementAnalysis(AchievementAnalysis achievementAnalysis);

    /**
     * 修改达成度分析
     * 
     * @param achievementAnalysis 达成度分析
     * @return 结果
     */
    public int updateAchievementAnalysis(AchievementAnalysis achievementAnalysis);

    /**
     * 批量删除达成度分析
     * 
     * @param analysisIds 需要删除的达成度分析主键集合
     * @return 结果
     */
    public int deleteAchievementAnalysisByAnalysisIds(Long[] analysisIds);

    /**
     * 删除达成度分析信息
     * 
     * @param analysisId 达成度分析主键
     * @return 结果
     */
    public int deleteAchievementAnalysisByAnalysisId(Long analysisId);
}
