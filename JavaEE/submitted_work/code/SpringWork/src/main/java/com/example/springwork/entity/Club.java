package com.example.springwork.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @ClassName: Club
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2021/12/11 14:31
 * @Version: v1.0
 */
@Entity
public class Club {
    private int clubId;
    private String logo;
    private String description;
    private String name;
    private Integer memberNumber;
    private Double expenditure;
    private String phoneNumber;

    @Id
    @Column(name = "club_id")
    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    @Basic
    @Column(name = "logo")
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "member_number")
    public Integer getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(Integer memberNumber) {
        this.memberNumber = memberNumber;
    }

    @Basic
    @Column(name = "expenditure")
    public Double getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(Double expenditure) {
        this.expenditure = expenditure;
    }

    @Basic
    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Club club = (Club) o;

        if (clubId != club.clubId) return false;
        if (logo != null ? !logo.equals(club.logo) : club.logo != null) return false;
        if (description != null ? !description.equals(club.description) : club.description != null) return false;
        if (name != null ? !name.equals(club.name) : club.name != null) return false;
        if (memberNumber != null ? !memberNumber.equals(club.memberNumber) : club.memberNumber != null) return false;
        if (expenditure != null ? !expenditure.equals(club.expenditure) : club.expenditure != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(club.phoneNumber) : club.phoneNumber != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = clubId;
        result = 31 * result + (logo != null ? logo.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (memberNumber != null ? memberNumber.hashCode() : 0);
        result = 31 * result + (expenditure != null ? expenditure.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        return result;
    }
}
