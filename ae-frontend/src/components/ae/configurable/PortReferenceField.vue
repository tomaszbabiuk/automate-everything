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

  props: ["id", "hint", "required", "counter", "fieldRef"],

  computed: {
    validation() {
      return this.$store.state.instanceValidation[this.id]
    },

    storeFieldData() {
      return this.$store.state.newInstance.fields[this.id]
    },  

    ports() {
      var portFilter = {
        clazz: this.fieldRef.clazz,
        canRead: this.fieldRef.type === "Input" || this.fieldRef.type === "Output",
        canWrite: this.fieldRef.type === "Output",
        any: this.fieldRef.type === "Any"
      }

      return this.$store.state.ports.filter((port) => {
        var portMatchesCriteria = port.valueClazz === portFilter.clazz &&
                                  (port.canRead === portFilter.canRead || portFilter.any) &&
                                  (port.canWrite === portFilter.canWrite || portFilter.any)

        return portMatchesCriteria
      })
    }
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
  }
};
</script>