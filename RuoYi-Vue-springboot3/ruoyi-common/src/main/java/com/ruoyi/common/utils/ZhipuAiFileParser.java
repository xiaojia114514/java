package com.ruoyi.common.utils;

import ai.z.openapi.ZhipuAiClient;
import ai.z.openapi.service.fileparsing.FileParsingDownloadReq;
import ai.z.openapi.service.fileparsing.FileParsingDownloadResponse;
import ai.z.openapi.service.fileparsing.FileParsingResponse;
import ai.z.openapi.service.fileparsing.FileParsingUploadReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 智谱AI文件解析工具类
 * 用于解析Excel、Word等文件
 * 
 * @author ruoyi
 */
public class ZhipuAiFileParser {

    private static final Logger log = LoggerFactory.getLogger(ZhipuAiFileParser.class);

    /**
     * 解析文件
     * 
     * @param client 智谱AI客户端
     * @param filePath 文件路径
     * @param fileType 文件类型
     * @param profile 上传文件路径
     * @return 解析内容
     * @throws Exception 异常信息
     */
    public static String parseFile(ZhipuAiClient client, String filePath, String fileType, String profile) throws Exception {
        log.info("开始解析文件: {}", filePath);
        // 构建完整的文件路径
        // 检查filePath是否已经包含profile前缀
        String fullPath;
        if (filePath.startsWith(profile)) {
            fullPath = filePath;
        } else {
            // 移除filePath中的/profile前缀（如果存在）
            String cleanedFilePath = filePath.replaceFirst("^/profile", "");
            fullPath = profile + cleanedFilePath;
        }
        log.info("构建完整文件路径: {}", fullPath);
        File file = new File(fullPath);
        if (!file.exists()) {
            log.error("文件不存在: {}", fullPath);
            throw new Exception("文件不存在: " + fullPath);
        }
        log.info("文件存在，开始创建解析任务...");

        // 创建解析任务
        FileParsingUploadReq uploadReq = FileParsingUploadReq.builder()
                .filePath(fullPath)
                .fileType(fileType)
                .toolType("lite")
                .build();

        log.info("创建解析任务...");
        FileParsingResponse response = client.fileParsing().createParseTask(uploadReq);
        if (!response.isSuccess() || response.getData().getTaskId() == null) {
            log.error("解析任务创建失败: {}", response.getMsg());
            throw new Exception("解析任务创建失败: " + response.getMsg());
        }
        log.info("解析任务创建成功，TaskId: {}", response.getData().getTaskId());

        String taskId = response.getData().getTaskId();

        // 获取解析结果
        log.info("开始获取解析结果...");
        int maxRetry = 100;
        int intervalMs = 3000;
        for (int i = 0; i < maxRetry; i++) {
            FileParsingDownloadReq downloadReq = FileParsingDownloadReq.builder()
                    .taskId(taskId)
                    .formatType("text")
                    .build();

            log.debug("第{}次尝试获取解析结果...", i + 1);
            FileParsingDownloadResponse resultResponse = client.fileParsing().getParseResult(downloadReq);
            if (resultResponse.isSuccess()) {
                String status = resultResponse.getData().getStatus();
                log.debug("解析任务状态: {}", status);
                if ("succeeded".equalsIgnoreCase(status)) {
                    log.info("解析任务成功完成");
                    return resultResponse.getData().getContent();
                } else if ("processing".equalsIgnoreCase(status)) {
                    log.debug("解析任务处理中，等待...");
                    Thread.sleep(intervalMs);
                } else {
                    log.error("解析任务异常: {}", resultResponse.getData().getMessage());
                    throw new Exception("解析任务异常: " + resultResponse.getData().getMessage());
                }
            } else {
                log.error("获取解析结果失败: {}", resultResponse.getMsg());
                throw new Exception("获取解析结果失败: " + resultResponse.getMsg());
            }
        }

        log.error("解析任务超时");
        throw new Exception("解析任务超时");
    }

    /**
     * 解析Excel文件
     * 
     * @param client 智谱AI客户端
     * @param filePath 文件路径
     * @param profile 上传文件路径
     * @return 解析内容
     * @throws Exception 异常信息
     */
    public static String parseExcelFile(ZhipuAiClient client, String filePath, String profile) throws Exception {
        return parseFile(client, filePath, "xls", profile);
    }

    /**
     * 解析Word文件
     * 
     * @param client 智谱AI客户端
     * @param filePath 文件路径
     * @param profile 上传文件路径
     * @return 解析内容
     * @throws Exception 异常信息
     */
    public static String parseWordFile(ZhipuAiClient client, String filePath, String profile) throws Exception {
        return parseFile(client, filePath, "docx", profile);
    }
}
