package com.example.springwork.entity;

import javax.persistence.*;

/**
 * @ClassName: Member
 * @Description: TODO
 * @Author:
 * @Date: 2021/12/10 22:22
 * @Version: v1.0
 */
@Entity
@IdClass(MemberPK.class)
public class Member {
    private int userId;
    private int clubId;
    private Integer level;
    private Double clubCost;

    @Id
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "club_id")
    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    @Basic
    @Column(name = "level")
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Basic
    @Column(name = "club_cost")
    public Double getClubCost() {
        return clubCost;
    }

    public void setClubCost(Double clubCost) {
        this.clubCost = clubCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        if (userId != member.userId) return false;
        if (clubId != member.clubId) return false;
        if (level != null ? !level.equals(member.level) : member.level != null) return false;
        if (clubCost != null ? !clubCost.equals(member.clubCost) : member.clubCost != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + clubId;
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (clubCost != null ? clubCost.hashCode() : 0);
        return result;
    }
}
