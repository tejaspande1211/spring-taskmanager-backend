package com.taskmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Test", description = "Authentication test endpoint")
@SecurityRequirement(name = "bearerAuth")
@RestController
public class TestController {

    @Operation(summary = "Test authentication", description = "Returns if JWT is valid")
    @GetMapping("/api/test")
    public String test() {
        return "You are authenticated";
    }
}
