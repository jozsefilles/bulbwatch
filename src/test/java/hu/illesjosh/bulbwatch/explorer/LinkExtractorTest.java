package hu.illesjosh.bulbwatch.explorer;

import static com.google.common.io.Resources.getResource;
import static hu.illesjosh.bulbwatch.explorer.LinkExtractor.url;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import com.google.common.io.Resources;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LinkExtractorTest {

    private static final String ANCHOR_SELECTOR = "a.interesting";
    private static final URL TEST_URL = url("http://example.org");
    private static Document TEST_DOC;

    @Mock
    private Downloader downloader;
    private LinkExtractor extractor;

    @BeforeAll
    public static void setupClass() throws IOException {
        var url = getResource("links.html");
        var html = Resources.toString(url, StandardCharsets.UTF_8);
        TEST_DOC = Jsoup.parse(html);
    }

    @BeforeEach
    public void setup() {
        when(downloader.download(TEST_URL))
            .thenReturn(Optional.of(TEST_DOC));

        extractor = new LinkExtractor(downloader, ANCHOR_SELECTOR);
    }

    @Test
    public void extractsLinks() {
        var result = extractor.extractLinksFromUrl(TEST_URL);

        assertIterableEquals(result,
            List.of(
                url("http://papucs.org"),
                url(TEST_URL, "kakucs.html")
            )
        );
    }

}