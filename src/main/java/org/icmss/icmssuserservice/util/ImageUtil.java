//package org.icmss.icmssuserservice.util;
//
//import org.example.exceptions.BusinessException;
//import org.springframework.http.HttpStatus;
//
//import java.io.ByteArrayOutputStream;
//import java.util.zip.Deflater;
//import java.util.zip.Inflater;
//
//public class ImageUtil {
//
//
//    private static final int BUFFER_SIZE = 4096;
//
//    public static byte[] compressImage(byte[] data) {
//
//        if (data == null) return null;
//
//        Deflater deflater = new Deflater();
//        deflater.setLevel(Deflater.BEST_COMPRESSION);
//        deflater.setInput(data);
//        deflater.finish();
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
//        byte[] tmp = new byte[BUFFER_SIZE];
//        while (!deflater.finished()) {
//            int size = deflater.deflate(tmp);
//            outputStream.write(tmp, 0, size);
//        }
//
//        try {
//            outputStream.close();
//        } catch (Exception e) {
//            throw new BusinessException(HttpStatus.CONFLICT, "Failed to compress image");
//        }
//
//        return outputStream.toByteArray();
//    }
//
//    public static byte[] decompressImage(byte[] data) {
//
//        if(data == null) return null;
//
//        Inflater inflater = new Inflater();
//        inflater.setInput(data);
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
//        byte[] tmp = new byte[BUFFER_SIZE];
//        try {
//            while (!inflater.finished()) {
//                int count = inflater.inflate(tmp);
//                outputStream.write(tmp, 0, count);
//            }
//            outputStream.close();
//        } catch (Exception exception) {
//            throw new BusinessException(HttpStatus.CONFLICT, "Failed to decompress image");
//        }
//        return outputStream.toByteArray();
//    }
//
//}
