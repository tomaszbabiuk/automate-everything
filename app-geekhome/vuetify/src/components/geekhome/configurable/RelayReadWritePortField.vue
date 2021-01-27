<template>
  <div>
        <v-select
          :items="ports"
          v-model="selected" 
          item-text="id"
          item-value="id"
          :label="hint"
          :error="error" 
          :error-messages="errorMessages"
          clearable
          dense
        ></v-select>
  </div>
</template>

<script>
import {UPDATE_INSTANCE_FIELD} from '../../../plugins/vuex'

export default {
data: function() {
    return {
      error: false,
      errorMessages: [],
      selected: '',
      items: []
    };
  },
  props: ["id", "hint", "required", "counter"],
  computed: {
    validation() {
      return this.$store.state.instanceValidation[this.id]
    },
    storeFieldData() {
      return this.$store.state.newInstance.fields[this.id]
    },  
    ports() {
      return this.$store.state.ports.filter((port) => {
        return port.canWrite === true && port.valueType === 'Relay'
      })
    }
  },
  watch: {
    validation(val) {
      this.error = !val.valid
      this.errorMessages = val.reasons
    },
    storeFieldData(value) {
      console.log('store: ' + value)
      if (this.selected != value) {
        this.selected = value
      }
    },
    selected(value) {
      console.log('text: ' + value)
      this.$store.commit(UPDATE_INSTANCE_FIELD, { 
        name: this.id,
        value: value
      })
    }
  }
};
</script>