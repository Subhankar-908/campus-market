package com.ceog.campus_marketplace.Service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {
    private final Map<String, OtpEntry> otpStore = new ConcurrentHashMap<>();
    private static final long OTP_VALID_MS = 5 * 60 * 1000; // 5 min

    private record OtpEntry(String otp, long expiresAt) {}

    public String generateOtp(String mobile) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(mobile, new OtpEntry(otp, System.currentTimeMillis() + OTP_VALID_MS));
        return otp;
    }

    public boolean verifyOtp(String mobile, String otp) {
        OtpEntry entry = otpStore.get(mobile);
        if (entry == null) return false;
        if (System.currentTimeMillis() > entry.expiresAt()) {
            otpStore.remove(mobile);
            return false;
        }
        return entry.otp().equals(otp);
    }

    public void clearOtp(String mobile) {
        otpStore.remove(mobile);
    }
}