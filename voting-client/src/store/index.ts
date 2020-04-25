import Vuex, { ActionTree } from "vuex";
import Vue from "vue"
import Video from "@/model/Video"
import VuexPersist from "vuex-persist"
import PlaylistService from '@/services/PlaylistService';
import createMutationsSharer from "vuex-shared-mutations";

Vue.use(Vuex)

export interface RootState {
    updateId: number
    votes: any[]
    videos: any[]
    currentVideo: Video | null
}

const persistenceStrategy = new VuexPersist({
    reducer: (state: any) => ({votes: state.votes})
})

const store = new Vuex.Store<RootState>({
    state: {
        votes: [],
        videos: [],
        currentVideo: null,
        updateId: 0
    },
    mutations: {
        setPlaylist(state, { videos, updateId }) {
            state.videos = videos
            state.updateId = updateId
        },
        addLocalVote(state, {videoId, timestamp}){
            state.votes.push({videoId, timestamp})
        },
        removeLocalVote(state, videoId){
            state.votes.splice(state.votes.indexOf(state.votes.find(v => v.videoId == videoId)), 1)
        },
        cleanUpVotes(state) {
            state.votes = state.votes
                .filter(x => state.videos.find(s => s.videoId == x.videoId && s.addedToPlaylist == x.timestamp)
                )
        },
        changeIconPath(state, {videoId, iconIndex}){
            const video = state.videos.find(video => video.videoId == videoId)
            if(video){
                video.iconIndex = iconIndex;
            }
        },
        setCurrentVideo(state, {video}){
            state.currentVideo = video;
        },
        addVote(state, {updateId, videoId}){
            state.updateId = updateId;
            state.videos.find(s => s.videoId == videoId)!.votes++;
        },
        removeVote(state, {updateId, videoId}){
            state.updateId = updateId;
            state.videos.find(s => s.videoId == videoId)!.votes--;
        },
        addVideo(state, {updateId, video}){
            state.videos.push(video);
            state.updateId = updateId;
        },
        removeVideo(state, {updateId, videoId}){
            state.videos.splice(state.videos.indexOf(state.videos.find(s => s.videoId == videoId)!), 1)
            state.updateId = updateId;
        }
    },
    actions: {
        peek({ commit }) {
            PlaylistService.peek().then(video => {
               commit("setCurrentVideo", video)
            });
        },
        fetchPlaylist({ state, commit }) {
            PlaylistService.getAll().then(result => {
                result.videos.forEach(element => {
                    let tmp = state.votes.find(v => v.videoId == element.videoId && v.timestamp == element.addedToPlaylist) ? 1 : 0;
                    element.iconIndex = tmp;
                });
                commit("setPlaylist", result)
            })
        },
        refreshIfNecessary({ state, dispatch, commit }) {
            PlaylistService.getVersion().then(x => {
                if (x != state.updateId)
                    dispatch("fetchPlaylist")
                commit("cleanUpVotes")
            });
        },
        handleVote({state, dispatch, commit}, {updateId, videoId}){
            if(state.updateId != updateId - 1)
                dispatch("fetchPlaylist")
            else
                commit("addVote", {updateId, videoId})
        },
        handleVoteRemovement({state, dispatch, commit}, {updateId, videoId}){
            if(state.updateId != updateId - 1)
                dispatch("fetchPlaylist")
            else
                commit("removeVote", {updateId, videoId})
        },
        handleVideo({state, dispatch, commit}, {updateId, video}){
            if(state.updateId != updateId - 1)
                dispatch("fetchPlaylist")
            else{
                commit("addVideo", {updateId, video})
                commit("changeIconPath", {videoId: video.videoId, iconIndex: 0})
            }
        },
        handleVideoRemovement({state, dispatch, commit}, {updateId, videoId}){
            if(state.updateId != updateId - 1)
                dispatch("fetchPlaylist")
            else
                commit("removeVideo", {updateId, videoId})
        },
        removeUserVote({state, commit}, videoId){
            commit("removeLocalVote", videoId)
            commit("changeIconPath", {videoId: videoId, iconIndex: 0})
        },
        addUserVote({state, commit}, {videoId, timestamp}){
            commit("addLocalVote", {videoId, timestamp})
            commit("changeIconPath", {videoId: videoId, iconIndex: 1})
        }
    },
    plugins: [persistenceStrategy.plugin, createMutationsSharer({ predicate: ["addLocalVote", "removeLocalVote", "cleanUpVotes", "changeIconPath"] })]
})

export default store;
