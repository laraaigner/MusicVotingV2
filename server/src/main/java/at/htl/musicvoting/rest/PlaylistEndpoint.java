package at.htl.musicvoting.rest;


import at.htl.musicvoting.business.PlaylistHandler;
import at.htl.musicvoting.business.VideoRepository;
import at.htl.musicvoting.converter.VideoParser;
import at.htl.musicvoting.model.Playlist;
import at.htl.musicvoting.model.Video;
import at.htl.musicvoting.rest.auth.annotation.Secured;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

@Path("playlist")
@ApplicationScoped
public class PlaylistEndpoint {
    private Sse sse;
    private SseBroadcaster sseBroadcaster;

    @Inject
    PlaylistHandler playlist;

    @Inject
    VideoRepository videoRepository;

    @Context
    public void setSse (Sse sse) {
        this.sse = sse;
    }

    /**
     * Für Clienten um sich auf den Sse Clienten zu connecten
     */
    @GET
    @Path("/connect")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void register(@Context SseEventSink eventSink) {
        if(sseBroadcaster == null)
            sseBroadcaster = sse.newBroadcaster();

        System.out.println("new client connected");
        eventSink.send(sse.newEvent("Welcome!"));
        sseBroadcaster.register(eventSink);
    }

    /**
     * fügt ein Video in die Playlist hinzu und benachrichtigt die Clienten über Sse
     */
    @POST
    @Path("/add/video")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void addVideo(JsonObject object) {
        Video video = VideoParser.postedObjectToVideo(object);
        playlist.addVideo(video);
        System.out.println("video added");
        video = playlist.get(video.getVideoId());
        OutboundSseEvent event = sse.newEventBuilder()
                .name("add_video")
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(Json.createObjectBuilder()
                        .add("updateId", this.playlist.getPlaylist().getUpdateId())
                        .add("video", Json.createObjectBuilder()
                                .add("videoId", video.getVideoId())
                                .add("title", video.getTitle())
                                .add("channel", video.getChannel())
                                .add("thumbnail", video.getThumbnail())
                                .add("addedToPlaylist", video.getAddedToPlaylist())
                                .add("votes", video.getVotes())
                        )
                        .build())
                .build();
        broadcastMessage(event);
    }

    /**
     * löscht ein Video aus der Playlist und benachrichtig die Clienten mittels Sse
     */
    @POST
    @Path("/remove/video")
    @Secured
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void removeSong(@QueryParam("videoId")  String videoId) {
        if (videoId != null && playlist.contains(videoId)) {
            System.out.println("song removed");
            playlist.removeVideo(videoId);
            OutboundSseEvent event = sse.newEventBuilder()
                    .name("remove_video")
                    .mediaType(MediaType.APPLICATION_JSON_TYPE)
                    .data(Json.createObjectBuilder()
                            .add("updateId", this.playlist.getPlaylist().getUpdateId())
                            .add("videoId", videoId)
                            .build())
                    .build();
            broadcastMessage(event);
        }
    }

    /**
     * Fügt einen Vote zu dem Video hinzu und benachrichtig die Clienten mittels Sse
     */
    @POST
    @Path("/add/vote")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void vote(@QueryParam("videoId") String videoId) {
        System.out.println("entered addVote + "+videoId);
        if (videoId != null && playlist.contains(videoId)) {
            playlist.addVote(videoId);
            System.out.println("vote added");
            OutboundSseEvent event = sse.newEventBuilder()
                    .name("add_vote")
                    .mediaType(MediaType.APPLICATION_JSON_TYPE)
                    .data(Json.createObjectBuilder().add("updateId", this.playlist.getPlaylist().getUpdateId()).add("videoId", videoId).build())
                    .build();
            broadcastMessage(event);
        }
    }

    /**
     * Löscht einen Vote von dem Video und benachrichtig die Clienten mittels Sse
     */
    @POST
    @Path("/remove/vote")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void removeVote(@QueryParam("videoId") String videoId) {
        if (videoId != null && playlist.contains(videoId)) {
            playlist.removeVote(videoId);
            System.out.println("vote removed");
            OutboundSseEvent event = sse.newEventBuilder()
                    .name("remove_vote")
                    .mediaType(MediaType.APPLICATION_JSON_TYPE)
                    .data(Json.createObjectBuilder().add("updateId", this.playlist.getPlaylist().getUpdateId()).add("videoId", videoId).build())
                    .build();
            broadcastMessage(event);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaylist() {
        Playlist entity = playlist.getPlaylist();
        return Response.ok(entity).build();
    }

    /**
     * Gibt das nächste Video zurück
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/peek")
    public Response peek() {
        Video video = playlist.peek();
        if(video != null){
            return Response.ok(video).build();
        }
        return Response.noContent().build();
    }


    /**
     * Gibt das nächste Video von der Playlist zurück
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/pop")
    public Response pop() {
        Video video = playlist.playVideo();
        if (video == null)
            video = playlist.playRandom();
        else {
            OutboundSseEvent event = sse.newEventBuilder()
                    .name("remove_video")
                    .mediaType(MediaType.APPLICATION_JSON_TYPE)
                    .data(Json.createObjectBuilder()
                            .add("updateId", this.playlist.getPlaylist().getUpdateId())
                            .add("videoId", video.getVideoId())
                            .build())
                    .build();
            broadcastMessage(event);
        }
        broadcastNextVideo(video);
        return Response.ok(video).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/version")
    public Response getUpdateId() {
        return Response.ok(playlist.getPlaylist().getUpdateId()).build();
    }

    public void broadcastChange() {
        OutboundSseEvent event = sse.newEventBuilder()
                .name("change")
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(playlist.getPlaylist())
                .build();
        broadcastMessage(event);
    }

    /**
     * Schickt eine Message, wenn ein neues Video gestartet wurde
     */
    public void broadcastNextVideo(Video video) {
        OutboundSseEvent event = sse.newEventBuilder()
                .name("video_started")
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(video)
                .build();
        broadcastMessage(event);
    }

    /**
     * Diese Methode wird verwendet, um das senden von Messages zu vereinfachen.
     */
    public void broadcastMessage(OutboundSseEvent event){
        if(sseBroadcaster != null){

            sseBroadcaster.broadcast(event);
        }
    }
}
