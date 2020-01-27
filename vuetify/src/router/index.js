import Vue from 'vue'
import VueRouter from 'vue-router'
import Control from '../views/Control'
import Timeline from '../views/Timeline'
import Inbox from '../views/Inbox'
import Plugins from '../views/Plugins'
import House from '../views/House'

Vue.use(VueRouter)

const routes = [
  {
    path: '/control',
    name: 'control',
    component: Control
  },
  {
    path: '/timeline',
    name: 'timeline',
    component: Timeline
  },
  {
    path: '/inbox',
    name: 'inbox',
    component: Inbox
  },
  {
    path: '/plugins',
    name: 'plugins',
    component: Plugins
  },
  {
    path: '/house',
    name: 'house',
    component: House
  },
  {
    path: '/',
    redirect: '/inbox'
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
