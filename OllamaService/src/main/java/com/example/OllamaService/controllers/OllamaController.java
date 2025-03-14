package com.example.OllamaService.controllers;

import com.example.OllamaService.services.OllamaLocalService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ollama")
public class OllamaController {

    private final OllamaLocalService ollamaLocalService;

    public OllamaController(OllamaLocalService ollamaLocalService) {
        this.ollamaLocalService = ollamaLocalService;
    }

    @PostMapping("/askQuestion")
    public String generateResponse(
            @RequestParam String model,
            @RequestParam String system,
            @RequestParam String prompt) {
        return ollamaLocalService.getOllamaResponse(model, system, prompt);
    }
}
