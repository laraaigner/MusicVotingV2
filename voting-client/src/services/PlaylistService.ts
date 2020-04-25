import Axios from "axios"
let url = process.env.VUE_APP_API_URL + '/playlist/';
export default{

  async getVersion(){
    let response = await Axios.get(url + "version")
    return response.data;
  },
  async getAll() {
    let response = await Axios.get(url);
    return response.data;
  },
  async addVote(id: number) {
    let response = await Axios.post(url+"add/vote?videoId="+id);
    return response.data;
  },
  async removeVote(id: number) {
    let response = await Axios.post(url+"remove/vote?videoId="+id);
    return response.data;
  },
  async peek(){
    let response = await Axios.get(url+"peek");
    return response.data;
  }
}
