package at.htl.musicvoting.model;

import java.util.List;

public class YoutubeResult {
    private String nextPageToken;
    private List<Video> videos;

    public YoutubeResult(String nextPageToken, List<Video> videos) {
        this.nextPageToken = nextPageToken;
        this.videos = videos;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
