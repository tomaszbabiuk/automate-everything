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
import * as utils from "../utils.js"

export default {
  data: () => ({
    plugins: null
  }),
  methods: {
    enable() {
      alert("Enabling...");
    },
    disable() {
      alert("Disabling...");
    },
    init() {
      utils.axiosInstance
        .get("/rest/plugins")
        .then(response => {
          this.plugins = response.data;
          localStorage.plugins = response;
          this.$emit("clearError", null);
        })
        .catch(() => {
          var errorData = {
            message: "Problem loading plugins",
            actionTitle: this.$vuetify.lang.t("$vuetify.common.retry"),
            actionCallback: () => this.init()
          };
          this.$emit("error", errorData);
        });
    }
  },
  mounted: function() {
    this.init();
  }
};
</script>