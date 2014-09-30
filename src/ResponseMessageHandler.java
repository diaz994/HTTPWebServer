import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by diaz994 on 9/28/2014.
 */
public class ResponseMessageHandler {

    /***
     * Will build the html response message to send back to the client and to be rendered by the browser.
     * It takes in a file and the output stream of the socket and build the http response.
     * @param file
     * @param responseToClient
     */
    public static void HtmlResponseMessage(File file, DataOutputStream responseToClient) {
        // response data to client
        String responseData = "";

        // html data to send back to client
        String htmlData = "";

        // building the headers and content type
        responseData = Constants.HTTP_RESPONSE_OK;
        responseData += Constants.HTTP_RESPONSE_HEADER_CONTENT_TYPE_HTML;

        try {
            // putting an html file into the input stream
            InputStream inputStream = new FileInputStream(file.getAbsolutePath());

            // put the input stream to be able to read it
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // store the lines of the file
            String fileLines = "";

            // loop through each line
            while((fileLines = reader.readLine()) != null) {
                // add each line of html into the response html data
                htmlData += fileLines;
            }

            // write to the data output stream, the response headers and the html to be rendered by the browser
            responseToClient.writeBytes(responseData + htmlData);

            // catching the exceptions
        } catch(FileNotFoundException fileNotFoundException) {
            System.err.println(fileNotFoundException.getMessage());
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }
    }

    /***
     * The ImageResponseMessage function will take in a file (the html base file) and a DataOutputStream
     * to write the image to the client.
     * @param file
     * @param responseToClient
     */
    public static void ImageResponseMessage(File file, DataOutputStream responseToClient) {
        // response string to send back to client
        String responseData = "";

        // image to be shown to the client
        BufferedImage image = null;

        // building the headers for the response
        responseData = Constants.HTTP_RESPONSE_OK;
        responseData += Constants.HTTP_RESPONSE_HEADER_CONTENT_TYPE_IMAGE;

        try {
            // getting the file name of the image
            String fileName = file.getName();

            // getting the extension type
            String fileExtension = fileName.substring(fileName.indexOf(".") + 1);

            // reading the image and entering into the BufferedImage object
            image = ImageIO.read(file);

            // writing the response bytes to the client
            responseToClient.writeBytes(responseData);

            // writing the buffered image to the client
            ImageIO.write(image, fileExtension, responseToClient);

            // catching the IO exception
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }
    }
}
