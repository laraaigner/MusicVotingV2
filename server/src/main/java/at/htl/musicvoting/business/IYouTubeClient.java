package at.htl.musicvoting.business;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


/**
 * Dieses Interface wird dazu verwendet, um Videos über YouTube zu suchen.
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/search")
@RegisterRestClient
public interface   IYouTubeClient {

    @GET
    JsonObject getVideos(
            @QueryParam("q") String query,
            @QueryParam("part") String part,
            @QueryParam("maxResults") int maxResults,
            @QueryParam("key") String apiKey,
            @QueryParam("type") String type,
            @QueryParam("videoCategoryId") int catId,
            @QueryParam("pageToken") String pageToken
    );
}
