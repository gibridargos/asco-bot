package com.saidqosimov.taxinfobot.repository;


import com.saidqosimov.taxinfobot.entity.InfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfoRepository extends JpaRepository<InfoEntity, Integer> {
}
