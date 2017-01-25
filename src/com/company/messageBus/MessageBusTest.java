package com.company.messageBus;

/**
 * Created by AlanJager on 2017/1/25.
 */
public class MessageBusTest {

    public static void main(String[] args) {
        Test test = new Test();
        test.testMessageBusImpl();
    }

    static class Test {
        public void testMessageBusImpl() {
            MessageBus messageBus = new MessageBusImpl();
            Message message = new Message();
            message.setMessage("Hello world!");
            messageBus.send(message);
            messageBus.receive();
        }
    }
}
