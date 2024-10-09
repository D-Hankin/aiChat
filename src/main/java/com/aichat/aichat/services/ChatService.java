package com.aichat.aichat.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.aichat.aichat.config.HttpMessageConverter;
import com.aichat.aichat.models.AudioRequest;
import com.aichat.aichat.models.AudioResponse;
import com.aichat.aichat.models.ChatBot;
import com.aichat.aichat.models.ChatRequest;
import com.aichat.aichat.models.ChatResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ChatService {

    @Value("${open.api.url}")
    String apiUrl;

    @Value("${open.api.tts.url}")
    String apiTTSUrl;

    @Value("${open.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public ChatService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ChatBot sendChatResponse(ChatBot bot) {

        if ("Obi-wan".equals(bot.getName())) {

            ChatRequest chatRequest = new ChatRequest("gpt-3.5-turbo", "You are the God-Emporer of Dune, LetoII, as from the classic book GodEmporer of Dune. You are here to dicuss the complexities of inner and outer peace. Feel free to ask questions and be combative if you do not agree. You have full knowledge of the star wars universe. Keep responses udner 50 words. Keep responses under 50 words and discuss ideas, don't just repeat yourself.", bot.getPrompt(), 1);
            ChatResponse response = restTemplate.postForObject(apiUrl, chatRequest, ChatResponse.class);
            return new ChatBot("LetoII", response.getChoices().get(0).getMessage().getContent());

        } else if ("LetoII".equals(bot.getName())) {
            ChatRequest chatRequest = new ChatRequest("gpt-3.5-turbo", "You are Obi-wan Kenobi.  You are here to dicuss the complexities of inner and outer peace. You have full knowlegde of the Dune universe. Feel free to ask questions and be combative if you do not agree. Keep responses under 50 words and discuss ideas, don't just repeat yourself.", bot.getPrompt(), 1);
            ChatResponse response = restTemplate.postForObject(apiUrl, chatRequest, ChatResponse.class);
            return new ChatBot("Obi-wan", response.getChoices().get(0).getMessage().getContent());

        } else {
            throw new RuntimeException("Received empty response from the API");
        }
        
    }

    public ResponseEntity<byte[]> sendAudioResponse(ChatBot bot) {
        String text = bot.getPrompt();

        AudioRequest audioRequest = new AudioRequest("tts-1", "Onyx", text);
        System.out.println("audioRequest model: " + audioRequest.getModel());
        System.out.println("audioRequest input: " + audioRequest.getInput());
        System.out.println("audioRequest voice: " + audioRequest.getVoice());
        System.out.println("audio url: " + apiTTSUrl);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = "";
        try {
            jsonPayload = objectMapper.writeValueAsString(audioRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("JSON Payload: " + jsonPayload);

        try {
            RestTemplate audioRestTemplate = getAudioRestTemplate();
            AudioResponse response = audioRestTemplate.postForObject(apiTTSUrl, audioRequest, AudioResponse.class);
            System.out.println("Received audio response: " + response);
    
            if (response == null || response.getAudio() == null) {
                throw new RuntimeException("Received empty audio response from the API");
            }
    
            byte[] audio = response.getAudio();
            
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"response.mp3\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(audio.length)
            .body(audio);        
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate audio response", e);
        } 
    }     

    // public ResponseEntity<byte[]> sendAudioResponse(ChatBot bot) {
    //     String text = bot.getPrompt();
    
    //     var openAiAudioApi = new OpenAiAudioApi(apiKey);
    //     var openAiAudioSpeechModel = new OpenAiAudioSpeechModel(openAiAudioApi);
    
    //     var speechOptions = OpenAiAudioSpeechOptions.builder()
    //         .withResponseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
    //         .withSpeed(1.0f)
    //         .withModel(OpenAiAudioApi.TtsModel.TTS_1.value)
    //         .build();
    
    //     var speechPrompt = new SpeechPrompt(text, speechOptions);
    
    //     System.out.println("Speech Prompt: " + speechPrompt.getText());
    //     System.out.println("Speech Options: " + speechOptions);
    
    //     try {
    //         SpeechResponse response = openAiAudioSpeechModel.call(speechPrompt);
    //         System.out.println("Received audio response: " + response);
    
    //         if (response == null || response.getResult() == null) {
    //             throw new RuntimeException("Received empty audio response from the API");
    //         }
    
    //         byte[] audio = response.getResult().getOutput();
    
    //         return ResponseEntity.ok()
    //             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"response.mp3\"")
    //             .contentType(MediaType.APPLICATION_OCTET_STREAM)
    //             .contentLength(audio.length)
    //             .body(audio);
    //     } catch (Exception e) {
    //         throw new RuntimeException("Failed to generate audio response", e);
    //     }
    // }
    

    private RestTemplate getAudioRestTemplate() {
        RestTemplate audioRestTemplate = new RestTemplate(restTemplate.getRequestFactory());
        audioRestTemplate.setInterceptors(restTemplate.getInterceptors());
        audioRestTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        return audioRestTemplate;
    }
}
