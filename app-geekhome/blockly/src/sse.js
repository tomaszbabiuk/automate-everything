import store, { ADD_DISCOVERY_EVENT, UPDATE_PORT, SET_ERROR } from './plugins/vuex'
import { UPDATE_PLUGIN } from './plugins/vuex'
import vuetify from './plugins/vuetify'


import { EventSourcePolyfill } from 'event-source-polyfill';

export const lang = vuetify.framework.lang

export const sseClient = {
  pluginsMsgServer: null,
  discoveryEventsMsgServer: null,

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
  
  openPluginEvents: function() {
    this.pluginsMsgServer = new EventSourcePolyfill('/rest/plugins/live', {
      headers: {
          'Accept-Language': lang.current
      }
    })

    this.pluginsMsgServer.addEventListener("pluginChange", function(e) {
      store.commit(UPDATE_PLUGIN, JSON.parse(e.data))
    })

    this.pluginsMsgServer.onerror = innerException => {
      this.handleError(innerException)
      this.pluginsMsgServer.close()
    };
  },

  closePluginEvents: function() {
    if (this.pluginsMsgServer != null) {
      this.pluginsMsgServer.close();
    }
  },

  openDiscoveryEvents: function() {
    this.discoveryEventsMsgServer = new EventSourcePolyfill('/rest/adapterevents/live', {
      headers: {
          'Accept-Language': lang.current
      }
    });

    this.discoveryEventsMsgServer.addEventListener("DiscoveryEvent", function(e) {
      store.commit(ADD_DISCOVERY_EVENT, JSON.parse(e.data))
    })

    this.discoveryEventsMsgServer.addEventListener("PortUpdateEvent", function(e) {
      store.commit(UPDATE_PORT, JSON.parse(e.data))
    })
    
    this.discoveryEventsMsgServer.onerror = innerException => {
        this.handleError(innerException)
        this.discoveryEventsMsgServer.close()
    };
  },

  closeDiscoveryEvents: function() {
    if (this.discoveryEventsMsgServer != null) {
      this.discoveryEventsMsgServer.close();
    }
  }
}