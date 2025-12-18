package com.saidqosimov.taxinfobot.repository;


import com.saidqosimov.taxinfobot.entity.TaxInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaxInfoRepository extends JpaRepository<TaxInfoEntity, Long> {
    @Query(value = "select * from tax_info ORDER BY id", nativeQuery = true)
    List<TaxInfoEntity> findAllTaxInfo();
    List<TaxInfoEntity> findAllByTaxType(String typeTax);
}
