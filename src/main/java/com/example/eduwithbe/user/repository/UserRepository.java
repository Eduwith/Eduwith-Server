package com.example.eduwithbe.user.repository;

import com.example.eduwithbe.user.domain.UserEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository @Primary
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    //UserEntity findByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("update UserEntity u set u.name = :name, u.pwd = :pwd, u.address = :address where u.email = :email")
    void updateByUser(String email, String name, String pwd, String address);

    @Transactional
    @Modifying
    @Query("update UserEntity u set u.stamp = :stamp, u.point = :point, u.day = :day where u.email = :email")
    void updateByUserPoint(String email, int stamp, int point, int day);

    @Transactional
    @Modifying
    @Query("update UserEntity m set m.star = :star where m.email = :email")
    void updateStarByUser(String email, Float star);

}