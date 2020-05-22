package hu.illesjosh.bulbwatch;

import static java.util.stream.Collectors.toList;

import javax.inject.Inject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Guice;
import hu.illesjosh.bulbwatch.explorer.ProductExplorer;
import hu.illesjosh.bulbwatch.model.Product;
import hu.illesjosh.bulbwatch.parser.ProductParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class App {

    static final String PROPERTIES_RESOURCE = "application.properties";

    @Inject
    private ProductExplorer productExplorer;
    @Inject
    private ProductParser productParser;

    public static void main(String[] args) {
        var injector = Guice.createInjector(
            new BasicModule()
        );
        injector.getInstance(App.class)
            .doItAll();
    }

    private void doItAll() {
        exploreProducts();

        // visitProducts(); // TODO re-enable
    }

    private void exploreProducts() {
        var explored = getAllCategoryLinks().stream()
            .map(productExplorer::explore)
            .flatMap(Collection::stream)
            .collect(toList());
        explored.forEach(System.out::println); // TODO do something!
    }

    private void visitProducts() {
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

    private List<URL> getAllCategoryLinks() {
        return List.of(
            testCategoryLink()
        );
    }

    private List<Product> getAllProducts() {
        return List.of(testProduct());
    }

    // FIXME upgrade to real source
    private static URL testCategoryLink() {
        return url("https://lumenet.hu/xenon-hatasu-h7-izzo");
    }

    // FIXME upgrade to real source
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
