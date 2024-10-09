package com.aichat.aichat.models;

public class ChatBot {
        
        private String name;
        private String prompt;
    
        public ChatBot(String name, String prompt) {
            this.name = name;
            this.prompt = prompt;
        }
    
        public String getName() {
            return name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public String getPrompt() {
            return prompt;
        }
        
        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }
}
