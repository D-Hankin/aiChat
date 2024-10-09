package com.aichat.aichat.models;

public class AudioResponse {
    private byte[] audio;

    public AudioResponse(byte[] audio) {
        this.audio = audio;
    }

    public byte[] getAudio() {
        return audio;
    }

    public void setAudio(byte[] audio) {
        this.audio = audio;
    }

    
}
