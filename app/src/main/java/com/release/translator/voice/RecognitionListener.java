package com.release.translator.voice;

import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import com.release.translator.MainActivity;
import com.release.translator.R;

import java.util.ArrayList;

/**
 * Класс слушателя.
 * Тут опсываются методы с помошью которого осуществляется слушать пользователя
 * Внутренний класс DetectTask
 */
public class RecognitionListener implements android.speech.RecognitionListener {
    static final String TAG = "RecognitionListener";

    private SpeechManager speechManager;
    private MainActivity activity;

    RecognitionListener(SpeechManager speechManager, MainActivity activity) {
        this.speechManager = speechManager;
        this.activity = activity;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d(TAG, "onReadyForSpeech");

        speechManager.setCivFocus(true);
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.d(TAG, "onRmsChanged");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d(TAG, "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "onEndOfSpeech");

        speechManager.setCivFocus(false);
    }

    @Override
    public void onError(int error) {
        Log.d(TAG,  "Error code - " + error);

        speechManager.setCivFocus(false);
    }

    @Override
    public void onResults(Bundle results) {
        Log.d(TAG, "onResults " + results.size());

        ArrayList resultsString = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        //Получаем текстовый резуальтат от речи
        final String from_text = (String) resultsString.get(0);
        Log.d(TAG, "From text - " + from_text);

        //Отправляем результат на перевод
        MainActivity.TranslatorTask task = activity.getTranslatorTaskInstance(MainActivity.TranslatorTask.TYPE_VOICE);
        task.execute(from_text);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.d(TAG, "onPartialResults");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d(TAG, "onEvent " + eventType);
    }
}
