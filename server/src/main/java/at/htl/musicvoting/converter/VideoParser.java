package at.htl.musicvoting.converter;

import at.htl.musicvoting.model.Video;

import at.htl.musicvoting.model.YoutubeResult;

import javax.json.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;

public class VideoParser {

  /**
   * Konvertiert das JsonObject der YouTube suche zu einer Liste von Videos
   */
  public static YoutubeResult youTubeSearchToVideoParser(JsonObject object){
    JsonArray items = object.getJsonArray("items");
    List<Video> videos = new ArrayList<>();
    List<String> blackList = Arrays.asList("Fuck", "Scheisse", "Bitch", "Shit", "Arschloch", "Katja", "Krasavice");


    for (JsonValue item: items) {
      JsonObject itemObject = item.asJsonObject();
      JsonObject snippet = itemObject.getJsonObject("snippet");

      try {
        String videoId = itemObject.getJsonObject("id").getString("videoId");
        String title = snippet.getString("title");
        String channel = snippet.getString("channelTitle");

        System.out.println("Title: " + title);
        System.out.println("Channel: " + channel);

        System.out.println("Filter: " + blackList.parallelStream().map(String::toLowerCase).noneMatch(title.toLowerCase()::contains));

        //Abfrage für Filter
        if (blackList.parallelStream().map(String::toLowerCase).noneMatch(title.toLowerCase()::contains)) {
          String thumbnail = snippet.getJsonObject("thumbnails").getJsonObject("high").getString("url");
          Video video = new Video(videoId, unescapeXml(title), unescapeXml(channel), thumbnail);
          videos.add(video);
        }
      }
      catch (NullPointerException ex){
//        System.out.println("Object skipped: " + itemObject);
      }
    }
    return new YoutubeResult(object.getString("nextPageToken"), videos);
  }

  /**
   * Konvertiert ein Json Objekt zu einem Video Objekt
   */
  public static Video postedObjectToVideo(JsonObject object){
    String channel = object.getString("channel");
    String thumbnail = object.getString("thumbnail");
    String title = object.getString("title");
    String videoId = object.getString("videoId");
    return new Video(videoId,title,channel,thumbnail);
  }
}
