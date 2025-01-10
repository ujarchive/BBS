package com.example.demo.Utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class NamingPolicy {
    /**
     * ZIP파일명
     * IGB00056_{YYYYMMDD}_{SEQ}(3).zip
     * IGB00056_20240327_001.zip
     *
     * 이미지 파일명
     * {계약(청약)번호}_{문서분류}(4)_{SEQ}(3).jpg
     * A230201000000_AA01_001.jpg
     * A230201000000_AA02_002.jpg
     * A230201000000_AA02_003.jpg
     * A230201000001_AA03_004.jpg
     *
     * A230201000000_AA01_001.jpg
     * A230201000000_AA02_001.jpg
     * A230201000000_AA02_002.jpg
     * A230201000001_AA03_001.jpg
     * (같은 ZIP파일 내에서 파일명이 겹치지만 않으면 됩니다.)
     */

    static String zipName = "IGB00056";
    public static String changeNaming(MultipartFile multipartFiles, String contractId, String requestDate, String documentType) throws IOException {
//        String uploadDirectory = "/rcv"; // 업로드할 디렉토리 경로
//        File[] uploadedFiles = new File[multipartFiles.length];

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmdd");
        String currentDate = dateFormat.format(new Date());

//        for(int i=0; i< multipartFiles.length; i++){
            String changeFileName = "";

//            MultipartFile multipartFile = multipartFiles[i];
            String originalFilename = multipartFiles.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // 파일 이름을 구성 (순번은 001부터 시작)
            if (fileExtension.equalsIgnoreCase(".zip")) {
                changeFileName = zipName + "_" + requestDate + "_" + String.format("%03d",1) + ".zip";
            } else if (fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".jpeg") || fileExtension.equalsIgnoreCase(".png")) {
                changeFileName = contractId + "_" + "AA00" + "_" + String.format("%03d", 1) + fileExtension;
            } else if (fileExtension.equalsIgnoreCase(".pdf")) {
                changeFileName = contractId + "_" + "AA00" + "_" + String.format("%03d", 1) + fileExtension;
            } else {
                // 지원하지 않는 파일 형식일 경우 예외 처리
                throw new IllegalArgumentException("Unsupported file format.");
            }

            // MultipartFile을 File로 변환하여 저장
//            File convertedFile = new File(filePath);
//            FileOutputStream fos = new FileOutputStream(convertedFile);
//            fos.write(multipartFile.getBytes());
//            fos.close();

            // 변환된 파일을 배열에 저장
//            uploadedFiles[i] = convertedFile;
//        }
        // 업로드된 파일 배열 반환
        return changeFileName;
    }
}
