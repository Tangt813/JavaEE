package com.example.springwork.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @ClassName: MemberPK
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2021/12/10 22:22
 * @Version: v1.0
 */
public class MemberPK implements Serializable {
    private int userId;
    private int clubId;

    @Column(name = "user_id")
    @Id
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "club_id")
    @Id
    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemberPK memberPK = (MemberPK) o;

        if (userId != memberPK.userId) return false;
        if (clubId != memberPK.clubId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + clubId;
        return result;
    }
}
