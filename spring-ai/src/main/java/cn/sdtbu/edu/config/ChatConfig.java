package cn.sdtbu.edu.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository; // Correct import
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Wyh
 */
@Configuration
public class ChatConfig {

    @Bean
    public InMemoryChatMemoryRepository chatMemoryRepository() { // Renamed for clarity
        return new InMemoryChatMemoryRepository();
    }

    @Bean
    public ChatClient chatClient(OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient
                .builder(model)
                .defaultSystem("#你是一个关于治愈心理的音乐或电台或节目推荐助手,用户询问音乐相关问题,你会给出推荐的音乐,并且给出推荐理由," +
                        "询问用户是否满意,如果不满意,你会继续推荐,直到用户满意为止," +
                        "询问其他无关问题,你要拒绝回答,并且告诉用户你是一个音乐推荐助手,只会推荐音乐,不会回答其他问题")
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }
}