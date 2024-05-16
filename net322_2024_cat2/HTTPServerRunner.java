public class HTTPServerRunner {

    public static void main(String[] args) {

        String bindAddress="localhost"; // Initialize with first commandline program argument
        int bindPort=80; // Initialize with second commandline program argument

        SimpleNIOHTTPServer simpleNioHttpServer = new SimpleNIOHTTPServer(bindAddress, bindPort);
        simpleNioHttpServer.run();
    }
}
