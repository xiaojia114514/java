package com.ruoyi.analysis.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.analysis.domain.AchievementAnalysis;
import com.ruoyi.analysis.service.IAchievementAnalysisService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 达成度分析Controller
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/analysis/analysis")
public class AchievementAnalysisController extends BaseController
{
    @Autowired
    private IAchievementAnalysisService achievementAnalysisService;

    /**
     * 查询达成度分析列表
     */
    @PreAuthorize("@ss.hasPermi('analysis:analysis:list')")
    @GetMapping("/list")
    public TableDataInfo list(AchievementAnalysis achievementAnalysis)
    {
        startPage();
        List<AchievementAnalysis> list = achievementAnalysisService.selectAchievementAnalysisList(achievementAnalysis);
        return getDataTable(list);
    }

    /**
     * 导出达成度分析列表
     */
    @PreAuthorize("@ss.hasPermi('analysis:analysis:export')")
    @Log(title = "达成度分析", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AchievementAnalysis achievementAnalysis)
    {
        List<AchievementAnalysis> list = achievementAnalysisService.selectAchievementAnalysisList(achievementAnalysis);
        ExcelUtil<AchievementAnalysis> util = new ExcelUtil<AchievementAnalysis>(AchievementAnalysis.class);
        util.exportExcel(response, list, "达成度分析数据");
    }

    /**
     * 获取达成度分析详细信息
     */
    @PreAuthorize("@ss.hasPermi('analysis:analysis:query')")
    @GetMapping(value = "/{analysisId}")
    public AjaxResult getInfo(@PathVariable("analysisId") Long analysisId)
    {
        return success(achievementAnalysisService.selectAchievementAnalysisByAnalysisId(analysisId));
    }

    /**
     * 新增达成度分析
     */
    @PreAuthorize("@ss.hasPermi('analysis:analysis:add')")
    @Log(title = "达成度分析", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AchievementAnalysis achievementAnalysis)
    {
        return toAjax(achievementAnalysisService.insertAchievementAnalysis(achievementAnalysis));
    }

    /**
     * 修改达成度分析
     */
    @PreAuthorize("@ss.hasPermi('analysis:analysis:edit')")
    @Log(title = "达成度分析", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AchievementAnalysis achievementAnalysis)
    {
        return toAjax(achievementAnalysisService.updateAchievementAnalysis(achievementAnalysis));
    }

    /**
     * 删除达成度分析
     */
    @PreAuthorize("@ss.hasPermi('analysis:analysis:remove')")
    @Log(title = "达成度分析", businessType = BusinessType.DELETE)
	@DeleteMapping("/{analysisIds}")
    public AjaxResult remove(@PathVariable Long[] analysisIds)
    {
        return toAjax(achievementAnalysisService.deleteAchievementAnalysisByAnalysisIds(analysisIds));
    }
}
