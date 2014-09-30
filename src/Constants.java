

/**
 * Created by diaz994 on 9/27/2014.
 */
public class Constants {

    final public static int port = 6789;
    final public static boolean DEBUG = true;
    final public static String host = "localhost";

    // Response codes
    final public static String HTTP_RESPONSE_OK = "HTTP/1.1 200 OK\r\n";
    final public static String HTTP_RESPONSE_BAD_REQUEST = "HTTP/1.1 400 OK\r\n";
    final public static String HTTP_RESPONSE_NOT_FOUND = "HTTP/1.1 404 OK\r\n";

    final public static String output = "<html><head><title>Example</title></head><body><p>Worked!!!</p></body></html>";

    final public static String DEFAULT_FILE_PATH = "index.html";
    final public static String DEFAULT_FILE_NAME = "index.html";

    final public static String HTTP_RESPONSE_HEADER_CONTENT_TYPE_HTML ="Content-Type: text/html\r\n\r\n";
    final public static String HTTP_RESPONSE_HEADER_CONTENT_TYPE_IMAGE ="Content-Type: image\r\n\r\n";
}
