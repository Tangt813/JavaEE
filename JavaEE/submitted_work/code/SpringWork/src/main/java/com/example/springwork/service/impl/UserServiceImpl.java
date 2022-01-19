package com.example.springwork.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.example.springwork.repository.UserRepo;
import com.example.springwork.status.LoginStatus;
import com.example.springwork.status.SignUpStatus;
import com.example.springwork.service.UploadService;
import com.example.springwork.service.UserService;
import com.example.springwork.util.AesUtil;
import com.example.springwork.util.MD5Util;
import org.springframework.stereotype.Service;
import com.example.springwork.entity.User;


import javax.annotation.Resource;
import java.util.Optional;
import java.util.List;

/**
 * @ClassName: UserServiceImpl
 * @Description: 服务实现--学生
 * @Author: Tangt
 * @Date: 2021/12/10 23:15
 * @Version: v1.0
 */

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepo userRepo;


    private UploadService uploadService = new UploadServiceImpl();

    @Override
    public Optional<User> findById(int user_id) {
        return userRepo.findById(user_id);
    }

    @Override
    public Iterable<User> findAll() {

        return userRepo.findAll();
    }

    @Override
    public SignUpStatus signUp(String e_mail, String password, String name, String  file) {

        String head_portrait = "http://112.124.59.163:82/group1/M00/00/00/cHw7o2G0ORaAcqfwAAA98UZbx38780.jpg";
        User existUser = userRepo.findByeMail(e_mail);
        if (existUser != null) {
            return SignUpStatus.Email_Existed;
        }
        if (name == null) {
            name = "user";
        }
        if (file.length()!=0) {
            head_portrait=file;
        }
        User newUser = new User();
        newUser.setName(name);
        newUser.setRole("user");
        newUser.setHeadPortrait(head_portrait);
        newUser.seteMail(e_mail);
        try {
            String newpassword = AesUtil.decrypt(password);
            newpassword = MD5Util.MD5Encode(newpassword, "UTF-8");
            newUser.setPassword(newpassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        userRepo.save(newUser);
        return SignUpStatus.Successful;
    }

    @Override
    public LoginStatus login(User user) {
        User inUser = userRepo.findByeMail(user.geteMail());
        if (inUser == null)
            return LoginStatus.NoUser;
        try {
            System.out.println(inUser.getPassword());
            System.out.println(user.getPassword());
            System.out.println(AesUtil.decrypt(user.getPassword()));
            System.out.println(MD5Util.MD5Encode(AesUtil.decrypt(user.getPassword()),"UTF-8"));
            if (!(inUser.getPassword()).equals(MD5Util.MD5Encode(AesUtil.decrypt(user.getPassword()), "UTF-8")))
                return LoginStatus.IncorrectPassword;
        } catch (Exception e) {
            e.printStackTrace();
            return LoginStatus.IncorrectPassword;
        }
        return LoginStatus.Successful;

    }

    @Override
    public List<User> findByName(String name) {
        return userRepo.findByName(name);
    }

    @Override
    public int getUserIdByEmail(String e_mail) {
        User user = userRepo.findByeMail(e_mail);
        if (user == null)
            return -1;
        return user.getUserId();
    }

    @Override
    public boolean changePassword(int user_id, String password) {
        Optional<User> user = userRepo.findById(user_id);
        if (user == null || user.isEmpty()) {
            return false;
        } else {
            password = MD5Util.MD5Encode(password, "UTF-8");
            user.get().setPassword(password);
            userRepo.save(user.get());
            return true;
        }
    }

    @Override
    public boolean forgetPassword(String e_mail, String password) {
        User user = userRepo.findByeMail(e_mail);
        if (user == null) {
            return false;
        } else {
            password = MD5Util.MD5Encode(password, "UTF-8");
            user.setPassword(password);
            userRepo.save(user);
            return true;
        }
    }

    @Override
    public Optional<User> changeInfo(String headFile, String name) {
        System.out.println("satoken_info="+StpUtil.getTokenInfo());
        int user_id = StpUtil.getLoginIdAsInt();
        System.out.println(user_id);
        Optional<User> user = userRepo.findById(user_id);
        String head_portrait = "http://112.124.59.163:82/group1/M00/00/00/cHw7o2G0ORaAcqfwAAA98UZbx38780.jpg";
        ;
        if(headFile.length()!=0)
            head_portrait=headFile;
        user.get().setHeadPortrait(head_portrait);
        user.get().setName(name);
        userRepo.save(user.get());
        return user;
    }

    @Override
    public User findByEMail(String e_mail) {
        return userRepo.findByeMail(e_mail);
    }
}
