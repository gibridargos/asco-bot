package com.saidqosimov.taxinfobot.service;

import com.saidqosimov.taxinfobot.entity.InfoEntity;
import com.saidqosimov.taxinfobot.repository.InfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InfoService {
    private final InfoRepository infoRepository;
    public synchronized List<String> getTypeOfTax() {
        List<InfoEntity> infoEntities = infoRepository.findAll();
        Set<String> infosString = new LinkedHashSet<>();
        for (InfoEntity info : infoEntities) {
            infosString.add(info.getTypeOfTax());
        }
        return new LinkedList<>(infosString);
    }

    public synchronized String getInfoByTypeOfTax(String typeOfTax) {
        List<InfoEntity> infoEntities = infoRepository.findAll();
        for (InfoEntity info : infoEntities) {
            if (info.getTypeOfTax().equals(typeOfTax)) {
                return info.getFullInfo();
            }
        }
        return null;
    }
}
