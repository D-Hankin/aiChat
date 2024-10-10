package com.aichat.aichat.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.aichat.aichat.models.ChatBot;
import com.aichat.aichat.services.ChatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin("*")
public class ChatController {

    @Autowired
    private ChatService chatService;    

    @PostMapping("/chat")
    public ChatBot postChat(@RequestBody ChatBot bot) {

        return chatService.sendChatResponse(bot);
    }
    
    @PostMapping("/audio")
    public ResponseEntity<byte[]> postAudio(@RequestBody ChatBot bot) {
        return chatService.sendAudioResponse(bot);
    }
    

    @PostMapping("/combined")
    public ResponseEntity<MultiValueMap<Object, Object>> postCombinedResponseEntity(@RequestBody ChatBot bot) {
        ChatBot chatBot = chatService.sendChatResponse(bot);
        ResponseEntity<byte[]> audioResponse = chatService.sendAudioResponse(chatBot);
        byte[] audioBytes = audioResponse.getBody();
        
        if (audioBytes == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MultiValueMap<Object, Object> body = new LinkedMultiValueMap<>();
        body.add("chatBot", chatBot);
        body.add("audioFile", new ByteArrayResource(audioBytes) {
            @Override
            public String getFilename() {
                return "audio.mp3";
            }
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        
        return new ResponseEntity<MultiValueMap<Object, Object>>(body, headers, HttpStatus.OK);
    }
    
}   
