<template>
  <v-row>
    <v-col sm="12" md="12" lg="7" xl="7"> Discovery content </v-col>
    <v-col sm="12" md="12" lg="5" xl="5">
      <v-card v-if="discoveryEvents.length>0">

        <v-list>
          <v-subheader
            ><v-badge inline color="blue" :content="discoveryEvents.length">
              DISCOVERY REPORT
            </v-badge>
          </v-subheader>
          <v-subheader>
            <v-btn color="primary">Restart discovery</v-btn>
          </v-subheader>
        </v-list>

        <v-divider></v-divider>

        <v-list>

          <v-list-item v-for="event in discoveryEvents" :key="event.no">
            <v-list-item-content>
              <v-list-item-title>{{ event.message }}</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list>
      </v-card>
    </v-col>
  </v-row>
</template>
<script>
import { client } from "../rest.js";
import { sseClient } from "../sse.js";

export default {
  watch: {
    $route() {
      this.refresh();
    },
  },
  computed: {
    discoveryEvents() {
      return this.$store.state.discoveryEvents.filter((element) => {
        return element.factoryId == this.$route.params.clazz;
      });
    },
  },
  methods: {
    refresh: function () {
      this.events = [];
      client.clearDiscoveryEvents();
      client.getDiscoveryEvents();
      console.log("refreshing: " + this.$route.params.clazz);
      this.filter = this.$route.params.clazz;
    },
  },
  mounted: function () {
    this.refresh();
    sseClient.openDiscoveryEvents();
  },
  beforeDestroy() {
    sseClient.closeDiscoveryEvents();
  },
};
</script>