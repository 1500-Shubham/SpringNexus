package com.example.OllamaService.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.json.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
public class OllamaLocalService {
    // local ollama installed via homebrew

    private final RestTemplate restTemplate = new RestTemplate(); // Initialize RestTemplate
    @Value("${ollama.server}")
    private String apiUrl;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper


    public ResponseEntity<?> getOllamaResponse(String model, String systemPrompt, String userPrompt) {
        try {
            // Create the JSON request body using Jackson
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", model != null ? model : "llama2"); // Default to "llama2" if null
            requestBody.put("prompt", (systemPrompt != null ? systemPrompt : "") + " " +
                    (userPrompt != null ? userPrompt : "")); // Combine prompts
            requestBody.put("stream", false); // Disable streaming

            // Create HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Wrap request body and headers in an HttpEntity
            HttpEntity<String> httpEntity = new HttpEntity<>(requestBody.toString(), headers);

            // Send the POST request and get the response as a String
            // Send the POST request and get the response as a String
            String jsonResponse = restTemplate.postForObject(apiUrl, httpEntity, String.class);

            // Parse the response JSON and extract the "response" field
            JsonNode responseNode = objectMapper.readTree(jsonResponse); // Parse JSON response
            String response = responseNode.get("response").asText(); // Extract "response" field
            return ResponseEntity.of(Optional.of(Map.of("response", response))); // Return the extracted response field

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.of(Map.of("response", "Error: Unable to fetch response from Ollama API.")));
        }
    }
}

