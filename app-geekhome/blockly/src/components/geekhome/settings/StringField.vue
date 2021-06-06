<template>
  <div>
      <v-text-field v-model="text" :label="hint" :counter="counter" :error="error" :error-messages="errorMessages"></v-text-field>
  </div>
</template>

<script>
import {UPDATE_SETTINGS_FIELD} from '../../../plugins/vuex'

export default {
data: function() {
    return {
      error: false,
      errorMessages: [],
      text: '',
    };
  },
  props: ["id", "hint", "counter", "clazz"],
  computed: {
    validation() {
      return this.$store.state.settingsValidation[this.id]
    },
    settings() {
      return this.$store.state.settings;
    },  
  },
  watch: {
    validation(val) {
      this.error = !val.valid
      this.errorMessages = val.reasons
    },
    settings(val) {
      console.log('store: ' + val)
      
      // if (this.text != value) {
      //   this.text = value
      // }
    },
    text(value) {
      console.log('text: ' + value)
      this.$store.commit(UPDATE_SETTINGS_FIELD, { 
        clazz: this.clazz,
        name: this.id,
        value: value
      })
    }
  }
};
</script>