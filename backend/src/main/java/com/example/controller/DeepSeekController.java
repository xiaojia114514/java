package com.example.controller;

import com.example.service.DeepSeekService;
import com.example.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/deepseek")
public class DeepSeekController {
    private final DeepSeekService deepSeekService;

    public DeepSeekController(DeepSeekService deepSeekService) {
        this.deepSeekService = deepSeekService;
    }

    /**
     * 翻译接口
     *
     * @param request JSON格式: {"text": "待翻译文本"}
     */
    @PostMapping("/translate")
    public Result translate(@RequestBody Map<String, String> request) {
        try {
            String translatedText = deepSeekService.translateToEnglish(request.get("text"));
            return Result.success(translatedText);
        } catch (Exception e) {
            return Result.error("翻译失败: " + e.getMessage());
        }
    }

    /**
     * 缩写接口
     *
     * @param request JSON格式: {"text": "待缩写文本"}
     */
    @PostMapping("/summarize")
    public Result summarize(@RequestBody Map<String, String> request) {
        try {
            String summarizedText = deepSeekService.summarizeText(request.get("text"));
            return Result.success(summarizedText);
        } catch (Exception e) {
            return Result.error("缩写失败: " + e.getMessage());
        }
    }

}
