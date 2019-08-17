package com.sindra.ListDataBase;

import com.sindra.DataBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListDataBaseTest {

    private DataBase dataBase;

    @BeforeEach
    void setUp() {
        dataBase = new ListDataBase();
    }

    @Test
    void shouldBeEmpty() {
        ArrayList list = (ArrayList) dataBase.getData();
        assert(list.isEmpty());
    }

    @Test
    void shouldHaveSizeOne() {
        dataBase.set("key", "1");
        ArrayList list = (ArrayList) dataBase.getData();
        assertEquals(1, list.size());
    }

    @Test
    void shouldSetKeyValue() {
        dataBase.set("key1", "1");
        dataBase.set("key2", "2");
        for (Object node : (ArrayList) dataBase.getData()) {
            if (node != null) {
                try {
                    Node castedNode = (Node) node;

                    if (castedNode.key.equals("key2") && castedNode.keyValue.equals("2")) {
                        assert (true);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        assert(false);
    }

    @Test
    void shouldSetRepeatedKeyWithChangedValue() {
        dataBase.set("key1", "1");
        dataBase.set("key1", "2");
        for (Object node : (ArrayList) dataBase.getData()) {
            if (node != null) {
                try {
                    Node castedNode = (Node) node;

                    if (castedNode.key.equals("key1") && castedNode.keyValue.equals("2")) {
                        assert (true);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        assert(false);
    }

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
    void shouldIncreaseKeyValue() {
        dataBase.set("key1", "1");
        assertEquals(1, Integer.parseInt(dataBase.get("key1")));
        dataBase.incr("key1");
        assertEquals(2, Integer.parseInt(dataBase.get("key1")));
    }

    @Test
    void shouldIncreaseNotFoundKey() {
        dataBase.incr("key1");
        assertEquals(1, Integer.parseInt(dataBase.get("key1")));
    }

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