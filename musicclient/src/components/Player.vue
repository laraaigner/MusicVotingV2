<template>
  <div class="video">
      <youtube id="video" fit-parent="true" class="video" ref="youtube" :video-id="videoId" :player-vars="playerVars" @ended="playNextSong" @error="playNextSong"/>
      <v-layout align-center justify-center>
      <v-btn style="position: fixed; top: 90%" color="#cc0e41" @click="playNextSong()">
        <i style="color: white" class="material-icons">skip_next</i>
      </v-btn>
      </v-layout>
  </div>
</template>

<script>
export default {
  name: "Player",
  data() {
    return {
      videoId: 'lG0Ys-2d4MA',
      playerVars: {
        autoplay: 1,
        controls: 0
      }
    };
  },

  components: {},
  methods: {
    playNextSong() {
      fetch(process.env.VUE_APP_API_URL + "/playlist/pop", {
        method: "GET",
        credentials: "include",
        headers: {
          "Content-Type": "text/plain"
        }
      }).then(
        async function(response) {

          let tmp = await response.json();
          console.log('song ', tmp)
          this.videoId = tmp.videoId;
          this.$refs.youtube.player.playVideo()
          // let audio = document.getElementById("video");
          // audio.src = process.env.VUE_APP_API_URL + "/song/getmp3?id=" + tmp.id;
          // audio.play();
        }.bind(this)
      );
    },
    playVideo () {
      this.$refs.youtube.player.playVideo()
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.video{
    width: 100% !important;
    height: 100% !important;
    z-index: 0;
}

iframe{
    width: 100%;
    height: 100%;
}

h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
