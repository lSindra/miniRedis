package com.sindra.ListDataBase;

import com.sindra.DataBase;
import java.util.ArrayList;

public class ListDataBase implements DataBase{
    private ArrayList<Node> data;

    ListDataBase() {
        this.data = new ArrayList<>();
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public void set(String key, String keyValue) {
        Node foundNode = getNode(key);
        if(foundNode != null) {
            foundNode.keyValue = keyValue;
        } else {
            data.add(new Node(key, keyValue));
        }
    }

    @Override
    public String get(String key) {
        Node node = getNode(key);
        if(node != null) {
            return node.keyValue;
        }
        return null;
    }

    @Override
    public void del(String[] keys) {
        for (String key : keys) {
            data.remove(getNode(key));
        }
    }

    @Override
    public int dbSize() {
        return data.size();
    }

    @Override
    public void incr(String key) {
        Node node = getNode(key);
        if(node != null && node.keyValue != null) {
            int keyValue = Integer.parseInt(node.keyValue) + 1;
            node.keyValue = Integer.toString(keyValue);
        } else {
            set(key, "1");
        }
    }

    private Node getNode(String key) {
        for (Node node : data) {
            if (node != null && node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }
}
