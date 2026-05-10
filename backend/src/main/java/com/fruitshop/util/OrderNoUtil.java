package com.fruitshop.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class OrderNoUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private final AtomicInteger sequence = new AtomicInteger(0);

    public String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        int seq = sequence.incrementAndGet() % 1000;
        return timestamp + String.format("%03d", seq);
    }
}
