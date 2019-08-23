package com.sindra.app;

import com.sindra.DataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.sindra")
public class Application {

    @Autowired
    DataBase dataBase;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
