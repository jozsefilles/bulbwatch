package hu.illesjosh.bulbwatch.explorer;

import static java.util.stream.Collectors.toList;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import hu.illesjosh.bulbwatch.model.Product;
import hu.illesjosh.bulbwatch.parser.ProductParser;

public class ProductExplorer {

    private static final String CSS_PRODUCT_LINK = "a.product_link_normal";

    @Inject
    private LinkExtractor linkExtractor;
    @Inject
    private ProductParser productParser;

    public List<Product> explore(URL root) {
        return linkExtractor.extractLinksFromUrl(root)
            .stream()
            .map(productParser::parseProduct)
            .flatMap(Optional::stream)
            .collect(toList());
    }

}
