package com.complaint.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.complaint.cms.model.Complaint;
import com.complaint.cms.service.ComplaintService;

@Controller
public class ComplaintController {

@Autowired
private ComplaintService complaintService;

// Complaint form page
@GetMapping("/complaint")
public String complaintForm(Model model) {

model.addAttribute("complaint", new Complaint());

return "complaint-form";
}

// Save complaint
@PostMapping("/submitComplaint")
public String submitComplaint(@ModelAttribute Complaint complaint) {

complaintService.saveComplaint(complaint);

return "success";
}

// Track complaint page
@GetMapping("/track")
public String trackPage() {

return "track";
}

// Track complaint result
@PostMapping("/track")
public String trackResult(@RequestParam String rollNumber, Model model) {

List<Complaint> complaints =
        complaintService.getComplaintByRollNumber(rollNumber);

model.addAttribute("complaints", complaints);

return "track_result";
}

}