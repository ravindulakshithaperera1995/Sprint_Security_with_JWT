package com.example.demo.resources;

import com.example.demo.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("v1/test")
@Slf4j
public class TestResource {

    @Autowired
    private FileService fileService;

    @GetMapping("/data")
    public String getData(){
        return "Test";
    }

    @PostMapping("/parse")
    public void parseData(@RequestParam MultipartFile file){
        fileService.uploadFile(file);
    }

}
