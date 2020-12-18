import Vue from 'vue';
import Vuetify from 'vuetify/lib';
import pl from '../locale/pl'
import en from '../locale/en'

import IconBell from '../components/icons/IconBell'
import IconButton from '../components/icons/IconButton'
import IconCrosshair from '../components/icons/IconCrosshair'
import IconEqualizer from '../components/icons/IconEqualizer'
import IconHouse from '../components/icons/IconHouse'
import IconIcons from '../components/icons/IconIcons'
import IconTag from '../components/icons/IconTag'
import IconInbox from '../components/icons/IconInbox'
import IconPlugin from '../components/icons/IconPlugin'
import IconRobot from '../components/icons/IconRobot'
import IconTimeline from '../components/icons/IconTimeline'

import IconPL from '../components/icons/IconPL'
import IconUK from '../components/icons/IconUK'

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
      bell: {
        component: IconBell
      },
      button: {
        component: IconButton
      },
      crosshair: {
        component: IconCrosshair
      },
      equalizer: {
        component: IconEqualizer
      },
      house: {
        component: IconHouse
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
      }
    },
  },
});
