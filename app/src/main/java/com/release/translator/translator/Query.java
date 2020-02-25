package com.release.translator.translator;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.release.translator.Manager;
import com.release.translator.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.Charset;

/**
 * Класс делает запрос по URL
 * Обробатывает и возвращает переменную типа String
 */
public class Query {
    static final String TAG = "Query";

    /**
     * Делает запрос по Url
     * Если результат нет, то возвращается пустой объекут типа String
     */
    static String makeHttpRequest(URL url) {
        String value = "";

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                value = readFrom(inputStream);
            }
            else{
                Log.e("Error", "code - " + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(TAG, e.getMessage());
        }
        finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return value;
    }

    /**
     * Читает данные которые пришли в inputStream
     * Сохроняет в String и возвращает
     */
    private static String readFrom(InputStream inputStream) throws IOException{
        StringBuilder stringBuilder = new StringBuilder(); // Обект который хранит строки в виде массива

        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8")); // Раскадировка inputStream на UTF-8

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader); // Примнемает и передает значиня быстрее

            String perem = bufferedReader.readLine();

            while (perem != null){
                stringBuilder.append(perem);
                perem = bufferedReader.readLine(); // Сохраняем в виде символов
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Проверяет подключение к интернету
     * true - соеденение есть
     * false - соеденение нет
     */
    public static boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) return true;

        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) return true;

        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) return true;

        Toast.makeText(context, context.getString(R.string.not_connect), Toast.LENGTH_SHORT).show();
        return false;
    }
}
