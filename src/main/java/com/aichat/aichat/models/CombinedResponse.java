package com.aichat.aichat.models;

// NOT CURRENTLY IN USE
public class CombinedResponse {
    private ChatBot chatBot;
    private byte[] audioBytes;

    public CombinedResponse(ChatBot chatBot, byte[] audioBytes) {
        this.chatBot = chatBot;
        this.audioBytes = audioBytes;
    }

    public ChatBot getChatBot() {
        return chatBot;
    }

    public void setChatBot(ChatBot chatBot) {
        this.chatBot = chatBot;
    }

    public byte[] getAudioBytes() {
        return audioBytes;
    }

    public void setAudioBytes(byte[] audioBytes) {
        this.audioBytes = audioBytes;
    }
    
}
