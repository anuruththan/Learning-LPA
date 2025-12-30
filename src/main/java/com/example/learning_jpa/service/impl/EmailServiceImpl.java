package com.example.learning_jpa.service.impl;

import com.example.learning_jpa.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendConfirmation(String receiverEmail, String UUID){
        log.info("Sending confirmation email to {}", receiverEmail);
    }
}
