import Vue from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify'
import store from './plugins/vuex'

import ADCard from './components/admindashboard/Card'
import ADStarsCard from'./components/admindashboard/StatsCard'
import ADOffset from'./components/admindashboard/Offset'

import ConfigurableForm from './components/geekhome/configurable/Form'
import ConfigurableStringField from './components/geekhome/configurable/StringField'
import ConfigurableDoubleField from './components/geekhome/configurable/DoubleField'
import ConfigurableRelayReadWritePortField from './components/geekhome/configurable/RelayReadWritePortField'
import ConfigurableIconSelector from './components/geekhome/configurable/IconSelector'
import ConfigurableTagsSelector from './components/geekhome/configurable/TagsSelector'
import ConfigurableBlockConfigurator from './components/geekhome/configurable/BlockConfigurator'

import PortControl from './components/geekhome/PortControl'

import BlocklyComponent from './components/BlocklyComponent.vue'


//material dashboard
Vue.component('material-card', ADCard)
Vue.component('material-stats-card', ADStarsCard)
Vue.component('helper-offset', ADOffset)

//configurables
Vue.component('configurable-form', ConfigurableForm)
Vue.component('configurable-stringfield', ConfigurableStringField)
Vue.component('configurable-doublefield', ConfigurableDoubleField)
Vue.component('configurable-relayreadwriteportfield', ConfigurableRelayReadWritePortField)
Vue.component('configurable-iconselector', ConfigurableIconSelector)
Vue.component('configurable-tagsselector', ConfigurableTagsSelector)
Vue.component('configurable-blockconfigurator', ConfigurableBlockConfigurator)

//geekhome
Vue.component('portcontrol', PortControl)

//google
Vue.component('BlocklyComponent', BlocklyComponent)

Vue.config.productionTip = false
//Add unimported components to ignore list to prevent warnings.
Vue.config.ignoredElements = ['field','block','category','xml','mutation','value','sep']

new Vue({
  store,
  router,
  vuetify,
  render: h => h(App)
}).$mount('#app')
