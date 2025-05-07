package com.example.OllamaService.controllers;

import com.example.OllamaService.dto.OllamaDTO;
import com.example.OllamaService.services.OllamaLocalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ollama")
public class OllamaController {

    private final OllamaLocalService ollamaLocalService;

    public OllamaController(OllamaLocalService ollamaLocalService) {
        this.ollamaLocalService = ollamaLocalService;
    }

    @PostMapping("/askQuestion")
    public ResponseEntity<?> generateResponse(@RequestBody OllamaDTO dto) {
        return ollamaLocalService.getOllamaResponse(dto.getModel(), dto.getSystem(), dto.getPrompt());
    }
}
