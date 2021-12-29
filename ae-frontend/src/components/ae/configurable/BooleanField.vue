<template>
  <div>
      <v-checkbox v-model="checked" :label="hint" :error="error" :error-messages="errorMessages"></v-checkbox>
  </div>
</template>

<script>
import {UPDATE_INSTANCE_FIELD} from '../../../plugins/vuex'

export default {
  data: function() {
    return {
      error: false,
      errorMessages: [],
      checked: false,
    };
  },

  props: ["id", "hint"],

  computed: {
    validation() {
      return this.$store.state.instanceValidation[this.id]
    },

    storeFieldData() {
      return this.$store.state.newInstance.fields[this.id]
    },  
  },

  watch: {
    validation(val) {
      this.error = !val.valid
      this.errorMessages = val.reasons
    },

    storeFieldData(value) {
      if (this.checked != value) {
        this.checked = value
      }
    },

    checked(value) {
      this.$store.commit(UPDATE_INSTANCE_FIELD, { 
        name: this.id,
        value: value ? "1" : "0"
      })
    }
  },

  mounted: function() {
    this.checked = this.storeFieldData == "1"
  }
};
</script>