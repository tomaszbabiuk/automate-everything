import Vue from 'vue'
import VueRouter from 'vue-router'
import Control from '../views/Control'
import Timeline from '../views/Timeline'
import Inbox from '../views/Inbox'
import Plugins from '../views/Plugins'
import Objects from '../views/Objects'
import Tags from '../views/Tags'
import Icons from '../views/Icons'
import Discover from '../views/Discover'
import PluginSettings from '../views/PluginSettings'

Vue.use(VueRouter)

const routes = [
  {
    path: '/control',
    name: 'control',
    component: Control,
    meta: { titleRes: '$vuetify.navigation.control' }
  },
  {
    path: '/discover/:clazz',
    name: 'discover',
    component: Discover,
    meta: { titleRes: '$vuetify.navigation.discover' }
  },
  {
    path: '/timeline',
    name: 'timeline',
    component: Timeline,
    meta: { titleRes: '$vuetify.navigation.timeline' }
  },
  {
    path: '/inbox',
    name: 'inbox',
    component: Inbox,
    meta: { titleRes: '$vuetify.navigation.inbox' }
  },
  {
    path: '/plugins/:category',
    name: 'plugins',
    component: Plugins,
    meta: { titleRes: '$vuetify.navigation.plugins' }
  },
  {
    path: '/pluginSettings/:id',
    name: 'pluginSettings',
    component: PluginSettings,
    meta: { titleRes: '$vuetify.navigation.plugin_settings' }
  },
  {
    path: '/objects/:clazz',
    name: 'objects',
    component: Objects,
    meta: { titleRes: '$vuetify.navigation.objects' }
  },
  {
    path: '/tags',
    name: 'tags',
    component: Tags,
    meta: { titleRes: '$vuetify.navigation.tags' }
  },
  {
    path: '/icons',
    name: 'icons',
    component: Icons,
    meta: { titleRes: '$vuetify.navigation.icons' }
  },
  {
    path: '/',
    redirect: '/inbox',
    meta: { titleRes: '$vuetify.navigation.inbox' }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
