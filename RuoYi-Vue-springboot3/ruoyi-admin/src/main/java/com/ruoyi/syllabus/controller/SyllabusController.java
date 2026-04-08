package com.ruoyi.syllabus.controller;

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
import com.ruoyi.syllabus.domain.Syllabus;
import com.ruoyi.syllabus.service.ISyllabusService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 教学大纲Controller
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
@RestController
@RequestMapping("/syllabus/syllabus")
public class SyllabusController extends BaseController
{
    @Autowired
    private ISyllabusService syllabusService;

    /**
     * 查询教学大纲列表
     */
    @PreAuthorize("@ss.hasPermi('syllabus:syllabus:list')")
    @GetMapping("/list")
    public TableDataInfo list(Syllabus syllabus)
    {
        startPage();
        List<Syllabus> list = syllabusService.selectSyllabusList(syllabus);
        return getDataTable(list);
    }

    /**
     * 导出教学大纲列表
     */
    @PreAuthorize("@ss.hasPermi('syllabus:syllabus:export')")
    @Log(title = "教学大纲", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Syllabus syllabus)
    {
        List<Syllabus> list = syllabusService.selectSyllabusList(syllabus);
        ExcelUtil<Syllabus> util = new ExcelUtil<Syllabus>(Syllabus.class);
        util.exportExcel(response, list, "教学大纲数据");
    }

    /**
     * 获取教学大纲详细信息
     */
    @PreAuthorize("@ss.hasPermi('syllabus:syllabus:query')")
    @GetMapping(value = "/{syllabusId}")
    public AjaxResult getInfo(@PathVariable("syllabusId") Long syllabusId)
    {
        return success(syllabusService.selectSyllabusBySyllabusId(syllabusId));
    }

    /**
     * 新增教学大纲
     */
    @PreAuthorize("@ss.hasPermi('syllabus:syllabus:add')")
    @Log(title = "教学大纲", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Syllabus syllabus)
    {
        return toAjax(syllabusService.insertSyllabus(syllabus));
    }

    /**
     * 修改教学大纲
     */
    @PreAuthorize("@ss.hasPermi('syllabus:syllabus:edit')")
    @Log(title = "教学大纲", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Syllabus syllabus)
    {
        return toAjax(syllabusService.updateSyllabus(syllabus));
    }

    /**
     * 删除教学大纲
     */
    @PreAuthorize("@ss.hasPermi('syllabus:syllabus:remove')")
    @Log(title = "教学大纲", businessType = BusinessType.DELETE)
	@DeleteMapping("/{syllabusIds}")
    public AjaxResult remove(@PathVariable Long[] syllabusIds)
    {
        return toAjax(syllabusService.deleteSyllabusBySyllabusIds(syllabusIds));
    }
}
