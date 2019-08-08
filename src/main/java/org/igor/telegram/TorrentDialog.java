package org.igor.telegram;

import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class TorrentDialog extends Dialog {
    private Path categoryPath;
    private Document document;
    private Properties properties;
    private String homeDir = System.getProperty("user.home");

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
                    categoryPath = Paths.get(homeDir, films);
                    break;
                case ("/series"):
                    categoryPath = Paths.get(homeDir, series);
                    break;
                case ("/mult"):
                    categoryPath = Paths.get(homeDir, mult);
                    break;
                case ("/packages"):
                    categoryPath = Paths.get(homeDir, packages);
                    break;
                case ("/packagesWin"):
                    categoryPath = Paths.get(homeDir, packagesWin);
                    break;
                case ("/music"):
                    categoryPath = Paths.get(homeDir, music);
                    break;
            }
        }

        String fileName;
        boolean isTorrent = false;
        if (document != null) {
            fileName = document.getFileName();
            isTorrent = fileName.endsWith(".torrent");
        }
        if (categoryPath != null && isTorrent) {
            downloadTorrent();
            exitDialog();
            setAnswer("Torrent is downloading");
        } else if (categoryPath == null)
            setAnswer("Select category:\n" +
                    "/films /mult /series /packages /packagesWin /music");
        else
            setAnswer("Input torrent file");

        return getAnswer();
    }

    @Override
    public String handleDialog(Document document) {
        this.document = document;
        resetDocument();
        return handleDialog("");
    }

    private void downloadTorrent() {
        try {
            GetFile getFile = new GetFile();
            getFile.setFileId(document.getFileId());
            File file = Main.getBot().execute(getFile);
            URL fileUrl = new URL(file.getFileUrl(Main.getBot().getBotToken()));
            HttpURLConnection httpURLConnection = (HttpURLConnection) fileUrl.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            Files.copy(inputStream, Paths.get(categoryPath.toString(), document.getFileName()));
        } catch (TelegramApiException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
