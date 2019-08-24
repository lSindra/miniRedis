package com.sindra;

import com.sindra.MapDataBase.DataTypes.SetMember;

import java.util.ArrayList;
import java.util.Collection;

public interface DataBase<type> {
    type getHashMap();
    void set(String key, String keyValue);
    void set(String key, String keyValue, int expirationInSeconds);
    String get(String key);
    int zadd(String key, Collection<SetMember> members);
    int zcard(String key);
    int zrank(String key, String memberKey);
    ArrayList<SetMember> zrange(String key, int first, int last);
    int del(String[] keys);
    int dbSize();
    String incr(String key);
}
