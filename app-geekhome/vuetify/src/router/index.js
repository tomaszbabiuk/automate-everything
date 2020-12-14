import Vue from 'vue'
import VueRouter from 'vue-router'
import Control from '../views/Control'
import Timeline from '../views/Timeline'
import Inbox from '../views/Inbox'
import Plugins from '../views/Plugins'
import Configurables from '../views/Configurables'
import Tags from '../views/Tags'
import Instances from '../views/Instances'
import Discover from '../views/Discover'

Vue.use(VueRouter)

const routes = [
  {
    path: '/control',
    name: 'control',
    component: Control
  },
  {
    path: '/discover',
    name: 'discover',
    component: Discover
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
    path: '/configurables/:clazz',
    name: 'configurables',
    component: Configurables
  },
  {
    path: '/tags',
    name: 'tags',
    component: Tags
  },
  {
    path: '/instances/:clazz',
    name: 'instances',
    component: Instances
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
