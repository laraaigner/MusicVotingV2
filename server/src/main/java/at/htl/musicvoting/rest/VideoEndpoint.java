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

    @ConfigProperty(name = "youtube.apikey")
    public String apiKey = "";

    @ConfigProperty(name = "youtube.part")
    public String part = "";

    @ConfigProperty(name = "youtube.maxresults")
    public int maxResults = 0;


    /**
     * Diese Methode ermöglicht es, YouTube zu durchsuchen
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVideos(@QueryParam("query") String query, @DefaultValue("") @QueryParam("pagetoken") String pagetoken) {
        System.out.println(part);
        JsonObject json = youTubeClient.getVideos(query, part, maxResults, apiKey,"video", 10, pagetoken);
        YoutubeResult result = VideoParser.youTubeSearchToVideoParser(json);
        System.out.println("result.getVideos() = " + result.getVideos());
        return Response.ok(result).build();
    }
}
