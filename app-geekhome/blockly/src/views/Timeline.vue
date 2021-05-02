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
        <v-card-text :key="now">{{ formatTimeAgo(item.timestamp) }}</v-card-text>
      </v-card>
    </v-timeline-item>
  </v-timeline>
</template>

<script>
import { client } from "../rest.js";

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
    formatTimeAgo: function(timestamp) {

      var totalSeconds =  Math.round((this.now - timestamp)/1000);
      if (totalSeconds < 0) {
        return "---"
      } 

      if (totalSeconds < 60) {
       return this.$vuetify.lang.t("$vuetify.ago.s", totalSeconds);
      } 
      
      var totalMinutes = Math.round(totalSeconds / 60);
      
      if (totalMinutes < 60) {
        return this.$vuetify.lang.t("$vuetify.ago.m", totalMinutes);  
      }

      var totalHours = Math.round(totalSeconds / 3600);
      var minutesModulo = totalMinutes % 60;
      if (totalHours < 24) {
        return this.$vuetify.lang.t("$vuetify.ago.h", totalHours, minutesModulo);
      }

      var totalDays = Math.round(totalSeconds / 86400)
      var hoursModulo = totalHours % 24;

      return this.$vuetify.lang.t("$vuetify.ago.d", totalDays, hoursModulo)
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
