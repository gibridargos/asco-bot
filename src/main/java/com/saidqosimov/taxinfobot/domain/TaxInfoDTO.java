package com.saidqosimov.taxinfobot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaxInfoDTO {
    private Long id;
    private String taxType;
    private String reportName;
    private String reportingPeriod;
    private String reportDate;
    private String reportComment;

    private String taxName;
    private String paymentPeriod;
    private String taxDate;
    private String taxComment;
    private boolean deleted = false;
}
