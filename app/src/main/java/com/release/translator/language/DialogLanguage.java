package com.release.translator.language;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.release.translator.MainActivity;
import com.release.translator.Manager;
import com.release.translator.R;

/**
 * Класс создает диалоговое окно где пользователь
 * может выбрать язык
 */
public class DialogLanguage extends Dialog {
    MainActivity activity;

    public DialogLanguage(MainActivity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayout());
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.TOP;
        params.y = getDpForPx(60f, getContext());
        getWindow().setAttributes(params);
    }

    /**
     * Метод создает основной Layout диалогового окна
     * Доступный языки берет с класса Language
     * @return
     */
    private View getLayout(){
        LinearLayout layout = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);

        params.weight = 1;

        //Получаем все доступные языки
        final Language language_list[] = Language.values();
        for (int i = 0; i < 2; i++) {
            LinearLayout child_layout = new LinearLayout(getContext());
            child_layout.setOrientation(LinearLayout.VERTICAL);

            for (final Language language : language_list){
                View include_layout;
                if (i == 0) include_layout = getLayoutInflater().inflate(R.layout.adapter_language_left, null);
                else include_layout = getLayoutInflater().inflate(R.layout.adapter_language_right, null);
                setLanguageUI(include_layout, language);

                final int finalI = i;
                //При нажатие на любой язык обробатываем следующие события
                include_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (language != Manager.left_lng && language != Manager.right_lng) {
                            if (finalI == 0) {
                                setLanguageUI(activity.findViewById(R.id.left_language), language);
                                Manager.left_lng = language;
                            } else {
                                setLanguageUI(activity.findViewById(R.id.right_language), language);
                                Manager.right_lng = language;
                            }
                        }
                        else
                            Toast.makeText(activity, activity.getResources().getString(R.string.you_have_chosen_this_language), Toast.LENGTH_SHORT).show();

                        hide();
                    }
                });

                child_layout.addView(include_layout);
            }

            layout.addView(child_layout, params);
        }

        return layout;
    }

    /**
     * Метод вычисляет px в формате dp
     */
    public static int getDpForPx(float dip, Context context){
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                context.getResources().getDisplayMetrics()));
    }

    /**
     * Устанавливает UI элементы
     */
    public static void setLanguageUI(View include_layout, Language language){
        ((TextView) include_layout.findViewById(R.id.name)).setText(language.getName());
        Glide.with(include_layout.getContext())
                .load(language.getFlagPath())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) include_layout.findViewById(R.id.image));
    }
}
