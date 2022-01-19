package com.example.springwork.repository;

import com.example.springwork.entity.Club;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *@ClassName: ClubRepo
 *@Description: 与数据库连接--社团
 *@Author:  Tangt
 *@Date: 2021/12/9 22:12
 *@Version: v1.0
 */
@Repository
public interface ClubRepo extends CrudRepository<Club,Integer> {

    Club findByName(String name);



}
