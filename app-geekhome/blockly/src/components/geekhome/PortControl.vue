<template>
  <div>
    <v-btn v-if="valueType == 'Relay'" @click="togglePort()">Toggle</v-btn>
    <v-slider v-model="powerLevel" v-if="valueType == 'PowerLevel'" hint="Changing" max="100" min="0" thumb-label @click="controlPowerLevel()"></v-slider>
  </div>
</template>
<script>
import { client } from "../../rest.js";

export default {
  data: function () {
    return {
      powerLevel: 0,
      powerLevelThrottlingTimeout: null,
    };
  },
  props: ["valueType", "portId"],
  computed: {
    port: function() {
      return this.$store.state.ports.filter(element => {
        return element.id === this.portId
      })
    }
  },
  watch: {
    powerLevel() {
      clearTimeout(this.powerLevelThrottlingTimeout)
      this.powerLevelThrottlingTimeout = setTimeout(this.controlPowerLevel, 200)
    }
  },
  methods: {
    togglePort: function() {
      this.$store.state.ports.forEach(element => {
        if (element.id === this.portId) {
          var targetValue = 0
          if (element.integerValue === 0) {
            targetValue = 1
          }
          client.controlPort(this.portId, targetValue)
        }
      })
    },

    controlPowerLevel: function() {
      console.log("Power level" + this.powerLevel)
      this.$store.state.ports.forEach(element => {
        if (element.id === this.portId) {
          client.controlPort(this.portId, this.powerLevel)
        }
      })
    }
  }
};
</script>