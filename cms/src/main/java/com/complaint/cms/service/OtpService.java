package com.complaint.cms.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    // Stores email -> OTP
    private final Map<String, String> otpStore = new HashMap<>();

    // Generate and store a 6-digit OTP for the given email
    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, otp);
        return otp;
    }

    // Verify OTP for the given email
    public boolean verifyOtp(String email, String otp) {
        String stored = otpStore.get(email);
        if (stored != null && stored.equals(otp)) {
            return true;
        }
        return false;
    }

    // Clear OTP after successful use
    public void clearOtp(String email) {
        otpStore.remove(email);
    }
}
