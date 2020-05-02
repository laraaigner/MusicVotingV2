package at.htl.musicvoting.model;


import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

@Entity
public class Video{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String videoId;
    private String title;
    private String channel;
    private String thumbnail;

    @Transient
    private int votes;

    @Transient
    private Long addedToPlaylist;

    public Video() {
    }

    public Video(String videoId, String title, String channel, String thumbnail) {
        this.videoId = videoId;
        this.title = title;
        this.channel = channel;
        this.thumbnail = thumbnail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getVotes() {
        return votes;
    }

    public Long getAddedToPlaylist() {
        return addedToPlaylist;
    }

    public void setAddedToPlaylist(Long addedToPlaylist) {
        this.addedToPlaylist = addedToPlaylist;
    }

    public void resetVotes() {
        votes = 0;
    }

    public void increaseVotes() {
        votes++;
    }

    public void decreaseVotes() {
        votes--;
    }

  @Override
  public String toString() {
    return "Video{" +
      ", videoId='" + videoId + '\'' +
      ", title='" + title + '\'' +
      ", channel='" + channel + '\'' +
      '}';
  }
}
