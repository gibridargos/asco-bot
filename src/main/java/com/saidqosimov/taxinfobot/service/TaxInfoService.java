package com.saidqosimov.taxinfobot.service;

import com.saidqosimov.taxinfobot.domain.TaxInfoDTO;
import com.saidqosimov.taxinfobot.entity.TaxInfoEntity;
import com.saidqosimov.taxinfobot.repository.TaxInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaxInfoService {
    private final TaxInfoRepository taxInfoRepository;

    public synchronized List<String> getTaxTypes() {
        List<TaxInfoEntity> allTaxInfo = new LinkedList<>(taxInfoRepository.findAllTaxInfo());
        Set<String> taxTypes = new LinkedHashSet<>();
        for (TaxInfoEntity taxInfoEntity : allTaxInfo) {
            taxTypes.add(taxInfoEntity.getTaxType());
        }
        return new LinkedList<>(taxTypes);
    }

    public synchronized List<TaxInfoDTO> getAllTaxes() {
        List<TaxInfoDTO> taxInfoDTOS = new LinkedList<>();
        for (TaxInfoEntity taxInfo : taxInfoRepository.findAll()) {
            TaxInfoDTO taxInfoDTO = TaxInfoDTO.builder()
                    .id(taxInfo.getId())
                    .taxType(taxInfo.getTaxType())
                    .reportName(taxInfo.getReportName())
                    .reportingPeriod(taxInfo.getReportingPeriod())
                    .reportDate(taxInfo.getReportDate())
                    .reportComment(taxInfo.getReportComment())
                    .taxName(taxInfo.getTaxName())
                    .paymentPeriod(taxInfo.getPaymentPeriod())
                    .taxDate(taxInfo.getTaxDate())
                    .taxComment(taxInfo.getTaxComment())
                    .deleted(taxInfo.isDeleted())
                    .build();
            taxInfoDTOS.add(taxInfoDTO);
        }
        return taxInfoDTOS;
    }

    public synchronized List<TaxInfoEntity> getTaxInfoByType(String typeTax) {
        return taxInfoRepository.findAllByTaxType(typeTax);
    }

/*    public TaxInfoEntity setTax(TaxInfoDTO taxInfoDTO) {
        TaxInfoEntity taxInfoEntity = TaxInfoEntity.builder()
                .taxType(taxInfoDTO.getTaxType())
                .reportName(taxInfoDTO.getReportName())
                .reportingPeriod(taxInfoDTO.getReportingPeriod())
                .reportDate(taxInfoDTO.getReportDate())
                .reportComment(taxInfoDTO.getReportComment())
                .taxName(taxInfoDTO.getTaxName())
                .paymentPeriod(taxInfoDTO.getPaymentPeriod())
                .taxDate(taxInfoDTO.getTaxDate())
                .taxComment(taxInfoDTO.getTaxComment())
                .deleted(taxInfoDTO.isDeleted())
                .build();
        taxInfoRepository.save(taxInfoEntity);
        return null;
    }

    public TaxInfoEntity editTaxById(Long id, TaxInfoDTO taxInfoDTO) {
        TaxInfoEntity taxInfoEntity = taxInfoRepository.findById(id).orElse(null);
        assert taxInfoEntity != null;
        if (taxInfoDTO.getTaxType() != null) {
            taxInfoEntity.setTaxType(taxInfoDTO.getTaxType());
        }
        if (taxInfoDTO.getReportName() != null) {
            taxInfoEntity.setReportName(taxInfoDTO.getReportName());
        }
        if (taxInfoDTO.getReportingPeriod() != null) {
            taxInfoEntity.setReportingPeriod(taxInfoDTO.getReportingPeriod());
        }
        if (taxInfoDTO.getReportDate() != null) {
            taxInfoEntity.setReportDate(taxInfoDTO.getReportDate());
        }
        if (taxInfoDTO.getReportComment() != null) {
            taxInfoEntity.setReportComment(taxInfoDTO.getReportComment());
        }
        if (taxInfoDTO.getTaxName() != null) {
            taxInfoEntity.setTaxName(taxInfoDTO.getTaxName());
        }
        if (taxInfoDTO.getPaymentPeriod() != null) {
            taxInfoEntity.setPaymentPeriod(taxInfoDTO.getPaymentPeriod());
        }
        if (taxInfoDTO.getTaxDate() != null) {
            taxInfoEntity.setTaxDate(taxInfoDTO.getTaxDate());
        }
        if (taxInfoDTO.getTaxComment() != null) {
            taxInfoEntity.setTaxComment(taxInfoDTO.getTaxComment());
        }
        taxInfoEntity.setDeleted(taxInfoDTO.isDeleted());
        return taxInfoRepository.save(taxInfoEntity);
    }*/
}
