package com.example.springwork.service;

import com.example.springwork.util.FastDFSUtil;
import org.csource.common.MyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ClassName: PictureService
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2021/12/11 13:52
 * @Version: v1.0
 */
@Service
public interface UploadService {
    String uploadPicture(MultipartFile file) throws IOException, MyException;
}
