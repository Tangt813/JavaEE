package com.example.springwork.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @ClassName: ParticipatePK
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2021/12/13 19:30
 * @Version: v1.0
 */
public class ParticipatePK implements Serializable {
    private int userId;
    private int activityId;

    @Column(name = "user_id")
    @Id
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "activity_id")
    @Id
    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParticipatePK that = (ParticipatePK) o;

        if (userId != that.userId) return false;
        if (activityId != that.activityId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + activityId;
        return result;
    }
}
