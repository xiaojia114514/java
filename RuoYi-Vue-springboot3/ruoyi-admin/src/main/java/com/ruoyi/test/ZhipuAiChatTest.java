package com.ruoyi.test;

import ai.z.openapi.ZhipuAiClient;
import ai.z.openapi.service.model.ChatCompletionCreateParams;
import ai.z.openapi.service.model.ChatCompletionResponse;
import ai.z.openapi.service.model.ChatMessage;
import ai.z.openapi.service.model.ChatMessageRole;

public class ZhipuAiChatTest {
    public static void main(String[] args) {
        // 创建客户端
        ZhipuAiClient client = ZhipuAiClient.builder().ofZHIPU()
                .apiKey("78113699b99c4beb92a4c2844f59803a.El8by0GXUkXTG0Rm")
                .build();
        try {
            // 测试聊天功能
            System.out.println("=== 智谱AI聊天测试 ===");
            testChatCompletion(client);

        } catch (Exception e) {
            System.err.println("发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试智谱AI聊天功能
     * @param client ZhipuAiClient实例
     */
    private static void testChatCompletion(ZhipuAiClient client) throws Exception {
        // 构造提示词
        String scoreContent = "学号,姓名,总分\n1001,张三,85\n1002,李四,92\n1003,王五,78";
        String reviewContent = "课程目标1: 掌握基本概念 (对应题目1,2,3)\n课程目标2: 理解核心原理 (对应题目4,5)\n课程目标3: 应用实践能力 (对应题目6,7,8)";
        
        String prompt = "请根据以下考试成绩单和试卷实质审查表的内容，生成成绩细目表表格。\n"
                + "考试成绩单内容：\n" + scoreContent + "\n"
                + "试卷实质审查表内容：\n" + reviewContent + "\n"
                + "要求：\n"
                + "1. 生成Markdown格式的成绩细目表表格\n"
                + "2. 表头包含学号、课程目标1、课程目标2、课程目标3、课程目标4、课程目标5、课程目标6等列\n"
                + "3. 表格第一行显示各课程目标各自对应的支撑点题目号\n"
                + "4. 后续行显示每个学生的学号和各课程目标的得分,具体得分留空\n"
                + "5. 参考格式如下: |学号 | 课程目标1 | 课程目标2 | 课程目标3 |\n"
                +"|  | 题目1,2,3 | 题目4,5 | 题目6,7,8 |\n"
                +"| 1001 | | | |\n"
                +"| 1002 | | | |\n"
                +"| 1003 | | | |\n"
                + "6. 确保数据准确，格式清晰\n"
                + "7. 只返回Markdown表格内容，不要包含其他文字\n";
        System.out.println("构造聊天请求...");
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

        System.out.println("发送请求到智谱AI...");
        // 发送请求
        ChatCompletionResponse response = client.chat().createChatCompletion(request);
        System.out.println("收到智谱AI响应...");
        
        if (response.isSuccess()) {
            System.out.println("AI分析成功！");
            if (response.getData() != null && response.getData().getChoices() != null && !response.getData().getChoices().isEmpty()) {
                Object content = response.getData().getChoices().get(0).getMessage().getContent();
                System.out.println("AI回复内容:");
                System.out.println(content != null ? content.toString() : "");
            } else {
                System.err.println("AI响应数据为空");
            }
        } else {
            System.err.println("AI分析失败: " + response.getMsg());
        }
    }
}
