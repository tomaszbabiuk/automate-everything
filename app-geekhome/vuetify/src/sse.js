import Vue from 'vue'
import store, { ADD_DISCOVERY_EVENT, UPDATE_PORT } from './plugins/vuex'
import { UPDATE_PLUGIN } from './plugins/vuex'

export const sseClient = {
  pluginsMsgServer: null,
  discoveryEventsMsgServer: null,

  openPluginEvents: function() {
    Vue.SSE('/rest/plugins/live', { format: 'json' })
      .then(sse => {
        this.pluginsMsgServer = sse;

        sse.onError(e => {
          console.error('lost connection; giving up!', e);
          sse.close();
        });

        sse.subscribe('pluginChange', (message) => {
          store.commit(UPDATE_PLUGIN, message)
        });
      })
      .catch(err => {
        console.error('Failed to connect to server', err);
      });
  },

  closePluginEvents: function() {
    if (this.pluginsMsgServer != null) {
      this.pluginsMsgServer.close();
    }
  },

  openDiscoveryEvents: function() {
    var openInternal = function(server) {
      console.log('reconnecting')

      Vue.SSE('/rest/adapterevents/live', { format: 'json' })
      .then(sse => {
        server = sse;

        sse.onError(e => {
          sse.close();
          console.info('lost connection; auto-retrying in 5 secs...', e);

          setTimeout(function() { openInternal(server) }, 5000)
        });

        sse.subscribe('discoveryEvent', (payload) => {
          store.commit(ADD_DISCOVERY_EVENT, payload)
        });

        sse.subscribe('portUpdateEvent', (payload) => {
          store.commit(UPDATE_PORT, payload)
        });
      })
      .catch(err => {
        console.info('Failed to connect to server; auto-retrying in 5 secs...', err);

        setTimeout(function() { openInternal(server) }, 5000)
      });
    }

    openInternal(this.discoveryEventsMsgServer)
  },

  closeDiscoveryEvents: function() {
    if (this.discoveryEventsMsgServer != null) {
      this.discoveryEventsMsgServer.close();
    }
  }
}