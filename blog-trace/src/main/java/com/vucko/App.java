package com.vucko;

import com.alibaba.fastjson.JSON;
import com.vucko.kafka.KafkaConsumer;
import com.vucko.kafka.KafkaProducer;
import com.vucko.kafka.KafkaProperties;
import com.vucko.parser.Node;
import com.vucko.parser.NodeParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        List<Map<String, Object>> spans = new ArrayList<Map<String, Object>>();
        Map<String, Object> span = new HashMap<String, Object>();
        span.put("spanId", "1");
        span.put("parSpanId", "");
        span.put("serviceName", "getMems.do");
        spans.add(span);
        span = new HashMap<String, Object>();
        span.put("spanId", "5454");
        span.put("parSpanId", "3");
        span.put("serviceName", "datasource2");
        spans.add(span);
        span = new HashMap<String, Object>();
        span.put("spanId", "4");
        span.put("parSpanId", "3");
        span.put("serviceName", "datasource1");
        spans.add(span);
        span = new HashMap<String, Object>();
        span.put("spanId", "6452");
        span.put("parSpanId", "3");
        span.put("serviceName", "datasource1");
        spans.add(span);

        span = new HashMap<String, Object>();
        span.put("spanId", "2");
        span.put("parSpanId", "1");
        span.put("serviceName", "Controller");
        spans.add(span);
        span = new HashMap<String, Object>();
        span.put("spanId", "3");
        span.put("parSpanId", "1");
        span.put("serviceName", "Service");
        spans.add(span);


        NodeParser parser = new NodeParser(spans);
        List<Node> sortSpans = parser.getTreeNode();

        for (int i = 0; i < sortSpans.size(); i++) {
            System.out.println(sortSpans.get(i));
        }

        System.out.println(JSON.toJSONString(sortSpans));

    }
}

