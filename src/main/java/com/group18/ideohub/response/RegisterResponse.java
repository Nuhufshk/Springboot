package com.group18.ideohub.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterResponse<T> {
    private boolean status;
    private String message;
    private T data;
}
