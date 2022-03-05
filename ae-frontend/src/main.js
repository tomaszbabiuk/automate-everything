import Vue from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify'
import store from './plugins/vuex'

import ConfigurableForm from './components/ae/configurable/Form'
import ConfigurableStringField from './components/ae/configurable/StringField'
import ConfigurableBooleanField from './components/ae/configurable/BooleanField'
import ConfigurablePercentField from './components/ae/configurable/PercentField'
import ConfigurableDurationField from './components/ae/configurable/DurationField'
import ConfigurableBigDecimalField from './components/ae/configurable/BigDecimalField'
import ConfigurableTemperatureField from './components/ae/configurable/TemperatureField'
import ConfigurablePortReferenceField from './components/ae/configurable/PortReferenceField'
import ConfigurableInstanceReferenceField from './components/ae/configurable/InstanceReferenceField'
import ConfigurableSingleOptionEnumerationField from './components/ae/configurable/SingleOptionEnumerationField'
import ConfigurableIconSelector from './components/ae/configurable/IconSelector'
import ConfigurableTagsSelector from './components/ae/configurable/TagsSelector'
import ConfigurableBlockConfigurator from './components/ae/configurable/BlockConfigurator'

import SettingsForm from './components/ae/settings/Form'
import SettingsStringField from './components/ae/settings/StringField'
import SettingsPasswordStringField from './components/ae/settings/PasswordStringField'
import SettingsBigDecimalField from './components/ae/settings/BigDecimalField'
import SettingsBooleanField from './components/ae/settings/BooleanField'

import PortControl from './components/ae/PortControl'
import DeviceControl from './components/ae/DeviceControl'

import QrcodeVue from 'qrcode.vue'

//configurables
Vue.component('configurable-form', ConfigurableForm)
Vue.component('configurable-stringfield', ConfigurableStringField)
Vue.component('configurable-booleanfield', ConfigurableBooleanField)
Vue.component('configurable-percentfield', ConfigurablePercentField)
Vue.component('configurable-durationfield', ConfigurableDurationField)
Vue.component('configurable-bigdecimalfield', ConfigurableBigDecimalField)
Vue.component('configurable-temperaturefield', ConfigurableTemperatureField)
Vue.component('configurable-portreferencefield', ConfigurablePortReferenceField)
Vue.component('configurable-instancereferencefield', ConfigurableInstanceReferenceField)
Vue.component('configurable-singleoptionenumerationfield', ConfigurableSingleOptionEnumerationField)
Vue.component('configurable-iconselector', ConfigurableIconSelector)
Vue.component('configurable-tagsselector', ConfigurableTagsSelector)
Vue.component('configurable-blockconfigurator', ConfigurableBlockConfigurator)

//settings
Vue.component('settings-form', SettingsForm)
Vue.component('settings-stringfield', SettingsStringField)
Vue.component('settings-passwordstringfield', SettingsPasswordStringField)
Vue.component('settings-bigdecimalfield', SettingsBigDecimalField)
Vue.component('settings-booleanfield', SettingsBooleanField)

//geekhome
Vue.component('portcontrol', PortControl)
Vue.component('devicecontrol', DeviceControl)

//external
Vue.component('external-qrcode', QrcodeVue)

Vue.config.productionTip = false
//Add unimported components to ignore list to prevent warnings.
Vue.config.ignoredElements = ['field','block','category','xml','mutation','value','sep']

new Vue({
  store,
  router,
  vuetify,
  render: h => h(App)
}).$mount('#app')
