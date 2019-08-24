package com.sindra.app;

import com.sindra.DataBase;
import com.sindra.MapDataBase.DataTypes.SetMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class Controller {

    @Autowired
    private DataBase database;

    private static final String OK = "OK";

    @GetMapping("/{key}")
    public String getValueByKey(@PathVariable String key) {
        String value = database.get(key);
        if(value == null) return "(nil)";
        return value;
    }

    @PutMapping("/{key}")
    public String setValueWithKey(@RequestParam String value,
                                  @PathVariable String key,
                                  @RequestParam(defaultValue = "0") int expiration) {
        if (expiration > 0) database.set(key, value, expiration);
        else database.set(key, value);
        return OK;
    }

    @DeleteMapping("/{keys}")
    public int deleteValueWithKey(@PathVariable String[] keys) {
        return database.del(keys);
    }

    @GetMapping("/dbsize")
    public int getSize() {
        return database.dbSize();
    }

    @GetMapping("/incr/{key}")
    public String increaseValueOnKey(@PathVariable String key) {
        return database.incr(key);
    }

    @PutMapping("/zadd/{key}")
    public int setValueWithKey(@PathVariable String key,
                               @RequestParam String[] members) {
        Collection<SetMember> collection = new ArrayList<>();
        for (String member : members) {
            String[] parts = member.split(" ");
            if(parts.length == 2) {
                String score = parts[0];
                String value = parts[1];
                SetMember setMember = new SetMember(score, value);

                if (setMember.isValid()) {
                    collection.add(setMember);
                }
            }
        }
        return database.zadd(key, collection);
    }

    @GetMapping("/zcard/{key}")
    public int getZCard(@PathVariable String key) {
        return database.zcard(key);
    }

    @PostMapping("/zrank/{key}")
    public int getZRank(@PathVariable String key, @RequestParam String member) {
        return database.zrank(key, member);
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/zrange/{key}")
    public String getZRange(@PathVariable String key,
                         @RequestParam int first,
                         @RequestParam int last) {
        ArrayList<SetMember> zrange = database.zrange(key, first, last);
        StringBuilder builder = new StringBuilder();
        for (SetMember member : zrange) {
            if(builder.length() > 0) builder.append("\n");
            builder.append(member.getKey());
        }
        return builder.toString();
    }
}
