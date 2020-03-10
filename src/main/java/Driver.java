public class Driver {

    public static void main(String[] args) {

        System.out.println("Fetching all links");
        WebCrawler wc = new WebCrawler("https://messino.creativepace.com");

        System.out.println("Started export");
        wc.print();
        System.out.println("Export finished");

    }
}
