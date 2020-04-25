import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'
import Login from "./views/Login.vue"
import AuthenticationService from "./services/AuthenticationService"

Vue.use(Router)

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'home',
      meta: {
        requiresAuth: true
      },
      component: Home
    },
    {
      path: "/login",
      name: "login",
      component: Login
    }
  ]
})

router.beforeEach((to, from, next) => {
  // only continue to the route if the authentication succeeds

  AuthenticationService.authenticate().then(res => {
    if (res.status != 200 && to.meta.requiresAuth)
      next({ path: '/login', query: { origin: from.path } })
    else
      next()
  })



})

export default router
