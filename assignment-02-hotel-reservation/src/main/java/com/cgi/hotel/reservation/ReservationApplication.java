package com.cgi.hotel.reservation;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class ReservationApplication {

    @PostConstruct
    public void init() {
        log.info("ReservationApplication started successfully.");
    }

    public static void main(String[] args) {
        SpringApplication.run(ReservationApplication.class, args);
    }
}


