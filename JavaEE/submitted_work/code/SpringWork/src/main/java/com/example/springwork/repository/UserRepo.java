package com.example.springwork.repository;

import com.example.springwork.entity.Member;
import com.example.springwork.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

/**
 * @ClassName: UserRepo
 * @Description: TODO
 * @Author:
 * @Date: 2021/12/10 17:05
 * @Version: v1.0
 */
@Repository
public interface UserRepo extends CrudRepository<User, Integer> {
    List<User> findByName(String name);

    User findByeMail(String e_mail);

//    User findByUserId(int user_id);


}
