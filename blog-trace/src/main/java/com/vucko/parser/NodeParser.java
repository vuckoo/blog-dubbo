package com.vucko.parser;

import java.util.*;

/**
 * Created by vucko on 2017/6/30.
 */
public class NodeParser {

    private Node root;

    private Map<String, Node> nodeMap;

    public NodeParser() {
    }

    public NodeParser(List<Map<String, Object>> spans) {
        nodeMap = new HashMap<String, Node>(20);
        for (int i = 0; i < spans.size(); i++) {
            Span span = new Span(spans.get(i));
            Node node = new Node();
            node.setSpanId(span.getSpanId());
            node.setSpan(span);
            if(nodeMap.containsKey(node.getSpanId())){
                throw new RuntimeException("duplicate spanId: "+ node.getSpanId());
            }
            nodeMap.put(node.getSpanId(), node);
            if ("".equals(node.getSpan().getParSpanId())) {
                root = node;//找到根节点
            }
        }
    }

    public List<Node> getTreeNode() {

        Node root = getTree();
        List<Node> nodes = new ArrayList<Node>();
        tree2List(nodes, root, 0);
        if (nodes.size() != nodeMap.size()) {
            throw new RuntimeException("size of tree nodes and size of node list not match : " + nodes.size() + "-" + nodeMap.size());
        }

        return nodes;
    }

    private void tree2List(List<Node> nodes, Node node, int startLevel) {
        //add itself
        node.setLevel(startLevel);
        nodes.add(node);
        //add children nodes
        List<Node> children = node.getChildren();
        if (children != null) {
            children.sort((o1, o2) -> o1.getSpanId().compareTo(o2.getSpanId()));
            for (int i = 0; i < children.size(); i++) {
                Node child = children.get(i);
                tree2List(nodes, child, startLevel + 1);
            }
        }
    }

    private Node getTree() {

        Iterator<String> it = nodeMap.keySet().iterator();
        while (it.hasNext()) {
            String spanId = it.next();
            if (root.getSpanId().equals(spanId)) {
                continue;
            }
            Node node = nodeMap.get(spanId);
            String parSpanId = node.getSpan().getParSpanId();
            if (nodeMap.containsKey(parSpanId)) {
                Node parNode = nodeMap.get(parSpanId);
                node.setParNode(parNode);
                parNode.addChild(node);
            } else {
                throw new RuntimeException("parent span not found by id: " + parSpanId);
            }
        }

        return root;
    }


}
