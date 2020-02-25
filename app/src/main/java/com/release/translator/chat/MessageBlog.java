package com.release.translator.chat;

/**
 * Класс служит для хранение данных о смс
 */
public class MessageBlog {
    /**
     * С какой стороны сообщение
     * isLeft == true (Сообщение с левой стороны)
     * isLeft == false (Сообщение с правой стороны)
     */
    boolean isLeft;
    String from_text, to_text;

    public MessageBlog(boolean isLeft, String from_text, String to_text) {
        this.isLeft = isLeft;
        this.from_text = from_text;
        this.to_text = to_text;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public String getFrom_text() {
        return from_text;
    }

    public String getTo_text() {
        return to_text;
    }
}
