package com.example.demo.application.exception;

import com.example.demo.application.model.error.ErrorResponse;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessException extends RuntimeException {

    private ErrorResponse errorResponse;

    public BusinessException(String title, String description, Integer status) {
        this.errorResponse = new ErrorResponse(title, description, status);
    }
}