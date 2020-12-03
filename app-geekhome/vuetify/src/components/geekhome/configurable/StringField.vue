<template>
  <div>
      <v-text-field v-model="inputVal" :label="hint" :required="required" :counter="counter"></v-text-field>
  </div>
</template>

<script>
import {UPDATE_INSTANCE} from '../../../plugins/vuex'

export default {
  props: ["initialValue", "instanceId", "hint", "required", "counter"],
  computed: {
    inputVal: {
      get() {
        return this.initialValue;
      },
      set(value) {
        this.$store.commit(UPDATE_INSTANCE, { 
          instanceId: this.instanceId, 
          value: value 
          })
      }
    }
  
  }
};
</script>