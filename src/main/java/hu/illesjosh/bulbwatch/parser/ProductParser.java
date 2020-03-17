package hu.illesjosh.bulbwatch.parser;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.YEAR;
import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import hu.illesjosh.bulbwatch.model.Review;

public class ProductParser {

	private static final String CSS_PROD_NAME = "div.page_artdet_name_2 span.text_biggest";
	private static final String CSS_REVIEWS = "div.page_artforum_element";
	private static final String CSS_AUTHOR = "div.page_artforum_element_head_left span strong";
	private static final String CSS_CONTENT = "div.page_artforum_element_message span";

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
