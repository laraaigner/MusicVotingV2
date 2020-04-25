<template>
  <v-app dark>
    <v-tabs fixed-tabs class="app" slider-color="#F20643">
      <v-tab active-class="active" style=" border-bottom: 2px solid lightgrey;">Playlist</v-tab>
      <v-tab active-class="active" style=" border-bottom: 2px solid lightgrey;">Hinzufügen</v-tab>
      <v-tab-item color="primary">
        <v-card flat>
          <song-preview/>
          <voting-list fill-height="100%"/>
        </v-card>
      </v-tab-item>
      <v-tab-item>
        <v-card flat>
          <add-view/>
        </v-card>
      </v-tab-item>
    </v-tabs>
  </v-app>
</template>

<script lang="js">
import Vue from "vue";
import Vuetify from 'vuetify'
import VotingList from '@/components/VotingList.vue'
import AddView from '@/components/AddView.vue'
import SongPreview from '@/components/VideoPreview.vue'
import PlaylistService from '@/services/PlaylistService.ts'
import ReconnectingEventSource from "reconnecting-eventsource";

export default Vue.extend({
  name: 'App',
  components: {
    Vue,
    Vuetify,
    VotingList,
    AddView,
    SongPreview
  },
  data() {
    return {
      eventSource: null
    };
  },
  methods: {
    refresh(){
      this.$store.dispatch("refreshIfNecessary")
      this.$store.dispatch("peek")
    }
  },
  created(){
    window.addEventListener('focus', () => this.refresh());
  },
  destroyed() {
    this.eventSource.close();
    window.removeEventListener('visibilitychange',  this.refresh);
  },
  mounted() {
    this.eventSource = new ReconnectingEventSource(
      process.env.VUE_APP_API_URL + "/playlist/connect",{withCredentials: false,max_retry_time: 3000,}
    );
    this.eventSource.addEventListener("video_started", e => {
      this.$store.dispatch("peek")
    });
    this.eventSource.addEventListener("add_vote", e => {
      console.log(JSON.parse(e.data));
      this.$store.dispatch("handleVote", JSON.parse(e.data))
    });
    this.eventSource.addEventListener("remove_vote", e => {
      console.log(JSON.parse(e.data));
      this.$store.dispatch("handleVoteRemovement", JSON.parse(e.data))
    });
    this.eventSource.addEventListener("add_video", e => {
      console.log(JSON.parse(e.data));
      this.$store.dispatch("handleVideo", JSON.parse(e.data))
    });
    this.eventSource.addEventListener("remove_video", e => {
      console.log(JSON.parse(e.data));
      this.$store.dispatch("handleVideoRemovement", JSON.parse(e.data))
    });
    this.$store.dispatch("fetchPlaylist")
  }
});
</script>

<style>
.app {
  font-family: sans-serif;
}
.v-tabs__wrapper {
  position: sticky;
  z-index: 5;
}
</style>
