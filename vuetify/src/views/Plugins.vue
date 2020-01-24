<template>
  <div>
    <v-card flat class="pa-5 mb-5" v-for="plugin in plugins" :key="plugin.name">
      <v-row>
        <v-col md="8" sm="12">
          <div class="caption grey--text">{{$vuetify.lang.t('$vuetify.plugins_list.name')}}</div>
          <div>{{plugin.name}}</div>
        </v-col>
        <v-col md="1" sm="12">
          <div class="caption grey--text">{{$vuetify.lang.t('$vuetify.plugins_list.version')}}</div>
          <div>{{plugin.version}}</div>
        </v-col>
        <v-col md="3" sm="12" align="right" v-if="plugin.enabled">
          <div class="caption green--text">{{$vuetify.lang.t('$vuetify.plugins_list.enabled')}}</div>
          <v-chip outlined @click="disable">{{$vuetify.lang.t('$vuetify.plugins_list.disable')}}</v-chip>
        </v-col>
        <v-col md="3" sm="12" align="right" v-else>
          <div class="caption red--text">{{$vuetify.lang.t('$vuetify.plugins_list.disabled')}}</div>
          <v-chip outlined @click="enable">{{$vuetify.lang.t('$vuetify.plugins_list.enable')}}</v-chip>
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
import { client } from "../utils.js"

export default {
  methods: {
    enable() {
      alert("Enabling...");
    },
    disable() {
      alert("Disabling...");
    },
  },
  computed: {
    plugins () {
      return this.$store.state.plugins
    }
  },
  mounted: function() {
    client.getPlugins()
  }
};
</script>