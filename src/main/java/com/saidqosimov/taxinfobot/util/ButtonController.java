package com.saidqosimov.taxinfobot.util;

import com.saidqosimov.taxinfobot.domain.TaxInfoDTO;
import com.saidqosimov.taxinfobot.domain.UserDTO;
import com.saidqosimov.taxinfobot.enums.UserStep;
import com.saidqosimov.taxinfobot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ButtonController {
    private final UserService userService;

    public ReplyKeyboardMarkup getStartButtons(int langId) {
        return KeyboardButtonUtil.keyboard(KeyboardButtonUtil.collection(
                        KeyboardButtonUtil.row(KeyboardButtonUtil.button(Constants.TAX_LIST[langId])
                        ), KeyboardButtonUtil.row(
                                KeyboardButtonUtil.button(Constants.STANDARD_QQS[langId]),
                                KeyboardButtonUtil.button(Constants.STANDARD_AOS[langId])
                        ), KeyboardButtonUtil.row(
                                KeyboardButtonUtil.button(Constants.INFO[langId])
                        ), KeyboardButtonUtil.row(
                                KeyboardButtonUtil.button(Constants.CONTACT_US[langId]),
                                KeyboardButtonUtil.button(Constants.SETTINGS[langId])
                        )
                )
        );
    }

    public ReplyKeyboardMarkup getMainButtons(int langId) {
        return KeyboardButtonUtil.keyboard(KeyboardButtonUtil.collection(
                        KeyboardButtonUtil.row(
                                KeyboardButtonUtil.button(Constants.INFO[langId]),
                                KeyboardButtonUtil.button(Constants.MY_TAXES[langId])
                        ), KeyboardButtonUtil.row(
                                KeyboardButtonUtil.button(Constants.CONTACT_US[langId]),
                                KeyboardButtonUtil.button(Constants.SETTINGS[langId])
                        )
                )
        );
    }

    public ReplyKeyboardMarkup getLanguageReplyButtons() {
        return KeyboardButtonUtil.keyboard(
                KeyboardButtonUtil.collection(
                        KeyboardButtonUtil.row(
                                KeyboardButtonUtil.button(Constants.LANG_UZ),
                                KeyboardButtonUtil.button(Constants.LANG_RU)
                        )
                )
        );
    }
    public InlineKeyboardMarkup getTheDayBeforeNotification(int langId, Long chatId) {
        Boolean b = userService.getCurrentUser(chatId).getTheDayBefore();
        if (b) {
            return InlineKeyboardUtil.keyboard(
                    InlineKeyboardUtil.collection(
                            InlineKeyboardUtil.row(
                                    InlineKeyboardUtil.button(Constants.DISABLE[langId], Constants.THE_DAY_BEFORE_OFF_NOTIFICATION)
                            )
                    )
            );
        } else {
            return InlineKeyboardUtil.keyboard(
                    InlineKeyboardUtil.collection(
                            InlineKeyboardUtil.row(
                                    InlineKeyboardUtil.button(Constants.ENABLE[langId], Constants.THE_DAY_BEFORE_ON_NOTIFICATION)
                            )
                    )
            );
        }
    }
    public InlineKeyboardMarkup getThreeDaysAgoNotification(int langId, Long chatId) {
        Boolean b = userService.getCurrentUser(chatId).getThreeDaysAgo();
        if (b) {
            return InlineKeyboardUtil.keyboard(
                    InlineKeyboardUtil.collection(
                            InlineKeyboardUtil.row(
                                    InlineKeyboardUtil.button(Constants.DISABLE[langId], Constants.THREE_DAYS_AGO_OFF_NOTIFICATION)
                            )
                    )
            );
        } else {
            return InlineKeyboardUtil.keyboard(
                    InlineKeyboardUtil.collection(
                            InlineKeyboardUtil.row(
                                    InlineKeyboardUtil.button(Constants.ENABLE[langId], Constants.THREE_DAYS_AGO_ON_NOTIFICATION)
                            )
                    )
            );
        }
    }

    public InlineKeyboardMarkup getTwoDaysAgoNotification(int langId, Long chatId) {
        Boolean b = userService.getCurrentUser(chatId).getTwoDaysAgo();
        if (b) {
            return InlineKeyboardUtil.keyboard(
                    InlineKeyboardUtil.collection(
                            InlineKeyboardUtil.row(
                                    InlineKeyboardUtil.button(Constants.DISABLE[langId], Constants.TWO_DAYS_AGO_OFF_NOTIFICATION)
                            )
                    )
            );
        } else {
            return InlineKeyboardUtil.keyboard(
                    InlineKeyboardUtil.collection(
                            InlineKeyboardUtil.row(
                                    InlineKeyboardUtil.button(Constants.ENABLE[langId], Constants.TWO_DAYS_AGO_ON_NOTIFICATION)
                            )
                    )
            );
        }

    }

    public InlineKeyboardMarkup getRequestClearData(int langId) {
        return InlineKeyboardUtil.keyboard(
                InlineKeyboardUtil.collection(
                        InlineKeyboardUtil.row(
                                InlineKeyboardUtil.button(Constants.ALLOW[langId], Constants.CLEAR_ALL_TAXES),
                                InlineKeyboardUtil.button(Constants.DENY[langId], Constants.DENY_ACCESS)
                        )
                )
        );
    }




    public InlineKeyboardMarkup createInlineKeyboardMarkup(int currentPage, List<String> taxTypes, long chatId, int langId, List<TaxInfoDTO> allTaxes) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new LinkedList<>();
        List<InlineKeyboardButton> paginationRow = new LinkedList<>();

        if (currentPage > 0) {
            InlineKeyboardButton buttonPrev = new InlineKeyboardButton();
            buttonPrev.setText(Constants.BACK_BUTTON);
            buttonPrev.setCallbackData(Constants.PAGE + (currentPage - 1));
            paginationRow.add(buttonPrev);
        }
        InlineKeyboardButton buttonNext = new InlineKeyboardButton();
        if (currentPage < calculateTotalPages(taxTypes.size()) - 1) {
            buttonNext.setText(Constants.NEXT_BUTTON);
            buttonNext.setCallbackData(Constants.PAGE + (currentPage + 1));
            paginationRow.add(buttonNext);
        }
        int startIndex = currentPage * 10;
        int endIndex = Math.min((currentPage + 1) * 10, taxTypes.size());

        List<InlineKeyboardButton> dataSelectionRow = new LinkedList<>();
        List<InlineKeyboardButton> dataSelectionRow2 = new LinkedList<>();
        for (int i = (startIndex - currentPage * 10); i < (endIndex - currentPage * 10); i++) {
            if (i < 5) {
                InlineKeyboardButton selectDataButton = new InlineKeyboardButton();
                selectDataButton.setText(String.valueOf(i + 1));
                selectDataButton.setCallbackData(String.valueOf(i + currentPage * 10));
                dataSelectionRow.add(selectDataButton);
            }
            if (i >= 5) {
                InlineKeyboardButton selectDataButton = new InlineKeyboardButton();
                selectDataButton.setText(String.valueOf(i + 1));
                selectDataButton.setCallbackData(String.valueOf(i + currentPage * 10));
                dataSelectionRow2.add(selectDataButton);
            }
        }

        keyboard.add(dataSelectionRow);
        keyboard.add(dataSelectionRow2);
        keyboard.add(paginationRow);

        UserDTO currentUser = userService.getCurrentUser(chatId);
        if (currentUser.getStep().equals(UserStep.SELECT_TAX) /*|| currentUser.getStep().equals(UserStep.SELECT_TAX_AOS) || currentUser.getStep().equals(UserStep.SELECT_TAX_QQS)*/) {
            List<InlineKeyboardButton> cacheButton = new LinkedList<>();

            InlineKeyboardButton cacheSave = new InlineKeyboardButton();
            cacheSave.setText(Constants.SAVE[langId]);
            cacheSave.setCallbackData(Constants.SAVE_CACHE);

            InlineKeyboardButton cacheClear = new InlineKeyboardButton();
            cacheClear.setText(Constants.CLEAR[langId]);
            cacheClear.setCallbackData(Constants.CLEAR_CACHE);

            cacheButton.add(cacheClear);
            cacheButton.add(cacheSave);
            keyboard.add(cacheButton);

        } else if (currentUser.getStep().equals(UserStep.SHOW_MY_TAX)) {
            List<InlineKeyboardButton> cacheButton = new LinkedList<>();
            List<InlineKeyboardButton> clearButton = new LinkedList<>();

            InlineKeyboardButton cacheSave = new InlineKeyboardButton();
            cacheSave.setText(Constants.DELETE_SELECTED_TAXES_BUTTON[langId]);
            cacheSave.setCallbackData(Constants.DELETE_SELECTED_TAXES);

            InlineKeyboardButton cacheClear = new InlineKeyboardButton();
            cacheClear.setText(Constants.CLEAR[langId]);
            cacheClear.setCallbackData(Constants.CLEAR_CACHE);

/*            InlineKeyboardButton deleteAllTaxes = new InlineKeyboardButton();
            deleteAllTaxes.setText(Constants.CLEAR_ALL_TAXES_BUTTON[langId]);
            deleteAllTaxes.setCallbackData(Constants.CLEAR_ALL_TAXES_REQUEST);*/

            cacheButton.add(cacheClear);
            cacheButton.add(cacheSave);
            //clearButton.add(deleteAllTaxes);
            keyboard.add(cacheButton);
            keyboard.add(clearButton);
        }
        markup.setKeyboard(keyboard);
        return markup;
    }

    private static int calculateTotalPages(int totalDataItems) {
        int itemsPerPage = 10;
        return (int) Math.ceil((double) totalDataItems / itemsPerPage);
    }

    public ReplyKeyboard getSettingButtons(int langId) {
        return KeyboardButtonUtil.keyboard(KeyboardButtonUtil.collection(
                        KeyboardButtonUtil.row(
                                KeyboardButtonUtil.button(Constants.BACK_TO_MAIN_MENU[langId])
                        ), KeyboardButtonUtil.row(
                                KeyboardButtonUtil.button(Constants.ADD_TAXES_BUTTON[langId]),
                                KeyboardButtonUtil.button(Constants.EDIT_NOTIFICATION_DATE[langId])
                        ), KeyboardButtonUtil.row(
                                KeyboardButtonUtil.button(Constants.USER_MANUAL[langId]),
                                KeyboardButtonUtil.button(Constants.CHANGE_LANGUAGE[langId])
                        ),
                        KeyboardButtonUtil.row(
                                KeyboardButtonUtil.button(Constants.CLEAR_ALL_TAXES_BUTTON[langId])
                        )
                )
        );
    }

    public InlineKeyboardMarkup saveNDSandAOS(int langId) {
        return InlineKeyboardUtil.keyboard(
                InlineKeyboardUtil.collection(
                        InlineKeyboardUtil.row(
                                InlineKeyboardUtil.button(Constants.SAVE[langId],Constants.SAVE_CACHE),
                                InlineKeyboardUtil.button(Constants.DENY[langId], Constants.DENY_ACCESS)
                        )
                )
        );
    }

/*    public ReplyKeyboard getSelectTaxesButton(int langId) {
        return KeyboardButtonUtil.keyboard(KeyboardButtonUtil.collection(
                        KeyboardButtonUtil.row(
                                KeyboardButtonUtil.button(Constants.TAX_LIST[langId])
                        ), KeyboardButtonUtil.row(
                                KeyboardButtonUtil.button(Constants.STANDARD_AOS[langId]),
                                KeyboardButtonUtil.button(Constants.STANDARD_QQS[langId])
                        ), KeyboardButtonUtil.row(
                                KeyboardButtonUtil.button(Constants.BACK_TO_MAIN_MENU[langId])
                        )
                )
        );
    }*/
}
