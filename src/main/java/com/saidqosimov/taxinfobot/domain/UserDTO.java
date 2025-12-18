package com.saidqosimov.taxinfobot.domain;

import com.saidqosimov.taxinfobot.enums.Languages;
import com.saidqosimov.taxinfobot.enums.UserStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDTO {
    private Long id;
    private Long chatId;
    private Boolean isActive = true;
    private UserStep step;
    private Languages lang;
    private Boolean threeDaysAgo = false;
    private Boolean twoDaysAgo = false;
    private Boolean theDayBefore = false;
    private List<TaxInfoDTO> taxInfo = new LinkedList<>();
}
