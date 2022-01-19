package com.example.springwork.util;


import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
public class FastDFSUtil {
    private static StorageClient1 client1;
    static {
        try {
            ClientGlobal.initByProperties("fastdfs-client.properties");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            client1 = new StorageClient1(trackerServer, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 上传文件
     */
    public static String uploadFile(MultipartFile file) throws IOException, MyException {
        String fileName = file.getOriginalFilename();
        //返回上传到服务器的路径
        return client1.upload_file1(file.getBytes(), fileName.substring(fileName.lastIndexOf(".") + 1), null);
    }

    /**
     * 下载文件
     */
    public static byte[] downloadFile(String fileId) throws IOException, MyException {
        return client1.download_file1(fileId);
    }
    //测试
    public static String uploadFile(String localFilePath) throws IOException, MyException {
        return client1.upload_file1(localFilePath, localFilePath.substring(localFilePath.lastIndexOf(".") + 1), null);
    }
    /**
     * 获取访问文件的令牌，有全局异常处理（开启防盗链的情况下）
     */
    public static String getToken(String fileId) throws UnsupportedEncodingException, NoSuchAlgorithmException, MyException {
        int ts = (int) Instant.now().getEpochSecond();
        String subStr = fileId.substring(7); // 注意，这个地址里边不包含 group，千万别搞错了
        String token = ProtoCommon.getToken(subStr, ts, "FastDFS1234567890"); // FastDFS1234567890 是前面配置的参数 fastdfs.http_secret_key 的值
        StringBuilder sb = new StringBuilder();
        String IP = "http(s)://xx.xx.xx.x/"; //服务器外网ip，括号中的 s 表示按照前面自己的配置来决定是http还是https访问
        sb.append(IP);
        sb.append(fileId);
        sb.append("?token=").append(token);
        sb.append("&ts=").append(ts);
        return sb.toString();
    }
}

