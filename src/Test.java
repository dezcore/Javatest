package src;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
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

    public static void writeFile(String fileName, String content) {
        FileOutputStream fos;
        DataOutputStream outStream;

        try {
            fos = new FileOutputStream(fileName);
            outStream = new DataOutputStream(new BufferedOutputStream(fos));
            outStream.writeUTF(content);
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLargeFile(String fileName, String fileContent) {
        byte[] strBytes;
        ByteBuffer buffer;
        FileChannel channel;
        RandomAccessFile stream;

        try {
            stream = new RandomAccessFile(fileName, "rw");
            channel = stream.getChannel();
            strBytes = fileContent.getBytes();
            buffer = ByteBuffer.allocate(strBytes.length);
            buffer.put(strBytes);
            buffer.flip();
            channel.write(buffer);
            stream.close();
            channel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void parseHtml(String filePath, String html) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("script");

        for(Element element : elements) {
            if(element.data().contains("DOCS_modelChunk =")) {
                writeLargeFile(filePath, element.data());
                //System.out.println("element : " + element.data());
            }
        }
    }
    
    public static void jsonObjHandler(JsonObject obj) {
        //JsonElement current;
        if(obj != null) {
            System.out.println(obj.get("s").getAsString());
        }
    }

    public static void jsonArrayHandler(JsonArray array) {
        JsonObject jsonObj;
        JsonElement current, targetElement;

        if(array != null) {
            for(int i = 0; i < array.size(); i++) {
                current = array.get(i);

                if(current != null && current.isJsonObject()) {
                    jsonObj = current.getAsJsonObject();
                    targetElement = jsonObj.get("s");
                    if(targetElement != null)
                        System.out.println(targetElement.getAsString());
                }
            }
        }
    }

    public static void testJson(String json) {
        JsonElement element;
        
        try{
            element = JsonParser.parseString(json);
            if(element.isJsonArray()) {
                jsonArrayHandler(element.getAsJsonArray());
            } else if(element.isJsonObject()) {
                jsonObjHandler(element.getAsJsonObject());
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
    }

    public static void testRegex(String content) {
        Pattern pattern = Pattern.compile("\\[.*\\]");
        Matcher matcher = pattern.matcher(content);

        boolean matchFound = matcher.find();
        if(matchFound) {
            for(int i=0; i <= matcher.groupCount(); i++) {
                //System.out.println("testRegex : " + matcher.group(i));
                //System.out.println("Groupe " + i + " : " + m.group(i));
                //System.out.println("Match found : "+  matcher.group());
                testJson(matcher.group());
            }
        } else {
            System.out.println("Match not found");
        }
    }
    public static void main(String[] args) {
        String content = "";
        String htmlFilePath = "./data/text";
        String html = getFileContent("../data/test.html");
         //String content = getFileContent("./data/test");
        parseHtml(htmlFilePath, html);
        content = getFileContent("../data/text");
        //System.out.println(content);
        testRegex(content);
    }
}