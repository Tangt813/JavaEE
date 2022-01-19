package com.example.springwork.service;

import com.example.springwork.entity.Activity;
import com.example.springwork.entity.Club;
import com.example.springwork.entity.Member;
import com.example.springwork.vo.ActivityVo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName: ActivityService
 * @Description: TODO
 * @Author:
 * @Date: 2021/12/10 14:31
 * @Version: v1.0
 */
@Service
public interface ActivityService {

    List<Activity> findByName(String name);

    Optional<Activity> findById(int activity_id);

    Optional<Club> findClubById(int activity_id);

    List<Activity> findAll();

    Optional<Member> isInClub(int user_id, int activity_id);

    boolean participate(int user_id, int activity_id);

    List<ActivityVo> findByUserId(int user_id);

    boolean addActivity(String file, String description, Integer isPublic, int club_id, String name, String date);

    boolean changeActivity(String file, String description, Integer isPublic,int activity_id, int club_id, String name, String date);

    boolean deleteActivity(int activity_id);

    boolean isInActivity(int user_id, int activity_id);

    boolean quitActivity(int user_id,int activity_id);
}
