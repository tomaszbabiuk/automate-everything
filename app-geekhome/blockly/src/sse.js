import store, { 
  ADD_DISCOVERY_EVENT, UPDATE_PORT, SET_ERROR, ADD_INBOX_MESSAGE, SET_INBOX_UNREAD_COUNT,
  UPDATE_PLUGIN, UPDATE_AUTOMATION_UNIT,
  ADD_AUTOMATION_HISTORY } from './plugins/vuex'

import vuetify from './plugins/vuetify'


import { EventSourcePolyfill } from 'event-source-polyfill';

export const lang = vuetify.framework.lang

function createSseClient() {
  return {
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
  
      this.liveMsgServer.addEventListener("AutomationUnitDto", function(e) {
        console.log(e)
        var automationUnitDto = JSON.parse(e.data)
        store.commit(UPDATE_AUTOMATION_UNIT, automationUnitDto)
      })
  
      this.liveMsgServer.addEventListener("AutomationHistoryDto", function(e) {
        console.log(e)
        var automationHistoryDto = JSON.parse(e.data)
        store.commit(ADD_AUTOMATION_HISTORY, automationHistoryDto)
      })

      this.liveMsgServer.addEventListener("HeartbeatDto", function(e) {
        console.log(e)
        var heartbeatDto = JSON.parse(e.data)
        store.commit(SET_INBOX_UNREAD_COUNT, heartbeatDto.inboxUnreadCount)
      })

      this.liveMsgServer.addEventListener("InboxMessageDto", function(e) {
        console.log(e)
        var inboxMessageDto = JSON.parse(e.data)
        store.commit(ADD_INBOX_MESSAGE, inboxMessageDto)
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
}

export const sseClient = createSseClient()