package com.example.demo.application.controller;

import com.example.demo.application.model.request.ResellerRequest;
import com.example.demo.application.model.response.ResellerResponse;
import com.example.demo.domain.service.ResellerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResellerControllerTest {

    @Mock
    private ResellerService resellerService;

    @InjectMocks
    private ResellerController resellerController;

    @Test
    public void testCreateSeller() {

        ResellerRequest request = new ResellerRequest();
        request.setCnpj("04252011000110"); // Configure outros atributos conforme necessário

        ResellerResponse response = new ResellerResponse();
        response.setId(1L);

        when(resellerService.createSeller(any(ResellerRequest.class))).thenReturn(response);

        ResponseEntity<?> result = resellerController.createSeller(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(URI.create("/api/resellers/1"), result.getHeaders().getLocation());
        verify(resellerService, times(1)).createSeller(any(ResellerRequest.class));
    }

    @Test
    public void testGetSellerById() {

        Long sellerId = 1L;
        ResellerResponse response = new ResellerResponse();
        response.setId(sellerId);

        when(resellerService.getSellerById(sellerId)).thenReturn(response);

        ResponseEntity<ResellerResponse> result = resellerController.getSellerById(sellerId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(sellerId, result.getBody().getId());
        verify(resellerService, times(1)).getSellerById(sellerId);
    }

    @Test
    public void testGetAllSellers() {

        ResellerResponse response1 = new ResellerResponse();
        response1.setId(1L);
        ResellerResponse response2 = new ResellerResponse();
        response2.setId(2L);
        List<ResellerResponse> sellers = Arrays.asList(response1, response2);

        when(resellerService.getAllSellers()).thenReturn(sellers);

        ResponseEntity<List<ResellerResponse>> result = resellerController.getAllSellers();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
        verify(resellerService, times(1)).getAllSellers();
    }

    @Test
    public void testUpdateSeller() {

        Long sellerId = 1L;
        ResellerRequest request = new ResellerRequest();
        request.setCnpj("04252011000110"); // Configure outros atributos conforme necessário

        doNothing().when(resellerService).updateSeller(eq(sellerId), any(ResellerRequest.class));

        ResponseEntity<?> result = resellerController.updateSeller(sellerId, request);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(resellerService, times(1)).updateSeller(eq(sellerId), any(ResellerRequest.class));
    }

    @Test
    public void testDeleteSeller() {

        Long sellerId = 1L;
        doNothing().when(resellerService).deleteSeller(sellerId);

        ResponseEntity<Void> result = resellerController.deleteSeller(sellerId);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(resellerService, times(1)).deleteSeller(sellerId);
    }
}