package com.joshua.insureflow.exception;

import org.springframework.web.bind.annotation.ResponseBody;

public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
