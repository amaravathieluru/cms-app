package com.complaint.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.complaint.cms.model.Complaint;
import com.complaint.cms.service.ComplaintService;

@Controller
public class HodController {

@Autowired
private ComplaintService complaintService;

/* HOD Dashboard */

@GetMapping("/hod")
public String hodDashboard(Model model){

List<Complaint> complaints = complaintService.getAllComplaints();

model.addAttribute("complaints", complaints);

return "hod_dashboard";
}

/* Update Status + Remarks */

@PostMapping("/updateStatus")
public String updateStatus(
@RequestParam Long id,
@RequestParam String status,
@RequestParam String remarks
){

Complaint complaint = complaintService.getById(id);

complaint.setStatus(status);
complaint.setRemarks(remarks);

complaintService.saveComplaint(complaint);

return "redirect:/hod";
}

/* Delete Complaint */

@GetMapping("/delete/{id}")
public String deleteComplaint(@PathVariable Long id){

complaintService.deleteComplaint(id);

return "redirect:/hod";
}

/* Search by Roll Number */

@GetMapping("/search")
public String searchComplaint(
@RequestParam String rollNumber,
Model model){

List<Complaint> complaints =
        complaintService.getComplaintByRollNumber(rollNumber);

model.addAttribute("complaints", complaints);

return "hod_dashboard";
}

}