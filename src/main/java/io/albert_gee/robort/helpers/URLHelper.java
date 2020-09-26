package io.albert_gee.robort.helpers;

/**
 * This class contains useful static methods for working with URLs
 */
public class URLHelper {

    /**
     * Makes relative URL absolute
     * @param url URL
     * @param parent Parent of the URL
     * @return absolute URL
     */
    public static String makeUrlAbsolute(String url, String parent) {
        if (url == null || parent == null || parent.trim().length() == 0) {
            throw new IllegalArgumentException("URL of host were not provided");
        }

        String host = URLHelper.parseHost(parent);

        url = url.trim();
        String absoluteUrl;
        if (url.startsWith("http://") || url.startsWith("https://")) {
            absoluteUrl = url;
        } else if (url.startsWith("//")) {
            absoluteUrl = "http:" + url;
        } else if (url.startsWith("/")) {
            absoluteUrl = host + url;
        } else {
            absoluteUrl = host + "/" + url;
        }
        return absoluteUrl;
    }

    /**
     * Parse host from URL
     * @param url URL
     * @return host
     */
    public static String parseHost(String url) {
        if (url != null && url.trim().length() > 0) {

            String host;

            int startingPositionOfUrlAuthority;
            if (url.startsWith("https://")) {
                startingPositionOfUrlAuthority = 8;
            } else if (url.startsWith("http://")) {
                startingPositionOfUrlAuthority = 7;
            } else {
                startingPositionOfUrlAuthority = 0;
            }

            int positionOfClosingSlash = url.indexOf("/", startingPositionOfUrlAuthority);

            if (positionOfClosingSlash == -1) host = url;
            else host = url.substring(0, positionOfClosingSlash);

            return host;

        } else {
            return null;
        }
    }
}
