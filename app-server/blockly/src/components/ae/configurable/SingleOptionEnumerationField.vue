<template>
  <div>
        <v-select
          :items="items"
          v-model="selected" 
          item-text="text"
          item-value="id"
          :label="hint"
          :error="error" 
          :error-messages="errorMessages"
          :return-object="false"
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
      items: [],
    };
  },

  props: ["id", "hint", "required", "counter", "values"],

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
      if (this.selected != value) {
        this.selected = value
      }
    },

    selected(value) {
      this.$store.commit(UPDATE_INSTANCE_FIELD, { 
        name: this.id,
        value: value
      })
    }
  },

  mounted: function() {
    this.selected = this.storeFieldData
    for (var key in this.values) {
      this.items.push( {'id': key, 'text': this.values[key]})
    }
  }
};
</script>