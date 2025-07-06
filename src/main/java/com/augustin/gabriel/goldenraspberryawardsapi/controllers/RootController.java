package com.augustin.gabriel.goldenraspberryawardsapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @GetMapping
    public String get() {
        return "👍 Server is running 🏃💨";
    }
}
