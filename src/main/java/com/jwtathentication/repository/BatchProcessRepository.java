package com.jwtathentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwtathentication.entity.UserData;

@Repository
public interface BatchProcessRepository extends JpaRepository<UserData, Long>{

}
