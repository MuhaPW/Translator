package com.release.translator.edit;

import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.release.translator.MainActivity;
import com.release.translator.Manager;
import com.release.translator.R;
import com.release.translator.translator.Query;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Класс полностью берет на себе управление с View элементами.
 * Он так же создает, запускает слушатель
 */
public class EditManager implements View.OnClickListener{
    EditText edit_text;
    MainActivity activity;

    public EditManager(final MainActivity activity){
        this.activity = activity;
        Resources res = activity.getResources();

        edit_text = activity.findViewById(R.id.edit_text);

        //Устанавливаем tint изображений
        activity.findViewById(R.id.civ_text_send).setOnClickListener(this);
        CircleImageView left_edit = activity.findViewById(R.id.left_edit);
        left_edit.setColorFilter(res.getColor(R.color.translator_left_user));
        left_edit.setOnClickListener(this);

        CircleImageView right_edit = activity.findViewById(R.id.right_edit);
        right_edit.setColorFilter(res.getColor(R.color.translator_right_user));
        right_edit.setOnClickListener(this);

        edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                //Если текст был изменен
                String from_text = edit_text.getText().toString();

                //Проверяем не пустой ли текст и есть ли соединение с интернетом
                if (!from_text.isEmpty() && Query.hasConnection(activity)) {
                    //Создаем объект таска и передаем data
                    MainActivity.TranslatorTask task = activity.getTranslatorTaskInstance(MainActivity.TranslatorTask.TYPE_TEXT);
                    task.execute(from_text);
                }
                //В другом случае скрываем переводимый текст. Даже если он пустой
                else {
                    activity.findViewById(R.id.translated_text_layout).setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.left_edit:
            case R.id.right_edit:
                //По умолчанию EditText не видем. Делаем его видемым
                activity.findViewById(R.id.layout_edit).setVisibility(View.VISIBLE);

                //Устанавливаем текущий язык
                Manager.setCurrentLangViewById(view.getId());
                break;
            case R.id.civ_text_send:
                String from_text = edit_text.getText().toString();
                String to_text = ((TextView) activity.findViewById(R.id.translated_text)).getText().toString();

                //Создаем новое сообщение
                activity.createSms(from_text, to_text);

                edit_text.setText("");
                break;
        }
    }
}
