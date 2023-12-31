package lambda;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.io.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringEscapeUtils;


public class LambdaInvokerUsingURLPayload {

    public static String invoke_lambda(String payloadString) throws Exception {
        InputStream inputStream = LambdaInvokerUsingURLPayload.class.getResourceAsStream("configs/JavaConfig.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String json="";
        String line;
        while ((line = reader.readLine()) != null) {
            json += line;
        }
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        String value = jsonObject.get("URL").getAsString();
        String functionUrl = value;
        //String payload = "{\"body\": \"test test\"}\"}";
        String payload = "{\"body\": \"" + payloadString + "\"}";

        URL url = new URL(functionUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestMethod("GET");
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.write(payload.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
        int responseCode = con.getResponseCode();
//        System.out.println("response code", String.valueOf(responseCode));
        InputStream inputStream2 = con.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream2, StandardCharsets.UTF_8));

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String jsonResponse = response.toString();
//        String decodedJson = StringEscapeUtils.unescapeJava(jsonResponse);

//        String[] parts = decodedJson.split(",");
//        String[] newArray = new String[parts.length - 2];
//        System.arraycopy(parts, 2, newArray, 0, newArray.length);
//        System.out.println(Arrays.toString(newArray));
        return jsonResponse;
    }
}
