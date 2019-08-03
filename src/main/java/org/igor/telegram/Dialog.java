package org.igor.telegram;

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

    public abstract String handleDialog(String message);
}
