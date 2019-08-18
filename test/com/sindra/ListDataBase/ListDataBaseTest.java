package com.sindra.ListDataBase;

import com.sindra.DataBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}