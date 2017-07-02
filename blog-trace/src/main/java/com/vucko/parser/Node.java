package com.vucko.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vucko on 2017/6/30.
 */
public class Node {

    private String spanId;

    private Span span;

    private int level;

    private Node parNode;

    private List<Node> children;

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public Span getSpan() {
        return span;
    }

    public void setSpan(Span span) {
        this.span = span;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Node getParNode() {
        return parNode;
    }

    public void setParNode(Node parNode) {
        this.parNode = parNode;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public void addChild(Node node) {
        if(children == null){
            children = new ArrayList<Node>();
        }
        this.children.add(node);
    }

    @Override
    public String toString() {
        return "Node{" +
                "spanId='" + spanId + '\'' +
                ", span=" + span +
                ", level=" + level +
                ", parNode=" + parNode +
                '}';
    }
}
