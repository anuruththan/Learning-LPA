package com.example.learning_jpa.service.impl;

import com.example.learning_jpa.service.EmailService;
import com.example.learning_jpa.service.QrCodeGenService;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private QrCodeGenService qrCodeGenService;

    @Value("${spring.mail.username}")
    private String emailId;

    @Override
    public void sendConfirmation(String receiverEmail, String uuid) throws MessagingException, IOException, WriterException {

        byte[] qrCode = qrCodeGenService.generateQrCode(uuid);
        Context context = new Context();
        context.setVariable("name", receiverEmail);
        context.setVariable("uuid", uuid);

        String htmlContent = templateEngine.process("qr-email", context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, true);
        helper.setFrom(emailId);
        helper.setTo(receiverEmail);
        helper.setSubject("🎟️ Your Secure QR Pass For The Stall");
        helper.setText(htmlContent, true);

        helper.addInline("qrCode",
                new ByteArrayResource(qrCode),
                "image/png");

        javaMailSender.send(message);
    }
}
