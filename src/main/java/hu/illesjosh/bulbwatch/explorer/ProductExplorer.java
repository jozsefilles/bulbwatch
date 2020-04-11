package hu.illesjosh.bulbwatch.explorer;

import static java.util.stream.Collectors.toList;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import hu.illesjosh.bulbwatch.model.Product;
import hu.illesjosh.bulbwatch.parser.ProductParser;

public class ProductExplorer {

	private static final String CSS_PRODUCT_LINK = "a.product_link_normal";

	private LinkExtractor linkExtractor; // TODO Spring should come finally
	private ProductParser productParser;

	public ProductExplorer() {
		linkExtractor = new LinkExtractor(CSS_PRODUCT_LINK);
		productParser = new ProductParser();
	}

	public List<Product> explore(URL root) {
		return linkExtractor.extractLinks(root)
			.stream()
			.map(productParser::parseProduct)
			.flatMap(Optional::stream)
			.collect(toList());
	}

}
