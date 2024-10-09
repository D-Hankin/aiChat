package com.aichat.aichat.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.aichat.aichat.models.ChatBot;
import com.aichat.aichat.services.ChatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    

}
