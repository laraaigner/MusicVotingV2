<template>
  <v-container grid-list-md text-xs-center>
    <v-layout row wrap>
      <v-flex xs4>
        <v-img :src="thumbnail" :lazy-src="defaultThumbnail" aspect-ratio="1"/>
      </v-flex>
      <v-flex xs8 height="100%">
        <div style="width: 100%;height:100%;position: relative">
          <span
            style="width: 100%;font-weight: bold;float:left;overflow-wrap: break-word;text-align: left;padding-top: 5px;font-size: large"
          >{{this.title}}</span>
          <br>
          <span
            style="width: 100%;text-align: left;float:left;white-space: pre-line"
          >{{this.channel}}</span>
          <div
            style="position: absolute;
                    bottom: 0;
                    right: 0;"
          ></div>
        </div>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import Vue from "vue";
import Vuetify from "vuetify";
import PlaylistService from "@/services/PlaylistService.ts";

export default Vue.extend({
  name: "video-preview",
  data() {
    return {
      defaultThumbnail: require("@/assets/images/defaultCover.png"),
      thumbnail: "",
      title: "Es wird noch kein Titel gespielt",
      channel: "Wiedergabe muss erst gestartet werden"
    };
  },
  components: {
    Vue,
    Vuetify,
    PlaylistService
  },
  created() {
    this.$store.subscribe((mutation, store) => {
      if (mutation.type === "setCurrentVideo") {
        const video = mutation.payload;
        if (video) {
          this.title = video.title;
          this.channel = video.channel;
          this.thumbnail = video.thumbnail;
        }
      }
    });
    this.$store.dispatch("peek");
  }
});
</script>
