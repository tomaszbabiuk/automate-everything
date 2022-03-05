<template>
  <div>
      <v-checkbox v-model="checked" :label="hint" :error="error" :error-messages="errorMessages" :disabled="disabled"></v-checkbox>
  </div>
</template>

<script>
import {UPDATE_SETTINGS_FIELD} from '../../../plugins/vuex'

export default {
  data: function() {
    return {
      error: false,
      errorMessages: [],
      checked: false,
    };
  },

  props: ["id", "hint", "counter", "disabled", "clazz", "initialValue"],

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

    checked(value) {
      this.$store.commit(UPDATE_SETTINGS_FIELD, { 
        clazz: this.clazz,
        name: this.id,
        value: value ? "1" : "0"
      })
    }
  },

  mounted: function() {
    var settings = this.$store.state.settings[this.clazz]
    if (settings != null) {
      this.checked = settings[this.id] == "1"
    } else {
      this.checked = this.initialValue == "1"
    }
  }
};
</script>