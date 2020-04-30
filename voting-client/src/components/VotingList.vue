<template>
  <v-container grid-list-md text-xs-center>
    <transition-group name="list-complete" tag="p">
      <v-layout
        style="border-top: 1px solid #CCC"
        row
        wrap
        align-center
        v-for="item in $store.state.videos"
        v-bind:key="item.videoId"
        class="list-complete-item"
      >
        <v-flex xs2>
          <div style="background-color: #141517 !important; height: 50px; width: 50px">
            <div
              style="line-height: 50px;white-space: nowrap;color: #f4f4f4;font-weight: bold;font-size: 20px"
            >{{item.votes}}</div>
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
      </v-layout>
    </transition-group>
    <v-snackbar
      v-model="snackbar"
      :style="'background-color:red'"
      :timeout="timeout"
      bottom
    >Playlist wird neu geladen</v-snackbar>
  </v-container>
</template>

<script>
import Vue from "vue";
import Vuetify from "vuetify";
import PlaylistService from "@/services/PlaylistService.ts";

export default {
  name: "voting-list",
  data() {
    return {
      snackbar: false,
      timeout: 1000,
      imagePaths: ["favorite_border", "favorite"],
      defaultThumbnail: require("@/assets/images/defaultCover.png")
    };
  },
  components: {
    Vue,
    Vuetify
  },
  created() {
    this.$store.subscribe((mutation, state) => {
      if (mutation.type === "setPlaylist") {
        this.snackbar = true;
        this.$store.commit("cleanUpVotes");
        /*mutation.payload.songs.forEach(element => {
          this.prepareSong(element);
        });
        this.$store.state.songs.splice(0,0);*/
      }
      if (
        mutation.type == "setPlaylist" ||
        mutation.type == "removeVote" ||
        mutation.type == "addVote" ||
        mutation.type == "addVideo" ||
        mutation.type == "removeVideo"
      )
        this.sort();
    });
  },
  methods: {
    handleClick(item) {
      console.log("click received");
      console.log("item: ", item);
      if (
        this.$store.state.votes.find(
          v => v.videoId == item.videoId && v.timestamp == item.addedToPlaylist
        )
      ) {
        this.removeVote(item.videoId, item.addedToPlaylist);
        PlaylistService.removeVote(item.videoId);
      } else {
        this.addVote(item.videoId, item.addedToPlaylist);
        PlaylistService.addVote(item.videoId);
      }
    },
    prepareVideo(item) {
      if (
        this.$store.state.votes.find(
          v => v.videoId == item.videoId && v.timestamp == item.addedToPlaylist
        )
      ) {
        this.$store.commit("changeIconPath", {
          videoId: item.videoId,
          iconIndex: 1
        });
      }
    },
    addVote(videoId, timestamp) {
      this.$store.dispatch("addUserVote", { videoId, timestamp });
    },
    removeVote(videoId, timestamp) {
      this.$store.dispatch("removeUserVote", videoId);
    },
    sort() {
      this.$store.state.videos.sort((a, b) => {
        if (a.votes > b.votes) return -1;
        else if (a.votes < b.votes) return 1;
        else {
          if (a.addedToPlaylist >= b.addedToPlaylist) return 1;
          else return -1;
        }
      });
    }
  }
};
</script>

<style scoped>
.list-complete-item {
  transition: all 1s;
  margin-right: 10px;
}
.list-complete-enter,
.list-complete-leave-to {
  opacity: 0;
  transform: translateX(200px);
}
.list-complete-leave-active {
  position: absolute;
}
i[icon$="favorite"] {
  color: #f20643 !important;
}
</style>
