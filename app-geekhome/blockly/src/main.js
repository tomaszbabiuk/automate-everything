import Vue from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify'
import store from './plugins/vuex'

import ConfigurableForm from './components/geekhome/configurable/Form'
import ConfigurableStringField from './components/geekhome/configurable/StringField'
import ConfigurablePowerLevelField from './components/geekhome/configurable/PowerLevelField'
import ConfigurableDurationField from './components/geekhome/configurable/DurationField'
import ConfigurableDoubleField from './components/geekhome/configurable/DoubleField'
import ConfigurableReferenceField from './components/geekhome/configurable/ReferenceField'
import ConfigurableIconSelector from './components/geekhome/configurable/IconSelector'
import ConfigurableTagsSelector from './components/geekhome/configurable/TagsSelector'
import ConfigurableBlockConfigurator from './components/geekhome/configurable/BlockConfigurator'

import SettingsForm from './components/geekhome/settings/Form'
import SettingsStringField from './components/geekhome/settings/StringField'

import PortControl from './components/geekhome/PortControl'

//configurables
Vue.component('configurable-form', ConfigurableForm)
Vue.component('configurable-stringfield', ConfigurableStringField)
Vue.component('configurable-powerlevelfield', ConfigurablePowerLevelField)
Vue.component('configurable-durationfield', ConfigurableDurationField)
Vue.component('configurable-doublefield', ConfigurableDoubleField)
Vue.component('configurable-referencefield', ConfigurableReferenceField)
Vue.component('configurable-iconselector', ConfigurableIconSelector)
Vue.component('configurable-tagsselector', ConfigurableTagsSelector)
Vue.component('configurable-blockconfigurator', ConfigurableBlockConfigurator)

//settings
Vue.component('settings-form', SettingsForm)
Vue.component('settings-stringfield', SettingsStringField)

//geekhome
Vue.component('portcontrol', PortControl)

Vue.config.productionTip = false
//Add unimported components to ignore list to prevent warnings.
Vue.config.ignoredElements = ['field','block','category','xml','mutation','value','sep']

new Vue({
  store,
  router,
  vuetify,
  render: h => h(App)
}).$mount('#app')
