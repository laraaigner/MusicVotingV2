package at.htl.musicvoting.model;

import java.util.List;

public class Playlist {

    Long timestamp;
    Long updateId;

    List<Video> videos;

    public Playlist(Long timestamp, List<Video> videos) {
        this.timestamp = timestamp;
        this.videos = videos;
        this.updateId = 0l;
    }

    public void increaseUpdateId(){
        this.updateId++;
    }

    public Long getUpdateId() {
        return updateId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
