package com.example.learning_jpa.service;

import org.springframework.stereotype.Service;


public interface EmailService {

    public void sendConfirmation(String receiverEmail, String UUID);

}
