package com.example.demo.domain.utils;

import com.example.demo.domain.entity.order.PurchaseOrderEntity;

import java.time.format.DateTimeFormatter;

public class GenerateProtocol {


    public static String generateProtocol(PurchaseOrderEntity entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dataFormatted = entity.getDate().format(formatter);
        return String.format("%06d-%06d-%s", entity.getId(), entity.getClient().getId(), dataFormatted);
    }
}
