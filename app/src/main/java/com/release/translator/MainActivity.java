package com.release.translator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.release.translator.chat.ChatAdapter;
import com.release.translator.chat.MessageBlog;
import com.release.translator.edit.EditManager;
import com.release.translator.language.DialogLanguage;
import com.release.translator.language.Language;
import com.release.translator.translator.Translator;
import com.release.translator.voice.SpeechManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView translated_text;
    View translated_text_layout;

    ArrayList<MessageBlog> messageBlogs;
    RecyclerView recycler_chat;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DialogLanguage.setLanguageUI(findViewById(R.id.left_language), Language.ENGLISH);
        DialogLanguage.setLanguageUI(findViewById(R.id.right_language), Language.RUSSIAN);

        viewInitialize();
    }

    public void viewInitialize(){
        translated_text = findViewById(R.id.translated_text);
        translated_text_layout = findViewById(R.id.translated_text_layout);
        progressBar = findViewById(R.id.progressBar);

        messageBlogs = new ArrayList<>();
        //RecyclerView для чата
        recycler_chat = findViewById(R.id.recycler_chat);
        recycler_chat.setLayoutManager(new LinearLayoutManager(this));
        //Что бы элементы распологались с низу вверх
        ((LinearLayoutManager) recycler_chat.getLayoutManager()).setStackFromEnd(true);
        recycler_chat.setAdapter(new ChatAdapter(messageBlogs));

        new SpeechManager(this);
        new EditManager(this);

        final DialogLanguage dialogLanguage = new DialogLanguage(this);

        //Открываем диаловогое окно, в котором будет находится список возможных языков
        findViewById(R.id.layout_language).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLanguage.show();
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.layout_edit).getVisibility() == View.VISIBLE){
            findViewById(R.id.layout_edit).setVisibility(View.INVISIBLE);

            ((EditText) findViewById(R.id.edit_text)).setText("");
        }else {
            super.onBackPressed();
        }
    }

    /**
     * Класс переводит текст
     * Если тип текстовый просто показывает перевод в translated_text_layout
     * Если тип голосовой то сразу же создает смс.
     */
    public class TranslatorTask extends AsyncTask<String, Void, String> {
        public static final int TYPE_TEXT = 0;
        public static final int TYPE_VOICE = 1;

        String from_text;

        private int type;
        TranslatorTask(int type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            //Получаемый текст, который нужно перевсти
            from_text = strings[0];

            if (from_text.isEmpty()) return "";

            Translator translator = new Translator();
            //Переводим текст, и ответ сразу возвращаем
            return translator.translate(from_text, Manager.currentLanguage, Manager.getNotCurrentLanguage());
        }

        @Override
        protected void onPostExecute(String to_text) {
            super.onPostExecute(to_text);

            progressBar.setVisibility(View.INVISIBLE);

            //Если тип текстовый
            if (type == TYPE_TEXT) {
                //Если текст который был переведен не пустой
                if (!to_text.isEmpty()) {
                    //Устанавливаем текст
                    translated_text.setText(to_text);

                    //Если этот текст скрыт, показываем его
                    if (translated_text_layout.getVisibility() == View.INVISIBLE) {
                        translated_text_layout.setVisibility(View.VISIBLE);
                    }
                }
            }
            //Если тип голосовой
            else {
                //Если текст не пустой
                if (!to_text.isEmpty())
                    createSms(from_text, to_text);
            }
        }
    }

    public TranslatorTask getTranslatorTaskInstance(int type){
        return new TranslatorTask(type);
    }

    /**
     * Создает смс в чате. (То есть просто добавляет)
     * @param from_text получаемый текст
     * @param to_text переводимый текст
     */
    public void createSms(String from_text, String to_text){
        if (findViewById(R.id.translator_hint).getVisibility() == View.VISIBLE)
            findViewById(R.id.translator_hint).setVisibility(View.INVISIBLE);

        if (!from_text.isEmpty() && !to_text.isEmpty()){
            messageBlogs.add(new MessageBlog(Manager.isCurrentUserLeft(), from_text, to_text));
            recycler_chat.getAdapter().notifyItemInserted(messageBlogs.size() - 1);
            recycler_chat.scrollToPosition(messageBlogs.size() - 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 527: {
                //Если запрос отменен, результирующие массивы пусты.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // разрешение было предоставлено, ууу! Сделать
                    //связанное с контактами задание, которое вам нужно сделать.
                } else {
                    // разрешение отказано, бу! Отключить
                    // функциональность, которая зависит от этого разрешения.
                }
                return;
            }
            // другие строки 'case' для проверки других
            //разрешения это приложение может запросить
        }
    }
}
