package at.htl.musicvoting.business;

import at.htl.musicvoting.model.Playlist;
import at.htl.musicvoting.model.Video;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.LinkedList;
import java.util.List;

/**
 * Diese Klasse kümmert sich um die Playlist
 */
@ApplicationScoped
public class PlaylistHolder {

    private Playlist playlist;

    @PostConstruct
    public void init(){
        playlist = new Playlist(getTimestamp(), new LinkedList<>());
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void updatePlaylist(List<Video> videos){
        playlist.increaseUpdateId();
        playlist.setTimestamp(getTimestamp());
        playlist.setVideos(videos);
    }

    private long getTimestamp(){
        return System.currentTimeMillis();
    }
}
