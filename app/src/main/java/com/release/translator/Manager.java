package com.release.translator;

import com.release.translator.language.Language;

/**
 * Главный Manager всего переводчика
 * Тут хранятся статичные переменные для управление языками
 */
public class Manager {
    //Языки по умолчанию English и Russian
    public static Language left_lng = Language.ENGLISH;
    public static Language right_lng = Language.RUSSIAN;

    //Текущий язык
    public static Language currentLanguage;

    /**
     * Получить не текущий язык
     * То есть если выбран левый язык, возвращает правый
     */
    static Language getNotCurrentLanguage(){
        if (currentLanguage != left_lng) return left_lng;
        else return right_lng;
    }

    /**
     * @return
     * true если текущий язык, это левый язык
     * false если текущий язык, это правый язык
     */
    public static boolean isCurrentUserLeft(){
        return currentLanguage == left_lng;
    }

    /**
     * Устанавливаем значение текущего языка по ViewId
     */
    public static void setCurrentLangViewById(int viewId){
        switch (viewId){
            case R.id.left_edit:
            case R.id.left_voice:
                currentLanguage = left_lng;
                break;
            case R.id.right_edit:
            case R.id.right_voice:
                currentLanguage = right_lng;
                break;
        }
    }
}
