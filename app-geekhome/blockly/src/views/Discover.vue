<template>
  <v-row>
    <v-col sm="12" md="12" lg="7" xl="7">
      <v-data-table
        :headers="headers"
        :items="ports"
        item-key="id"
        class="elevation-1"
      >
        <template v-slot:top>
          <v-toolbar flat>
            <v-toolbar-title>{{
              $vuetify.lang.t("$vuetify.discover.ports")
            }}</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn
              right
              color="primary"
              dark
              class="mb-2"
              @click.stop="restartDiscovery()"
            >
              {{ $vuetify.lang.t("$vuetify.discover.restart_discovery") }}
            </v-btn>
          </v-toolbar>
        </template>
        <template v-slot:[`item.value`]="{ item }" }>
          <div v-if="item.canRead">
            {{ item.interfaceValue }}
          </div>
          <div v-else>
            {{ $vuetify.lang.t("$vuetify.common.nA") }}
          </div>
        </template>
        <template v-slot:[`item.control`]="{ item }" }>
          <div v-if="item.canWrite">
              <portcontrol :valueType="item.valueType" :portId="item.id"></portcontrol>
          </div>
          <div v-else>
            {{ $vuetify.lang.t("$vuetify.common.nA") }}
          </div>
        </template>
        <template v-slot:no-data>
          {{ $vuetify.lang.t("$vuetify.noDataText") }}
        </template>
      </v-data-table>
    </v-col>
    <v-col sm="12" md="12" lg="5" xl="5">
      <v-card v-if="discoveryEvents.length > 0">
        <v-list>
          <v-subheader
            ><v-badge inline color="blue" :content="discoveryEvents.length">
              DISCOVERY REPORT
            </v-badge>
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
import store from "../plugins/vuex";
import {
  CLEAR_DISCOVERY_EVENTS,
} from "../plugins/vuex";

export default {
  data: () => ({
    headers: [],
  }),
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
    ports() {
      return this.$store.state.ports.filter((element) => {
        return element.factoryId == this.$route.params.clazz;
      });
    },
  },
  methods: {
    restartDiscovery: async function () {
      var factoryId = this.$route.params.clazz;
      store.commit(CLEAR_DISCOVERY_EVENTS)
      await client.enablePlugin(factoryId, false)
      await client.enablePlugin(factoryId, true)
    },

    refresh: function () {
      this.events = [];
      client.clearDiscoveryEvents();
      client.getDiscoveryEvents();
      client.getPorts();
      this.filter = this.$route.params.clazz;
    },
  },
  mounted: function () {
    this.refresh();
    sseClient.openDiscoveryEvents();

    this.headers = [
      {
        text: this.$vuetify.lang.t("$vuetify.discover.id"),
        align: "start",
        sortable: false,
        value: "id",
      },
      {
        text: this.$vuetify.lang.t("$vuetify.discover.value"),
        value: "value",
        sortable: false,
      },
      {
        text: this.$vuetify.lang.t("$vuetify.discover.connected"),
        value: "connected",
        sortable: false,
      },
      {
        text: this.$vuetify.lang.t("$vuetify.discover.control"),
        value: "control",
        sortable: false,
        align: "end",
      },
    ];
  },
  beforeDestroy() {
    sseClient.closeDiscoveryEvents();
  },
};
</script>