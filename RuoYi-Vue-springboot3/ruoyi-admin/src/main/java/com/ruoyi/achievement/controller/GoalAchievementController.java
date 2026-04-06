package com.ruoyi.achievement.controller;

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
import com.ruoyi.achievement.domain.GoalAchievement;
import com.ruoyi.achievement.service.IGoalAchievementService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 目标达成度Controller
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/achievement/achievement")
public class GoalAchievementController extends BaseController
{
    @Autowired
    private IGoalAchievementService goalAchievementService;

    /**
     * 查询目标达成度列表
     */
    @PreAuthorize("@ss.hasPermi('achievement:achievement:list')")
    @GetMapping("/list")
    public TableDataInfo list(GoalAchievement goalAchievement)
    {
        startPage();
        List<GoalAchievement> list = goalAchievementService.selectGoalAchievementList(goalAchievement);
        return getDataTable(list);
    }

    /**
     * 导出目标达成度列表
     */
    @PreAuthorize("@ss.hasPermi('achievement:achievement:export')")
    @Log(title = "目标达成度", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GoalAchievement goalAchievement)
    {
        List<GoalAchievement> list = goalAchievementService.selectGoalAchievementList(goalAchievement);
        ExcelUtil<GoalAchievement> util = new ExcelUtil<GoalAchievement>(GoalAchievement.class);
        util.exportExcel(response, list, "目标达成度数据");
    }

    /**
     * 获取目标达成度详细信息
     */
    @PreAuthorize("@ss.hasPermi('achievement:achievement:query')")
    @GetMapping(value = "/{achievementId}")
    public AjaxResult getInfo(@PathVariable("achievementId") Long achievementId)
    {
        return success(goalAchievementService.selectGoalAchievementByAchievementId(achievementId));
    }

    /**
     * 新增目标达成度
     */
    @PreAuthorize("@ss.hasPermi('achievement:achievement:add')")
    @Log(title = "目标达成度", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GoalAchievement goalAchievement)
    {
        return toAjax(goalAchievementService.insertGoalAchievement(goalAchievement));
    }

    /**
     * 修改目标达成度
     */
    @PreAuthorize("@ss.hasPermi('achievement:achievement:edit')")
    @Log(title = "目标达成度", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GoalAchievement goalAchievement)
    {
        return toAjax(goalAchievementService.updateGoalAchievement(goalAchievement));
    }

    /**
     * 删除目标达成度
     */
    @PreAuthorize("@ss.hasPermi('achievement:achievement:remove')")
    @Log(title = "目标达成度", businessType = BusinessType.DELETE)
	@DeleteMapping("/{achievementIds}")
    public AjaxResult remove(@PathVariable Long[] achievementIds)
    {
        return toAjax(goalAchievementService.deleteGoalAchievementByAchievementIds(achievementIds));
    }
}
