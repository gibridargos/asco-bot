package com.saidqosimov.taxinfobot.service;

import com.saidqosimov.taxinfobot.entity.TaxInfoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CacheDataService {
    private final UserService userService;
    private final TaxInfoService taxInfoService;

    Map<Long, List<String>> map = new HashMap<>();

    public void addTax(long chatId, String taxType) {
        List<String> taxTypeList;
        if (map.get(chatId) == null) {
            taxTypeList = new LinkedList<>();
            taxTypeList.add(taxType);
        } else {
            taxTypeList = map.get(chatId);
            taxTypeList.add(taxType);
        }
        map.put(chatId, taxTypeList);
    }

    public void saveTaxes(long chatId) {
        List<TaxInfoEntity> taxInfoEntityList = new LinkedList<>();
        List<String> taxTypeList = map.get(chatId);
        for (String taxType : taxTypeList) {
            taxInfoEntityList.addAll(taxInfoService.getTaxInfoByType(taxType));
        }
        userService.addTaxForUser(taxInfoEntityList, chatId);
        clear(chatId);
    }

    public void clear(long chatId) {
        map.remove(chatId);
    }

    public boolean checkCache(long chatId) {
        if (map.get(chatId) != null) {
            return true;
        }
        return false;
    }

    public List<TaxInfoEntity> getCache(long chatId) {
        List<TaxInfoEntity> taxInfoEntityList = new LinkedList<>();
        List<String> taxTypeList = map.get(chatId);
        for (String taxType : taxTypeList) {
            taxInfoEntityList.addAll(taxInfoService.getTaxInfoByType(taxType));
        }
        return taxInfoEntityList;
    }

    Map<Long, List<String >> mapDelete = new HashMap<>();

    public void addTaxforDelete(long chatId, String taxData) {
        List<String> taxList;
        if (mapDelete.get(chatId) == null) {
            taxList = new LinkedList<>();
            taxList.add(taxData);
        } else {
            taxList = mapDelete.get(chatId);
            taxList.add(taxData);
        }
        mapDelete.put(chatId, taxList);
    }

    public void deleteSelectedTaxes(long chatId) {
        List<TaxInfoEntity> taxInfoEntityList = new LinkedList<>();
        List<String> taxTypeList = mapDelete.get(chatId);
        for (String taxType : taxTypeList) {
            taxInfoEntityList.addAll(taxInfoService.getTaxInfoByType(taxType));
        }
        userService.deleteUserTaxesByTaxInfoEntityList(chatId, taxInfoEntityList);
        clearDeletedCache(chatId);
    }

    public void clearDeletedCache(long chatId) {
        mapDelete.remove(chatId);
    }

    public boolean checkForDeleteCache(long chatId) {
        if (mapDelete.get(chatId) != null) {
            return true;
        }
        return false;
    }
}
