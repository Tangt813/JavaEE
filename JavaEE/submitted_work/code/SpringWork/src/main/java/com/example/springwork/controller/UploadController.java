package com.example.springwork.controller;

import cn.dev33.satoken.util.SaResult;
import com.example.springwork.service.UploadService;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ClassName: UploadController
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2021/12/15 0:41
 * @Version: v1.0
 */
@RestController
@CrossOrigin
public class UploadController {

    @Autowired
    UploadService uploadService;

    @PostMapping("upload")
    public SaResult upload(MultipartFile file)
    {
        try {
            return SaResult.code(200).setData(uploadService.uploadPicture(file));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return SaResult.code(400);
    }
}
