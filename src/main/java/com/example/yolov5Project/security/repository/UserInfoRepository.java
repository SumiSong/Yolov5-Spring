package com.example.yolov5Project.security.repository;

import com.example.yolov5Project.security.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAll();
    Optional<UserEntity> findByUserId(String userId);

}
