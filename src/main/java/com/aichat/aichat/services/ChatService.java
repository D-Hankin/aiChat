package com.aichat.aichat.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.aichat.aichat.models.AudioRequest;
import com.aichat.aichat.models.ChatBot;
import com.aichat.aichat.models.ChatRequest;
import com.aichat.aichat.models.ChatResponse;

@Service
public class ChatService {

    @Value("${open.api.url}")
    String apiUrl;

    @Value("${open.api.tts.url}")
    String apiTTSUrl;

    @Value("${open.api.whisper.url}")
    String apiWhisperUrl;

    private final RestTemplate restTemplate;

    public ChatService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ChatBot sendChatResponse(ChatBot bot) {

        if ("George Carlin".equals(bot.getName())) {

            ChatRequest chatRequest = new ChatRequest("gpt-3.5-turbo", "You are Bill Hicks, the legendary stand-up comedian known for your dark, edgy humor and deep philosophical musings. You're dead. You're unapologetically rebellious, challenging authority and societal norms, often with a mix of cynicism and hope for humanity. You joke about the afterlife, seeing it as another stage of existence, a cosmic joke where the punchline is death itself. Your humor is insightful, irreverent, and laced with existential reflection. You can be crude and unfeeling. Ask questions and be combative if you do not agree. Keep responses under 200 words and discuss ideas.", bot.getPrompt(), 1);
            ChatResponse response = restTemplate.postForObject(apiUrl, chatRequest, ChatResponse.class);
            if (response == null || response.getChoices() == null || response.getChoices().isEmpty() || response.getChoices().get(0).getMessage() == null) {
                throw new RuntimeException("Received invalid response from the API");
            }
            return new ChatBot("Bill Hicks", response.getChoices().get(0).getMessage().getContent());

        } else if ("Bill Hicks".equals(bot.getName())) {
            ChatRequest chatRequest = new ChatRequest("gpt-3.5-turbo", "You are George Carlin, the legendary comedian known for your sharp wit, brilliant observations, and unapologetic critique of society, politics, and human nature. You're dead. You can be crude and unfeeling. Ask questions and be combative if you do not agree. Mix it up to keep the conversation moving on. Keep responses under 200 words and discuss ideas, don't just repeat yourself.", bot.getPrompt(), 1);
            ChatResponse response = restTemplate.postForObject(apiUrl, chatRequest, ChatResponse.class);
            if (response == null || response.getChoices() == null || response.getChoices().isEmpty() || response.getChoices().get(0).getMessage() == null) {
                throw new RuntimeException("Received invalid response from the API");
            }
            return new ChatBot("George Carlin", response.getChoices().get(0).getMessage().getContent());

        } else {
            throw new RuntimeException("Received empty response from the API");
        }
        
    }

    public ResponseEntity<byte[]> sendAudioResponse(ChatBot bot) {
        String text = bot.getPrompt();

        AudioRequest audioRequest;

        if ("George Carlin".equals(bot.getName())) {
            audioRequest = new AudioRequest("tts-1", "echo", text);
        } else if ("Bill Hicks".equals(bot.getName())) {
            audioRequest = new AudioRequest("tts-1", "onyx", text);
        } else {
            throw new RuntimeException("Received invalid response from the API");
        }        

        try {
            byte[] response = restTemplate.postForObject(apiTTSUrl, audioRequest, byte[].class);
    
            if (response == null || response == null) {
                throw new RuntimeException("Received empty audio response from the API");
            }
            
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"response.mp3\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(response.length)
            .body(response);        
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate audio response", e);
        } 
    }

    
    public String getTranscription(byte[] audioData) {

        ByteArrayResource resource = new ByteArrayResource(audioData) {
            @Override
            public String getFilename() {
                return "audio.mp3";
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);
        body.add("model", "whisper-1");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body);

        return restTemplate.postForObject(apiWhisperUrl, requestEntity, String.class); 
    } 

    public String getResponse(String transcription) {
        ChatRequest chatRequest = new ChatRequest("gpt-3.5-turbo", "You are the Devil himself.", transcription, 1);
        ChatResponse response = restTemplate.postForObject(apiUrl, chatRequest, ChatResponse.class);
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty() || response.getChoices().get(0).getMessage() == null) {
            throw new RuntimeException("Received invalid response from the API");
        }
        return response.getChoices().get(0).getMessage().getContent();  
    }

    public byte[] getAudioResponse(String response) {

        AudioRequest audioRequest = new AudioRequest("tts-1", "onyx", response);
        return restTemplate.postForObject(apiTTSUrl, audioRequest, byte[].class);
    }
}
