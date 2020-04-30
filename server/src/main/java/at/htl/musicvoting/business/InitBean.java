package at.htl.musicvoting.business;

import at.htl.musicvoting.converter.VideoParser;
import at.htl.musicvoting.model.Video;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.transaction.Transactional;

import java.util.List;

/**
* In dieser Klasse werden beim Starten des Servers, standardmaessig ein paar Videos in die Playlist geladen.
*/
@ApplicationScoped
public class InitBean {

    @Inject
    @RestClient
    IYouTubeClient youTubeClient;

    @Inject
    VideoRepository videoRepository;

    @ConfigProperty(name = "youtube.apikey")
    public String apiKey = "";

    @ConfigProperty(name = "youtube.part")
    public String part = "";

    @ConfigProperty(name = "youtube.maxresults")
    public int maxResults = 0;

    @Transactional
    void init(@Observes StartupEvent ev) {
        JsonObject videos = youTubeClient.getVideos("Thunder", part,maxResults,apiKey,"video", 10,"");

        List<Video> elements = VideoParser.youTubeSearchToVideoParser(videos).getVideos();

        elements.forEach(x -> videoRepository.save(x));
    }
}
