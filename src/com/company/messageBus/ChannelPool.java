package com.company.messageBus;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ReturnListener;
import sun.jvm.hotspot.utilities.Assert;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by AlanJager on 2017/1/25.
 */
public class ChannelPool {
    private ChannelPool channelPool;
    private ArrayBlockingQueue<Channel> pool;
    private static final int SIZE = 10;

    ChannelPool(Connection connection) {
        try {
            pool = new ArrayBlockingQueue<Channel>(SIZE);

            for (int i = 0; i < SIZE; i++) {
                Channel channel = connection.createChannel();
                pool.add(channel);
                channel.addReturnListener(new ReturnListener() {
                    @Override
                    public void handleReturn(int i, String s, String s1, String s2, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {

                    }
                });
            }
        } catch (IOException e) {
            System.out.println("Create channel error");
        }


    }

    Channel get() throws InterruptedException {
        try {
            final Channel chan = pool.poll(10, TimeUnit.MINUTES);
            return chan;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  get a channel back to the pool
     */
    public void returnChannel(Channel channel) {
        pool.add(channel);
    }

    /**
     * destroy the pool
     */
    public void destory() {
        for (Channel channel : pool) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
