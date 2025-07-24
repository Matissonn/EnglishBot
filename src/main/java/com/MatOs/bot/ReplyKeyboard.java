package com.MatOs.bot;

import com.MatOs.Main;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReplyKeyboard {
    public static void setReplyKeyboardMarkup(SendMessage sendMessage, String userName) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup();

        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow firstRow = new KeyboardRow();

        if(Objects.equals(userName, Main.dotenv.get("OWNER_USERNAME")) ||
                Objects.equals(userName, Main.dotenv.get("DEVELOPER_USERNAME"))){
            firstRow.add(new KeyboardButton("ДОБАВИТЬ участника"));
            firstRow.add(new KeyboardButton("УДАЛИТЬ участника"));
            firstRow.add(new KeyboardButton("Задания"));
        }else{
            firstRow.add(new KeyboardButton("Задания"));
        }

        keyboard.add(firstRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
    }
}