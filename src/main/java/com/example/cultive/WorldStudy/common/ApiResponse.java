package com.example.cultive.WorldStudy.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private Integer StatusCode;
    private T data;
}
