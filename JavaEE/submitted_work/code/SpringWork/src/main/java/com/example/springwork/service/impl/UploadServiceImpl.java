package com.example.springwork.service.impl;

import com.example.springwork.service.UploadService;
import com.example.springwork.util.FastDFSUtil;
import org.csource.common.MyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ClassName: PictureServiceImpl
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2021/12/11 13:53
 * @Version: v1.0
 */
@Service
public class UploadServiceImpl implements UploadService {
    public String uploadPicture(MultipartFile file) throws IOException, MyException {
        String filePartName = FastDFSUtil.uploadFile(file);
        String filePath = "http://112.124.59.163:82/" + filePartName; // nginxHost 为服务器域名，注意跟前面设置http还是https访问保持一致
//        System.out.println("在服务器的文件名为：" + filePartName);
        return filePath;
    }
}
