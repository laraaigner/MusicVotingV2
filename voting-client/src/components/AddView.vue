<template>
  <v-container grid-list-md text-xs-center>
    <v-layout rpw wrap align-center>
      <v-flex xs12>
        <v-text-field color="#F20643" v-model="search" label="Suche" @input="searchInputChanged()"></v-text-field>
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
              :src="defaultThumbnail"
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
  </v-container>
</template>

<script>
import Vue from "vue";
import Vuetify from "vuetify";
import VideoService from "@/services/VideoService.ts";

import PlaylistService from "@/services/PlaylistService.ts";
import InfiniteLoading from "vue-infinite-loading";

export default Vue.extend({
  components: {},
  name: "add-view",
  data() {
    return {
      videos: [],
      imagePaths: ["favorite_border", "favorite", "add"],
      defaultThumbnail: require("@/assets/images/defaultCover.png"),
      search: "",
      videosInPlaylist: []
    };
  },
  components: {
    Vue,
    Vuetify
  },
  mounted() {
    this.searchInputChanged();
  },
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
        VideoService.addVideo(item).then(result => {
          // let unsubscribe = this.$store.subscribeAction({
          //   after: (action, state) => {
          //     if (
          //       action.type == "handleVideo" &&
          //       action.payload.video.videoId
          //     ) {
          //       let tmp = this.$store.state.videos.find(
          //         s => s.videoId == item.videoId
          //       );
          //       if (tmp) {
          //         this.addVote(tmp.videoId, tmp.addedToPlaylist);
          //         PlaylistService.addVote(tmp.videoId);
          //       }
          //       unsubscribe();
          //     }
          //   }
          //});
        });
      }
    },
    searchInputChanged() {
      this.fetchLocalVideos();
    },
    fetchLocalVideos() {
      VideoService.find(this.search).then(result => {
        this.videos = result;
        this.videos.forEach(video => {
          video = this.prepareVideo(video);
        });
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
