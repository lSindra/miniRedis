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
        dataBase.incr();
        ArrayList list = (ArrayList) dataBase.getData();
        assertEquals(1, list.size());
    }

    @Test
    void shouldSetKeyValue() {
        dataBase.incr();
        dataBase.set("key", "value");
        dataBase.set("key1", "value1");
        for (Object node : (ArrayList) dataBase.getData()) {
            if (node != null) {
                try {
                    Node castedNode = (Node) node;

                    if (castedNode.key.equals("key1") && castedNode.keyValue.equals("value1")) {
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
        dataBase.incr();
        dataBase.set("key", "value");
        dataBase.set("key", "value1");
        for (Object node : (ArrayList) dataBase.getData()) {
            if (node != null) {
                try {
                    Node castedNode = (Node) node;

                    if (castedNode.key.equals("key") && castedNode.keyValue.equals("value1")) {
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
}