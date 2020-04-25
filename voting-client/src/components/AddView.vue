<template>
  <v-container grid-list-md text-xs-center>
    <v-layout rpw wrap align-center>
      <v-flex xs12>
        <v-text-field v-model="search" label="Suche" @input="searchInputChanged()"></v-text-field>
      </v-flex>
    </v-layout>

    <v-layout
      style="border-bottom: 1px solid #CCC"
      row
      wrap
      align-center
      v-for="item in videos"
      v-bind:key="item.video"
    >
      <template>
        <v-flex xs2>
          <div style="backgroundColor: #141517; height: 50px; width: 50px">
            <v-img
              :src="item.thumbnail"
              :lazy-src="defaultThumbnail"
              aspect-ratio="1"
              class="grey lighten-2"
            >
              <template v-slot:placeholder>
                <v-layout fill-height align-center justify-center ma-0>
                  <v-progress-circular indeterminate color="grey lighten-5"></v-progress-circular>
                </v-layout>
              </template>
            </v-img>
          </div>
        </v-flex>
        <v-flex xs9 height="100%">
          <div style="width: 100%">
            <span
              style="text-align: left;font-weight: bold; white-space: nowrap; display: block; overflow: hidden;text-overflow: ellipsis"
            >{{item.title}}</span>
            <span style="text-align: left;width: 100%;display:block">{{item.channel}}</span>
          </div>
        </v-flex>
        <v-flex xs1>
          <i
            class="material-icons"
            :icon="imagePaths[item.iconIndex]"
            @click="handleClick(item)"
            style="cursor: pointer;font-size: 27px !important;"
          >{{imagePaths[item.iconIndex]}}</i>
        </v-flex>
      </template>
    </v-layout>
    <infinite-loading @infinite="fetchLocalVideos" spinner="waveDots" ref="InfiniteLoading">
      <span slot="no-more"></span>
      <span slot="no-results">Keine Lieder gefunden.</span>
    </infinite-loading>
  </v-container>
</template>

<script>
import Vue from "vue";
import Vuetify from "vuetify";
import VideoService from "@/services/VideoService.ts";

import PlaylistService from "@/services/PlaylistService.ts";
import InfiniteLoading from "vue-infinite-loading";
import { debounce } from "throttle-debounce";

Vue.use(InfiniteLoading);

function search(that) {
  that.nextPageToken = "";
  that.videos = [];
  that.fetchLocalVideos(null, true);
}

const throttledSearch = debounce(500, search);

export default Vue.extend({
  components: {
    InfiniteLoading
  },
  name: "add-view",
  data() {
    return {
      videos: [],
      imagePaths: ["favorite_border", "favorite", "add"],
      defaultThumbnail: require("@/assets/images/defaultCover.png"),
      search: "",
      nextPageToken: "",
      videosInPlaylist: []
    };
  },
  components: {
    Vue,
    Vuetify
  },
  mounted() {},
  created() {
    this.$store.subscribe((mutation, state) => {
      if (mutation.type === "setPlaylist") {
        this.videos.forEach(video => {
          video = this.prepareVideo(video);
        });
        this.$store.commit("cleanUpVotes");
        this.videos.splice(0, 0);
      } else if (mutation.type === "changeIconPath") {
        let tmp = this.videos.find(s => s.videoId === mutation.payload.videoId);
        if (tmp) {
          this.prepareVideo(tmp);
          this.videos.splice(0, 0);
        }
      } else if (mutation.type === "removeVideo") {
        let tmp = this.videos.find(s => s.videoId === mutation.payload.videoId);
        if (tmp) {
          this.prepareVideo(tmp);
          this.videos.splice(0, 0);
        }
      }
    });
  },
  methods: {
    handleClick(item) {
      if (
        this.$store.state.votes.find(
          v =>
            v.videoId == item.videoId &&
            this.$store.state.videos.find(s => s.videoId == item.videoId)
              .addedToPlaylist
        )
      ) {
        this.removeVote(item.videoId);
        PlaylistService.removeVote(item.videoId);
      } else if (
        this.$store.state.videos &&
        this.$store.state.videos.map(s => s.videoId).indexOf(item.videoId) >= 0
      ) {
        this.addVote(
          item.videoId,
          this.$store.state.videos.find(s => s.videoId == item.videoId)
            .addedToPlaylist
        );
        PlaylistService.addVote(item.videoId);
      } else {
        console.log(item);
        VideoService.addVideo(item).then(result => {});
      }
    },
    searchInputChanged() {
      throttledSearch(this);
    },
    fetchLocalVideos($state, reset = false) {
      VideoService.find(this.search, this.nextPageToken).then(result => {
        if (reset) this.videos = [];
        this.nextPageToken = result.nextPageToken;
        this.videos.push(...result.videos);
        this.videos.forEach(video => {
          video = this.prepareVideo(video);
        });
        if ($state) {
          if (result.videos.length > 0) $state.loaded();
          else $state.complete();
        }
      });
    },
    addVote(videoId, timestamp) {
      this.$store.dispatch("addUserVote", { videoId, timestamp });
    },
    removeVote(videoId, timestamp) {
      this.$store.dispatch("removeUserVote", videoId);
    },
    prepareVideo(item) {
      if (
        this.$store.state.videos.map(s => s.videoId).indexOf(item.videoId) >= 0
      ) {
        item.iconIndex = this.$store.state.videos.find(
          s => s.videoId == item.videoId
        ).iconIndex;
      } else {
        item.iconIndex = 2;
      }
    },

    playlistChanged(tmp) {
      this.videos.forEach(video => {
        video = this.prepareVideo(video);
      });
      this.video.splice(0, 0);
    }
  }
});
</script>
<style>
i[icon$="favorite"] {
  color: #f20643 !important;
}
i[icon$="add"] {
  color: #f20643 !important;
}
</style>
