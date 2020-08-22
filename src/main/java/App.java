import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class App {
    public static final String REMOTE_SERVICE_URI = "https://api.nasa.gov/planetary/apod?api_key=mrVUnENfjzYPa0Hh7fQXEQAlyklPf7Xx0Ka8SMtD";
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = getHttpClient();
        String nasaUrl = getNasaUrl(httpClient);
        if (nasaUrl == null) return;
        byte[] data = getData(httpClient, nasaUrl);
        String filename = nasaUrl.substring(nasaUrl.lastIndexOf('/') + 1);
        WriteDataToFile(data, filename);
    }

    private static void WriteDataToFile(byte[] data, String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(data);
        } catch (Exception ex) {
            System.out.println("oops");
        }
    }

    private static byte[] getData(CloseableHttpClient httpClient, String nasaUrl) throws IOException {
        HttpGet request = new HttpGet(nasaUrl);
        CloseableHttpResponse response = httpClient.execute(request);
        return response.getEntity().getContent().readAllBytes();
    }

    private static String getNasaUrl(CloseableHttpClient httpClient) throws IOException {
        HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
        CloseableHttpResponse response = httpClient.execute(request);
        String nasaUrlJson = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
        String nasaUrlStr = null;
        try {
            ObjectNode node = mapper.readValue(nasaUrlJson, ObjectNode.class);
            if (node.has("url"))
                nasaUrlStr = node.get("url").textValue();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return nasaUrlStr;
    }

    private static CloseableHttpClient getHttpClient() {
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
    }

}
