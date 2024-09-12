package com.apiGateway.APIGateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackController {
    @GetMapping("/fallback")
    public String fallbackCall()
    {
        return "some Error occurred... Please try after some time!! If issue still persists contact support team!!";
    }
}
