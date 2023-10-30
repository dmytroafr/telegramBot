package com.bot.complimentBot.bot;

import com.bot.complimentBot.exception.ServiceException;
import com.bot.complimentBot.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;


@Component
public class ComplimentBot extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(ComplimentBot.class);
    private static final String START = "/start";
    private static final String USD = "/usd";
    private static final String EUR = "/eur";
    private static final String HELP = "/help";
    @Autowired
    private CurrencyService currencyService;

    public ComplimentBot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        var message = update.getMessage().getText();
        logger.info("Цей пан відправив нам повідомлення - "+update.getMessage().getFrom().getFirstName());
        var chatId = update.getMessage().getChatId();
        switch (message) {
            case START -> {
                String username = update.getMessage().getFrom().getFirstName();
                startCommand(chatId, username);
            }
            case USD -> usdCommand(chatId);
            case EUR -> eurCommand(chatId);
            case HELP -> {
                String username = update.getMessage().getFrom().getFirstName();
                helpCommand(chatId, username);
            }
            default -> unknowingCommand(chatId);
        }
    }

    @Override
    public String getBotUsername() {
        return "Compliment Bot";
    }

    private void sendMessage(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Не змогли відправити повідомлення", e);
        }
    }

    private void startCommand(Long chatId, String username) {
        var text = "Бонжур пане, %s\n" +
                "Для початку офіційні курси валют НБУ (не готівка, і навіть не міжбанк)\n" +
                "/usd - подивитись курс доллар на сьогодні\n" +
                "/eur - подивитись курс євро на сьогодні\n" +
                "Ще є команда\n" +
                "/help - допоможу, чим зможу";
        var formattedText = String.format(text, username);
        sendMessage(chatId, formattedText);
    }

    private void usdCommand(Long chatId) {
        String formattedText;
        try {
            var usd = currencyService.getUSD();
            var text = "Курс долару на %s складає %s грн";
            formattedText = String.format(text, LocalDate.now(), usd);
        } catch (ServiceException e) {
            logger.error("Помилка отримання курсу долара");
            formattedText = "Не вдалося отримати курс, йдіть нахуй";
        }
        sendMessage(chatId, formattedText);
    }

    private void eurCommand(Long chatId) {
        String formattedText;
        try {
            var eur = currencyService.getEUR();
            var text = "Курс євро на %s складає %s грн";
            formattedText = String.format(text, LocalDate.now(), eur);
        } catch (ServiceException e) {
            logger.error("Помилка отримання курсу євро");
            formattedText = "Не вдалося отримати курс, йдіть нахуй";
        }
        sendMessage(chatId, formattedText);
    }

    private void helpCommand(Long chatId, String username) {
        var text = """
                Послухай, %s, я вмію не багато
                /usd - подивитись курс доллар на сьогодні
                /eur - подивитись курс євро на сьогодні
                і все.""";
        var formattedText = String.format(text, username);
        sendMessage(chatId, formattedText);
    }

    private void unknowingCommand(Long chatId) {
        var text = "Не розумію, що ти там кажеш";
        sendMessage(chatId, text);
    }
}
