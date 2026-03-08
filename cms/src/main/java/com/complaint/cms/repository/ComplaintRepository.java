package com.complaint.cms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.complaint.cms.model.Complaint;

public interface ComplaintRepository extends JpaRepository<Complaint, Long>{

List<Complaint> findByRollNumber(String rollNumber);

long countByStatus(String status);

}