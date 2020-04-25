<template>
  <div>
    <v-layout row>
      <v-flex xs12>
        <v-card dark color="primary">
          <div class="example1" style="width: 66%;margin-right: auto;margin-left: auto;">
            <p class="px-0">{{text}}</p>
          </div>
        </v-card>
      </v-flex>
    </v-layout>
    <v-layout row fluidwrap>
      <v-flex xs2></v-flex>
      <v-flex xs8>
        <div style="height:40vh">
          <v-img v-bind:src="defaultThumbnail" aspect-ratio="1" class="grey lighten-2" max-height="100%"></v-img>
        </div>
      </v-flex>
      <v-flex xs2></v-flex>
    </v-layout>
  </div>
</template>

<script lang="js">
export default {
  name: "PlayingSong",
  data() {
    return {
      title: "",
      artist: "",
      text: "Playlist wurde noch nicht gestarted",
      defaultThumbnail: require('@/assets/images/defaultCover.png')
    };
  },
  created(){
    this.refresh();
  },
  methods: {
    refresh() {
      fetch(process.env.VUE_APP_API_URL + "/playlist/peek", {
        method: "GET",
        credentials: "include"
      }).then(async function(res) {
        let tmp = await res.json();
        this.title = tmp.title;
        this.artist = tmp.artist;
        this.text = this.title + " - " + this.artist;
      }.bind(this));
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.example1 {
  height: 50px;
  overflow: hidden;
  white-space: nowrap;
  position: relative;
}
.example1 p {
  position: absolute;
  width: 100%;
  height: 100%;
  margin: 0;
  line-height: 50px;
  text-align: center;
  /* Starting position */
  -moz-transform: translateX(100%);
  -webkit-transform: translateX(100%);
  transform: translateX(100%);
  /* Apply animation to this element */
  -moz-animation: example1 15s linear infinite;
  -webkit-animation: example1 15s linear infinite;
  animation: example1 15s linear infinite;
}
/* Move it (define the animation) */
@-moz-keyframes example1 {
  0% {
    -moz-transform: translateX(100%);
  }
  100% {
    -moz-transform: translateX(-100%);
  }
}
@-webkit-keyframes example1 {
  0% {
    -webkit-transform: translateX(100%);
  }
  100% {
    -webkit-transform: translateX(-100%);
  }
}
@keyframes example1 {
  0% {
    -moz-transform: translateX(100%); /* Firefox bug fix */
    -webkit-transform: translateX(100%); /* Firefox bug fix */
    transform: translateX(100%);
  }
  100% {
    -moz-transform: translateX(-100%); /* Firefox bug fix */
    -webkit-transform: translateX(-100%); /* Firefox bug fix */
    transform: translateX(-100%);
  }
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
