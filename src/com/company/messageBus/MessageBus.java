package com.company.messageBus;

/**
 * Created by AlanJager on 2017/1/25.
 */
public interface MessageBus {
    public void send(Message message);

    public void send(Message message, MessageCallBack messageCallBack);

    public String receive();
}
