<template>
  <div>
    <v-card flat class="pa-5 mb-5" v-for="plugin in plugins" :key="plugin.name">
      <v-row>
        <v-col md="8" sm="12">
          <div class="caption grey--text">Name</div>
          <div>{{plugin.name}}</div>
        </v-col>
        <v-col md="1" sm="12">
          <div class="caption grey--text">Version</div>
          <div>{{plugin.version}}</div>
        </v-col>
        <v-col md="3" sm="12" align="right">
          <div class="caption grey--text">Enabled</div>
          <v-chip v-if="plugin.enabled" outlined @click="enable" color="green">Yes</v-chip>
          <v-chip v-else outlined @click="enable" color="red">No</v-chip>
        </v-col>
        <v-col md="12" sm="12">
          <div class="caption grey--text">Description</div>
          <div class="text-justify">{{plugin.description}}</div>
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<script>
import axios from "axios";

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
    }
  },
  mounted: function() {
    this.plugins = localStorage.plugins;

    axios.get("http://localhost/rest/plugins").then(response => {
      this.plugins = response.data;
      localStorage.plugins = response;
    });
  }
};
</script>