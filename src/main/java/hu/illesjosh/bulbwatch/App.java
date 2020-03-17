package hu.illesjosh.bulbwatch;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import hu.illesjosh.bulbwatch.model.Product;
import hu.illesjosh.bulbwatch.parser.ProductParser;

public class App {

	private ProductParser productParser;

	public static void main(String[] args) {
		new App().doItAll();
	}

	// TODO Add Spring
	public App() {
		this.productParser = new ProductParser();
	}

	private void doItAll() {
		var subs = getAllProducts();
		var updates = new ArrayList<>();
		for (var s : subs) {
			try {
				var doc = download(s.getUrl());
				var name = productParser.parseProductName(doc);
				updates.add(name);
				var reviews = productParser.parseReviews(doc);
				// var u = reviews.stream()
				// .filter(r -> r.getDate() .after(s.getLastDate()))
				// .collect(Collectors.toList());
				// saveUpdates(s, updates);
				updates.addAll(reviews);
			} catch (IOException e) {
				System.err.println(String.format("Could not download %s, reason: %s", s.getUrl(), e.getMessage()));
			}
		}
		reportUpdates(updates);
	}

	private List<Product> getAllProducts() {
		return List.of(testProduct());
	}

	private static Product testProduct() {
		return Product.builder()
			.url(url("https://lumenet.hu/tungsram-sportlight-extreme-40-h7-58520-sup"))
			.build();
	}

	private static URL url(String url) throws IllegalArgumentException {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException();
		}
	}

	private Document download(URL url) throws IOException {
		return Jsoup.parse(url, 5_000);
	}

	private void reportUpdates(List<Object> updates) {
		for (var u : updates) {
			System.out.println(u);
		}
	}

}
