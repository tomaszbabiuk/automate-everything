<template>
  <div>
      <v-text-field v-model="text" :label="hint" :counter="counter" :error="error" :error-messages="errorMessages" type="number"></v-text-field>
  </div>
</template>

<script>
import {UPDATE_INSTANCE_FIELD} from '../../../plugins/vuex'

export default {
data: function() {
    return {
      error: false,
      errorMessages: [],
      text: '',
    };
  },
  props: ["initialValue", "id", "hint", "required", "counter"],
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
      console.log('store: ' + value)
      if (this.text != value) {
        this.text = value
      }
    },
    text(value) {
      console.log('text: ' + value)
      this.$store.commit(UPDATE_INSTANCE_FIELD, { 
        name: this.id,
        value: value
      })
    }
  }
};
</script>