package com.example.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class DeepSeekService {

    private final ChatLanguageModel translationModel;
    private final ChatLanguageModel summarizationModel;

    public DeepSeekService() {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("DEEPSEEK_API_KEY");

        // 初始化翻译模型（低随机性）
        this.translationModel = OpenAiChatModel.builder()
                .baseUrl("https://api.deepseek.com/v1")
                .apiKey(apiKey)
                .modelName("deepseek-chat")
                .temperature(0.1)
                .topP(0.3)
                .timeout(Duration.ofSeconds(60))
                .build();

        // 初始化缩写模型（适度随机性）
        this.summarizationModel = OpenAiChatModel.builder()
                .baseUrl("https://api.deepseek.com/v1")
                .apiKey(apiKey)
                .modelName("deepseek-chat")
                .temperature(0.3)
                .frequencyPenalty(0.5)
                .maxTokens(100)
                .timeout(Duration.ofSeconds(60))
                .build();
    }

    /**
     * 翻译文本（中→英）
     */
    public String translateToEnglish(String text) {
        String prompt = String.format("""
            你是一名严格的专业翻译，只需返回以下文本的英文翻译，不要添加任何前缀、后缀、解释或格式：
            %s
            """, text);
        return translationModel.generate(prompt);
    }

    /**
     * 缩写文本（保留30%长度）
     */
    public String summarizeText(String text) {
        String prompt = String.format("""
            你是一名严格的文本缩写工具，只需返回以下文本的不超过20字的缩写结果，不要添加任何前缀、后缀或解释：
            %s
            """, text);
        return summarizationModel.generate(prompt);
    }
}
