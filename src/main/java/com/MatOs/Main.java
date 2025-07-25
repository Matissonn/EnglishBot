package com.MatOs;

import com.MatOs.bot.TelegramBot;
import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static Dotenv dotenv = Dotenv.load();

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TelegramBot());
    }
}



//-----------В ПЕРВУЮ ОЧЕРЕДЬ----------//
//вторая попытка, после неё показать правильный ответ   <--- готово
//эмодзи inlineKeyboardButton ✅❌   <--- готово
//добавить объяснение на каждый правильный ответ   (нужны пояснения)
//важные надписи выделяются жирным курсивом   <--- готово
//цельный текст с подстановками ответа одним сообщением, (А1) (А2) ... и ответы на них - другое сообщение с кнопками   (нужны задания)
//добавить приветственное сообщение с информацией о курсе, боте и соц. сетях
//добавить функции для команд из менюs (info - информация о курсе и о боте; contacts; profile - кол-во выполненных заданий, общий счет, время добавления на курс;)



//-----------ИСПРАВИТЬ----------//
//cancel не может отменять выполнение задания
//кнопка "Задания" пропадает, если задание выполняется, появляется кнопка "Завершить"
//после выполненного задания появляется другое, выполнение заданий завершается кнопкой "Завершить"
//вернуть кнопки на свои места (для владельцев и участников разные кнопки)



//-----------ИДЕИ----------//
//перевод первичных баллов в тестовые
//