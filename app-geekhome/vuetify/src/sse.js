import Vue from 'vue'
import store from './plugins/vuex'
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
    Vue.SSE('/rest/discoveryevents/live', { format: 'json' })
      .then(sse => {
        this.discoveryEventsMsgServer = sse;

        sse.onError(e => {
          console.error('lost connection; giving up!', e);
          sse.close();
        });

        sse.subscribe('*', (message) => {
          console.log(message)
          //store.commit(UPDATE_PLUGIN, message)
        });
      })
      .catch(err => {
        console.error('Failed to connect to server', err);
      });
  },

  closeDiscoveryEvents: function() {
    if (this.discoveryEventsMsgServer != null) {
      this.discoveryEventsMsgServer.close();
    }
  }
}