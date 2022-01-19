package com.example.springwork.vo;

import com.example.springwork.entity.Club;
import com.example.springwork.entity.User;

/**
 * @ClassName: ClubVo
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2021/12/15 10:32
 * @Version: v1.0
 */
public class ClubVo {
    private Club club;
    private int level;

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
