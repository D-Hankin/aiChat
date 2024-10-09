package com.aichat.aichat.models;

public class AudioRequest {
    private String model;
    private String input;
    private String voice;

    public AudioRequest(String model, String voice, String input) {
        this.model = model;
        this.voice = voice;
        this.input = input;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }
    
}
