package com.ruoyi.goal.controller;

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
import com.ruoyi.goal.domain.CourseGoal;
import com.ruoyi.goal.service.ICourseGoalService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 课程目标Controller
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/goal/goal")
public class CourseGoalController extends BaseController
{
    @Autowired
    private ICourseGoalService courseGoalService;

    /**
     * 查询课程目标列表
     */
    @PreAuthorize("@ss.hasPermi('goal:goal:list')")
    @GetMapping("/list")
    public TableDataInfo list(CourseGoal courseGoal)
    {
        startPage();
        List<CourseGoal> list = courseGoalService.selectCourseGoalList(courseGoal);
        return getDataTable(list);
    }

    /**
     * 导出课程目标列表
     */
    @PreAuthorize("@ss.hasPermi('goal:goal:export')")
    @Log(title = "课程目标", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CourseGoal courseGoal)
    {
        List<CourseGoal> list = courseGoalService.selectCourseGoalList(courseGoal);
        ExcelUtil<CourseGoal> util = new ExcelUtil<CourseGoal>(CourseGoal.class);
        util.exportExcel(response, list, "课程目标数据");
    }

    /**
     * 获取课程目标详细信息
     */
    @PreAuthorize("@ss.hasPermi('goal:goal:query')")
    @GetMapping(value = "/{goalId}")
    public AjaxResult getInfo(@PathVariable("goalId") Long goalId)
    {
        return success(courseGoalService.selectCourseGoalByGoalId(goalId));
    }

    /**
     * 新增课程目标
     */
    @PreAuthorize("@ss.hasPermi('goal:goal:add')")
    @Log(title = "课程目标", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CourseGoal courseGoal)
    {
        return toAjax(courseGoalService.insertCourseGoal(courseGoal));
    }

    /**
     * 修改课程目标
     */
    @PreAuthorize("@ss.hasPermi('goal:goal:edit')")
    @Log(title = "课程目标", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CourseGoal courseGoal)
    {
        return toAjax(courseGoalService.updateCourseGoal(courseGoal));
    }

    /**
     * 删除课程目标
     */
    @PreAuthorize("@ss.hasPermi('goal:goal:remove')")
    @Log(title = "课程目标", businessType = BusinessType.DELETE)
	@DeleteMapping("/{goalIds}")
    public AjaxResult remove(@PathVariable Long[] goalIds)
    {
        return toAjax(courseGoalService.deleteCourseGoalByGoalIds(goalIds));
    }
}
