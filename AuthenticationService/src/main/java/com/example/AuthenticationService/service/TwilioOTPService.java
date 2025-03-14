package com.example.AuthenticationService.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
public class TwilioOTPService {

    @Value("${twilio.accountSid}")
    private String ACCOUNT_SID;

    @Value("${twilio.authToken}")
    private String AUTH_TOKEN;

    @Value("${twilio.phoneNumber}")
    private String FROM_PHONE;

    // In-memory map to store OTP and its expiration time
    private final Map<String, OTPDetails> otpStore = new HashMap<>();

    // This runs after all @Value fields are initialized
    @PostConstruct
    public void initTwilio() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//        System.out.println("Twilio Initialized Successfully");
    }

    // Generates a random OTP
    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);  // Generates 6-digit OTP
        return String.valueOf(otp);
    }

    // Sends OTP via SMS to the provided phone number
    public void sendOtp(String phoneNumber, String otp) {
        Message.creator(
                new PhoneNumber(phoneNumber), // To number
                new PhoneNumber(FROM_PHONE),   // From number (your Twilio number)
                "Your OTP is: " + otp         // Message
        ).create();

        // Store OTP and expiration time (2 minutes expiration for example)
        otpStore.put(phoneNumber, new OTPDetails(otp, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(2)));
    }

    // Validate OTP
    public boolean validateOtp(String phoneNumber, String otp) {
        OTPDetails otpDetails = otpStore.get(phoneNumber);

        if (otpDetails != null && otpDetails.getOtp().equals(otp)) {
            // Check if OTP has expired
            if (System.currentTimeMillis() < otpDetails.getExpirationTime()) {
                otpStore.remove(phoneNumber); // OTP is valid, remove it from store
                return true;
            } else {
                otpStore.remove(phoneNumber); // OTP expired, remove it
            }
        }
        return false; // Invalid OTP or expired OTP
    }

    // Helper class to store OTP and its expiration time
    private static class OTPDetails {
        private final String otp;
        private final long expirationTime;

        public OTPDetails(String otp, long expirationTime) {
            this.otp = otp;
            this.expirationTime = expirationTime;
        }

        public String getOtp() {
            return otp;
        }

        public long getExpirationTime() {
            return expirationTime;
        }
    }
    // You can also implement a simple cache or database to store OTP with expiry time
}
