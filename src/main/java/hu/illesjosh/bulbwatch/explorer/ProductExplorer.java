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

    private final LinkExtractor linkExtractor;
    private final ProductParser productParser;

    @Inject
    public ProductExplorer(LinkExtractor linkExtractor, ProductParser productParser) {
        this.linkExtractor = linkExtractor;
        this.productParser = productParser;
    }

    public List<Product> explore(URL root) {
        return linkExtractor.extractLinksFromUrl(root)
            .stream()
            .map(productParser::parseProduct)
            .flatMap(Optional::stream)
            .collect(toList());
    }

}
