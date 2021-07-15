<template>
  <v-timeline>
    <v-timeline-item v-for="(item, i) in automationHistory" :key="i" small>
      <template v-slot:opposite>
        <span :class="`headline font-weight-bold silver--text`" v-text="item.change"></span>
      </template>
      <v-card class="elevation-2">
        <v-card-title class="headline">
            <v-list-item-icon v-if="item.iconId === null">
              <v-icon x-large left>$vuetify.icon.robot</v-icon>
            </v-list-item-icon>
            <v-list-item-icon v-else>
              <img
                left
                :src="'/rest/icons/' + item.iconId + '/raw'"
                width="40"
                height="40"
              />
            </v-list-item-icon>
          {{ item.subject }}
        </v-card-title>
        <v-card-text :key="now">{{ formatTimeAgoProxy(item.timestamp) }}</v-card-text>
      </v-card>
    </v-timeline-item>
  </v-timeline>
</template>

<script>
import { client } from "../rest.js";
import { formatTimeAgo } from "../elapsed.js";

export default {
  data: () => ({
    now: 0
  }),

  computed: {
    automationHistory() {
      return this.$store.state.automationHistory;
    }
  },

  mounted: function() {
    client.getAutomationHistory();
  },

  methods: {
    formatTimeAgoProxy: function(timestamp) {
      return formatTimeAgo(this.now, timestamp)
    }
  },

  watch: {
    now: {
        handler() {
          setTimeout(() => {
              var now = (new Date).getTime()
              this.now = now;
              this.$forceUpdate();
          }, 1000);
        },
        immediate: true
    }
  }
};
</script>
