package com.aichat.aichat.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAiRestTemplate {
    
    @Value("${open.api.key}")
    private String apiKey;

    @Bean
    @Qualifier("openaiRestTemplate")
    RestTemplate openaiRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Content-Type", "application/json");
            request.getHeaders().add("Authorization", "Bearer " + apiKey);
            return execution.execute(request, body);
        });
        
        return restTemplate;
    }
}
