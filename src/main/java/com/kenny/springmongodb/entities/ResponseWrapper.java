package com.kenny.springmongodb.entities;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWrapper<T> {

    private String  message = "Request was successful";
    private int code = 200;
    private T data;


}
