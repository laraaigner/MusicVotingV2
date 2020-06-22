package at.htl.musicvoting.business;


import at.htl.musicvoting.model.Playlist;
import at.htl.musicvoting.model.Video;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Diese Klasse haltet die Liste der Videos.
 */
@ApplicationScoped
public class PlaylistHandler {

    private static final Logger LOG = Logger.getLogger(PlaylistHandler.class);

    @Inject
    PlaylistHolder playlistHolder;

    @Inject
    VideoRepository videoRepository;

    private Video actualVideo;
    private List<Video> playlist = new LinkedList<>();

    private Comparator<Video> comparator = Comparator.comparing(Video::getVotes).reversed()
            .thenComparing(Video::getAddedToPlaylist);

    public Video getCurrentVideo() {
        return actualVideo;
    }


    public Playlist getPlaylist(){
        return playlistHolder.getPlaylist();
    }

    /**
     * Sortiert die Playlist neu
     */
    private void updatePlaylist(){
        playlist.sort(comparator);
        playlistHolder.updatePlaylist(playlist);
    }

    /**
     * Fügt ein Video in die Playlist hinzu
     */
    public synchronized void addVideo(Video video){
        if(get(video.getVideoId()) == null){
            video.setAddedToPlaylist(System.currentTimeMillis());
            video.resetVotes();
            playlist.add(video);
            LOG.info(video.toString());
        }
        else{
            addVote(video.getVideoId());
        }
        updatePlaylist();
    }

    /**
     * Löscht das Video aus der Playlist
     */
    public void removeVideo(String videoId){
        Video video = get(videoId);
        if(video != null){
            playlist.remove(video);
            updatePlaylist();
        }
    }

    /**
     * Gibt zurück ob dieses Video in der Playlist vorhanden ist.
     */
    public Video get(String videoId) {
        return playlist.stream().filter(x -> x.getVideoId().equals(videoId)).findFirst().orElse(null);
    }

    public boolean contains(String videoId){
        return get(videoId) != null;
    }

    /**
     * fügt einen Vote zu dem Video hinzu
     */
    public void addVote(String videoId){
        Video video = get(videoId);
        if(video != null){
            video.increaseVotes();
            updatePlaylist();
        }
    }

    /**
     * löscht einen Vote von dem Video
     */
    public void removeVote(String videoId){
        Video video = get(videoId);
        if(video != null){
            video.decreaseVotes();
            updatePlaylist();
        }
    }


    /**
     * Diese Methode gibt das Video zurück welches als nächstes Gespielt werden soll,
     * entweder das mit den meisten Votes oder ein zufälliges aus der Playlist
     * @return Video das abgespielt werden soll
     */
    public Video playVideo(){
        Video video = null;
        if(playlist.size() > 0){
            video = playlist.stream().findFirst().get();
            playlist.remove(video);
            updatePlaylist();
        }
        actualVideo = video;
        return actualVideo;
    }

    /**
     * @return gibt das aktuelle Video zurück
     */
    public Video peek(){ return actualVideo; }

    /**
     *
     * @return zufälliges Video aus der Playlist
     */
    public Video playRandom() {
        Video video = videoRepository.getRandom();
        actualVideo = video;
        return video;
    }
}
