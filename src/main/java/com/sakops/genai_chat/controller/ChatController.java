package com.sakops.genai_chat.controller;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final OpenAiChatModel chatModel;

    public ChatController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @PostMapping
    public String chat(@RequestBody String userMessage) {
        return chatModel.call(userMessage);
    }
}
