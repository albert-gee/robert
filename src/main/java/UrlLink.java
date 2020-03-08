import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class represents a structured data of a URL string such as response HTTP code and where particular URL was found
 *
 * @author Albert Gainutdinov
 * @version 1.0
 */
public class UrlLink {

    // Found url address
    private URL url;

    // Where URL was found
    private UrlLink parent;

    // HTTP response code
    private int httpStatusCode;

    public UrlLink(String url, UrlLink foundOn) throws IOException {
        setUrl(url);
        setParent(foundOn);
        setHttpStatusCode();
    }

    /**
     * @return current URL string
     */
    public URL getUrl() {
        return url;
    }

    /**
     * @return parent URL string where current URL was found
     */
    public UrlLink getParent() {
        return this.parent;
    }

    /**
     * @return HTTP status code
     */
    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

    /**
     * Sets current URL string
     * @param url - URL string
     */
    private void setUrl(String url) throws IOException {
        if (UrlValidator.isLinkValid(url)) {
            this.url = new URL(url.trim());
        } else {
            throw new IllegalArgumentException("URL is invalid: " + url);
        }
    }

    /**
     * Sets parent URL string where current URL was found
     * @param parent - parent URL string
     */
    private void setParent(UrlLink parent) {
        this.parent = parent;
    }

    /**
     * HTTP status code of current URL
     */
    private void setHttpStatusCode() throws IOException {



        URLConnection connection = this.getUrl().openConnection();

        HttpURLConnection c = (HttpURLConnection) connection;
        c.setRequestMethod("GET");
        c.connect();
        this.httpStatusCode = c.getResponseCode();
    }
}
