package com.aichat.aichat.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.aichat.aichat.models.ChatResponse;
import com.aichat.aichat.services.ChatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin("*")
public class ChatController {

    @Autowired
    private ChatService chatService;    

    @PostMapping("/chat")
    public ChatResponse postChat(@RequestBody String promt) {

        return chatService.sendChatResponse(promt);
    }
    
}
