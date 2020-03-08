/**
 * This class describes invalid link and its data
 *
 * @author Albert Gainutdinov
 * @version 1.0
 */
public class InvalidLink {

    // Invalid link
    private String link;

    // Parent link where the invalid link was found
    private UrlLink parent;

    public InvalidLink(String link, UrlLink parent) {
        setLink(link);
        setParent(parent);
    }

    /**
     * Sets URL of link
     * @param link - URL of link
     */
    public void setLink(String link) {
        this.link = (link != null) ? link.trim() : "";
    }

    /**
     * Parent link where invalid link was found
     * @param parent - parent link URL
     */
    public void setParent(UrlLink parent) {
        this.parent = parent;
    }

    public String getLink() {
        return this.link;
    }

    public UrlLink getParent() {
        return this.parent;
    }
}
