package com.sindra.MapDataBase;

import com.sindra.Data;
import com.sindra.DataBase;
import com.sindra.MapDataBase.DataTypes.SetMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MapDataBaseTest {

    private DataBase dataBase;

    @BeforeEach
    void setUp() {
        dataBase = new MapDataBase();
    }

    //SET
    @Test
    void shouldBeEmpty() {
        ConcurrentHashMap<String, Data> data = ((MapDataBase) dataBase).getHashMap();
        assert(data.isEmpty());
    }

    @Test
    void shouldHaveSizeOne() {
        dataBase.set("key", "1");
        ConcurrentHashMap<String, Data> data = ((MapDataBase) dataBase).getHashMap();
        assertEquals(1, data.size());
    }

    @Test
    void shouldSetKeyValue() {
        dataBase.set("key1", "1");
        dataBase.set("key2", "2");
        ConcurrentHashMap<String, Data> data = ((MapDataBase) dataBase).getHashMap();
        AtomicReference key1 = data.get("key1").getReference();
        AtomicReference key2 = data.get("key2").getReference();
        if(key1.get().equals("1") && key2.get().equals("2")) assert(true);
        else assert(false);
    }

    @Test
    void shouldSetRepeatedKeyWithChangedValue() {
        dataBase.set("key1", "1");
        dataBase.set("key1", "2");
        ConcurrentHashMap<String, Data> data = ((MapDataBase) dataBase).getHashMap();
        AtomicReference key1 = data.get("key1").getReference();
        if(key1.get().equals("2")) assert(true);
        else assert(false);
    }

    @Test
    void shouldNotOverrideTypesDifferentThanString() {
        Collection<SetMember> membersCollection = new ArrayList<>();
        membersCollection.add(new SetMember("1", "uno"));
        membersCollection.add(new SetMember("1", "uno2"));
        membersCollection.add(new SetMember("2", "uno2"));

        dataBase.zadd("key1", membersCollection);
        dataBase.set("key1", "2");

        ConcurrentHashMap<String, Data> data = ((MapDataBase) dataBase).getHashMap();
        assertEquals(1, data.size());
        assert((data.get("key1").getReference()).get() instanceof TreeSet);
    }

    //SET EXPIRATION
    @Test
    void shouldExpire() throws InterruptedException {
        dataBase.set("key", "1", 1);
        ConcurrentHashMap<String, Data> data = ((MapDataBase) dataBase).getHashMap();
        assert(!data.isEmpty());
        sleep(500);
        assert(!data.isEmpty());
        sleep(1500);
        assert(data.isEmpty());
    }

    @Test
    void shouldOverrideExpiration() throws InterruptedException {
        dataBase.set("key", "1", 1);
        ConcurrentHashMap<String, Data> data = ((MapDataBase) dataBase).getHashMap();
        assert(!data.isEmpty());
        sleep(500);
        assert(!data.isEmpty());
        dataBase.set("key", "1", 1);
        sleep(500);
        assert(!data.isEmpty());
        sleep(1500);
        assert(data.isEmpty());
    }

    @Test
    void shouldNeverExpire() throws InterruptedException {
        dataBase.set("key", "1", 1);
        ConcurrentHashMap<String, Data> data = ((MapDataBase) dataBase).getHashMap();
        assert(!data.isEmpty());
        sleep(500);
        assert(!data.isEmpty());
        dataBase.set("key", "1");
        sleep(1500);
        assert(!data.isEmpty());
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

    @Test
    void shouldGetOnlyStringType() {
        Collection<SetMember> membersCollection = new ArrayList<>();
        membersCollection.add(new SetMember("1", "uno"));
        membersCollection.add(new SetMember("1", "uno2"));
        membersCollection.add(new SetMember("2", "uno2"));

        dataBase.zadd("key1", membersCollection);
        dataBase.set("key2", "2");

        assertNull(dataBase.get("key1"));
        assertEquals("2", dataBase.get("key2"));
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
    assertEquals("1", dataBase.incr("key1"));
    assertEquals(1, Integer.parseInt(dataBase.get("key1")));
    }

    @Test
    void shouldFailOnIncrease() {
        dataBase.set("key1", "text");
        assertEquals("Error: value not integer", dataBase.incr("key1"));
        assertEquals("text", dataBase.get("key1"));
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
        Collection<SetMember> membersCollection = new ArrayList<>();
        membersCollection.add(new SetMember("1", "uno"));
        membersCollection.add(new SetMember("2", "uno2"));
        membersCollection.add(new SetMember("3", "uno3"));

        dataBase.zadd("key1", membersCollection);

        ConcurrentHashMap<String, Data> data = ((MapDataBase) dataBase).getHashMap();
        Collection key = (Collection) data.get("key1").getReference().get();
        assertEquals(3, key.size());
    }

    @Test
    void shouldCreateAndUpdateSortedSetWithMembers() {
        Collection<SetMember> membersCollection = new ArrayList<>();
        membersCollection.add(new SetMember("1", "uno"));
        membersCollection.add(new SetMember("1", "uno2"));
        membersCollection.add(new SetMember("2", "uno2"));

        dataBase.zadd("key1", membersCollection);

        ConcurrentHashMap<String, Data> data = ((MapDataBase) dataBase).getHashMap();
        Collection key = (Collection) data.get("key1").getReference().get();

        assertEquals(1, data.size());
        assertEquals(3, key.size());

        membersCollection.add(new SetMember("3", "uno3"));

        dataBase.zadd("key1", membersCollection);

        assertEquals(1, data.size());
        assertEquals(4, key.size());
    }

    @Test
    void sortedSetShouldBeSortedCorrectly() {
        Collection<SetMember> membersCollection = new ArrayList<>();
        SetMember uno = new SetMember("1", "uno");
        SetMember uno2 = new SetMember("2", "uno2");
        SetMember uno3 = new SetMember("2", "uno3");
        SetMember uno4 = new SetMember("3", "uno4");
        SetMember uno5 = new SetMember("5", "uno5");

        membersCollection.add(uno5);
        membersCollection.add(uno2);
        membersCollection.add(uno3);
        membersCollection.add(uno4);
        membersCollection.add(uno);

        dataBase.zadd("key1", membersCollection);

        ConcurrentHashMap<String, Data> data = ((MapDataBase) dataBase).getHashMap();
        Collection key = (Collection) data.get("key1").getReference().get();
        assertEquals(5, key.size());

        Object[] members = key.toArray();
        assert(members[0].equals(uno));
        assert(members[1].equals(uno2));
        assert(members[2].equals(uno3));
        assert(members[3].equals(uno4));
        assert(members[4].equals(uno5));
    }

    @Test
    void shouldNotOverrideTypesDifferentThanSortedSet() {
        Collection<SetMember> membersCollection = new ArrayList<>();
        membersCollection.add(new SetMember("1", "uno"));
        membersCollection.add(new SetMember("1", "uno2"));
        membersCollection.add(new SetMember("2", "uno2"));

        dataBase.set("key1", "1");
        dataBase.zadd("key1", membersCollection);

        ConcurrentHashMap<String, Data> data = ((MapDataBase) dataBase).getHashMap();
        assertEquals(1, data.size());
        assert(( data.get("key1").getReference()).get() instanceof String);
    }

    //zcard
    @Test
    void shouldGetCorrectSortedSetSize() {
        Collection<SetMember> membersCollection = new ArrayList<>();
        membersCollection.add(new SetMember("1", "uno"));
        membersCollection.add(new SetMember("2", "uno2"));
        membersCollection.add(new SetMember("3", "uno3"));

        dataBase.zadd("key1", membersCollection);
        dataBase.zadd("key2", new ArrayList<>());

        assertEquals(3, dataBase.zcard("key1"));
        assertEquals(0, dataBase.zcard("key2"));
    }

    //zrank
    @Test
    void shouldGetCorrectRankForMemberKeyInKey() {
        Collection<SetMember> membersCollection = new ArrayList<>();
        membersCollection.add(new SetMember("1", "uno"));
        membersCollection.add(new SetMember("2", "uno2"));
        membersCollection.add(new SetMember("3", "uno3"));

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
        Collection<SetMember> membersCollection = new ArrayList<>();
        membersCollection.add(new SetMember("1", "uno"));
        membersCollection.add(new SetMember("2", "uno2"));
        membersCollection.add(new SetMember("3", "uno3"));

        dataBase.zadd("key1", membersCollection);

        ArrayList<SetMember> set = dataBase.zrange("key1", 1, 2);
        assertEquals("uno2", set.get(0).getKey());
        assertEquals("uno3", set.get(1).getKey());
        assertEquals(2, set.size());
    }

    @Test
    void shouldGetAllFromRangeUpToLast() {
        Collection<SetMember> membersCollection = new ArrayList<>();
        membersCollection.add(new SetMember("1", "uno"));
        membersCollection.add(new SetMember("2", "uno2"));
        membersCollection.add(new SetMember("3", "uno3"));

        dataBase.zadd("key1", membersCollection);

        ArrayList<SetMember> set = dataBase.zrange("key1", 0, -1);
        assertEquals("uno", set.get(0).getKey());
        assertEquals("uno2", set.get(1).getKey());
        assertEquals("uno3", set.get(2).getKey());
        assertEquals(3, set.size());
    }

    @Test
    void shouldGetRangeEmpty() {
        Collection<SetMember> membersCollection = new ArrayList<>();
        membersCollection.add(new SetMember("1", "uno"));
        membersCollection.add(new SetMember("2", "uno2"));
        membersCollection.add(new SetMember("3", "uno3"));

        dataBase.zadd("key1", membersCollection);

        ArrayList<SetMember> set = dataBase.zrange("key1", 2, 1);
        assertEquals(0, set.size());
    }
}