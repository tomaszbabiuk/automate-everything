import Vue from 'vue';
import Vuetify from 'vuetify/lib';
import pl from '../locale/pl'
import en from '../locale/en'
import IconBell from '../components/icons/IconBell'
import IconButton from '../components/icons/IconButton'
import IconCrosshair from '../components/icons/IconCrosshair'
import IconEqualizer from '../components/icons/IconEqualizer'
import IconHouse from '../components/icons/IconHouse'
import IconInbox from '../components/icons/IconInbox'
import IconPlugin from '../components/icons/IconPlugin'
import IconRobot from '../components/icons/IconRobot'
import IconTimeline from '../components/icons/IconTimeline'

Vue.use(Vuetify);

export default new Vuetify({
  lang: {
    locales: { en, pl },
    current: 'en',
  },
  icons: {
    values: {
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
