package com.saidqosimov.taxinfobot.service;

import com.saidqosimov.taxinfobot.domain.TaxInfoDTO;
import com.saidqosimov.taxinfobot.domain.UserDTO;
import com.saidqosimov.taxinfobot.entity.TaxInfoEntity;
import com.saidqosimov.taxinfobot.entity.UserEntity;
import com.saidqosimov.taxinfobot.enums.Languages;
import com.saidqosimov.taxinfobot.enums.UserStep;
import com.saidqosimov.taxinfobot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public synchronized void changeStep(Long chatId, UserStep step) {
        UserEntity user = userRepository.findByChatId(chatId);
        user.setStep(step);
        userRepository.save(user);
    }

    public synchronized List<String> getMyTaxes(Long chatId) {
        List<TaxInfoEntity> taxInfoList = userRepository.findByChatId(chatId).getTaxInfo();
        Set<String> typeOfTax = new HashSet<>();
        for (TaxInfoEntity taxInfo : taxInfoList) {
            typeOfTax.add(taxInfo.getTaxType());
        }
        return new LinkedList<>(typeOfTax);
    }

    public synchronized void changeLang(long chatId, Languages lang) {
        UserEntity user = userRepository.findByChatId(chatId);
        user.setLang(lang);
        userRepository.save(user);
    }

    public synchronized void save(Long chatId) {
        UserEntity user = UserEntity.builder()
                .chatId(chatId)
                .step(UserStep.AUTHENTICATE)
                .theDayBefore(true)
                .twoDaysAgo(false)
                .threeDaysAgo(false)
                .lang(Languages.uz)
                .isActive(true)
                .build();
        userRepository.save(user);
    }

    public synchronized Boolean checkUser(Long chatId) {
        UserEntity user = userRepository.findByChatId(chatId);
        return user == null;
    }

    public synchronized UserDTO getCurrentUser(Long chatId) {
        UserEntity user = userRepository.findByChatId(chatId);
        List<TaxInfoEntity> taxInfoEntities = user.getTaxInfo();
        List<TaxInfoDTO> taxInfoDTOS = new LinkedList<>();
        for (TaxInfoEntity taxInfo : taxInfoEntities) {
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
        return UserDTO.builder()
                .chatId(chatId)
                .isActive(user.getIsActive())
                .step(user.getStep())
                .lang(user.getLang())
                .theDayBefore(user.getTheDayBefore())
                .twoDaysAgo(user.getTwoDaysAgo())
                .threeDaysAgo(user.getThreeDaysAgo())
                .build();
    }

    public synchronized void addTaxForUser(List<TaxInfoEntity> taxInfoByType, long chatId) {
        UserEntity user = userRepository.findByChatId(chatId);
        List<TaxInfoEntity> taxInfo = user.getTaxInfo();
        Set<TaxInfoEntity> taxInfoSet = new LinkedHashSet<>(taxInfo);
        taxInfoSet.addAll(taxInfoByType);
        user.setTaxInfo(new LinkedList<>(taxInfoSet));
        userRepository.save(user);
    }

    public synchronized void deleteUserTaxesByTaxInfoEntityList(long chatId, List<TaxInfoEntity> taxInfos) {
        UserEntity user = userRepository.findByChatId(chatId);
        user.getTaxInfo().removeAll(taxInfos);
        userRepository.save(user);
    }

    public synchronized void deleteAllMyTaxes(long chatId) {
        UserEntity user = userRepository.findByChatId(chatId);
        List<TaxInfoEntity> taxInfo = new LinkedList<>();
        user.setTaxInfo(taxInfo);
        userRepository.save(user);
    }

    public synchronized List<UserEntity> getAllUser() {
        return userRepository.findAll();
    }

    public synchronized void setTheDayBeforeNotificationStatus(boolean b, long chatId) {
        UserEntity user = userRepository.findByChatId(chatId);
        user.setTheDayBefore(b);
        userRepository.save(user);
    }

    public synchronized void setTwoDaysAgoNotificationStatus(boolean b, long chatId) {
        UserEntity user = userRepository.findByChatId(chatId);
        user.setTwoDaysAgo(b);
        userRepository.save(user);
    }

    public synchronized void setThreeDaysAgoNotificationStatus(boolean b, long chatId) {
        UserEntity user = userRepository.findByChatId(chatId);
        user.setThreeDaysAgo(b);
        userRepository.save(user);
    }

}
