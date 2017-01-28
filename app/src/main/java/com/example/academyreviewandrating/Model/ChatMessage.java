package com.example.academyreviewandrating.Model;
import java.util.Date;

/**
 * Created by reale on 18/11/2016.
 */

/**
 * Chat class model
 * data on chat message
 */
public class ChatMessage {
    private String messageText;
    private String messageUser;
    private long messageTime;
    private Boolean read = true;

    /**
     * Constructor
     * @param messageText
     * @param messageUser
     * @param R
     */
    public ChatMessage(String messageText, String messageUser,Boolean R) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.read = R;

        messageTime = new Date().getTime();
    }

    public ChatMessage() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public void setUnread(Boolean R)
    {
        read = R;
    }
    public Boolean getread()
    {
        return read;
    }


}

