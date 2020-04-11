package hu.illesjosh.bulbwatch.parser;

import static java.time.temporal.ChronoField.*;
import static java.util.stream.Collectors.toList;

import javax.inject.Inject;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Optional;

import hu.illesjosh.bulbwatch.explorer.Downloader;
import hu.illesjosh.bulbwatch.model.Product;
import hu.illesjosh.bulbwatch.model.Review;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ProductParser {

    private static final String CSS_PROD_NAME = "div.page_artdet_name_2 span.text_biggest";
    private static final String CSS_REVIEWS = "div.page_artforum_element";
    private static final String CSS_AUTHOR = "div.page_artforum_element_head_left span strong";
    private static final String CSS_CONTENT = "div.page_artforum_element_message span";

    @Inject
    private Downloader downloader;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder().appendValue(YEAR)
        .appendLiteral('.')
        .appendValue(MONTH_OF_YEAR)
        .appendLiteral('.')
        .appendValue(DAY_OF_MONTH)
        .appendLiteral(' ')
        .appendValue(HOUR_OF_DAY)
        .appendLiteral(':')
        .appendValue(MINUTE_OF_HOUR)
        .parseStrict()
        .toFormatter();

    public Optional<Product> parseProduct(URL url) {
        return downloader.download(url)
            .map(this::parseProductName)
            .map(n -> Product.builder()
                .url(url)
                .name(n)
                .lastReviewDate(LocalDate.MIN)
                .build());
    }

    public String parseProductName(Document doc) {
        return doc.selectFirst(CSS_PROD_NAME)
            .text();
    }

    public List<Review> parseReviews(Document doc) {
        var elements = doc.select(CSS_REVIEWS);
        return elements.stream()
            .map(this::parseReview)
            .collect(toList());
    }

    public Review parseReview(Element elem) {
        var authorElem = elem.selectFirst(CSS_AUTHOR);
        var author = authorElem.text();

        var dateStr = authorElem.nextElementSibling()
            .nextSibling()
            .toString();
        var date = LocalDateTime.parse(dateStr.strip(), DATE_TIME_FORMATTER);

        var content = elem.selectFirst(CSS_CONTENT)
            .text();

        return Review.builder()
            .author(author)
            .date(date)
            .content(content)
            .build();
    }

}
