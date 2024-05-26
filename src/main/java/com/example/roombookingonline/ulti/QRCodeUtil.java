package com.example.roombookingonline.ulti;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QRCodeUtil {
    public static void saveFile(String data, String uploadDir, String fileName) throws IOException, WriterException {
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        String path = uploadDir + "/" + fileName + ".jpg";
        BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 300, 300);
        MatrixToImageWriter.writeToPath(matrix,"jpg", Paths.get(path));
    }
}
