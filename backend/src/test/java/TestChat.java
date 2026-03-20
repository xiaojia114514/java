//import io.github.cdimascio.dotenv.Dotenv;
//import org.junit.jupiter.api.Test;
//import dev.langchain4j.model.chat.ChatLanguageModel;
//import dev.langchain4j.model.openai.OpenAiChatModel;
//public class TestChat {
//        Dotenv dotenv = Dotenv.load();
//        String apiKey = dotenv.get("DEEPSEEK_API_KEY");
//    @Test
//    void translate() {
//
//        // 配置模型（低随机性，适合翻译）
//        ChatLanguageModel model = OpenAiChatModel.builder()
//                .baseUrl("https://api.deepseek.com/v1")
//                .apiKey(apiKey)
//                .modelName("deepseek-chat")
//                .temperature(0.1)  // 低随机性
//                .topP(0.3)        // 限制采样范围
//                .build();
//
//        // 定义翻译指令
//        String prompt = """
//               你是一名严格的专业翻译，只需返回以下文本的英文翻译，不要添加任何前缀、后缀、解释或格式：
//               "深度学习模型的训练需要大量数据和计算资源。"
//               """;
//
//        // 调用API并打印结果
//        String translation = model.generate(prompt);
//        System.out.println(translation);
//    }
//    @Test
//    void summarizeText() {
//
//        // 配置模型（适度随机性，抑制重复）
//        ChatLanguageModel model = OpenAiChatModel.builder()
//                .baseUrl("https://api.deepseek.com/v1")
//                .apiKey(apiKey)
//                .modelName("deepseek-chat")
//                .temperature(0.3)      // 适度创造性
//                .frequencyPenalty(0.5) // 抑制重复词
//                .maxTokens(100)        // 限制输出长度
//                .build();
//
//        // 定义缩写指令
//        String prompt = """
//               你是一名严格的文本缩写工具，只需返回以下文本的缩写至原长度30%的缩写结果，不要添加任何前缀、后缀或解释：
//               "深度学习模型的训练需要大量数据和计算资源，通常涉及GPU集群，训练时间可能长达数周。"
//               """;
//
//        // 调用API并打印结果
//        String summary = model.generate(prompt);
//        System.out.println(summary);
//    }
//}
