package com.example.springwork.service.impl;

import com.example.springwork.entity.*;
import com.example.springwork.repository.ApplicationRepo;
import com.example.springwork.repository.ClubRepo;
import com.example.springwork.repository.MemberRepo;
import com.example.springwork.repository.UserRepo;
import com.example.springwork.vo.ClubVo;
import com.example.springwork.vo.UserVo;
import com.example.springwork.service.ClubService;
import com.example.springwork.service.UploadService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName: ClubServiceImpl
 * @Description: 服务实现--社团
 * @Author: Tangt
 * @Date: 2021/12/10 23:10
 * @Version: v1.0
 */
@Service
public class ClubServiceImpl implements ClubService {

    @Resource
    private ClubRepo clubRepo;

    @Resource
    private MemberRepo memberRepo;

    @Resource
    private UserRepo userRepo;

    @Resource
    private ApplicationRepo applicationRepo;

    private UploadService uploadService = new UploadServiceImpl();

    @Override
    public Club findByName(String name) {
        return clubRepo.findByName(name);
    }

    @Override
    public Optional<Club> findById(int club_id) {
        return clubRepo.findById(club_id);
    }

    @Override
    public Iterable<Club> findAll() {
        return clubRepo.findAll();
    }

    @Override
    public List<UserVo> findMember(int club_id) {
//        Optional<Club> club=clubRepo.findById(club_id);
        List<Member> memberList = memberRepo.findAllByClubId(club_id);
        if (memberList.isEmpty())
            return null;
        List<UserVo> userList = new ArrayList<>();
        for (Member member : memberList) {
            UserVo userVo = new UserVo();
            userVo.setUser(userRepo.findById(member.getUserId()).get());
            userVo.setLevel(member.getLevel());
            userList.add(userVo);
        }
        return userList;
    }

    @Override
    public boolean deleteMember(int club_id, int user_id) {
        MemberPK memberPK = new MemberPK();
        memberPK.setUserId(user_id);
        memberPK.setClubId(club_id);
        Optional<Member> member = memberRepo.findById(memberPK);
        if (member.isEmpty()) {
            return false;
        } else {
            Optional<Club> club = clubRepo.findById(club_id);
            club.get().setMemberNumber(club.get().getMemberNumber() - 1);
            Member memberToDelete = new Member();
            memberToDelete.setClubId(member.get().getClubId());
            memberToDelete.setUserId(member.get().getUserId());
            memberRepo.delete(memberToDelete);
            return true;
        }
    }

    @Override
    public boolean addMember(int club_id, int user_id, int level, double club_cost) {
        MemberPK memberPK = new MemberPK();
        memberPK.setUserId(user_id);
        memberPK.setClubId(club_id);
        Optional<Member> member = memberRepo.findById(memberPK);
        if (!member.isEmpty()) {
            return false;
        } else {
            Optional<Club> club = clubRepo.findById(club_id);
            club.get().setMemberNumber(club.get().getMemberNumber() + 1);
            Member memberToSave = new Member();
            memberToSave.setClubId(club_id);
            memberToSave.setUserId(user_id);
            memberToSave.setLevel(level);
            memberToSave.setClubCost(club_cost);
            memberRepo.save(memberToSave);
            return true;
        }
    }

    @Override
    public boolean addClub(int user_id, String description, String phone_number, String name, String headFile, Double expenditure) {
        String logo = "http://112.124.59.163:82/group1/M00/00/00/cHw7o2G0ORaAcqfwAAA98UZbx38780.jpg";
        if (expenditure == null)
            expenditure = 0.0;
        if (description == null || description.isEmpty()) {
            description = "Nothing";
        }
        if (clubRepo.findByName(name) != null) {
            return false;
        }
        if(headFile.length()!=0)
            logo=headFile;
        Club newClub = new Club();
        newClub.setName(name);
        newClub.setDescription(description);
        newClub.setLogo(logo);
        newClub.setPhoneNumber(phone_number);
        newClub.setExpenditure(expenditure);
        newClub.setMemberNumber(1);
        clubRepo.save(newClub);
        newClub = clubRepo.findByName(name);

        //set user as club's owner
        int club_id = newClub.getClubId();
        Member Owner = new Member();
        Owner.setClubId(club_id);
        Owner.setUserId(user_id);
        Owner.setLevel(2);
        Owner.setClubCost(0.0);
        memberRepo.save(Owner);

        return true;
    }

    @Override
    public boolean changeMember(int user_id, int club_id, int level) {

        MemberPK memberPK = new MemberPK();
        memberPK.setUserId(user_id);
        memberPK.setClubId(club_id);
        Optional<Member> member = memberRepo.findById(memberPK);
        if (member == null || member.isEmpty()) {
            return false;
        }
        member.get().setLevel(level);
        memberRepo.save(member.get());
        return true;

    }

    @Override
    public boolean application(int user_id, int club_id) {
        ApplicationPK applicationPK = new ApplicationPK();
        applicationPK.setUserId(user_id);
        applicationPK.setClubId(club_id);
        MemberPK memberPK = new MemberPK();
        memberPK.setUserId(user_id);
        memberPK.setClubId(club_id);
        Optional<Application> application = applicationRepo.findById(applicationPK);
        Optional<Member> member = memberRepo.findById(memberPK);
        if (!application.isEmpty() || !member.isEmpty())
            return false;
        Application newApplication = new Application();
        newApplication.setClubId(club_id);
        newApplication.setUserId(user_id);
        applicationRepo.save(newApplication);
        return true;
    }

    @Override
    public List<User> findApplication(int club_id) {
        List<Application> applicationList = applicationRepo.findAllByClubId(club_id);
        if (applicationList == null || applicationList.isEmpty())
            return null;
        else {
            List<User> userList = new ArrayList<>();
            for (Application application : applicationList
            ) {
                userList.add(userRepo.findById(application.getUserId()).get());
            }
            return userList;
        }
    }


    @Override
    public boolean isInClub(int user_id, int club_id) {
        Optional<Club> club = clubRepo.findById(club_id);
        Optional<User> user = userRepo.findById(user_id);
        if (club == null || user == null || club.isEmpty() || user.isEmpty())
            return false;
        MemberPK memberPK = new MemberPK();
        memberPK.setClubId(club.get().getClubId());
        memberPK.setUserId(user.get().getUserId());
        Optional<Member> member = memberRepo.findById(memberPK);
        if (member == null || member.isEmpty())
            return false;
        else
            return true;
    }

    @Override
    public boolean deleteApplication(int user_id, int club_id) {
        ApplicationPK applicationPK = new ApplicationPK();
        applicationPK.setUserId(user_id);
        applicationPK.setClubId(club_id);
        applicationRepo.deleteById(applicationPK);
        return true;
    }

    @Override
    public boolean isInApplication(int user_id, int club_id) {
        ApplicationPK applicationPK = new ApplicationPK();
        applicationPK.setUserId(user_id);
        applicationPK.setClubId(club_id);
        Optional<Application> application = applicationRepo.findById(applicationPK);
        if (application == null || application.isEmpty())
            return false;
        else
            return true;
    }

    @Override
    public int findUserLevel(int user_id, int club_id) {
        MemberPK memberPK = new MemberPK();
        memberPK.setClubId(club_id);
        memberPK.setUserId(user_id);
        Optional<Member> member = memberRepo.findById(memberPK);
        return member.get().getLevel();
    }

    @Override
    public boolean deleteClub(int club_id) {
        Optional<Club> club = clubRepo.findById(club_id);
        if (club.isEmpty()) {
            return false;
        } else {
            clubRepo.delete(club.get());
            return true;
        }
    }

    @Override
    public List<ClubVo> findByUserId(int user_id) {
        List<Member> memberList = memberRepo.findAllByUserId(user_id);
        if (memberList.isEmpty())
            return null;
        List<ClubVo> clubVoList = new ArrayList<ClubVo>();
        for (Member member : memberList) {
            ClubVo clubVo = new ClubVo();
            clubVo.setClub(clubRepo.findById(member.getClubId()).get());
            clubVo.setLevel(member.getLevel());
            clubVoList.add(clubVo);
        }
        return clubVoList;
    }


}
