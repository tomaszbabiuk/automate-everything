import Vue from 'vue'
import VueRouter from 'vue-router'
// import Home from '../views/Home'
import Control from '../views/Control'
import Timeline from '../views/Timeline'
import Inbox from '../views/Inbox'
import Plugins from '../views/Plugins'

Vue.use(VueRouter)

const routes = [
  {
    path: '/:locale',
    template: '<router-view />',
    children: [
      // {
      //   path: '/',
      //   name: 'home',
      //   component: Home
      // },
      {
        path: 'control',
        name: 'control',
        component: Control
      },
      {
        path: 'timeline',
        name: 'timeline',
        component: Timeline
      },
      {
        path: 'inbox',
        name: 'inbox',
        component: Inbox
      },
      {
        path: 'plugins',
        name: 'plugins',
        component: Plugins
      }
    ]
  },
  {
    path: '/',
    redirect: '/en'
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
