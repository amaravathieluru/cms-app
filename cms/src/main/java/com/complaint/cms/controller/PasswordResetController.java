package com.complaint.cms.controller;

import com.complaint.cms.model.Student;
import com.complaint.cms.service.EmailService;
import com.complaint.cms.service.OtpService;
import com.complaint.cms.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PasswordResetController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ─── FORGOT PASSWORD ─────────────────────────────────────

    // Show forgot password page (enter email)
    @GetMapping("/forgot-password")
    public String showForgotPassword() {
        return "forgot-password";
    }

    // Handle email submission - send OTP
    @PostMapping("/forgot-password")
    public String sendOtp(@RequestParam String email, Model model, 
                          jakarta.servlet.http.HttpSession session) {
        Student student = studentService.findByEmail(email);

        if (student == null) {
            model.addAttribute("error", "No account found with this email address.");
            return "forgot-password";
        }

        // Generate and send OTP
        String otp = otpService.generateOtp(email);
        emailService.sendOtpEmail(email, otp);

        // Store email in session so verify-otp page can access it
        session.setAttribute("resetEmail", email);

        return "redirect:/verify-otp";
    }

    // ─── VERIFY OTP ──────────────────────────────────────────

    // Show OTP verification page
    @GetMapping("/verify-otp")
    public String showVerifyOtp(Model model,
                                jakarta.servlet.http.HttpSession session) {
        String email = (String) session.getAttribute("resetEmail");
        if (email == null) {
            return "redirect:/forgot-password";
        }
        model.addAttribute("email", email);
        return "verify-otp";
    }

    // Handle OTP verification
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otp,
                            Model model,
                            jakarta.servlet.http.HttpSession session) {

        if (!otpService.verifyOtp(email, otp)) {
            model.addAttribute("email", email);
            model.addAttribute("error", "Invalid or expired OTP. Please try again.");
            return "verify-otp";
        }

        // OTP verified - store in session and go to reset page
        session.setAttribute("resetEmail", email);
        session.setAttribute("resetOtp", otp);
        return "redirect:/reset-password";
    }

    // ─── RESET PASSWORD ──────────────────────────────────────

    // Show reset password page
    @GetMapping("/reset-password")
    public String showResetPassword(Model model,
                                    jakarta.servlet.http.HttpSession session) {
        String email = (String) session.getAttribute("resetEmail");
        String otp = (String) session.getAttribute("resetOtp");
        if (email == null || otp == null) {
            return "redirect:/forgot-password";
        }
        model.addAttribute("email", email);
        model.addAttribute("otp", otp);
        return "reset-password";
    }

    // Handle new password submission
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String otp,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Model model,
                                jakarta.servlet.http.HttpSession session) {

        // Re-verify OTP for security
        if (!otpService.verifyOtp(email, otp)) {
            model.addAttribute("error", "OTP expired. Please start over.");
            return "redirect:/forgot-password";
        }

        // Check passwords match
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("email", email);
            model.addAttribute("otp", otp);
            model.addAttribute("error", "Passwords do not match.");
            return "reset-password";
        }

        // Check password length
        if (newPassword.length() < 6) {
            model.addAttribute("email", email);
            model.addAttribute("otp", otp);
            model.addAttribute("error", "Password must be at least 6 characters.");
            return "reset-password";
        }

        // Update password in DB
        Student student = studentService.findByEmail(email);
        student.setPassword(passwordEncoder.encode(newPassword));
        studentService.saveStudent(student);

        // Clear OTP and session after use
        otpService.clearOtp(email);
        session.removeAttribute("resetEmail");
        session.removeAttribute("resetOtp");

        // Send confirmation email
        emailService.sendPasswordChangedEmail(email, student.getName());

        return "redirect:/login?passwordReset";
    }

    // ─── CHANGE PASSWORD (logged in students) ────────────────

    // Show change password page
    @GetMapping("/change-password")
    public String showChangePassword() {
        return "change-password";
    }

    // Handle change password
    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Authentication authentication,
            Model model) {

        String username = authentication.getName();
        Student student = studentService.findByUsername(username);

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, student.getPassword())) {
            model.addAttribute("error", "Current password is incorrect.");
            return "change-password";
        }

        // Check new passwords match
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match.");
            return "change-password";
        }

        // Check length
        if (newPassword.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters.");
            return "change-password";
        }

        // Check not same as current
        if (passwordEncoder.matches(newPassword, student.getPassword())) {
            model.addAttribute("error", "New password cannot be the same as current password.");
            return "change-password";
        }

        // Save new password
        student.setPassword(passwordEncoder.encode(newPassword));
        studentService.saveStudent(student);

        // Send confirmation email
        emailService.sendPasswordChangedEmail(student.getEmail(), student.getName());

        return "redirect:/student-dashboard?passwordChanged";
    }
}
