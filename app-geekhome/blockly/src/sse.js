import store, { ADD_DISCOVERY_EVENT, UPDATE_PORT, SET_ERROR } from './plugins/vuex'
import { UPDATE_PLUGIN } from './plugins/vuex'
import vuetify from './plugins/vuetify'


import { EventSourcePolyfill } from 'event-source-polyfill';

export const lang = vuetify.framework.lang

export const sseClient = {
  liveMsgServer: null,

  handleError: function(innerException) {
    console.log('EventSource error: ', innerException);
    var errorData = {
      message: "$vuetify.rest.error",
      actionTitle: "$vuetify.common.refresh_page",
      actionCallback: () => location.href = location.href + "",
      innerException: innerException
    }

    store.commit(SET_ERROR, errorData)
  },
  
  openLiveEvents: function() {
    this.liveMsgServer = new EventSourcePolyfill('/rest/live', {
      headers: {
          'Accept-Language': lang.current
      }
    })

    this.liveMsgServer.addEventListener("DiscoveryEventDto", function(e) {
      console.log(e)
      var discoveryEventDto = JSON.parse(e.data)
      store.commit(ADD_DISCOVERY_EVENT, discoveryEventDto)
    })

    this.liveMsgServer.addEventListener("PortDto", function(e) {
      console.log(e)
      var portDto = JSON.parse(e.data)
      store.commit(UPDATE_PORT, portDto)
    })

    this.liveMsgServer.addEventListener("PluginDto", function(e) {
      console.log(e)
      var pluginDto = JSON.parse(e.data)
      store.commit(UPDATE_PLUGIN, pluginDto)
    })


    this.liveMsgServer.onerror = innerException => {
      this.handleError(innerException)
      this.liveMsgServer.close()
    };
  },

  closeLiveEvents: function() {
    if (this.liveMsgServer != null) {
      this.liveMsgServer.close();
    }
  },
}