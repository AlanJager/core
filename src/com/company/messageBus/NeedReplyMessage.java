package com.company.messageBus;

/**
 * Created by zouye on 2017/1/25.
 */
public class NeedReplyMessage {
    private int timeout;
    private String message;

    public NeedReplyMessage(String message, int timeout) {
        this.message = message;
        this.timeout = timeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
