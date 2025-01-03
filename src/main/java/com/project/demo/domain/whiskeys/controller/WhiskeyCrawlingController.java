package com.project.demo.domain.whiskeys.controller;

import com.project.demo.domain.whiskeys.service.WhiskeyCrawlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WhiskeyCrawlingController {
    @Autowired
    private WhiskeyCrawlingService whiskeyCrawlingService;

    @GetMapping("/crawling")
    public void crawling() {
        whiskeyCrawlingService.crawl();
    }
}
