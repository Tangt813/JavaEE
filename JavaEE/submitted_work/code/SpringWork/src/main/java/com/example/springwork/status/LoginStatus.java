package com.example.springwork.status;

/**
 * @ClassName: LoginStatus
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2021/12/11 15:05
 * @Version: v1.0
 */
public enum LoginStatus {
    IncorrectPassword,//400
    Successful,//200
    NoUser,//404
    UnknownStatus//500
}
