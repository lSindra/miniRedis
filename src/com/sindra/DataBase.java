package com.sindra;

import com.sindra.ListDataBase.DataTypes.SetMembers;

import java.util.Collection;

public interface DataBase<type> {
    type getData();
    void set(String key, String keyValue);
    String get(String key);
    void zadd(String key, Collection<SetMembers> members);
    int zcard(String key);
    int zrank(String key, String memberKey);
    void del(String[] keys);
    int dbSize();
    void incr(String key);
}
