import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/***
 * Victor Diaz
 * 1000858671
 * September 30th, 2014
 * This is the multi-threaded code for the http web server. It will spin off a thread for each client
 * connection.
 */
public class HttpWebServer extends Thread {

    // Initializing the listening socket on the server
    private static ServerSocket listeningSocket = null;

    // initiating the client socket
    private static Socket clientSocket = null;

    public static void main(String[] args) {
        // if we are in debug mode use the default port number in Constants.java
        if(Constants.DEBUG) {
            int portNumber = Constants.port;
            // use the user defined port number
        } else {
            if(args.length != 1) {
                System.err.println("Usage: java HttpWebServer <port number>");
                System.exit(1);
            }

            int portNumber = Integer.parseInt(args[0]);
        }

        // initiating the boolean value to declare if the server is listening for connections
        boolean isListening = true;

        try {
            // open up a new socket at the port number specified
            listeningSocket = new ServerSocket(Constants.port);
            System.out.println("Opened socket on port: " + Constants.port);

            // loop to keep the server listening for more connections
            while(isListening) {
                // call the method to connect a client if he sends a request
                ConnectClient();
            }

        } catch(IOException ioException) {
            System.err.println("Could not listen on port: " + ioException);
        }
    }

    /***
     * This function will connect a client to our server and prepare to send a response back to the client.
     */
    private static void ConnectClient() {
        try {
            // wait until accept a connection
            clientSocket = listeningSocket.accept();
            System.out.println("Successfully listening on port: " + Constants.port);
            System.out.println("Client has made a connection. :)");

            // initiating the connection object to process our response
            Connection clientConnection = new Connection(clientSocket);

            // throw the client connection into a new thread to start listening for more connections
            Thread clientConnectionThread = new Thread(clientConnection);

            // start running the thread
            clientConnectionThread.start();

            // catch an IOException if you cant listen on the port
        } catch (IOException ioException) {
            System.err.println("Could not listen on port: " + Constants.port);
        }
    }
}
