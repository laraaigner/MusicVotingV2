import Vue from 'vue'
import App from './App.vue'
import Vuetify from 'vuetify'
import router from './router'
//@ts-ignore
import VueYoutube from 'vue-youtube'

import 'vuetify/dist/vuetify.min.css';
// main.js
import 'material-design-icons-iconfont/dist/material-design-icons.css'

Vue.config.productionTip = false

Vue.use(VueYoutube)

Vue.use(Vuetify, {
  iconfont: 'md' // 'md' || 'mdi' || 'fa' || 'fa4'
});

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
