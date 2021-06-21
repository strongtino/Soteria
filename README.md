# Soteria

This is a system that allows you to manage licenses for various software. 
  
The backend is built using REST API which has one GET request endpoint responding with the information about validation and MongoDB which stores all the request logs, licenses etc. The frontend is built upon the JDA API which hooks into the Discord bot meaning the licenses and everything else is manageable using Discord bot commands.

### Usage
You need to have a dedicated machine that will be running the backend all the time which will be responding to the license requests.  
In every software that you want to implement the licensing you need to make an implementation that will check if the license is valid before your application starts.  
  
A simple implementation example can be seen below: 

```java
public class ImplementationExample {

    public static void main(String[] args) {
        if (checkLicense("C5NPG5M922GLPG0J6NM77GKVN7TC8IEH", "Soteria")) {
            // Do stuff...
        } else {
            System.exit(1);
        }
    }

    private static boolean checkLicense(String license, String software) {
        try {
            URL url = new URL("http://localhost:8080/license?key=" + license + "&software=" + software);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");

            // Too Many Requests
            if (connection.getResponseCode() == 429) {
                respond("You've sent too many license requests.", "Please wait a few minutes and try again.");
                return false;
            }
            InputStream response = connection.getInputStream();
            JsonObject object = JsonParser.parseReader(new InputStreamReader(response, StandardCharsets.UTF_8)).getAsJsonObject();

            // Just in case
            if (object == null) {
                respond("Something went wrong while trying to validate the software.", "Please try again or contact the software distributor.");
                return false;
            }
            boolean valid = object.get("validationType").getAsString().equals("VALID");
            String user = object.get("user").isJsonNull() ? null : object.get("user").getAsString();

            if (!valid || user == null) {
                respond("License key is invalid. If you think that's a", "mistake please contact the software distributor.");
                return false;
            }
            respond("Hello " + user + ", thanks for purchasing " + software + '!');
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            respond("Something went wrong while trying to validate the software.", "Please try again or contact the software distributor.");
            return false;
        }
    }

    private static void respond(String... text) {
        System.out.println(String.join("", Collections.nCopies(50, "#")));
        Stream.of(text).forEach(System.out::println);
        System.out.println(String.join("", Collections.nCopies(50, "#")));
    }
}
```

### Warning
You need to make sure to properly obfuscate your software because otherwise it can be easily removed with a decompiler.

### Contacts
If you think that something could be improved, you can either create a pull request or contact me on one of the socials, I would highly appreciate that!  
  
Discord: **valentino#9447**  
Twitter: **@strongtinofc**
