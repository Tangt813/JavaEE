package com.example.springwork.repository;

import com.example.springwork.entity.Activity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName: ActivityRepo
 * @Description: 与数据库连接--社团活动
 * @Author:
 * @Date: 2021/12/10 14:28
 * @Version: v1.0
 */
@Repository
public interface ActivityRepo extends CrudRepository<Activity,Integer> {

    @Query("select A from Activity A where A.name like %?1%")
    List<Activity> findByNameLike(String name);


    List<Activity> findAll();

}
