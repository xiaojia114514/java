package com.ruoyi.course.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ZhipuAiFileParser;
import com.ruoyi.syllabus.domain.Syllabus;
import com.ruoyi.syllabus.mapper.SyllabusMapper;
import com.ruoyi.review.domain.PaperReview;
import com.ruoyi.review.mapper.PaperReviewMapper;
import com.ruoyi.score.domain.ExamScore;
import com.ruoyi.score.mapper.ExamScoreMapper;
import com.ruoyi.score.domain.ScoreDetail;
import com.ruoyi.score.mapper.ScoreDetailMapper;
import com.ruoyi.report.domain.QualityReportTemplate;
import com.ruoyi.report.mapper.QualityReportTemplateMapper;
import com.ruoyi.report.domain.QualityReport;
import com.ruoyi.report.mapper.QualityReportMapper;
import ai.z.openapi.ZhipuAiClient;
import ai.z.openapi.service.model.ChatCompletionCreateParams;
import ai.z.openapi.service.model.ChatMessage;
import ai.z.openapi.service.model.ChatMessageRole;
import ai.z.openapi.service.model.ChatCompletionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.course.mapper.CourseMapper;
import com.ruoyi.course.domain.Course;
import com.ruoyi.course.service.ICourseService;

/**
 * 课程管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
@Service
public class CourseServiceImpl implements ICourseService 
{
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private SyllabusMapper syllabusMapper;

    @Autowired
    private PaperReviewMapper paperReviewMapper;

    @Autowired
    private ExamScoreMapper examScoreMapper;

    @Autowired
    private ScoreDetailMapper scoreDetailMapper;

    @Autowired
    private QualityReportTemplateMapper qualityReportTemplateMapper;

    @Autowired
    private QualityReportMapper qualityReportMapper;

    // 智谱API密钥
    @Value("${zhipu.ai.api-key}")
    private String apiKey;

    // 上传文件路径
    @Value("${ruoyi.profile}")
    private String profile;

    /**
     * 查询课程管理
     * 
     * @param courseId 课程管理主键
     * @return 课程管理
     */
    @Override
    public Course selectCourseByCourseId(Long courseId)
    {
        return courseMapper.selectCourseByCourseId(courseId);
    }

    /**
     * 查询课程管理列表
     * 
     * @param course 课程管理
     * @return 课程管理
     */
    @Override
    public List<Course> selectCourseList(Course course)
    {
        return courseMapper.selectCourseList(course);
    }

    /**
     * 新增课程管理
     * 
     * @param course 课程管理
     * @return 结果
     */
    @Override
    public int insertCourse(Course course)
    {
        course.setCreateTime(DateUtils.getNowDate());
        return courseMapper.insertCourse(course);
    }

    /**
     * 修改课程管理
     * 
     * @param course 课程管理
     * @return 结果
     */
    @Override
    public int updateCourse(Course course)
    {
        course.setUpdateTime(DateUtils.getNowDate());
        return courseMapper.updateCourse(course);
    }

    /**
     * 批量删除课程管理
     * 
     * @param courseIds 需要删除的课程管理主键
     * @return 结果
     */
    @Override
    public int deleteCourseByCourseIds(Long[] courseIds)
    {
        return courseMapper.deleteCourseByCourseIds(courseIds);
    }

    /**
     * 删除课程管理信息
     * 
     * @param courseId 课程管理主键
     * @return 结果
     */
    @Override
    public int deleteCourseByCourseId(Long courseId)
    {
        return courseMapper.deleteCourseByCourseId(courseId);
    }

    /**
     * 生成教学质量分析报告
     * 
     * @param courseId 课程ID
     * @param templateId 模板ID
     * @return 报告生成结果
     * @throws Exception 异常信息
     */
    @Override
    public String generateQualityReport(Long courseId, Long templateId) throws Exception {
        // 1. 获取课程信息
        Course course = courseMapper.selectCourseByCourseId(courseId);
        if (course == null) {
            throw new Exception("课程不存在");
        }

        // 2. 前置数据检查与获取
        // 获取教学大纲
        Syllabus syllabus = new Syllabus();
        syllabus.setCourseId(courseId);
        List<Syllabus> syllabusList = syllabusMapper.selectSyllabusList(syllabus);
        if (syllabusList == null || syllabusList.isEmpty()) {
            throw new Exception("错误：未找到该课程的教学大纲，请先完成相关数据录入");
        }
        Syllabus selectedSyllabus = syllabusList.get(0);

        // 获取试卷实质审查表
        PaperReview paperReview = paperReviewMapper.selectPaperReviewByCourseId(courseId);
        if (paperReview == null) {
            throw new Exception("错误：未找到该课程的试卷实质审查表，请先完成相关数据录入");
        }

        // 获取试卷成绩单
        ExamScore examScore = new ExamScore();
        examScore.setCourseId(courseId);
        List<ExamScore> examScoreList = examScoreMapper.selectExamScoreList(examScore);
        if (examScoreList == null || examScoreList.isEmpty()) {
            throw new Exception("错误：未找到该课程的试卷成绩单，请先完成相关数据录入");
        }
        ExamScore selectedExamScore = examScoreList.get(0);

        // 获取成绩细目表
        ScoreDetail scoreDetail = new ScoreDetail();
        scoreDetail.setCourseId(courseId);
        List<ScoreDetail> scoreDetailList = scoreDetailMapper.selectScoreDetailList(scoreDetail);
        if (scoreDetailList == null || scoreDetailList.isEmpty()) {
            throw new Exception("错误：未找到该课程的成绩细目表，请先完成相关数据录入");
        }
        ScoreDetail selectedScoreDetail = scoreDetailList.get(0);

        // 3. 初始化智谱AI客户端
        ZhipuAiClient client = ZhipuAiClient.builder().ofZHIPU()
                .apiKey(apiKey)
                .build();

        // 4. 数据解析处理
        // 解析教学大纲
        if (StringUtils.isEmpty(selectedSyllabus.getParseContent())) {
            String syllabusContent = ZhipuAiFileParser.parseWordFile(client, selectedSyllabus.getSyllabusFile(), profile);
            selectedSyllabus.setParseContent(syllabusContent);
            syllabusMapper.updateSyllabus(selectedSyllabus);
        }

        // 解析试卷实质审查表
        if (StringUtils.isEmpty(paperReview.getParseContent())) {
            String reviewContent = ZhipuAiFileParser.parseWordFile(client, paperReview.getReviewFile(), profile);
            paperReview.setParseContent(reviewContent);
            paperReviewMapper.updatePaperReview(paperReview);
        }

        // 解析试卷成绩单
        if (StringUtils.isEmpty(selectedExamScore.getParseContent())) {
            String scoreContent = ZhipuAiFileParser.parseExcelFile(client, selectedExamScore.getScoreFile(), profile);
            selectedExamScore.setParseContent(scoreContent);
            examScoreMapper.updateExamScore(selectedExamScore);
        }

        // 解析成绩细目表
        if (StringUtils.isEmpty(selectedScoreDetail.getParseContent())) {
            String detailContent = ZhipuAiFileParser.parseExcelFile(client, selectedScoreDetail.getDetailFile(), profile);
            selectedScoreDetail.setParseContent(detailContent);
            scoreDetailMapper.updateScoreDetail(selectedScoreDetail);
        }

        // 获取并解析报告模板
        QualityReportTemplate template = qualityReportTemplateMapper.selectQualityReportTemplateByReportTemplateId(templateId);
        if (template == null) {
            throw new Exception("报告模板不存在");
        }

        if (StringUtils.isEmpty(template.getParseContent())) {
            String templateContent = ZhipuAiFileParser.parseWordFile(client, template.getReportTemplateFile(), profile);
            template.setParseContent(templateContent);
            qualityReportTemplateMapper.updateQualityReportTemplate(template);
        }

        // 5. AI报告生成
        String reportContent = generateReportWithAI(client, 
                selectedSyllabus.getParseContent(),
                paperReview.getParseContent(),
                selectedExamScore.getParseContent(),
                selectedScoreDetail.getParseContent(),
                template.getParseContent(),
                course.getCourseName());

        // 6. 存储报告
        QualityReport report = new QualityReport();
        report.setCourseId(courseId);
        report.setCourseName(course.getCourseName());
        report.setReportName(course.getCourseName() + "-课程目标达成情况分析报告");
        report.setReportContent(reportContent);
        report.setCreateTime(DateUtils.getNowDate());
        qualityReportMapper.insertQualityReport(report);

        return course.getCourseName() + "-课程目标达成情况分析报告已生成，请在课程目标达成情况分析报告页查看";
    }

    /**
     * 使用AI生成报告
     * 
     * @param client 智谱AI客户端
     * @param syllabusContent 教学大纲解析内容
     * @param reviewContent 试卷实质审查表解析内容
     * @param scoreContent 试卷成绩单解析内容
     * @param detailContent 成绩细目表解析内容
     * @param templateContent 模板解析内容
     * @param courseName 课程名称
     * @return 报告内容
     * @throws Exception 异常信息
     */
    private String generateReportWithAI(ZhipuAiClient client, 
                                      String syllabusContent,
                                      String reviewContent,
                                      String scoreContent,
                                      String detailContent,
                                      String templateContent,
                                      String courseName) throws Exception {
        // 构造提示词
        String prompt = "请根据以下课程数据和报告模板，生成一份完整的教学质量分析报告。\n" +
                "课程名称：" + courseName + "\n" +
                "教学大纲内容：\n" + syllabusContent + "\n" +
                "试卷实质审查表内容：\n" + reviewContent + "\n" +
                "试卷成绩单内容：\n" + scoreContent + "\n" +
                "成绩细目表内容：\n" + detailContent + "\n" +
                "报告模板内容：\n" + templateContent + "\n" +
                "要求：\n" +
                "1. 生成Markdown格式的报告\n" +
                "2. 报告结构清晰，内容完整\n" +
                "3. 基于提供的数据进行分析，确保数据准确性\n" +
                "4. 按照模板的结构和要求生成报告\n" +
                "5. 只返回Markdown格式的报告内容，不要包含其他文字\n";

        // 创建聊天请求
        ChatCompletionCreateParams request = ChatCompletionCreateParams.builder()
                .model("glm-4.7")
                .messages(java.util.Arrays.asList(
                        ChatMessage.builder()
                                .role(ChatMessageRole.USER.value())
                                .content(prompt)
                                .build()
                ))
                .build();

        // 发送请求
        ChatCompletionResponse response = client.chat().createChatCompletion(request);
        if (!response.isSuccess()) {
            throw new Exception("AI分析失败: " + response.getMsg());
        }

        // 获取AI回复内容
        Object content = response.getData().getChoices().get(0).getMessage().getContent();
        return content != null ? content.toString() : "";
    }
}
