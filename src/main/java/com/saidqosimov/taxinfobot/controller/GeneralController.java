package com.saidqosimov.taxinfobot.controller;

import com.saidqosimov.taxinfobot.domain.TaxInfoDTO;
import com.saidqosimov.taxinfobot.domain.UserDTO;
import com.saidqosimov.taxinfobot.entity.TaxInfoEntity;
import com.saidqosimov.taxinfobot.enums.Languages;
import com.saidqosimov.taxinfobot.enums.MessageType;
import com.saidqosimov.taxinfobot.enums.UserStep;
import com.saidqosimov.taxinfobot.model.CodeMessage;
import com.saidqosimov.taxinfobot.service.*;
import com.saidqosimov.taxinfobot.util.ButtonController;
import com.saidqosimov.taxinfobot.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.*;

@Component
@RequiredArgsConstructor
public class GeneralController {
    private final UserService userService;
    private final InfoService infoService;
    private final ButtonController buttonController;
    private final CacheDataService cacheDataService;
    private final TranslateService translate;
    private final TaxInfoService taxInfoService;
    private final ScheduledService scheduledService;

    public List<CodeMessage> handle(Message message) {
        List<CodeMessage> response = new LinkedList<>();
        String request = message.getText();
        Long chatId = message.getChatId();
        if (userService.checkUser(chatId)) {
            userService.save(chatId);
        }
        UserDTO currentUser = userService.getCurrentUser(chatId);
        int langId = 0;
        switch (currentUser.getLang()) {
            case uz -> langId = 0;
            case ru -> langId = 1;
        }
        if (currentUser.getIsActive()) {
            if (request.equals(Constants.START)) {
                userService.changeStep(chatId, UserStep.AUTHENTICATE);
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(Constants.AUTH_RESPONSE[0] + "\n\n" + Constants.AUTH_RESPONSE[1])
                        .parseMode("HTML")
                        .build();
                sendMessage.setReplyMarkup(buttonController.getLanguageReplyButtons());
                CodeMessage codeMessage = CodeMessage.builder()
                        .sendMessage(sendMessage)
                        .messageType(MessageType.SEND_MESSAGE)
                        .build();
                response.add(codeMessage);
            } else if (request.equals(Constants.TAX_LIST[langId]) || request.equals(Constants.ADD_TAXES_BUTTON[langId])) {
                userService.changeStep(chatId, UserStep.SELECT_TAX);
                response.add(getSelectTaxHelp(langId, chatId));
                response.add(sendPage(taxInfoService.getTaxTypes(), chatId, langId, UserStep.SELECT_TAX));
            } else if (request.equals(Constants.STANDARD_AOS[langId])) {
                response.add(getQQSAOSSHelp(langId, chatId));
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(getAOSText(langId))
                        .replyMarkup(buttonController.saveNDSandAOS(langId))
                        .parseMode("HTML")
                        .build();
                CodeMessage codeMessage = CodeMessage.builder()
                        .sendMessage(sendMessage)
                        .messageType(MessageType.SEND_MESSAGE)
                        .build();
                response.add(codeMessage);
            } else if (request.equals(Constants.STANDARD_QQS[langId])) {
                response.add(getQQSAOSSHelp(langId, chatId));
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(getQQSText(langId))
                        .replyMarkup(buttonController.saveNDSandAOS(langId))
                        .parseMode("HTML")
                        .build();
                CodeMessage codeMessage = CodeMessage.builder()
                        .sendMessage(sendMessage)
                        .messageType(MessageType.SEND_MESSAGE)
                        .build();
                response.add(codeMessage);
            }  /*else if (request.equals(Constants.STANDARD_AOS[langId])) {
            response.add(getSelectTaxHelp(langId, chatId));
            response.add(sendPage(getAOS(), chatId, langId, UserStep.SELECT_TAX_AOS));
        } else if (request.equals(Constants.STANDARD_QQS[langId])) {
            userService.changeStep(chatId, UserStep.SELECT_TAX_QQS);
            response.add(getSelectTaxHelp(langId, chatId));
            response.add(sendPage(getQQS(), chatId, langId, UserStep.SELECT_TAX_QQS));
        }*/ else if (request.equals(Constants.MY_TAXES[langId])) {
                userService.changeStep(chatId, UserStep.SHOW_MY_TAX);
                response.add(sendPage(userService.getMyTaxes(chatId), chatId, langId, UserStep.SHOW_MY_TAX));
            } else if (request.equals(Constants.INFO[langId])) {
                userService.changeStep(chatId, UserStep.SHOW_INFO);
                response.add(getSelectTaxHelp(langId, chatId));
                response.add(sendPage(infoService.getTypeOfTax(), chatId, langId, UserStep.SHOW_INFO));
            } else if (request.equals(Constants.SETTINGS[langId])) {
                userService.changeStep(chatId, UserStep.SETTING);
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(Constants.SETTINGS[langId])
                        .replyMarkup(buttonController.getSettingButtons(langId))
                        .build();
                CodeMessage codeMessage = CodeMessage.builder()
                        .sendMessage(sendMessage)
                        .messageType(MessageType.SEND_MESSAGE)
                        .build();
                response.add(codeMessage);
            } else if (request.equals(Constants.CHANGE_LANGUAGE[langId])) {
                userService.changeStep(chatId, UserStep.SELECT_LANG);
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(Constants.SELECT_LANGUAGE[langId])
                        .replyMarkup(buttonController.getLanguageReplyButtons())
                        .build();
                CodeMessage codeMessage = CodeMessage.builder()
                        .sendMessage(sendMessage)
                        .messageType(MessageType.SEND_MESSAGE)
                        .build();
                response.add(codeMessage);
            } else if (request.equals(Constants.BACK_TO_MAIN_MENU[langId])) {
                userService.changeStep(chatId, UserStep.MAIN_MENU);
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(Constants.MAIN_MENU[langId])
                        .build();
                sendMessage.setReplyMarkup(buttonController.getMainButtons(langId));
                CodeMessage codeMessage = CodeMessage.builder()
                        .sendMessage(sendMessage)
                        .messageType(MessageType.SEND_MESSAGE)
                        .build();
                response.add(codeMessage);
            } else if (request.equals(Constants.EDIT_NOTIFICATION_DATE[langId])) {
                response.addAll(getEditNotifications(langId, currentUser));
            } else if (request.equals(Constants.CLEAR_ALL_TAXES_BUTTON[langId])) {
                response.add(getDeleteAllTaxesRequest(langId, chatId));
            } else if (request.equals(Constants.CONTACT_US[langId])) {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(Constants.CONTACT[langId])
                        .build();
                sendMessage.setReplyMarkup(buttonController.getMainButtons(langId));
                CodeMessage codeMessage = CodeMessage.builder()
                        .sendMessage(sendMessage)
                        .messageType(MessageType.SEND_MESSAGE)
                        .build();
                response.add(codeMessage);
            } else if (request.equals(Constants.USER_MANUAL[langId])) {/*
            SendVideo sendVideo = SendVideo.builder()
                    .chatId(chatId)
                    .video(new InputFile(""))
                    .caption(Constants.CAPTION[langId])
                    .build();
            CodeMessage codeMessage = CodeMessage.builder()
                    .sendVideo(sendVideo)
                    .messageType(MessageType.SEND_VIDEO)
                    .build();
            response.add(codeMessage);*/
            } else if ((currentUser.getStep().equals(UserStep.AUTHENTICATE) || currentUser.getStep().equals(UserStep.SELECT_LANG)) && request.equals(Constants.LANG_UZ) || request.equals(Constants.LANG_RU)) {
                List<CodeMessage> codeMessages = selectLang(request, chatId);
                response.addAll(codeMessages);
                userService.changeStep(chatId, UserStep.MAIN_MENU);
            } else if (currentUser.getStep().equals(UserStep.AUTHENTICATE)) {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(Constants.AUTH_RESPONSE[0] + "\n\n" + Constants.AUTH_RESPONSE[1])
                        .parseMode("HTML")
                        .build();
                sendMessage.setReplyMarkup(buttonController.getLanguageReplyButtons());
                CodeMessage codeMessage = CodeMessage.builder()
                        .sendMessage(sendMessage)
                        .messageType(MessageType.SEND_MESSAGE)
                        .build();
                response.add(codeMessage);
            }
        }
        return response;
    }


    public List<CodeMessage> handle(CallbackQuery callbackQuery) {
        List<CodeMessage> response = new LinkedList<>();
        String callbackData = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = callbackQuery.getMessage().getMessageId();
        String text = callbackQuery.getMessage().getText();
        List<String> taxTypes = new LinkedList<>();
        UserDTO currentUser = userService.getCurrentUser(chatId);
        int langId = 0;
        switch (currentUser.getLang()) {
            case uz -> langId = 0;
            case ru -> langId = 1;
        }
        if (text.startsWith(Constants.TAX_LIST[langId])) {
            userService.changeStep(chatId, UserStep.SELECT_TAX);
            taxTypes = taxInfoService.getTaxTypes();
        } else if (text.startsWith(Constants.MY_TAXES[langId])) {
            userService.changeStep(chatId, UserStep.SHOW_MY_TAX);
            taxTypes = userService.getMyTaxes(chatId);
        } else if (text.startsWith(Constants.INFO[langId])) {
            userService.changeStep(chatId, UserStep.SHOW_INFO);
            taxTypes = infoService.getTypeOfTax();
        } else if (text.startsWith(Constants.STANDARD_QQS[langId]) || text.startsWith(Constants.STANDARD_AOS[langId])) {
            userService.changeStep(chatId, UserStep.MAIN_MENU);
        }
        currentUser = userService.getCurrentUser(chatId);
        if (callbackData.startsWith(Constants.PAGE)) {
            // yangi page ga o'tkazish
            response.add(editPage(callbackQuery, taxTypes, langId, userService.getCurrentUser(chatId).getStep()));
        } else if ((callbackData.equals(Constants.SAVE_CACHE) || callbackData.equals(Constants.CLEAR_CACHE)) && (currentUser.getStep().equals(UserStep.SELECT_TAX)/* || currentUser.getStep().equals(UserStep.SELECT_TAX_AOS) || currentUser.getStep().equals(UserStep.SELECT_TAX_QQS)*/)) {
            //soliq tanlab saqlash yoki tanlanganlarni tozalash
            if (cacheDataService.checkCache(chatId)) {
                String resp;
                if (callbackData.equals(Constants.SAVE_CACHE)) {
                    resp = Constants.DATA_SAVED[langId];
                    List<TaxInfoEntity> caches = cacheDataService.getCache(chatId);
                    response.addAll(scheduledService.sendNotificationMonthlyTaxInfo(caches, langId, chatId));
                    cacheDataService.saveTaxes(chatId);
                } else {
                    resp = Constants.DATA_CLEARED[langId];
                    cacheDataService.clear(chatId);
                }
                DeleteMessage deleteMessage = DeleteMessage.builder()
                        .messageId(messageId)
                        .chatId(chatId)
                        .build();
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(resp)
                        .replyMarkup(buttonController.getMainButtons(langId))
                        .build();
                response.add(CodeMessage.builder().deleteMessage(deleteMessage).messageType(MessageType.DELETE_MESSAGE).build());
                response.add(CodeMessage.builder().sendMessage(sendMessage).messageType(MessageType.SEND_MESSAGE).build());
            } else {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(Constants.DATA_NOT_ENTERED[langId])
                        .build();
                response.add(CodeMessage.builder()
                        .sendMessage(sendMessage)
                        .messageType(MessageType.SEND_MESSAGE)
                        .build());
            }
        } else if (callbackData.equals(Constants.DELETE_SELECTED_TAXES) || callbackData.equals(Constants.CLEAR_CACHE) && currentUser.getStep().equals(UserStep.SHOW_MY_TAX)) {
            //mening soliqlarim qismida soliqlarni o'chirish yoki tanlanganlarni tozalash
            if (cacheDataService.checkForDeleteCache(chatId)) {
                String resp;
                if (callbackData.equals(Constants.DELETE_SELECTED_TAXES)) {
                    resp = Constants.SUCCESS_DELETE[langId];
                    cacheDataService.deleteSelectedTaxes(chatId);
                } else {
                    resp = Constants.DATA_CLEARED[langId];
                    cacheDataService.clearDeletedCache(chatId);
                }
                DeleteMessage deleteMessage = DeleteMessage.builder()
                        .messageId(messageId)
                        .chatId(chatId)
                        .build();
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(resp)
                        .replyMarkup(buttonController.getMainButtons(langId))
                        .build();
                response.add(CodeMessage.builder().deleteMessage(deleteMessage).messageType(MessageType.DELETE_MESSAGE).build());
                response.add(CodeMessage.builder().sendMessage(sendMessage).messageType(MessageType.SEND_MESSAGE).build());
            } else {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(Constants.DATA_NOT_ENTERED[langId])
                        .build();
                response.add(CodeMessage.builder()
                        .sendMessage(sendMessage)
                        .messageType(MessageType.SEND_MESSAGE)
                        .build());
            }
        } else if (callbackData.equals(Constants.CLEAR_ALL_TAXES) && text.equals(Constants.DELETE_ALL_TAXES_REQUEST[langId])) {
            // Hamma ma'lumotlarni o'chirishga tasdiq
            userService.deleteAllMyTaxes(chatId);
            EditMessageText editMessageText = EditMessageText.builder()
                    .chatId(chatId)
                    .text(Constants.SUCCESS_DELETE_ALL_TAXES[langId])
                    .messageId(messageId)
                    .build();
            CodeMessage codeMessage = CodeMessage.builder()
                    .editMessageText(editMessageText)
                    .messageType(MessageType.EDIT_MESSAGE)
                    .build();
            response.add(codeMessage);

        } else if (callbackData.equals(Constants.DENY_ACCESS) && text.equals(Constants.DELETE_ALL_TAXES_REQUEST[langId])) {
            //Ma'lumotlarni tozalash bekor qilindi
            EditMessageText editMessageText = EditMessageText.builder()
                    .chatId(chatId)
                    .text(Constants.DENY_DELETE[langId])
                    .messageId(messageId)
                    .build();
            CodeMessage codeMessage = CodeMessage.builder()
                    .editMessageText(editMessageText)
                    .messageType(MessageType.EDIT_MESSAGE)
                    .build();
            response.add(codeMessage);
        } else if (callbackData.equals(Constants.THE_DAY_BEFORE_ON_NOTIFICATION) || callbackData.equals(Constants.THE_DAY_BEFORE_OFF_NOTIFICATION)) {
            //1 kun oldin yoqish va o'chirish
            boolean status = callbackData.equals(Constants.THE_DAY_BEFORE_ON_NOTIFICATION);
            userService.setTheDayBeforeNotificationStatus(status, chatId);
            if (status) {
                response.addAll(getNotificationResponse(chatId, Constants.THE_DAY_CHANGE_NOTIFICATION_TO_ON[langId], callbackQuery.getMessage().getMessageId(), callbackQuery.getMessage().getText(), buttonController.getTheDayBeforeNotification(langId, chatId)));
            } else {
                response.addAll(getNotificationResponse(chatId, Constants.THE_DAY_CHANGE_NOTIFICATION_TO_OFF[langId], callbackQuery.getMessage().getMessageId(), callbackQuery.getMessage().getText(), buttonController.getTheDayBeforeNotification(langId, chatId)));
            }
        } else if (callbackData.equals(Constants.TWO_DAYS_AGO_ON_NOTIFICATION) || callbackData.equals(Constants.TWO_DAYS_AGO_OFF_NOTIFICATION)) {
            //2 kun oldin yoqish va o'chirish
            boolean status = callbackData.equals(Constants.TWO_DAYS_AGO_ON_NOTIFICATION);
            userService.setTwoDaysAgoNotificationStatus(status, chatId);
            if (status) {
                response.addAll(getNotificationResponse(chatId, Constants.TWO_DAY_CHANGE_NOTIFICATION_TO_ON[langId], callbackQuery.getMessage().getMessageId(), callbackQuery.getMessage().getText(), buttonController.getTwoDaysAgoNotification(langId, chatId)));

            } else {
                response.addAll(getNotificationResponse(chatId, Constants.TWO_DAY_CHANGE_NOTIFICATION_TO_OFF[langId], callbackQuery.getMessage().getMessageId(), callbackQuery.getMessage().getText(), buttonController.getTwoDaysAgoNotification(langId, chatId)));
            }
        } else if (callbackData.equals(Constants.THREE_DAYS_AGO_ON_NOTIFICATION) || callbackData.equals(Constants.THREE_DAYS_AGO_OFF_NOTIFICATION)) {
            //3 kun oldin yoqish va o'chirish
            boolean status = callbackData.equals(Constants.THREE_DAYS_AGO_ON_NOTIFICATION);
            userService.setThreeDaysAgoNotificationStatus(status, chatId);
            if (status) {
                response.addAll(getNotificationResponse(chatId, Constants.THREE_DAY_CHANGE_NOTIFICATION_TO_ON[langId], callbackQuery.getMessage().getMessageId(), Constants.THREE_DAYS_BEFORE[langId], buttonController.getThreeDaysAgoNotification(langId, chatId)));
            } else {
                response.addAll(getNotificationResponse(chatId, Constants.THREE_DAY_CHANGE_NOTIFICATION_TO_OFF[langId], callbackQuery.getMessage().getMessageId(), Constants.THREE_DAYS_BEFORE[langId], buttonController.getThreeDaysAgoNotification(langId, chatId)));
            }
        } else if (text.startsWith(Constants.STANDARD_AOS[langId])) {
            //standart aos qo'shish yoki bekor qilish
            if (callbackData.equals(Constants.DENY_ACCESS)) {
                EditMessageText editMessageText = EditMessageText.builder()
                        .chatId(chatId)
                        .text(Constants.DENY_DELETE[langId])
                        .messageId(messageId)
                        .build();
                CodeMessage codeMessage = CodeMessage.builder()
                        .editMessageText(editMessageText)
                        .messageType(MessageType.EDIT_MESSAGE)
                        .build();
                response.add(codeMessage);
            } else if (callbackData.equals(Constants.SAVE_CACHE)) {
                userService.addTaxForUser(getAOS(), chatId);
                DeleteMessage deleteMessage = DeleteMessage.builder()
                        .messageId(messageId)
                        .chatId(chatId)
                        .build();
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(Constants.DATA_SAVED[langId])
                        .replyMarkup(buttonController.getMainButtons(langId))
                        .build();
                response.add(CodeMessage.builder().deleteMessage(deleteMessage).messageType(MessageType.DELETE_MESSAGE).build());
                response.add(CodeMessage.builder().sendMessage(sendMessage).messageType(MessageType.SEND_MESSAGE).build());
                response.addAll(scheduledService.sendNotificationMonthlyTaxInfo(getAOS(), langId, chatId));
            }
        } else if (text.startsWith(Constants.STANDARD_QQS[langId])) {
            //standart qqs qo'shish yoki bekor qilish
            if (callbackData.equals(Constants.DENY_ACCESS)) {
                EditMessageText editMessageText = EditMessageText.builder()
                        .chatId(chatId)
                        .text(Constants.DENY_DELETE[langId])
                        .messageId(messageId)
                        .build();
                CodeMessage codeMessage = CodeMessage.builder()
                        .editMessageText(editMessageText)
                        .messageType(MessageType.EDIT_MESSAGE)
                        .build();
                response.add(codeMessage);
            } else if (callbackData.equals(Constants.SAVE_CACHE)) {
                userService.addTaxForUser(getQQS(), chatId);
                DeleteMessage deleteMessage = DeleteMessage.builder()
                        .messageId(messageId)
                        .chatId(chatId)
                        .build();
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(Constants.DATA_SAVED[langId])
                        .replyMarkup(buttonController.getMainButtons(langId))
                        .build();
                response.add(CodeMessage.builder().deleteMessage(deleteMessage).messageType(MessageType.DELETE_MESSAGE).build());
                response.add(CodeMessage.builder().sendMessage(sendMessage).messageType(MessageType.SEND_MESSAGE).build());
                response.addAll(scheduledService.sendNotificationMonthlyTaxInfo(getQQS(), langId, chatId));
            }
        } else {
            //raqamlar bosilganda
            int dataIndex = Integer.parseInt(callbackData);
            String selectedData = taxTypes.get(dataIndex);
            currentUser = userService.getCurrentUser(chatId);
            //obuna bo'lish uchun soliq turi tanlanganda
            if (currentUser.getStep().equals(UserStep.SELECT_TAX) /*|| currentUser.getStep().equals(UserStep.SELECT_TAX_AOS) || currentUser.getStep().equals(UserStep.SELECT_TAX_QQS)*/) {
                cacheDataService.addTax(chatId, selectedData);
            }
            //obuna bo'lgan solig'ini tanlaganda
            else if (currentUser.getStep().equals(UserStep.SHOW_MY_TAX)) {
                cacheDataService.addTaxforDelete(chatId, selectedData);
            } else if (currentUser.getStep().equals(UserStep.SHOW_INFO)) {
                String infoByTypeOfTax;
                if (langId == 0) {
                    infoByTypeOfTax = "<b>" + selectedData + "</b>\n" + infoService.getInfoByTypeOfTax(selectedData);
                } else {
                    infoByTypeOfTax = "<b>" + translate.translate(selectedData) + "</b>\n" + translate.getInfoRu(selectedData);
                }
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(infoByTypeOfTax)
                        .parseMode("HTML")
                        .build();
                CodeMessage codeMessage = CodeMessage.builder()
                        .sendMessage(sendMessage)
                        .messageType(MessageType.SEND_MESSAGE)
                        .build();
                response.add(codeMessage);
            }
        }

        return response;
    }

    private List<CodeMessage> getNotificationResponse(long chatId, String response, Integer messageId, String editResponse, InlineKeyboardMarkup inlineKeyboardMarkup) {
        List<CodeMessage> codeMessages = new LinkedList<>();
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(response)
                .build();
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(editResponse)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        codeMessages.add(CodeMessage.builder().sendMessage(sendMessage).messageType(MessageType.SEND_MESSAGE).build());
        codeMessages.add(CodeMessage.builder().editMessageText(editMessageText).messageType(MessageType.EDIT_MESSAGE).build());
        return codeMessages;
    }

    private List<CodeMessage> selectLang(String languageData, long chatId) {
        List<CodeMessage> response = new LinkedList<>();
        UserDTO currentUser = userService.getCurrentUser(chatId);
        if (languageData.equals(Constants.LANG_UZ)) {
            userService.changeLang(chatId, Languages.uz);
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(Constants.LANGUAGE_SUCCESSFUL_CHANGED[0])
                    .build();
            if (currentUser.getStep().equals(UserStep.AUTHENTICATE)) {
                sendMessage.setReplyMarkup(buttonController.getStartButtons(0));
            } else {
                sendMessage.setReplyMarkup(buttonController.getMainButtons(0));
            }
            response.add(CodeMessage.builder()
                    .sendMessage(sendMessage)
                    .messageType(MessageType.SEND_MESSAGE)
                    .build());
        } else if (languageData.equals(Constants.LANG_RU)) {
            userService.changeLang(chatId, Languages.ru);
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(Constants.LANGUAGE_SUCCESSFUL_CHANGED[1])
                    .build();
            if (currentUser.getStep().equals(UserStep.AUTHENTICATE)) {
                sendMessage.setReplyMarkup(buttonController.getStartButtons(1));
            } else {
                sendMessage.setReplyMarkup(buttonController.getMainButtons(1));
            }
            response.add(CodeMessage.builder()
                    .sendMessage(sendMessage)
                    .messageType(MessageType.SEND_MESSAGE)
                    .build());
        }
        return response;
    }


    private CodeMessage getSelectTaxHelp(int langId, long chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(Constants.SELECT_TAX_HELP[langId])
                .build();
        return CodeMessage.builder()
                .sendMessage(sendMessage)
                .messageType(MessageType.SEND_MESSAGE)
                .build();
    }

    private CodeMessage getQQSAOSSHelp(int langId, long chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(Constants.QQS_AOS_HELP[langId])
                .build();
        return CodeMessage.builder()
                .sendMessage(sendMessage)
                .messageType(MessageType.SEND_MESSAGE)
                .build();
    }

    private CodeMessage getDeleteAllTaxesRequest(int langId, long chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(Constants.DELETE_ALL_TAXES_REQUEST[langId])
                .replyMarkup(buttonController.getRequestClearData(langId))
                .build();
        return CodeMessage.builder()
                .sendMessage(sendMessage)
                .messageType(MessageType.SEND_MESSAGE)
                .build();
    }


    private List<CodeMessage> getEditNotifications(int langId, UserDTO currentUser) {
        long chatId = currentUser.getChatId();
        List<CodeMessage> codeMessages = new ArrayList<>();
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(Constants.NOTIFICATION_DATE_REQUEST[langId])
                .build();
        codeMessages.add(CodeMessage.builder()
                .sendMessage(sendMessage)
                .messageType(MessageType.SEND_MESSAGE)
                .build());
        sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(Constants.THE_DAY_BEFORE[langId])
                .replyMarkup(buttonController.getTheDayBeforeNotification(langId, chatId))
                .build();
        codeMessages.add(CodeMessage.builder()
                .sendMessage(sendMessage)
                .messageType(MessageType.SEND_MESSAGE)
                .build());
        sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(Constants.TWO_DAYS_BEFORE[langId])
                .replyMarkup(buttonController.getTwoDaysAgoNotification(langId, chatId))
                .build();
        codeMessages.add(CodeMessage.builder()
                .sendMessage(sendMessage)
                .messageType(MessageType.SEND_MESSAGE)
                .build());
        sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(Constants.THREE_DAYS_BEFORE[langId])
                .replyMarkup(buttonController.getThreeDaysAgoNotification(langId, chatId))
                .build();
        codeMessages.add(CodeMessage.builder()
                .sendMessage(sendMessage)
                .messageType(MessageType.SEND_MESSAGE)
                .build());
        return codeMessages;
    }

    private CodeMessage sendPage(List<String> taxTypes, Long chatId, int langId, UserStep userStep) {
        int startIndex = 0;
        int endIndex = Math.min(10, taxTypes.size());
        StringBuilder messageText = getHeaderText(chatId, taxTypes, 0, langId);
        List<String> myTaxes = new LinkedList<>();
        List<TaxInfoDTO> allTaxes = taxInfoService.getAllTaxes();
        if (userService.getCurrentUser(chatId).getStep().equals(UserStep.SHOW_INFO)) {
            myTaxes = infoService.getTypeOfTax();
        } else {
            myTaxes = userService.getMyTaxes(chatId);
        }
        for (int i = startIndex; i < endIndex; i++) {
            String taxType;
            String selected = "\n";
            if (langId != 0) {
                taxType = translate.translate(taxTypes.get(i));
            } else {
                taxType = taxTypes.get(i);
            }
            if (myTaxes.contains(taxTypes.get(i)) && (userStep.equals(UserStep.SELECT_TAX)/* || userStep.equals(UserStep.SELECT_TAX_AOS) || userStep.equals(UserStep.SELECT_TAX_QQS)*/)) {
                selected = "✅\n";
            }
            assert messageText != null;
            messageText.append(i + 1).append(") ");
            messageText.append(taxType).append(selected);
        }
        assert messageText != null;
        if (userStep.equals(UserStep.SHOW_MY_TAX)) {
            messageText.append("\n<b>").append(Constants.MY_TAXES_CAPTION[langId]).append("</b>");
        }
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(messageText.toString())
                .replyMarkup(buttonController.createInlineKeyboardMarkup(0, taxTypes, chatId, langId, allTaxes))
                .parseMode("HTML")
                .build();
        return CodeMessage.builder()
                .sendMessage(sendMessage)
                .messageType(MessageType.SEND_MESSAGE)
                .build();
    }

    private CodeMessage editPage(CallbackQuery callbackQuery, List<String> taxTypes, int langId, UserStep userStep) {
        String callbackData = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = callbackQuery.getMessage().getMessageId();

        int page = Integer.parseInt(callbackData.split(":")[1]);
        int startIndex = page * 10;
        int endIndex = Math.min((page + 1) * 10, taxTypes.size());
        StringBuilder messageText = getHeaderText(chatId, taxTypes, page, langId);
        List<String> myTaxes = new LinkedList<>();
        List<TaxInfoDTO> allTaxes = taxInfoService.getAllTaxes();
        if (userService.getCurrentUser(chatId).getStep().equals(UserStep.SHOW_INFO)) {
            myTaxes = infoService.getTypeOfTax();
        } else {
            myTaxes = userService.getMyTaxes(chatId);
        }
        for (int i = (startIndex - page * 10); i < (endIndex - page * 10); i++) {
            String taxType;
            String selected = "\n";

            if (langId != 0) {
                taxType = translate.translate(taxTypes.get(i + page * 10));
            } else {
                taxType = taxTypes.get(i + page * 10);
            }
            if (myTaxes.contains(taxTypes.get(i + page * 10)) && (userStep.equals(UserStep.SELECT_TAX) /*|| userStep.equals(UserStep.SELECT_TAX_AOS) || userStep.equals(UserStep.SELECT_TAX_QQS)*/)) {
                selected = "✅\n";
            }
            assert messageText != null;
            messageText.append(i + 1).append(") ");
            messageText.append(taxType).append(selected);
        }
        assert messageText != null;
        if (userStep.equals(UserStep.SHOW_MY_TAX)) {
            messageText.append("\n<b>").append(Constants.MY_TAXES_CAPTION[langId]).append("</b>");
        }
        EditMessageText editMessage = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(messageText.toString())
                .parseMode("HTML")
                .replyMarkup(buttonController.createInlineKeyboardMarkup(page, taxTypes, chatId, langId, allTaxes))
                .build();
        return CodeMessage.builder()
                .editMessageText(editMessage)
                .messageType(MessageType.EDIT_MESSAGE)
                .build();
    }

    private StringBuilder getHeaderText(Long chatId, List<String> taxTypes, int page, int langId) {
        UserDTO currentUser = userService.getCurrentUser(chatId);
        if (currentUser.getStep().equals(UserStep.SELECT_TAX)) {
            return new StringBuilder("<b>" + Constants.TAX_LIST[langId] + "</b> (" + (page + 1) + "/" + calculateTotalPages(taxTypes.size()) + ") :\n");
        } /*else if (currentUser.getStep().equals(UserStep.SELECT_TAX_AOS)) {
            return new StringBuilder(Constants.STANDARD_AOS_TAX_LIST[langId] + " (" + (page + 1) + "/" + calculateTotalPages(taxTypes.size()) + ") :\n");
        } else if (currentUser.getStep().equals(UserStep.SELECT_TAX_QQS)) {
            return new StringBuilder(Constants.STANDARD_QQS_TAX_LIST[langId] + " (" + (page + 1) + "/" + calculateTotalPages(taxTypes.size()) + ") :\n");
        } */ else if (currentUser.getStep().equals(UserStep.SHOW_MY_TAX)) {
            return new StringBuilder("<b>" + Constants.MY_TAXES[langId] + "</b> (" + (page + 1) + "/" + calculateTotalPages(taxTypes.size()) + ") :\n");
        } else if (currentUser.getStep().equals(UserStep.SHOW_INFO)) {
            return new StringBuilder("<b>" + Constants.INFO[langId] + "</b> (" + (page + 1) + "/" + calculateTotalPages(taxTypes.size()) + ") :\n");
        }
        return null;
    }

    private List<TaxInfoEntity> getQQS() {
        List<TaxInfoEntity> taxes = new LinkedList<>();
        taxes.addAll(taxInfoService.getTaxInfoByType("Қўшилган қиймат солиғи"));
        taxes.addAll(taxInfoService.getTaxInfoByType("Фойда солиғи"));
        taxes.addAll(taxInfoService.getTaxInfoByType("Жисмоний шахслардан олинадиган даромад солиғи ва ижтимоий солиқ"));
        taxes.addAll(taxInfoService.getTaxInfoByType("Молиявий ҳисоботлар\n(Хўжалик юритувчи субъектлар (кичик корхоналар ва микрофирмалар) учун)"));
        return taxes;
    }

    private List<TaxInfoEntity> getAOS() {
        List<TaxInfoEntity> taxes = new LinkedList<>();
        taxes.addAll(taxInfoService.getTaxInfoByType("Айланмадан олинадиган солиқ"));
        taxes.addAll(taxInfoService.getTaxInfoByType("Жисмоний шахслардан олинадиган даромад солиғи ва ижтимоий солиқ"));
        taxes.addAll(taxInfoService.getTaxInfoByType("Молиявий ҳисоботлар\n(Хўжалик юритувчи субъектлар (кичик корхоналар ва микрофирмалар) учун)"));
        return taxes;
    }

    private String getQQSText(int langId) {
        String text = "<b>" + Constants.STANDARD_QQS[langId] + "</b>\n";
        int i = 1;
        Set<String> taxTypeSet = new LinkedHashSet<>();
        for (TaxInfoEntity taxInfo : getQQS()) {
            if (langId == 0) {
                taxTypeSet.add(taxInfo.getTaxType());
            } else if (langId == 1) {
                taxTypeSet.add(translate.translate(taxInfo.getTaxType()));
            }
        }
        for (String taxType : taxTypeSet) {
            text += (i + ") " + taxType + "\n");
            i++;
        }
        return text;
    }

    private String getAOSText(int langId) {
        String text = "<b>" + Constants.STANDARD_AOS[langId] + "</b>\n";
        int i = 1;
        Set<String> taxTypeSet = new LinkedHashSet<>();
        for (TaxInfoEntity taxInfo : getAOS()) {
            if (langId == 0) {
                taxTypeSet.add(taxInfo.getTaxType());
            } else if (langId == 1) {
                taxTypeSet.add(translate.translate(taxInfo.getTaxType()));
            }
        }
        for (String taxType : taxTypeSet) {
            text += (i + ") " + taxType + "\n");
            i++;
        }
        return text;
    }

    private static int calculateTotalPages(int totalDataItems) {
        int itemsPerPage = 10;
        return (int) Math.ceil((double) totalDataItems / itemsPerPage);
    }
}