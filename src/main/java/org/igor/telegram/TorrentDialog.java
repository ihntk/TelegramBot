package org.igor.telegram;

import org.telegram.telegrambots.meta.api.objects.Document;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class TorrentDialog extends Dialog {
    private Path categoryPath;
    private Document document;
    private Properties properties;

    private final String films;
    private final String mult;
    private final String series;
    private final String music;
    private final String packages;
    private final String packagesWin;

    public TorrentDialog() {
        setActiveDialog();
        properties = Main.getProperties();

        films = properties.getProperty("films");
        mult = properties.getProperty("mult");
        series = properties.getProperty("series");
        music = properties.getProperty("music");
        packages = properties.getProperty("packages");
        packagesWin = properties.getProperty("packagesWin");
    }

    public String handleDialog(String message) {
        setAnswer("");
        List<String> messageCommands = Arrays.asList(message.split(" "));

        for (String command : messageCommands) {
            switch (command) {
                case ("/films"):
                    categoryPath = Paths.get(films);
                    break;
                case ("/series"):
                    categoryPath = Paths.get(series);
                    break;
                case ("/mult"):
                    categoryPath = Paths.get(mult);
                    break;
                case ("/packages"):
                    categoryPath = Paths.get(packages);
                    break;
                case ("/packagesWin"):
                    categoryPath = Paths.get(packagesWin);
                    break;
                case ("/music"):
                    categoryPath = Paths.get(music);
                    break;
            }
        }

        String fileName = document.getFileName();
        boolean isTorrent = fileName.endsWith(".torrent");

        if (categoryPath != null && isTorrent) {
            downloadTorrent();
            exitDialog();
            setAnswer("Torrent is downloading");
        } else if (categoryPath == null)
            setAnswer("Select category");
        else
            setAnswer("Input torrent file");

        return getAnswer();
    }

    @Override
    public String handleDialog(Document document) {
        this.document = document;
        return handleDialog("");
    }

    private void downloadTorrent() {

    }
}
