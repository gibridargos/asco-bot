package com.saidqosimov.taxinfobot.repository;

import com.saidqosimov.taxinfobot.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "select * from users ORDER BY id", nativeQuery = true)
    List<UserEntity> findAll();
    UserEntity findByChatId(Long chatId);
}
