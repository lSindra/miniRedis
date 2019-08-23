package com.sindra;

import com.sindra.MapDataBase.DataTypes.SetMembers;

import java.util.ArrayList;
import java.util.Collection;

public interface DataBase<type> {
    type getHashMap();
    void set(String key, String keyValue);
    void set(String key, String keyValue, int expirationInSeconds);
    String get(String key);
    void zadd(String key, Collection<SetMembers> members);
    int zcard(String key);
    int zrank(String key, String memberKey);
    ArrayList<SetMembers> zrange(String key, int first, int last);
    void del(String[] keys);
    int dbSize();
    boolean incr(String key);
}
