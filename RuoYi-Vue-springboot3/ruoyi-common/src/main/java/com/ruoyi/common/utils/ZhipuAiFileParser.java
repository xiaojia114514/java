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
     * 根据文件扩展名获取文件类型
     * 
     * @param filePath 文件路径
     * @return 文件类型
     * @throws Exception 异常信息
     */
    private static String getFileTypeByExtension(String filePath) throws Exception {
        if (StringUtils.isEmpty(filePath)) {
            throw new Exception("文件路径为空");
        }
        
        // 获取文件扩展名
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex == -1) {
            throw new Exception("文件没有扩展名");
        }
        
        String extension = filePath.substring(lastDotIndex + 1).toLowerCase();
        
        // 根据扩展名返回对应的文件类型
        switch (extension) {
            case "doc":
            case "docx":
                return extension;
            case "xls":
            case "xlsx":
                return "xls";
            case "pdf":
                return "pdf";
            case "txt":
                return "txt";
            case "md":
                return "md";
            case "csv":
                return "csv";
            case "ppt":
            case "pptx":
                return extension;
            case "png":
                return "png";
            case "jpg":
            case "jpeg":
                return "jpg";
            default:
                throw new Exception("不支持的文件类型: " + extension);
        }
    }

    /**
     * 解析文件
     * 
     * @param client 智谱AI客户端
     * @param filePath 文件路径
     * @param fileType 文件类型（如果为null或空，则根据文件扩展名自动判断）
     * @param profile 上传文件路径
     * @return 解析内容
     * @throws Exception 异常信息
     */
    public static String parseFile(ZhipuAiClient client, String filePath, String fileType, String profile) throws Exception {
        log.info("开始解析文件: {}", filePath);
        
        // 根据文件扩展名自动判断文件类型（如果未指定）
        if (StringUtils.isEmpty(fileType)) {
            fileType = getFileTypeByExtension(filePath);
            log.info("根据文件扩展名自动判断文件类型: {}", fileType);
        }
        
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
        
        // 记录完整的响应信息，便于调试
        log.info("智谱AI响应 - isSuccess: {}, msg: {}, code: {}", 
            response.isSuccess(), 
            response.getMsg(), 
            response.getCode());
        
        // 记录data字段的详细信息
        if (response.getData() != null) {
            log.info("智谱AI响应数据 - TaskId: {}, Message: {}", 
                response.getData().getTaskId(), 
                response.getData().getMessage());
        } else {
            log.warn("智谱AI响应数据为null");
        }
        
        // 特殊处理"Call Successful"消息，即使isSuccess返回false也继续处理
        boolean isCallSuccessful = "Call Successful".equals(response.getMsg());
        if (isCallSuccessful) {
            log.info("检测到'Call Successful'消息，特殊处理");
        }
        
        // 检查解析任务是否创建成功
        boolean hasValidTaskId = response.getData() != null && response.getData().getTaskId() != null;
        if (!response.isSuccess() && !isCallSuccessful) {
            log.error("解析任务创建失败: {}", response.getMsg());
            throw new Exception("解析任务创建失败: " + response.getMsg());
        }
        if (!hasValidTaskId) {
            log.error("解析任务创建失败：未获取到有效TaskId");
            throw new Exception("解析任务创建失败：未获取到有效TaskId");
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
        // 根据文件扩展名判断文件类型
        String fileType = "xls";
        if (filePath.toLowerCase().endsWith(".xlsx")) {
            fileType = "xlsx";
        }
        log.info("解析Excel文件，文件类型: {}", fileType);
        return parseFile(client, filePath, fileType, profile);
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
        // 根据文件扩展名判断文件类型
        String fileType = "docx";
        if (filePath.toLowerCase().endsWith(".doc")) {
            fileType = "doc";
        }
        return parseFile(client, filePath, fileType, profile);
    }
}
