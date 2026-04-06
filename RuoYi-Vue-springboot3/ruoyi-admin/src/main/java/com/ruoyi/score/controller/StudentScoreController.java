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
import com.ruoyi.score.domain.StudentScore;
import com.ruoyi.score.service.IStudentScoreService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 学生成绩Controller
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/score/score")
public class StudentScoreController extends BaseController
{
    @Autowired
    private IStudentScoreService studentScoreService;

    /**
     * 查询学生成绩列表
     */
    @PreAuthorize("@ss.hasPermi('score:score:list')")
    @GetMapping("/list")
    public TableDataInfo list(StudentScore studentScore)
    {
        startPage();
        List<StudentScore> list = studentScoreService.selectStudentScoreList(studentScore);
        return getDataTable(list);
    }

    /**
     * 导出学生成绩列表
     */
    @PreAuthorize("@ss.hasPermi('score:score:export')")
    @Log(title = "学生成绩", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StudentScore studentScore)
    {
        List<StudentScore> list = studentScoreService.selectStudentScoreList(studentScore);
        ExcelUtil<StudentScore> util = new ExcelUtil<StudentScore>(StudentScore.class);
        util.exportExcel(response, list, "学生成绩数据");
    }

    /**
     * 获取学生成绩详细信息
     */
    @PreAuthorize("@ss.hasPermi('score:score:query')")
    @GetMapping(value = "/{studentScoreId}")
    public AjaxResult getInfo(@PathVariable("studentScoreId") Long studentScoreId)
    {
        return success(studentScoreService.selectStudentScoreByStudentScoreId(studentScoreId));
    }

    /**
     * 新增学生成绩
     */
    @PreAuthorize("@ss.hasPermi('score:score:add')")
    @Log(title = "学生成绩", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StudentScore studentScore)
    {
        return toAjax(studentScoreService.insertStudentScore(studentScore));
    }

    /**
     * 修改学生成绩
     */
    @PreAuthorize("@ss.hasPermi('score:score:edit')")
    @Log(title = "学生成绩", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StudentScore studentScore)
    {
        return toAjax(studentScoreService.updateStudentScore(studentScore));
    }

    /**
     * 删除学生成绩
     */
    @PreAuthorize("@ss.hasPermi('score:score:remove')")
    @Log(title = "学生成绩", businessType = BusinessType.DELETE)
	@DeleteMapping("/{studentScoreIds}")
    public AjaxResult remove(@PathVariable Long[] studentScoreIds)
    {
        return toAjax(studentScoreService.deleteStudentScoreByStudentScoreIds(studentScoreIds));
    }
}
