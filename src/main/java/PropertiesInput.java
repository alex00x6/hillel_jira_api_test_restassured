import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class PropertiesInput {

    Properties prop = new Properties();
    InputStream input = null;

    public HashMap<String, String> readProperties() {
        HashMap<String, String> credentials = new HashMap<>();

        try {
            input = new FileInputStream("content.properties");

            // load a properties file
            prop.load(input);

            // get the properties value and save them in Map
            credentials.put("issue_key", prop.getProperty("issue_key"));
            credentials.put("comment_id", prop.getProperty("comment_id"));
            credentials.put("issue_type", prop.getProperty("issue_type"));
            credentials.put("comment_text", prop.getProperty("comment_text"));
            credentials.put("summary_text", prop.getProperty("summary_text"));
            credentials.put("login", prop.getProperty("login"));
            credentials.put("password_right", prop.getProperty("password_right"));
            credentials.put("password_wrong", prop.getProperty("password_wrong"));
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return credentials;
    }
}
