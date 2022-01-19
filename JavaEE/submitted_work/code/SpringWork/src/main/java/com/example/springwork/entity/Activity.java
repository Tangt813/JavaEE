package com.example.springwork.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @ClassName: Activity
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2021/12/15 19:01
 * @Version: v1.0
 */
@Entity
public class Activity {
    private int activityId;
    private String description;
    private Integer isPublic;
    private String date;
    private String name;
    private int clubId;
    private String picture;
    private Integer personNumber;

    @Id
    @Column(name = "activity_id")
    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
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
    @Column(name = "is_public")
    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    @Basic
    @Column(name = "date")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
    @Column(name = "club_id")
    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    @Basic
    @Column(name = "picture")
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Basic
    @Column(name = "person_number")
    public Integer getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(Integer personNumber) {
        this.personNumber = personNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Activity activity = (Activity) o;

        if (activityId != activity.activityId) return false;
        if (clubId != activity.clubId) return false;
        if (description != null ? !description.equals(activity.description) : activity.description != null)
            return false;
        if (isPublic != null ? !isPublic.equals(activity.isPublic) : activity.isPublic != null) return false;
        if (date != null ? !date.equals(activity.date) : activity.date != null) return false;
        if (name != null ? !name.equals(activity.name) : activity.name != null) return false;
        if (picture != null ? !picture.equals(activity.picture) : activity.picture != null) return false;
        if (personNumber != null ? !personNumber.equals(activity.personNumber) : activity.personNumber != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = activityId;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (isPublic != null ? isPublic.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + clubId;
        result = 31 * result + (picture != null ? picture.hashCode() : 0);
        result = 31 * result + (personNumber != null ? personNumber.hashCode() : 0);
        return result;
    }
}
