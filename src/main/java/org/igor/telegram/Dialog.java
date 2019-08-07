package org.igor.telegram;

import org.telegram.telegrambots.meta.api.objects.Document;

public abstract class Dialog {

    private String answer;

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setActiveDialog() {
        Main.getBot().setActiveDialog(this);
    }

    public void exitDialog() {
        Main.getBot().setActiveDialog(null);
    }

    public abstract String handleDialog(String message);

    public abstract String handleDialog(Document document);
}
