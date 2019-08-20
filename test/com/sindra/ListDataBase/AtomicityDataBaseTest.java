package com.sindra.ListDataBase;

import com.sindra.DataBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Execution(ExecutionMode.CONCURRENT)
class AtomicityDataBaseTest {

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
}