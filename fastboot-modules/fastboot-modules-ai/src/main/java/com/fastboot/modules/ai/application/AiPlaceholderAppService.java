package com.fastboot.modules.ai.application;

import com.fastboot.modules.ai.api.AiChatPort;
import org.springframework.stereotype.Service;

@Service
public class AiPlaceholderAppService implements AiChatPort {

    @Override
    public String chat(String prompt) {
        return "AI module placeholder: " + prompt;
    }
}
