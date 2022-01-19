package com.example.springwork.status;

/**
 * @ClassName: AddStatus
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2021/12/13 16:20
 * @Version: v1.0
 */
public enum AddStatus {
    Successful,//200
    Already_Existed,//400
    Worry_PK,//400
    Already_Existed_Or_Activity_Not_Public//400
}
