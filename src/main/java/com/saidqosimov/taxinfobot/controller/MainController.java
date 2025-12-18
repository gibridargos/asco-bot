package com.saidqosimov.taxinfobot.controller;

import com.saidqosimov.taxinfobot.config.ApplicationConfiguration;
import com.saidqosimov.taxinfobot.model.CodeMessage;
import com.saidqosimov.taxinfobot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MainController extends TelegramLongPollingBot {
    private final ApplicationConfiguration applicationConfiguration;
    private final GeneralController generalController;
    private final ScheduledService scheduledService;
    private final UserService userService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            Chat chat = message.getChat();

            if (chat.isGroupChat() || chat.isSuperGroupChat()) {
                GetChatMember getChatMember = new GetChatMember();
                getChatMember.setChatId(message.getChatId());
                getChatMember.setUserId(message.getFrom().getId());
                try {
                    ChatMember chatMember = execute(getChatMember);
                    String status = chatMember.getStatus();
                    if ("creator".equals(status) || "administrator".equals(status)) {
                        if(update.getMessage().getText().equals("/start") || update.getMessage().getText().equals("/start@"+applicationConfiguration.getUsername())){
                            userService.save(update.getMessage().getChatId());
                        }
                        /*sendMsg(generalController.handle(update.getMessage()));*/
                    }
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else if (chat.isUserChat()) {
                sendMsg(generalController.handle(update.getMessage()));
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Chat chat = callbackQuery.getMessage().getChat();
/*            if (chat.isGroupChat() || chat.isSuperGroupChat()) {
                GetChatMember getChatMember = new GetChatMember();
                getChatMember.setChatId(callbackQuery.getMessage().getChatId());
                getChatMember.setUserId(callbackQuery.getFrom().getId());
                try {
                    ChatMember chatMember = execute(getChatMember);
                    String status = chatMember.getStatus();
                    if ("creator".equals(status) || "administrator".equals(status)) {
                        sendMsg(generalController.handle(update.getCallbackQuery()));
                    }
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else */if (chat.isUserChat()) {
                sendMsg(generalController.handle(update.getCallbackQuery()));
            }

        }
    }

    @Scheduled(cron = "0 0 10 * * ?", zone = "Asia/Tashkent")
    private void sendDailyNotification() {
        sendMsg(scheduledService.send());
    }

    @Scheduled(cron = "0 0 10 1 * ?", zone = "Asia/Tashkent")
    public void sendMonthlyMessage() {
        sendMsg(scheduledService.sendMonthlyScheduledMessages());
    }

    public synchronized void sendMsg(List<CodeMessage> messageList) {
        for (CodeMessage message : messageList) {
            switch (message.getMessageType()) {
                case SEND_MESSAGE -> {
                    try {
                        execute(message.getSendMessage());
                    } catch (TelegramApiException e) {
                        if (e.getMessage().contains("403")) {
                            System.out.println("Foydalanuvchi botni bloklagan :" + message.getSendMessage().getChatId());
                            userService.deleteAllMyTaxes(Long.parseLong(message.getSendMessage().getChatId()));
                        } else {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                }
                case EDIT_MESSAGE -> {
                    try {
                        execute(message.getEditMessageText());
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case DELETE_MESSAGE -> {
                    try {
                        execute(message.getDeleteMessage());
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                case SEND_PHOTO -> {
                    try {
                        execute(message.getSendPhoto());
                    } catch (TelegramApiException e) {
                        if (e.getMessage().contains("403")) {
                            System.out.println("Foydalanuvchi botni bloklagan :" + message.getSendPhoto().getChatId());
                            userService.deleteAllMyTaxes(Long.parseLong(message.getSendPhoto().getChatId()));
                        } else {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                }
                case SEND_VIDEO -> {
                    try {
                        execute(message.getSendVideo());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
        }
    }


    @Override
    public String getBotUsername() {
        return applicationConfiguration.getUsername();
    }

    @Override
    public String getBotToken() {
        return applicationConfiguration.getToken();
    }
}
