<template>
  <div class="playlist">
    <v-layout row>
      <v-flex xs12>
        <v-card dark color="primary">
          <div style="height: 50px; background-color: #cc0e41">
            <p
              style="position: absolute;width: 100%;height: 100%;margin: 0;line-height: 50px; font-size: 21px"
            >Playlist</p>
          </div>
        </v-card>
      </v-flex>
    </v-layout>
    <v-layout row>
      <v-flex xs12>
        <div style="height: 70vh;overflow-x: hidden;opacity: 0.8; background-color: gray; color: white;
         font-size: 19px" >
          <transition-group name="list-complete" tag="p" style="position: absolute; top: 60px" >
            <v-layout row v-for="item in playlist" v-bind:key="item.videoId" class="list-complete-item">
              <v-flex xs1>{{item.votes}}</v-flex>
              <v-flex xs5>{{item.title}}</v-flex>
              <v-flex xs5>{{item.channel}}</v-flex>
              <v-flex xs1>
                <i class="material-icons" @click="remove(item.videoId)" style="cursor: pointer">clear</i>
              </v-flex>
            </v-layout>
          </transition-group>
        </div>
      </v-flex>
    </v-layout>
    <v-layout row fluidwrap></v-layout>
  </div>
</template>

<script lang="js">

export default {
  name: "Playlist",
  data() {
    return {
        playlist: []
     };
  },
  created(){
      fetch(process.env.VUE_APP_API_URL + "/playlist/", {
        method: "GET",
        credentials: "include"
      }).then(async function(res) {
        let tmp = await res.json();
        this.playlist = tmp.videos;
        this.sort();
      }.bind(this));
  },
  methods: {
    remove(id){
      fetch(process.env.VUE_APP_API_URL + "/playlist/remove/video?videoId=" + id, {
        method: "POST",
        credentials: "include"
      });
    },
    addVideo(video) {
        this.playlist.push(video);
        this.sort();
    },
    removeVideo(id){
        const index = this.getIndexOfSong(id);
        if(index != -1)
            this.playlist.splice(index, 1);
        this.sort();
    },
    addVote(id){
        this.playlist[this.getIndexOfSong(id)].votes++;
        this.sort();
    },
    removeVote(id){
        this.playlist[this.getIndexOfSong(id)].votes--;
        this.sort();
    },
    getIndexOfSong(id){
        for(var i = 0;i < this.playlist.length;i++){
            if(this.playlist[i].videoId == id)
                return i;
        }
        return -1;
    },
    sort(){
      this.playlist.sort((a,b) => {
        if(a.votes > b.votes) return -1;
        else if(a.votes < b.votes) return 1;
        else{
          if(a.addedToPlaylist >= b.addedToPlaylist) return 1;
          else return -1;
        }
      });
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .playlist{
    z-index: 1;
  }

.list-complete-item {
  transition: all 1s;
  margin-right: 10px;
}
.list-complete-enter, .list-complete-leave-to
/* .list-complete-leave-active below version 2.1.8 */ {
  opacity: 0;
  transform: translateX(300px);
}
.list-complete-leave-active {
  position: absolute;
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
