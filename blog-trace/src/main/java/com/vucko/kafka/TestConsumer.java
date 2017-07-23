package com.vucko.kafka;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * Created by vucko on 2017/6/27.
 */
public class TestConsumer implements Runnable {

    private final KafkaConsumer<String, String> consumer;

    public TestConsumer(String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094");
        props.put("group.id", KafkaProperties.groupId);
        props.put("enable.auto.commit", "true");        //本例使用自动提交位移
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        this.consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));   // 本例使用分区副本自动分配策略
    }

    public void run() {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(" consumed " + record.partition() +
                        " partition message with offset " + record.offset()+": ");
            }
        }
    }

    public static void main(String[] args) {

        Thread c = new Thread(new TestConsumer(KafkaProperties.topic));
        c.start();

    }
}
