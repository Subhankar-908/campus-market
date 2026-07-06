package com.ceog.campus_marketplace.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.account.sid}")
    private String accountSid;
    @Value("${twilio.auth.token}")
    private String authToken;
    @Value("${twilio.phone.number}")
    private String twilioNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void sendOtpSms(String mobile, String otp) {
        String e164 = "+91" + mobile;  // convert plain 10-digit → E.164
        Message.creator(
                new PhoneNumber(e164),
                new PhoneNumber(twilioNumber),
                "Your CampusMarket OTP is: " + otp
        ).create();
    }
}
