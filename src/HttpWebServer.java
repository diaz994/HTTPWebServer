import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpWebServer extends Thread {

    private static ServerSocket listeningSocket = null;
    private static Socket clientSocket = null;
    String httpRootDirectory;

    public static void main(String[] args) {
        if(Constants.DEBUG) {
            int portNumber = Constants.port;
        } else {
            if(args.length != 1) {
                System.err.println("Usage: java HttpWebServer <port number>");
                System.exit(1);
            }

            int portNumber = Integer.parseInt(args[0]);
        }

        boolean isListening = true;

        try {
            listeningSocket = new ServerSocket(Constants.port);
            System.out.println("Opened socket on port: " + Constants.port);

            while(isListening) {
                ConnectClient();
            }

        } catch(IOException ioException) {
            System.err.println("Could not listen on port: " + ioException);
        }
    }

    private static void ConnectClient() {
        try {
            clientSocket = listeningSocket.accept();
            System.out.println("Successfully listening on port: " + Constants.port);
            System.out.println("Client has made a connection. :)");


            Connection clientConnection = new Connection(clientSocket);
            ServerHttpResponse httpResponse = new ServerHttpResponse();

            Thread clientConnectionThread = new Thread(clientConnection);
            clientConnectionThread.start();

        } catch (IOException ioException) {
            System.err.println("Could not listen on port: " + Constants.port);
        }
    }
}
