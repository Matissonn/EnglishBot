package com.MatOs.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InlineKeyboard {
    public static List<String> ops;

    public static void setInlineKeyboardMarkup(SendMessage sendMessage, EditMessageText editMessage, ArrayList<String> ops, String callback) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        if(sendMessage != null)
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        else editMessage.setReplyMarkup(inlineKeyboardMarkup);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for(int i = 0; i < ops.size(); i++) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            if(Objects.equals(ops.get(i), TelegramBot.task.getOptions().getFirst()) && Objects.equals(callback, TelegramBot.task.getOptions().getFirst())){
                button.setText("✅" + ops.get(i));
                button.setCallbackData(ops.get(i));
            }else if(Objects.equals(ops.get(i), callback) && !Objects.equals(callback, TelegramBot.task.getOptions().getFirst())){
                button.setText("❌" + ops.get(i));
                button.setCallbackData(ops.get(i));
            }else if(Objects.equals(callback, "/")){
                button.setCallbackData("/");
            }else if(Objects.equals(callback, "")){
                button.setText(ops.get(i));
                button.setCallbackData(ops.get(i));
            }

            row.add(button);

            keyboard.add(row);
        }

        inlineKeyboardMarkup.setKeyboard(keyboard);
    }
}