import Axios from "axios"
import Video from "@/model/Video";


let videoUrl = process.env.VUE_APP_API_URL + '/video';
let playlistUrl = process.env.VUE_APP_API_URL + '/playlist/'
export default {

  async find(term: String) {
    let response = await Axios.get(videoUrl + '?query=' + term);
    return response.data;
  },

  async addVideo(video: Video) {
    let response = await Axios.post(playlistUrl + 'add/video', { channel: video.channel, title: video.title, thumbnail: video.thumbnail, videoId: video.videoId })
    return response;
  }
}
