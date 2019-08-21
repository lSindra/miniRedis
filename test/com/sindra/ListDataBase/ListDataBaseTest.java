package com.sindra.ListDataBase;

import com.sindra.DataBase;
import com.sindra.ListDataBase.DataTypes.SetMembers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListDataBaseTest {

    private DataBase dataBase;

    @BeforeEach
    void setUp() {
        dataBase = new ListDataBase();
    }

    //SET
    @Test
    void shouldBeEmpty() {
        ConcurrentHashMap data = (ConcurrentHashMap) dataBase.getData();
        assert(data.isEmpty());
    }

    @Test
    void shouldHaveSizeOne() {
        dataBase.set("key", "1");
        ConcurrentHashMap data = (ConcurrentHashMap) dataBase.getData();
        assertEquals(1, data.size());
    }

    @Test
    void shouldSetKeyValue() {
        dataBase.set("key1", "1");
        dataBase.set("key2", "2");
        ConcurrentHashMap data = (ConcurrentHashMap) dataBase.getData();
        AtomicReference key1 = (AtomicReference) data.get("key1");
        AtomicReference key2 = (AtomicReference) data.get("key2");
        if(key1.get().equals("1") && key2.get().equals("2")) assert(true);
        else assert(false);
    }

    @Test
    void shouldSetRepeatedKeyWithChangedValue() {
        dataBase.set("key1", "1");
        dataBase.set("key1", "2");
        ConcurrentHashMap data = (ConcurrentHashMap) dataBase.getData();
        AtomicReference key1 = (AtomicReference) data.get("key1");
        if(key1.get().equals("2")) assert(true);
        else assert(false);
    }

    //GET
    @Test
    void shouldGetDataValue3() {
        dataBase.set("key", "3");
        assertEquals("3", dataBase.get("key"));
    }

    @Test
    void shouldGetSizeFour() {
        dataBase.set("key1", "1");
        dataBase.set("key2", "2");
        dataBase.set("key3", "3");
        dataBase.set("key4", "4");

        assertEquals(4, dataBase.dbSize());
    }

    //INCREMENT
    @Test
    void shouldIncreaseKeyValue() {
        dataBase.set("key1", "1");
        assertEquals(1, Integer.parseInt(dataBase.get("key1")));
        dataBase.incr("key1");
        assertEquals(2, Integer.parseInt(dataBase.get("key1")));
        dataBase.incr("key1");
        assertEquals(3, Integer.parseInt(dataBase.get("key1")));
    }

    @Test
    void shouldIncreaseNotFoundKey() {
        dataBase.incr("key1");
        assertEquals(1, Integer.parseInt(dataBase.get("key1")));
    }

    //DELETE
    @Test
    void shouldDeleteOneKey() {
        dataBase.set("key1", "1");
        dataBase.set("key2", "2");
        dataBase.set("key3", "3");
        dataBase.set("key4", "4");

        dataBase.del(new String[]{"key1"});

        assertEquals(3, dataBase.dbSize());
    }

    @Test
    void shouldDeleteMultipleKeys() {
        dataBase.set("key1", "1");
        dataBase.set("key2", "2");
        dataBase.set("key3", "3");
        dataBase.set("key4", "4");

        dataBase.del(new String[]{"key1", "key2"});

        assertEquals(2, dataBase.dbSize());
    }

    //ZADD
    @Test
    void shouldCreateSortedSetWithMembers() {
        Collection<SetMembers> membersCollection = new ArrayList<>();
        membersCollection.add(new SetMembers("1", "uno"));
        membersCollection.add(new SetMembers("2", "uno2"));
        membersCollection.add(new SetMembers("3", "uno3"));

        dataBase.zadd("key1", membersCollection);

        ConcurrentHashMap data = (ConcurrentHashMap) dataBase.getData();
        AtomicReference<Collection<SetMembers>> key = (AtomicReference<Collection<SetMembers>>) data.get("key1");
        assertEquals(3, key.get().size());
    }

    @Test
    void shouldCreateAndUpdateSortedSetWithMembers() {
        Collection<SetMembers> membersCollection = new ArrayList<>();
        membersCollection.add(new SetMembers("1", "uno"));
        membersCollection.add(new SetMembers("1", "uno2"));
        membersCollection.add(new SetMembers("2", "uno2"));

        dataBase.zadd("key1", membersCollection);

        ConcurrentHashMap data = (ConcurrentHashMap) dataBase.getData();
        AtomicReference<Collection<SetMembers>> key = (AtomicReference<Collection<SetMembers>>) data.get("key1");

        assertEquals(1, data.size());
        assertEquals(3, key.get().size());

        membersCollection.add(new SetMembers("2", "uno2"));

        dataBase.zadd("key1", membersCollection);

        assertEquals(1, data.size());
        assertEquals(4, key.get().size());
    }

    @Test
    void sortedSetShouldBeSortedCorrectly() {
        Collection<SetMembers> membersCollection = new ArrayList<>();
        SetMembers uno = new SetMembers("1", "uno");
        SetMembers uno2 = new SetMembers("2", "uno2");
        SetMembers uno3 = new SetMembers("2", "uno3");
        SetMembers uno4 = new SetMembers("3", "uno4");
        SetMembers uno5 = new SetMembers("5", "uno5");

        membersCollection.add(uno5);
        membersCollection.add(uno2);
        membersCollection.add(uno3);
        membersCollection.add(uno4);
        membersCollection.add(uno);

        dataBase.zadd("key1", membersCollection);

        ConcurrentHashMap data = (ConcurrentHashMap) dataBase.getData();
        AtomicReference<Collection<SetMembers>> key = (AtomicReference<Collection<SetMembers>>) data.get("key1");
        assertEquals(5, key.get().size());

        Object[] members = key.get().toArray();
        assert(members[0].equals(uno));
        assert(members[1].equals(uno2));
        assert(members[2].equals(uno3));
        assert(members[3].equals(uno4));
        assert(members[4].equals(uno5));
    }

    //zcard
    @Test
    void shouldGetCorrectSortedSetSize() { //todo taking too long, 16
        Collection<SetMembers> membersCollection = new ArrayList<>();
        membersCollection.add(new SetMembers("1", "uno"));
        membersCollection.add(new SetMembers("2", "uno2"));
        membersCollection.add(new SetMembers("3", "uno3"));

        dataBase.zadd("key1", membersCollection);
        dataBase.zadd("key2", new ArrayList<>());

        assertEquals(3, dataBase.zcard("key1"));
        assertEquals(0, dataBase.zcard("key2"));
    }

    //zrank
    @Test
    void shouldGetCorrectRankForMemberKeyInKey() {
        Collection<SetMembers> membersCollection = new ArrayList<>();
        membersCollection.add(new SetMembers("1", "uno"));
        membersCollection.add(new SetMembers("2", "uno2"));
        membersCollection.add(new SetMembers("3", "uno3"));

        dataBase.zadd("key1", membersCollection);
        dataBase.zadd("key2", new ArrayList<>());

        assertEquals(0, dataBase.zrank("key1", "uno"));
        assertEquals(1, dataBase.zrank("key1", "uno2"));
        assertEquals(2, dataBase.zrank("key1", "uno3"));
        assertEquals(-1, dataBase.zrank("key1", "fake"));
        assertEquals(-1, dataBase.zrank("fake", "uno"));
    }

    //zrange
    @Test
    void shouldGetCorrectRangeForKey() {
        Collection<SetMembers> membersCollection = new ArrayList<>();
        membersCollection.add(new SetMembers("1", "uno"));
        membersCollection.add(new SetMembers("2", "uno2"));
        membersCollection.add(new SetMembers("3", "uno3"));

        dataBase.zadd("key1", membersCollection);

        ArrayList<SetMembers> set = dataBase.zrange("key1", 1, 2);
        assertEquals("uno2", set.get(0).getKey());
        assertEquals("uno3", set.get(1).getKey());
        assertEquals(2, set.size());
    }

    @Test
    void shouldGetAllFromRangeUpToLast() {
        Collection<SetMembers> membersCollection = new ArrayList<>();
        membersCollection.add(new SetMembers("1", "uno"));
        membersCollection.add(new SetMembers("2", "uno2"));
        membersCollection.add(new SetMembers("3", "uno3"));

        dataBase.zadd("key1", membersCollection);

        ArrayList<SetMembers> set = dataBase.zrange("key1", 0, -1);
        assertEquals("uno", set.get(0).getKey());
        assertEquals("uno2", set.get(1).getKey());
        assertEquals("uno3", set.get(2).getKey());
        assertEquals(3, set.size());
    }

    @Test
    void shouldGetRangeEmpty() {
        Collection<SetMembers> membersCollection = new ArrayList<>();
        membersCollection.add(new SetMembers("1", "uno"));
        membersCollection.add(new SetMembers("2", "uno2"));
        membersCollection.add(new SetMembers("3", "uno3"));

        dataBase.zadd("key1", membersCollection);

        ArrayList<SetMembers> set = dataBase.zrange("key1", 2, 1);
        assertEquals(0, set.size());
    }

}