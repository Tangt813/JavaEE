package com.example.springwork.controller;

import com.example.springwork.util.AesUtil;
import com.example.springwork.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: Email
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2021/12/13 17:35
 * @Version: v1.0
 */
@RestController
@CrossOrigin
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("sendcode")
    private String send(@RequestParam("e_mail") String e_mail) {

        SimpleMailMessage message = new SimpleMailMessage();
        // 发件人
        message.setFrom("tang813201@163.com");
        // 收件人
        message.setTo(e_mail);
        // 邮件标题
        message.setSubject("验证码");
        // 邮件内容
        String text = String.valueOf(RandomUtil.getRandom(4));
        System.out.println(text);
        message.setText(text);
        // 抄送人
        message.setCc("tang813201@163.com");
        mailSender.send(message);
        try {
            text = AesUtil.encrypt(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    @GetMapping("sendmessage")
    private void sendMessage(@RequestParam("e_mail") String e_mail) {

        SimpleMailMessage message = new SimpleMailMessage();
        // 发件人
        message.setFrom("tang813201@163.com");
        // 收件人
        message.setTo(e_mail);
        // 邮件标题
        message.setSubject("用户创建成功");
        // 邮件内容

        message.setText("你已经成功创建用户！开始加入到多彩的社团活动吧~");
        // 抄送人
        message.setCc("tang813201@163.com");

        mailSender.send(message);
    }
}
