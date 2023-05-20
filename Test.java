import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class Test{

    private static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }

        return resultStringBuilder.toString();
    }

    public static String getFileContent(String fileName) {
        Class clazz;
        String res = null;
        InputStream inputStream;

        try {
            clazz = Test.class;
            inputStream = clazz.getResourceAsStream(fileName);
            res = readFromInputStream(inputStream);
            //File file = new File(classLoader.getResource("fileTest.txt").getFile());
            //inputStream = new FileInputStream(file);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static void testJson(String json) {
        try{
            JsonElement element = JsonParser.parseString(json);
            JsonObject obj = element.getAsJsonObject();
            System.out.println(obj.get("s").getAsString());
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        String content = getFileContent("./test");
        Pattern pattern = Pattern.compile("\\{.*\\}");
        Matcher matcher = pattern.matcher(content);

        boolean matchFound = matcher.find();
        if(matchFound) {
            for(int i=0; i <= matcher.groupCount(); i++) {
                // affichage de la sous-chaîne capturée
                //System.out.println("Groupe " + i + " : " + m.group(i));
                //System.out.println("Match found : "+  matcher.group());
                testJson(matcher.group());
            }
            //System.out.println("File : " + getFileContent("./test"));
            //System.out.println("Match found : "+  matcher.group());
        } else {
            System.out.println("Match not found");
        }
    }
}