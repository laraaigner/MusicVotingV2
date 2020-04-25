import Vue from 'vue'
import App from './App.vue'
//import vuetify from './plugins/vuetify'
import router from './router'
import store from './store/index'
import './registerServiceWorker'
import 'material-design-icons-iconfont/dist/material-design-icons.css'

import vuetify from './plugins/vuetify';

Vue.config.productionTip = false
Vue.config.devtools = true

new Vue({
  vuetify,
  router,
  store,
  render: h => h(App)
}).$mount('#app')