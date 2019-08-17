package com.sindra;

public interface DataBase<type> {
    type getData();
    void set(String key, String keyValue);
    String get(String key);
    void del(String[] key);
    int dbSize();
    void incr();
}
