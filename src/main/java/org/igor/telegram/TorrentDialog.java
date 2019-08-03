package org.igor.telegram;

public class TorrentDialog extends Dialog {
    public String handleDialog(String message) {
        if (message.trim().equals("/torrent")) {
            setActiveDialog();
            setAnswer("What cen i help you?");
        }

        return getAnswer();
    }
}
