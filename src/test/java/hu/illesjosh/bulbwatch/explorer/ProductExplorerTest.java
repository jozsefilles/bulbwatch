package hu.illesjosh.bulbwatch.explorer;

import static hu.illesjosh.bulbwatch.explorer.LinkExtractor.url;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import hu.illesjosh.bulbwatch.model.Product;
import hu.illesjosh.bulbwatch.parser.ProductParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductExplorerTest {

    private static final URL PROD_CAT_PAGE_URL = url("https://lumenet.hu/emelt-fenyu-emelt-szinhomersekletu-h4-izzo");
    private static final List<URL> EXTRACTED_LINKS = List.of(
        url(PROD_CAT_PAGE_URL, "about"),
        url(PROD_CAT_PAGE_URL, "contact")
    );

    @Mock
    private LinkExtractor linkExtractor;
    @Mock
    private ProductParser productParser;

    private ProductExplorer explorer;

    @Captor
    private ArgumentCaptor<URL> urlCaptor;

    @BeforeEach
    public void setup() {
        explorer = new ProductExplorer(linkExtractor, productParser);

        when(linkExtractor.extractLinksFromUrl(PROD_CAT_PAGE_URL))
            .thenReturn(EXTRACTED_LINKS);

        when(productParser.parseProduct(any()))
            .then(call ->
                Optional.of(
                    Product.builder()
                        .url(call.getArgument(0))
                        .build()));
    }

    @Test
    public void extractsLinks() {
        explorer.explore(PROD_CAT_PAGE_URL);

        verify(linkExtractor).extractLinksFromUrl(PROD_CAT_PAGE_URL);
    }

    @Test
    public void parsesExtractedLinks() {
        explorer.explore(PROD_CAT_PAGE_URL);

        verify(productParser, times(EXTRACTED_LINKS.size())).parseProduct(urlCaptor.capture());
        var parsedLinks = urlCaptor.getAllValues();
        assertIterableEquals(EXTRACTED_LINKS, parsedLinks);
    }

    @Test
    public void returnsParsedProducts() {
        var parsedProducts = explorer.explore(PROD_CAT_PAGE_URL);

        var parsedProductUrls = parsedProducts.stream()
            .map(Product::getUrl)
            .collect(toList());
        assertIterableEquals(EXTRACTED_LINKS, parsedProductUrls);
    }

}