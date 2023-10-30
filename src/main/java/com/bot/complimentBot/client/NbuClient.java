package com.bot.complimentBot.client;

import com.bot.complimentBot.exception.ServiceException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class NbuClient {
    @Autowired
    private OkHttpClient okHttpClient;
    @Value("${nbu.currency.rates.xml.url}")
    private String url;

    public String getXml() throws ServiceException {
        var request = new Request.Builder().url(url).build();
        try (var response = okHttpClient.newCall(request).execute()){
            return response.body().string();

        } catch (IOException e){
            throw new ServiceException("Помилка отримання курсів валют",e);
        }

    }
}
