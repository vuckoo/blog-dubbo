package com.vucko.parser;

import java.util.Map;

/**
 * Created by vucko on 2017/6/30.
 */
public class Span {

    public Span() {
    }

    public Span(Map<String,Object> map) {
        this.spanId = (String) map.get("spanId");
        this.parSpanId = (String) map.get("parSpanId");
        this.serviceName =  (String) map.get("serviceName");
    }

    private String spanId;

    private String parSpanId;

    private String traceId;

    private String serviceName;

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getParSpanId() {
        return parSpanId;
    }

    public void setParSpanId(String parSpanId) {
        this.parSpanId = parSpanId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}
