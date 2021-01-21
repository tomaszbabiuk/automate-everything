<template>
  <v-list>
      <v-list-item v-for="event in discoveryEvents" :key="event.no">
        <v-list-item-content>
          <v-list-item-title>{{event.message}}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
  </v-list>
</template>
<script>
import { client } from "../rest.js";
import { sseClient } from '../sse.js';

export default {
  watch: {
    $route() {
      this.refresh()
    },
  },
  computed: {
    discoveryEvents() {
      return this.$store.state.discoveryEvents.filter(element => { 
        return element.factoryId == this.$route.params.clazz
      })
    }
  },
  methods: {
    refresh: function() {
      this.events = []
      client.clearDiscoveryEvents()
      client.getDiscoveryEvents()
      console.log("refreshing: " + this.$route.params.clazz)
      this.filter = this.$route.params.clazz
    }
  },
  mounted: function() {
    this.refresh()
    sseClient.openDiscoveryEvents()
  },
  beforeDestroy() {
    sseClient.closeDiscoveryEvents()
  },
}
</script>