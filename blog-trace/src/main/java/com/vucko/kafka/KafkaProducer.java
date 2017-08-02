package com.vucko.kafka;

import com.alibaba.fastjson.JSONObject;
import com.vucko.parser.Span;
import kafka.common.QueueFullException;
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
//        props.put("batch.num.messages", 100);//异步模式单批次提交到broker的消息数量
//        props.put("queue.buffering.max.ms", 5000);//缓存批量数据的等待时间，对达不到单批次缓存数量的保证
//        props.put("queue.buffering.max.message", 1000);//异步模式缓存数据的最大数量
//        props.put("queue.enqueue.timeout.ms", 0);//异步模式缓存数据队列达到最大值时的处理：0当queue满时丢掉，负值是queue满时block,正值是queue满时block相应的时间
        ProducerConfig config = new ProducerConfig(props);
        config.kafka$producer$async$AsyncProducerConfig$_setter_$batchNumMessages_$eq(100);
        config.kafka$producer$async$AsyncProducerConfig$_setter_$queueBufferingMaxMs_$eq(5000);
        config.kafka$producer$async$AsyncProducerConfig$_setter_$queueBufferingMaxMessages_$eq(1000);
        config.kafka$producer$async$AsyncProducerConfig$_setter_$queueEnqueueTimeoutMs_$eq(0);
        producer = new kafka.javaapi.producer.Producer<Integer, String>(config);
        this.topic = topic;
    }

    public void run() {
        int messageNo = 0;

        while (messageNo <5000) {
            String spanId = UUID.randomUUID().toString();
            Span span = new Span();
            span.setSpanId(spanId);
            span.setServiceName("服务调用 ");
            String messageStr = JSONObject.toJSONString(span);
            System.out.println("messageNo:" + messageNo + ": " + messageStr);
            try {
                producer.send(new KeyedMessage<Integer, String>(topic, messageStr));
            }catch (QueueFullException e){
                System.out.println("queue full, throwing new data...");
            }

            messageNo++;
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        messageNo = 0;
        while (messageNo <100) {
            String spanId = UUID.randomUUID().toString();
            Span span = new Span();
            span.setSpanId(spanId);
            span.setServiceName("服务调用 ");
            String messageStr = JSONObject.toJSONString(span);
            System.out.println("messageNo:" + messageNo + ": " + messageStr);
            producer.send(new KeyedMessage<Integer, String>(topic, messageStr));

            messageNo++;
        }
    }

    public static void main(String[] args) {
        Thread p = new Thread(new KafkaProducer(KafkaProperties.topic));
        p.start();

    }
}
