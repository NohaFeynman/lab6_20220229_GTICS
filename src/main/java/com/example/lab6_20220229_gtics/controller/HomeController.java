package com.example.lab6_20220229_gtics.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String inicio(){ return "home/dashboard"; }
}
