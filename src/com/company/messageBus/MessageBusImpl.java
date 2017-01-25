package com.company.messageBus;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by AlanJager on 2017/1/25.
 */
public class MessageBusImpl implements MessageBus{
    private final static String QUEUE_NAME = "message_bug";

    @Override
    public void send(Message message) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, message.getMessage().getBytes());
            System.out.println(" [x] Sent '" + message + "'");

            channel.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Send message error through queue: " + QUEUE_NAME);
        }
    }

    @Override
    public void send(NeedReplyMessage needReplyMessage, MessageCallBack messageCallBack) {

    }

    @Override
    public String receive() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = null;
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                }
            };
            channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
