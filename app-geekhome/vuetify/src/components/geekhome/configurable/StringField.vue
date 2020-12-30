<template>
  <div>
      <v-text-field v-model="inputVal" :label="hint" :required="required" :counter="counter" :error="error" :error-messages="errorMessages"></v-text-field>
  </div>
</template>

<script>
import {UPDATE_INSTANCE} from '../../../plugins/vuex'

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
        this.$store.commit(UPDATE_INSTANCE, { 
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