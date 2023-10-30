package com.bot.complimentBot.service;

import com.bot.complimentBot.exception.ServiceException;

public interface CurrencyService {

    String getUSD() throws ServiceException;
    String getEUR() throws ServiceException;
}
