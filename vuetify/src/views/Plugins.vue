<template>
  <div>
    <v-card flat class="pa-5 mb-5" v-for="plugin in plugins" :key="plugin.name">
      <v-layout row wrap justify-end>
        <v-flex md8 sm12>
          <div class="caption grey--text">Name</div>
          <div>{{plugin.name}}</div>
        </v-flex>
        <v-flex md3 sm8>
          <div class="caption grey--text">Version</div>
          <div>{{plugin.version}}</div>
        </v-flex>
        <v-flex md1 sm4>
          <v-chip v-if="plugin.enabled" outlined @click="disable" color="green">Enabled - click to disable</v-chip>
          <v-chip v-else outlined @click="enable" color="red">Disabled - click to enable</v-chip>
        </v-flex>
        <v-flex md12 sm12>
          <div class="caption grey--text">Description</div>
          <div class="text-justify">{{plugin.description}}</div>
        </v-flex>
      </v-layout>
    </v-card>
  </div>
</template>

<script>
import axios from 'axios'

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
  },
  mounted: function() {
    this.plugins = localStorage.plugins

    axios
      .get('http://localhost/rest/plugins')
      .then(response => {
        this.plugins = response.data
        localStorage.plugins = response
      })  
  }
};
</script>