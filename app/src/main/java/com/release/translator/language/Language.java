package com.release.translator.language;

/**
 * Список языков которых можно использовать
 * Тут еще описываются методы для них
 */
public enum Language {
    KAZAKH("kk"),
    RUSSIAN("ru"),
    ENGLISH("en"),
    TURKISH("tr"),
    CHINA("zh");

    public String code;
    Language(String code) {
        this.code = code;
    }

    /**
     * Получить называние языка
     */
    public String getName(){
        switch (this){
            case KAZAKH:
                return "Қазақ";
            case RUSSIAN:
                return "Русский";
            case ENGLISH:
                return "English";
            case TURKISH:
                return "Türk";
            case CHINA:
                return "中國人";
            default:
                return "";
        }
    }

    /**
     * Получить URL путь к изображению флага
     */
    public String getFlagPath(){
        switch (this){
            case KAZAKH:
                return "https://i.ibb.co/3YsCpW5/kk.png";
            case RUSSIAN:
                return "https://i.ibb.co/ZLSmmX5/ru.png";
            case ENGLISH:
                return "https://i.ibb.co/XDpHptP/en.png";
            case TURKISH:
                return "https://i.ibb.co/LrpD6rT/tr.jpg";
            case CHINA:
                return "https://i.ibb.co/YZtVg47/zh.png";
            default:
                return "";
        }
    }

}
