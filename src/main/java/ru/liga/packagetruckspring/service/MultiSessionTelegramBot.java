package ru.liga.packagetruckspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.GetMyCommands;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.menubutton.SetChatMenuButton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeChat;
import org.telegram.telegrambots.meta.api.objects.menubutton.MenuButtonCommands;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.packagetruckspring.exception.CustomException;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MultiSessionTelegramBot extends TelegramLongPollingBot {

    private ThreadLocal<Update> updateEvent = new ThreadLocal<>();

    public MultiSessionTelegramBot(String botToken) {
        super(botToken);
    }

    @Override
    public String getBotUsername() {
        return "RestartBot";
    }

    @Override
    public final void onUpdateReceived(Update updateEvent) {
        try {
            this.updateEvent.set(updateEvent);
            onUpdateEventReceived(this.updateEvent.get());
        } catch (Exception e) {
            throw new CustomException("Ошибка в Telegram Bot", e);
        }
    }

    public void onUpdateEventReceived(Update updateEvent) throws CustomException {
    }

    /**
     * Метод возвращает ID текущего Telegram-чата
     */
    public Long getCurrentChatId() {
        if (updateEvent.get().hasMessage()) {
            return updateEvent.get().getMessage().getFrom().getId();
        }

        if (updateEvent.get().hasCallbackQuery()) {
            return updateEvent.get().getCallbackQuery().getFrom().getId();
        }

        return null;
    }

    /**
     * Метод возвращает текст из последнего сообщения Telegram-чата
     */
    public String getMessageText() {
        return updateEvent.get().hasMessage() ? updateEvent.get().getMessage().getText() : "";
    }

    public Document getMessageDocument() {
        return updateEvent.get().getMessage().getDocument();
    }

    public boolean isMessageCommand() {
        return updateEvent.get().hasMessage() && updateEvent.get().getMessage().isCommand();
    }

    public boolean isMessageDocument() {
        return updateEvent.get().hasMessage() && updateEvent.get().getMessage().hasDocument();
    }

    /**
     * Метод отправляет в чат ТЕКСТ (текстовое сообщение).
     * Поддерживается markdown-разметка.
     */
    public Message sendTextMessage(String text) {
        SendMessage command = createApiSendMessageCommand(text);
        return executeTelegramApiMethod(command);
    }

    /**
     * Метод изменяет ТЕКСТ в уже отправленном сообщении.
     */
    public void updateTextMessage(Message message, String text) {
        EditMessageText command = new EditMessageText();
        command.setChatId(message.getChatId());
        command.setMessageId(message.getMessageId());
        command.setText(text);
        executeTelegramApiMethod(command);
    }

    public static String loadMessage(String name) {
        try {
            var is = ClassLoader.getSystemResourceAsStream("messages/" + name + ".txt");
            return new String(is.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Can't load message!");
        }
    }

    public void showMainMenu(String... commands) {
        ArrayList<BotCommand> list = new ArrayList<BotCommand>();

        //convert strings to command list
        for (int i = 0; i < commands.length; i += 2) {
            String description = commands[i];
            String key = commands[i + 1];

            if (key.startsWith("/")) //remove first /
                key = key.substring(1);

            BotCommand bc = new BotCommand(key, description);
            list.add(bc);
        }

        //get commands list
        var chatId = getCurrentChatId();
        GetMyCommands gmcs = new GetMyCommands();
        gmcs.setScope(BotCommandScopeChat.builder().chatId(chatId).build());
        ArrayList<BotCommand> oldCommands = executeTelegramApiMethod(gmcs);

        //ignore commands change for same command list
        if (oldCommands.equals(list))
            return;

        //set commands list
        SetMyCommands cmds = new SetMyCommands();
        cmds.setCommands(list);
        cmds.setScope(BotCommandScopeChat.builder().chatId(chatId).build());
        executeTelegramApiMethod(cmds);

        //show menu button
        var ex = new SetChatMenuButton();
        ex.setChatId(chatId);
        ex.setMenuButton(MenuButtonCommands.builder().build());
        executeTelegramApiMethod(ex);
    }

    private SendMessage createApiSendMessageCommand(String text) {
        SendMessage message = new SendMessage();
        message.setText(new String(text.getBytes(), StandardCharsets.UTF_8));
        message.setParseMode("markdown");
        message.setChatId(getCurrentChatId());
        return message;
    }

    private <T extends Serializable, Method extends BotApiMethod<T>> T executeTelegramApiMethod(Method method) {
        try {
            return super.sendApiMethod(method);
        } catch (TelegramApiException e) {
            throw new CustomException("Ошибка в методе Telegram", e);
        }
    }

}
