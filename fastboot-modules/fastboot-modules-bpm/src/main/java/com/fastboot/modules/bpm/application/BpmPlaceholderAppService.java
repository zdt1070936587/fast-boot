package com.fastboot.modules.bpm.application;

import org.springframework.stereotype.Service;

@Service
public class BpmPlaceholderAppService {

    public String status() {
        return "bpm-module-ready";
    }
}
