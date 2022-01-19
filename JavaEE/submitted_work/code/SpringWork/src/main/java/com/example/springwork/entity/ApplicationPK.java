package com.example.springwork.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @ClassName: ApplicationPK
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2021/12/13 23:52
 * @Version: v1.0
 */
public class ApplicationPK implements Serializable {
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

        ApplicationPK that = (ApplicationPK) o;

        if (userId != that.userId) return false;
        if (clubId != that.clubId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + clubId;
        return result;
    }
}
