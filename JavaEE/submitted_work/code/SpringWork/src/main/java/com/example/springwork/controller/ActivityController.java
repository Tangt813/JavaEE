package com.example.springwork.controller;

import cn.dev33.satoken.util.SaResult;
import com.example.springwork.entity.Club;
import com.example.springwork.status.AddStatus;
import com.example.springwork.status.DeleteStatus;
import com.example.springwork.service.ActivityService;
import com.example.springwork.service.ClubService;
import com.example.springwork.entity.Activity;
import com.example.springwork.entity.Member;
import com.example.springwork.vo.ActivityVo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName: ActivityController
 * @Description: 与前端连接--社团活动
 * @Author: Tangt
 * @Date: 2021/12/10 10:54
 * @Version: v1.0
 */
@Api(tags = "社团接口", hidden = true)
@CrossOrigin
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ClubService clubService;

    @Autowired
    private ActivityService activityService;

    @ApiOperation(value = "查询所有的活动", notes = "查询所有的活动")
    @ApiImplicitParams({}
    )
    @GetMapping("/all")
    public SaResult findAllActivity() {
        List<Activity> activityList=new ArrayList<Activity>();
        activityList = activityService.findAll();
        if (activityList==null||activityList.size()==0)
            return SaResult.code(400);
        else
            return SaResult.code(200).setData(activityList);
    }

    @ApiOperation(value = "获取活动所属社团", notes = "根据活动获取举办该活动的社团")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activity_id", value = "活动id", dataType = "int")
    }
    )
    @GetMapping("/getclub")
    public SaResult getClub(@RequestParam("activity_id") int activity_id) {
        Optional<Club> club = activityService.findClubById(activity_id);
        if (club != null) {
            return SaResult.code(200).setData(club);
        } else {
            return SaResult.code(400);
        }
    }

    @ApiOperation(value = "判断用户是否在社团内", notes = "根据用户id和活动id判断用户是否在举办该活动的社团内")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activity_id", value = "活动id", dataType = "int"),
            @ApiImplicitParam(name = "user_id", value = "用户id", dataType = "int")
    }
    )
    @GetMapping("/isinclub")
    public SaResult isInClub(@RequestParam("user_id") int user_id, @RequestParam("activity_id") int activity_id) {
        Optional<Member> member = activityService.isInClub(user_id, activity_id);
        if (member == null || member.isEmpty()) {
            return SaResult.code(200).setData(false);
        } else {
            return SaResult.code(200).setData(true);
        }
    }

    @ApiOperation(value = "获取符合名字要求的活动列表", notes = "根据名字获取相匹配的活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "活动name", dataType = "String")
    }
    )
    @GetMapping("/findbyname")
    public SaResult findByName(@RequestParam("name") String name) {
        List<Activity> activityList = activityService.findByName(name);
        if (activityList == null || activityList.isEmpty())
            return SaResult.code(404);
        else
            return SaResult.code(200).setData(activityList);
    }

    @ApiOperation(value = "用户参与活动", notes = "用户参与活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user_id", value = "用户id", dataType = "int"),
            @ApiImplicitParam(name = "activity_id", value = "活动id", dataType = "int")
    }
    )
    @PostMapping("participate")
    public SaResult participate(@RequestParam("user_id") int user_id,
                                @RequestParam("activity_id") int activity_id) {
        boolean isSuccess = activityService.participate(user_id, activity_id);
        if (isSuccess)
            return SaResult.code(200).setData(AddStatus.Successful);
        else {
            return SaResult.code(400).setData(AddStatus.Already_Existed_Or_Activity_Not_Public);
        }

    }

    @ApiOperation(value = "判断用户是否参与活动", notes = "判断用户是否参与了活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user_id", value = "用户id", dataType = "int"),
            @ApiImplicitParam(name = "activity_id", value = "活动id", dataType = "int")
    }
    )
    @GetMapping("isinactivity")
    public SaResult isInActivity(@RequestParam("user_id") int user_id,
                                 @RequestParam("activity_id") int activity_id) {
        boolean isSuccess = activityService.isInActivity(user_id, activity_id);
        if (isSuccess)
            return SaResult.code(200).setData(true);
        else
            return SaResult.code(200).setData(false);
    }

    @ApiOperation(value = "获取用户参与的活动", notes = "获取用户参与的活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user_id", value = "用户id", dataType = "int")
    }
    )
    @GetMapping("findbyuserid")
    public SaResult findByUserId(@RequestParam("user_id") int user_id) {
        List<ActivityVo> activityVoList = activityService.findByUserId(user_id);
        if (activityVoList == null || activityVoList.isEmpty())
            return SaResult.code(400);
        else
            return SaResult.code(200).setData(activityVoList);
    }

    @ApiOperation(value = "增加活动", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "club_id", value = "社团id", dataType = "int"),
            @ApiImplicitParam(name = "name", value = "活动name", dataType = "int"),
            @ApiImplicitParam(name = "date", value = "活动时间", dataType = "Date"),
            @ApiImplicitParam(name = "File", value = "活动的照片", dataType = "String"),
            @ApiImplicitParam(name = "isPublic", value = "是否对社团外开放", dataType = "Integer"),
            @ApiImplicitParam(name = "description", value = "活动描述", dataType = "String")

    }
    )
    @PostMapping("addactivity")
    public SaResult addActivity(String file,
                                String description,
                                Integer isPublic,
                                @RequestParam("club_id") int club_id,
                                @RequestParam("name") String name,
                                @RequestParam("date") String date
    ) {
        boolean isSuccess = activityService.addActivity(file, description, isPublic, club_id, name, date);

        if (isSuccess)
            return SaResult.code(200).setData(AddStatus.Successful);
        else
            return SaResult.code(400).setData(AddStatus.Already_Existed);
    }

    @PostMapping("deleteactivity")
    public SaResult deleteActivity(@RequestParam("activity_id") int activity_id) {
        boolean isSuccess = activityService.deleteActivity(activity_id);
        if (isSuccess)
            return SaResult.code(200).setData(DeleteStatus.Successful);
        else
            return SaResult.code(400).setData(DeleteStatus.Activity_Not_Existed);
    }

    @PostMapping("quitactivity")
    public SaResult quitActivity(@RequestParam("user_id")int user_id,
                                 @RequestParam("activity_id")int activity_id)
    {
        boolean isSuccess = activityService.quitActivity(user_id,activity_id);
        if (isSuccess)
            return SaResult.code(200).setData(DeleteStatus.Successful);
        else
            return SaResult.code(400).setData(DeleteStatus.User_Do_Not_Participate);
    }

    @GetMapping("findbyid")
    public SaResult findById(@RequestParam("activity_id")int activity_id)
    {
        Optional<Activity>activity=activityService.findById(activity_id);
        if(activity==null)
            return SaResult.code(400);
        else
            return SaResult.code(200).setData(activity);
    }

    @PostMapping("changeactivity")
    public SaResult changeActivity(String file,
                                String description,
                                Integer isPublic,
                                @RequestParam("activity_id")int activity_id,
                                @RequestParam("club_id") int club_id,
                                @RequestParam("name") String name,
                                @RequestParam("date") String date
    ) {
        boolean isSuccess = activityService.changeActivity(file, description, isPublic,activity_id, club_id, name, date);

        if (isSuccess)
            return SaResult.code(200).setData(AddStatus.Successful);
        else
            return SaResult.code(400).setData(AddStatus.Already_Existed);
    }

}
