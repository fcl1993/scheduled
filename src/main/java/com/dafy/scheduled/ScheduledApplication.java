package com.dafy.scheduled;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"com.dafy.scheduled.web"})
public class ScheduledApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(ScheduledApplication.class, args);
    }

}
