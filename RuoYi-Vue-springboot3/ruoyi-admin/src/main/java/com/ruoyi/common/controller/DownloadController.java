package com.ruoyi.common.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URLEncoder;

/**
 * 下载控制器
 * 用于处理文件下载请求
 */
@RestController
@RequestMapping("/download")
public class DownloadController {

    @Value("${ruoyi.profile}")
    private String profile;

    /**
     * 下载成绩细目表
     * 
     * @param fileName 文件名
     * @param request 请求
     * @param response 响应
     */
    @GetMapping("/detail/{fileName}")
    public void downloadDetail(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) {
        String filePath = profile + "/download/detail/" + fileName;
        File file = new File(filePath);

        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis);
             OutputStream os = response.getOutputStream()) {

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + encodedFileName);
            response.setContentLength((int) file.length());

            // 写入响应
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            os.flush();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}