/***
 * Victor Diaz
 * 1000858671
 * September 30, 2014
 */
public class Utils {

    /***
     *
     * @param path
     * @return
     */
    public static String ParseFilePath(String path)
    {
        // Remove any unnecessary ending slashes typed by the user
        if (path.endsWith("/"))
        {
            path = path.substring(0, path.length() - 1);
        }

        // Make sure the path is in correct format
        if ((CountOccurrencesOf(path, '/') > 1) && (CountOccurrencesOf(path, '.') > 1))
        {
            path = path.substring(path.lastIndexOf("/"));
        }

        // Check to see if there are any parent directories lised
        // in the path message
        if (path.substring(1).contains(("/")))
        {
            // Replace path with an IDE compatable path
            path = path.replace("/", "\\" + "\\");
            path = "." + path;
        }
        else
        {
            // No parent directory, remove path separator
            path = path.replace("/", "");
        }

        // Return the parsed path
        return path;
    }

    /***
     *
     * @param s
     * @param c
     * @return
     */
    public static int CountOccurrencesOf(String s, char c)
    {
        // Record occurrences of character in a string
        int count = 0;

        for (int i = 0; i < s.length(); i++)
        {
            // Check if the character in string is same as the given char
            if (s.toCharArray()[i] == c)
            {
                // Update the count
                count += 1;
            }
        }

        // Return the count
        return count;
    }

    /***
     *
     * @param path
     * @return
     */
    public static String GetFileNameOnly(String path)
    {
        // Check to see if the path contains parent directories info
        if (path.substring(1).contains(("\\")))
        {
            // Fileer the whole path to get file name only
            // return the file name
            return path.substring(path.lastIndexOf("\\"));
        }
        else
        {
            // return the file name
            return path;
        }
    }

    /***
     *
     * @param filename
     * @return
     */
    public static String GetContentType(String filename)
    {
        // If the path contains the directory instead of file name
        // Return default html content type
        if (!filename.contains("."))
        {
            return "text/html";
        }

        // Switch filenames based on file extension
        // Return the content type
        String switchCondition = filename.substring(filename.indexOf("."));
        if (switchCondition.equals(".html") || switchCondition.equals(".txt") || switchCondition.equals(".htm")) {
            return "text/html";
        } else if (switchCondition.equals(".jpg") || switchCondition.equals(".jpeg") || switchCondition.equals(".jp3")
                || switchCondition.equals(".jfif") || switchCondition.equals(".png") || switchCondition.equals(".gif")
                || switchCondition.equals(".bmp") || switchCondition.equals(".dib") || switchCondition.equals(".tif")
                || switchCondition.equals(".tiff") || switchCondition.equals(".ico")) {
            return "image";

            // Also return text/html for all the other file types
        } else {
            return "text/html";
        }
    }

    /***
     *
     * @param requestMsg
     * @return
     */
    public static String FindHostHeaderLine(String [] requestMsg)
    {
        // Record host header count
        int hostHeaderCount = 0;
        String hostHeader = "";

        // Check to make sure only one host header exists
        // and no space between 'Host' and ':'
        for (int i = 0; i < requestMsg.length; i++)
        {
            if (requestMsg[i].contains("Host:"))
            {
                // Update found host header count and get the header line
                hostHeaderCount += 1;
                hostHeader = requestMsg[i];
            }
        }

        // Exactly one host header line found; return that header line
        if (hostHeaderCount == 1)
        {
            return hostHeader;
        }

        // Error on host header line: return null
        return null;
    }
}
