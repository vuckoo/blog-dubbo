package com.vucko.kafka;

import com.alibaba.fastjson.JSONObject;
import com.vucko.parser.Span;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.producer.async.AsyncProducerConfig;
import org.mortbay.util.ajax.JSON;

import java.util.Properties;
import java.util.UUID;

/**
 * Created by vucko on 2017/6/27.
 */
public class KafkaProducer implements Runnable {

    private final kafka.javaapi.producer.Producer<Integer, String> producer;
    private final String topic;
    private final Properties props = new Properties();

    public KafkaProducer(String topic) {
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list", "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094");
        props.put("producer.type", "async");
        ProducerConfig config = new ProducerConfig(props);
        config.kafka$producer$SyncProducerConfigShared$_setter_$requestRequiredAcks_$eq((short) 0);
        // 在异步模式下，一个batch发送的消息数量。producer会等待直到要发送的消息数量达到这个值，之后才会发送。
        // 但如果消息数量不够，达到queue.buffer.max.ms时也会直接发送。
        config.kafka$producer$async$AsyncProducerConfig$_setter_$batchNumMessages_$eq(100);
        // 默认值：200，当使用异步模式时，缓冲数据的最大时间。例如设为100的话，会每隔100毫秒把所有的消息批量发送。
        // 这会提高吞吐量，但是会增加消息的到达延时
        config.kafka$producer$async$AsyncProducerConfig$_setter_$queueBufferingMaxMs_$eq(5000);
        producer = new kafka.javaapi.producer.Producer<Integer, String>(config);
        this.topic = topic;
    }

    public void run() {
        int messageNo = 0;

        while (true) {
            String spanId = UUID.randomUUID().toString();
            Span span = new Span();
            span.setSpanId(spanId);
            span.setServiceName("服务调用 ");
            String messageStr = JSONObject.toJSONString(span);
            System.out.println("messageNo:" + messageNo + ": " + messageStr);
            producer.send(new KeyedMessage<Integer, String>(topic, messageStr));

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            messageNo++;
        }
    }

    public static void main(String[] args) {
        Thread p = new Thread(new KafkaProducer(KafkaProperties.topic));
        p.start();

    }
}
