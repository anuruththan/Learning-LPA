package com.example.learning_jpa.service;

import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.IOException;


public interface EmailService {

    public void sendConfirmation(String receiverEmail, String UUID) throws MessagingException, IOException, WriterException;

}
