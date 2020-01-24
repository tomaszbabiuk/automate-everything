import Vue from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify'
import store from './plugins/vuex'

import ADCard from './components/admindashboard/Card.vue'
import ADStarsCard from'./components/admindashboard/StatsCard.vue'
import ADOffset from'./components/admindashboard/Offset.vue'

//material dashboard
Vue.component('material-card', ADCard)
Vue.component('material-stats-card', ADStarsCard)
Vue.component('helper-offset', ADOffset)

Vue.config.productionTip = false

new Vue({
  store,
  router,
  vuetify,
  render: h => h(App)
}).$mount('#app')
