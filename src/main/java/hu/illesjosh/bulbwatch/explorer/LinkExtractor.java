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

    @Inject
    LinkExtractor(Downloader downloader) {
        this.downloader = downloader;
    }

    public List<URL> extractLinksFromUrl(URL pageUrl, String linkSelector) {
        var links = downloader.download(pageUrl)
            .map(doc -> extractLinksFromDocument(pageUrl, doc, linkSelector))
            .orElseGet(Collections::emptyList);
        return links;
    }

    private List<URL> extractLinksFromDocument(URL pageUrl, Document doc, String linkSelector) {
        var links = doc.select(linkSelector)
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
