package com.easynetworks.lotteFactoring.Utils;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


@Slf4j
public class SFTPUtils {

    /*
     *  송신(이지네트웍스->롯데카드) 디렉토리 : /rcv
        수신(롯데카드->이지네트웍스) 디렉토리 : /snd
     */
    private static final String PROD_IP = "124.243.96.113";
    private static final String PROD_USERNAME = "eznet";
    private static final String PROD_PASSWORD = "eznet@r";

//    private static final String DEV_IP = "124.243.96.62";
//    private static final String DEV_USERNAME = "eznet";
//    private static final String DEV_PASSWORD = "eznet@t";

    private static final String LOCAL_IP = "127.0.0.1";
    private static final String LOCAL_USERNAME = "easynt";
    private static final String LOCAL_PASSWORD = "easynt!@#";

    private static final int PORT = 22;

    private Session session = null;
    private Channel channel = null;
    private ChannelSftp channelSftp = null;

    public static Session getSFTPConnection() throws Exception {
        JSch jsch = new JSch();
        Session session  = null;

        try {
            session  = jsch.getSession(LOCAL_USERNAME, LOCAL_IP, 22);
            session.setPassword(LOCAL_PASSWORD);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return session;
    }

    public static Session getLotteSFTPConnection() throws Exception {
        JSch jsch = new JSch();
        Session session  = null;

        try {
            session  = jsch.getSession(PROD_USERNAME, PROD_IP, 22);
            session.setPassword(PROD_PASSWORD);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return session;
    }

    //디렉토리 생성
    public static void mkdir(ChannelSftp channelSftp, String path, String dir, String mkdirName) {
        try {
            channelSftp.cd(path);
            if (!exists(channelSftp, dir)) {
                channelSftp.mkdir(mkdirName);
            }
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    //디렉토리 존재
    public static boolean exists(ChannelSftp channelSftp, String uploadPath) {
        Vector res = null;
        try {
            res = channelSftp.ls(uploadPath);
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                return false;
            }
        }
        return res != null && !res.isEmpty();
    }

    //파일 삭제
    public static void delete(ChannelSftp channelSftp, String path, String dir, String mkdirName) {
        try {
            channelSftp.cd(path);
            if (exists(channelSftp, dir)) {
                channelSftp.rm(dir);
            }
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    //파일업로드
    public static boolean upload(ChannelSftp channelSftp, String path, MultipartFile file, String dirName) {
        //log.info("fileOriginName: "+file.getOriginalFilename()); //API_롯데카드-장기월납서비스_제휴사연동규격서[API] v1.5.pdf
        boolean isUpload = false;

        try (InputStream in = file.getInputStream()) {
//            String renamedFileName = NamingPolicy.changeNaming(file, contractId, file.getContentType()); // CONTRACTID_application/pdf_001.pdf
            SFTPUtils.mkdir(channelSftp, path, dirName, dirName);
            channelSftp.cd(path+"/"+dirName); //파일 저장경로로 이동  => cd /home/easynt/rcv/
            log.info(path+"/"+dirName);
            //중복체크 코드 입력
            channelSftp.put(in, file.getOriginalFilename()); //파일이름을 변경하여 put
            channelSftp.chmod(444, file.getOriginalFilename());
            // 업로드 확인
            if (exists(channelSftp, path + "/" + dirName + "/" + file.getOriginalFilename())) {
                isUpload = true;
            }
        } catch (IOException | SftpException e) {
            e.printStackTrace();
        }
        return isUpload;
    }

    public static boolean uploadToLotte(ChannelSftp channelSftp, String path, MultipartFile file, String dirName) {
        boolean isUpload = false;

        try (InputStream in = file.getInputStream()) {
            channelSftp.cd(path); // cd ./rcv
            //중복체크 코드입력
            channelSftp.put(in, file.getOriginalFilename()); //파일이름을 변경하여 put
            //channelSftp.chmod(444, file.getOriginalFilename());
            // 업로드 확인
            if (exists(channelSftp, path + "/" + file.getOriginalFilename())) {
                isUpload = true;
            }
        } catch (IOException | SftpException e) {
            e.printStackTrace();
        }
        return isUpload;
    }

    //파일 다운로드
    public static boolean download(ChannelSftp channelSftp, String remoteFilePath, String fileName, String localFilePath) {
        boolean isDownload = false;
        String fullLocalPath = localFilePath + File.separator + fileName;
        String fullRemotePath = remoteFilePath + "/" + fileName;
        try (OutputStream out = new FileOutputStream(fullLocalPath)) {
            log.info("Starting download from " + fullRemotePath + " to " + fullLocalPath);
            channelSftp.get(fullRemotePath, out);
            isDownload = true;
            log.info("Download complete: " + fullLocalPath);
        } catch (SftpException | IOException e) {
            log.error("Download error: " + fullRemotePath + " to " + fullLocalPath, e);
        }
        return isDownload;
    }

    //파일 압축하기
    public static boolean zip(List<File> files, String zipFileName){
//        ZipUtil.zipFiles(files, "path/to/output.zip");
        try (FileOutputStream fos = new FileOutputStream(zipFileName);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            for (File fileToZip : files) {
                try (FileInputStream fis = new FileInputStream(fileToZip)) {
                    ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                    zipOut.putNextEntry(zipEntry);

                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes);
                    }
                    return true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     *
     * @param zipFilePath: 압축 해제할 ZIP 파일의 경로
     * @param destDirectory: 압축을 풀어 파일을 저장할 대상 디렉토리
     * @throws IOException
     */
    public static void unzip(ChannelSftp channelSftp, String zipFilePath, String destDirectory, String dirName,String originName) throws IOException {
        log.info("zipFilePath: " + zipFilePath); //zipFilePath: /home/youjin/rcv/CONTRACTID02_20240529
        log.info("destDirectory: " + destDirectory); //destDirectory: /home/youjin/rcv/CONTRACTID02_20240529
        log.info("fileName: " + originName);
        log.info("zipFilePath+\"/\"+originName :" + zipFilePath+"/"+originName);

        Path destDir = Paths.get(destDirectory); log.info("destDir: " + destDir);
        if (!exists(channelSftp, destDirectory)) {
            mkdir(channelSftp, zipFilePath, dirName, dirName);
        }
        try (ZipFile zipFile = new ZipFile(zipFilePath+"/"+originName)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                Path entryDestination = destDir.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(entryDestination);
                } else {
                    Files.createDirectories(entryDestination.getParent());
                    try (InputStream in = zipFile.getInputStream(entry);
                         FileOutputStream out = new FileOutputStream(entryDestination.toFile())) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = in.read(buffer)) >= 0) {
                            out.write(buffer, 0, length);
                        }
                    }
                }
            }
        }
    }

    //파일 확장자 검사(zip, jpg, jpeg만 가능)
    public static String checkFileExtension(MultipartFile file){
        if (file == null) {
            return "400";
        }else if(Objects.equals(file.getContentType(), "application/zip")){
            return "01";
        }else if(Objects.equals(file.getContentType(), "image/jpeg")){
            return "02";
        }else{
            return "400";
        }
    }

    //연결종료
    public void disconnection() {
        channelSftp.quit();
        session.disconnect();
    }

    public static void disconnection(ChannelSftp channelSftp, Session session) {
        channelSftp.quit();
        session.disconnect();
    }

}
