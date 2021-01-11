import Vue from 'vue'
import store from './plugins/vuex'
import { UPDATE_PLUGIN } from './plugins/vuex'

let msgServer;

export const sseClient = {
  open: function() {
    Vue.SSE('/rest/plugins/live', { format: 'json' })
      .then(sse => {
        msgServer = sse;

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

  close: function() {
    msgServer.close();
  }
}