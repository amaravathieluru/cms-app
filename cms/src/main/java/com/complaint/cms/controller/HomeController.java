package com.complaint.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/student-dashboard")
    public String studentDashboard() {
        return "student_dashboard";
    }
}
