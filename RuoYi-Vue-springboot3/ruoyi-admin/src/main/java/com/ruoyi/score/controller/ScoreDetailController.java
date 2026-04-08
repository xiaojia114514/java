package com.ruoyi.score.controller;

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
import com.ruoyi.score.domain.ScoreDetail;
import com.ruoyi.score.service.IScoreDetailService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 成绩细目Controller
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
@RestController
@RequestMapping("/score/detail")
public class ScoreDetailController extends BaseController
{
    @Autowired
    private IScoreDetailService scoreDetailService;

    /**
     * 查询成绩细目列表
     */
    @PreAuthorize("@ss.hasPermi('score:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(ScoreDetail scoreDetail)
    {
        startPage();
        List<ScoreDetail> list = scoreDetailService.selectScoreDetailList(scoreDetail);
        return getDataTable(list);
    }

    /**
     * 导出成绩细目列表
     */
    @PreAuthorize("@ss.hasPermi('score:detail:export')")
    @Log(title = "成绩细目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ScoreDetail scoreDetail)
    {
        List<ScoreDetail> list = scoreDetailService.selectScoreDetailList(scoreDetail);
        ExcelUtil<ScoreDetail> util = new ExcelUtil<ScoreDetail>(ScoreDetail.class);
        util.exportExcel(response, list, "成绩细目数据");
    }

    /**
     * 获取成绩细目详细信息
     */
    @PreAuthorize("@ss.hasPermi('score:detail:query')")
    @GetMapping(value = "/{scoreDetailId}")
    public AjaxResult getInfo(@PathVariable("scoreDetailId") Long scoreDetailId)
    {
        return success(scoreDetailService.selectScoreDetailByScoreDetailId(scoreDetailId));
    }

    /**
     * 新增成绩细目
     */
    @PreAuthorize("@ss.hasPermi('score:detail:add')")
    @Log(title = "成绩细目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ScoreDetail scoreDetail)
    {
        return toAjax(scoreDetailService.insertScoreDetail(scoreDetail));
    }

    /**
     * 修改成绩细目
     */
    @PreAuthorize("@ss.hasPermi('score:detail:edit')")
    @Log(title = "成绩细目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ScoreDetail scoreDetail)
    {
        return toAjax(scoreDetailService.updateScoreDetail(scoreDetail));
    }

    /**
     * 删除成绩细目
     */
    @PreAuthorize("@ss.hasPermi('score:detail:remove')")
    @Log(title = "成绩细目", businessType = BusinessType.DELETE)
	@DeleteMapping("/{scoreDetailIds}")
    public AjaxResult remove(@PathVariable Long[] scoreDetailIds)
    {
        return toAjax(scoreDetailService.deleteScoreDetailByScoreDetailIds(scoreDetailIds));
    }
}
