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
import com.ruoyi.score.domain.ExamScore;
import com.ruoyi.score.service.IExamScoreService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 考试成绩单Controller
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/score/score_list")
public class ExamScoreController extends BaseController
{
    @Autowired
    private IExamScoreService examScoreService;

    /**
     * 查询考试成绩单列表
     */
    @PreAuthorize("@ss.hasPermi('score:score_list:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExamScore examScore)
    {
        startPage();
        List<ExamScore> list = examScoreService.selectExamScoreList(examScore);
        return getDataTable(list);
    }

    /**
     * 导出考试成绩单列表
     */
    @PreAuthorize("@ss.hasPermi('score:score_list:export')")
    @Log(title = "考试成绩单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExamScore examScore)
    {
        List<ExamScore> list = examScoreService.selectExamScoreList(examScore);
        ExcelUtil<ExamScore> util = new ExcelUtil<ExamScore>(ExamScore.class);
        util.exportExcel(response, list, "考试成绩单数据");
    }

    /**
     * 获取考试成绩单详细信息
     */
    @PreAuthorize("@ss.hasPermi('score:score_list:query')")
    @GetMapping(value = "/{scoreId}")
    public AjaxResult getInfo(@PathVariable("scoreId") Long scoreId)
    {
        return success(examScoreService.selectExamScoreByScoreId(scoreId));
    }

    /**
     * 新增考试成绩单
     */
    @PreAuthorize("@ss.hasPermi('score:score_list:add')")
    @Log(title = "考试成绩单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExamScore examScore)
    {
        return toAjax(examScoreService.insertExamScore(examScore));
    }

    /**
     * 修改考试成绩单
     */
    @PreAuthorize("@ss.hasPermi('score:score_list:edit')")
    @Log(title = "考试成绩单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExamScore examScore)
    {
        return toAjax(examScoreService.updateExamScore(examScore));
    }

    /**
     * 删除考试成绩单
     */
    @PreAuthorize("@ss.hasPermi('score:score_list:remove')")
    @Log(title = "考试成绩单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{scoreIds}")
    public AjaxResult remove(@PathVariable Long[] scoreIds)
    {
        return toAjax(examScoreService.deleteExamScoreByScoreIds(scoreIds));
    }
}
