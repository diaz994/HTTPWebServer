/**
 * Created by diaz994 on 9/27/2014.
 */
public class Utils {
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

    /**
     * Name       : CountOccurencesOf
     * Input      : s as String, c as char
     * Output     : Number of occurrences of a char
     * Description: Count the number of occurrences of a char in a string
     * @param s
     * @param c
     * @return integer
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

    /**
     * Name       : GetFileNameOnly
     * Input      : Complete path of the file, including name
     * Output     : File name without path info
     * Description: Gets the file name without path info
     * @param path
     * @return path
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

    /**
     * Name       : GetContentType
     * Input      : File name to parse to get content type; eg: html/text, image
     * Output     : Content type as string
     * Description: Parses file name to get the content type
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
        } else if (switchCondition.equals(".jpg") || switchCondition.equals(".jpeg") || switchCondition.equals(".jp3") || switchCondition.equals(".jfif") || switchCondition.equals(".png") || switchCondition.equals(".gif") || switchCondition.equals(".bmp") || switchCondition.equals(".dib") || switchCondition.equals(".tif") || switchCondition.equals(".tiff") || switchCondition.equals(".ico")) {
            return "image";

            // Also return text/html for all the other file types
        } else {
            return "text/html";
        }
    }

    /**
     * Name       : FindHostHeaderLine
     * Input      : requestMsg as array of strings
     * Output     : Host header as string
     * Description: Returns the header line that contains host information
     * @param requestMsg
     * @return string/null
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
