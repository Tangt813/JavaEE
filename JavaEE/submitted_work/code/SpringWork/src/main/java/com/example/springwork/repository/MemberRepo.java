package com.example.springwork.repository;

import com.example.springwork.entity.Member;
import com.example.springwork.entity.MemberPK;
import com.example.springwork.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName: MemberRepo
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2021/12/10 23:31
 * @Version: v1.0
 */
public interface MemberRepo extends CrudRepository<Member, MemberPK> {


//    Member findById(MemberPK memberPK);


    List<Member> findAllByClubId(int club_id);

    List<Member> findAllByUserId(int user_id);
}
