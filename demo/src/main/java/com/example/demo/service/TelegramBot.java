package com.example.demo.service;

import com.example.demo.components.*;
import com.example.demo.files.JsonFiles;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private boolean readMessageStatus = false;
    private boolean readNotificationStatus = false;
    private String adminUserName = "denis_moiseyev11";
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String command = message.getText();
            Long chatId = message.getChatId();
            User user = message.getFrom();


            if (Objects.equals(user.getUserName(), adminUserName)) {
                if (readMessageStatus) {
                    sendMessage(chatId, "Отправлено");
                    //sendToBack
                    readMessageStatus = false;
                } else if (readNotificationStatus) {
                    Notification n = new Notification(message.getText(), UUID.randomUUID().toString(), true);
//                    ArrayList<Components> list = new ArrayList<>();
//                    list.add(n);
                    String json = JsonFiles.createJsonStringFromPojo(n);
                    Server.sendElement(json);
                    sendMessage(chatId, "Отправлено");
                    readNotificationStatus = false;
                }
                switch (command) {
                    case "/start" -> sendAdminPanel(chatId);
                    case "Уведомление" -> {
                        sendMessage(chatId, "Введите текст уведомления");
                        readNotificationStatus = true;
                    }
                    case "Сообщение" -> {
                        sendMessage(chatId, "Введите текст сообщения");
                        readMessageStatus = true;
                    }
                    case "Выбор" -> {
                        ArrayList<String> options = new ArrayList<>();
                        options.add("Спать");
                        options.add("Есть");
                        sendCheckbox(chatId, "Что больше любите?", options, false);
                        //sendToBack
                        sendMessage(chatId, "Отправлено");
                    }
                    case "Оценка" -> {
                        //sendToBack
                        sendMessage(chatId, "Отправлено");
                    }
                }
            } else {
                if (readMessageStatus) {
                    sendMessage(chatId, "Сообщение было отправлено");
                    readMessageStatus = false;
                }
                switch (command) {
                    case "/start" -> sendWelcomeMessage(chatId);
                    case "Обновить список" -> sendComponents(chatId);
                }
            }


        }
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();
            Long chatId = callbackQuery.getMessage().getChatId();
            int messageId = callbackQuery.getMessage().getMessageId();

            // Дополнительные действия, основанные на callbackData
            switch (callbackData) {
                case "closeNotification":
                    deleteMessage(chatId, messageId);
                    break;
                case "setReply":
                    sendMessage(chatId, "Введите ответ на сообщение");
                    readMessageStatus = true;
                    break;
                case "messageIgnored":
                    deleteMessage(chatId, messageId);
                    break;
                case "btnGradeClicked":
                    clearMsgBtns(chatId, messageId, "Спасибо за оценку!");
                    break;
            }

            // Отправить подтверждение обработки callbackQuery
            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
            answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
            try {
                execute(answerCallbackQuery);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void clearMsgBtns(Long chatId, Integer messageId, String text) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(text);
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        try {
            execute(editMessageText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendAdminPanel(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Че добавить?");

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setOneTimeKeyboard(false);
        markup.setSelective(true);
        markup.setResizeKeyboard(true);

        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton btn = new KeyboardButton("Уведомление");
        KeyboardButton btn1 = new KeyboardButton("Сообщение");
        KeyboardButton btn2 = new KeyboardButton("Выбор");
        KeyboardButton btn3 = new KeyboardButton("Оценка");
        row1.add(btn);
        row1.add(btn1);
        row1.add(btn2);
        row1.add(btn3);

        markup.setKeyboard(List.of(row1));
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteMessage(Long chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), messageId);
        try {
            execute(deleteMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendComponents(Long chatId) throws JsonProcessingException {
        ArrayList<Components> arrayList = Server.getComponentsList();

        for (Components c : arrayList) {
            if (c instanceof Notification) {
                sendNotification(chatId, c.getText());
            } else if (c instanceof Checkbox) {
                sendCheckbox(chatId, c.getText(), ((Checkbox) c).getBox(), ((Checkbox) c).isMultiply());
            } else if (c instanceof com.example.demo.components.Message) {
                sendReplyToMessage(chatId, c.getText(), ((com.example.demo.components.Message) c).getSender());
            } else if (c instanceof Rate) {
                sendRate(chatId, c.getText(), ((Rate) c).getCountOfButton());
            } else if (c instanceof ElementsList) {
                sendElementsList(chatId, ((ElementsList) c).getElements());
            }
        }
    }

    private void sendElementsList(Long chatId, ArrayList<Components> box) {
        for (Components c : box) {
            if (c instanceof Notification) {
                sendNotification(chatId, c.getText());
            } else if (c instanceof Checkbox) {
                sendCheckbox(chatId, c.getText(), ((Checkbox) c).getBox(), ((Checkbox) c).isMultiply());
            } else if (c instanceof com.example.demo.components.Message) {
                sendReplyToMessage(chatId, c.getText(), ((com.example.demo.components.Message) c).getSender());
            } else if (c instanceof Rate) {
                sendRate(chatId, c.getText(), ((Rate) c).getCountOfButton());
            }
        }
    }

    private void sendRate(Long chatId, String text, int btnCount) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        int rows = btnCount / 5;
        int grade = 1;
        for (int i = 0; i < rows; i++) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                InlineKeyboardButton btn = new InlineKeyboardButton(Integer.toString(grade));
                btn.setCallbackData("btnGradeClicked");
                row.add(btn);
                grade++;
            }
            keyboard.add(row);
        }
        System.out.println(keyboard);

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendReplyToMessage(Long chatId, String messageText, String sender) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setParseMode("HTML");
        message.setText("Вам пришло сообщение от " + sender + ":\n<i>" + messageText + "</i>");

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> inlineRow1 = new ArrayList<>();
        InlineKeyboardButton client1 = new InlineKeyboardButton("Ответить");
        client1.setCallbackData("setReply");
        InlineKeyboardButton client2 = new InlineKeyboardButton("Игнорировать");
        client2.setCallbackData("messageIgnored");
        inlineRow1.add(client1);
        inlineRow1.add(client2);

        keyboard.add(inlineRow1);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendCheckbox(Long chatId, String text, ArrayList<String> box, boolean isMultiply) {
        SendPoll poll = new SendPoll();
        poll.setChatId(chatId);
        poll.setQuestion(text);
        poll.setAllowMultipleAnswers(isMultiply);
        poll.setOptions(box);
        try {
            execute(poll);  //Regularly, if new users have voted in polls available to the user, they will receive an updateMessagePoll, with updated pollResults.
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendNotification(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> inlineRow1 = new ArrayList<>();
        InlineKeyboardButton client1 = new InlineKeyboardButton("OK");
        client1.setCallbackData("closeNotification");
        inlineRow1.add(client1);

        keyboard.add(inlineRow1);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendWelcomeMessage(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Тут будет отображаться список ваших дел");

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setOneTimeKeyboard(false);
        markup.setSelective(true);
        markup.setResizeKeyboard(true);

        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton btn = new KeyboardButton("Обновить список");
        row1.add(btn);

        markup.setKeyboard(List.of(row1));
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "quizBot";
    }

    @Override
    public String getBotToken() {
        return "5855308874:AAEf5yXNTKcZuPEK9_3iwz3Ut2TJ-PaFL8I";
    }
}
