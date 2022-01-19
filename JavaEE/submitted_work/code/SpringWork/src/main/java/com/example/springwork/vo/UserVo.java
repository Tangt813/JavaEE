package com.example.springwork.vo;

import com.example.springwork.entity.User;

public class UserVo {
    private User user;
    private int level;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
