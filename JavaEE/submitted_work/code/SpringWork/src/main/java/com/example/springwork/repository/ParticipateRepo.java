package com.example.springwork.repository;

import com.example.springwork.entity.Activity;
import com.example.springwork.entity.Participate;
import com.example.springwork.entity.ParticipatePK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName: ParticipateRepo
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2021/12/13 19:31
 * @Version: v1.0
 */
@Repository
public interface ParticipateRepo extends CrudRepository<Participate, ParticipatePK> {

    List<Participate> findByUserId(int user_id);
}
