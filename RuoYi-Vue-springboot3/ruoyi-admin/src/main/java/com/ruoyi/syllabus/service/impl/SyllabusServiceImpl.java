package com.ruoyi.syllabus.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.syllabus.mapper.SyllabusMapper;
import com.ruoyi.syllabus.domain.Syllabus;
import com.ruoyi.syllabus.service.ISyllabusService;

/**
 * 教学大纲Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
@Service
public class SyllabusServiceImpl implements ISyllabusService 
{
    @Autowired
    private SyllabusMapper syllabusMapper;

    /**
     * 查询教学大纲
     * 
     * @param syllabusId 教学大纲主键
     * @return 教学大纲
     */
    @Override
    public Syllabus selectSyllabusBySyllabusId(Long syllabusId)
    {
        return syllabusMapper.selectSyllabusBySyllabusId(syllabusId);
    }

    /**
     * 查询教学大纲列表
     * 
     * @param syllabus 教学大纲
     * @return 教学大纲
     */
    @Override
    public List<Syllabus> selectSyllabusList(Syllabus syllabus)
    {
        return syllabusMapper.selectSyllabusList(syllabus);
    }

    /**
     * 新增教学大纲
     * 
     * @param syllabus 教学大纲
     * @return 结果
     */
    @Override
    public int insertSyllabus(Syllabus syllabus)
    {
        syllabus.setCreateTime(DateUtils.getNowDate());
        return syllabusMapper.insertSyllabus(syllabus);
    }

    /**
     * 修改教学大纲
     * 
     * @param syllabus 教学大纲
     * @return 结果
     */
    @Override
    public int updateSyllabus(Syllabus syllabus)
    {
        syllabus.setUpdateTime(DateUtils.getNowDate());
        return syllabusMapper.updateSyllabus(syllabus);
    }

    /**
     * 批量删除教学大纲
     * 
     * @param syllabusIds 需要删除的教学大纲主键
     * @return 结果
     */
    @Override
    public int deleteSyllabusBySyllabusIds(Long[] syllabusIds)
    {
        return syllabusMapper.deleteSyllabusBySyllabusIds(syllabusIds);
    }

    /**
     * 删除教学大纲信息
     * 
     * @param syllabusId 教学大纲主键
     * @return 结果
     */
    @Override
    public int deleteSyllabusBySyllabusId(Long syllabusId)
    {
        return syllabusMapper.deleteSyllabusBySyllabusId(syllabusId);
    }
}
