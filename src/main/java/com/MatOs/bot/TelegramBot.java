package com.MatOs.bot;

import com.MatOs.Main;
import com.MatOs.Task;
import com.MatOs.TaskManager;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class TelegramBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return Main.dotenv.get("USERNAME");
    }

    @Override
    public String getBotToken() {
        return Main.dotenv.get("BOT_TOKEN");
    }

    String botText;
    String userText;

    String[] options = new String[]{" ", "addName", "deleteName", "typeIn"};
    String option;
    String addUserName;
    String deleteUserName;
    public static Task task;
    int tries;
    ArrayList<String> ops;

    @Override
    public void onUpdateReceived(Update update) {
        Message message;

        if(update.hasMessage()){
            message = update.getMessage();
            messageHandler(message, update);
        }else if(update.hasCallbackQuery()){
            callbackHandler(update);
        }
    }

    public void messageHandler(Message message, Update update) {
        userText = message.getText();

        if(ProfileHandler.findUser(message.getFrom().getUserName()) == null && (!Objects.equals(message.getFrom().getUserName(), Main.dotenv.get("OWNER_USERNAME")) &&
                !Objects.equals(message.getFrom().getUserName(), Main.dotenv.get("DEVELOPER_USERNAME")))){
            botText = "Тебя нету в базе!!!";
            sendMsg(botText, message.getChatId(), message.getFrom().getUserName());
        }else if(Objects.equals(message.getFrom().getUserName(), Main.dotenv.get("OWNER_USERNAME")) ||
                Objects.equals(message.getFrom().getUserName(), Main.dotenv.get("DEVELOPER_USERNAME"))){

            if(Objects.equals(userText, "/cancel")){
                botText = "Текущая задача прервана";
                sendMsg(botText, message.getChatId(), message.getFrom().getUserName());
                userText = "";
                option = options[0];
                addUserName = "";
                deleteUserName = "";
            }

            if(Objects.equals(userText, "ДОБАВИТЬ участника")){

                botText = "Хорошо! Добваляем нового участника курса.\nНапиши мне его имя пользователя";
                sendMsg(botText, message.getChatId(), message.getFrom().getUserName());
                option = options[1];

            }else if(Objects.equals(option, options[1])){

                addUserName = userText;
                if(ProfileHandler.findUser(addUserName) != null){
                    botText = "Этот участник уже есть на курсе";
                    sendMsg(botText, message.getChatId(), message.getFrom().getUserName());
                }else{
                    ProfileHandler.insert(addUserName);
                }

                addUserName = "";
                botText = "Отлично! Участник ДОБАВЛЕН";
                sendMsg(botText, message.getChatId(), message.getFrom().getUserName());
                option = options[0];
            }else if(Objects.equals(userText, "Задания")){
                task = TaskManager.getTask(message.getFrom().getUserName());
                tries = 0;

                assert task != null;
                if(Objects.equals(task.getType(), "select")){
                    botText = "Задание " + task.getId() + "\nПрочитайте текст. Выберите один из предложенных вариантов ответа:\n\n" + task.getText();
                }else if(Objects.equals(task.getType(), "typeIn")){
                    botText = "Задание " + task.getId() + "\nОт приведенных в скобках слов образуйте ОДНОКОРЕННЫЕ слова таким образом, " +
                            "чтобы они грамматически и лексически соответствовали содержанию предложения. Помните, что заданную форму слова необходимо изменить. " +
                            "Введите ответ ЗАГЛАВНЫМИ БУКВАМИ:\n\n" + task.getText();
                    option = options[3];
                }

                sendMsg(botText, message.getChatId(), message.getFrom().getUserName());
            }else if(Objects.equals(option, options[3])){
                if(Objects.equals(userText, task.getOptions().getFirst())){
                    sendMsg("Отлично! Задание выполнено правильно!\n" +
                            task.getText().replace("...", "\"" + "<i><b>" + userText + "</b></i>" + "\""), message.getChatId(), message.getFrom().getUserName());
                    option = options[0];
                    ProfileHandler.updateCompletedTasks(message.getFrom().getUserName(), 1);
                }else{
                    if(tries == 0){
                        sendMsg("Неправильно, попробуй еще раз\n" +
                                task.getText(), message.getChatId(), message.getFrom().getUserName());
                        tries++;
                    }else if(tries == 1){
                        sendMsg("Задание выполнено неправильно, показываю правильный ответ:\n" +
                                task.getText().replace("...", "\"" + "<i><b>" + task.getOptions().getFirst() + "</b></i>" +"\""), message.getChatId(), message.getFrom().getUserName());
                        ProfileHandler.updateCompletedTasks(update.getCallbackQuery().getFrom().getUserName(), 1);
                    }
                }

            }else if(Objects.equals(userText, "УДАЛИТЬ участника")) {

                botText = "Понял! Удаляем.\nНапиши мне его имя пользователя";
                sendMsg(botText, message.getChatId(), message.getFrom().getUserName());
                option = options[2];
            }else if(Objects.equals(option, options[2])) {

                deleteUserName = userText;
                if (ProfileHandler.findUser(deleteUserName) == null) {
                    botText = "Такого участника нет!";
                    sendMsg(botText, message.getChatId(), message.getFrom().getUserName());
                } else {
                    ProfileHandler.delete(deleteUserName);
                    deleteUserName = "";
                    botText = "Все! Участник УДАЛЕН";
                    sendMsg(botText, message.getChatId(), message.getFrom().getUserName());
                    option = options[0];
                }
            }
        }else if(ProfileHandler.findUser(message.getFrom().getUserName()) != null){
            botText = "Привет, " + message.getFrom().getUserName() + "!";
            sendMsg(botText, message.getChatId(), message.getFrom().getUserName());
            //сюда задания
        }

        String name = message.getFrom().getUserName();

        System.out.println("Chat ID: " + message.getChatId() + " Mesage: \"" + userText + "\"-[" + name + "]");
    }

    public void callbackHandler(Update update){
        String callBack = update.getCallbackQuery().getData();
        long messageId = update.getCallbackQuery().getMessage().getMessageId();

        if(Objects.equals(callBack, task.getOptions().getFirst())){
            editMsg(update.getCallbackQuery().getId(),
                    "Отлично! Задание выполнено правильно!\n" + task.getText().replace("...", "\"" + "<i><b>" + callBack + "</b></i>" + "\""),
                    update.getCallbackQuery().getMessage().getChatId(), messageId, callBack);
            ProfileHandler.updateCompletedTasks(update.getCallbackQuery().getFrom().getUserName(), 1);
        }else if(!Objects.equals(callBack, task.getOptions().getFirst()) && Objects.equals(callBack, "/")){
            if(tries == 0){
                editMsg(update.getCallbackQuery().getId(),
                        "Неправильно, попробуй еще раз\n" + task.getText(),
                        update.getCallbackQuery().getMessage().getChatId(), messageId, callBack);
                tries++;
            }else if(tries == 1){
                editMsg(update.getCallbackQuery().getId(),
                        "Задание выполнено неправильно, показываю правильный ответ:\n" +
                                task.getText().replace("...", "\"" + "<i><b>" + task.getOptions().getFirst() + "</b></i>" +  "\""),
                        update.getCallbackQuery().getMessage().getChatId(), messageId, task.getOptions().getFirst());
                ProfileHandler.updateCompletedTasks(update.getCallbackQuery().getFrom().getUserName(), 1);
            }
        }
    }

    public void sendMsg(String text, long chatId, String userName){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setParseMode("HTML");
        ReplyKeyboard.setReplyKeyboardMarkup(sendMessage, userName);
        if(task == null || task.getOptions().isEmpty()){
            System.out.println("No task");
        }
        if(text.startsWith("Задание") && task != null && Objects.equals(task.getType(), "select")){
            ops = new ArrayList<>(task.getOptions());
            Collections.shuffle(ops);
            InlineKeyboard.setInlineKeyboardMarkup(sendMessage, null, ops, "");
        }
        try {
            execute(sendMessage);

        } catch (TelegramApiException e) {
            System.out.println("Failed to send message: " + e.getMessage());
        }
    }

    public void editMsg(String callBackQueryId, String text, long chatId, long messageId, String callback){
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callBackQueryId);
        answerCallbackQuery.setShowAlert(false);
        answerCallbackQuery.setText(text);

        EditMessageText newMessage = new EditMessageText();
        newMessage.setChatId(String.valueOf(chatId));
        newMessage.setMessageId((int) messageId);
        newMessage.setText(text);
        newMessage.setParseMode("HTML");;

        if(task != null) {
            InlineKeyboard.setInlineKeyboardMarkup(null, newMessage, ops, callback);
        }

        try {
            execute(answerCallbackQuery);
            execute(newMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}