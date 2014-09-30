import javax.rmi.CORBA.Util;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by diaz994 on 9/25/2014.
 */
public class Connection implements Runnable {

    Socket socket;
    BufferedReader input = null;
    DataOutputStream output = null;

    public Connection(Socket socket) throws IOException{
        this.socket = socket;
//        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        output = new DataOutputStream(socket.getOutputStream());
//        getRequest();
    }

    public String getRequest() throws IOException {
        String request = null;
        while((request = input.readLine()) != null) {
            System.out.println("Request: " + request);
        }

        return request;
    }

    private void CreateServerResponse() throws Exception {
        String clientRequest = "";

        String tempRequestHold = "";

        String requestMessage = "";

        String clientRequestUnparsedPath = "";

        File clientRequestFilePath = null;

        String fileName = "";

        String clientAbsolutePath = "";


        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new DataOutputStream(socket.getOutputStream());

            while((tempRequestHold = input.readLine()) != null) {
                requestMessage += (tempRequestHold + "\r\n");
                System.out.println("Request: " + tempRequestHold);

                if(tempRequestHold.trim().equals("")) {
                    break;
                }
            }

            requestMessage += ("     ");

            if(requestMessage.trim().equals("") == false) {
                System.out.println("Request is empty");

                String[] requestMsgLines = requestMessage.split("\r\n");

                clientRequest = requestMsgLines[0];

                System.out.println("clientRequest: " + clientRequest);

                String[] clientRequestSplit = clientRequest.split(" ");

                clientRequestUnparsedPath = clientRequestSplit[1];

                System.out.println("clientRequestUnparsedPath: " + clientRequestUnparsedPath);

                if(clientRequestUnparsedPath.trim().equals("/") == false) {
                    String parsedClientRequestPath = Utils.ParseFilePath(clientRequestUnparsedPath);

                    clientRequestFilePath = new File(parsedClientRequestPath);
                    clientAbsolutePath = clientRequestFilePath.getAbsolutePath();

                    fileName = Utils.GetFileNameOnly(clientAbsolutePath);
                } else {
                    clientRequestFilePath = new File(Constants.DEFAULT_FILE_PATH);
                    fileName = Constants.DEFAULT_FILE_NAME;
                }

                File file = new File(clientAbsolutePath);
                if(file.exists() == true) {
                    String contentType = Utils.GetContentType(fileName);
                    if (contentType.equals("text/html")) {
                        ResponseMessageHandler.HtmlResponseMessage(file, output);
                        System.out.println(output.toString());
                    }
                    if (contentType.equals("image")) {
                        ResponseMessageHandler.ImageResponseMessage(file, output);
                    }
                    System.out.println("Check: true");
                } else {
                    String newUrlAddress = "";

                    if(clientRequestUnparsedPath.charAt(clientRequestUnparsedPath.length() - 1) == '/') {
                        clientRequestUnparsedPath = clientRequestUnparsedPath.substring(0, clientRequestUnparsedPath.length() - 1);
                    } else {
                        // TODO: Send 404 response
                    }

                    String host = (Utils.FindHostHeaderLine(requestMsgLines).split(" "))[1].trim();
                }
            } else {
                throw new BadHttpRequestException();
            }
        } catch (SocketException socketException) {
            System.out.println(socketException.getMessage());
        } catch(IOException ioException) {
            System.out.println(ioException.getMessage());
        } catch(BadHttpRequestException badHttpRequestException) {
            System.out.println(badHttpRequestException.getMessage());
        } finally {
            if(input != null) {
                input.close();
            }

            if(output != null) {
                output.close();
            }
        }
    }

    @Override
    public void run() {
        try {
            CreateServerResponse();
        } catch(Exception exception) {
            System.err.println(exception.getStackTrace());
        }
    }

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
