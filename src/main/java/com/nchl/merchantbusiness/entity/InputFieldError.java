package com.nchl.merchantbusiness.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InputFieldError {

    private String field;
    private String message;

}