package at.htl.musicvoting.rest;

import at.htl.musicvoting.business.IYouTubeClient;
import at.htl.musicvoting.converter.VideoParser;
import at.htl.musicvoting.model.Video;
import at.htl.musicvoting.model.YoutubeResult;
import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/video")
public class VideoEndpoint {
    @Inject
    @RestClient
    IYouTubeClient youTubeClient;

    @ConfigProperty(name = "youtube.apikey1")
    public String apiKey1 = "";

    @ConfigProperty(name = "youtube.apikey2")
    public String apiKey2 = "";

    @ConfigProperty(name = "youtube.apikey3")
    public String apiKey3 = "";

    @ConfigProperty(name = "youtube.apikey4")
    public String apiKey4 = "";

    @ConfigProperty(name = "youtube.part")
    public String part = "";

    @ConfigProperty(name = "youtube.maxresults")
    public int maxResults = 0;

    private int index = 0;


    /**
     * Diese Methode ermöglicht es, YouTube zu durchsuchen
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVideos(@QueryParam("query") String query, @DefaultValue("") @QueryParam("pagetoken") String pagetoken) {
        String apiKey = "";
        switch (index) {
            case 0:
                apiKey = apiKey1;
                break;
            case 1:
                apiKey = apiKey2;
                break;
            case 2:
                apiKey = apiKey3;
                break;
            case 3:
                apiKey = apiKey4;
                break;
        }
        if (++index == 4){
            index = 0;
        }
        System.out.println("using api key: " + index+1);
        System.out.println(apiKey);
        JsonObject json = youTubeClient.getVideos(query, part, maxResults, apiKey, "video", 10, pagetoken);
        YoutubeResult result = VideoParser.youTubeSearchToVideoParser(json);
        System.out.println("result.getVideos() = " + result.getVideos());
        return Response.ok(result).build();
    }
}
