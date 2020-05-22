package hu.illesjosh.bulbwatch.explorer;

import static java.util.stream.Collectors.toList;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import hu.illesjosh.bulbwatch.model.Product;
import hu.illesjosh.bulbwatch.parser.ProductParser;

public class ProductExplorer {

    private final LinkExtractor linkExtractor;
    private final String productLinkSelector;
    private final ProductParser productParser;

    @Inject
    ProductExplorer(
        LinkExtractor linkExtractor,
        @Named("product.link-selector") String productLinkSelector,
        ProductParser productParser) {

        this.linkExtractor = linkExtractor;
        this.productLinkSelector = productLinkSelector;
        this.productParser = productParser;
    }

    public List<Product> explore(URL root) {
        return linkExtractor.extractLinksFromUrl(root, productLinkSelector)
            .stream()
            .map(productParser::parseProduct)
            .flatMap(Optional::stream)
            .collect(toList());
    }

}
