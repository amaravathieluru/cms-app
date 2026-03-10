package com.complaint.cms.repository;

import com.complaint.cms.model.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findByEmail(String email);
    void deleteByEmail(String email);
}
