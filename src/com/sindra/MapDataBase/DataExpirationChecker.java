package com.sindra.MapDataBase;

import com.sindra.Data;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class DataExpirationChecker {
    private MapDataBase dataBase;

    DataExpirationChecker(MapDataBase dataBase, long checkRate) {
        this.dataBase = dataBase;
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(this::checkForExpiredData, 0, checkRate, TimeUnit.SECONDS);
    }

    private void checkForExpiredData() {
        ConcurrentHashMap<String, Data> hashMap = dataBase.getHashMap();
        Iterator<String> it = hashMap.keys().asIterator();

        while(it.hasNext()) {
            String next = it.next();
            Data data = dataBase.getData(next);
            if(data.getExpiration() < System.currentTimeMillis() && data.expires()) dataBase.del(new String[]{next});
        }
    }
}
