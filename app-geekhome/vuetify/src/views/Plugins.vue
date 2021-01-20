<template>
  <div>
    <v-card class="pa-3 mb-3" v-for="plugin in plugins" :key="plugin.name">
      <v-row>
        <v-col md="8" sm="12">
          <div class="caption grey--text">{{$vuetify.lang.t('$vuetify.plugins_list.name')}}</div>
          <div>{{plugin.name}}</div>
        </v-col>
        <v-col md="1" sm="2">
          <div class="caption grey--text">{{$vuetify.lang.t('$vuetify.plugins_list.version')}}</div>
          <div>{{plugin.version}}</div>
        </v-col>
        <v-col md="3" sm="10" align="right" v-if="plugin.enabled">
          <div class="caption green--text">{{$vuetify.lang.t('$vuetify.plugins_list.enabled')}}</div>
          <v-chip
            outlined
            @click="disable(plugin)"
          >{{$vuetify.lang.t('$vuetify.plugins_list.disable')}}</v-chip>
        </v-col>
        <v-col md="3" sm="12" align="right" v-else>
          <div class="caption red--text">{{$vuetify.lang.t('$vuetify.plugins_list.disabled')}}</div>
          <v-chip
            outlined
            @click="enable(plugin)"
          >{{$vuetify.lang.t('$vuetify.plugins_list.enable')}}</v-chip>
        </v-col>
        <v-col md="12" sm="12">
          <div class="caption grey--text">{{$vuetify.lang.t('$vuetify.plugins_list.description')}}</div>
          <div class="text-justify">{{plugin.description}}</div>
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<script>
import { client } from "../rest.js";
import { sseClient } from "../sse.js";

export default {
  methods: {
    enable(plugin) {
      client.enablePlugin(plugin.id, true);
    },
    disable(plugin) {
      client.enablePlugin(plugin.id, false);
    }
  },
  computed: {
    plugins() {
      return this.$store.state.plugins;
    }
  },
  mounted: function() {
    client.getPlugins();

    sseClient.openPluginEvents()
  },
  beforeDestroy() {
    sseClient.closePluginEvents()
  },
};
</script>