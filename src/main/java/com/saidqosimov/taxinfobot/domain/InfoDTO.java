package com.saidqosimov.taxinfobot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class InfoDTO {
    private Integer id;
    private String typeOfTax;
    private String fullInfo;
}
