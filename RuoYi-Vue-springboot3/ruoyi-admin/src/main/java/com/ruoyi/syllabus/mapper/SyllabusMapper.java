package com.ruoyi.syllabus.mapper;

import java.util.List;
import com.ruoyi.syllabus.domain.Syllabus;

/**
 * 教学大纲Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public interface SyllabusMapper 
{
    /**
     * 查询教学大纲
     * 
     * @param syllabusId 教学大纲主键
     * @return 教学大纲
     */
    public Syllabus selectSyllabusBySyllabusId(Long syllabusId);

    /**
     * 查询教学大纲列表
     * 
     * @param syllabus 教学大纲
     * @return 教学大纲集合
     */
    public List<Syllabus> selectSyllabusList(Syllabus syllabus);

    /**
     * 新增教学大纲
     * 
     * @param syllabus 教学大纲
     * @return 结果
     */
    public int insertSyllabus(Syllabus syllabus);

    /**
     * 修改教学大纲
     * 
     * @param syllabus 教学大纲
     * @return 结果
     */
    public int updateSyllabus(Syllabus syllabus);

    /**
     * 删除教学大纲
     * 
     * @param syllabusId 教学大纲主键
     * @return 结果
     */
    public int deleteSyllabusBySyllabusId(Long syllabusId);

    /**
     * 批量删除教学大纲
     * 
     * @param syllabusIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSyllabusBySyllabusIds(Long[] syllabusIds);
}
