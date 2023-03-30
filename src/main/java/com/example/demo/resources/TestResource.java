package com.example.demo.resources;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/test")
@Slf4j
public class TestResource {

    @GetMapping("/data")
    public String getData(){
        return "Test";
    }
}
