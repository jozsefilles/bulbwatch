package hu.illesjosh.bulbwatch.explorer;

import static java.util.stream.Collectors.toList;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LinkExtractor {

	private final String selector; // TODO rename anchorCssSelector or sth

	public List<URL> extractLinks(URL pageUrl) {
		return Downloader.download(pageUrl)
			.stream()
			.map(doc -> doc.select(selector)) // TODO href attr, text needs retrieved
			.flatMap(Elements::stream)
			.map(Element::text)
			.map(LinkExtractor::url)
			.flatMap(Optional::stream)
			.collect(toList());
	}

	private static Optional<URL> url(String spec) {
		try {
			var url = new URL(spec);
			return Optional.of(url);
		} catch (MalformedURLException e) {
			return Optional.empty();
		}
	}

}
