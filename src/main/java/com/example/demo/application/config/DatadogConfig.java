//package com.example.demo.application.config;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class DatadogConfig {
//
//    @Value("${management.metrics.export.datadog.api-key:não definido}")
//    private String datadogApiKey;
//
//    @PostConstruct
//    public void logApiKey() {
//        System.out.println("Datadog API Key: " + datadogApiKey);
//    }
//}
