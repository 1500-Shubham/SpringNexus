package com.example.OllamaService.dto;

import org.springframework.web.bind.annotation.RequestParam;

public class OllamaDTO {

    private String model;
    private String system;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    private String prompt;
}
