<template>
  <div>
      <v-slider v-model="powerLevel" :label="hint" :counter="counter" :error="error" :error-messages="errorMessages" max="100" min="0"
       thumb-label></v-slider>
  </div>
</template>

<script>
import {UPDATE_INSTANCE_FIELD} from '../../../plugins/vuex'

export default {
  data: function() {
    return {
      error: false,
      errorMessages: [],
      powerLevel: 0,
    };
  },

  props: ["id", "hint", "counter"],

  computed: {
    validation() {
      return this.$store.state.instanceValidation[this.id]
    },

    storeFieldData() {
      return this.$store.state.newInstance.fields[this.id]
    },  
  },

  watch: {
    powerLevel(val) {
      console.log('value: ' + val)
      this.$store.commit(UPDATE_INSTANCE_FIELD, { 
        name: this.id,
        value: val
      })
    },

    validation(val) {
      this.error = !val.valid
      this.errorMessages = val.reasons
    },

    storeFieldData(value) {
      if (this.powerLevel != value) {
        this.powerLevel = value
      }
    },

    text(value) {
      console.log('text: ' + value)
      this.$store.commit(UPDATE_INSTANCE_FIELD, { 
        name: this.id,
        value: value
      })
    },

    mounted: function() {
      this.powerLevel = this.storeFieldData
    }
  }
};
</script>