package com.ruoyi.score.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ZhipuAiFileParser;
import com.ruoyi.common.utils.poi.DynamicExcelUtil;
import com.ruoyi.review.mapper.PaperReviewMapper;
import com.ruoyi.review.domain.PaperReview;
import ai.z.openapi.ZhipuAiClient;
import ai.z.openapi.service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.score.mapper.ExamScoreMapper;
import com.ruoyi.score.domain.ExamScore;
import com.ruoyi.score.service.IExamScoreService;

/**
 * 考试成绩单Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
@Service
public class ExamScoreServiceImpl implements IExamScoreService 
{
    private static final Logger log = LoggerFactory.getLogger(ExamScoreServiceImpl.class);

    @Autowired
    private ExamScoreMapper examScoreMapper;

    @Autowired
    private PaperReviewMapper paperReviewMapper;

    // 智谱API密钥
    @Value("${zhipu.ai.api-key}")
    private String apiKey;

    // 上传文件路径
    @Value("${ruoyi.profile}")
    private String profile;

    /**
     * 生成成绩细目表
     * 
     * @param scoreId 考试成绩单主键
     * @return 下载链接
     * @throws Exception 异常信息
     */
    @Override
    public String generateDetail(Long scoreId) throws Exception {
        log.info("开始生成成绩细目表，scoreId: {}", scoreId);
        // 1. 获取考试成绩单信息
        ExamScore examScore = examScoreMapper.selectExamScoreByScoreId(scoreId);
        if (examScore == null) {
            log.error("考试成绩单不存在，scoreId: {}", scoreId);
            throw new Exception("考试成绩单不存在");
        }
        log.info("获取考试成绩单信息成功: {}", examScore.getScoreName());

        // 2. 获取课程ID
        Long courseId = examScore.getCourseId();
        if (courseId == null) {
            log.error("课程ID不存在，scoreId: {}", scoreId);
            throw new Exception("课程ID不存在");
        }
        log.info("获取课程ID成功: {}", courseId);

        // 3. 查询试卷实质审查表
        PaperReview paperReview = paperReviewMapper.selectPaperReviewByCourseId(courseId);
        if (paperReview == null) {
            log.error("对应课程的试卷实质审查表不存在，courseId: {}", courseId);
            throw new Exception("对应课程的试卷实质审查表不存在");
        }
        log.info("查询试卷实质审查表成功: {}", paperReview.getReviewName());

        // 4. 检查解析状态并处理
        log.info("开始检查解析状态...");
        ZhipuAiClient client = ZhipuAiClient.builder().ofZHIPU()
                .apiKey(apiKey)
                .build();
        log.info("智谱AI客户端初始化成功");

        // 解析考试成绩单
        if (StringUtils.isEmpty(examScore.getParseContent())) {
            log.info("考试成绩单未解析，开始解析...");
            String scoreFileContent = parseScoreFile(client, examScore.getScoreFile());
            examScore.setParseContent(scoreFileContent);
            examScoreMapper.updateExamScore(examScore);
            log.info("考试成绩单解析完成");
        } else {
            log.info("考试成绩单已解析，跳过解析步骤");
        }

        // 解析试卷实质审查表
        if (StringUtils.isEmpty(paperReview.getParseContent())) {
            log.info("试卷实质审查表未解析，开始解析...");
            String reviewFileContent = parseReviewFile(client, paperReview.getReviewFile());
            paperReview.setParseContent(reviewFileContent);
            paperReviewMapper.updatePaperReview(paperReview);
            log.info("试卷实质审查表解析完成");
        } else {
            log.info("试卷实质审查表已解析，跳过解析步骤");
        }

        // 5. AI分析与结果生成
        log.info("开始AI分析与结果生成...");
        String detailTable = generateDetailTable(client, examScore.getParseContent(), paperReview.getParseContent());
        log.info("AI分析与结果生成完成");

        // 6. 生成Excel文件并返回下载链接
        log.info("开始生成Excel文件...");
        String downloadUrl = generateExcel(detailTable, examScore.getScoreName());
        log.info("生成Excel文件完成，下载链接: {}", downloadUrl);
        return downloadUrl;
    }

    /**
     * 解析考试成绩单
     * 
     * @param client 智谱AI客户端
     * @param filePath 文件路径
     * @return 解析内容
     * @throws Exception 异常信息
     */
    private String parseScoreFile(ZhipuAiClient client, String filePath) throws Exception {
        return ZhipuAiFileParser.parseExcelFile(client, filePath, profile);
    }

    /**
     * 解析试卷实质审查表
     * 
     * @param client 智谱AI客户端
     * @param filePath 文件路径
     * @return 解析内容
     * @throws Exception 异常信息
     */
    private String parseReviewFile(ZhipuAiClient client, String filePath) throws Exception {
        return ZhipuAiFileParser.parseWordFile(client, filePath, profile);
    }

    /**
     * 生成成绩细目表
     * 
     * @param client 智谱AI客户端
     * @param scoreContent 考试成绩单解析内容
     * @param reviewContent 试卷实质审查表解析内容
     * @return 成绩细目表（Markdown格式）
     * @throws Exception 异常信息
     */
    private String generateDetailTable(ZhipuAiClient client, String scoreContent, String reviewContent) throws Exception {
        log.info("开始生成成绩细目表...");
        // 构造提示词
        String prompt = "请根据以下考试成绩单和试卷实质审查表的内容，生成成绩细目表表格。\n"
                + "考试成绩单内容：\n" + scoreContent + "\n"
                + "试卷实质审查表内容：\n" + reviewContent + "\n"
                + "要求：\n"
                + "1. 生成Markdown格式的成绩细目表表格\n"
                + "2. 表头包含学号、课程目标1、课程目标2、课程目标3、课程目标4、课程目标5、课程目标6等列\n"
                + "3. 表格第一行显示各课程目标各自对应的支撑点题目号\n"
                + "4. 后续行显示每个学生的学号和各课程目标的得分,具体得分留空\n"
                + "5. 严格按照以下参考格式(列数不固定): \n"
                +" |学号 | 课程目标1 | 课程目标2 | 课程目标3 |\n"
                +"|  | 题目1,2,3 | 题目4,5 | 题目6,7,8 |\n"
                +"| 1001 | | | |\n"
                +"| 1002 | | | |\n"
                +"| 1003 | | | |\n"
                + "6. 确保数据准确，格式清晰\n"
                + "7. 只返回Markdown表格内容，不要包含其他文字\n";
        log.info("构造聊天请求...");
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

        log.info("发送请求到智谱AI...");
        try {
            // 发送请求
            long startTime = System.currentTimeMillis();
            ChatCompletionResponse response = client.chat().createChatCompletion(request);
            long endTime = System.currentTimeMillis();
            log.info("收到智谱AI响应，耗时: {}ms", endTime - startTime);
            
            if (response.isSuccess()) {
                log.info("AI分析成功，响应状态: {}", response.getCode());
                if (response.getData() != null) {
                    log.info("AI响应数据不为空，choices数量: {}", response.getData().getChoices() != null ? response.getData().getChoices().size() : 0);
                    if (response.getData().getChoices() != null && !response.getData().getChoices().isEmpty()) {
                        Object content = response.getData().getChoices().get(0).getMessage().getContent();
                        log.info("获取AI回复内容成功，内容长度: {}", content != null ? content.toString().length() : 0);
                        return content != null ? content.toString() : "";
                    } else {
                        log.error("AI响应choices为空");
                        throw new Exception("AI响应choices为空");
                    }
                } else {
                    log.error("AI响应数据为空");
                    throw new Exception("AI响应数据为空");
                }
            } else {
                log.error("AI分析失败: {}, 错误码: {}", response.getMsg(), response.getCode());
                throw new Exception("AI分析失败: " + response.getMsg());
            }
        } catch (Exception e) {
            log.error("调用智谱AI接口异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 生成Excel文件
     * 
     * @param markdownContent Markdown格式的成绩细目表
     * @param fileName 文件名
     * @return 下载链接
     * @throws Exception 异常信息
     */
    private String generateExcel(String markdownContent, String fileName) throws Exception {
        log.info("开始生成Excel文件...");
        // 解析Markdown表格
        log.info("解析Markdown表格...");
        List<List<String>> tableData = parseMarkdownTable(markdownContent);
        if (tableData == null || tableData.isEmpty()) {
            log.error("解析Markdown表格失败");
            throw new Exception("解析Markdown表格失败");
        }
        log.info("解析Markdown表格成功，行数: {}", tableData.size());

        // 生成带时间戳的文件名
        String timestamp = DateUtils.dateTimeNow("yyyyMMddHHmm");
        String newFileName = "成绩细目表模板_" + timestamp;
        
        // 生成Excel文件
        String downloadDir = profile + "/download/detail/";
        log.info("生成Excel文件到: {}{}.xlsx", downloadDir, newFileName);
        DynamicExcelUtil.generateExcel(tableData, "成绩细目表", downloadDir + newFileName + ".xlsx");

        // 返回下载链接，使用与项目其他下载功能一致的格式
        String downloadUrl = "/profile/download/detail/" + newFileName + ".xlsx";
        log.info("生成Excel文件完成，下载链接: {}", downloadUrl);
        return downloadUrl;
    }

    /**
     * 解析Markdown表格
     * 
     * @param markdownContent Markdown格式的表格
     * @return 表格数据
     */
    private List<List<String>> parseMarkdownTable(String markdownContent) {
        List<List<String>> tableData = new java.util.ArrayList<>();
        String[] lines = markdownContent.split("\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("|-") || line.startsWith("---")) {
                continue;
            }

            if (line.startsWith("|")) {
                String[] cells = line.split("\\|");
                List<String> row = new java.util.ArrayList<>();
                for (int i = 1; i < cells.length; i++) {
                    row.add(cells[i].trim());
                }
                tableData.add(row);
            }
        }

        return tableData;
    }

    /**
     * 查询考试成绩单
     * 
     * @param scoreId 考试成绩单主键
     * @return 考试成绩单
     */
    @Override
    public ExamScore selectExamScoreByScoreId(Long scoreId)
    {
        return examScoreMapper.selectExamScoreByScoreId(scoreId);
    }

    /**
     * 查询考试成绩单列表
     * 
     * @param examScore 考试成绩单
     * @return 考试成绩单
     */
    @Override
    public List<ExamScore> selectExamScoreList(ExamScore examScore)
    {
        return examScoreMapper.selectExamScoreList(examScore);
    }

    /**
     * 新增考试成绩单
     * 
     * @param examScore 考试成绩单
     * @return 结果
     */
    @Override
    public int insertExamScore(ExamScore examScore)
    {
        examScore.setCreateTime(DateUtils.getNowDate());
        return examScoreMapper.insertExamScore(examScore);
    }

    /**
     * 修改考试成绩单
     * 
     * @param examScore 考试成绩单
     * @return 结果
     */
    @Override
    public int updateExamScore(ExamScore examScore)
    {
        examScore.setUpdateTime(DateUtils.getNowDate());
        return examScoreMapper.updateExamScore(examScore);
    }

    /**
     * 批量删除考试成绩单
     * 
     * @param scoreIds 需要删除的考试成绩单主键
     * @return 结果
     */
    @Override
    public int deleteExamScoreByScoreIds(Long[] scoreIds)
    {
        return examScoreMapper.deleteExamScoreByScoreIds(scoreIds);
    }

    /**
     * 删除考试成绩单信息
     * 
     * @param scoreId 考试成绩单主键
     * @return 结果
     */
    @Override
    public int deleteExamScoreByScoreId(Long scoreId)
    {
        return examScoreMapper.deleteExamScoreByScoreId(scoreId);
    }
}
