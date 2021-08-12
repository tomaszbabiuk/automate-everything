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
import ConfigurableGenericPortField from './components/geekhome/configurable/GenericPortField'
import ConfigurableRelayOutputPortField from './components/geekhome/configurable/RelayOutputPortField'
import ConfigurablePowerLevelOutputPortField from './components/geekhome/configurable/PowerLevelOutputPortField'
import ConfigurableTemperatureInputPortField from './components/geekhome/configurable/TemperatureInputPortField'
import ConfigurableHumidityInputPortField from './components/geekhome/configurable/HumidityInputPortField'
import ConfigurableWattageInputPortField from './components/geekhome/configurable/WattageInputPortField'
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
Vue.component('configurable-genericportfield', ConfigurableGenericPortField)
Vue.component('configurable-relayoutputportfield', ConfigurableRelayOutputPortField)
Vue.component('configurable-powerleveloutputportfield', ConfigurablePowerLevelOutputPortField)
Vue.component('configurable-temperatureinputportfield', ConfigurableTemperatureInputPortField)
Vue.component('configurable-humidityinputportfield', ConfigurableHumidityInputPortField)
Vue.component('configurable-wattageinputportfield', ConfigurableWattageInputPortField)
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
