package com.example.springwork.service;

import com.example.springwork.entity.User;
import com.example.springwork.status.LoginStatus;
import com.example.springwork.status.SignUpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    Optional<User> findById(int user_id);

    Iterable<User> findAll();

    SignUpStatus signUp(String e_mail,String password,String name, String file);

    LoginStatus login(User user);

    List<User> findByName(String name);

    int getUserIdByEmail(String e_mail);

    boolean changePassword(int user_id,String password);

    boolean forgetPassword(String e_mail,String password);

    Optional<User> changeInfo(String headFile,String name);

    User findByEMail(String e_mail);




}
