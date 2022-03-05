<template>
  <div>
      <v-text-field v-model="text" :label="hint" :counter="counter" :error="error" :error-messages="errorMessages" :disabled="disabled" type="number"></v-text-field>
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

  props: ["id", "hint", "counter", "clazz", "disabled", "initialValue"],

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

    text(value) {
      this.$store.commit(UPDATE_SETTINGS_FIELD, { 
        clazz: this.clazz,
        name: this.id,
        value: value
      })
    }

  },

  mounted: function() {
    var settings = this.$store.state.settings[this.clazz]
    if (settings != null) {
      this.text = settings[this.id]
    } else {
      this.text = this.initialValue
    }
  }
};
</script>