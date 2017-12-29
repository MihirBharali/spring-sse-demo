package com.mihir.sse.domain;

public class Dialogue {
   private String chatId;
   private String speaker;
   private String message;

    public Dialogue(String chatId, String speaker, String message) {
        this.chatId = chatId;
        this.speaker = speaker;
        this.message = message;
    }

    public String getChatId() {
        return chatId;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getMessage() {
        return message;
    }
}
