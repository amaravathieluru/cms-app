package com.complaint.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.complaint.cms.service.ComplaintService;

@Controller
public class DashboardController {

@Autowired
private ComplaintService complaintService;

@GetMapping("/dashboard")
public String dashboard(Authentication authentication, Model model) {

String role = authentication.getAuthorities().toString();

if(role.contains("STUDENT")) {
return "student_dashboard";
}

else if(role.contains("HOD")) {

model.addAttribute("total", complaintService.total());
model.addAttribute("solved", complaintService.solved());
model.addAttribute("pending", complaintService.pending());

return "dashboard";   // HOD dashboard
}

else if(role.contains("PRINCIPAL")) {
return "principal_dashboard";
}

return "dashboard";
}

}