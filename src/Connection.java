import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

/***
 * Victor Diaz
 * 1000858671
 * 9/30/2014
 * Connection.java
 * This class will create a client connection and send back an http response
 * for an http request
 */
public class Connection implements Runnable {

    // Initiation client socket
    Socket socket;

    // Initiating the input
    BufferedReader input = null;

    // Initiating the output
    DataOutputStream output = null;

    // Constructor initiating the socket
    public Connection(Socket socket) throws IOException{
        this.socket = socket;;
    }

    // Parsing the http request from the client
    public String getRequest() throws IOException {
        String request = null;
        // reading each line of the request
        while((request = input.readLine()) != null) {
            System.out.println("Request: " + request);
        }

        return request;
    }

    /***
     * This function will create a server response for a specific request
     * It will call to show the 404,400, and 200 http response codes
     * @throws Exception
     */
    private void CreateServerResponse() throws Exception {
        // this will hold the http request
        String clientRequest = "";

        // a temporary hold for the request to be used to store each line
        String tempRequestHold = "";

        // this will hold the entire reques message with line breaks
        String requestMessage = "";

        // the unparsed path of the html file that the client is requesting
        String clientRequestUnparsedPath = "";

        // initiating the file that the client is requesting
        File clientRequestFilePath = null;

        // the file name for the html file
        String fileName = "";

        // the absolute path to requested file
        String clientAbsolutePath = "";


        try {
            // attaching the input stream of the socket to a BufferedReader so it can be read by the class
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // create an output stream to write data to the socket
            output = new DataOutputStream(socket.getOutputStream());

            // store each line of the bytes coming through the socket to a temp var until it reaches the end
            while((tempRequestHold = input.readLine()) != null) {
                // write each line into the request message
                requestMessage += (tempRequestHold + "\r\n");

                // Writing the request out to the console
                System.out.println("Request: " + tempRequestHold);

                // if you reach the end of the header lines then exit the loop
                if(tempRequestHold.trim().equals("")) {
                    break;
                }
            }

            // add the empty space to the request to indicate the end of the request
            requestMessage += ("     ");

            // Check to see if the request message is empty
            if(requestMessage.trim().equals("") == false) {
                System.out.println("Request is empty");

                // split the reques into each line and store it into a string array
                String[] requestMsgLines = requestMessage.split("\r\n");

                // the client request will be on the first line to request a certain page
                clientRequest = requestMsgLines[0];

                System.out.println("clientRequest: " + clientRequest);

                // split the requested header line of the clients request into sections by white space
                String[] clientRequestSplit = clientRequest.split(" ");

                // the requested page as an unparsed path
                clientRequestUnparsedPath = clientRequestSplit[1];

                System.out.println("clientRequestUnparsedPath: " + clientRequestUnparsedPath);

                // if the user only put the host and port number then we want the base resource file to be
                // index.html
                if(clientRequestUnparsedPath.trim().equals("/") == false) {
                    String parsedClientRequestPath = Utils.ParseFilePath(clientRequestUnparsedPath);

                    clientRequestFilePath = new File(parsedClientRequestPath);
                    clientAbsolutePath = clientRequestFilePath.getAbsolutePath();

                    fileName = Utils.GetFileNameOnly(clientAbsolutePath);
                } else {
                    // the path is the default base file path
                    clientRequestFilePath = new File(Constants.DEFAULT_FILE_PATH);
                    // setting the defailt file name
                    fileName = Constants.DEFAULT_FILE_NAME;
                }

                // Creating a new file object wfrom the file
                File file = new File(clientAbsolutePath);

                // check if the file exists
                if(file.exists() == true) {
                    // retrieve the content type of the request
                    String contentType = Utils.GetContentType(fileName);
                    // if the content type of html send an html response
                    if (contentType.equals("text/html")) {
                        // Call the message handler to send the response after we pass in the file
                        // to be parsed and the output stream to be written to
                        ResponseMessageHandler.HtmlResponseMessage(file, output);
                    }

                    // if the content type is image then send the image response message
                    if (contentType.equals("image")) {
                        // call the message handler to send back an image response
                        // pass in the file and the output stream to be written to
                        ResponseMessageHandler.ImageResponseMessage(file, output);
                    }
                } else {
                    // if the requested resource does not exist then send a 404 page
                    ResponseMessageHandler.Error404Message(file, output);
                }
                // if all else fails then that means it was a bar request
                // throw a bad http request exception to be caught
            } else {
                throw new BadHttpRequestException();
            }
            // catch a socket exception if it occurs
        } catch (SocketException socketException) {
            System.out.println(socketException.getMessage());
            // catch an IOException if it occurs
        } catch(IOException ioException) {
            System.out.println(ioException.getMessage());
            // catch the bad request exception and show the 400 message
        } catch(BadHttpRequestException badHttpRequestException) {
            File file = new File(clientAbsolutePath);
            ResponseMessageHandler.BadRequest400Message(file, output);
            // closing the streams
        } finally {
            // if the input stream is not null (it has content) then close it
            if(input != null) {
                input.close();
            }
            // if the output writer has content close it
            if(output != null) {
                output.close();
            }
        }
    }

    /***
     * One of the methods to override as part of the Runnable interface.
     * It calls the CreateServerResponse() method on the thread
     */
    @Override
    public void run() {
        try {
            CreateServerResponse();
        } catch(Exception exception) {
            System.err.println(exception.getStackTrace());
        }
    }

    /***
     * Closes the objects initialized in run after it has finished.
     */
    @Override
    protected void finalize() {
        try {
            if(socket != null) {
                socket.close();
            }
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        } finally {
            try {
                super.finalize();
            } catch(Throwable exception) {
                System.err.println(exception.getMessage());
            }
        }
    }
}
