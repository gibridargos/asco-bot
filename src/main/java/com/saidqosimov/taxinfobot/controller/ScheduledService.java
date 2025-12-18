package com.saidqosimov.taxinfobot.controller;

import com.saidqosimov.taxinfobot.config.ApplicationConfiguration;
import com.saidqosimov.taxinfobot.entity.TaxInfoEntity;
import com.saidqosimov.taxinfobot.entity.UserEntity;
import com.saidqosimov.taxinfobot.enums.MessageType;
import com.saidqosimov.taxinfobot.model.CodeMessage;
import com.saidqosimov.taxinfobot.service.TranslateService;
import com.saidqosimov.taxinfobot.service.UserService;
import com.saidqosimov.taxinfobot.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ScheduledService {
    private final UserService userService;
    private final TranslateService translate;
    private final ApplicationConfiguration applicationConfiguration;
    private final DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("d.M.yyyy");
    private final DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("dd.M.yyyy");
    private final DateTimeFormatter formatter4 = DateTimeFormatter.ofPattern("d.MM.yyyy");

    public synchronized List<CodeMessage> send() {
        ZoneId tashkentZoneId = ZoneId.of("Asia/Tashkent");
        LocalDate today = ZonedDateTime.now(tashkentZoneId).toLocalDate();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime nextDayTime = startOfDay.plusHours(24);
        LocalDateTime twoDayTime = startOfDay.plusHours(48);
        LocalDateTime threeDayTime = startOfDay.plusHours(72);
        LocalDate nextDay = nextDayTime.toLocalDate();
        LocalDate threeDayBefore = threeDayTime.toLocalDate();
        LocalDate twoDayBefore = twoDayTime.toLocalDate();
        List<CodeMessage> response = new LinkedList<>();

        List<UserEntity> allUser = userService.getAllUser();
        for (UserEntity user : allUser) {
            response.addAll(getResponse(user, today));
            if (user.getTheDayBefore()) {
                response.addAll(getResponse(user, nextDay));
            }
            if (user.getTwoDaysAgo()) {
                response.addAll(getResponse(user, twoDayBefore));
            }
            if (user.getThreeDaysAgo()) {
                response.addAll(getResponse(user, threeDayBefore));
            }
        }
        return response;
    }

    private List<CodeMessage> getResponse(UserEntity user, LocalDate date) {
        List<CodeMessage> response = new LinkedList<>();
        int langId = 0;
        switch (user.getLang()) {
            case uz -> langId = 0;
            case ru -> langId = 1;
        }
        int i = 0;
        int reportIndex = 0;
        int taxIndex = 0;

        StringBuilder report = new StringBuilder(Constants.REPORTS_DEADLINE[langId] + "\n\n");
        StringBuilder tax = new StringBuilder(Constants.TAX_PAY_DEADLINE[langId] + "\n\n");
        Set<TaxInfoEntity> taxInfoList = new LinkedHashSet<>(user.getTaxInfo());

        Set<String> taxSet = new LinkedHashSet<>();
        Set<String> reportSet = new LinkedHashSet<>();

        for (TaxInfoEntity taxInfo : taxInfoList) {
            String reportName = "";
            String reportingPeriod = "";
            String reportComment = "";
            String taxName = "";
            String paymentPeriod = "";
            String taxComment = "";
            if (langId == 1) {
                reportName = translate.translate(taxInfo.getReportName());
                reportingPeriod = translate.translate(taxInfo.getReportingPeriod());
                reportComment = translate.translate(taxInfo.getReportComment());
                taxName = translate.translate(taxInfo.getTaxName());
                paymentPeriod = translate.translate(taxInfo.getPaymentPeriod());
                taxComment = translate.translate(taxInfo.getTaxComment());
            } else {
                reportName = taxInfo.getReportName();
                reportingPeriod = taxInfo.getReportingPeriod();
                reportComment = taxInfo.getReportComment();
                taxName = taxInfo.getTaxName();
                paymentPeriod = taxInfo.getPaymentPeriod();
                taxComment = taxInfo.getTaxComment();
            }
            if (taxInfo.getReportDate() != null) {
                LocalDate reportDate = parseDate(taxInfo.getReportDate());
                if (reportDate.equals(date)) {
                    if (i == 0) {
                        i++;
                        response.add(getSendPhoto(langId, user.getChatId(), taxInfo.getReportDate()));
                    }
                    reportSet.add("<b>" + reportName + "</b> — " + reportingPeriod + "\n");
                    if (reportComment != null) {
                        reportSet.add("<i>" + reportComment + "</i>\n");
                    }
                }
            }
            if (taxInfo.getTaxDate() != null) {
                LocalDate taxDate = parseDate(taxInfo.getTaxDate());
                if (taxDate.equals(date)) {
                    if (i == 0) {
                        i++;
                        response.add(getSendPhoto(langId, user.getChatId(), taxInfo.getTaxDate()));
                    }
                    taxSet.add("<b>" + taxName + "</b> — " + paymentPeriod + "\n");
                    if (taxComment != null) {
                        taxSet.add("<i>" + taxComment + "</i>\n");
                    }
                }
            }
        }

        for (String data : reportSet) {
            if (data.startsWith("<b>")) {
                reportIndex++;
                report.append(reportIndex).append(") ").append(data);
            } else if (data.startsWith("<i>")) {
                report.append(data);
            }
        }
        for (String data : taxSet) {
            if (data.startsWith("<b>")) {
                taxIndex++;
                tax.append(taxIndex).append(") ").append(data);
            } else if (data.startsWith("<i>")) {
                tax.append(data);
            }
        }

        StringBuilder text = new StringBuilder();

        if (!report.toString().equals(Constants.REPORTS_DEADLINE[langId] + "\n\n")) {
            text.append(report).append("\n");
        }
        if (!tax.toString().equals(Constants.TAX_PAY_DEADLINE[langId] + "\n\n")) {
            text.append(tax).append("\n");
        }
        if (!text.isEmpty()) {
            response.add(getCodeMessage(text.toString(), user.getChatId()));
        }
        return response;
    }

    private CodeMessage getCodeMessage(String s, Long chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(s)
                .parseMode("HTML")
                .build();
        return CodeMessage.builder()
                .messageType(MessageType.SEND_MESSAGE)
                .sendMessage(sendMessage)
                .build();
    }

    private CodeMessage getSendPhoto(int langId, long chatId, String date) {
        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(applicationConfiguration.getPhotoUrl()))
                .caption("<b>" + Constants.NOTIFICATION[langId] + " (" + date + ")</b>")
                .parseMode("HTML")
                .build();
        return CodeMessage.builder()
                .sendPhoto(sendPhoto)
                .messageType(MessageType.SEND_PHOTO)
                .build();
    }

    public LocalDate parseDate(String dateString) {
        LocalDate date = null;
        DateTimeParseException exception = null;
        try {
            date = LocalDate.parse(dateString, formatter1);
        } catch (DateTimeParseException e) {
            exception = e;
            try {
                date = LocalDate.parse(dateString, formatter2);
            } catch (DateTimeParseException e2) {
                exception = e2;
                try {
                    date = LocalDate.parse(dateString, formatter3);
                } catch (DateTimeParseException e3) {
                    exception = e3;
                    try {
                        date = LocalDate.parse(dateString, formatter4);
                    } catch (DateTimeParseException e4) {
                        exception = e4;
                    }
                }
            }
        }

        if (date == null) {
            throw exception;
        }
        return date;
    }

    public List<CodeMessage> sendMonthlyScheduledMessages() {
        List<CodeMessage> response = new LinkedList<>();
        List<UserEntity> allUser = userService.getAllUser();
        for (UserEntity user : allUser) {
            int langId = 0;
            switch (user.getLang()) {
                case uz -> langId = 0;
                case ru -> langId = 1;
            }
            response.addAll(sendNotificationMonthlyTaxInfo(user.getTaxInfo(), langId, user.getChatId()));
        }
        return response;
    }

    public List<CodeMessage> sendNotificationMonthlyTaxInfo(List<TaxInfoEntity> caches, int langId, long chatId) {
        ZoneId tashkentZoneId = ZoneId.of("Asia/Tashkent");
        LocalDate today = ZonedDateTime.now(tashkentZoneId).toLocalDate();
        int monthValue = today.getMonthValue();
        int i = 0;
        int reportIndex = 0;
        int taxIndex = 0;

        StringBuilder report = new StringBuilder("<b>" + Constants.REPORTS_DEADLINE[langId] + "</b>\n");
        StringBuilder tax = new StringBuilder("<b>" + Constants.TAX_PAY_DEADLINE[langId] + "</b>\n");

        Set<String> taxSet = new LinkedHashSet<>();
        Set<String> reportSet = new LinkedHashSet<>();

        //String allReportComments = "";
        //String allTaxComments = "";

        List<CodeMessage> responseReport = new LinkedList<>();
        List<CodeMessage> responseTax = new LinkedList<>();
        List<CodeMessage> response = new LinkedList<>();
/*        Set<String> cache = new HashSet<>();
        for (TaxInfoEntity taxInfo : caches) {
            cache.add(taxInfo.getTaxType());
        }*/
        for (TaxInfoEntity taxData : caches) {
            String reportName = "";
            String reportingPeriod = "";
            String reportComment = "";
            String taxName = "";
            String paymentPeriod = "";
            String taxComment = "";
            if (langId != 0) {
                reportName = translate.translate(taxData.getReportName());
                reportingPeriod = translate.translate(taxData.getReportingPeriod());
                reportComment = translate.translate(taxData.getReportComment());
                taxName = translate.translate(taxData.getTaxName());
                paymentPeriod = translate.translate(taxData.getPaymentPeriod());
                taxComment = translate.translate(taxData.getTaxComment());
            } else {
                reportName = taxData.getReportName();
                reportingPeriod = taxData.getReportingPeriod();
                reportComment = taxData.getReportComment();
                taxName = taxData.getTaxName();
                paymentPeriod = taxData.getPaymentPeriod();
                taxComment = taxData.getTaxComment();
            }
            if (taxData.getReportDate() != null && !taxData.getReportDate().isEmpty()) {
                String[] reportDate = taxData.getReportDate().split("\\.");
                int getMonthReportDate = Integer.parseInt(reportDate[1]);
                if (monthValue == getMonthReportDate) {
                    reportSet.add("<b>" + reportName + "</b> — " + reportingPeriod + " (" + taxData.getReportDate() + ")\n");
                    if (reportComment != null) {
                        reportSet.add("<i>" + reportComment + "</i>\n");
                    }
                }
                if (taxData.getTaxDate() != null && !taxData.getTaxDate().isEmpty()) {
                    String[] taxDate = taxData.getTaxDate().split("\\.");
                    int getMonthTaxDate = Integer.parseInt(taxDate[1]);
                    if (monthValue == getMonthTaxDate) {
                        taxSet.add("<b>" + taxName + "</b> — " + paymentPeriod + " (" + taxData.getTaxDate() + ")\n");
                        if (taxComment != null) {
                            taxSet.add("<i>" + taxComment + "</i>\n");
                        }
                    }
                }
            }
        }
        for (String data : reportSet) {
            if (data.startsWith("<b>")) {
                reportIndex++;
                report.append(reportIndex).append(") ").append(data);
            } else if (data.startsWith("<i>")) {
                report.append(data);
            }
        }
        for (String data : taxSet) {
            if (data.startsWith("<b>")) {
                taxIndex++;
                tax.append(taxIndex).append(") ").append(data);
            } else if (data.startsWith("<i>")) {
                tax.append(data);
            }
        }
        //report += allReportComments;
        //tax += allTaxComments;
        if (!report.toString().equals("<b>" + Constants.REPORTS_DEADLINE[langId] + "</b>\n")) {
            response.add(getSendPhoto(langId, chatId, monthValue));
            i++;
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(report.toString())
                    .parseMode("HTML")
                    .build();
            responseReport.add(CodeMessage.builder()
                    .sendMessage(sendMessage)
                    .messageType(MessageType.SEND_MESSAGE)
                    .build());
            response.addAll(responseReport);
        }
        if (!tax.toString().equals("<b>" + Constants.TAX_PAY_DEADLINE[langId] + "</b>\n")) {
            if (i == 0) {
                response.add(getSendPhoto(langId, chatId, monthValue));
            }
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(tax.toString())
                    .parseMode("HTML")
                    .build();
            responseTax.add(CodeMessage.builder()
                    .sendMessage(sendMessage)
                    .messageType(MessageType.SEND_MESSAGE)
                    .build());
            response.addAll(responseTax);
        }
        return response;
    }

    private CodeMessage getSendPhoto(int langId, long chatId, int monthValue) {
        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(Constants.MONTHLY_PHOTO[(monthValue - 1)]))
                .caption(Constants.NEED_PAY_FOR_SELECTED_TAX[langId])
                .build();
        return CodeMessage.builder()
                .sendPhoto(sendPhoto)
                .messageType(MessageType.SEND_PHOTO)
                .build();
    }

}