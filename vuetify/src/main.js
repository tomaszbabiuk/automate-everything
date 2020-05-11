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

//material dashboard
Vue.component('material-card', ADCard)
Vue.component('material-stats-card', ADStarsCard)
Vue.component('helper-offset', ADOffset)

//geekhome
Vue.component('configurable-form', ConfigurableForm)
Vue.component('configurable-stringfield', ConfigurableStringField)

Vue.config.productionTip = false

new Vue({
  store,
  router,
  vuetify,
  render: h => h(App)
}).$mount('#app')
