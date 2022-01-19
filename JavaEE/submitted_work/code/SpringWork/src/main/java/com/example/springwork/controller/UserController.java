package com.example.springwork.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.springwork.status.LoginStatus;
import com.example.springwork.status.SignUpStatus;
import com.example.springwork.service.ClubService;
import com.example.springwork.service.UserService;
import com.example.springwork.entity.User;
import com.example.springwork.util.AesUtil;
import com.example.springwork.vo.TokenVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName: UserController
 * @Description: 与前端连接--学生
 * @Author: Tangt
 * @Date: 2021/12/9 22:10
 * @Version: v1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClubService clubService;


    @GetMapping("/login")
    public SaResult userLogin(@RequestParam("e_mail") String e_mail,
                              @RequestParam("password") String password) {
        System.out.println("-------------------");
//        System.out.println("token_Value="+StpUtil.getTokenValue());
        System.out.println("token_info="+StpUtil.getTokenInfo());
        System.out.println("--------------------");
        User loginUser = new User();
        loginUser.seteMail(e_mail);
        loginUser.setPassword(password);
        switch (userService.login(loginUser)) {
            case IncorrectPassword:
                System.out.println("incorrect");
                return SaResult.code(400).setData(LoginStatus.IncorrectPassword).setMsg("incorrectPassword");
            case Successful:
                System.out.println("-------------------");
                System.out.println(userService.getUserIdByEmail(e_mail));
                StpUtil.login(userService.getUserIdByEmail(e_mail));
                User user = userService.findByEMail(e_mail);
                System.out.println("User login "+e_mail+" "+password);
                System.out.println("token_info="+StpUtil.getTokenInfo());
                System.out.println("token value= "+StpUtil.getTokenValue());
                System.out.println("StpUtil.getLoginIdByToken(satoken)="+StpUtil.getLoginIdByToken(StpUtil.getTokenValue()));
                System.out.println("-------------------");
                TokenVo tokenVo=new TokenVo();
                tokenVo.setToken(StpUtil.getTokenInfo());
                tokenVo.setUser(user);
                return SaResult.code(200).setData(tokenVo).setMsg("successful");
            case NoUser:
                System.out.println("nouser");
                return SaResult.code(404).setData(LoginStatus.NoUser).setMsg("noUser");
        }
        System.out.println("unknown");
        return SaResult.code(400).setData(LoginStatus.UnknownStatus).setMsg("unknownStatus");
    }

    @GetMapping("/islogin")
    public SaResult isLogin() {
        System.out.println("-------------------");
        System.out.println("User check islogin");
        System.out.println(StpUtil.isLogin());
        System.out.println("Id="+StpUtil.getLoginId());
        System.out.println("-------------------");
        return SaResult.code(200).setData(StpUtil.isLogin());
    }

    @GetMapping("/logout")
    public SaResult logOut() {

        System.out.println("-------------------");
        System.out.println("User logout");
        System.out.println("Id= "+StpUtil.getLoginId());
        System.out.println(StpUtil.isLogin());
        System.out.println(StpUtil.getTokenInfo());
        System.out.println("-------------------");
        StpUtil.logout();
        return SaResult.code(200).setData("Successful");
    }

    @GetMapping("/all")
    public SaResult findAllUser() {
        Iterable<User> userList = userService.findAll();
        if (!userList.iterator().hasNext()) {
            return SaResult.code(400);
        } else {
            return SaResult.code(200).setData(userList);
        }
    }

    @GetMapping("/findbyid")
    public SaResult findUserById(@RequestParam("user_id") int user_id) {
        Optional<User> user = userService.findById(user_id);
        if (user == null || user.isEmpty()) {
            return SaResult.code(400);
        } else {
            return SaResult.code(200).setData(user);
        }
    }

    @GetMapping("/findbyname")
    public SaResult findUserByName(@RequestParam("name") String name) {
        List<User> userList = userService.findByName(name);
        if (userList == null || userList.isEmpty()) {
            return SaResult.code(400);
        } else {
            return SaResult.code(200).setData(userList);
        }
    }

    @PostMapping("/signup")
    public SaResult signUp(String headFile,
                           String name,
                           @RequestParam("password") String password,
                           @RequestParam("e_mail") String e_mail) {
        switch (userService.signUp(e_mail, password, name, headFile)) {
            case Successful:
                return SaResult.code(200).setData(SignUpStatus.Successful);
            case Email_Existed:
                return SaResult.code(400).setData(SignUpStatus.Email_Existed);
        }
        return SaResult.code(404);
    }

    @PostMapping("/changepassword")
    public SaResult changePassword(@RequestParam("user_id") int user_id,
                                   @RequestParam("password") String password,
                                   @RequestParam("code")String code,
                                   @RequestParam("encode")String encode) {
        try {
            if(AesUtil.encrypt(code).equals(encode))
                return SaResult.code(400).setData("Wrong code");
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean isSuccess = userService.changePassword(user_id, password);
        if (isSuccess)
            return SaResult.code(200).setData("Successful");
        else
            return SaResult.code(400).setData("Fail");
    }

    @PostMapping("/forgetpassword")
    public SaResult forgetPassword(@RequestParam("e_mail") String e_mail,
                                   @RequestParam("password") String password,
                                   @RequestParam("code")String code,
                                   @RequestParam("encode")String encode) {
        try {
            if(AesUtil.encrypt(code).equals(encode))
                return SaResult.code(400).setData("Wrong code");
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean isSuccess = userService.forgetPassword(e_mail, password);
        if (isSuccess)
            return SaResult.code(200).setData("Successful");
        else
            return SaResult.code(400).setData("Fail");
    }

    @PostMapping("/changeinfo")
    public SaResult changeInfo(@RequestParam("headFile") String headFile,
                               @RequestParam("name") String name) {
        Optional<User> user = userService.changeInfo(headFile, name);
        if (user == null || user.isEmpty())
            return SaResult.code(400).setData(false);
        else
            return SaResult.code(200).setData(user);
    }
}
