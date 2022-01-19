package com.example.springwork.service.impl;

import com.example.springwork.repository.*;
import com.example.springwork.service.ActivityService;
import com.example.springwork.entity.*;

import com.example.springwork.service.ClubService;
import com.example.springwork.service.UploadService;
import com.example.springwork.vo.ActivityVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName: ActivityServiceImpl
 * @Description: TODO
 * @Author:
 * @Date: 2021/12/10 14:33
 * @Version: v1.0
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private ActivityRepo activityRepo;

    @Resource
    private ClubRepo clubRepo;

    @Resource
    private MemberRepo memberRepo;

    @Resource
    private UserRepo userRepo;

    @Resource
    private ParticipateRepo participateRepo;

    private ClubService clubService = new ClubServiceImpl();


    private UploadService uploadService = new UploadServiceImpl();

    @Override
    public List<Activity> findByName(String name) {
        List<Activity> activityList = activityRepo.findByNameLike(name);
        if (activityList == null || activityList.isEmpty())
            return null;
        else
            return activityList;
    }

    @Override
    public Optional<Activity> findById(int activity_id) {
        Optional<Activity> activity = activityRepo.findById(activity_id);
        if (activity.isEmpty())
            return null;
        else
            return activity;

    }

    @Override
    public Optional<Club> findClubById(int activity_id) {
        Optional<Activity> activity = activityRepo.findById(activity_id);
        if (activity == null || activity.isEmpty()) {
            return null;
        } else {
            int club_id = activity.get().getClubId();
            Optional<Club> club = clubRepo.findById(club_id);
            if (club == null || club.isEmpty())
                return null;
            else
                return club;
        }
    }

    @Override
    public List<Activity> findAll() {
        return activityRepo.findAll();
    }

    @Override
    public Optional<Member> isInClub(int user_id, int activity_id) {
        Optional<Club> club = findClubById(activity_id);
        Optional<User> user = userRepo.findById(user_id);
        if (club == null || user == null || club.isEmpty() || user.isEmpty())
            return null;
        MemberPK memberPK = new MemberPK();
        memberPK.setClubId(club.get().getClubId());
        memberPK.setUserId(user.get().getUserId());
        return memberRepo.findById(memberPK);
    }

    @Override
    public boolean participate(int user_id, int activity_id) {
        //judgement existence
        ParticipatePK participate = new ParticipatePK();
        participate.setUserId(user_id);
        participate.setActivityId(activity_id);
        Optional<Participate> inParticipate = participateRepo.findById(participate);
        if (inParticipate == null || !inParticipate.isEmpty())
            return false;
        else {
            Optional<Activity> activity = activityRepo.findById(activity_id);
            if (activity.get().getIsPublic() == 0)//not public
            {
                int club_id = activity.get().getClubId();
                Optional<Club> club = clubRepo.findById(club_id);
                Optional<User> user = userRepo.findById(user_id);
                if (club.isEmpty() || user.isEmpty())
                    return false;
                MemberPK memberPK = new MemberPK();
                memberPK.setClubId(club.get().getClubId());
                memberPK.setUserId(user.get().getUserId());
                Optional<Member> member = memberRepo.findById(memberPK);
                if (member.isEmpty())
                    return false;
            }
            Participate newParticipate = new Participate();
            newParticipate.setActivityId(activity_id);
            newParticipate.setUserId(user_id);
            participateRepo.save(newParticipate);
            activity.get().setPersonNumber(activity.get().getPersonNumber() + 1);
            activityRepo.save(activity.get());
            return true;
        }

    }

    @Override
    public List<ActivityVo> findByUserId(int user_id) {

        List<Participate> participateList = participateRepo.findByUserId(user_id);
        if (participateList == null || participateList.isEmpty())
            return null;
        else {
            List<ActivityVo> activityVoList=new ArrayList<>();
            for (Participate participate : participateList
            ) {
                ActivityVo activityVo=new ActivityVo();
                int activity_id = participate.getActivityId();
                Optional<Activity> activity=activityRepo.findById(activity_id);
                activityVo.setActivity(activity.get());
                Optional<Member> userIsInClub=isInClub(user_id,activity_id);
                if(userIsInClub==null||userIsInClub.isEmpty())
                    activityVo.setLevel(-1);
                else
                {
                    activityVo.setLevel(userIsInClub.get().getLevel());
                }
                activityVoList.add(activityVo);
            }
            return activityVoList;
        }
    }

    @Override
    public boolean addActivity(String file, String description, Integer isPublic, int club_id, String name,String   date) {

        String picture = "http://112.124.59.163:82/group1/M00/00/00/cHw7o2G0ORaAcqfwAAA98UZbx38780.jpg";
        if (description == null || description.isEmpty()) {
            description = "Nothing";
        }
        if (isPublic == null) {
            isPublic = 1;
        }
        if (file.length()!=0) {
            picture=file;
        }
        Activity newActivity = new Activity();
        newActivity.setName(name);
        newActivity.setDescription(description);
        newActivity.setPicture(picture);
        newActivity.setIsPublic(isPublic);
        newActivity.setClubId(club_id);
        newActivity.setDate(date);
        newActivity.setPersonNumber(0);
        activityRepo.save(newActivity);
        return true;
    }

    @Override
    public boolean changeActivity(String file, String description, Integer isPublic, int activity_id, int club_id, String name, String date) {
        Optional<Activity> activity = activityRepo.findById(activity_id);
        if(activity==null||activity.isEmpty())
            return false;
        else
        {
            String picture = "http://112.124.59.163:82/group1/M00/00/00/cHw7o2G0ORaAcqfwAAA98UZbx38780.jpg";
            if (description == null || description.isEmpty()) {
                description = "Nothing";
            }
            if (isPublic == null) {
                isPublic = 1;
            }
            if (file.length()!=0) {
                picture=file;
            }
            activity.get().setName(name);
            activity.get().setDescription(description);
            activity.get().setPicture(picture);
            activity.get().setIsPublic(isPublic);
            activity.get().setClubId(club_id);
            activity.get().setDate(date);
            activityRepo.save(activity.get());
            return true;
        }
    }

    @Override
    public boolean deleteActivity(int activity_id) {
        Optional<Activity> activity = activityRepo.findById(activity_id);
        if (activity == null || activity.isEmpty())
            return false;
        else {
            activityRepo.delete(activity.get());
            return true;
        }

    }

    @Override
    public boolean isInActivity(int user_id, int activity_id) {
        ParticipatePK participatePK = new ParticipatePK();
        participatePK.setActivityId(activity_id);
        participatePK.setUserId(user_id);
        Optional<Participate> participate = participateRepo.findById(participatePK);
        if (participate==null|| participate.isEmpty())
            return false;
        else
            return true;
    }

    @Override
    public boolean quitActivity(int user_id, int activity_id) {
        ParticipatePK participatePK = new ParticipatePK();
        participatePK.setActivityId(activity_id);
        participatePK.setUserId(user_id);
        Optional<Participate> participate = participateRepo.findById(participatePK);
        if (participate.isEmpty())
            return false;
        else
        {
            participateRepo.delete(participate.get());
            return true;
        }

    }

}
