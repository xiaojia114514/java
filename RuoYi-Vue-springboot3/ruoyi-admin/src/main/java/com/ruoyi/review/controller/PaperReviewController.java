package com.ruoyi.review.controller;

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
import com.ruoyi.review.domain.PaperReview;
import com.ruoyi.review.service.IPaperReviewService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 试卷实质审查Controller
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/review/review")
public class PaperReviewController extends BaseController
{
    @Autowired
    private IPaperReviewService paperReviewService;

    /**
     * 查询试卷实质审查列表
     */
    @PreAuthorize("@ss.hasPermi('review:review:list')")
    @GetMapping("/list")
    public TableDataInfo list(PaperReview paperReview)
    {
        startPage();
        List<PaperReview> list = paperReviewService.selectPaperReviewList(paperReview);
        return getDataTable(list);
    }

    /**
     * 导出试卷实质审查列表
     */
    @PreAuthorize("@ss.hasPermi('review:review:export')")
    @Log(title = "试卷实质审查", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PaperReview paperReview)
    {
        List<PaperReview> list = paperReviewService.selectPaperReviewList(paperReview);
        ExcelUtil<PaperReview> util = new ExcelUtil<PaperReview>(PaperReview.class);
        util.exportExcel(response, list, "试卷实质审查数据");
    }

    /**
     * 获取试卷实质审查详细信息
     */
    @PreAuthorize("@ss.hasPermi('review:review:query')")
    @GetMapping(value = "/{reviewId}")
    public AjaxResult getInfo(@PathVariable("reviewId") Long reviewId)
    {
        return success(paperReviewService.selectPaperReviewByReviewId(reviewId));
    }

    /**
     * 新增试卷实质审查
     */
    @PreAuthorize("@ss.hasPermi('review:review:add')")
    @Log(title = "试卷实质审查", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PaperReview paperReview)
    {
        return toAjax(paperReviewService.insertPaperReview(paperReview));
    }

    /**
     * 修改试卷实质审查
     */
    @PreAuthorize("@ss.hasPermi('review:review:edit')")
    @Log(title = "试卷实质审查", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PaperReview paperReview)
    {
        return toAjax(paperReviewService.updatePaperReview(paperReview));
    }

    /**
     * 删除试卷实质审查
     */
    @PreAuthorize("@ss.hasPermi('review:review:remove')")
    @Log(title = "试卷实质审查", businessType = BusinessType.DELETE)
	@DeleteMapping("/{reviewIds}")
    public AjaxResult remove(@PathVariable Long[] reviewIds)
    {
        return toAjax(paperReviewService.deletePaperReviewByReviewIds(reviewIds));
    }
}
