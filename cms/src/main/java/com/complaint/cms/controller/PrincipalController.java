package com.complaint.cms.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.complaint.cms.model.Complaint;
import com.complaint.cms.service.ComplaintService;

@Controller
public class PrincipalController {

    @Autowired
    private ComplaintService complaintService;

    /* Principal Dashboard */
    @GetMapping("/principal")
    public String principalDashboard(Model model){
        model.addAttribute("complaints", complaintService.getAllComplaints());
        return "principal_dashboard";
    }

    /* Update Status + Remarks */
    @PostMapping("/principalUpdate")
    public String updateStatus(
            @RequestParam Long id,
            @RequestParam String status,
            @RequestParam String remarks) {

        Complaint complaint = complaintService.getById(id);
        if (complaint != null) {
            complaint.setStatus(status);
            complaint.setRemarks(remarks);
            complaintService.saveComplaint(complaint);
        }

        return "redirect:/principal";
    }

    /* Delete Complaint */
    @GetMapping("/principalDelete/{id}")
    public String deleteComplaint(@PathVariable Long id){
        if (complaintService.getById(id) != null) {
            complaintService.deleteComplaint(id);
        }
        return "redirect:/principal";
    }

    /* Search Complaint */
    @GetMapping("/principalSearch")
    public String searchComplaint(@RequestParam String rollNumber, Model model){
        List<Complaint> complaints = complaintService.getComplaintByRollNumber(rollNumber);
        model.addAttribute("complaints", complaints);
        return "principal_dashboard";
    }
}