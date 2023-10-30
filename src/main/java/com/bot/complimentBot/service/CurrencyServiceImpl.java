package com.bot.complimentBot.service;

import com.bot.complimentBot.client.NbuClient;
import com.bot.complimentBot.exception.ServiceException;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

@Service
@Component
public class CurrencyServiceImpl implements CurrencyService{
    public static final String XPATH_USD = "/exchange/currency[25]/rate";
    public static final String XPATH_EUR = "/exchange/currency[32]/rate";
    @Autowired
    private NbuClient nbuClient;
    @Override
    public String getUSD() throws ServiceException {
        var xml = nbuClient.getXml();
        return extractFromXml(xml,XPATH_USD);
    }

    @Override
    public String getEUR() throws ServiceException {
        var xml = nbuClient.getXml();
        return extractFromXml(xml,XPATH_EUR);
    }

    private static String extractFromXml (String xml, String xpathExpression) throws ServiceException {
        var source = new InputSource(new StringReader(xml));

        try {
            var xpath = XPathFactory.newInstance().newXPath();
//            var document = (Document) xpath.evaluate("/",source, XPathConstants.NODE);
            return xpath.evaluate(xpathExpression,source);
        } catch (XPathExpressionException e){
            throw new ServiceException("Не вдалось розпарсити XML",e);
        }

    }
}
