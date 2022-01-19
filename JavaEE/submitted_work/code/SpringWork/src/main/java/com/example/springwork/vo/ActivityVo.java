package com.example.springwork.vo;

import com.example.springwork.entity.Activity;

/**
 * @ClassName: ActivityVo
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2021/12/16 12:46
 * @Version: v1.0
 */
public class ActivityVo {
    private Activity activity;
    private int  level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
