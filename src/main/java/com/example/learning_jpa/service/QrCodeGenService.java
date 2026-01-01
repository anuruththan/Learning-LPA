package com.example.learning_jpa.service;

import com.google.zxing.WriterException;

import java.io.IOException;

public interface QrCodeGenService {
    public byte[] generateQrCode(String uuid) throws WriterException, IOException;
}
