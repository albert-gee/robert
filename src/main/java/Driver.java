import crawler.Classifier;
import crawler.Crawler;
import crawler.SimpleBuffer;
import crawler.UriFactory;
import crawler.interfaces.BufferInterface;
import crawler.interfaces.HandlerInterface;
import crawler.interfaces.UriFactoryInterface;
import crawler.interfaces.UriInterface;
import crawler.uriRuleEntities.*;

import java.util.HashMap;
import java.util.Map;

public class Driver {

    public static void main(String... args) {

        // Create UriFactory and add rules
        Map<String, Class<?>> rules = new HashMap<>();
        rules.put("tel",    Tel.class);
        rules.put("mailto", Mailto.class);
        rules.put("http",   Http.class);
        rules.put("https",  Http.class);
        rules.put("file",   File.class);
        rules.put(null,     UnknownUri.class);
        UriFactoryInterface uriFactory = new UriFactory(rules);

        // Create Buffer
        BufferInterface buffer = new SimpleBuffer();

        // Create Handler
        HandlerInterface handler = new Classifier();

        // Create Crawler and add Buffer, UriFactory, and Handler
        Crawler crawler = new Crawler(buffer, uriFactory, handler);
        crawler.addUris("https://hocbr.creativepace.com");

        // Handle URIs
        crawler.handle();
    }
}
