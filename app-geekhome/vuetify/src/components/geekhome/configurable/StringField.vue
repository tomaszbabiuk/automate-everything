<template>
  <div>
      <v-text-field v-model="inputVal" :label="hint" :counter="counter" :error="error" :error-messages="errorMessages"></v-text-field>
  </div>
</template>

<script>
import {UPDATE_INSTANCE_FIELD} from '../../../plugins/vuex'

export default {
data: function() {
    return {
      error: false,
      errorMessages: []
    };
  },
  props: ["initialValue", "id", "hint", "required", "counter"],
  computed: {
    validation() {
      return this.$store.state.instanceValidation[this.id]
    },
    inputVal: {
      get() {
        return this.initialValue;
      },
      set(value) {
        this.$store.commit(UPDATE_INSTANCE_FIELD, { 
          name: this.id,
          value: value
          })
      }
    }
  
  },
  watch: {
    validation(val) {
      this.error = !val.valid
      this.errorMessages = val.reasons
    }
  }
};
</script>