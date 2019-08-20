package com.sindra.ListDataBase;

import com.sindra.DataBase;
import com.sindra.ListDataBase.DataTypes.SetMembers;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ListDataBase implements DataBase {
    private final ConcurrentHashMap<String, AtomicReference> data;

    ListDataBase() {
        this.data = new ConcurrentHashMap<>();
    }

    @Override
    public final Object getData() {
        return data;
    }

    @Override
    public synchronized void set(String key, String keyValue) {
        AtomicReference referenceToString = data.get(key);
        if(referenceToString != null && referenceToString.get() instanceof String) {
            referenceToString.set((keyValue));
        } else {
            data.put(key, new AtomicReference<>(keyValue));
        }
    }

    @Override
    public String get(String key) {
        AtomicReference atomicReference = data.get(key);
        if(atomicReference != null) {
            Object o = data.get(key).get();
            if (o instanceof String) return (String) data.get(key).get();
        }
        return null;
    }

    @Override
    public synchronized void zadd(String key, Collection members) {
        AtomicReference referenceToString = data.get(key);
        if(referenceToString != null && referenceToString.get() instanceof TreeSet) {
            referenceToString.set((members));
        } else {
            TreeSet<SetMembers> sortedSet = new TreeSet<>(members);
            data.put(key, new AtomicReference<>(sortedSet));
        }
    }

    @Override
    public int zcard(String key) {
        return Objects.requireNonNull(zget(key)).size();//todo make sure
    }

    @Override
    public int zrank(String key, String memberKey) {
        TreeSet<SetMembers> treeSet = zget(key);

        if(treeSet == null) return -1;

        return filterSortedSetByKey(memberKey, treeSet);
    }

    private int filterSortedSetByKey(String memberKey, TreeSet<SetMembers> treeSet) {
        SetMembers member = treeSet.stream().filter(
                customer -> customer.getKey().equals(memberKey))
                .findAny().orElse(new SetMembers("0", "null"));

        return treeSet.contains(member) ? treeSet.headSet(member).size() : -1;
    }

    private TreeSet zget(String key) {
        AtomicReference atomicReference = data.get(key);
        if(atomicReference != null) {
            Object o = atomicReference.get();
            if (o instanceof TreeSet) {
                return (TreeSet) o;
            }
        }
        return null;
    }

    @Override
    public synchronized void del(String[] keys) {
        for (String key : keys) {
            data.remove(key);
        }
    }

    @Override
    public int dbSize() {
        return data.size();
    }

    @Override
    public synchronized void incr(String key) {
        AtomicReference reference = data.get(key);
        if(reference != null && reference.get() instanceof String) {
            reference.getAndUpdate(
                    oldValue -> String.valueOf(Integer.parseInt((String) oldValue) + 1));
        } else {
            set(key, "1");
        }
    }
}
