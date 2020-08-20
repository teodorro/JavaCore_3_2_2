import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = { "date", "explanation", "media_type", "service_version", "title", "hdurl" })
public class NasaUrl {

    private String url;

    public NasaUrl(@JsonProperty("url") String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
