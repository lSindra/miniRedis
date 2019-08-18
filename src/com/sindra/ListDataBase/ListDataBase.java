package com.sindra.ListDataBase;

import com.sindra.DataBase;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ListDataBase implements DataBase {
    private final ConcurrentHashMap<String, AtomicReference<String>> data;

    ListDataBase() {
        this.data = new ConcurrentHashMap<>();
    }

    @Override
    public final Object getData() {
        return data;
    }

    @Override
    public void set(String key, String keyValue) {
        AtomicReference<String> referenceToString = data.get(key);
        if(referenceToString != null) {
            referenceToString.set(keyValue);
        } else {
            data.put(key, new AtomicReference<>(keyValue));
        }
    }

    @Override
    public String get(String key) {
        return data.get(key).get();
    }

    @Override
    public void del(String[] keys) {
        for (String key : keys) {
            data.remove(key);
        }
    }

    @Override
    public int dbSize() {
        return data.size();
    }

    @Override
    public void incr(String key) {
        AtomicReference<String> reference = data.get(key);
        if(reference != null) {
            reference.getAndUpdate(oldValue -> String.valueOf(Integer.parseInt(oldValue) + 1));
        } else {
            set(key, "1");
        }
    }
}
