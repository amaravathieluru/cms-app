package com.complaint.cms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.complaint.cms.model.Complaint;
import com.complaint.cms.repository.ComplaintRepository;

@Service
public class ComplaintService {

@Autowired
private ComplaintRepository complaintRepository;

/* Save complaint */

public void saveComplaint(Complaint complaint) {
complaintRepository.save(complaint);
}

/* Get complaints by roll number */

public List<Complaint> getComplaintByRollNumber(String rollNumber) {
return complaintRepository.findByRollNumber(rollNumber);
}

/* Get all complaints */

public List<Complaint> getAllComplaints() {
return complaintRepository.findAll();
}

/* Get complaint by ID */

public Complaint getById(Long id){
return complaintRepository.findById(id).orElse(null);
}

/* Delete complaint */

public void deleteComplaint(Long id){
complaintRepository.deleteById(id);
}

/* Dashboard statistics */

public long total() {
return complaintRepository.count();
}

public long solved() {
return complaintRepository.countByStatus("Solved");
}

public long pending() {
return complaintRepository.countByStatus("Pending");
}

/* Update status + remarks */

public void updateStatus(Long id, String status, String remarks) {

Complaint complaint = complaintRepository.findById(id).orElse(null);

if (complaint != null) {

complaint.setStatus(status);
complaint.setRemarks(remarks);

complaintRepository.save(complaint);

}

}

}