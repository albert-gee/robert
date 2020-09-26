package io.albert_gee.robort.helpers;

/**
 * This class contains useful static methods for working with URIs
 */
public class URIHelper {

    /**
     * Get scheme from URI, e.g. "http", "mailto"
     * @param uri - URI
     * @return scheme of provided URI
     */
    public static String parseScheme(String uri) {
        if (uri == null || uri.trim().isEmpty()) {
            throw new IllegalArgumentException("Couldn't determine scheme - provided URI is null");
        }

        int firstOccurrenceOfColon = uri.indexOf(":"); // Finds position of first occurrence of colon in uriString

        return (firstOccurrenceOfColon != -1) ? uri.substring(0, firstOccurrenceOfColon) : null;
    }

    /**
     * Get authority from URI
     * Authority is the part of a URI between {scheme with semicolon and with two slashes} and {the first next slash},
     * e.g. https://  <=  john.doe@www.example.com:123  =>  /
     *
     * @param uri URI
     * @return authority
     */
    public static String parseAuthority(String uri) {

        if (uri == null || uri.trim().isEmpty()) {
            throw new IllegalArgumentException("Couldn't get authority from empty uri");
        }

        String result;

        // Starts with ONE slash
        if (uri.startsWith("/") && !uri.startsWith("//")) {
            result = null;
        } else {
            // Remove everything before first colon and the colon
            int semicolonPositionInUri = uri.indexOf(":");
            String uriWithoutScheme = (semicolonPositionInUri != -1) ? uri.substring(semicolonPositionInUri + 1) : uri;

            // Remove two slashes from the beginning
            int beginningSlashesPositionInUri = uriWithoutScheme.indexOf("//");
            String uriWithoutSchemeAndBeginningSlashes = (beginningSlashesPositionInUri != -1) ?
                    uriWithoutScheme.substring(beginningSlashesPositionInUri + 2) :
                    uriWithoutScheme;

            // Remove everything after the next slash with the slash
            int nextFirstSlashPositionInUri = uriWithoutSchemeAndBeginningSlashes.indexOf("/");
            result = (nextFirstSlashPositionInUri != -1) ?
                    uriWithoutSchemeAndBeginningSlashes.substring(0, nextFirstSlashPositionInUri) :
                    uriWithoutSchemeAndBeginningSlashes;
        }

        return result;
    }
}
