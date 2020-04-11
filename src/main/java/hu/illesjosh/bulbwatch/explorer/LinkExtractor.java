package hu.illesjosh.bulbwatch.explorer;

import static java.util.stream.Collectors.toList;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Document;

public class LinkExtractor {

    private final Downloader downloader;
    private final String anchorCssSelector;

    @Inject
    public LinkExtractor(Downloader downloader, String anchorCssSelector) {
        this.downloader = downloader;
        this.anchorCssSelector = anchorCssSelector;
    }

    public List<URL> extractLinksFromUrl(URL pageUrl) {
        var links = downloader.download(pageUrl)
            .map(doc -> extractLinksFromDocument(pageUrl, doc))
            .orElseGet(Collections::emptyList);
        return links;
    }

    private List<URL> extractLinksFromDocument(URL pageUrl, Document doc) {
        var links = doc.select(anchorCssSelector)
            .stream()
            .map(el -> el.attr("href"))
            .map(href -> url(pageUrl, href))
            .collect(toList());
        return links;
    }

    public static URL url(String spec) throws IllegalArgumentException {
        try {
            return new URL(spec);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException();
        }
    }

    public static URL url(URL context, String spec) throws IllegalArgumentException {
        try {
            return new URL(context, spec);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException();
        }
    }

}
