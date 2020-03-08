public class Driver {

    public static void main(String[] args) {

        System.out.println("Fetching all links");
        WebCrawler wc = new WebCrawler("http://hocbr.loc");

        wc.print();

        System.out.println("Exporting finished");

    }
}
