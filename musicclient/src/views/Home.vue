<template>
  <v-app id="inspire">
    <v-toolbar style="height: 7vh; z-index: 1; box-shadow: 0px 15px 50px black;" dark color="secondary" fixed app>
      <v-toolbar-title style="z-index: 11; font-size: 28px; position: absolute; top: 15px">MusicVoting</v-toolbar-title>
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
          <playlist ref="playlist" style="position: absolute;width: 28vw; height: 90vh; margin-top: 8vh;
          z-index: 0; top: 0%; right: 10px"/>
        </v-app>
      </div>
    </v-content>
    <v-footer style="height: 3px" dark color="secondary" app>
      <span style="position: absolute; left: 5px" class="white--text"> &copy; MusicVoting by HTL Leonding 2019</span>
    </v-footer>
  </v-app>
</template>

<script lang="js">
import Vue from "vue";
import Player from "@/components/Player.vue";
import PlayingSong from "@/components/PlayingSong.vue";
import Playlist from "@/components/Playlist.vue";
import ToggleButton from 'vue-js-toggle-button'
Vue.use(ToggleButton)

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
