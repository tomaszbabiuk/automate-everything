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
        <v-col md="3" sm="10" align="right">
        <v-switch
          class="float-right"
          inset
          v-model="plugin.enabled"
          @click="submitState(plugin)"
        >{{$vuetify.lang.t('$vuetify.plugins_list.disable')}}</v-switch>
        </v-col>
        <v-col md="10" sm="10">
          <div class="caption grey--text">{{$vuetify.lang.t('$vuetify.plugins_list.description')}}</div>
          <div class="text-justify">{{plugin.description}}</div>
        </v-col>
        <v-col md="2" sm="2">
          <v-btn v-if="plugin.settingGroups.length > 0"
            class="float-right"
            outlined
            color="primary"
            @click="openPluginSettings(plugin)"
          >{{$vuetify.lang.t('$vuetify.plugins_list.settings')}}</v-btn>
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<script>
import { client } from "../rest.js";

export default {
  methods: {
    submitState(plugin) {
      if (plugin.enabled) {
        client.enablePlugin(plugin.id, true);
      } else {
        client.enablePlugin(plugin.id, false);
      }
    },

    openPluginSettings: function (plugin) {
      this.$router.push({
        name: "pluginSettings",
        params: { id: plugin.id },
      });
    },
  },
  computed: {
    plugins() {
      return this.$store.state.plugins;
    }
  },
  mounted: function() {
    client.getPlugins();
  },
};
</script>