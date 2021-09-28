import Vue from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify'
import store from './plugins/vuex'

import ConfigurableForm from './components/ae/configurable/Form'
import ConfigurableStringField from './components/ae/configurable/StringField'
import ConfigurableBooleanField from './components/ae/configurable/BooleanField'
import ConfigurablePowerLevelField from './components/ae/configurable/PowerLevelField'
import ConfigurableDurationField from './components/ae/configurable/DurationField'
import ConfigurableDoubleField from './components/ae/configurable/DoubleField'
import ConfigurableReferenceField from './components/ae/configurable/ReferenceField'
import ConfigurableIconSelector from './components/ae/configurable/IconSelector'
import ConfigurableTagsSelector from './components/ae/configurable/TagsSelector'
import ConfigurableBlockConfigurator from './components/ae/configurable/BlockConfigurator'

import SettingsForm from './components/ae/settings/Form'
import SettingsStringField from './components/ae/settings/StringField'

import PortControl from './components/ae/PortControl'

//configurables
Vue.component('configurable-form', ConfigurableForm)
Vue.component('configurable-stringfield', ConfigurableStringField)
Vue.component('configurable-booleanfield', ConfigurableBooleanField)
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
