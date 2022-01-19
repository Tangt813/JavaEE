package com.example.springwork.status;

/**
 * @ClassName: DeleteStatus
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2021/12/13 15:59
 * @Version: v1.0
 */
public enum DeleteStatus {
    Successful,//200
    User_Do_Not_Belong_To_Club,//400
    Activity_Not_Existed,
    Club_Not_Existed,
    User_Do_Not_Participate
}
