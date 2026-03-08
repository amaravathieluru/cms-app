package com.complaint.cms.controller;

import com.complaint.cms.model.Student;
import com.complaint.cms.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {

    @Autowired
    private StudentService studentService;

    // Show registration page
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("student", new Student());
        return "register";
    }

    // Handle registration form submission
    @PostMapping("/register")
    public String registerStudent(@ModelAttribute Student student, Model model) {
        String result = studentService.registerStudent(student);

        if (result.equals("success")) {
            return "redirect:/login?registered";
        } else {
            model.addAttribute("error", result);
            model.addAttribute("student", student);
            return "register";
        }
    }
}
