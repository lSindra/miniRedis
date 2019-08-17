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
    public void del(String[] key) {

    }

    @Override
    public int dbSize() {
        return 0;
    }

    @Override
    public void incr() {
        data.add(null);
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
