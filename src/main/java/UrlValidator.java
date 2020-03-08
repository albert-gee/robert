import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Albert Gaintudinov
 * @version 1.0
 */
class UrlValidator {

    /**
     * Checks if URL has HTTP or HTTPS protocol
     * @param url - URL address
     * @return true if URL starts with http:// or https://
     */
    public static boolean isAbsoluteUrl(String url) {
        if (url == null) return false;

        String trimmedLowerCaseUrl = url.trim().toLowerCase();

        return trimmedLowerCaseUrl.startsWith("http://") || trimmedLowerCaseUrl.startsWith("https://");
    }

    /**
     * Checks if provided link is not empty, is absolute, and returns status code < 400
     * @param link - link to be checked
     * @return boolean
     */
    public static boolean isLinkValid(String link) {

        boolean isValid;

        if (link != null && link.trim().length() > 0 && isAbsoluteUrl(link)) {

            try {
                URL url = new URL(link);

                URLConnection connection = url.openConnection();

                HttpURLConnection c = (HttpURLConnection) connection;
                c.setRequestMethod("GET");
                c.connect();

                int statusCode = c.getResponseCode();

                isValid = statusCode < 400;

            } catch (IOException e) {
                isValid = false;
            }
        } else {
            isValid = false;
        }

        return isValid;
    }

    /**
     * Checks if current URL is a web page
     * @return boolean
     * @throws IOException
     */
    public static boolean isWebPage(URL url) throws IOException {
        if (url == null) return false;

        URLConnection connection = url.openConnection();
        return connection.getHeaderField("Content-Type").contains("text/html");
    }

    /**
     * Checks if URL belongs to host
     * @param host - current host
     * @param link - URL of link to be checked
     * @return boolean
     * @throws IOException if connection failed
     */
    public static boolean isLinkInternal(String host, URL link) throws IOException {
        if (link != null && UrlValidator.isLinkValid(link.toString())) {

            String hostWithoutHttps = host.replace("https://", "");
            String hostWithoutHttp = hostWithoutHttps.replace("http://", "");

            String linkWithoutHttps = link.getHost().replace("https://", "");
            String linkWithoutHttp = linkWithoutHttps.replace("http://", "");

            return hostWithoutHttp.equals(linkWithoutHttp);
        } else {
            throw new IllegalArgumentException("Provided link or host is invalid");
        }
    }
}