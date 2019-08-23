package com.sindra.MapDataBase;

import com.sindra.Data;
import com.sindra.DataBase;
import com.sindra.MapDataBase.DataTypes.SetMembers;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Service("Database")
public class MapDataBase implements DataBase {
    private final ConcurrentHashMap<String, Data> hashMap;

    public MapDataBase() {
        this.hashMap = new ConcurrentHashMap<>();
        new DataExpirationChecker(this, 1);
    }

    @Override
    public final ConcurrentHashMap<String, Data> getHashMap() {
        return hashMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized void set(String key, String keyValue) {
        Data data = getData(key);
        AtomicReference referenceToString = data.getReference();
        if(referenceToString.get() != null) {
            if(referenceToString.get() instanceof String) {
                referenceToString.set((keyValue));
                data.setExpires(false);
            }
        } else hashMap.put(key, new Data(keyValue));
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized void set(String key, String keyValue, int expirationTime) {
        Data data = getData(key);
        AtomicReference referenceToString = data.getReference();
        if(referenceToString.get() != null) {
            if(referenceToString.get() instanceof String) {
                referenceToString.set(keyValue);
                data.setExpiration(expirationTime);
                data.setExpires(true);
            }
        } else hashMap.put(key, new Data(keyValue, expirationTime));
    }

    @Override
    public String get(String key) {
        AtomicReference atomicReference = getData(key).getReference();
        if(atomicReference != null) {
            Object o = atomicReference.get();
            if (o instanceof String) return (String) atomicReference.get();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized void zadd(String key, Collection members) {
        AtomicReference referenceToString = getData(key).getReference();
        if(referenceToString.get() != null) {
            if(referenceToString.get() instanceof TreeSet) referenceToString.set((members));
        } else {
            TreeSet<SetMembers> sortedSet = new TreeSet<>(members);
            hashMap.put(key, new Data(sortedSet));
        }
    }

    @Override
    public int zcard(String key) {
        return Objects.requireNonNull(zget(key)).size();
    }

    @Override
    public int zrank(String key, String memberKey) {
        TreeSet<SetMembers> treeSet = zget(key);

        if(treeSet == null) return -1;

        return filterSortedSetByKey(memberKey, treeSet);
    }

    @Override
    public ArrayList<SetMembers> zrange(String key, int first, int last) {
        TreeSet<SetMembers> set = zget(key);
        if (set != null) {
            if(last < 0) last = set.size() + last;
            return getSubSetFromSortedSetByIndex(set, first, last);
        }
        return new ArrayList<>();
    }

    private ArrayList<SetMembers> getSubSetFromSortedSetByIndex(
            TreeSet<SetMembers> treeSet, int firstIndex, int lastIndex) {
        Iterator<SetMembers> it = treeSet.iterator();
        ArrayList<SetMembers> subset = new ArrayList<>();

        if(firstIndex > lastIndex) return subset;

        int i = 0;
        while(it.hasNext() && i <= lastIndex) {
            SetMembers next = it.next();

            if(i >= firstIndex) subset.add(next);

            i++;
        }

        return subset;
    }

    private int filterSortedSetByKey(String memberKey, TreeSet<SetMembers> treeSet) {
        SetMembers member = treeSet.stream().filter(
                customer -> customer.getKey().equals(memberKey))
                .findAny().orElse(new SetMembers("0", "null"));

        return treeSet.contains(member) ? treeSet.headSet(member).size() : -1;
    }

    @SuppressWarnings("unchecked")
    private TreeSet<SetMembers> zget(String key) {
        AtomicReference atomicReference = getData(key).getReference();
        if(atomicReference != null) {
            Object o = atomicReference.get();
            if (o instanceof TreeSet) {
                return (TreeSet<SetMembers>) o;
            }
        }
        return null;
    }

    @Override
    public synchronized void del(String[] keys) {
        for (String key : keys) {
            hashMap.remove(key);
        }
    }

    @Override
    public int dbSize() {
        return hashMap.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized boolean incr(String key) {
        Data data = getData(key);
        AtomicReference reference = data.getReference();
        if(reference.get() != null) {
            boolean referenceIsStringOfNumber = reference.get() instanceof String
                    && canParseInt((String) reference.get());
            if(referenceIsStringOfNumber) {
                reference.getAndUpdate(
                    oldValue -> String.valueOf(Integer.parseInt((String) oldValue) + 1));
                return true;
            } else return false;
        } else {
            set(key, "1", 0);
            return true;
        }
    }

    private boolean canParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    Data getData(String key) {
        Data data = hashMap.get(key);

        if(data == null) data = new Data(null);
        return data;
    }
}
