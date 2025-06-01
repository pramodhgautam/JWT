package com.nchl.merchantbusiness.service;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private final SnsClient snsClient;

    @Value("${aws.sns.topic.arn}")
    private String topicArn;

    public OtpService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    public String generateOtp() {

        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);

    }

    public void sendOtpEmail(String email, String otp) {

        String message = "Your OTP is: " + otp + ". This OTP is valid for 5 minutes.";

        PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .message(message)
                .subject("Your OTP Code")
                .messageAttributes(createEmailAttribute(email))
                .build();

        snsClient.publish(request);
    }

    private Map<String, MessageAttributeValue> createEmailAttribute(String email) {
        return Map.of(
                "email", MessageAttributeValue.builder()
                        .dataType("String")
                        .stringValue(email)
                        .build()
        );
    }
}
