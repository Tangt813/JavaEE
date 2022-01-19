package com.example.springwork.repository;

import com.example.springwork.entity.Activity;
import com.example.springwork.entity.Application;
import com.example.springwork.entity.ApplicationPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName: ApplicationRepo
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2021/12/13 23:53
 * @Version: v1.0
 */
@Repository
public interface ApplicationRepo extends CrudRepository<Application, ApplicationPK> {
    List<Application> findAllByClubId(int club_id);
}
