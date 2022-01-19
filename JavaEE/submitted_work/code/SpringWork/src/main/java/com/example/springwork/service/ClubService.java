package com.example.springwork.service;

import com.example.springwork.entity.Club;
import com.example.springwork.entity.User;
import com.example.springwork.vo.ClubVo;
import com.example.springwork.vo.UserVo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public interface ClubService {

    Club findByName(String name);

    Optional<Club> findById(int club_id);

    Iterable<Club> findAll();

    List<UserVo> findMember(int club_id);

    boolean deleteMember(int club_id, int user_id);

    boolean addMember(int club_id,int user_id,int level,double club_cost);

    boolean addClub(int user_id,String description, String phone_number, String name, String  headFile,Double expenditure);

    boolean changeMember(int user_id,int club_id,int level);

    boolean application(int user_id,int club_id);

    List<User> findApplication(int club_id);

    boolean isInClub(int user_id,int club_id);

    boolean deleteApplication(int user_id,int club_id);

    boolean isInApplication(int user_id,int  club_id);

    int findUserLevel(int user_id,int club_id);

    boolean deleteClub(int club_id);

    List<ClubVo>findByUserId(int user_id);
}
