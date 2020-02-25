package com.release.translator.voice;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.release.translator.MainActivity;
import com.release.translator.Manager;
import com.release.translator.R;
import com.release.translator.translator.Query;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Класс полностью берет на себе управление с View элементами.
 * Он так же создает, запускает слушатель
 */
public class SpeechManager {
    CircleImageView selected_civ;
    MainActivity activity;
    Resources res;

    static final String TAG = "SpeechManager";

    public SpeechManager(final MainActivity activity) {
        this.activity = activity;
        res = activity.getResources();

        // Полверяем можно ли в телефоне рапознавать голос
        if(SpeechRecognizer.isRecognitionAvailable(activity)) {

            final SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
            // Создаем объект слушателя
            RecognitionListener listener = new RecognitionListener(this, activity);
            speechRecognizer.setRecognitionListener(listener);

            // В случае нажатие кнопки голоса
            View.OnClickListener voice_clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Проверяем подключен ли устройства к интернету
                    if (Query.hasConnection(activity)) {
                        selected_civ = (CircleImageView) view;
                        //Устанавливаем текущий язык
                        Manager.setCurrentLangViewById(view.getId());

                        //Проверяем разрешение, если его нет делаем запрос на получение
                        checkPermission(activity);
                        //Если все нормально, запускаем слушатель
                        startSpeak(speechRecognizer);
                    }
                }
            };
            //Инициализируем CIV
            CircleImageView left_civ = activity.findViewById(R.id.left_voice);
            left_civ.setColorFilter(res.getColor(R.color.translator_left_user));
            left_civ.setOnClickListener(voice_clickListener);

            CircleImageView right_civ = activity.findViewById(R.id.right_voice);
            right_civ.setColorFilter(res.getColor(R.color.translator_right_user));
            right_civ.setOnClickListener(voice_clickListener);
        } else {
            Log.e(TAG, "voice is off");
        }
    }

    /**
     * Запустить слушатель пользователя
     * @param speechRecognizer
     */
    static void startSpeak(SpeechRecognizer speechRecognizer){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Manager.currentLanguage.code);

        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
        speechRecognizer.startListening(intent);

        Log.d(TAG, "listener is started");
    }

    /**
     * Проверка наличие разрешение Микрофон. Если разрешение нет, то метод сам просит его дать
     * @param activity
     */
    static void checkPermission(Activity activity){
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.RECORD_AUDIO)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        527);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request. (In this example I just punched in
                // the value 527)
            }
        }
    }

    /**
     * Управление UI.
     * @param isEnabled == true в случае если объект слушает пользователя
     *                  == false в случае если обхект не слшуает пользователя
     */
    void setCivFocus(boolean isEnabled){
        if (isEnabled){
            selected_civ.setColorFilter(res.getColor(android.R.color.white));

            if (Manager.isCurrentUserLeft()) {
                selected_civ.setCircleBackgroundColor(res.getColor(R.color.translator_left_user));
                activity.findViewById(R.id.right_voice).setClickable(false);
            }
            else {
                selected_civ.setCircleBackgroundColor(res.getColor(R.color.translator_right_user));
                activity.findViewById(R.id.left_voice).setClickable(false);
            }
        }
        else {
            selected_civ.setCircleBackgroundColor(res.getColor(android.R.color.white));

            if (Manager.isCurrentUserLeft()) {
                selected_civ.setColorFilter(res.getColor(R.color.translator_left_user));
                activity.findViewById(R.id.right_voice).setClickable(true);
            }
            else {
                selected_civ.setColorFilter(res.getColor(R.color.translator_right_user));
                activity.findViewById(R.id.left_voice).setClickable(true);
            }
        }
    }
}




