<template>
  <div>
    <div v-if="factories.length == 0" class="text-center">
      {{ $vuetify.lang.t("$vuetify.noDataText") }}
    </div>
    <div v-else>
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
                <portcontrol
                  :valueType="item.valueType"
                  :portId="item.id"
                  :disabled="!item.connected"
                ></portcontrol>
              </div>
              <div v-else>
                {{ $vuetify.lang.t("$vuetify.common.nA") }}
              </div>
            </template>
            <template v-slot:[`item.connected`]="{ item }" }>
              <v-checkbox
                :input-value="item.connected"
                value
                disabled
              ></v-checkbox>
            </template>
            <template v-slot:[`item.actions`]="{ item }" }>
              <v-icon @click="openDeletePortDialog(item)">mdi-delete</v-icon>
            </template>
            <template v-slot:no-data>
              {{ $vuetify.lang.t("$vuetify.noDataText") }}
            </template>
          </v-data-table>
        </v-col>
        <v-col sm="12" md="12" lg="5" xl="5">
          <v-skeleton-loader v-if="loading" type="card"></v-skeleton-loader>
          <v-card v-if="discoveryEvents.length > 0">
            <v-list>
              <v-subheader>
                <v-badge inline color="blue" :content="discoveryEvents.length">
                  {{ $vuetify.lang.t("$vuetify.discover.report_header") }}
                </v-badge>
              </v-subheader>
            </v-list>
            <v-divider></v-divider>
            <v-list>
              <v-list-item v-for="event in discoveryEvents" :key="event.no">
                <v-list-item-content>
                  <v-list-item-title class="text-wrap">{{ event.message }}</v-list-item-title>
                </v-list-item-content>
              </v-list-item>
            </v-list>
          </v-card>
        </v-col>
      </v-row>
      <v-dialog v-model="deletePortDialog.show" max-width="500px">
        <v-card>
          <v-card-title class="headline">{{
            $vuetify.lang.t("$vuetify.discover.delete_port_question")
          }}</v-card-title>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
              color="blue darken-1"
              text
              @click="closeDeletePortDialog()"
              >{{ $vuetify.lang.t("$vuetify.common.cancel") }}</v-btn
            >
            <v-btn
              color="blue darken-1"
              text
              @click="deletePort(deletePortDialog.portId)"
              >{{ $vuetify.lang.t("$vuetify.common.ok") }}</v-btn
            >
            <v-spacer></v-spacer>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
  </div>
</template>
<script>
import { client } from "../rest.js";
import store from "../plugins/vuex";
import { CLEAR_DISCOVERY_EVENTS } from "../plugins/vuex";

export default {
  data: () => ({
    headers: [],
    loading: true,
    deletePortDialog: {
      portId: null,
      show: false,
    },
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

    factories() {
      return this.$store.state.plugins.filter((element) => {
        return element.enabled && element.isHardwareFactory;
      });
    },
  },

  methods: {
    restartDiscovery: async function () {
      var factoryId = this.$route.params.clazz;
      store.commit(CLEAR_DISCOVERY_EVENTS);
      await client.discoverHardwareAdapters(factoryId);
    },

    refresh: async function () {
      this.loading = true;
      this.events = [];
      client.clearDiscoveryEvents();

      var that = this;
      await Promise.all([client.getDiscoveryEvents(), client.getPorts()]).then(
        function () {
          that.loading = false;
        }
      );

      this.filter = this.$route.params.clazz;
    },

    openDeletePortDialog: function (portDto) {
      this.deletePortDialog = {
        portId: portDto.id,
        show: true,
      };
    },

    closeDeletePortDialog: function () {
      this.deletePortDialog.show = false;
    },

    deletePort: function (portId) {
      client.deletePort(portId);
      this.closeDeletePortDialog();
    },
  },

  mounted: function () {
    this.refresh();

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
      {
        text: this.$vuetify.lang.t("$vuetify.discover.actions"),
        value: "actions",
        sortable: false,
        align: "end",
      },
    ];
  },
};
</script>