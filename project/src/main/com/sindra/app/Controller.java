package com.sindra.app;

import com.sindra.DataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    @Autowired
    DataBase database;

    private static final String OK = "OK";

    @GetMapping("/{key}")
    public String getValueByKey(@PathVariable String key) {
        String value = database.get(key);
        if(value == null) return "(nil)";
        return value;
    }

    @PutMapping("/{key}")
    public String setValueWithKey(@RequestParam String value, @PathVariable String key) {
        database.set(key, value);
        return OK;
    }

    @DeleteMapping("/{key}")
    public String deleteValueWithKey(@PathVariable String[] key) {
        database.del(key);
        return OK;
    }
}
