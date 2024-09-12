package com.example.bookstore.controller;

import com.example.bookstore.service.GenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generate")
public class GenerateController {

    private final GenerateService generateService;

    @Autowired
    public GenerateController(GenerateService generateService) {
        this.generateService = generateService;
    }

    @PostMapping("/authors")
    public void generateAuthors(@RequestParam int count) {
        generateService.generateAuthors(count);
    }

    @PostMapping("/books")
    public void generateBooks(@RequestParam int count) {
        generateService.generateBooks(count);
    }

    @PostMapping("/genres")
    public void generateGenres(@RequestParam int count) {
        generateService.generateGenres(count);
    }

}
