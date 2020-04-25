<template>
  <v-app id="inspire">
    <v-toolbar style="height: 8vh; z-index: 1" dark color="secondary" fixed app>
      <v-toolbar-title>MusicVotingV2</v-toolbar-title>
    </v-toolbar>
    <v-content>
      <div id="app">
        <v-app id="inspire">
          <!--<v-container grid-list-md text-xs-center>
            <v-layout row>
              <v-flex xs7>
                &lt;!&ndash;<v-layout row>
                  <v-flex xs12>
                    <playing-song ref="playingSong"/>
                  </v-flex>
                </v-layout>&ndash;&gt;
                <v-layout row>
                  <v-flex xs12></v-flex>
                </v-layout>
                <v-layout row>
                  <v-flex xs12>
                    <Player/>
                  </v-flex>
                </v-layout>
              </v-flex>
              <v-flex xs5>
                <playlist ref="playlist"/>
              </v-flex>
            </v-layout>
          </v-container>-->
          <Player style="height: 90vh; width: 100vw"/>
          <playlist ref="playlist" style="width: 40vw; height: 90vh; margin-top: 8vh;"/>
        </v-app>
      </div>
    </v-content>
    <v-footer dark color="transparent" app>
      <span class="black--text">&copy;MusicVoting by HTL Leonding 2019</span>
    </v-footer>
  </v-app>
</template>

<script lang="js">
import Vue from "vue";
import Player from "@/components/Player.vue";
import PlayingSong from "@/components/PlayingSong.vue";
import Playlist from "@/components/Playlist.vue";

export default Vue.extend({
  name: "Home",
  components: {
    Player,
    PlayingSong,
    Playlist
  },
  created() {
    console.log('hallo')
    const eventSource = new EventSource(
      process.env.VUE_APP_API_URL + "/playlist/connect"
    );
    eventSource.addEventListener("add_video", e => {
      let tmp = JSON.parse(e.data).video;
      this.$refs.playlist.addVideo(tmp);
    })
    eventSource.addEventListener("add_vote", e => {
       let tmp = JSON.parse(e.data).videoId;
      this.$refs.playlist.addVote(tmp);
    })
    eventSource.addEventListener("remove_video", e => {
      this.$refs.playlist.removeVideo(JSON.parse(e.data).videoId);
    })
    eventSource.addEventListener("remove_vote", e => {
      this.$refs.playlist.removeVote(JSON.parse(e.data).videoId);
    })
    eventSource.addEventListener("video_started", e => {
      //this.$refs.playingSong.refresh();
    })
  }
});
</script>
