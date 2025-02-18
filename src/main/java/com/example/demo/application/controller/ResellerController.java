package com.example.demo.application.controller;

import com.example.demo.application.model.request.ResellerRequest;
import com.example.demo.application.model.response.ResellerResponse;
import com.example.demo.application.validate.OnPost;
import com.example.demo.application.validate.OnPut;
import com.example.demo.domain.service.ResellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/resellers")
public class ResellerController {

    @Autowired
    private ResellerService sellerService;

    @PostMapping
    public ResponseEntity<?> createSeller(@Validated(OnPost.class) @RequestBody ResellerRequest request) {
        ResellerResponse newSeller = sellerService.createSeller(request);
        return ResponseEntity.created(URI.create(
                String.format("/api/resellers/%d", newSeller.getId()))).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResellerResponse> getSellerById(@PathVariable Long id) {
        ResellerResponse seller = sellerService.getSellerById(id);
        return ResponseEntity.ok(seller);
    }

    @GetMapping
    public ResponseEntity<List<ResellerResponse>> getAllSellers() {
        List<ResellerResponse> sellers = sellerService.getAllSellers();
        return ResponseEntity.ok(sellers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSeller(@PathVariable Long id, @Validated({OnPut.class}) @RequestBody ResellerRequest request) {
        sellerService.updateSeller(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
}
