package hu.illesjosh.bulbwatch.explorer;

import static hu.illesjosh.bulbwatch.explorer.LinkExtractor.url;

import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductExplorerTest {

    private static final URL PROD_CAT_PAGE_URL = url("https://lumenet.hu/emelt-fenyu-emelt-szinhomersekletu-h4-izzo");

    private ProductExplorer explorer;

    @BeforeEach
    public void setup() {
        explorer = new ProductExplorer();
    }

    @Test
    public void test() {
        var products = explorer.explore(PROD_CAT_PAGE_URL);
        System.out.println(products);
    }

}