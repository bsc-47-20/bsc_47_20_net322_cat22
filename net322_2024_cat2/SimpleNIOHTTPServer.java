
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;

class SimpleNIOHTTPServer implements HTTPServerHandler {

    private String bindAddress;
    private int bindPort;

    public SimpleNIOHTTPServer(String bindAddress, int bindPort) {
        this.bindAddress = bindAddress;
        this.bindPort = bindPort;
    }

    public void run() {
        try {
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverChannel.socket();
            Selector selector = Selector.open();
            InetSocketAddress localPort = new InetSocketAddress(bindAddress, bindPort);
            serverSocket.bind(localPort);
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    try {
                        if (key.isAcceptable()) {
                            ServerSocketChannel server = (ServerSocketChannel) key.channel();
                            SocketChannel channel = server.accept();
                            channel.configureBlocking(false);
                            channel.register(selector, SelectionKey.OP_READ);
                        } else if (key.isReadable()) {
                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(4096);
                            int bytesRead = channel.read(buffer);
                            if (bytesRead == -1) {
                                channel.close();
                                continue;
                            }
                            buffer.flip();
                            String request = new String(buffer.array(), 0, bytesRead);
                            String response = processRequest(request);
                            buffer.clear();
                            buffer.put(response.getBytes());
                            buffer.flip();
                            channel.register(selector, SelectionKey.OP_WRITE, buffer);
                        } else if (key.isWritable()) {
                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();
                            channel.write(buffer);
                            if (!buffer.hasRemaining()) {
                                channel.close();
                            }
                        }
                    } catch (IOException ex) {
                        key.cancel();
                        try {
                            key.channel().close();
                        } catch (IOException cex) {
                            cex.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String processRequest(String request) {
        String[] lines = request.split("\r\n");
        String[] firstLine = lines[0].split("\\s+");
        String method = firstLine[0];
        String path = firstLine[1];

        if (method.equals("GET")) {
            if (path.equals("/")) {
                return readFileContent("index.html");
            } else if (path.equals("/register")) {
                return readFileContent("register.html");
            }
        } else if (method.equals("POST") && path.equals("/register")) {
        
            String formData = lines[lines.length - 1];
            String[] formFields = formData.split("&");
            String username = formFields[0].split("=")[1];
            String email = formFields[1].split("=")[1];
            saveFormData(username, email);
            return "HTTP/1.1 200 OK\r\nContent-Length: 20\r\n\r\nRegistration success";
        }

        return "HTTP/1.1 404 Not Found\r\nContent-Length: 13\r\n\r\n404 Not Found";
    }

    private String readFileContent(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            return "HTTP/1.1 200 OK\r\nContent-Length: " + content.length() + "\r\n\r\n" + content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "HTTP/1.1 500 Internal Server Error\r\nContent-Length: 24\r\n\r\n500 Internal Server Error";
        }
    }

    private void saveFormData(String username, String email) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("db.txt", true));
            writer.write(username + " " + email + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public interface HTTPServerHandler {
    void run();
}