<template>
  <div>
      <v-text-field v-model="inputVal" :label="hint" :required="required" :counter="counter"></v-text-field>
  </div>
</template>

<script>
import {UPDATE_INSTANCE} from '../../../plugins/vuex'

export default {
  props: ["initialValue", "id", "hint", "required", "counter"],
  computed: {
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
  
  }
};
</script>