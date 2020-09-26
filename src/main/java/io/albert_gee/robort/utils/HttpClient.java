package io.albert_gee.robort.utils;

import io.albert_gee.robort.entities.HtmlDocument;
import io.albert_gee.robort.interfaces.Document;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

    private final String uri;
    private final HttpURLConnection connection;

    @Getter
    private Document document;

    @Getter
    private int statusCode;

    public HttpClient(String uri) throws IOException {
        this.uri = uri;
        connection = getConnection();
        setStatusCode();
        setDocument();
    }

    /**
     * Sets connection to URL
     */
    private HttpURLConnection getConnection() throws IOException {
        HttpURLConnection connection = this.connection;

        if (this.connection == null) {

            URL url = new URL(uri);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.connect();
            connection = c;
        }

        return connection;
    }

    /**
     * Get content type, e.g. "application/xml"
     * @return content type
     */
    public String getContentType() {
        return connection.getContentType();
    }

    private void setStatusCode() throws IOException {
        if (getConnection() != null) {
            this.statusCode = getConnection().getResponseCode();
        }
    }

    /**
     * Open URL and retrieve the content
     */
    private void setDocument() {
        if (getStatusCode() == 200 && getContentType() != null && getContentType().contains("text/html")) {
            try {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    StringBuilder sb = new StringBuilder();

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        sb.append(System.lineSeparator());
                    };
                    in.close();

                    document = new HtmlDocument(sb.toString(), uri, statusCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
