package com.example.springwork.controller;

import cn.dev33.satoken.util.SaResult;
import com.example.springwork.entity.Club;
import com.example.springwork.entity.User;
import com.example.springwork.service.UserService;
import com.example.springwork.vo.ClubVo;
import com.example.springwork.vo.UserVo;
import com.example.springwork.status.AddStatus;
import com.example.springwork.status.DeleteStatus;
import com.example.springwork.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName: ClubController
 * @Description: 与前端连接--社团
 * @Author: Tangt
 * @Date: 2021/12/10 10:20
 * @Version: v1.0
 */
@RestController
@CrossOrigin
@RequestMapping("/club")
public class ClubController {

    @Autowired
    private ClubService clubService;

    @Autowired
    private UserService userService;


    @GetMapping("/all")
    public SaResult findAllClub() {
        Iterable<Club> clubList;
        clubList = clubService.findAll();
        if (!clubList.iterator().hasNext())
            return SaResult.code(400);
        else
            return SaResult.code(200).setData(clubList);
    }

    @GetMapping("/findbyname")
    public SaResult findByName(@RequestParam("name") String name) {
        Club club = clubService.findByName(name);
        if (club == null)
            return SaResult.code(400);
        else
            return SaResult.code(200).setData(club);
    }

    @GetMapping("/findbyid")
    public SaResult findById(@RequestParam("club_id") int club_id) {
        Optional<Club> club = clubService.findById(club_id);
        if (club == null || club.isEmpty())
            return SaResult.code(400);
        else
            return SaResult.code(200).setData(club);
    }

    @GetMapping("finduserlevel")
    public SaResult findUserById(@RequestParam("user_id") int user_id,
                                 @RequestParam("club_id") int club_id) {
        int level = clubService.findUserLevel(user_id, club_id);
        return SaResult.code(200).setData(level);
    }

    @GetMapping("/findmember")
    public SaResult findMember(@RequestParam("club_id") int club_id) {
        List<UserVo> userList = clubService.findMember(club_id);
        if (userList == null || userList.isEmpty()) {
            return SaResult.code(400);
        } else {
            return SaResult.code(200).setData(userList);
        }
    }

    @PostMapping("/deletemember")
    public SaResult deleteMember(@RequestParam("club_id") int club_id,
                                 @RequestParam("user_id") int user_id) {
        boolean isSuccess = clubService.deleteMember(club_id, user_id);
        if (isSuccess)
            return SaResult.code(200).setData(DeleteStatus.Successful);
        else
            return SaResult.code(400).setData(DeleteStatus.User_Do_Not_Belong_To_Club);
    }

    @PostMapping("/addmember")
    public SaResult addMember(@RequestParam("club_id") int club_id,
                              @RequestParam("user_id") int user_id,
                              @RequestParam("level") int level,
                              @RequestParam("club_cost") double club_cost) {
        boolean isSuccess = clubService.addMember(club_id, user_id, level, club_cost);
        if (isSuccess) {
            return SaResult.code(200).setData(AddStatus.Successful);
        } else {
            return SaResult.code(400).setData(AddStatus.Already_Existed);
        }
    }

    @PostMapping("/addclub")
    public SaResult addClub(String headFile,
                            String description,
                            Double expenditure,
                            @RequestParam("user_id") int user_id,
                            @RequestParam("name") String name,
                            @RequestParam("phone_number") String phone_number) {
        boolean isSuccess = clubService.addClub(user_id, description, phone_number, name, headFile, expenditure);

        if (isSuccess)
            return SaResult.code(200).setData(AddStatus.Successful);
        else
            return SaResult.code(400).setData(AddStatus.Already_Existed);
    }

    @PostMapping("deleteclub")
    public SaResult deleteClub(@RequestParam("club_id") int club_id) {
        boolean isSuccess = clubService.deleteClub(club_id);
        if (isSuccess)
            return SaResult.code(200).setData(DeleteStatus.Successful);
        else
            return SaResult.code(400).setData(DeleteStatus.Club_Not_Existed);
    }

    @PostMapping("/changemember")
    public SaResult changeMember(@RequestParam("user_id") int user_id,
                                 @RequestParam("club_id") int club_id,
                                 @RequestParam("level") int level) {
        boolean isSuccess = clubService.changeMember(user_id, club_id, level);
        if (isSuccess)
            return SaResult.code(200).setData(true);
        else
            return SaResult.code(400).setData(false);
    }

    @PostMapping("/application")
    public SaResult application(@RequestParam("user_id") int user_id,
                                @RequestParam("club_id") int club_id) {
        boolean isSuccess = clubService.application(user_id, club_id);
        if (isSuccess)
            return SaResult.code(200).setData(AddStatus.Successful);
        else
            return SaResult.code(400).setData(AddStatus.Already_Existed);
    }

    @PostMapping("deleteapplication")
    public SaResult deleteApplication(@RequestParam("user_id") int user_id,
                                      @RequestParam("club_id") int club_id,
                                      @RequestParam("is_success") boolean isSuccess) {
        boolean isDeleteSuccess = clubService.deleteApplication(user_id, club_id);
        if (isSuccess) {
            double club_cost = clubService.findById(club_id).get().getExpenditure();

            boolean isAddSuccess = clubService.addMember(club_id, user_id, 0, club_cost);
            if (isAddSuccess && isDeleteSuccess) {
                return SaResult.code(200).setData(true);
            } else {
                return SaResult.code(400).setData(false);
            }
        } else {
            if (isDeleteSuccess) {
                return SaResult.code(200).setData(true);
            } else {
                return SaResult.code(400).setData(false);
            }
        }
    }

    @GetMapping("findapplication")
    public SaResult findApplication(@RequestParam("club_id") int club_id) {
        List<User> userList = clubService.findApplication(club_id);
        if (userList == null || userList.isEmpty())
            return SaResult.code(400);
        else
            return SaResult.code(200).setData(userList);
    }

    @GetMapping("isinapplication")
    public SaResult isInApplication(@RequestParam("user_id") int user_id,
                                    @RequestParam("club_id") int club_id) {
        boolean isSuccess = clubService.isInApplication(user_id, club_id);
        if (isSuccess) {
            return SaResult.code(200).setData(true);

        } else {
            return SaResult.code(200).setData(false);
        }
    }

    @GetMapping("isinclub")
    public SaResult isInClub(@RequestParam("user_id") int user_id,
                             @RequestParam("club_id") int club_id) {
        boolean isSuccess = clubService.isInClub(user_id, club_id);
        if (isSuccess) {
            return SaResult.code(200).setData(true);


        } else {
            return SaResult.code(200).setData(false);
        }
    }

    @GetMapping("findbyuserid")
    public SaResult findByUserId(@RequestParam("user_id")int user_id)
    {
        List<ClubVo> clubList=clubService.findByUserId(user_id);
        if(clubList==null||clubList.isEmpty())
        {
            return SaResult.code(400);
        }
        else
            return SaResult.code(200).setData(clubList);
    }

}
