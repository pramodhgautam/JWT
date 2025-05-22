package com.nchl.merchantbusiness.utilities;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.util.Base64;

public class QrCodeReader {
    public static String readQrCode(String base64QrCode) throws Exception {
        // Decode base64
        String base64Image = base64QrCode.split(",")[1];
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        // Read image
        BinaryBitmap binaryBitmap = new BinaryBitmap(
                new HybridBinarizer(
                        new BufferedImageLuminanceSource(
                                ImageIO.read(new ByteArrayInputStream(imageBytes))
                        )
                ));

        // Decode QR code
        Result result = new MultiFormatReader().decode(binaryBitmap);
        return result.getText();
    }
}