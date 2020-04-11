package hu.illesjosh.bulbwatch.explorer;

import java.net.URL;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Downloader {

    private static final int HTTP_TIMEOUT = 5000;

    public Optional<Document> download(URL url) {
        try {
            return Optional.of(Jsoup.parse(url, HTTP_TIMEOUT));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
