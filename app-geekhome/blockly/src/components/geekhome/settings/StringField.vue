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
      return this.$store.state.settingsValidation[this.clazz]
    },
  },

  watch: {
    validation(value) {
      var validation = value[this.id]
      this.error = !validation.valid
      this.errorMessages = validation.reasons
    },

    settings(value) {
      console.log('store: ' + value)
      if (this.text != value) {
        this.text = value
      }
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