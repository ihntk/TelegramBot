package org.igor.telegram;

public abstract class Dialog {

    private String answerMessage;

    public String getAnswer() {
        return answerMessage;
    }

    public abstract String handleDialog(String message);
}
