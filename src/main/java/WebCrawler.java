import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class WebCrawler {

    // Set of analyzed links
    private Set<String> buffer;

    // Name of the file where results will be written
    private String outputFileName;

    // Map of URLs where the key is the URL and the value is where the URL was found
    private Set<UrlLink> internalPageLinks;
    private Set<UrlLink> externalPageLinks;
    private Set<UrlLink> internalFileLinks;
    private Set<UrlLink> invalidLinks;

    // Host of website
    public UrlLink host;


    private UrlLink currentLink;

    /**
     * This constructor takes host of the website to be checked and fetches all links from it
     * @param host - host of the website to be checked
     */
    public WebCrawler(String host) {

        setOutputFileName();
        this.buffer = new HashSet<>();

        this.internalPageLinks = new HashSet<>();
        this.internalFileLinks = new HashSet<>();
        this.externalPageLinks = new HashSet<>();
        this.invalidLinks      = new HashSet<>();

        try {
            setHost(host);
            fetchWebPageLinks(this.getHost());
        } catch (IOException e) {
            e.printStackTrace();
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
     * Sets host of website to be checked
     * @param host - host of the website
     */
    private void setHost(String host) throws IOException {
        if (host == null) throw new IllegalArgumentException("You are trying to analyze a website, but you didn't provide its host");
        if (!UrlValidator.isLinkValid(host)) throw new IllegalArgumentException("You are trying to analyze a website, but you provided invalid host");
        this.host = new UrlLink(host.trim(), null);
    }

    /**
     * Sets the file where results will be written
     */
    private void setOutputFileName() {
        this.outputFileName = "review" + System.currentTimeMillis() + ".txt";
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
        // If Link is already in buffer we skip it
        if (!isLinkInBuffer(link.getUrl().toString())) {

            // If not, add it to buffer and analyze
            this.buffer.add(link.getUrl().toString());

            System.out.println("Link added to buffer: " + link.getUrl().toString());

            // Link not empty and returns 200 status code
            if (UrlValidator.isLinkValid(link.getUrl().toString())) {

                // Link belongs to host
                if (UrlValidator.isLinkInternal(this.getHost().getUrl().toString(), link.getUrl())) {

                    if (UrlValidator.isWebPage(link.getUrl())) {
                        this.internalPageLinks.add(link);

                        Set<String> pageLinks = UrlService.getPageLinks(link);
                        for (String pageLink : pageLinks) {

                            if (UrlValidator.isLinkValid(pageLink)) {
                                UrlLink preparedLink = UrlService.prepareLink(pageLink, link, this.getHost());
                                fetchWebPageLinks(preparedLink);
                            }
                        }
                    } else {
                        this.internalFileLinks.add(link);
                    }
                } else {
                    this.externalPageLinks.add(link);
                }
            } else {
                this.invalidLinks.add(link);
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
    }

    /**
     * Prints links to file
     * @param linksSet - set of links to be printed
     * @param title - Title preceding rows os links
     */
    private void printLinksToFile(Set<UrlLink> linksSet, String title) {

        System.out.println("Started printing " + title);

        try {

            FileWriter fw = new FileWriter(this.outputFileName);

            String contentLine = title + ":\n";

            for (UrlLink internalPageLink : linksSet) {
                contentLine += UrlService.analyzePage(internalPageLink) + "\n";
                fw.append(contentLine);
            }

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
