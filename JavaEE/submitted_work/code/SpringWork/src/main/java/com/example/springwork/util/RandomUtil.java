package com.example.springwork.util;

/**
 * @ClassName: RandomUtils
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2021/12/13 19:16
 * @Version: v1.0
 */
public class RandomUtil {
    //获取m~n范围内的整数
    public static int getRandom(int m,int n){
        int random=(int)(Math.random()*(n-m))+m;
        return random;
    }

    //获取位数为n的随机数
    public static int getRandom(int length){
        int m=getNumber(length);
        int n=m*10-1;
        int random=(int)(Math.random()*(n-m))+m;
        return random;
    }

    public static int getNumber(int n){
        if(n<1){
            n=1;
        }
        if(n==1){
            return 1;
        }else{
            n=n-1;
            return 10*getNumber(n);
        }
    }

//    public static void main(String[] args) {
//        System.out.println(getRandom(4));
//    }
}