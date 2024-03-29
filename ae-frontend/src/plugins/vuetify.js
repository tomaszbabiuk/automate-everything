import Vue from 'vue';
import Vuetify from 'vuetify/lib';
import pl from '../locale/pl'
import en from '../locale/en'

import IconButton from '../components/icons/IconButton'
import IconCrosshair from '../components/icons/IconCrosshair'
import IconEmpty from '../components/icons/IconEmpty'
import IconObjects from '../components/icons/IconObjects'
import IconIcons from '../components/icons/IconIcons'
import IconTag from '../components/icons/IconTag'
import IconInbox from '../components/icons/IconInbox'
import IconPlugin from '../components/icons/IconPlugin'
import IconRobot from '../components/icons/IconRobot'
import IconTimeline from '../components/icons/IconTimeline'

import IconPause from '../components/icons/IconPause'
import IconPlay from '../components/icons/IconPlay'

import IconPL from '../components/icons/IconPL'
import IconUK from '../components/icons/IconUK'

import IconGroupingTogether from '../components/icons/IconGroupingTogether'
import IconGroupingSeparately from '../components/icons/IconGroupingSeparately'

Vue.use(Vuetify);

export default new Vuetify({
  lang: {
    locales: { en, pl },
    current: 'pl',
  },
  icons: {
    values: {
      flag_pl: {
        component: IconPL
      },
      flag_uk: {
        component: IconUK
      },
      button: {
        component: IconButton
      },
      crosshair: {
        component: IconCrosshair
      },
      empty: {
        component: IconEmpty
      },
      objects: {
        component: IconObjects
      },
      tag: {
        component: IconTag
      },
      icons: {
        component: IconIcons
      },
      inbox: {
        component: IconInbox
      },
      plugin: {
        component: IconPlugin,
      },
      robot: {
        component: IconRobot,
      },
      timeline: {
        component: IconTimeline
      },
      pause: {
        component: IconPause
      },
      play: {
        component: IconPlay
      },
      grouping_together: {
        component: IconGroupingTogether
      },
      grouping_separately: {
        component: IconGroupingSeparately
      },
    },
  },
});
