import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * This class describes the web crawler.
 * It can fetch links recursively of a website any depth.
 */
public class WebCrawler {

    // Name of the file where results will be written
    private File outputFile;

    // Host of website to be analyzed
    public UrlLink host;

    // Set of analyzed links
    private Set<String> buffer;

    // Map of URLs where the key is the URL and the value is where the URL was found
    private Set<UrlLink> mailOrTels;
    private Set<UrlLink> internalPageLinks;
    private Set<UrlLink> externalPageLinks;
    private Set<UrlLink> internalFileLinks;
    private Set<UrlLink> invalidLinks;

    /**
     * This constructor takes host of the website to be checked and fetches all links from it
     * @param host - host of the website to be checked
     */
    public WebCrawler(String host) {

        this.buffer             = new HashSet<>();

        this.mailOrTels         = new HashSet<>();
        this.internalPageLinks  = new HashSet<>();
        this.internalFileLinks  = new HashSet<>();
        this.externalPageLinks  = new HashSet<>();
        this.invalidLinks       = new HashSet<>();

        try {
            setHost(host);
            setOutputFile();
            fetchWebPageLinks(this.getHost());
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Gets host of website to be analyzed
     * @return UrlLink object of host of website
     */
    public UrlLink getHost() {
        return this.host;
    }

    /**
     * @return name of the file where results will be printed
     */
    public File getOutputFile() {
        return this.outputFile;
    }

    /**
     * Sets host of website to be checked
     * @param host - host of the website
     */
    private void setHost(String host) throws IOException {
        if (host == null) throw new IllegalArgumentException("You are trying to analyze a website, but you didn't provide its host");
        this.host = new UrlLink(host.trim(), null, host);
    }

    /**
     * Sets the file where results will be written
     */
    private void setOutputFile() {
        this.outputFile = new File("review" + System.currentTimeMillis() + ".txt");
    }

    /**
     * Check if link is in buffer
     * @param link - link
     * @return boolean
     */
    private boolean isLinkInBuffer(String link) {
        return this.buffer.contains(link);
    }

    /**
     * Gets all website links and categorized them
     * @param link
     * @throws IOException
     */
    public void fetchWebPageLinks(UrlLink link) throws IOException {
        // If Link is already in buffer, skip it
        if (!isLinkInBuffer(link.getUrl())) {

            // If not, add it to buffer
            this.buffer.add(link.getUrl());
            System.out.println("Link added to buffer: " + link);

            // Link is mailto: or tel:
            if (link.isMailOrTel()) {
                this.mailOrTels.add(link);

            } else {
                // Link is empty or returns 200 status code
                if (!link.isValid()) {
                    this.invalidLinks.add(link);

                // Link is valid
                } else {

                    // Link is external
                    if (!link.isInternal(this.getHost().getUrl())) {
                        this.externalPageLinks.add(link);

                    // Link is internal
                    } else {

                        // Link is internal file
                        if (!link.isWebPage()) {
                            this.internalFileLinks.add(link);

                        } else {

                            // Link is web page
                            this.internalPageLinks.add(link);

                            Set<String> pageLinks = link.getPageLinks();
                            for (String pageLink : pageLinks) {

                                UrlLink preparedLink = new UrlLink(pageLink, link, this.getHost().getUrl());
                                fetchWebPageLinks(preparedLink);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Prints all links to file
     */
    public void print() {
        printLinksToFile(this.internalPageLinks, "Internal Pages");
        printLinksToFile(this.internalFileLinks, "Internal Files");
        printLinksToFile(this.externalPageLinks, "External Pages");
        printLinksToFile(this.invalidLinks, "Invalid Links");
        printLinksToFile(this.mailOrTels, "Email addresses and telephone numbers");
    }

    /**
     * Prints links to file
     * @param linksSet - set of links to be printed
     * @param title - Title preceding rows os links
     */
    private void printLinksToFile(Set<UrlLink> linksSet, String title) {

        System.out.println("Started printing " + title + ". Size: " + linksSet.size());

        try {

            FileWriter fw = new FileWriter(this.getOutputFile(), true);

            StringBuilder content = new StringBuilder(title + ":\n");

            List<UrlLink> list = new ArrayList<>(linksSet);
            Collections.sort(list);

            for (UrlLink link : list) {
                content.append(link.getAnalyze()).append("\n");
            }
            content.append("\n");

            fw.write(content.toString());
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
