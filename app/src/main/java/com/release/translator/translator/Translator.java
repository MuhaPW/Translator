package com.release.translator.translator;

import android.net.Uri;
import android.util.Log;

import com.release.translator.language.Language;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Класс переводит текст с одного языка на другую
 * Для этого он использует Яндекс Переводчик API
 * Подробнее: https://yandex.ru/dev/translate/
 *
 * Максимальное число бесплатных символов на день 1 млн
 * Тариф 1 млн = 15$. При этом надо учитовать то, что если перейти в платную версию,
 * бесплатные 1 млн символов не будут работать.
 */
public class Translator {
    /**
     * Перевод текста
     * @param text переводимый текст
     * @param lang_from с какого языка
     * @param lang_to на какой язык
     * @return текст который уже был переведен
     */
    public String translate(String text, Language lang_from, Language lang_to){
        URL url = new CreateURL().translator(text, lang_from.code, lang_to.code);
        String answer = Query.makeHttpRequest(url);

        String value = "";
        //Если поленный результат не пустой
        if (!answer.isEmpty()) {
            Document doc = getDocFromXML(answer);
            value = doc.getElementsByTagName("text").item(0).getTextContent();
        }

        return value;
    }

    /**
     * Обнаружение языка
     * @param text текст с помшью которого будет обнаружен язык
     * @return код языка (Пример - 'en')
     */
    public String detect(String text){
        URL url = new CreateURL().detect(text);
        String answer = Query.makeHttpRequest(url);

        Document doc = getDocFromXML(answer);
        String value = doc.getDocumentElement().getAttribute("lang");

        return value;
    }

    /**
     * Класс описывает метод с помошью которых создается
     * объект URL для запроса в сервер
     */
    private static class CreateURL {
        // Ключь API от Яндекса
        public final static String API_KEY = "trnsl.1.1.20200214T125637Z.f069fd5e48fa1ad7.1a1ef1a1b80bfd8f23f6460c238cff4a3394fa30";

        // Подробнее: https://yandex.ru/dev/translate/doc/dg/reference/translate-docpage/
        public final static String TRANSLATOR_URL = "https://translate.yandex.net/api/v1.5/tr/translate";

        // Подробнее: https://yandex.ru/dev/translate/doc/dg/reference/detect-docpage/
        public final static String LANG_DETECT_URL = "https://translate.yandex.net/api/v1.5/tr/detect";

        URL translator(String text, String lang_from, String lang_to){
            Uri.Builder builder = Uri.parse(TRANSLATOR_URL).buildUpon();
            builder.appendQueryParameter("key", API_KEY);
            builder.appendQueryParameter("text", text);

            String paramLang = "";
            if (!lang_from.isEmpty()) paramLang = lang_from + "-";
            paramLang = paramLang + lang_to;

            builder.appendQueryParameter("lang", paramLang);

            return createURLByBuilder(builder);
        }

        URL detect(String text){
            Uri.Builder builder = Uri.parse(LANG_DETECT_URL).buildUpon();
            builder.appendQueryParameter("key", API_KEY);
            builder.appendQueryParameter("text", text);

            return createURLByBuilder(builder);
        }

        private URL createURLByBuilder(Uri.Builder builder){
            URL url = null;
            try {
                url = new URL(builder.build().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return url;
        }
    }

    /**
     * Конверитрование String (XML) в Document
     * @param xmlText конвертируемый текст
     * @return объект типа Document
     */
    private static Document getDocFromXML(String xmlText){
        Document doc = null;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(xmlText)));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return doc;
    }
}