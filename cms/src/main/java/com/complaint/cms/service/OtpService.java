package com.complaint.cms.service;

import com.complaint.cms.model.OtpToken;
import com.complaint.cms.repository.OtpTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpTokenRepository otpTokenRepository;

    @Transactional
    public String generateOtp(String email) {
        // Delete any existing OTP for this email
        otpTokenRepository.deleteByEmail(email);

        // Generate new 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Save to database
        OtpToken token = new OtpToken(email, otp);
        otpTokenRepository.save(token);

        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        Optional<OtpToken> token = otpTokenRepository.findByEmail(email);

        if (token.isEmpty()) return false;

        // Check if expired
        if (LocalDateTime.now().isAfter(token.get().getExpiryTime())) {
            otpTokenRepository.deleteByEmail(email);
            return false;
        }

        return token.get().getOtp().equals(otp);
    }

    @Transactional
    public void clearOtp(String email) {
        otpTokenRepository.deleteByEmail(email);
    }
}
