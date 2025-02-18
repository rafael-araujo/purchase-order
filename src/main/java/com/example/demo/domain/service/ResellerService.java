package com.example.demo.domain.service;

import com.example.demo.application.model.request.ResellerRequest;
import com.example.demo.application.model.response.ResellerResponse;

import java.util.List;

public interface ResellerService {

    ResellerResponse createSeller(ResellerRequest request);
    ResellerResponse getSellerById(Long id);
    List<ResellerResponse> getAllSellers();
    void updateSeller(Long id, ResellerRequest request);
    void deleteSeller(Long id);
}
